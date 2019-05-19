package michaelfmnk.ms.core.components;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@RequiredArgsConstructor
public class MsEntity {
    private final UUID identifier;
    private Integer sourceId;

    public String getLoggingPrefix() {
        String shortIdentifier = identifier.toString().substring(identifier.toString().length() - 5);
        return System.currentTimeMillis() + ":" + shortIdentifier + ": at sourceId " + sourceId + ": ";
    }
}
