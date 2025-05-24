package bufferChannel;

import controlModule.IControlModule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleBuffer implements IBufferChannel {
    private final IControlModule control;

    @Override
    public void buffer(String barcode) {
        control.bufferBaggage(barcode);
    }

    @Override
    public void releaseIfPossible() {
        control.releaseBufferedBaggage();
    }
}
