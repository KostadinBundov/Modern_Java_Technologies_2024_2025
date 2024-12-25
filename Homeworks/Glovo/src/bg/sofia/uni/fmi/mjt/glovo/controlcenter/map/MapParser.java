package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapEntityException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapSizeException;

public class MapParser {
    public static MapEntity[][] createMap(char[][] mapLayout) {
        if (mapLayout == null || mapLayout.length == 0) {
            throw new IllegalArgumentException("Map cannot be null or empty!");
        }

        MapEntity[][] map = new MapEntity[mapLayout.length][];
        final int rowLength = mapLayout[0].length;

        for (int i = 0; i < mapLayout.length; i++) {
            if (mapLayout[i] == null || mapLayout[i].length == 0) {
                throw new IllegalArgumentException("Map cannot be null or empty!");
            }
            int columnsCount = mapLayout[i].length;

            if (columnsCount != rowLength) {
                throw new InvalidMapSizeException("Map size must be n x m!");
            }

            map[i] = new MapEntity[columnsCount];

            for (int j = 0; j < columnsCount; j++) {
                Location location = new Location(i, j);
                MapEntityType type = getType(mapLayout[i][j]);

                map[i][j] = new MapEntity(location, type);
            }
        }

        return map;
    }

    private static MapEntityType getType(char entity) {
        if (entity == MapEntityType.ROAD.getSymbol()) {
            return MapEntityType.ROAD;

        } else if (entity == MapEntityType.WALL.getSymbol()) {
            return MapEntityType.WALL;

        } else if (entity == MapEntityType.CLIENT.getSymbol()) {
            return MapEntityType.CLIENT;

        } else if (entity == MapEntityType.RESTAURANT.getSymbol()) {
            return MapEntityType.RESTAURANT;

        } else if (entity == MapEntityType.DELIVERY_GUY_CAR.getSymbol()) {
            return MapEntityType.DELIVERY_GUY_CAR;

        } else if (entity == MapEntityType.DELIVERY_GUY_BIKE.getSymbol()) {
            return MapEntityType.DELIVERY_GUY_BIKE;

        } else {
            throw new InvalidMapEntityException("Invalid map entity!");
        }
    }
}
