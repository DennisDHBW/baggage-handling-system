package scanner;

import baggage.BaggageTag;
import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shared.Location;

@Slf4j
public class Scanner implements IScanner {
    private final String         name;
    private final IControlModule control;

    public Scanner(String name, IControlModule control) {
        this.name    = name;
        this.control = control;
    }

    @Override
    public void scan(BaggageTag tag) {
        log.info("{} scanning tag {}", name, tag.getCode());
        if (control.getBooking(tag.getCode()) == null) {
            log.error("Scan error: tag {} not recognised", tag.getCode());
            return;
        }
        String loc = name.equals("FirstScan") ? "FirstScanPoint" : "SecurityScan";
        control.updateTagLocation(tag, loc);
        log.info("{} scan successful for tag {}", name, tag.getCode());
    }
}
