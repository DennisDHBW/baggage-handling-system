package main;

import bufferChannel.IBufferChannel;
import bufferChannel.SimpleBuffer;
import controlModule.ControlModule;
import conveyorBelt.ConveyorBelt;
import conveyorBelt.IConveyorBelt;
import grabber.GateGrabber;
import grabber.IGrabber;
import scanner.CameraScanner;
import scanner.IScanner;
import shared.Location;
import sorter.ISorter;
import sorter.SortingUnit;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class Application {

    private static final Random random = new Random();

    public static void main(String[] args) {
        log.info("=== BAGGAGE CONVEYOR SIMULATION STARTED ===");

        // Initialize system components
        ControlModule controlModule = new ControlModule();
        IConveyorBelt entryConveyor = new ConveyorBelt(controlModule);
        IScanner cameraScanner = new CameraScanner(controlModule);
        ISorter sortingUnit = new SortingUnit(controlModule);
        IBufferChannel bufferChannel = new SimpleBuffer(controlModule);
        IGrabber gateGrabber = new GateGrabber(controlModule);

        // Define test baggage items
        List<String> baggageCodes = Arrays.asList(
                "TAG001", "TAG002", "TAG003", "TAG004", "TAG005",
                "TAG006", "TAG007", "TAG008", "TAG009", "TAG010"
        );

        log.info("Simulation starting with {} baggage items", baggageCodes.size());

        // Phase 1: Check-in and registration of all baggage items
        simulateCheckIn(controlModule, baggageCodes);

        // Phase 2: Move baggage items through the system
        for (String barcode : baggageCodes) {
            processIndividualBaggage(barcode, controlModule, entryConveyor,
                    cameraScanner, sortingUnit, bufferChannel, gateGrabber);

            // Simulate processing delay between items
            simulateDelay(500);
        }

        // Phase 3: Final processing and buffer releases
        simulateFinalProcessing(controlModule, bufferChannel, gateGrabber);

        // Phase 4: Display system status
        displayFinalStatus(controlModule);

        log.info("=== SIMULATION COMPLETED ===");
    }

    private static void simulateCheckIn(ControlModule controlModule, List<String> baggageCodes) {
        log.info("\n--- PHASE 1: CHECK-IN SIMULATION ---");

        for (String barcode : baggageCodes) {
            log.info("Checking in baggage item: {}", barcode);
            controlModule.register(barcode);
            simulateDelay(200);
        }
    }

    private static void processIndividualBaggage(String barcode, ControlModule controlModule,
                                                 IConveyorBelt entryConveyor, IScanner cameraScanner,
                                                 ISorter sortingUnit, IBufferChannel bufferChannel,
                                                 IGrabber gateGrabber) {

        log.info("\n--- Processing baggage item: {} ---", barcode);

        // Step 1: Place baggage onto entry conveyor
        log.info("Placing baggage {} onto entry conveyor", barcode);
        entryConveyor.acceptBaggage(barcode);
        simulateDelay(300);

        // Step 2: Barcode scan
        log.info("Scanning barcode for baggage {}", barcode);
        cameraScanner.scan(barcode);
        simulateDelay(400);

        // Check for scan error
        if (controlModule.getLocation(barcode) == Location.ERROR) {
            log.warn("Scan error for {}. Simulating staff intervention.", barcode);
            // Simulate error handling
            controlModule.updateLocation(barcode, Location.FIRST_SCAN_POINT);
            simulateDelay(1000);
        }

        // Optional security screening (randomized)
        if (random.nextBoolean()) {
            simulateSecurityCheck(barcode, controlModule);
        }

        // Step 3: Sorting
        log.info("Sorting baggage {}", barcode);
        sortingUnit.sort(barcode);
        simulateDelay(300);

        // Simulate belt release after some time
        if (random.nextInt(10) < 3) { // 30% chance
            simulateBeltRelease(controlModule, bufferChannel);
        }
    }

    private static void simulateSecurityCheck(String barcode, ControlModule controlModule) {
        log.info("Performing security check for {}", barcode);

        // Simulate successful security check
        if (controlModule.getLocation(barcode) == Location.FIRST_SCAN_POINT) {
            controlModule.updateLocation(barcode, Location.CONTROLLED);
            log.info("Security check successful for {}", barcode);
        }

        simulateDelay(800);
    }

    private static void simulateBeltRelease(ControlModule controlModule, IBufferChannel bufferChannel) {
        // Simulate that an output belt becomes free
        String[] belts = {"SORTED_A", "SORTED_B"};
        String randomBelt = belts[random.nextInt(belts.length)];

        log.info("Output belt {} is now free", randomBelt);
        controlModule.setDestinationStatus(randomBelt, true);

        // Attempt to release buffered baggage
        bufferChannel.releaseIfPossible();

        simulateDelay(200);
    }

    private static void simulateFinalProcessing(ControlModule controlModule,
                                                IBufferChannel bufferChannel, IGrabber gateGrabber) {
        log.info("\n--- PHASE 3: FINAL PROCESSING ---");

        // Free both belts for final processing
        controlModule.setDestinationStatus("SORTED_A", true);
        controlModule.setDestinationStatus("SORTED_B", true);

        // Multiple attempts to release all buffered baggage
        for (int i = 0; i < 5; i++) {
            bufferChannel.releaseIfPossible();
            simulateDelay(300);
        }

        // Gate grabber collects sorted baggage items
        log.info("Gate grabber collecting sorted baggage items");

        // Collect baggage from SORTED_A
        List<String> sortedABaggage = controlModule.getBaggageAt(Location.SORTED_A);
        for (String code : sortedABaggage) {
            log.info("Grabber picks up baggage {} from SORTED_A belt", code);
            gateGrabber.grab(code);
            simulateDelay(200);
        }

        // Collect baggage from SORTED_B
        List<String> sortedBBaggage = controlModule.getBaggageAt(Location.SORTED_B);
        for (String code : sortedBBaggage) {
            log.info("Grabber picks up baggage {} from SORTED_B belt", code);
            gateGrabber.grab(code);
            simulateDelay(200);
        }
    }

    private static void displayFinalStatus(ControlModule controlModule) {
        log.info("\n--- SYSTEM STATUS ---");
        log.info("Active baggage items in system: {}", controlModule.getBaggageMap().size());
        log.info("Buffered baggage items: {}", controlModule.getBufferQueue().size());

        if (!controlModule.getBaggageMap().isEmpty()) {
            log.info("Remaining baggage items:");
            controlModule.getBaggageMap().forEach((barcode, location) ->
                    log.info("  {} -> {}", barcode, location));
        }

        if (!controlModule.getBufferQueue().isEmpty()) {
            log.info("Buffered baggage items: {}", controlModule.getBufferQueue());
        }

        log.info("Belt statuses:");
        controlModule.getOutputBandStatus().forEach((belt, status) ->
                log.info("  {} -> {}", belt, status ? "FREE" : "OCCUPIED"));
    }

    private static void simulateDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
