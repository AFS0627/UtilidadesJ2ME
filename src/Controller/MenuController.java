package Controller;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

import Model.MenuModel;
import View.MenuView;

public class MenuController implements MenuView.MenuListener {

	private MIDlet midlet;
	private Display display;

	private MenuModel model;
	private MenuView view;

	public MenuController(MIDlet midlet, Display display) {
		this.midlet = midlet;
		this.display = display;

		model = new MenuModel();

		view = new MenuView(model.getNomes(), model.getIcones(), model
				.getDescricoes());

		view.setListener(this);
	}

	public void iniciar() {
		display.setCurrent(view);
	}

	public void selecionar(int opcao) {

		if (opcao == 0) {
			abrirCalculadora();
		}

		if (opcao == 1) {
			abrirNotas();
		}

		if (opcao == 2) {
			abrirJogos();
		}
	}

	private void abrirCalculadora() {
	}

	private void abrirNotas() {
	}

	private void abrirJogos() {
	}
}