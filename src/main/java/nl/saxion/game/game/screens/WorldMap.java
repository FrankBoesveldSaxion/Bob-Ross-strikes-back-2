package nl.saxion.game.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import nl.saxion.game.game.entities.Player;
import nl.saxion.game.game.entities.Score;
import nl.saxion.game.game.systems.DifficultySystem;
import nl.saxion.game.game.systems.TimerSystem;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;
import nl.saxion.game.game.entities.EnemyDrone;

import java.util.ArrayList;

public class WorldMap extends ScalableGameScreen {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Player player;
    private Score score;
    private DifficultySystem difficulty;
    private TimerSystem timerSystem;


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
        difficulty = new DifficultySystem();
        timerSystem = new TimerSystem();
        timerSystem.initTimer();

        // Hide the collision layer so the player doesn't see it
        tiledMap.getLayers().get("collision").setVisible(false);

        player = new Player(320, 160, tiledMap);
        player.show();

        camara = new OrthographicCamera();
        camara.setToOrtho(false, 320, 180);

        score = new Score();
        score.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        GameApp.clearScreen();
        GameApp.startShapeRenderingFilled();
        GameApp.startSpriteRendering();

        timerSystem.timerLogic(delta);

        camara.position.set(player.getX(), player.getY(), 0);
        camara.update();

        mapRenderer.setView(camara);
        mapRenderer.render();

        GameApp.getShapeRenderer().setProjectionMatrix(camara.combined);

        player.render(delta);

        difficulty.spawnEnemiesBasedOnScore(
                delta,
                timerSystem,
                tiledMap,
                player,
                enemies,
                score
        );

        GameApp.endSpriteRendering();
        GameApp.endShapeRendering();

        GameApp.startSpriteRendering();
        score.render(delta);
        GameApp.endSpriteRendering();
    }


    @Override
    public void hide() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }

}
