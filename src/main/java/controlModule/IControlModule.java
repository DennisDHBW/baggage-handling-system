package controlModule;

import baggage.Baggage;
import baggage.BaggageTag;
import bufferChannel.IBufferChannel;

public interface IControlModule {
    void registerTag(BaggageTag tag);
    void registerBand(String bandName);
    void updateTagLocation(BaggageTag tag, String location);
    boolean isBandFree(String bandName);
    void markBandFull(String bandName);
    void markBandFree(String bandName);
    Baggage getBooking(String tagCode);
    void completeTag(BaggageTag tag);
    void setBufferChannel(IBufferChannel channel);
}
