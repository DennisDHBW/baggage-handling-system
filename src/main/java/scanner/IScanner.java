package scanner;

import baggage.Baggage;
import baggage.BaggageTag;

public interface IScanner {
    BaggageTag scan(Baggage baggage);
}
