package Controller;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import Model.MenuModel;
import View.MenuView;

public class MenuController implements CommandListener {

	private MIDlet midlet;
	private Display display;

	private MenuModel model;
	private MenuView view;

	public MenuController(MIDlet midlet, Display display) {
		this.midlet = midlet;
		this.display = display;

		model = new MenuModel();
		view = new MenuView(display, model.getOpcoes(), this);
	}

	public void iniciar() {
		display.setCurrent(view);
	}

	public void commandAction(Command command, Displayable displayable) {

		if (command == view.getSair()) {
			midlet.notifyDestroyed();
			return;
		}

		
	}

}