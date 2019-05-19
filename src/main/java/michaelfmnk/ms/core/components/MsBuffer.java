package michaelfmnk.ms.core.components;

import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MsBuffer<T> {
    private final LinkedBlockingQueue<T> entitiesQueue = new LinkedBlockingQueue<>();

    public void submit(T msEntity) {
        entitiesQueue.add(msEntity);
    }

    @SneakyThrows
    public T await() {
        return entitiesQueue.poll(10, TimeUnit.SECONDS);
    }
}
