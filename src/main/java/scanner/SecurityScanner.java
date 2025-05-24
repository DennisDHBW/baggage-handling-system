package scanner;

import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;
import shared.Location;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class SecurityScanner implements IScanner {
    private final IControlModule control;

    @Override
    public void scan(String barcode) {
        if (control.getLocation(barcode) != Location.ERROR) {
            boolean pass = ThreadLocalRandom.current().nextBoolean();
            if (pass) control.updateLocation(barcode, Location.CONTROLLED);
            else control.updateLocation(barcode, Location.ERROR);
        }
    }
}