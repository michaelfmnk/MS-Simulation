package michaelfmnk.ms.core;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import michaelfmnk.ms.core.components.MsEntity;
import michaelfmnk.ms.core.components.MsInput;
import michaelfmnk.ms.core.components.channel.BufferingMsChannel;
import michaelfmnk.ms.core.components.cpu.BufferingMsCpu;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@Log4j2
public class MsSystem {
    private final Integer entitiesLimit;
    private final ConcurrentHashMap<Integer, AtomicInteger> entitiesCountPerChannel = new ConcurrentHashMap<>();

    private final BufferingMsCpu computingCpu;
    private final Duration simulationDuration;

    private final List<MsInput> inputs;
    private final List<BufferingMsChannel> computingChannels = new ArrayList<>();

    @Builder(builderMethodName = "configure")
    public MsSystem(Integer entitiesLimit, Duration cpuMin, Duration cpuMax,
                    Duration channelMin, Duration channelMax,
                    Duration simulationDuration, List<MsInput> inputs) {
        this.entitiesLimit = entitiesLimit;
        this.simulationDuration = simulationDuration;
        this.computingCpu = new BufferingMsCpu(cpuMin, cpuMax);
        this.inputs = inputs;
        createChannels(inputs.size(), channelMin, channelMax);


        for (int i = 0; i < inputs.size(); i++) {
            entitiesCountPerChannel.put(i, new AtomicInteger(0));
        }

    }

    private List<BufferingMsChannel> createChannels(Integer count, Duration channelMin, Duration channelMax) {
        for (int i = 0; i < count; i += 1) {
            computingChannels.add(i, new BufferingMsChannel(channelMin, channelMax));
        }
        return computingChannels;
    }

    public void start() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(inputs.size() * 3);

        startChannels(executorService);
        startCpu(executorService);
        startInputs(executorService);

        Thread.sleep(simulationDuration.toMillis());
        executorService.shutdownNow();
    }

    private void startChannels(ExecutorService executorService) {
        for (BufferingMsChannel channel : computingChannels) {
            executorService.submit(() -> {
                while (true) {
                    MsEntity msEntity = channel.pollAndCompute();
                    entitiesCountPerChannel.get(msEntity.getSourceId()).decrementAndGet();
                }
            });
        }
    }

    private void startCpu(ExecutorService executorService) {
        executorService.submit(() -> {
            while (true) {
                MsEntity computedEntity = computingCpu.pollAndCompute();
                computingChannels.get(computedEntity.getSourceId()).submit(computedEntity);
            }
        });
    }

    private void startInputs(ExecutorService executorService) {
        for (int inputIndex = 0; inputIndex < inputs.size(); inputIndex += 1) {
            MsInput input = inputs.get(inputIndex);
            final int sourceId = inputIndex;
            executorService.submit(() -> {

                while (true) {
                    MsEntity newEntity = input.get();
                    newEntity.setSourceId(sourceId);

                    System.out.println(entitiesCountPerChannel);
                    if (entitiesCountPerChannel.get(sourceId).getAndIncrement() < entitiesLimit) {
                        log.info("{} added to input buffer", newEntity.getLoggingPrefix());
                        computingCpu.submit(newEntity);
                    } else {
                        entitiesCountPerChannel.get(sourceId).decrementAndGet();
                        log.error("{} was lost", newEntity.getLoggingPrefix());
                    }
                }
            });
        }
    }

}
