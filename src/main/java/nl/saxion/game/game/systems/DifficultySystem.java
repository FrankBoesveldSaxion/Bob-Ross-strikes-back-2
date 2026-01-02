package nl.saxion.game.game.systems;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import nl.saxion.game.game.entities.EnemyDrone;
import nl.saxion.game.game.entities.Player;
import nl.saxion.game.game.entities.Score;
import nl.saxion.gameapp.GameApp;
import java.util.ArrayList;

public class DifficultySystem {

    private boolean spawnedOnce = false;

    public void spawnEnemiesBasedOnScore(
            float delta,
            TimerSystem time,
            TiledMap tiledMap,
            Player player,
            ArrayList<EnemyDrone> enemies,
            Score score
    ) {
        // every 1 enemy based on de config.
        int enemyDroneSpawnPerSecond = EnemyDroneConfig.ENEMY_SPAWN_PER_SECOND;
        // increase difficulty after 25 seconds of playing.
        if (time.getTime() == 25){
            enemyDroneSpawnPerSecond = EnemyDroneConfig.ENEMY_SPAWN_PER_SECOND / 2;
        }

        if (time.getTime() % enemyDroneSpawnPerSecond == 0) {
            if (!spawnedOnce) {
                spawnedOnce = true; // prevents more than 1 spawn

                float[] pos = getRandomSpawn(tiledMap);
                EnemyDrone enemy = new EnemyDrone(pos[0], pos[1], tiledMap, player);
                enemy.show();
                enemies.add(enemy);
            }
        } else {
            spawnedOnce = false;
        }

        //Render enemies
        for (EnemyDrone enemy : enemies) {
            enemy.render(delta, enemies);
        }

        //Handle death
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).isDead()) {
                enemies.remove(i);
                score.increaseScoreBy(EnemyDroneConfig.SCORE_INCREASE_WHEN_DEAD);
            }
        }

        // Update player reference
        player.setEnemies(enemies);
    }


    private float[] getRandomSpawn(TiledMap tiledMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("collision");

        if (layer == null) {
            System.out.println("ERROR: collision layer not found!");
            return new float[]{0, 0};
        }

        int mapWidth = layer.getWidth();
        int mapHeight = layer.getHeight();

        while (true) {
            int tileX = (int) GameApp.random(0, mapWidth - 1);
            int tileY = (int) GameApp.random(0, mapHeight - 1);

            TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

            if (cell == null ||
                    cell.getTile() == null ||
                    !cell.getTile().getProperties().containsKey("blocked")) {

                float worldX = tileX * 16;
                float worldY = tileY * 16;

                return new float[]{worldX, worldY};
            }
        }
    }
}

