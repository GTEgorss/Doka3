import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyFont {
    Font helvetica;

    public MyFont() {
        try {
            helvetica = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/egorsergeev/IdeaProjects/Doka3/src/Helvetica/9202.ttf"));
            helvetica = helvetica.deriveFont(1, 40F);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
