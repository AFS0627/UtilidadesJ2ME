package Controller;

import javax.microedition.lcdui.Display;
import Model.InformacoesModel;
import View.InformacoesView;

public class InformacoesController implements InformacoesView.VoltarListener {
	private Display display;
	private MenuController menuController;
	private InformacoesModel model;
	private InformacoesView view;

	public InformacoesController(Display display, MenuController menuController) {
		this.display = display;
		this.menuController = menuController;
		this.model = new InformacoesModel();
		this.view = new InformacoesView(model);
		view.setListener(this);
	}

	public void iniciar() {
		display.setCurrent(view);
		new Thread() {
			public void run() {
				model.carregarArmazenamento();
				model.carregarRMS();
				view.atualizar();
			}
		}.start();
	}

	public void voltar() {
		menuController.iniciar();
	}
}