package scanner;

import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shared.Location;

@RequiredArgsConstructor
@Slf4j
public class CameraScanner implements IScanner {
    private final IControlModule control;

    @Override
    public void scan(String barcode) {
        if (control.getLocation(barcode) == Location.INCOMING_CONVEYOR) {
            control.updateLocation(barcode, Location.FIRST_SCAN_POINT);
        } else {
            control.updateLocation(barcode, Location.ERROR);
            log.warn("Scan Error: {}", barcode);
        }
    }
}
