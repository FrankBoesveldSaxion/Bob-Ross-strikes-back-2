package nl.saxion.game.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import nl.saxion.game.game.entities.Player;
import nl.saxion.game.game.entities.Score;
import nl.saxion.game.game.systems.EnemyDroneConfig;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;
import nl.saxion.game.game.entities.EnemyDrone;

import java.util.ArrayList;

public class WorldMap extends ScalableGameScreen {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Player player;
    private Score score;

    // Holds all enemies in the world
    private final ArrayList<EnemyDrone> enemies = new ArrayList<>();

    private OrthographicCamera camara;

    public WorldMap() {
        super(1280, 720);
    }

    @Override
    public void show() {
        // Load the TMX tilemap
        tiledMap = new TmxMapLoader().load("maps/test/testMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Hide the collision layer so the player doesn't see it
        tiledMap.getLayers().get("collision").setVisible(false);

        player = new Player(320, 160, tiledMap);
        player.show();

        camara = new OrthographicCamera();
        camara.setToOrtho(false, 320, 180);

        score = new Score();
        score.show();

        // Spawn enemies
        for (int i = 0; i < EnemyDroneConfig.ENEMY_COUNT; i++) {
            float[] pos = getRandomSpawn();
            enemies.add(new EnemyDrone(pos[0], pos[1], tiledMap, player));
        }
        for (EnemyDrone enemy : enemies) {
            enemy.show();
        }

        // Give player reference to enemies for attack system
        player.setEnemies(enemies);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        GameApp.clearScreen();
        GameApp.startShapeRenderingFilled();
        GameApp.startSpriteRendering();

        // Camera follows the player
        camara.position.set(player.getX(), player.getY(), 0);
        camara.update();

        // Render the map using the camera
        mapRenderer.setView(camara);
        mapRenderer.render();

        // Set projection for game world objects
        GameApp.getShapeRenderer().setProjectionMatrix(camara.combined);

        player.render(delta);

        // Remove dead enemies and render alive ones
        enemies.removeIf(enemy -> {
            if (enemy.isDead()) {
                score.increaseScoreBy(EnemyDroneConfig.SCORE_INCREASE_WHEN_DEAD);
                return true;
            }
            return false;
        });

        for (EnemyDrone enemyDrone : enemies) {
            enemyDrone.render(delta, enemies);
        }

        GameApp.endSpriteRendering();
        GameApp.endShapeRendering();

        // Render UI AFTER ending world rendering - uses screen coordinates
        GameApp.startSpriteRendering();
        score.render(delta);
        GameApp.endSpriteRendering();
    }

    @Override
    public void hide() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }

    private float[] getRandomSpawn() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("collision");

        if (layer == null) {
            System.out.println("ERROR: collision layer not found!");
            return new float[]{0, 0};
        }

        int mapWidth = layer.getWidth();
        int mapHeight = layer.getHeight();

        while (true) {
            int tileX = (int) GameApp.random(0, mapWidth - 1);
            int tileY = (int) GameApp.random(0, mapHeight - 1);

            TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

            if (cell == null ||
                    cell.getTile() == null ||
                    !cell.getTile().getProperties().containsKey("blocked")) {

                float worldX = tileX * 16;
                float worldY = tileY * 16;

                return new float[]{worldX, worldY};
            }
        }
    }
}
