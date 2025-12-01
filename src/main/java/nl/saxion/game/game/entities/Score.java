package nl.saxion.game.game.entities;

import com.badlogic.gdx.graphics.Color;
import nl.saxion.gameapp.GameApp;

public class Score {

    int positionX = GameApp.getWindowWidth() + 300;
    int positionY = GameApp.getWindowHeight() + 150;
    int score = 0;

    float timeSinceLastAdd = 0f;

    public void update(float delta) {
        // waits until 1 second has passed then update the score.
        timeSinceLastAdd += delta;
        if (timeSinceLastAdd >= 1f) {
            increaseScoreBy(1);
            timeSinceLastAdd -= 1f;
        }
    }

    public void render() {
        GameApp.drawTexture("scoreBoard", positionX - 75, positionY - 75, 275, 200);
        GameApp.setColor(Color.RED);
        GameApp.drawRect(positionX, positionY, 100, 100);

        GameApp.drawTextCentered("basicSmall", "Score:", positionX, positionY + 50, Color.CYAN);

        GameApp.drawTextCentered("cyberpunk", String.valueOf(score), positionX + 100, positionY + 20, Color.CYAN);
    }

    public void increaseScoreBy(int increase) {
        score += increase;
    }

    public int getScore() {
        return score;
    }

}
