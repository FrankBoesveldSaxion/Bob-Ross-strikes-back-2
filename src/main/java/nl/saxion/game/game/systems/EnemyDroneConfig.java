package nl.saxion.game.game.systems;

public class EnemyDroneConfig {

    // How many enemies should spawn in the world?
    public static int ENEMY_COUNT = 5;

    // Movement speed of each enemy (pixels per second)
    public static float ENEMY_SPEED = 30f;

    // If true, enemies spawn at random valid locations on the map
    public static boolean RANDOM_SPAWN = true;

    public static float PUSH_RADIUS = 5f;

    public static int HEALTH = 1;
    public static int SCORE_INCREASE_WHEN_DEAD = 10;

    /*
        If RANDOM_SPAWN = false,
        the spawner will use these coordinates instead.

        Each entry in the array is:
            [ x-position , y-position ]

        You can add or remove spawn points freely.
    */
    public static float[][] FIXED_SPAWNS = {
            {350, 160},
            {500, 200},
            {100, 300}
    };
}
