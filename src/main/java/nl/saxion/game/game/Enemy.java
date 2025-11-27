package nl.saxion.game.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import nl.saxion.gameapp.GameApp;

public class Enemy {

    private float x;
    private float y;
    private final TiledMap map;
    private final Player player;

    public Enemy(float startX, float startY, TiledMap map, Player player) {
        this.x = startX;
        this.y = startY;
        this.map = map;
        this.player = player;
    }

    public void update(float delta) {
        float targetX = player.getX();
        float targetY = player.getY();

        float dx = targetX - x;
        float dy = targetY - y;

        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 0.5f) return;

        // Enemy movement speed
        float speed = 30 * delta;

        float normX = dx / dist;
        float normY = dy / dist;

        float newX = x + normX * speed;
        float newY = y + normY * speed;

        if (!isCollision(newX, y)) x = newX;
        if (!isCollision(x, newY)) y = newY;
    }

    private boolean isCollision(float x, float y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("collision");

        if (layer == null) {
            System.out.println("ERROR: Collision layer not found!");
            return false;
        }

        int tileX = (int) (x / 16);
        int tileY = (int) (y / 16);

        TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

        if (cell == null || cell.getTile() == null) return false;

        return cell.getTile().getProperties().containsKey("blocked");
    }

    public void render() {
        GameApp.drawCircle(x, y, 4, "red-600");
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
