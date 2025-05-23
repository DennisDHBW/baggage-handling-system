package scanner;

import baggage.Baggage;
import baggage.BaggageTag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckInScanner implements IScanner {

    public BaggageTag scan(Baggage baggage) {
        if (baggage == null) {
            log.warn("Baggage is null");
            return null;
        }
        return baggage.getBaggageTag();
    }

}
