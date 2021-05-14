package daro.lang.cli;

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
     * Main function of this CLI application.
     * @param args Command line arguments to the program
     */
    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        Scanner scanner = new Scanner(System.in);
        StringBuilder code = new StringBuilder();
        System.out.print("> ");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            code.append(line);
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
            } catch (InterpreterException error) {
                printError("Runtime error", error.getPosition(), program, error.getMessage());
                code.setLength(0);
            } catch (ParsingException error) {
                if (line.isEmpty()) {
                    printError("Syntax error", error.getPosition(), program, error.getMessage());
                    code.setLength(0);
                }
            }
            System.out.print("> ");
        }
        scanner.close();
    }
}
