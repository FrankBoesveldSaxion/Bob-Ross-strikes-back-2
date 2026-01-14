
package nl.saxion.game.game.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import nl.saxion.game.game.systems.SpriteConfig;
import nl.saxion.gameapp.GameApp;

import java.util.ArrayList;

import static nl.saxion.game.game.systems.CollisionSystem.isCollision;

public class Player {
    private float x;
    private float y;
    private final TiledMap map;

    // 1 = left, 2 = right
    private int currentDirection = 2;

    // Cooldown voor de volgende aanval
    private float attackCooldown = 0f;

    // Enemies referentie (voor damage tijdens slash)
    private ArrayList<EnemyDrone> enemies;

    // --- Animatie & states ---
    private enum PlayerState { IDLE, RUNNING, ATTACK_TRANSITION, ATTACK_SLASH }
    private PlayerState state = PlayerState.IDLE;

    public float attackCooldown = 0f; // time left until next attack
    private String currentAnimationKey = "";

    // Keys per fase
    private String transitionAnimationKey = ""; // stand->slash of run->slash (L/R)
    private String slashAnimationKey = "";      // daadwerkelijke slag (L/R)

    // Damage valt één keer per aanval, op het hit-frame
    private boolean attackDamageApplied = false;
    private int slashHitFrameIndex = 0; // dynamisch bepaald op start van de aanval

    public Player(float startX, float startY, TiledMap map) {
        this.x = startX;
        this.y = startY;
        this.map = map;
    }

    public void setEnemies(ArrayList<EnemyDrone> enemies) {
        this.enemies = enemies;
    }

