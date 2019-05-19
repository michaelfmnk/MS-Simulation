package michaelfmnk.ms.core.components.cpu;

import michaelfmnk.ms.core.components.MsBuffer;
import michaelfmnk.ms.core.components.MsEntity;

import java.time.Duration;

public class BufferingMsCpu extends MsCpu {
    private final MsBuffer<MsEntity> msBuffer = new MsBuffer<>();

    public BufferingMsCpu(Duration min, Duration max) {
        super(min, max);
    }

    @Override
    public synchronized void submit(MsEntity msEntity) {
        msBuffer.submit(msEntity);
    }

    public MsEntity pollAndCompute() {
        MsEntity msEntity = msBuffer.await();
        computingDelay();
        return msEntity;
    }
}
