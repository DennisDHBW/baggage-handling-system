package conveyorBelt;

import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;
import shared.Location;

@RequiredArgsConstructor
public class EntryConveyor implements IConveyor {
    private final IControlModule control;

    @Override
    public void acceptBaggage(String barcode) {
        control.updateLocation(barcode, Location.INCOMING_CONVEYOR);
    }
}
