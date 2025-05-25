package bufferChannel;

import baggage.BaggageTag;
import controlModule.IControlModule;
import conveyorBelt.IConveyorBelt;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Slf4j
public class BufferChannel implements IBufferChannel {
    private final IControlModule              control;
    private final Map<String, IConveyorBelt> destBelts;
    private final Queue<BufferedItem> queue = new LinkedList<>();

    public BufferChannel(IControlModule control, Map<String, IConveyorBelt> destBelts) {
        this.control   = control;
        this.destBelts = destBelts;
        control.setBufferChannel(this);
    }

    @Override
    public void buffer(BaggageTag tag, String targetBand) {
        queue.offer(new BufferedItem(tag, targetBand));
        control.updateTagLocation(tag, "Buffered");
        log.info("Tag {} buffered for band {}", tag.getCode(), targetBand);
    }

    @Override
    public void attemptRelease(String bandName) {
        for (BufferedItem item : queue) {
            if (item.getTargetBand().equals(bandName)) {
                queue.remove(item);
                log.info("Releasing buffered tag {} to band {}", item.getTag().getCode(), bandName);
                destBelts.get(bandName).receive(item.getTag());
                return;
            }
        }
    }

    @Data
    private static class BufferedItem {
        private final BaggageTag tag;
        private final String     targetBand;
    }
}
