package main;

import baggage.Baggage;
import baggage.BaggageTag;
import lombok.extern.slf4j.Slf4j;
import scanner.CheckInScanner;
import shared.Location;
import utility.Utility;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Application {
    public static void main(String[] args) {

        Map<String, Location> baggageLocation = new HashMap<>();
        CheckInScanner scanner = new CheckInScanner();

        // create baggage with baggage tag
        Baggage baggage = Baggage.builder()
                .volume(60)
                .weight(25)
                .build();
        BaggageTag baggageTag = BaggageTag.builder()
                .airportCodeFrom("FRA")
                .airportCodeTo("SIN")
                .flightDate(LocalDate.parse("2025-05-23"))
                .barcode(Utility.generateRandomBarcode())
                .build();
        baggage.setBaggageTag(baggageTag);

        BaggageTag scannedBaggageTag = scanner.scan(baggage);
        baggageLocation.put(scannedBaggageTag.getBarcode(), Location.CHECK_IN);
        log.info(baggageLocation.toString());
    }
}