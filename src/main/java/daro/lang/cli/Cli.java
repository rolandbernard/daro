package daro.lang.cli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import daro.lang.ast.Position;
import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.InterpreterException;
import daro.lang.parser.ParsingException;
import daro.lang.values.UserObject;

/**
 * This is a small REPL (Read-Eval-Print Loop) cli interface into the interpreter designed for
 * testing and demonstration of the programming language.
 * 
 * @author Roland Bernard
 */
public class Cli {

    /**
     * This method computes the line number from a offset into the given text.
     * @param offset The offset to check the line number of
     * @param text The text the offset refers to
     * @return The line number of the offset
     */
    public static int lineFromOffset(int offset, String text) {
        String[] lines = text.split("\n");
        int current = 0;
        int line = 0;
        while (line < lines.length && current <= offset) {
            current += lines[line].length() + 1;
            line++;
        }
        return line;
    }

    /**
     * This method computes the column number from a offset into the given text.
     * @param offset The offset to check the column number of
     * @param text The text the offset refers to
     * @return The column number of the offset
     */
    public static int columnFromOffset(int offset, String text) {
        String[] lines = text.split("\n");
        int current = 0;
        int line = 0;
        while (line < lines.length && current <= offset) {
            current += lines[line].length() + 1;
            line++;
        }
        line--;
        current -= lines[line].length() + 1;
        return offset - current + 1;
    }

    /**
     * This method prints a error onto System.out.err,
     * @param type The type of error. e.g Syntax error, Runtime error, ...
     * @param position The position the error occurred at
     * @param program The source code of the program that was run
     * @param message The message the error contains
     */
    private static void printError(String type, Position position, String program, String message) {
        System.err.println(
            type + " at " + lineFromOffset(position.getStart(), program)
            + ":" + columnFromOffset(position.getStart(), program)
            + ": " + message
        );
    }

    /**
     * This method prints a error onto System.out.err,
     * @param type The type of error. e.g Syntax error, Runtime error, ...
     * @param file The file the error occurred in
     * @param position The position the error occurred at
     * @param program The source code of the program that was run
     * @param message The message the error contains
     */
    private static void printError(String type, String file, Position position, String program, String message) {
        System.err.println(
            type + " at " + file + ":" + lineFromOffset(position.getStart(), program)
            + ":" + columnFromOffset(position.getStart(), program)
            + ": " + message
        );
    }

    /**
     * This method prints a error onto System.out.err,
     * @param type The type of error. e.g Syntax error, Runtime error, ...
     * @param file The file the error occurred in
     * @param message The message the error contains
     */
    private static void printError(String type, String file, String message) {
        System.err.println(
            type + " in " + file + ": " + message
        );
    }

    /**
     * Execute the given files inside a single interpreter.
     * @param files The filenames of the files that should be executed
     */
    public static void executeFiles(String[] files) {
        Interpreter interpreter = new Interpreter();
        String lastSource = "";
        String lastFile = "";
        try {
            for (String file : files) {
                lastFile = file;
                lastSource = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
                interpreter.execute(lastSource);
            }
        } catch (InterpreterException error) {
            printError("Runtime error", lastFile, error.getPosition(), lastSource, error.getMessage());
        } catch (ParsingException error) {
            printError("Syntax error", lastFile, error.getPosition(), lastSource, error.getMessage());
        } catch (IOException e) {
            printError("File error", lastFile, "Failed to open file");
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
                printError("Runtime error", error.getPosition(), program, error.getMessage());
                code.setLength(0);
                System.out.print("> ");
            } catch (ParsingException error) {
                if (line.isEmpty()) {
                    printError("Syntax error", error.getPosition(), program, error.getMessage());
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
     * @param args Command line arguments to the program
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            executeFiles(args);
        } else {
            executeRepl();
        }
    }
}
