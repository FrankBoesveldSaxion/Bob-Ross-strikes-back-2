package nl.saxion.game.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import nl.saxion.game.game.systems.EnemyDroneConfig;
import nl.saxion.game.game.systems.SpriteConfig;
import nl.saxion.gameapp.GameApp;

import java.util.ArrayList;

import static nl.saxion.game.game.systems.CollisionSystem.isCollision;

public class EnemyDrone {

    private float x;
    private float y;
    private int health;
    private boolean isDying = false; // Track death state

    private final TiledMap map;
    private final Player player;
    private boolean isDead = false;

    public EnemyDrone(float startX, float startY, TiledMap map, Player player) {
        this.x = startX;
        this.y = startY;
        this.map = map;
        this.player = player;
        this.health = EnemyDroneConfig.HEALTH;
    }

    public void show() {
        GameApp.addSpriteSheet("enemyDrone", "textures/animations/droneEnemy/enemyDrone.png", SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("enemyDrone", "enemyDrone", SpriteConfig.FRAME_DURATION, true);

        GameApp.addSpriteSheet("droneDeath", "textures/animations/droneEnemy/deathSheet.png", SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("droneDeath", "droneDeath", SpriteConfig.FRAME_DURATION, false);
    }

    public void render(float delta, ArrayList<EnemyDrone> allEnemies) {
        // Check if drone should start dying
        if (health <= 0 && !isDying) {
            isDying = true;
            GameApp.resetAnimation("droneDeath");
        }

        // Draw the appropriate animation based on state
        if (isDying) {
            GameApp.drawAnimation("droneDeath", x - 15, y - 20, 32f, 32f);
            GameApp.updateAnimation("droneDeath");
            if (GameApp.isAnimationFinished("droneDeath")){
                isDead = true;
            }
            return;
        } else {
            GameApp.drawAnimation("enemyDrone", x - 15, y - 20, 32f, 32f);
            GameApp.updateAnimation("enemyDrone");
        }

        float targetX = player.getX();
        float targetY = player.getY();

        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist < 0.5f) return;

        float speed = EnemyDroneConfig.ENEMY_SPEED * delta;
        float normX = dx / dist;
        float normY = dy / dist;
        float moveX = normX * speed;
        float moveY = normY * speed;

        if (!isCollision(x + moveX, y + moveY, map)) {
            x += moveX;
            y += moveY;
        } else {
            if (!isCollision(x + moveX, y, map)) {
                x += moveX;
            }
            if (!isCollision(x, y + moveY, map)) {
                y += moveY;
            }
        }

        for (EnemyDrone other : allEnemies) {
            if (other == this) continue;

            float d = (float) Math.sqrt(
                    (x - other.x) * (x - other.x) +
                            (y - other.y) * (y - other.y)
            );

            if (d < EnemyDroneConfig.PUSH_RADIUS * 2) {
                float overlap = (EnemyDroneConfig.PUSH_RADIUS * 2) - d;
                float ox = (x - other.x) / d * overlap * 0.5f;
                float oy = (y - other.y) / d * overlap * 0.5f;

                if (!isCollision(x + ox, y, map)) {
                    x += ox;
                }
                if (!isCollision(x, y + oy, map)) {
                    y += oy;
                }
            }
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isDead(){
        return isDead;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
