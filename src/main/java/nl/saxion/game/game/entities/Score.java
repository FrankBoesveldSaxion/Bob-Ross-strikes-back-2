package nl.saxion.game.game.entities;

import com.badlogic.gdx.graphics.Color;
import nl.saxion.game.game.systems.GameState;
import nl.saxion.gameapp.GameApp;

public class Score {

    int positionX = GameApp.getWindowWidth() + 300;
    int positionY = GameApp.getWindowHeight() + 150;

    public void show() {
        GameApp.addFont("cyberpunk", "fonts/Sefa.ttf", 75);
        GameApp.addTexture("scoreBoard", "textures/scoreBoard.png");
        GameApp.addFont("basicSmall", "fonts/basic.ttf", 50);

        positionX = GameApp.getWindowWidth() + 250;
        positionY = GameApp.getWindowHeight() + 150;
    }

    public void render(float delta, float rightWith, float virtualHeight) {
        float height = virtualHeight - 200;
        GameState.updateScore(delta);

        GameApp.drawTexture("scoreBoard", rightWith - 15, height, 275, 200);
        GameApp.drawTextCentered("basicSmall", "Score:", rightWith + 90, height + 125, Color.CYAN);
        GameApp.drawTextCentered("cyberpunk", String.valueOf(GameState.score), rightWith + 140, height + 80, Color.CYAN);
    }
}
