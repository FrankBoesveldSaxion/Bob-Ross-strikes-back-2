package nl.saxion.game.game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class WorldMap extends ScalableGameScreen {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Player player;
    private OrthographicCamera camara;

    public WorldMap() {
        super(1280, 720);
    }

    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load("maps/test/testMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        camara = new OrthographicCamera();
        camara.setToOrtho(false, 320,  180);

        //makes the collision layer invisible.
        tiledMap.getLayers().get("collision").setVisible(false);

        player = new Player(320, 160, tiledMap);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        GameApp.clearScreen();

        player.update(delta);

        camara.position.set(player.getX(), player.getY(), 0);
        camara.update();

        mapRenderer.setView(camara);
        mapRenderer.render();

        GameApp.startShapeRenderingFilled();
        GameApp.getShapeRenderer().setProjectionMatrix(camara.combined);
        player.render();
        GameApp.endShapeRendering();
    }


    @Override
    public void hide() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }
}
