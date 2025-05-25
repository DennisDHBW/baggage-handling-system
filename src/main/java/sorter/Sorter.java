package sorter;

import baggage.BaggageTag;
import bufferChannel.IBufferChannel;
import controlModule.IControlModule;
import conveyorBelt.IConveyorBelt;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class Sorter implements ISorter {
    private final String               name;
    private final IControlModule control;
    private final Map<String, IConveyorBelt> destBelts;
    private final IBufferChannel       bufferChannel;

    public Sorter(String name,
                  IControlModule control,
                  Map<String, IConveyorBelt> destBelts,
                  IBufferChannel bufferChannel) {
        this.name          = name;
        this.control       = control;
        this.destBelts     = destBelts;
        this.bufferChannel = bufferChannel;
    }

    @Override
    public void sort(BaggageTag tag) {
        String dest = tag.getBaggage().getDestination();
        IConveyorBelt belt = destBelts.get(dest);
        if (belt == null) {
            log.error("{} no belt found for destination {}", name, dest);
            control.updateTagLocation(tag, "ErrorNoBelt");
            return;
        }
        log.info("{} sorting tag {} to belt {}", name, tag.getCode(), dest);
        if (control.isBandFree(dest)) {
            belt.receive(tag);
        } else {
            log.warn("{} detects belt {} full, buffering tag {}", name, dest, tag.getCode());
            bufferChannel.buffer(tag, dest);
        }
    }
}
