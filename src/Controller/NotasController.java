package Controller;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import Model.NotasModel;
import View.NotasView;

public class NotasController implements NotasView.NotasListener,
		CommandListener {
	private Display display;
	private MenuController menuController;
	private NotasModel model;
	private NotasView view;
	private TextBox telaTitulo;
	private TextBox telaTexto;
	private Command comandoContinuar;
	private Command comandoSalvar;
	private Command comandoCancelar;
	private Command comandoVoltar;
	private boolean editando;
	private int indiceEditando;
	private String titulo;

	public NotasController(Display display, MenuController menuController) {
		this.display = display;
		this.menuController = menuController;
		model = new NotasModel();
		view = new NotasView(model);
		view.setListener(this);
		comandoContinuar = new Command("Continuar", Command.OK, 1);
		comandoSalvar = new Command("Salvar", Command.OK, 1);
		comandoCancelar = new Command("Cancelar", Command.BACK, 2);
		comandoVoltar = new Command("Voltar", Command.BACK, 1);
	}

	public void iniciar() {
		display.setCurrent(view);
	}

	public void nova() {
		editando = false;
		indiceEditando = -1;
		telaTitulo = new TextBox("Titulo", "", 50, TextField.ANY);
		telaTitulo.addCommand(comandoContinuar);
		telaTitulo.addCommand(comandoCancelar);
		telaTitulo.setCommandListener(this);
		display.setCurrent(telaTitulo);
	}

	public void ver(int indice) {
		NotasModel.Nota nota = model.getNota(indice);
		if (nota == null) {
			return;
		}
		telaTexto = new TextBox(nota.getTitulo(), nota.getTexto(), 500,
				TextField.ANY);
		telaTexto.addCommand(comandoVoltar);
		telaTexto.setCommandListener(this);
		display.setCurrent(telaTexto);
	}

	public void editar(int indice) {
		NotasModel.Nota nota = model.getNota(indice);
		if (nota == null) {
			return;
		}
		editando = true;
		indiceEditando = indice;
		telaTitulo = new TextBox("Titulo", nota.getTitulo(), 50, TextField.ANY);
		telaTitulo.addCommand(comandoContinuar);
		telaTitulo.addCommand(comandoCancelar);
		telaTitulo.setCommandListener(this);
		display.setCurrent(telaTitulo);
	}

	private void abrirTexto() {
		titulo = telaTitulo.getString();
		if (titulo.length() == 0) {
			titulo = "Sem titulo";
		}
		String textoInicial = "";
		if (editando) {
			NotasModel.Nota nota = model.getNota(indiceEditando);
			if (nota != null) {
				textoInicial = nota.getTexto();
			}
		}
		telaTexto = new TextBox(titulo, textoInicial, 500, TextField.ANY);
		telaTexto.addCommand(comandoSalvar);
		telaTexto.addCommand(comandoCancelar);
		telaTexto.setCommandListener(this);
		display.setCurrent(telaTexto);
	}

	public void excluir(int indice) {
		model.excluir(indice);
		if (model.getQuantidade() == 0) {
			view.setSelecionado(0);
		} else if (indice >= model.getQuantidade()) {
			view.setSelecionado(model.getQuantidade() - 1);
		} else {
			view.setSelecionado(indice);
		}
		view.voltarLista();
		display.setCurrent(view);
	}

	public void voltar() {
		menuController.iniciar();
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == comandoContinuar) {
			abrirTexto();
		} else if (command == comandoSalvar) {
			String texto = telaTexto.getString();
			if (editando) {
				model.editar(indiceEditando, titulo, texto);
			} else {
				model.adicionar(titulo, texto);
			}
			display.setCurrent(view);
		} else if (command == comandoCancelar) {
			display.setCurrent(view);
		} else if (command == comandoVoltar) {
			display.setCurrent(view);
		}
	}
}