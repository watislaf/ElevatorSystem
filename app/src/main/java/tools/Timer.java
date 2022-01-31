package tools;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Timer {
    private long current_time;
    private long counter_time;

    public void tick(long deltaTime) {
        current_time -= deltaTime;
    }


    public boolean isReady() {
        return current_time <= 0;
    }

    public void restart(long timeToCount) {
        current_time = timeToCount;
        counter_time = timeToCount;
    }

    public double getPercent() {
        if (current_time <= 0) {
            return 0;
        }
        return current_time * 1. / counter_time;
    }
}
