package projekt.H1;

import org.junit.jupiter.api.Test;
import projekt.model.Player;
import projekt.model.PlayerImpl;
import projekt.model.ResourceType;
import org.junit.jupiter.api.Assertions;

public class H1_1_tests {
    @Test
    void H1_1_addResource() {
        PlayerImpl.Builder playerBuilder = new PlayerImpl.Builder(0);
        Player player = playerBuilder.build(null);
        player.addResource(ResourceType.CLAY, 20);
        Assertions.assertEquals(player.getResources().get(ResourceType.CLAY), 20);
        player.addResource(ResourceType.GRAIN, -2);
        Assertions.assertEquals(player.getResources().get(ResourceType.GRAIN), 0);
        player.addResource(ResourceType.ORE, 0);
        Assertions.assertEquals(player.getResources().get(ResourceType.ORE), 0);
    }

    @Test
    void H1_1_addResources() {
        PlayerImpl.Builder playerBuilder = new PlayerImpl.Builder(0);
        Player player = playerBuilder.build(null);
        player.addResources(resources.randomResources1);
        Assertions.assertEquals(player.getResources().get(ResourceType.CLAY), 10);
        Assertions.assertEquals(player.getResources().get(ResourceType.ORE), 5);
        Assertions.assertEquals(player.getResources().get(ResourceType.GRAIN), 0);
        Assertions.assertEquals(player.getResources().get(ResourceType.WOOD), 0);
    }
}
