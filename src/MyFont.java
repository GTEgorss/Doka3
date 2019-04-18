import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyFont {
    Font helvetica;

    public MyFont() {
        try {
            helvetica = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("media/helvetica/HelveticaBold/HelveticaBold.ttf"));
            helvetica = helvetica.deriveFont(Font.BOLD, 40F);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
