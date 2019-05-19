package michaelfmnk.ms.core.components;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MsInput implements Supplier<MsEntity> {
    private final Duration min;
    private final Duration max;

    @Override
    public MsEntity get() {
        delay();
        return new MsEntity(UUID.randomUUID());
    }

    @SneakyThrows
    private void delay() {
        double awaitingTime = Math.random() * max.toMillis() + min.toMillis();
        Thread.sleep((long) awaitingTime);
    }
}
