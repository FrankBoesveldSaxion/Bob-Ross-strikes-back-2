package nl.saxion.game.game.systems;

public class TimerSystem {
    public int time;
    public boolean running;
    private float accumulator;

    public void initTimer(){
        this.running = true;
        this.time = 0;
        this.accumulator = 0f;
    }

    // when dead stop timer
    public void stopTimer() {
        this.running = false;
    }

    public void timerLogic(float delta){
        if (!running) return;
        accumulator += delta;
        while (accumulator >= 1.0f) {
            time++;
            accumulator -= 1.0f;
        }
    }

    public int getTime() {
        return this.time;
    }
}