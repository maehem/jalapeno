/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor;

import static com.maehem.jalapeno.processor.Token.TokenType.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
/**
 * This is a single meaningful chunk of code. It is created by the tokenizer and
 * consumed by the parser.
 */
public class Token {

    public final String text;
    public final TokenType type;

    /**
     * This defines the different kinds of tokens or meaningful chunks of code
     * that the parser knows how to consume. These let us distinguish, for
     * example, between a string "foo" and a variable named "foo".
     *
     * HACK: A typical tokenizer would actually have unique token types for each
     * keyword (print, goto, etc.) so that the parser doesn't have to look at
     * the names, but Jasic is a little more crude.
     */
    public enum TokenType {
        WORD, NUMBER, STRING, LABEL, LINE,
        EQUALS, OPERATOR, LEFT_PAREN, RIGHT_PAREN, EOF, COMMA
    }

    /**
     * This defines the different states the tokenizer can be in while it's
     * scanning through the source code. Tokenizers are state machines, which
     * means the only data they need to store is where they are in the source
     * code and this one "state" or mode value.
     *
     * One of the main differences between tokenizing and parsing is this
     * regularity. Because the tokenizer stores only this one state value, it
     * can't handle nesting (which would require also storing a number to
     * identify how deeply nested you are). The parser is able to handle that.
     */
    public enum TokenizeState {
        DEFAULT, WORD, NUMBER, STRING, COMMENT
    }

    public Token(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }

    /**
     * This function takes a script as a string of characters and chunks it into
     * a sequence of tokens. Each token is a meaningful unit of program, like a
     * variable name, a number, a string, or an operator.
     */
    public static List<Token> tokenize(String source) {
        List<Token> tokens = new ArrayList<>();

        String token = "";
        TokenizeState state = TokenizeState.DEFAULT;

        // Many tokens are a single character, like operators and ().
        String charTokens = "\n=+-*/<>(),";
        TokenType[] tokenTypes = {LINE, EQUALS,
            OPERATOR, OPERATOR, OPERATOR,
            OPERATOR, OPERATOR, OPERATOR,
            LEFT_PAREN, RIGHT_PAREN,COMMA
        };

        // Scan through the code one character at a time, building up the list
        // of tokens.
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (state) {
                case DEFAULT:
                    // Our comment is two -- tokens so we need to check before
                    // assuming its a minus sign
                    if ( c == '-' && source.charAt(i+1 ) == '-') {
                        state = TokenizeState.COMMENT;
                    } else if (charTokens.indexOf(c) != -1) {
                        tokens.add(new Token(Character.toString(c),
                                tokenTypes[charTokens.indexOf(c)]));
                    } else if (Character.isLetter(c) || c=='_') { // We support underbars as WORD start.
                        token += c;
                        state = TokenizeState.WORD;
                    } else if (Character.isDigit(c)) {
                        token += c;
                        state = TokenizeState.NUMBER;
                    } else if (c == '"') {
                        state = TokenizeState.STRING;
                    } else if (c == '\'') {
                        state = TokenizeState.COMMENT;  // TODO Depricated.  Delete me.
                    }
                    break;

                case WORD:
                    if (Character.isLetterOrDigit(c) || c=='_' ) { // We allow underbars
                        token += c;
                    } else if (c == ':') {
                        tokens.add(new Token(token, TokenType.LABEL));
                        token = "";
                        state = TokenizeState.DEFAULT;
                    } else {
                        tokens.add(new Token(token, TokenType.WORD));
                        token = "";
                        state = TokenizeState.DEFAULT;
                        i--; // Reprocess this character in the default state.
                    }
                    break;

                case NUMBER:
                    // HACK: Negative numbers and floating points aren't supported.
                    // To get a negative number, just do 0 - <your number>.
                    // To get a floating point, divide.
                    if (Character.isDigit(c)) {
                        token += c;
                    } else {
                        tokens.add(new Token(token, TokenType.NUMBER));
                        token = "";
                        state = TokenizeState.DEFAULT;
                        i--; // Reprocess this character in the default state.
                    }
                    break;

                case STRING:
                    if (c == '"') {
                        tokens.add(new Token(token, TokenType.STRING));
                        token = "";
                        state = TokenizeState.DEFAULT;
                    } else {
                        token += c;
                    }
                    break;

                case COMMENT:
                    if (c == '\n') {
                        state = TokenizeState.DEFAULT;
                    }
                    break;
            }
        }

        // HACK: Silently ignore any in-progress token when we run out of
        // characters. This means that, for example, if a script has a string
        // that's missing the closing ", it will just ditch it.
        return tokens;
    }
}
