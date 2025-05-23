package utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utility {
    public static Random RANDOM = new Random();
    public static List<String> barcodes = new ArrayList<>();

    public static String generateRandomBarcode() {
        int length = 12;

        StringBuilder code;
        do {
            code = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int digit = RANDOM.nextInt(10);
                code.append(digit);
            }
        } while (barcodes.contains(code.toString()));
        return code.toString();
    }
}
