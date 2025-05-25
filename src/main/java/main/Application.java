package main;

import baggage.Baggage;
import baggage.BaggageTag;
import bufferChannel.BufferChannel;
import controlModule.ControlModule;
import conveyorBelt.DestinationBelt;
import conveyorBelt.EntryBelt;
import conveyorBelt.IConveyorBelt;
import grabber.GateArm;
import scanner.Scanner;
import scanner.IScanner;
import lombok.extern.slf4j.Slf4j;
import sorter.Sorter;

import java.util.*;

@Slf4j
public class Application {
    public static void main(String[] args) {
        ControlModule control = new ControlModule();

        // 1. Entry & first scan
        IScanner firstScanner = new Scanner("FirstScan", control);
        EntryBelt entryBelt   = new EntryBelt("EntryConveyor", control, firstScanner);

        // 2. Set up destination belts (capacity = 1 each)
        Map<String, DestinationBelt> destBeltObj = new HashMap<>();
        destBeltObj.put("LHR", new DestinationBelt("LHR", control, 1));
        destBeltObj.put("JFK", new DestinationBelt("JFK", control, 1));
        destBeltObj.put("DXB", new DestinationBelt("DXB", control, 1));

        // Create a view as IConveyorBelt for sorter & buffer
        Map<String, IConveyorBelt> destBeltConveyors = new HashMap<>();
        destBeltObj.forEach(destBeltConveyors::put);

        // 3. Buffer channel & sorter
        BufferChannel buffer = new BufferChannel(control, destBeltConveyors);
        Sorter sorter = new Sorter("MainSorter", control, destBeltConveyors, buffer);

        // 4. Security scanner (optional)
        Scanner securityScanner = new Scanner("SecurityScan", control);

        // 5. Gate arm
        GateArm gateArm = new GateArm("GateArm", control, destBeltObj);

        // Create multiple baggages
        List<BaggageTag> tags = Arrays.asList(
                BaggageTag.builder()
                        .code(UUID.randomUUID().toString())
                        .baggage(Baggage.builder()
                                .id("B1")
                                .origin("CGN")
                                .destination("LHR")
                                .weight(23.5)
                                .build())
                        .build(),
                BaggageTag.builder()
                        .code(UUID.randomUUID().toString())
                        .baggage(Baggage.builder()
                                .id("B2")
                                .origin("CGN")
                                .destination("JFK")
                                .weight(18.2)
                                .build())
                        .build(),
                BaggageTag.builder()
                        .code(UUID.randomUUID().toString())
                        .baggage(Baggage.builder()
                                .id("B3")
                                .origin("CGN")
                                .destination("DXB")
                                .weight(25.0)
                                .build())
                        .build()
        );

        Random rnd = new Random();

        // Drive each bag through the system
        for (BaggageTag tag : tags) {
            control.registerTag(tag);
            entryBelt.receive(tag);
            sorter.sort(tag);

            // Optional security check
            if (rnd.nextBoolean()) {
                log.info("Tag {} selected for additional security scan", tag.getCode());
                securityScanner.scan(tag);
            }

            gateArm.handle(tag);
        }
    }
}