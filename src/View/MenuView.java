package View;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

public class MenuView extends List {

    private Command abrir;
    private Command sair;

    public MenuView(Display display, String[] opcoes, CommandListener listener) {
        super("Utilidades", List.IMPLICIT, opcoes, null);

        abrir = new Command("Abrir", Command.OK, 1);
        sair = new Command("Sair", Command.EXIT, 1);

        addCommand(abrir);
        addCommand(sair);

        setCommandListener(listener);
    }

    public Command getAbrir() {
        return abrir;
    }

    public Command getSair() {
        return sair;
    }
}