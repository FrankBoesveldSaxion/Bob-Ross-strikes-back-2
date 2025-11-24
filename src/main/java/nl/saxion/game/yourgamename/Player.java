package nl.saxion.game.yourgamename;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import nl.saxion.gameapp.GameApp;

public class Player {
    private float x;
    private float y;
    private float speed = 100;
    private final TiledMap map;

    public Player(float startX, float startY, TiledMap map) {
        this.x = startX;
        this.y = startY;
        this.map = map;
    }

    public void update(float delta) {
        float newX = x;
        float newY = y;

        // Bereken nieuwe positie
        if (GameApp.isKeyPressed(51)) newY += speed * delta; // W
        if (GameApp.isKeyPressed(47)) newY -= speed * delta; // S
        if (GameApp.isKeyPressed(29)) newX -= speed * delta; // A
        if (GameApp.isKeyPressed(32)) newX += speed * delta; // D

        // Check collision voordat je beweegt
        if (!isCollision(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    private boolean isCollision(float x, float y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("collision");

        if (layer == null) {
            System.out.println("ERROR: collision layer not found!");
            return false;
        }

        int tileSize = 16;
        int tileX = (int) (x / tileSize);
        int tileY = (int) (y / tileSize);

        TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

        if (cell == null || cell.getTile() == null) {
            return false;
        }


        //returns true if there is a collision.
        if (cell.getTile().getProperties().get("blocked").equals(true)) {
            System.out.println("COLLISION at (" + tileX + "," + tileY + ")");
            return true;
        }else {
            return false;
        }
    }

    public void render() {
        GameApp.drawCircle(x, y, 16);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
