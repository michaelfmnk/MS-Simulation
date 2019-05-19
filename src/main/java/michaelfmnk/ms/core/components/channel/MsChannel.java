package michaelfmnk.ms.core.components.channel;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import michaelfmnk.ms.core.components.MsEntity;

import java.time.Duration;

@Log4j2
@RequiredArgsConstructor
public class MsChannel {
    protected final Duration min;
    protected final Duration max;

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
