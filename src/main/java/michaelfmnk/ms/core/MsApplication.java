package michaelfmnk.ms.core;

import michaelfmnk.ms.core.components.MsInput;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MsApplication {

    public void run() {
        MsSystem msSystem = MsSystem.configure()
                .channelMin(Duration.ofMillis(150))
                .channelMax(Duration.ofMillis(150))
                .cpuMin(Duration.ofMillis(70))
                .cpuMax(Duration.ofMillis(70))
                .inputs(createInputs(2))
                .simulationDuration(Duration.ofSeconds(10))
                .entitiesLimit(4)
                .build();

        try {
            msSystem.start();
        } catch (InterruptedException e) {
            System.err.println("failed due to " + e.getMessage());
        }
    }

    private List<MsInput> createInputs(int number) {
        List<MsInput> msInputs = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            msInputs.add(i, new MsInput(Duration.ofMillis(140), Duration.ofMillis(160)));
        }
        return msInputs;
    }
}
