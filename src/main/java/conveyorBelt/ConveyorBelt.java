package conveyorBelt;

import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;
import shared.Location;

@RequiredArgsConstructor
public class ConveyorBelt implements IConveyorBelt {
    private final IControlModule control;

    @Override
    public void acceptBaggage(String barcode) {
        control.updateLocation(barcode, Location.INCOMING_CONVEYOR);
    }
}
