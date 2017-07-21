import java.io.IOException;

public class Killer extends Thread {
    public void run() {
        try {
            int s = RawConsoleInput.read(true);
            if (s == 27) { // отменить потоки если нажали esc
                ConsoleApp.interruptT();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


