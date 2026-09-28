package Controller;

import javax.microedition.lcdui.Display;
import Model.CalculadoraModel;
import View.CalculadoraView;

public class CalculadoraController implements
		CalculadoraView.CalculadoraListener {
	private Display display;
	private MenuController menuController;
	private CalculadoraModel model;
	private CalculadoraView view;

	public CalculadoraController(Display display, MenuController menuController) {
		this.display = display;
		this.menuController = menuController;
		model = new CalculadoraModel();
		view = new CalculadoraView(model);
		view.setListener(this);
	}

	public void iniciar() {
		display.setCurrent(view);
	}

	public void voltar() {
		menuController.iniciar();
	}
}