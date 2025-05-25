package bufferChannel;

import baggage.BaggageTag;

public interface IBufferChannel {
    void buffer(BaggageTag tag, String targetBand);
    void attemptRelease(String bandName);
}