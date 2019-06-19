/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
/**
 * This defines a single class that contains an entire interpreter for a
 * language very similar to the original BASIC. Everything is here (albeit in
 * very simplified form): tokenizing, parsing, and interpretation. The file is
 * organized in phases, with each appearing roughly in the order that they occur
 * when a program is run. You should be able to read this top-down to walk
 * through the entire process of loading and running a program.
 *
 * Jasic language syntax ---------------------
 *
 * Comments start with ' and proceed to the end of the line:
 *
 * print "hi there" ' this is a comment
 *
 * Numbers and strings are supported. Strings should be in "double quotes", and
 * only positive integers can be parsed (though numbers are double internally).
 *
 * Variables are identified by name which must start with a letter and can
 * contain letters or numbers. Case is significant for names and keywords.
 *
 * Each statement is on its own line. Optionally, a line may have a label before
 * the statement. A label is a name that ends with a colon:
 *
 * foo:
 *
 *
 * The following statements are supported:
 *
 * <name> = <expression>
 * Evaluates the expression and assigns the result to the given named variable.
 * All variables are globally scoped.
 *
 * pi = (314159 / 10000)
 *
 * print <expression>
 * Evaluates the expression and prints the result.
 *
 * print "hello, " + "world"
 *
 * input <name>
 * Reads in a line of input from the user and stores it in the variable with the
 * given name.
 *
 * input guess
 *
 * goto <label>
 * Jumps to the statement after the label with the given name.
 *
 * goto loop
 *
 * if <expression> then <label>
 * Evaluates the expression. If it evaluates to a non-zero number, then jumps to
 * the statement after the given label.
 *
 * if a < b then dosomething
 *
 *
 * The following expressions are supported:
 *
 * <expression> = <expression>
 * Evaluates to 1 if the two expressions are equal, 0 otherwise.
 *
 * <expression> + <expression>
 * If the left-hand expression is a number, then adds the two expressions,
 * otherwise concatenates the two strings.
 *
 * <expression> - <expression>
 * <expression> * <expression>
 * <expression> / <expression>
 * <expression> < <expression>
 * <expression> > <expression>
 * You can figure it out.
 *
 * <name>
 * A name in an expression simply returns the value of the variable with that
 * name. If the variable was never set, it defaults to 0.
 *
 * All binary operators have the same precedence. Sorry, I had to cut corners
 * somewhere.
 *
 * To keep things simple, I've omitted some stuff or hacked things a bit. When
 * possible, I'll leave a "HACK" note there explaining what and why. If you make
 * your own interpreter, you'll want to address those.
 *
 * @author Bob Nystrom
 */
public class Cartridge {

    //private final Map<String, Value> variables;
    //private final LinkedHashMap<String, Integer> labels;
    private final MachineState machineState = new MachineState();
    
    //private final BufferedReader lineIn;

    /**
     * Constructs a new Jasic instance. The instance stores the global state of
     * the interpreter such as the values of all of the variables and the
     * current statement.
     */
    public Cartridge() {
        //variables = new HashMap<>();
        //labels = new LinkedHashMap<>();

        //InputStreamReader converter = new InputStreamReader(System.in);
        //lineIn = new BufferedReader(converter);
    }

   //private int currentStatement;
    /**
     * Runs the interpreter as a command-line app. Takes one argument: a path to
     * a script file to load and run. The script should contain one statement
     * per line.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        // Just show the usage and quit if a script wasn't provided.
        if (args.length != 1) {
            System.out.println("Usage: jasic <script>");
            System.out.println("Where <script> is a relative path to a .jas script to run.");
            return;
        }

        // Read the file.
        String contents = readFile(args[0]);

        // Run it.
        Cartridge cart = new Cartridge();
        cart.interpret(contents);
    }

//    public static Cartridge load(String fileName, Scanner input, int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    /**
     * This is where the magic happens. This runs the code through the parsing
     * pipeline to generate the AST. Then it executes each statement. It keeps
     * track of the current line in a member variable that the statement objects
     * have access to. This lets "goto" and "if then" do flow control by simply
     * setting the index of the current statement.
     *
     * In an interpreter that didn't mix the interpretation logic in with the
     * AST node classes, this would be doing a lot more work.
     *
     * @param source A string containing the source code of a .jas script to
     * interpret.
     */
    public void interpret(String source) {
        // Tokenize.
        System.out.println("Tokenize");
        List<Token> tokens = new ArrayList<>();
        tokens.addAll(Token.tokenize("FUNCTION _MAIN()\n_INIT()\nWHILE(1)\n _UPDATE()\n END\nEND\n"));
        tokens.addAll(Token.tokenize(source));
        tokens.addAll(Token.tokenize("_MAIN()\n"));  // Must be last so that all global tokens execute first.
       //List<Token> tokens = Token.tokenize(source);
        //tokens.

        System.out.println("Token Dump:");
        for ( Token t : tokens ) {
            System.out.println(t.text + ": " + t.type.toString());
        }
        
        // Parse.
        Parser parser = new Parser(tokens);
        System.out.println("Parse...");
        //List<Statement> statements = parser.parse(labels);
        machineState.setStatements(parser.parse(machineState.getLabels()));
        System.out.println("\nParse end.");

        System.out.println(machineState.toString());
       
        machineState.restart();
        
        machineState.run();
    }

     public static String readFile(String path) {
            FileInputStream stream = null;
        try {
            stream = new FileInputStream(path);
            return readFile(stream);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
     }
   
    // Utility stuff -----------------------------------------------------------
    /**
     * Reads the file from the given path and returns its contents as a single
     * string.
     *
     * @param path Path to the text file to read.
     * @return The contents of the file or null if the load failed.
     * @throws IOException
     */
//     public static String readFile(String path) {
    public static String readFile(InputStream stream) {
        try {
            //FileInputStream stream = new FileInputStream(path);

            try {
                InputStreamReader input = new InputStreamReader(stream,
                        Charset.defaultCharset());
                Reader reader = new BufferedReader(input);

                StringBuilder builder = new StringBuilder();
                char[] buffer = new char[8192];
                int read;

                while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                    builder.append(buffer, 0, read);
                }

                // HACK: The parser expects every statement to end in a newline,
                // even the very last one, so we'll just tack one on here in
                // case the file doesn't have one.
                builder.append("\n");

                return builder.toString();
            } finally {
                stream.close();
            }
//        } catch (FileNotFoundException ex) {
//            System.out.println("File not found: " + path);
//            System.exit(1);
//            return null;
        } catch (IOException ex) {
            System.out.println("I/O Error");
            System.exit(74);
        }
        return null;
    }
}
