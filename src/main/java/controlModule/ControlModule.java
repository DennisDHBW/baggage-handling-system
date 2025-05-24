package controlModule;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import shared.Location;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Slf4j
public class ControlModule implements IControlModule {
    private static final int BAND_CAPACITY = 3;

    @Getter
    private final Map<String, Location> baggageMap = new HashMap<>();
    @Getter
    private final Queue<String> bufferQueue = new LinkedList<>();
    @Getter
    private final Map<String, Boolean> outputBandStatus = new HashMap<>();
    private final Map<String, Integer> bandUsage = new HashMap<>();

    public ControlModule() {
        outputBandStatus.put("SORTED_A", true);
        outputBandStatus.put("SORTED_B", true);
        bandUsage.put("SORTED_A", 0);
        bandUsage.put("SORTED_B", 0);
    }

    @Override
    public void register(String barcode) {
        baggageMap.put(barcode, Location.CHECK_IN);
        log.info(">> Registered: {} at CHECK_IN", barcode);
    }

    @Override
    public void updateLocation(String barcode, Location newLocation) {
        if (!baggageMap.containsKey(barcode)) return;

        Location oldLocation = baggageMap.get(barcode);
        baggageMap.put(barcode, newLocation);

        updateBandUsage(oldLocation, newLocation);

        log.info(">> Location Update: {} -> {}", barcode, newLocation);
    }

    @Override
    public Location getLocation(String barcode) {
        return baggageMap.getOrDefault(barcode, Location.UNKNOWN);
    }

    @Override
    public boolean isDestinationAvailable(String destination) {
        boolean statusFree = outputBandStatus.getOrDefault(destination, false);
        int currentUsage = bandUsage.getOrDefault(destination, 0);
        boolean hasCapacity = currentUsage < BAND_CAPACITY;

        return statusFree && hasCapacity;
    }

    @Override
    public void complete(String barcode) {
        Location oldLocation = baggageMap.get(barcode);
        baggageMap.put(barcode, Location.COMPLETED);
        log.info(">> COMPLETED: {}", barcode);

        updateBandUsage(oldLocation, Location.COMPLETED);

        baggageMap.remove(barcode);
    }

    @Override
    public void setDestinationStatus(String destination, boolean isFree) {
        outputBandStatus.put(destination, isFree);
        if (isFree) {
            bandUsage.put(destination, 0); // Reset usage when band becomes free
        }
        log.info("Conveyor {} is {} (usage: {}/{})", destination,
                isFree ? "free" : "full", bandUsage.get(destination), BAND_CAPACITY);
    }

    @Override
    public void bufferBaggage(String barcode) {
        updateLocation(barcode, Location.BUFFERED);
        bufferQueue.add(barcode);
    }

    @Override
    public void releaseBufferedBaggage() {
        if (!bufferQueue.isEmpty()) {
            String barcode = bufferQueue.peek();
            String target = barcode.hashCode() % 2 == 0 ? "SORTED_A" : "SORTED_B";
            if (isDestinationAvailable(target)) {
                updateLocation(barcode, Location.valueOf(target));
                bufferQueue.poll();
                log.info(">> Released from buffer: {} -> {}", barcode, target);
            }
        }
    }

    private void updateBandUsage(Location oldLocation, Location newLocation) {
        // Decrease usage for old location
        if ((oldLocation == Location.SORTED_A || oldLocation == Location.SORTED_B)) {
            String oldBand = oldLocation.toString();
            bandUsage.put(oldBand, Math.max(0, bandUsage.getOrDefault(oldBand, 0) - 1));
        }

        if (newLocation == Location.SORTED_A || newLocation == Location.SORTED_B) {
            String newBand = newLocation.toString();
            int newUsage = bandUsage.getOrDefault(newBand, 0) + 1;
            bandUsage.put(newBand, newUsage);

            // Check if band is now full
            if (newUsage >= BAND_CAPACITY) {
                outputBandStatus.put(newBand, false);
                log.info("Band {} is now FULL ({}/{})", newBand, newUsage, BAND_CAPACITY);
            }
        }
    }

    public Map<String, Integer> getBandUsage() {
        return new HashMap<>(bandUsage);
    }

    @Override
    public List<String> getBaggageAt(Location location) {
        return baggageMap.entrySet().stream()
                .filter(entry -> entry.getValue() == location)
                .map(Map.Entry::getKey)
                .toList();
    }
}