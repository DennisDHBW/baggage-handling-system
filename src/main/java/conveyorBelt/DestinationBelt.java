package conveyorBelt;


import baggage.BaggageTag;
import controlModule.IControlModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DestinationBelt implements IConveyorBelt {
    private final String         name;
    private final IControlModule control;
    private       int            capacity;

    public DestinationBelt(String name, IControlModule control, int capacity) {
        this.name     = name;
        this.control  = control;
        this.capacity = capacity;
        control.registerBand(name);
    }

    @Override
    public void receive(BaggageTag tag) {
        if (capacity <= 0) {
            log.warn("{} cannot accept tag {}, capacity reached", name, tag.getCode());
            control.markBandFull(name);
            return;
        }
        capacity--;
        log.info("{} accepted tag {}, remaining capacity {}", name, tag.getCode(), capacity);
        control.updateTagLocation(tag, name);
        if (capacity == 0) {
            control.markBandFull(name);
        }
    }

    /**
     * Called when a piece is removed at the gate, freeing one slot.
     */
    public void releaseOne() {
        capacity++;
        log.info("{} released one slot, new capacity {}", name, capacity);
        control.markBandFree(name);
    }
}