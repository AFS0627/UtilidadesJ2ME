import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

import Controller.MenuController;

public class App extends MIDlet {

    private Display display;
    private MenuController controller;

    public void startApp() {
        display = Display.getDisplay(this);

        controller = new MenuController(this, display);
        controller.iniciar();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}