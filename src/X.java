import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Random;

public class X {
    private final Random r = new Random();
    @Save
    private int x;
    private float y;
    @Save
    private String s;

    public X() {
        x = r.nextInt(256);
        y = r.nextFloat(256);
        s = getRandomString();
    }

    @NotNull
    private String getRandomString() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = r.nextInt(20) + 5;
        return r.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public String toString() {
        return "x=" + String.format("%03d", x) +
                ", y=" + String.format("%1$7.3f", y).replace(' ', '0') +
                ", s='" + s + '\'';
    }
}
