package tools;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Timer {
    private final long TIME_TO_COUNT;
    private long current_time;

    public void tick(long deltaTime) {
        current_time -= deltaTime;
    }

    public boolean isReady() {
        return current_time <= 0;
    }

    public void restart() {
        current_time = TIME_TO_COUNT;
    }
}
