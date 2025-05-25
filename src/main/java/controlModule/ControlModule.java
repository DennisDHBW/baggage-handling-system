package controlModule;

import baggage.Baggage;
import baggage.BaggageTag;
import bufferChannel.IBufferChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ControlModule implements IControlModule {
    private final Map<String, BaggageTag> tags       = new HashMap<>();
    private final Map<String, String>    tagLocation = new HashMap<>();
    private final Map<String, Boolean>   bandStatus  = new HashMap<>();
    private IBufferChannel bufferChannel;

    @Override
    public void registerTag(BaggageTag tag) {
        tags.put(tag.getCode(), tag);
        tagLocation.put(tag.getCode(), "CheckedIn");
        log.info("Tag {} registered for baggage {}→{}",
                tag.getCode(), tag.getBaggage().getOrigin(), tag.getBaggage().getDestination());
    }

    @Override
    public void registerBand(String bandName) {
        bandStatus.put(bandName, true);
        log.info("Band {} initialised and marked as free", bandName);
    }

    @Override
    public void updateTagLocation(BaggageTag tag, String location) {
        tagLocation.put(tag.getCode(), location);
        log.info("Tag {} location updated to {}", tag.getCode(), location);
    }

    @Override
    public boolean isBandFree(String bandName) {
        return bandStatus.getOrDefault(bandName, true);
    }

    @Override
    public void markBandFull(String bandName) {
        bandStatus.put(bandName, false);
        log.warn("Band {} is now full", bandName);
    }

    @Override
    public void markBandFree(String bandName) {
        bandStatus.put(bandName, true);
        log.info("Band {} is now free", bandName);
        if (bufferChannel != null) {
            bufferChannel.attemptRelease(bandName);
        }
    }

    @Override
    public Baggage getBooking(String tagCode) {
        BaggageTag tag = tags.get(tagCode);
        return tag != null ? tag.getBaggage() : null;
    }

    @Override
    public void completeTag(BaggageTag tag) {
        log.info("Final scan of tag {}", tag.getCode());
        updateTagLocation(tag, "Completed");
        tags.remove(tag.getCode());
        tagLocation.remove(tag.getCode());
        log.info("Tag {} processing completed and removed", tag.getCode());
    }

    @Override
    public void setBufferChannel(IBufferChannel channel) {
        this.bufferChannel = channel;
    }
}