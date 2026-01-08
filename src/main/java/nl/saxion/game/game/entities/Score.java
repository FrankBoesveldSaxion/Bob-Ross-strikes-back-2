package nl.saxion.game.game.entities;

import com.badlogic.gdx.graphics.Color;
import nl.saxion.game.game.systems.GameState;
import nl.saxion.gameapp.GameApp;

import javax.xml.parsers.SAXParser;

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

    public void render(float delta, float rightWith) {

        GameState.updateScore(delta);

        int boardY = GameApp.getWindowHeight() + 75;

        GameApp.drawTexture("scoreBoard", rightWith - 15, boardY, 275, 200);
        GameApp.drawTextCentered("basicSmall", "Score:", rightWith + 90, boardY + 125, Color.CYAN);
        GameApp.drawTextCentered("cyberpunk", String.valueOf(GameState.score), rightWith + 140, boardY + 80, Color.CYAN);
    }
}
