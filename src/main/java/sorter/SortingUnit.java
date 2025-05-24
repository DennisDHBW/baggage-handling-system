package sorter;

import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;
import shared.Location;

@RequiredArgsConstructor
public class SortingUnit implements ISorter {
    private final IControlModule control;

    @Override
    public void sort(String barcode) {
        String target = (barcode.hashCode() % 2 == 0) ? "SORTED_A" : "SORTED_B";

        if (control.isDestinationAvailable(target)) {
            control.updateLocation(barcode, Location.valueOf(target));
            control.setDestinationStatus(target, false);
        } else {
            control.bufferBaggage(barcode);
        }
    }
}