package Controller;

import javax.microedition.lcdui.Display;
import Model.JogosModel;
import View.MenuView;

public class JogosController implements MenuView.MenuListener,
		MenuView.VoltarListener {
	private Display display;
	private MenuController menuController;
	private JogosModel model;
	private MenuView view;
	private SnakeController snakeController;

	public JogosController(Display display, MenuController menuController) {
		this.display = display;
		this.menuController = menuController;
		model = new JogosModel();
		view = new MenuView(model.getNomes(), model.getIcones(), model
				.getDescricoes(), true);
		view.setListener(this);
		view.setVoltarListener(this);
		snakeController = new SnakeController(display, this);
	}

	public void iniciar() {
		display.setCurrent(view);
	}

	public void selecionar(int opcao) {
		if (opcao == 0) {
			abrirSnake();
		}
	}

	private void abrirSnake() {
		snakeController.iniciar();
	}

	public void voltar() {
		menuController.iniciar();
	}
}