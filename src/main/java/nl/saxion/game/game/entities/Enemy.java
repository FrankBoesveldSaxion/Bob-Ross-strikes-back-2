package nl.saxion.game.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import nl.saxion.gameapp.GameApp;

import static nl.saxion.game.game.systems.CollisionSystem.isCollision;

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

        if (!isCollision(newX, y, map)) x = newX;
        if (!isCollision(x, newY, map)) y = newY;
    }

    public void render() {
        GameApp.drawCircle(x, y, 4, "red-600");
    }
}
