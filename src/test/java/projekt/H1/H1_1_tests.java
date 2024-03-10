package projekt.H1;

import org.junit.jupiter.api.Test;
import projekt.model.Player;
import projekt.model.PlayerImpl;
import projekt.model.ResourceType;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public class H1_1_tests {
    @Test
    void H1_1_addResource() {
        PlayerImpl.Builder playerBuilder = new PlayerImpl.Builder(0);
        Player player = playerBuilder.build(null);

        player.addResource(ResourceType.CLAY, 20);
        assertEquals(player.getResources().get(ResourceType.CLAY), 20);

        player.addResource(ResourceType.GRAIN, -2);
        assertEquals(0, (int) player.getResources().getOrDefault(ResourceType.GRAIN, 0));
        assertNull(player.getResources().get(ResourceType.GRAIN));

        player.addResource(ResourceType.ORE, 0);
        assertEquals(0, (int) player.getResources().getOrDefault(ResourceType.ORE, 0));
        assertNull(player.getResources().get(ResourceType.ORE));
    }

    @Test
    void H1_1_addResources() {
        PlayerImpl.Builder playerBuilder = new PlayerImpl.Builder(0);
        Player player = playerBuilder.build(null);
        player.addResources(resources.randomResources1);

        assertEquals(player.getResources().get(ResourceType.CLAY), 10);

        assertEquals(player.getResources().get(ResourceType.ORE), 5);

        assertEquals(0, (int) player.getResources().getOrDefault(ResourceType.GRAIN, 0));
        assertNull(player.getResources().get(ResourceType.GRAIN));

        assertEquals(0, (int) player.getResources().getOrDefault(ResourceType.WOOD, 0));
        assertNull(player.getResources().get(ResourceType.WOOD));
    }

    /**
     * REQUIRES addResources to work
     */
    @Test
    void H1_1_hasResources() {
        PlayerImpl.Builder playerBuilder = new PlayerImpl.Builder(0);
        Player player = playerBuilder.build(null);

        player.addResources(resources.randomResources1);
        Assertions.assertTrue(player.hasResources(resources.randomResources1));
        Assertions.assertFalse(player.hasResources(resources.randomResources2));
    }

    @Test
    void H1_1_removeResource() {
        PlayerImpl.Builder playerBuilder = new PlayerImpl.Builder(0);
        Player player = playerBuilder.build(null);

        player.addResources(resources.randomResources1);
        assertTrue(player.removeResource(ResourceType.CLAY, 5));
        assertEquals(player.getResources().get(ResourceType.CLAY), 5);

        assertFalse(player.removeResource(ResourceType.WOOD, 6));
        assertEquals(0, (int) player.getResources().getOrDefault(ResourceType.WOOD, 0));
        assertNull(player.getResources().get(ResourceType.WOOD));

        assertTrue(player.removeResource(ResourceType.CLAY, 5));
        assertEquals(0, (int) player.getResources().getOrDefault(ResourceType.CLAY, 0));
        assertNull(player.getResources().get(ResourceType.CLAY));

        assertFalse(player.removeResource(ResourceType.ORE, -5));
        assertEquals(player.getResources().get(ResourceType.ORE), 5);
    }

    @Test
    void H1_1_removeResources() {
        PlayerImpl.Builder playerBuilder = new PlayerImpl.Builder(0);
        Player player = playerBuilder.build(null);

        player.addResources(resources.randomResources1);
        assertFalse(player.removeResources(resources.randomResources2));
        assertTrue(player.hasResources(resources.randomResources1)); // player inv changed! if False

        assertTrue(player.removeResources(resources.randomResources1));
        resources.randomResources1.forEach((key, value) -> {
            assertEquals(0, (int) player.getResources().getOrDefault(key, 0));
            assertNull(player.getResources().get(key));
        });
    }
}
