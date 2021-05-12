package daro.lang;

import java.util.Scanner;

import daro.lang.ast.Position;
import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.InterpreterException;
import daro.lang.parser.ParsingException;
import daro.lang.values.UserObject;

public class Cli {

    private static int lineFromOffset(int offset, String text) {
        String[] lines = text.split("\n");
        int current = 0;
        int line = 0;
        while (line < lines.length && current <= offset) {
            current += lines[line].length() + 1;
            line++;
        }
        return line;
    }

    private static int columnFromOffset(int offset, String text) {
        String[] lines = text.split("\n");
        int current = 0;
        int line = 0;
        while (line < lines.length && current <= offset) {
            current += lines[line].length() + 1;
            line++;
        }
        line--;
        current -= lines[line].length() + 1;
        return offset - current;
    }

    private static void printError(String type, Position position, String program, String message) {
        System.err.println(
            type + " at " + lineFromOffset(position.getStart(), program)
            + ":" + columnFromOffset(position.getStart(), program)
            + ": " + message
        );
    }

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
                    System.out.println(value);
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
