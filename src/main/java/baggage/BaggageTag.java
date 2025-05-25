package baggage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaggageTag {
    private String code;
    private Baggage baggage;
}
