package daro.lang.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import daro.lang.ast.Position;
import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.InterpreterException;
import daro.lang.parser.ParsingException;
import daro.lang.values.UserObject;

/**
 * This is a small REPL (Read-Eval-Print Loop) cli interface into the interpreter designed for testing and demonstration
 * of the programming language.
 * 
 * @author Roland Bernard
 */
public class Cli {

    /**
     * This method prints a error onto System.out.err,
     * 
     * @param type
     *            The type of error. e.g Syntax error, Runtime error, ...
     * @param position
     *            The position the error occurred at
     * @param message
     *            The message the error contains
     */
    private static void printError(String type, Position position, String message) {
        System.err.println(type + " at " + position + ": " + message);
    }

    /**
     * Execute the given files inside a single interpreter.
     * 
     * @param files
     *            The filenames of the files that should be executed
     */
    public static void executeFiles(String[] files) {
        Interpreter interpreter = new Interpreter();
        for (String file : files) {
            try {
                String source = Files.readString(Path.of(file));
                interpreter.execute(source);
            } catch (InterpreterException error) {
                printError("Runtime error", error.getPosition(), error.getMessage());
            } catch (ParsingException error) {
                printError("Syntax error", error.getPosition(), error.getMessage());
            } catch (IOException e) {
                printError("File error", new Position(Path.of(file)), "Failed to open file");
            }
        }
    }

    /**
     * Start executing in REPL mode. This will continue until the stream has no more lines.
     */
    public static void executeRepl() {
        Interpreter interpreter = new Interpreter();
        Scanner scanner = new Scanner(System.in);
        StringBuilder code = new StringBuilder();
        System.out.print("> ");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            code.append(line + "\n");
            String program = code.toString();
            try {
                UserObject value = interpreter.execute(program);
                if (value != null) {
                    try {
                        System.out.println(value);
                    } catch (StackOverflowError error) {
                        System.out.println("..recursive object..");
                    }
                }
                code.setLength(0);
                System.out.print("> ");
            } catch (InterpreterException error) {
                printError("Runtime error", error.getPosition(), error.getMessage());
                code.setLength(0);
                System.out.print("> ");
            } catch (ParsingException error) {
                if (line.isEmpty()) {
                    printError("Syntax error", error.getPosition(), error.getMessage());
                    code.setLength(0);
                    System.out.print("> ");
                } else {
                    System.out.print("  ");
                }
            }
        }
        scanner.close();
    }

    /**
     * Main function of this CLI application.
     * 
     * @param args
     *            Command line arguments to the program
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            executeFiles(args);
        } else {
            executeRepl();
        }
    }
}
