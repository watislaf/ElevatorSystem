package tools;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
public class Timer {
    private long current_time;

    public void tick(long deltaTime) {
        current_time -= deltaTime;
    }


    public boolean isReady() {
        return current_time <= 0;
    }

    public void restart(long timeToCount) {
        current_time = timeToCount;
    }
}
