package controlModule;

import shared.Location;

import java.util.List;

public interface IControlModule {
    void register(String barcode);
    void updateLocation(String barcode, Location newLocation);
    Location getLocation(String barcode);
    boolean isDestinationAvailable(String destination);
    void complete(String barcode);
    void bufferBaggage(String barcode);
    void releaseBufferedBaggage();
    void setDestinationStatus(String destination, boolean isFree);
    List<String> getBaggageAt(Location location);
}
