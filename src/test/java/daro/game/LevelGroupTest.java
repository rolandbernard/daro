package daro.game;


import daro.game.main.LevelGroup;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


public class LevelGroupTest {

    @Test
    public void shouldGenerateList() {
        List<LevelGroup> groups = LevelGroup.parseLevels();
        assertNotEquals(null, groups);
    }
    @Test
    public void listShouldContainElements() {
        List<LevelGroup> groups = LevelGroup.parseLevels();
        assertTrue(groups.size() > 0);
    }
}
