package nl.saxion.game.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import nl.saxion.gameapp.GameApp;

import static nl.saxion.game.game.systems.CollisionSystem.isCollision;

public class Player {
    private float x;
    private float y;
    private final TiledMap map;

    public Player(float startX, float startY, TiledMap map) {
        this.x = startX;
        this.y = startY;
        this.map = map;
    }

    public void update(float delta) {
        float newX = x;
        float newY = y;

        // calculate the new position for the camara.
        // player does not move the camara moved in WorldMap.java
        float speed = 100;
        if (GameApp.isKeyPressed(51)){
            newY += speed * delta; // W
        }
        if (GameApp.isKeyPressed(47)){
            newY -= speed * delta; // S
        }
        if (GameApp.isKeyPressed(29)) {
            newX -= speed * delta; // A
        }
        if (GameApp.isKeyPressed(32)) {
            newX += speed * delta; // D
        }

        if (!isCollision(newX, newY, map)) {
            x = newX;
            y = newY;
        }
    }

    public void render() {
        //TODO replace with sprite logic
        GameApp.drawCircle(x, y, 4);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
