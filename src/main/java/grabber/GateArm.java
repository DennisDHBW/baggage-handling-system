package grabber;

import baggage.BaggageTag;
import controlModule.IControlModule;
import conveyorBelt.DestinationBelt;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GateArm implements IGateArm {
    private final String                   name;
    private final IControlModule           control;
    private final Map<String, DestinationBelt> destBelts;

    public GateArm(String name,
                   IControlModule control,
                   Map<String, DestinationBelt> destBelts) {
        this.name      = name;
        this.control   = control;
        this.destBelts = destBelts;
    }

    @Override
    public void handle(BaggageTag tag) {
        String dest = tag.getBaggage().getDestination();
        log.info("{} picking up tag {} for {}", name, tag.getCode(), dest);
        control.updateTagLocation(tag, "GateArm");
        DestinationBelt belt = destBelts.get(dest);
        if (belt != null) {
            belt.releaseOne();
        }
        control.completeTag(tag);
    }
}
