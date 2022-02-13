package controller;

import connector.clientServer.Server;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TimeTest {
    Thread controllerThread;
    private final Logger LOGGER = Logger.getLogger(Server.class.getName());

    @Test
    public void should_calculate_how_long_human_waits() {
        // given
        Controller controller = new Controller();
        controller.setGameSpeed(10);
        controllerThread = new Thread(controller::start);

        // when
        controllerThread.start();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException ignore) {
        }

        // then
        controller.stop();
        LOGGER.info("Wait inside average time = " + controller.getAverageTimeCustomerToWaitInside() / 1000);
        LOGGER.info("Wait outside average time = " + controller.getAverageTimeCustomerToWaitOutside() / 1000);
    }
}
