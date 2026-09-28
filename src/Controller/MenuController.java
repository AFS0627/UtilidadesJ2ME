package Controller;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import Model.MenuModel;
import View.MenuView;

public class MenuController implements MenuView.MenuListener {

	private MIDlet midlet;
	private Display display;

	private MenuModel model;
	private MenuView view;

	private InformacoesController informacoesController;
	private JogosController jogosController;
	private CalculadoraController calculadoraController;

	public MenuController(MIDlet midlet, Display display) {
		this.midlet = midlet;
		this.display = display;

		model = new MenuModel();

		view = new MenuView(model.getNomes(), model.getIcones(), model
				.getDescricoes());

		view.setListener(this);

		informacoesController = new InformacoesController(display, this);

		jogosController = new JogosController(display, this);

		calculadoraController = new CalculadoraController(display, this);
	}

	public void iniciar() {
		display.setCurrent(view);
	}

	public void selecionar(int opcao) {
		if (opcao == 0) {
			abrirCalculadora();
		} else if (opcao == 1) {
			abrirNotas();
		} else if (opcao == 2) {
			abrirJogos();
		} else if (opcao == 3) {
			abrirConversores();
		} else if (opcao == 4) {
			abrirInformacoes();
		}
	}

	private void abrirCalculadora() {
		calculadoraController.iniciar();
	}

	private void abrirNotas() {
	}

	private void abrirJogos() {
		jogosController.iniciar();
	}

	private void abrirConversores() {
	}

	private void abrirInformacoes() {
		informacoesController.iniciar();
	}
}
