package michaelfmnk.ms.core.components.cpu;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import michaelfmnk.ms.core.components.MsEntity;

import java.time.Duration;

@Log4j2
@RequiredArgsConstructor
public class MsCpu {
    private final Duration min;
    private final Duration max;

    public synchronized void submit(MsEntity msEntity) {
        log.info("{} being processed in cpu", msEntity.getLoggingPrefix());
        computingDelay();
    }

    @SneakyThrows
    protected void computingDelay() {
        double awaitingTime = Math.random() * max.toMillis() + min.toMillis();
        Thread.sleep((long) awaitingTime);
    }

}
