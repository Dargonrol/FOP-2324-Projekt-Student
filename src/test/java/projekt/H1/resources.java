package projekt.H1;

import projekt.model.ResourceType;

import java.util.HashMap;
import java.util.Map;

public class resources {
    public static final Map<ResourceType, Integer> randomResources1 = new HashMap<>(){{
        put(ResourceType.CLAY, 10);
        put(ResourceType.ORE, 5);
        put(ResourceType.GRAIN, -5);
        put(ResourceType.WOOD, 0);
    }};
}
