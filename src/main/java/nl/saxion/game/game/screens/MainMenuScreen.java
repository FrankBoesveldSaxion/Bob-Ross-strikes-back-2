package nl.saxion.game.game.screens;

import com.badlogic.gdx.Input;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class MainMenuScreen extends ScalableGameScreen {

    // Menu options
    private final String[] options = {
            "Start Game",
            "Quit"
    };

    // Which option is currently selected
    private int selectedIndex = 0;

    public MainMenuScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {
        GameApp.addMusic("Start-Music", "audio/begin-scherm.mp3");
        GameApp.playMusic("Start-Music", true);
        GameApp.addFont("basic", "fonts/basic.ttf", 60);
        GameApp.addTexture("menuBg", "textures/achtergrond_mainmenu.jpg");

        selectedIndex = 0; // reset selection when menu opens
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // Move selection up
        if (GameApp.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = options.length - 1;
            }
        }

        // Move selection down
        if (GameApp.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= options.length) {
                selectedIndex = 0;
            }
        }

        // Confirm selection
        if (GameApp.isKeyJustPressed(Input.Keys.ENTER)) {
            if (selectedIndex == 0) {
                // Start Game
                GameApp.switchScreen("WorldMap");
            }
            else if (selectedIndex == 1) {
                // Quit Game
                System.exit(0);
            }
        }

        // --- RENDERING ---

        GameApp.clearScreen("black");
        GameApp.startSpriteRendering();

        GameApp.drawTexture(
                "menuBg",
                0,
                0,
                getWorldWidth(),
                getWorldHeight()
        );


        // Draw menu options
        for (int i = 0; i < options.length; i++) {
            boolean selected = i == selectedIndex;

            String color = selected ? "white" : "gray-400";
            float x = getWorldWidth() / 2f;
            float y = getWorldHeight() * 0.45f - i * 80;

            String text = selected ? "> " + options[i] + " <" : options[i];

            // 1. zwarte 'outline' (schaduw) iets verschoven
            GameApp.drawTextCentered("basic", text,
                    x + 2,      // kleine offset rechts
                    y - 2,      // kleine offset naar beneden
                    "black");

            // 2. normale tekst erboven
            GameApp.drawTextCentered("basic", text,
                    x,
                    y,
                    color);
        }



        GameApp.endSpriteRendering();
    }

    @Override
    public void hide() {
        GameApp.disposeFont("basic");
        GameApp.disposeTexture("menuBg");
        GameApp.stopMusic("Start-Music");
        GameApp.disposeMusic("Start-Music");
    }
}
