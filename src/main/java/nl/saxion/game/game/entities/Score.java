package nl.saxion.game.game.entities;

import com.badlogic.gdx.graphics.Color;
import nl.saxion.gameapp.GameApp;

public class Score {

    int positionX = GameApp.getWindowWidth() + 300;
    int positionY = GameApp.getWindowHeight() + 150;
    int score = 0;

    float timeSinceLastAdd = 0f;

    public void show() {
        GameApp.addFont("cyberpunk", "fonts/Sefa.ttf", 75);
        GameApp.addTexture("scoreBoard", "textures/scoreBoard.png");
        GameApp.addFont("basicSmall", "fonts/basic.ttf", 50);

        positionX = GameApp.getWindowWidth() + 250;
        positionY = GameApp.getWindowHeight() + 150;
    }

    public void render(float delta, float rightWith) {

        timeSinceLastAdd += delta;
        if (timeSinceLastAdd >= 1f) {
            increaseScoreBy(1);
            timeSinceLastAdd -= 1f;
        }

        int boardY = GameApp.getWindowHeight() + 75;

        GameApp.drawTexture("scoreBoard", rightWith - 15, boardY, 275, 200);
        GameApp.drawTextCentered("basicSmall", "Score:", rightWith + 90, boardY + 125, Color.CYAN);
        GameApp.drawTextCentered("cyberpunk", String.valueOf(score), rightWith + 140, boardY + 80, Color.CYAN);
    }

    public void increaseScoreBy(int increase) {
        score += increase;
    }

    public int getScore() {
        return score;
    }

}
