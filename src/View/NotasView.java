package View;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import Model.NotasModel;
import Model.NotasModel.Nota;

public class NotasView extends Canvas implements CommandListener {
	private NotasModel model;
	private NotasListener listener;
	private Command comandoNova;
	private Command comandoVoltar;
	private int selecionado;
	private int opcaoMenu;
	private boolean menuNota;

	public NotasView(NotasModel model) {
		this.model = model;
		setFullScreenMode(true);
		comandoNova = new Command("Nova", Command.SCREEN, 1);
		comandoVoltar = new Command("Voltar", Command.BACK, 2);
		addCommand(comandoNova);
		addCommand(comandoVoltar);
		setCommandListener(this);
		selecionado = 0;
		opcaoMenu = 0;
		menuNota = false;
	}

	protected void paint(Graphics g) {
		int largura = getWidth();
		int altura = getHeight();
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, largura, altura);
		if (menuNota) {
			desenharMenuNota(g);
		} else {
			desenharLista(g);
		}
	}

	private void desenharLista(Graphics g) {
		g.setColor(0, 0, 0);
		g.drawString("Notas", 5, 5, Graphics.TOP | Graphics.LEFT);
		if (model.getQuantidade() == 0) {
			g.drawString("Nenhuma nota", getWidth() / 2, getHeight() / 2,
					Graphics.TOP | Graphics.HCENTER);
			return;
		}
		for (int i = 0; i < model.getQuantidade(); i++) {
			Nota nota = model.getNota(i);
			int y = 30 + i * 25;
			if (i == selecionado) {
				g.setColor(80, 180, 80);
				g.fillRect(5, y - 2, getWidth() - 10, 22);
				g.setColor(255, 255, 255);
			} else {
				g.setColor(0, 0, 0);
			}
			g.drawString(nota.getTitulo(), 10, y, Graphics.TOP | Graphics.LEFT);
		}
	}

	private void desenharMenuNota(Graphics g) {
		Nota nota = model.getNota(selecionado);
		g.setColor(0, 0, 0);
		if (nota != null) {
			g.drawString(nota.getTitulo(), getWidth() / 2, 5, Graphics.TOP
					| Graphics.HCENTER);
		}
		String[] opcoes = { "Ver", "Editar", "Excluir", "Voltar" };
		for (int i = 0; i < opcoes.length; i++) {
			int y = 35 + i * 25;
			if (i == opcaoMenu) {
				g.setColor(80, 180, 80);
				g.fillRect(5, y - 2, getWidth() - 10, 22);
				g.setColor(255, 255, 255);
			} else {
				g.setColor(0, 0, 0);
			}
			g.drawString(opcoes[i], 10, y, Graphics.TOP | Graphics.LEFT);
		}
	}

	protected void keyPressed(int keyCode) {
		int acao = getGameAction(keyCode);
		if (menuNota) {
			if (acao == UP) {
				opcaoMenu--;
				if (opcaoMenu < 0) {
					opcaoMenu = 3;
				}
			} else if (acao == DOWN) {
				opcaoMenu++;
				if (opcaoMenu > 3) {
					opcaoMenu = 0;
				}
			} else if (acao == FIRE) {
				selecionarMenu();
			}
			repaint();
			return;
		}
		if (model.getQuantidade() == 0) {
			return;
		}
		if (acao == UP) {
			selecionado--;
			if (selecionado < 0) {
				selecionado = model.getQuantidade() - 1;
			}
		} else if (acao == DOWN) {
			selecionado++;
			if (selecionado >= model.getQuantidade()) {
				selecionado = 0;
			}
		} else if (acao == FIRE) {
			opcaoMenu = 0;
			menuNota = true;
		}
		repaint();
	}

	private void selecionarMenu() {
		if (opcaoMenu == 0) {
			if (listener != null) {
				listener.ver(selecionado);
			}
		} else if (opcaoMenu == 1) {
			if (listener != null) {
				listener.editar(selecionado);
			}
		} else if (opcaoMenu == 2) {
			if (listener != null) {
				listener.excluir(selecionado);
			}
		} else {
			menuNota = false;
		}
		repaint();
	}

	public void novaNota() {
		if (listener != null) {
			listener.nova();
		}
	}

	public void setSelecionado(int indice) {
		selecionado = indice;
	}

	public void voltarLista() {
		menuNota = false;
		opcaoMenu = 0;
		repaint();
	}

	public void setListener(NotasListener listener) {
		this.listener = listener;
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == comandoNova) {
			novaNota();
		} else if (command == comandoVoltar) {
			if (menuNota) {
				menuNota = false;
				opcaoMenu = 0;
				repaint();
			} else if (listener != null) {
				listener.voltar();
			}
		}
	}

	public interface NotasListener {
		void nova();

		void ver(int indice);

		void editar(int indice);

		void excluir(int indice);

		void voltar();
	}
}