    public void show() {
        GameApp.addSound("Attack-Sound", "audio/attack-sound.mp3");
        GameApp.addSpriteSheet("bobWalkLeft", "textures/animations/Player/bobRossRunAnimationLeftRun.png", SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobWalkLeft", "bobWalkLeft", SpriteConfig.FRAME_DURATION, true);
        // --- Lopen ---
        GameApp.addSpriteSheet("bobWalkLeft",
                "textures/animations/Player/bobRossRunAnimationLeftRun.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobWalkLeft", "bobWalkLeft",
                SpriteConfig.FRAME_DURATION, true);

        GameApp.addSpriteSheet("bobWalkRight",
                "textures/animations/Player/bobRossRunAnimationRightRun.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobWalkRight", "bobWalkRight",
                SpriteConfig.FRAME_DURATION, true);

        // --- Stop-run (uitloop als je net stopt) ---
        GameApp.addSpriteSheet("bobStopRunLeft",
                "textures/animations/Player/BobRossRunToBaseLeft.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobStopRunLeft", "bobStopRunLeft",
                SpriteConfig.FRAME_DURATION, false);

        GameApp.addSpriteSheet("bobStopRunRight",
                "textures/animations/Player/BobRossRunToBaseRight.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobStopRunRight", "bobStopRunRight",
                SpriteConfig.FRAME_DURATION, false);

        // --- Overgang: RUN -> SLASH ---
        GameApp.addSpriteSheet("bobRunToSlashLeft",
                "textures/animations/Player/BobRossRunningToSlashLeft.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobRunToSlashLeft", "bobRunToSlashLeft",
                SpriteConfig.FRAME_DURATION, false);

        GameApp.addSpriteSheet("bobRunToSlashRight",
                "textures/animations/Player/BobRossRunningToSlashRight.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobRunToSlashRight", "bobRunToSlashRight",
                SpriteConfig.FRAME_DURATION, false);

        // --- Overgang: STAND -> SLASH ---
        GameApp.addSpriteSheet("bobStandToSlashLeft",
                "textures/animations/Player/BobRossStandToSlashLeft.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobStandToSlashLeft", "bobStandToSlashLeft",
                SpriteConfig.FRAME_DURATION, false);

        GameApp.addSpriteSheet("bobStandToSlashRight",
                "textures/animations/Player/BobRossStandToSlashRight.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobStandToSlashRight", "bobStandToSlashRight",
                SpriteConfig.FRAME_DURATION, false);

        // --- De daadwerkelijke SLASH (impact) ---
        GameApp.addSpriteSheet("bobSlashLeft",
                "textures/animations/Player/BobRossSlashLeft.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobSlashLeft", "bobSlashLeft",
                SpriteConfig.FRAME_DURATION, false);

        GameApp.addSpriteSheet("bobSlashRight",
                "textures/animations/Player/BobRossSlashRight.png",
                SpriteConfig.FRAME_WIDTH, SpriteConfig.FRAME_HEIGHT);
        GameApp.addAnimationFromSpritesheet("bobSlashRight", "bobSlashRight",
                SpriteConfig.FRAME_DURATION, false);
    }

    private boolean wasMoving = false;

    public void render(float delta) {

        // Cooldown aftellen (na render logica; maakt niet uit, zolang je per frame aftelt)
        if (attackCooldown > 0f) {
            attackCooldown -= delta;
        }

        // Lees input
        float newX = x;
        float newY = y;
        boolean isMoving = false;
        float speed = 100f;

        // Tijdens ATTACK_* locken we beweging voor stabiele animatie (optioneel, voelt het best)
        boolean movementLocked = (state == PlayerState.ATTACK_TRANSITION || state == PlayerState.ATTACK_SLASH);

        if (!movementLocked) {
            if (GameApp.isKeyPressed(Input.Keys.W)) { newY += speed * delta; isMoving = true; }
            if (GameApp.isKeyPressed(Input.Keys.S)) { newY -= speed * delta; isMoving = true; }
            if (GameApp.isKeyPressed(Input.Keys.A)) { newX -= speed * delta; isMoving = true; currentDirection = 1; }
            if (GameApp.isKeyPressed(Input.Keys.D)) { newX += speed * delta; isMoving = true; currentDirection = 2; }
        }

        // Spatie: start aanval alleen als cooldown klaar is en we niet al aan het aanvallen zijn
        if (GameApp.isKeyJustPressed(Input.Keys.SPACE) && canAttack()) {
            startAttack(isMoving);
        }

        // --- Kies & teken animaties op basis van state ---
        switch (state) {
            case ATTACK_TRANSITION -> {
                currentAnimationKey = transitionAnimationKey;
                GameApp.updateAnimation(currentAnimationKey);
                GameApp.drawAnimation(currentAnimationKey, x - 15, y - 5, 32f, 32f);

                // Is de overgang klaar? Doorzetten naar echte slash
                if (GameApp.isAnimationFinished(currentAnimationKey)) {
                    GameApp.resetAnimation(currentAnimationKey); // klaar voor volgende keer
                    GameApp.resetAnimation(slashAnimationKey);   // begin de slash op frame 0
                    state = PlayerState.ATTACK_SLASH;
                }
            }

            case ATTACK_SLASH -> {
                currentAnimationKey = slashAnimationKey;
                GameApp.updateAnimation(currentAnimationKey);
                GameApp.drawAnimation(currentAnimationKey, x - 15, y - 5, 32f, 32f);

                // Impact-moment: één keer damage toepassen op het hit-frame
                int currentFrame = GameApp.getAnimationCurrentFrameIndex(currentAnimationKey);
                if (!attackDamageApplied && currentFrame >= slashHitFrameIndex) {
                    mainAttack();                 // valt de klap
                    attackDamageApplied = true;   // slechts één keer
                    attackCooldown = SpriteConfig.ATTACK_MAIN_COOLDOWN_TIME;
                }

                // Slash klaar → terug naar bewegen/idle
                if (GameApp.isAnimationFinished(currentAnimationKey)) {
                    GameApp.resetAnimation(currentAnimationKey);
                    state = (GameApp.isKeyPressed(Input.Keys.W) ||
                            GameApp.isKeyPressed(Input.Keys.A) ||
                            GameApp.isKeyPressed(Input.Keys.S) ||
                            GameApp.isKeyPressed(Input.Keys.D))
                            ? PlayerState.RUNNING : PlayerState.IDLE;
                }
            }

            case RUNNING -> {
                currentAnimationKey = (currentDirection == 1) ? "bobWalkLeft" : "bobWalkRight";
                GameApp.updateAnimation(currentAnimationKey);
                GameApp.drawAnimation(currentAnimationKey, x - 15, y - 5, 32f, 32f);
                wasMoving = true;
            }

            case IDLE -> {
                // Net gestopt met lopen → korte uitloop
                currentAnimationKey = (currentDirection == 1) ? "bobStopRunLeft" : "bobStopRunRight";
                if (wasMoving) {
                    GameApp.resetAnimation(currentAnimationKey);
                    wasMoving = false;
                }
                GameApp.updateAnimation(currentAnimationKey);
                GameApp.drawAnimation(currentAnimationKey, x - 15, y - 5, 32f, 32f);
            }
        }

        // Positie alleen aanpassen als geen collision (en niet movement-locked)
        if (!movementLocked && !isCollision(newX, newY, map)) {
            x = newX;
            y = newY;
        }

        // State bijwerken als we niet aan het aanvallen zijn
        if (state != PlayerState.ATTACK_TRANSITION && state != PlayerState.ATTACK_SLASH) {
            state = isMoving ? PlayerState.RUNNING : PlayerState.IDLE;
        }
    }

    private void startAttack(boolean wasMovingWhenTriggered) {
        // Kies overgangsanimatie o.b.v. richting + bewegen
        if (wasMovingWhenTriggered) {
            transitionAnimationKey = (currentDirection == 1) ? "bobRunToSlashLeft" : "bobRunToSlashRight";
        } else {
            transitionAnimationKey = (currentDirection == 1) ? "bobStandToSlashLeft" : "bobStandToSlashRight";
        }

        // Kies slash-animatie o.b.v. richting
        slashAnimationKey = (currentDirection == 1) ? "bobSlashLeft" : "bobSlashRight";

        // Reset en start overgang
        GameApp.resetAnimation(transitionAnimationKey);
        state = PlayerState.ATTACK_TRANSITION;
        attackDamageApplied = false;

        // Dynamisch impact-frame bepalen: midden van de slash-animatie (veilig default)
        int slashFrames = GameApp.getAnimationFrameCount(slashAnimationKey);
        slashHitFrameIndex = Math.max(0, Math.min(slashFrames - 1, slashFrames / 2));
    }

    public void mainAttack() {
        if (enemies == null) return;
        GameApp.playSound("Attack-Sound", 0.5f);


        for (EnemyDrone enemyDrone : enemies) {
            float distance = calculateDistance(x, y, enemyDrone.getX(), enemyDrone.getY());
            float attackRange = SpriteConfig.ATTACK_RANGE;
            if (distance <= attackRange) {
                enemyDrone.takeDamage(1);
            }
        }
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public boolean canAttack() {
        // Niet opnieuw aanvallen tijdens overgang/slash
        return attackCooldown <= 0f
                && state != PlayerState.ATTACK_TRANSITION
                && state != PlayerState.ATTACK_SLASH;
    }

    public float getAttackCooldown(){
        return attackCooldown;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
