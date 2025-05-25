package baggage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Baggage {
    private String id;
    private String origin;
    private String destination;
    private double weight;
}

