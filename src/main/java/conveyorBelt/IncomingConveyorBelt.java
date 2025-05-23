package conveyorBelt;

import baggage.Baggage;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class IncomingConveyorBelt implements IConveyorBelt {
    final Queue<Baggage> queue = new LinkedList<>();

    public boolean receiveBaggage(Baggage baggage) {
        if (baggage == null) {
            log.warn("ReceiveBaggage: Baggage is null");
            return false;
        }
        return queue.add(baggage);
    }

    private void transferBaggage(Baggage baggage) {
        if (baggage == null) {
            log.warn("TransferBaggage: Baggage is null");
        }
    }

    public void transferBaggage() {
        while(!queue.isEmpty()) {
            Baggage baggage = queue.poll();
            transferBaggage(baggage);
        }
    }

}
