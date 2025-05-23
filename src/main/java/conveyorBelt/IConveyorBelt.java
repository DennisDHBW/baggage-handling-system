package conveyorBelt;

import baggage.Baggage;

public interface IConveyorBelt {
    boolean receiveBaggage(Baggage baggage);
    void transferBaggage();
}
