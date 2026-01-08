package nl.saxion.game.game.systems;

public class GameState {

    // Score shown to the player
    public static int score = 0;

    // Internal timer for score calculation
    private static float scoreTimer = 0f;

    public static float time = 0f;
    private static float timeTimer = 0f;

    public static void reset() {
        score = 0;
        scoreTimer = 0f;
        time = 0f;
        timeTimer = 0f;
    }

    // Call this every frame
    public static void updateScore(float delta) {
        scoreTimer += delta;

        // Every 1 second → +1 score
        if (scoreTimer >= 1f) {
            score++;
            scoreTimer -= 1f;
        }
    }

    public static void updateTime(float delta) {
        // Every 1 second → +1 time second
        timeTimer += delta;

        // Every 1 second → +1 time
        if (timeTimer >= 1f) {
            time++;
            timeTimer -= 1f;
        }
    }

    public static void increaseScoreBy(int increase) {
        score += increase;
    }
}

