package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.ast.AstAddition;
import daro.lang.ast.AstAssignment;
import daro.lang.ast.AstNode;
import daro.lang.ast.AstSymbol;
import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExecutionObserverTest {
    private Interpreter interpreter;
    private TestObserver observer;
    private ExecutionObserver[] observers;

    private class TestObserver implements ExecutionObserver {
        public int assignments = 0;
        public int additions = 0;
        public int localization = 0;

        @Override
        public void beforeNodeExecution(AstNode node, Scope scope) {
            if (node instanceof AstAssignment) {
                assignments++;
            } else if (node instanceof AstAddition) {
                additions++;
            }
        }

        @Override
        public void afterNodeExecution(AstNode node, UserObject value, Scope scope) {
            if (node instanceof AstAssignment) {
                assignments++;
            } else if (node instanceof AstAddition) {
                additions++;
            }
        }

        @Override
        public void beforeNodeLocalization(AstNode node, Scope scope) {
            if (node instanceof AstSymbol) {
                localization++;
            }
        }

        @Override
        public void afterNodeLocalization(AstNode node, VariableLocation location, Scope scope) {
            if (node instanceof AstSymbol) {
                localization++;
            }
        }
    }

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
        observer = new TestObserver();
        observers = new ExecutionObserver[] { observer };
    }

    @Test
    void countSimpleAssignments() {
        interpreter.execute("a = 1; b = 2; c = 3; d = 4;", observers);
        assertEquals(8, observer.assignments);
    }

    @Test
    void dontCountOtherExecutes() {
        interpreter.execute("a = 1; b = 2; c = 3; d = 4;", observers);
        interpreter.execute("a = 1; b = 2; c = 3; d = 4;");
        assertEquals(8, observer.assignments);
    }

    @Test
    void countAssignmentsInBlock() {
        interpreter.execute("if true { a = 1; b = 2; c = 3 }", observers);
        assertEquals(6, observer.assignments);
    }

    @Test
    void countAssignmentsDefiningFunction() {
        interpreter.execute("fn test() { x = 1; y = 2 }", observers);
        assertEquals(0, observer.assignments);
    }

    @Test
    void countAssignmentsExecutingFunction() {
        interpreter.execute("fn test() { x = 1; y = 2 }");
        interpreter.execute("test(); test()", observers);
        assertEquals(8, observer.assignments);
    }

    @Test
    void countAssignmentsDefiningClass() {
        interpreter.execute("class Test { x = 0; y = 1 }", observers);
        assertEquals(0, observer.assignments);
    }

    @Test
    void countAssignmentsInstantiatingClass() {
        interpreter.execute("class Test { x = 0; y = 1 }");
        interpreter.execute("new [10]Test", observers);
        assertEquals(40, observer.assignments);
    }

    @Test
    void countAssignmentsInitializingClass() {
        interpreter.execute("class Test { x = 0; y = 1 }");
        interpreter.execute("new Test { x = x = 5, y = y = 6, z = z = 7 }", observers);
        assertEquals(10, observer.assignments);
    }

    @Test
    void countUnfinishedAssignmentExecution() {
        try {
            interpreter.execute("x = 0 / 0", observers);
        } catch (InterpreterException error) { }
        assertEquals(1, observer.assignments);
    }

    @Test
    void countAdditions() {
        interpreter.execute("x = 1 + (0 * 12 + 7 * (1 + 1))", observers);
        assertEquals(6, observer.additions);
    }

    @Test
    void countAdditionsShortCircuitAnd() {
        interpreter.execute("0 == 1 + 1 && 2 == 1 + 1", observers);
        assertEquals(2, observer.additions);
    }

    @Test
    void countAdditionsShortCircuitOr() {
        interpreter.execute("2 == 1 + 1 || 0 == 1 + 1", observers);
        assertEquals(2, observer.additions);
    }

    @Test
    void countAdditionsFullAnd() {
        interpreter.execute("2 == 1 + 1 && 0 == 1 + 1", observers);
        assertEquals(4, observer.additions);
    }

    @Test
    void countAdditionsFullOr() {
        interpreter.execute("0 == 1 + 1 || 2 == 1 + 1", observers);
        assertEquals(4, observer.additions);
    }

    @Test
    void countInitializerAdditions() {
        interpreter.execute("new int { 1 + 2 + 3 + 4 }", observers);
        assertEquals(6, observer.additions);
    }

    @Test
    void countLocalization() {
        interpreter.execute("x = 1; y = x + x; z = y + y;", observers);
        assertEquals(6, observer.localization);
    }
}
