package nl.saxion.game.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import nl.saxion.game.game.systems.SpriteConfig;
import nl.saxion.gameapp.GameApp;


import java.util.ArrayList;

import static nl.saxion.game.game.systems.CollisionSystem.isCollision;

public class Player {
    private float x;
    private float y;
    private final TiledMap map;
    private int currentDirection = 2; // 1=left, 2=right
    private boolean spaceWasPressed = false; // Prevent holding space
    private ArrayList<EnemyDrone> enemies; // Reference to enemies list

    public Player(float startX, float startY, TiledMap map) {
        this.x = startX;
        this.y = startY;
        this.map = map;
    }

    public void setEnemies(ArrayList<EnemyDrone> enemies) {
        this.enemies = enemies;
    }

    public void show() {
        GameApp.addSpriteSheet("bobWalkLeft", "textures/animations/Player/bobRossRunAnimationLeftRun.png", SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobWalkLeft", "bobWalkLeft", SpriteConfig.FRAME_DURATION, true);

        GameApp.addSpriteSheet("bobWalkRight", "textures/animations/Player/bobRossRunAnimationRightRun.png", SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobWalkRight", "bobWalkRight", SpriteConfig.FRAME_DURATION, true);
    }

    public void render(float delta) {
        float newX = x;
        float newY = y;
        boolean isMoving = false;

        float speed = 100;
        if (GameApp.isKeyPressed(51)) {
            newY += speed * delta; // W
            isMoving = true;
        }
        if (GameApp.isKeyPressed(47)) {
            newY -= speed * delta; // S
            isMoving = true;
        }
        if (GameApp.isKeyPressed(29)) {
            newX -= speed * delta; // A
            currentDirection = 1;
            isMoving = true;
        }
        if (GameApp.isKeyPressed(32)) {
            newX += speed * delta; // D
            currentDirection = 2;
            isMoving = true;
        }

        // Handle attack with single press detection
        if (GameApp.isKeyPressed(62)) { // Space key
            if (!spaceWasPressed) {
                mainAttack();
                spaceWasPressed = true;
            }
        } else {
            spaceWasPressed = false;
        }

        // ONLY update animation if moving
        if (isMoving) {
            if (currentDirection == 1) {
                GameApp.updateAnimation("bobWalkLeft");
            } else {
                GameApp.updateAnimation("bobWalkRight");
            }
        } else {
            //TODO idle animation
        }

        // ALWAYS draw the animation
        if (currentDirection == 1) {
            GameApp.drawAnimation("bobWalkLeft", x - 15, y - 5, 32f, 32f);
        } else {
            GameApp.drawAnimation("bobWalkRight", x - 15, y - 5, 32f, 32f);
        }

        if (!isCollision(newX, newY, map)) {
            x = newX;
            y = newY;
        }
    }

    public void mainAttack() {
        if (enemies == null) return;

        // Check all enemies and damage those in range
        for (EnemyDrone enemy : enemies) {
            float distance = calculateDistance(x, y, enemy.getX(), enemy.getY());

            // Attack range in pixels
            float attackRange = SpriteConfig.ATTACK_RANGE;
            if (distance <= attackRange) {
                enemy.takeDamage(1);
            }
        }
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
