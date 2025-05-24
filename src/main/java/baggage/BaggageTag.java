package baggage;

import lombok.Builder;
import lombok.Data;
import shared.AirportCode;
import utility.Utility;

import java.time.LocalDateTime;

@Builder
@Data
public class BaggageTag {

    private AirportCode from;
    private AirportCode to;
    private LocalDateTime flightDateTimeFrom;
    private LocalDateTime flightDateTimeTo;

    @Builder.Default
    private String barcode = Utility.generateRandomBarcode();
}
