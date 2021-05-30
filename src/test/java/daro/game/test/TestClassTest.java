package daro.game.test;

import java.util.List;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestClassTest {
    @Test
    public void shouldAcceptSimpleTest() {
        List<TestResult> results = daro.game.test.Test.run("fn test() { return \"testOutput\"}", List.of(
                new daro.game.test.Test(1, TestType.EQUALS, TestSource.FUNCTION, "test", "\"testOutput\""))
        );
        for(TestResult result : results) {
            assertTrue(result.evaluate());
        }
    }
}
