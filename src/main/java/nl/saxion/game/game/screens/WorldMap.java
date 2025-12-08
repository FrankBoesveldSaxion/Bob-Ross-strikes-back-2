package nl.saxion.game.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import nl.saxion.game.game.entities.Player;
import nl.saxion.game.game.entities.Score;
import nl.saxion.game.game.systems.EnemyConfig;
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
        tiledMap = new TmxMapLoader().load("maps/NewMap/DefenitiveMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        // Hide the collision layer so the player doesn't see it
        // tiledMap.getLayers().get("collision").setVisible(false);

        GameApp.addFont("cyberpunk", "fonts/Sefa.ttf", 75);
        GameApp.addTexture("scoreBoard", "textures/scoreBoard.png");
        GameApp.addFont("basicSmall", "fonts/basic.ttf", 50);

        camara = new OrthographicCamera();
        camara.setToOrtho(false, 320, 180);

        // Create player at fixed position
        player = new Player(320, 160, tiledMap);
        /*
            Spawn enemies based on EnemyConfig settings.
            If RANDOM_SPAWN = true → spawn in random valid tiles.
            ENEMY_COUNT defines how many enemies are created.
        */
        for (int i = 0; i < EnemyConfig.ENEMY_COUNT; i++) {
            float[] pos = getRandomSpawn();
            enemies.add(new EnemyDrone(pos[0], pos[1], tiledMap, player));
        }

        player.show();
        for (EnemyDrone enemy : enemies) {
            enemy.show();
        }

        score = new Score();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        GameApp.clearScreen();
        GameApp.startShapeRenderingFilled();
        GameApp.startSpriteRendering();

        // Update the player's movement
        player.update(delta);
        score.update(delta);
        for (EnemyDrone enemyDrone : enemies) {
            enemyDrone.update(delta, enemies);
        }

        // Camera follows the player
        camara.position.set(player.getX(), player.getY(), 0);
        camara.update();

        // Render the map using the camera
        mapRenderer.setView(camara);
        mapRenderer.render();

        // Render player + enemies using shape renderer
        GameApp.getShapeRenderer().setProjectionMatrix(camara.combined);

        player.render();
        score.render();

        for (EnemyDrone enemyDrone : enemies) {
            enemyDrone.render();
        }

        GameApp.endSpriteRendering();
        GameApp.endShapeRendering();
    }

    @Override
    public void hide() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }

    /*
        Picks a random tile inside the map that is NOT a wall.
        It checks the "collision" layer:
            - If the tile has no "blocked" property → it's walkable.
            - Converts tile coordinates to world coordinates by multiplying by 16.
    */
    private float[] getRandomSpawn() {

        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("collision");

        if (layer == null) {
            System.out.println("ERROR: collision layer not found!");
            return new float[]{0, 0};
        }

        int mapWidth = layer.getWidth();
        int mapHeight = layer.getHeight();

        while (true) {
            // Pick a random tile on the map
            int tileX = (int) GameApp.random(0, mapWidth - 1);
            int tileY = (int) GameApp.random(0, mapHeight - 1);

            TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

            /*
                Tile is valid IF:
                    - It exists AND
                    - It is not marked as "blocked"
             */
            if (cell == null ||
                    cell.getTile() == null ||
                    !cell.getTile().getProperties().containsKey("blocked")) {

                // Convert tile position → world position
                float worldX = tileX * 16;
                float worldY = tileY * 16;

                return new float[]{worldX, worldY};
            }
        }
    }
}
