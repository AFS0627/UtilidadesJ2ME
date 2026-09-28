package Controller;

import javax.microedition.lcdui.Display;
import Model.SnakeModel;
import View.SnakeView;

public class SnakeController implements SnakeView.VoltarListener {
	private Display display;
	private JogosController jogosController;
	private SnakeModel model;
	private SnakeView view;

	public SnakeController(Display display, JogosController jogosController) {
		this.display = display;
		this.jogosController = jogosController;
		model = new SnakeModel();
		view = new SnakeView(model);
		view.setVoltarListener(this);
	}

	public void iniciar() {
		view.iniciar();
		display.setCurrent(view);
	}

	public void voltar() {
		jogosController.iniciar();
	}
}