package main;

import baggage.Baggage;
import baggage.BaggageTag;
import conveyorBelt.IConveyorBelt;
import conveyorBelt.IncomingConveyorBelt;
import lombok.extern.slf4j.Slf4j;
import scanner.CheckInScanner;
import scanner.IScanner;
import shared.Location;
import utility.Utility;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Application {
    public static void main(String[] args) {

        Map<String, Location> baggageLocation = new HashMap<>();
        IScanner scanner = new CheckInScanner();
        IConveyorBelt conveyorBelt = new IncomingConveyorBelt();

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

        // check-in & put conveyor belt
        BaggageTag scannedBaggageTag = scanner.scan(baggage);
        baggageLocation.put(scannedBaggageTag.getBarcode(), Location.CHECK_IN);

        // conveyor belt
        if (conveyorBelt.receiveBaggage(baggage)) {
            baggageLocation.put(scannedBaggageTag.getBarcode(), Location.INCOMING_CONVEYOR_BELT);
        }
        conveyorBelt.transferBaggage();

        //

        log.info(baggageLocation.toString());
    }
}