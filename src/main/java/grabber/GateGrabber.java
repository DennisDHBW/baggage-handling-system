package grabber;

import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;
import shared.Location;

@RequiredArgsConstructor
public class GateGrabber implements IGrabber {
    private final IControlModule control;

    @Override
    public void grab(String barcode) {
        if (control.getLocation(barcode).toString().startsWith("SORTED")) {
            control.updateLocation(barcode, Location.GATE_BAND);
            control.complete(barcode);
        }
    }
}
