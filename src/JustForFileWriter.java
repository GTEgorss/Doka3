import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

public class JustForFileWriter {

    public JustForFileWriter() {

    }

    public void write(MenuPanel menuPanel,
                      @NotNull String string1,
                      String URL) {
        try (FileWriter writer = new FileWriter(getClass().getResource(URL).toString(), false)) {
            writer.write(string1);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
