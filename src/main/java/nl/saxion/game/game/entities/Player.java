package nl.saxion.game.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import nl.saxion.game.game.systems.SpriteConfig;
import nl.saxion.gameapp.GameApp;

import static nl.saxion.game.game.systems.CollisionSystem.isCollision;

public class Player {
    private float x;
    private float y;
    private final TiledMap map;
    private int currentDirection = 2; // 1=left, 2=right

    public Player(float startX, float startY, TiledMap map) {
        this.x = startX;
        this.y = startY;
        this.map = map;
    }

    public void show() {
        SpriteConfig config = new SpriteConfig();
        GameApp.addSpriteSheet("bobWalkLeft", "textures/animations/bobRossRunAnimationLeftRun.png", config.getFrameWidth(), config.getFrameHeight());
        GameApp.addAnimationFromSpritesheet("bobWalkLeft", "bobWalkLeft", config.getFrameDuration(), true);

        GameApp.addSpriteSheet("bobWalkRight", "textures/animations/bobRossRunAnimationRightRun.png", config.getFrameWidth(), config.getFrameHeight());
        GameApp.addAnimationFromSpritesheet("bobWalkRight", "bobWalkRight", config.getFrameDuration(), true);
    }

    public void update(float delta) {
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

        // ONLY update animation if moving
        if (isMoving) {
            if (currentDirection == 1) {
                GameApp.updateAnimation("bobWalkLeft");
            } else {
                GameApp.updateAnimation("bobWalkRight");
            }
        }

        if (!isCollision(newX, newY, map)) {
            x = newX;
            y = newY;
        }
    }

    public void render() {
        // draw animation based on the direction.
        if (currentDirection == 1) {
            GameApp.drawAnimation("bobWalkLeft", x - 15, y - 5, 32f, 32f);
        } else {
            GameApp.drawAnimation("bobWalkRight", x - 15, y - 5, 32f, 32f);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
