package projekt.H1;

import org.junit.jupiter.api.Test;
import projekt.model.Player;
import projekt.model.PlayerImpl;
import projekt.model.ResourceType;
import org.junit.jupiter.api.Assertions;

public class H1_1_tests {
    @Test
    void H1_1_test() {
        // method addResource
        PlayerImpl.Builder playerBuilder = new PlayerImpl.Builder(0);
        Player player = playerBuilder.build(null);
        player.addResource(ResourceType.CLAY, 20);
        Assertions.assertEquals(player.getResources().get(ResourceType.CLAY), 20);

    }
}
