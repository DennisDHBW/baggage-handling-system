package baggage;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Builder
@Data
@Slf4j
public class Baggage {
    float weight;
    int volume;
    BaggageTag baggageTag;

    public void setBaggageTag(BaggageTag baggageTag) {
        if(baggageTag == null) {
            log.warn("Baggage tag is null");
            return;
        }
        this.baggageTag = baggageTag;
        baggageTag.setBaggage(this);
    }

    public String toString() {
        return "Baggage";
    }

}

