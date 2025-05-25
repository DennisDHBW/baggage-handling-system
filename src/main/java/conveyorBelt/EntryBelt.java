package conveyorBelt;

import baggage.BaggageTag;
import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import scanner.IScanner;
import shared.Location;

@Slf4j
public class EntryBelt implements IConveyorBelt {
    private final String         name;
    private final IControlModule control;
    private final IScanner scanner;

    public EntryBelt(String name, IControlModule control, IScanner scanner) {
        this.name    = name;
        this.control = control;
        this.scanner = scanner;
        control.registerBand(name);
    }

    @Override
    public void receive(BaggageTag tag) {
        log.info("{} received tag {}", name, tag.getCode());
        control.updateTagLocation(tag, name);
        log.info("{} handing over tag {} to scanner", name, tag.getCode());
        scanner.scan(tag);
    }
}
