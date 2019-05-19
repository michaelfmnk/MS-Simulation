package michaelfmnk.ms.core.components.channel;

import lombok.extern.log4j.Log4j2;
import michaelfmnk.ms.core.components.MsBuffer;
import michaelfmnk.ms.core.components.MsEntity;

import java.time.Duration;

@Log4j2
public class BufferingMsChannel extends MsChannel {
    private final MsBuffer<MsEntity> channelBuffer = new MsBuffer<>();

    public BufferingMsChannel(Duration min, Duration max) {
        super(min, max);
    }

    public synchronized void submit(MsEntity msEntity) {
        channelBuffer.submit(msEntity);
    }

    public MsEntity pollAndCompute() {
        MsEntity msEntity = channelBuffer.await();
        log.info("{} being transmitted and processed", msEntity.getLoggingPrefix());
        computingDelay();
        log.info("{} finished.", msEntity.getLoggingPrefix());
        return msEntity;
    }
}
