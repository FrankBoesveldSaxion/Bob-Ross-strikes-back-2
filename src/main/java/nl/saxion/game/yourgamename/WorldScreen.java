package nl.saxion.game.yourgamename;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class WorldScreen extends ScalableGameScreen {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Player player;
    private OrthographicCamera camara;

    public WorldScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load("maps/test/testMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        camara = new OrthographicCamera();
        camara.setToOrtho(false, 320,  180);

//        tiledMap.getLayers().get("collision").setVisible(false);

        player = new Player(320, 160, tiledMap);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        GameApp.clearScreen();

        player.update(delta);

        // camara follows plater
        camara.position.set(player.getX(), player.getY(), 0);
        camara.update();

        mapRenderer.setView(camara);
        mapRenderer.render();

        //render sprite
        GameApp.startShapeRenderingFilled();
        player.render();
        GameApp.endShapeRendering();
    }

    @Override
    public void hide() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }
}
