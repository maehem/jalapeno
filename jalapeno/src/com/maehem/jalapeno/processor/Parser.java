/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor;

import com.maehem.jalapeno.processor.Token.TokenType;
import com.maehem.jalapeno.processor.operation.expression.EmptyExpression;
import com.maehem.jalapeno.processor.operation.expression.Expression;
import com.maehem.jalapeno.processor.operation.expression.FunctionCallExpression;
import com.maehem.jalapeno.processor.operation.expression.OperatorExpression;
import com.maehem.jalapeno.processor.operation.expression.VariableExpression;
import com.maehem.jalapeno.processor.operation.expression.value.NumberValue;
import com.maehem.jalapeno.processor.operation.expression.value.StringValue;
import com.maehem.jalapeno.processor.statement.AssignStatement;
import com.maehem.jalapeno.processor.statement.EndStatement;
import com.maehem.jalapeno.processor.statement.FunctionCallStatement;
import com.maehem.jalapeno.processor.statement.FunctionStatement;
import com.maehem.jalapeno.processor.statement.GotoStatement;
import com.maehem.jalapeno.processor.statement.IfThenStatement;
import com.maehem.jalapeno.processor.statement.InputStatement;
import com.maehem.jalapeno.processor.statement.PrintStatement;
import com.maehem.jalapeno.processor.statement.ReturnStatement;
import com.maehem.jalapeno.processor.statement.Returnable;
import com.maehem.jalapeno.processor.statement.Statement;
import com.maehem.jalapeno.processor.statement.WhileStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author mark
 */
/**
 * This defines the Jasic parser. The parser takes in a sequence of tokens and
 * generates an abstract syntax tree. This is the nested data structure that
 * represents the series of statements, and the expressions (which can nest
 * arbitrarily deeply) that they evaluate. In technical terms, what we have is a
 * recursive descent parser, the simplest kind to hand-write.
 *
 * As a side-effect, this phase also stores off the line numbers for each label
 * in the program. It's a bit gross, but it works.
 */
public class Parser {

