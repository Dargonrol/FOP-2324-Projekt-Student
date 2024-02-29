package projekt.model.buildings;

import javafx.beans.property.Property;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.model.HexGrid;
import projekt.model.Intersection;
import projekt.model.Player;
import projekt.model.TilePosition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link Edge}.
 *
 * @param grid      the HexGrid instance this edge is placed in
 * @param position1 the first position
 * @param position2 the second position
 * @param roadOwner the road's owner, if a road has been built on this edge
 * @param port      a port this edge provides access to, if any
 */
public record EdgeImpl(
    HexGrid grid, TilePosition position1, TilePosition position2, Property<Player> roadOwner, Port port
) implements Edge {
    @Override
    public HexGrid getHexGrid() {
        return grid;
    }

    @Override
    public TilePosition getPosition1() {
        return position1;
    }

    @Override
    public TilePosition getPosition2() {
        return position2;
    }

    @Override
    public boolean hasPort() {
        return port != null;
    }

    @Override
    public Port getPort() {
        return port;
    }

    @Override
    @StudentImplementationRequired("H1.3")
    public boolean connectsTo(final Edge other) {
        return !this.getIntersections().stream()
            .filter(x -> x.getConnectedEdges()
                .stream()
                .anyMatch(y -> y.equals(other)))
            .toList()
            .isEmpty();
       }

    @Override
    @StudentImplementationRequired("H1.3")
    public Set<Intersection> getIntersections() {
        List<Set<TilePosition>> tilePos = this.grid.getIntersections().keySet().stream()
            .filter(x -> x.contains(this.position1) && x.contains(this.position2))
            .toList(); // list of the keys that map to the needed intersections

        // New set of the values mapped to the needed intersections
        Set<Intersection> ret = new HashSet<>();
        ret.add(this.grid.getIntersections().get(tilePos.get(0)));
        ret.add(this.grid.getIntersections().get(tilePos.get(1)));
        return ret;
    }

    @Override
    public Property<Player> getRoadOwnerProperty() {
        return roadOwner;
    }

    @Override
    @StudentImplementationRequired("H1.3")
    public Set<Edge> getConnectedRoads(final Player player) {
       return this.getConnectedEdges().stream()
           .filter(Edge::hasRoad)// filter for edges with roads, since getRoadOwner() has undefined behaviour if there is no road build
           .filter(x -> x.getRoadOwner().equals(player)).collect(Collectors.toSet());
        }
}
