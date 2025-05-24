package bufferChannel;

public interface IBufferChannel {
    void buffer(String barcode);
    void releaseIfPossible();
}
