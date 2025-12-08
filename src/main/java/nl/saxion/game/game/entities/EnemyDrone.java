package nl.saxion.game.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import nl.saxion.game.game.systems.EnemyConfig;
import nl.saxion.game.game.systems.SpriteConfig;
import nl.saxion.gameapp.GameApp;

import java.util.ArrayList;

import static nl.saxion.game.game.systems.CollisionSystem.isCollision;

public class EnemyDrone {

    private float x;
    private float y;


    private final TiledMap map;
    private final Player player;

    public EnemyDrone(float startX, float startY, TiledMap map, Player player) {
        this.x = startX;
        this.y = startY;
        this.map = map;
        this.player = player;
    }

    public void show() {
        SpriteConfig config = new SpriteConfig();
        GameApp.addSpriteSheet("enemyDrone", "textures/animations/enemy/enemyDrone.png", config.getFrameWidth(), config.getFrameHeight());
        GameApp.addAnimationFromSpritesheet("enemyDrone", "enemyDrone", config.getFrameDuration(), true);
    }

    public void render (float delta, ArrayList<EnemyDrone> allEnemies) {
        GameApp.drawAnimation("enemyDrone", x - 15, y - 20, 32f, 32f);

        float targetX = player.getX();
        float targetY = player.getY();

        // Direction vector from enemy to player
        float dx = targetX - x;
        float dy = targetY - y;

        // Distance to player
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        // Stop if extremely close to the player (prevents jittering)
        if (dist < 0.5f) return;

        // Calculate movement speed for this frame
        float speed = EnemyConfig.ENEMY_SPEED * delta;

        // Normalize direction (length = 1)
        float normX = dx / dist;
        float normY = dy / dist;

        // The intended movement this frame
        float moveX = normX * speed;
        float moveY = normY * speed;

        // --- WALL AVOIDANCE AND SLIDING BEHAVIOR ---

        // 1. Try full movement (diagonal movement toward the player)
        if (!isCollision(x + moveX, y + moveY, map)) {
            x += moveX;
            y += moveY;
        } else {
            // 2. If full movement is blocked, try moving only horizontally
            if (!isCollision(x + moveX, y, map)) {
                x += moveX;
            }
            // 3. If that fails, try only vertical movement
            if (!isCollision(x, y + moveY, map)) {
                y += moveY;
            }
            // This results in enemies "sliding" along walls rather than getting stuck
        }

        // --- ENEMY-TO-ENEMY AVOIDANCE ---

        // Prevent enemies from overlapping or pushing each other into walls
        for (EnemyDrone other : allEnemies) {
            if (other == this) continue; // skip self

            // Distance between this enemy and the other
            float d = (float) Math.sqrt(
                    (x - other.x) * (x - other.x) +
                            (y - other.y) * (y - other.y)
            );

            // If they are too close, push them apart slightly
            if (d < EnemyConfig.PUSH_RADIUS * 2) {
                float overlap = (EnemyConfig.PUSH_RADIUS * 2) - d;

                // Direction away from the other enemy
                float ox = (x - other.x) / d * overlap * 0.5f;
                float oy = (y - other.y) / d * overlap * 0.5f;

                // Only apply the push if it does NOT push the enemy into a wall
                if (!isCollision(x + ox, y, map)) {
                    x += ox;
                }
                if (!isCollision(x, y + oy, map)) {
                    y += oy;
                }
            }
            GameApp.updateAnimation("enemyDrone");
        }

    }
}
