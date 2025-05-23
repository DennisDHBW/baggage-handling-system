package baggage;

import lombok.*;
import utility.Utility;

import java.time.LocalDate;

@Builder
@Data
public class BaggageTag {
    String airportCodeFrom;
    String airportCodeTo;
    LocalDate flightDate;
    Baggage baggage;

    @Builder.Default
    String barcode = Utility.generateRandomBarcode();

    public String toString() {
        return "BaggageTag";
    }

}
