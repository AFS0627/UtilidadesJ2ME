import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class Helloworld extends MIDlet {

    public void startApp() {
        Display display = Display.getDisplay(this);

        Form form = new Form("Hello");
        form.append("Hello EX109!");

        display.setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}