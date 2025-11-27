package nl.saxion.game.game.systems.;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class CollisionSystem {

    static public boolean isCollision(float x, float y, TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("collision");

        if (layer == null) {
            System.out.println("ERROR: collision layer not found! check for a typo");
            return false;
        }

        //16: the tileSize
        int tileX = (int) (x / 16);
        int tileY = (int) (y / 16);

        TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

        if (cell == null || cell.getTile() == null) {
            return false;
        }

        // returns true if there is a collision.
        return cell.getTile().getProperties().get("blocked").equals(true);
    }
}
