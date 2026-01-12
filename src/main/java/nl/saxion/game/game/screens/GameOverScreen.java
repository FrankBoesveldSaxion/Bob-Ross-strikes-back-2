package nl.saxion.game.game.screens;

import com.badlogic.gdx.Input;
import nl.saxion.game.game.systems.GameState;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class GameOverScreen extends ScalableGameScreen {
    public GameOverScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {
        GameApp.addFont("basic", "fonts/basic.ttf", 50);
        GameApp.addSound("Game-Over", "audio/game-over.mp3");
        GameApp.playSound("Game-Over");
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // If user presses ENTER, return to main menu
        if (GameApp.isKeyJustPressed(Input.Keys.ENTER)) {
            GameApp.switchScreen("MainMenuScreen");
        }

        // Draw game over screen
        GameApp.clearScreen("black");
        GameApp.startSpriteRendering();

        // Big GAME OVER title
        GameApp.drawTextCentered("basic", "GAME OVER", getWorldWidth()/2, getWorldHeight()/2 + 90, "red-600");

        // Display final score
        GameApp.drawTextCentered(
                "basic",
                "Your score: " + GameState.score,
                getWorldWidth() / 2,
                getWorldHeight() / 2,
                "white"
        );


        // Display time
        int time = (int) GameState.time;
        GameApp.drawTextCentered(
                "basic",
                "Your time: " + time,
                getWorldWidth() / 2,
                getWorldHeight() / 2 - 40,
                "white"
        );

        // Small instructions
        GameApp.drawTextCentered("basic", "Press Enter to return to Main Menu", getWorldWidth()/2, getWorldHeight()/2 - 100, "white");

        GameApp.endSpriteRendering();
    }

    @Override
    public void hide() {
        GameApp.disposeFont("basic");
    }
}
