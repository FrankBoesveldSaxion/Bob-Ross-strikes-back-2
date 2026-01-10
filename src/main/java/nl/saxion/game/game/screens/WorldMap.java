package nl.saxion.game.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import nl.saxion.game.game.entities.Player;
import nl.saxion.game.game.entities.Score;
import nl.saxion.game.game.systems.DifficultySystem;
import nl.saxion.game.game.systems.EnemyDroneConfig;
import nl.saxion.game.game.systems.GameState;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;
import nl.saxion.game.game.entities.EnemyDrone;

import java.util.ArrayList;

public class WorldMap extends ScalableGameScreen {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Player player;
    public Score score;
    private DifficultySystem difficulty;


    // Holds all enemies in the world
    private final ArrayList<EnemyDrone> enemies = new ArrayList<>();

    private OrthographicCamera camara;

    public WorldMap() {
        super(1280, 720);
    }

    @Override
    public void show() {
        // Reset enemy and score.
        enemies.clear();
        GameState.reset();

        // Load the TMX tilemap
        tiledMap = new TmxMapLoader().load("maps/NewMap/DefenitiveMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        difficulty = new DifficultySystem();

        // Hide the collision layer so the player doesn't see it
        tiledMap.getLayers().get("Collision").setVisible(false);

        player = new Player(320, 160, tiledMap);
        player.show();

        camara = new OrthographicCamera();
        camara.setToOrtho(false, 320, 180);

        score = new Score();
        score.show();

        GameApp.addFont("cooldown", "fonts/Sefa.ttf", 150);
        GameApp.addFont("attackTip", "fonts/basic.ttf", 50);
    }

    @Override
    public void render(float delta) {
        GameState.updateTime(delta);

        for (EnemyDrone enemyDrone : enemies) {
            float dx = enemyDrone.getX() - player.getX();
            float dy = enemyDrone.getY() - player.getY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist < EnemyDroneConfig.PLAYER_DEATH_DISTANCE) {
                GameApp.switchScreen("GameOverScreen");
                return; // stop rendering this frame
            }
        }

        super.render(delta);
        GameApp.clearScreen();
        GameApp.startShapeRenderingFilled();
        GameApp.startSpriteRendering();

        camara.position.set(player.getX(), player.getY(), 0);
        camara.update();

        mapRenderer.setView(camara);
        mapRenderer.render();

        GameApp.getShapeRenderer().setProjectionMatrix(camara.combined);

        player.render(delta);

        difficulty.spawnEnemiesBasedOnScore(
                delta,
                tiledMap,
                player,
                enemies
        );

        GameApp.endSpriteRendering();
        drawUI(delta);
        GameApp.endShapeRendering();
    }

    @Override
    public void hide() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }

    public void drawUI(float delta) {

        // Correct virtual resolution from ScalableGameScreen
        float virtualWidth = getViewport().getWorldWidth();
        float virtualHeight = getViewport().getWorldHeight();

        float rightWith = virtualWidth - 275;

        GameApp.startSpriteRendering();
        score.render(delta, rightWith, virtualHeight);

        float cooldown = player.attackCooldown;
        String progress;
        Color color = Color.RED;

        // show the progress based on cooldown of attack
        if (cooldown < 0.4f) {
            progress = "";
        } else if (cooldown < 1.2f) {
            progress = "+";
        } else if (cooldown < 2.0f) {
            progress = "++";
        } else {
            progress = "+++";
        }

        if (player.canAttack()) {
            // player tip.
            GameApp.drawText("attackTip", "(Press space to attack)", rightWith - 30, virtualHeight - 200, Color.WHITE);
            color = Color.GREEN;
            progress = "+++";
        }


        GameApp.drawText("cooldown", progress, rightWith + 50, virtualHeight - 260, color);


        GameApp.endSpriteRendering();
    }

}
