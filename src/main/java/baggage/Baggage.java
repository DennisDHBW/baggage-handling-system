package baggage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shared.Location;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Baggage {
    private String barcode;
    private Location location;
}