    private final List<Token> tokens;
    private int position;
    
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        position = 0;
    }

    /**
     * The top-level function to start parsing. This will keep consuming tokens
     * and routing to the other parse functions for the different grammar syntax
     * until we run out of code to parse.
     *
     * @param labels A map of label names to statement indexes. The parser will
     * fill this in as it scans the code.
     * @return The list of parsed statements.
     */
    public List<Statement> parse(LinkedHashMap<String, Integer> labels) {
        List<Statement> statements = new ArrayList<>();
        Stack beginnings = new Stack();  // Track things that use EndStatement

        while (true) {
            // Ignore empty lines.
            while (match(TokenType.LINE)) {
            }

            if (match(TokenType.LABEL)) {
                // Mark the index of the statement after the label.
                labels.put(last(1).text, statements.size());
            } else if (match(TokenType.WORD, TokenType.EQUALS)) {
                String name = last(2).text;
                Expression value = expression();
                statements.add(new AssignStatement(name, value));
            } else if (match("end")) {
                // find the last label we pushed onto the Labels List.                
                //List<Map.Entry<String, Integer>> arrayList = new ArrayList<Map.Entry<String,Integer>>(labels.entrySet());
                //Map.Entry<String, Integer> label = arrayList.get(arrayList.size()-1);
                Returnable begin = (Returnable) beginnings.pop();
                statements.add(new EndStatement(begin));                
            } else if (match("print")) {
                statements.add(new PrintStatement(expression()));
            } else if (match("input")) {
                statements.add(new InputStatement(
                        consume(TokenType.WORD).text));
            } else if (match("goto")) {  // Depricated
                statements.add(new GotoStatement(
                        consume(TokenType.WORD).text));
            } else if (match("if")) {
                Expression condition = expression();
                consume("then");
                IfThenStatement s = new IfThenStatement(condition);
                statements.add(s);
                beginnings.push(s);
            } else if (match("return")) {
                Returnable begin = (Returnable) beginnings.peek();
                statements.add(new ReturnStatement(expression(), begin));
            } else if (match("while")) {
                Expression condition = expression();
                //consume("then");
                //String label = consume(TokenType.WORD).text;
                WhileStatement s = new WhileStatement(condition);
                statements.add(s);
                beginnings.push(s);
            } else if (match(TokenType.WORD, TokenType.LEFT_PAREN)) {  // Function call
                //System.out.println("I found a function! " + last(2).text);
                String name = last(2).text;
                position--;
                Expression operator = operator();
                //System.out.println("Consumed Function Call " + name + "( " + operator.toString() + " )");
                statements.add(new FunctionCallStatement(name, operator));
            } else if (match(TokenType.OPERATOR, TokenType.OPERATOR)) {
                
            } else if (match("function")) {
                String label = consume(TokenType.WORD).text;
                labels.put(label, statements.size());
                Expression operator = operator();
                FunctionStatement s = new FunctionStatement(label, operator /* , args */);
                statements.add(s);
                beginnings.push(s);
            } else {
                if ( position < tokens.size() ) {
                    System.out.println("I don't know what this is: " + get(0).text + " position=" + position);
                } else {
                    System.out.println("EOF Reached.");
                }
                break; // Unexpected token (likely EOF), so end.
            }
        }
        
        return statements;
    }

    // The following functions each represent one grammatical part of the
    // language. If this parsed English, these functions would be named like
    // noun() and verb().
    /**
     * Parses a single expression. Recursive descent parsers start with the
     * lowest-precedent term and moves towards higher precedence. For Jasic,
     * binary operators (+, -, etc.) are the lowest.
     *
     * @return The parsed expression.
     */
    private Expression expression() {
        return operator();
    }

    /**
     * Parses a series of binary operator expressions into a single expression.
     * In Jasic, all operators have the same predecence and associate
     * left-to-right. That means it will interpret: 1 + 2 * 3 - 4 / 5 like:
     * ((((1 + 2) * 3) - 4) / 5)
     *
     * It works by building the expression tree one at a time. So, given this
     * code: 1 + 2 * 3, this will:
     *
     * 1. Parse (1) as an atomic expression. 2. See the (+) and start a new
     * operator expression. 3. Parse (2) as an atomic expression. 4. Build a (1
     * + 2) expression and replace (1) with it. 5. See the (*) and start a new
     * operator expression. 6. Parse (3) as an atomic expression. 7. Build a ((1
     * + 2) * 3) expression and replace (1 + 2) with it. 8. Return the last
     * expression built.
     *
     * @return The parsed expression.
     */
    private Expression operator() {
        Expression expression = atomic();

        // Keep building operator expressions as long as we have operators.
        while (match(TokenType.OPERATOR )|| match(TokenType.COMMA)
                || match(TokenType.EQUALS)) {
            char operator = last(1).text.charAt(0);
            Expression right = atomic();
            expression = new OperatorExpression(expression, operator, right);
        }

        return expression;
    }

    /**
     * Parses an "atomic" expression. This is the highest level of precedence
     * and contains single literal tokens like 123 and "foo", as well as
     * parenthesized expressions.
     *
     * @return The parsed expression.
     */
    private Expression atomic() {
        if (match(TokenType.WORD, TokenType.LEFT_PAREN)) {
            // A word and paren is a reference to a function call.
            if ( match(TokenType.RIGHT_PAREN) ) {
                // Empty Expression
                return new FunctionCallExpression(last(1).text);
            }
            FunctionCallExpression exp = new FunctionCallExpression(last(2).text, expression());
            consume(TokenType.RIGHT_PAREN);
            
            return exp;
        } else if (match(TokenType.WORD)) {
            // A word is a reference to a variable.
            return new VariableExpression(last(1).text);
        } else if (match(TokenType.NUMBER)) {
            return new NumberValue(Double.parseDouble(last(1).text));
        } else if (match(TokenType.STRING)) {
            return new StringValue(last(1).text);
        } else if (match(TokenType.LEFT_PAREN)) {
            // The contents of a parenthesized expression can be any
            // expression. This lets us "restart" the precedence cascade
            // so that you can have a lower precedence expression inside
            // the parentheses.
            if ( match(TokenType.RIGHT_PAREN) ) {
                // Empty Expression
                return new EmptyExpression();
            }
            Expression expression = expression();
            consume(TokenType.RIGHT_PAREN);
            return expression;
        }
//        else if ( match(TokenType.RIGHT_PAREN)) {
//            // Empty Expression
//            return expression();
//        }
        throw new Error("Couldn't parse :(");
    }

    // The following functions are the core low-level operations that the
    // grammar parser is built in terms of. They match and consume tokens in
    // the token stream.
    /**
     * Consumes the next two tokens if they are the given type (in order).
     * Consumes no tokens if either check fails.
     *
     * @param type1 Expected type of the next token.
     * @param type2 Expected type of the subsequent token.
     * @return True if tokens were consumed.
     */
    private boolean match(TokenType type1, TokenType type2) {
        if (get(0).type != type1) {
            return false;
        }
        if (get(1).type != type2) {
            return false;
        }
        position += 2;
        return true;
    }

    /**
     * Consumes the next token if it's the given type.
     *
     * @param type Expected type of the next token.
     * @return True if the token was consumed.
     */
    private boolean match(TokenType type) {
        if (get(0).type != type) {
            return false;
        }
        position++;
        return true;
    }

    /**
     * Consumes the next token if it's a word token with the given name.
     *
     * @param name Expected name of the next word token.
     * @return True if the token was consumed.
     */
    private boolean match(String name) {
        if (get(0).type != TokenType.WORD) {
            return false;
        }
        if (!get(0).text.equalsIgnoreCase(name)) {
            return false;
        }
        position++;
        return true;
    }

    /**
     * Consumes the next token if it's the given type. If not, throws an
     * exception. This is for cases where the parser demands a token of a
     * certain type in a certain position, for example a matching ) after an
     * opening (.
     *
     * @param type Expected type of the next token.
     * @return The consumed token.
     */
    private Token consume(TokenType type) {
        if (get(0).type != type) {
            throw new Error("Expected " + type + ".");
        }
        return tokens.get(position++);
    }

    /**
     * Consumes the next token if it's a word with the given name. If not,
     * throws an exception.
     *
     * @param name Expected name of the next word token.
     * @return The consumed token.
     */
    private Token consume(String name) {
        if (!match(name) ) {
            throw new Error("Expected " + name + ".");
        }
        return last(1);
    }

    /**
     * Gets a previously consumed token, indexing backwards. last(1) will be the
     * token just consumed, last(2) the one before that, etc.
     *
     * @param offset How far back in the token stream to look.
     * @return The consumed token.
     */
    private Token last(int offset) {
        return tokens.get(position - offset);
    }

    /**
     * Gets an unconsumed token, indexing forward. get(0) will be the next token
     * to be consumed, get(1) the one after that, etc.
     *
     * @param offset How far forward in the token stream to look.
     * @return The yet-to-be-consumed token.
     */
    private Token get(int offset) {
        if (position + offset >= tokens.size()) {
            return new Token("", TokenType.EOF);
        }
        return tokens.get(position + offset);
    }

}
