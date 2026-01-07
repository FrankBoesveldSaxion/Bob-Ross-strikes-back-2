package nl.saxion.game.game.systems;

public class GameState {

    // Score shown to the player
    public static int score = 0;

    // Internal timer for score calculation
    private static float scoreTimer = 0f;

    public static void reset() {
        score = 0;
        scoreTimer = 0f;
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
}

