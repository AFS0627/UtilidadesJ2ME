package View;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.rms.RecordStore;
import Model.SnakeModel;

public class SnakeView extends Canvas implements Runnable, CommandListener {
	private Command comandoVoltar;
	private VoltarListener voltarListener;
	private SnakeModel model;
	private int[] cobraX;
	private int[] cobraY;
	private int tamanho;
	private int comidaX;
	private int comidaY;
	private int direcao;
	private int proximaDirecao;
	private int pontos;
	private boolean iniciado;
	private boolean jogando;
	private boolean gameOver;
	private boolean menu;
	private Thread thread;
	private int velocidade;
	private int tamanhoCobra;
	private int opcaoMenu;
	private static final int CIMA = 0;
	private static final int DIREITA = 1;
	private static final int BAIXO = 2;
	private static final int ESQUERDA = 3;
	private static final int MAX_COBRA = 100;

	public SnakeView(SnakeModel model) {
		this.model = model;
		cobraX = new int[MAX_COBRA];
		cobraY = new int[MAX_COBRA];
		setFullScreenMode(true);
		comandoVoltar = new Command("Voltar", Command.BACK, 1);
		addCommand(comandoVoltar);
		setCommandListener(this);
		carregarConfiguracoes();
		opcaoMenu = 0;
		menu = true;
		prepararJogo();
	}

	public void iniciar() {
		pararJogo();
		menu = true;
		opcaoMenu = 0;
		prepararJogo();
		repaint();
	}

	private void prepararJogo() {
		int largura = getWidth();
		int altura = getHeight();
		tamanho = 3;
		cobraX[0] = largura / tamanhoCobra / 2;
		cobraY[0] = altura / tamanhoCobra / 2;
		cobraX[1] = cobraX[0] - 1;
		cobraY[1] = cobraY[0];
		cobraX[2] = cobraX[0] - 2;
		cobraY[2] = cobraY[0];
		direcao = DIREITA;
		proximaDirecao = DIREITA;
		pontos = 0;
		iniciado = false;
		jogando = false;
		gameOver = false;
		thread = null;
		criarComida();
	}

	private void iniciarJogo() {
		if (iniciado || jogando) {
			return;
		}
		menu = false;
		iniciado = true;
		jogando = true;
		thread = new Thread(this);
		thread.start();
		repaint();
	}

	private void pararJogo() {
		jogando = false;
		thread = null;
	}

	public void run() {
		Thread minhaThread = Thread.currentThread();
		while (jogando && thread == minhaThread) {
			try {
				Thread.sleep(velocidade);
			} catch (Exception e) {
				return;
			}
			if (!jogando || thread != minhaThread) {
				return;
			}
			if (!gameOver) {
				mover();
				repaint();
			}
		}
	}

	private void mover() {
		direcao = proximaDirecao;
		int novoX = cobraX[0];
		int novoY = cobraY[0];
		if (direcao == CIMA) {
			novoY--;
		} else if (direcao == DIREITA) {
			novoX++;
		} else if (direcao == BAIXO) {
			novoY++;
		} else if (direcao == ESQUERDA) {
			novoX--;
		}
		int largura = getWidth() / tamanhoCobra;
		int altura = (getHeight() - 15) / tamanhoCobra;
		if (novoX < 0 || novoX >= largura || novoY < 0 || novoY >= altura) {
			morrer();
			return;
		}
		if (bateuNoCorpo(novoX, novoY)) {
			morrer();
			return;
		}
		boolean comeu = novoX == comidaX && novoY == comidaY;
		if (comeu) {
			if (tamanho < MAX_COBRA) {
				tamanho++;
			}
			pontos++;
			model.verificarRecorde(pontos);
			criarComida();
		}
		for (int i = tamanho - 1; i > 0; i--) {
			cobraX[i] = cobraX[i - 1];
			cobraY[i] = cobraY[i - 1];
		}
		cobraX[0] = novoX;
		cobraY[0] = novoY;
	}

	private boolean bateuNoCorpo(int x, int y) {
		for (int i = 0; i < tamanho; i++) {
			if (cobraX[i] == x && cobraY[i] == y) {
				return true;
			}
		}
		return false;
	}

	private void criarComida() {
		int largura = getWidth() / tamanhoCobra;
		int altura = (getHeight() - 15) / tamanhoCobra;
		long tempo = System.currentTimeMillis();
		comidaX = (int) (Math.abs(tempo) % largura);
		comidaY = (int) (Math.abs(tempo / 7) % altura);
		while (bateuNoCorpo(comidaX, comidaY)) {
			comidaX++;
			if (comidaX >= largura) {
				comidaX = 0;
				comidaY++;
				if (comidaY >= altura) {
					comidaY = 0;
				}
			}
		}
	}

	private void morrer() {
		gameOver = true;
		jogando = false;
		thread = null;
		model.verificarRecorde(pontos);
		repaint();
	}

	protected void paint(Graphics g) {
		int largura = getWidth();
		int altura = getHeight();
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, largura, altura);
		if (menu) {
			desenharMenu(g);
			return;
		}
		g.setColor(0, 0, 0);
		g.drawString("Pontos: " + pontos, 2, 2, Graphics.TOP | Graphics.LEFT);
		g.drawString("Recorde: " + model.getRecorde(), largura - 2, 2,
				Graphics.TOP | Graphics.RIGHT);
		int inicioY = 15;
		g.drawLine(0, inicioY, largura, inicioY);
		for (int i = 0; i < tamanho; i++) {
			int x = cobraX[i] * tamanhoCobra;
			int y = inicioY + cobraY[i] * tamanhoCobra;
			g.fillRect(x, y, tamanhoCobra, tamanhoCobra);
		}
		int comidaTelaX = comidaX * tamanhoCobra;
		int comidaTelaY = inicioY + comidaY * tamanhoCobra;
		g
				.drawRect(comidaTelaX, comidaTelaY, tamanhoCobra - 1,
						tamanhoCobra - 1);
		if (gameOver) {
			g.setColor(255, 255, 255);
			g.fillRect(15, altura / 2 - 20, largura - 30, 45);
			g.setColor(0, 0, 0);
			g.drawRect(15, altura / 2 - 20, largura - 30, 45);
			g.drawString("GAME OVER", largura / 2, altura / 2 - 15,
					Graphics.TOP | Graphics.HCENTER);
			g.drawString("OK - jogar novamente", largura / 2, altura / 2 + 2,
					Graphics.TOP | Graphics.HCENTER);
		}
	}

	private void desenharMenu(Graphics g) {
		int largura = getWidth();
		g.setColor(0, 0, 0);
		g.drawString("SNAKE", largura / 2, 10, Graphics.TOP | Graphics.HCENTER);
		String[] opcoes = { "Jogar", "Velocidade: " + getNomeVelocidade(),
				"Tamanho: " + getNomeTamanho(), "Voltar" };
		for (int i = 0; i < opcoes.length; i++) {
			int y = 40 + i * 25;
			if (i == opcaoMenu) {
				g.setColor(80, 180, 80);
				g.fillRect(10, y - 2, largura - 20, 22);
				g.setColor(255, 255, 255);
			} else {
				g.setColor(0, 0, 0);
			}
			g.drawString(opcoes[i], 15, y, Graphics.TOP | Graphics.LEFT);
		}
	}

	private String getNomeVelocidade() {
		if (velocidade == 180) {
			return "Lenta";
		}
		if (velocidade == 120) {
			return "Media";
		}
		return "Rapida";
	}

	private String getNomeTamanho() {
		if (tamanhoCobra == 6) {
			return "Pequeno";
		}
		if (tamanhoCobra == 8) {
			return "Medio";
		}
		return "Grande";
	}

	protected void keyPressed(int keyCode) {
		int acao = getGameAction(keyCode);
		if (menu) {
			if (acao == UP) {
				moverMenu(-1);
			} else if (acao == DOWN) {
				moverMenu(1);
			} else if (acao == FIRE) {
				selecionarMenu();
			}
			repaint();
			return;
		}
		if (!iniciado) {
			iniciarJogo();
			return;
		}
		if (gameOver) {
			if (acao == FIRE) {
				iniciar();
				menu = false;
				iniciarJogo();
			}
			return;
		}
		if (acao == UP && direcao != BAIXO) {
			proximaDirecao = CIMA;
		} else if (acao == RIGHT && direcao != ESQUERDA) {
			proximaDirecao = DIREITA;
		} else if (acao == DOWN && direcao != CIMA) {
			proximaDirecao = BAIXO;
		} else if (acao == LEFT && direcao != DIREITA) {
			proximaDirecao = ESQUERDA;
		}
	}

	private void moverMenu(int direcao) {
		opcaoMenu += direcao;
		if (opcaoMenu < 0) {
			opcaoMenu = 3;
		}
		if (opcaoMenu > 3) {
			opcaoMenu = 0;
		}
	}

	private void selecionarMenu() {
		if (opcaoMenu == 0) {
			iniciarJogo();
		} else if (opcaoMenu == 1) {
			if (velocidade == 180) {
				velocidade = 120;
			} else if (velocidade == 120) {
				velocidade = 70;
			} else {
				velocidade = 180;
			}
			salvarConfiguracoes();
		} else if (opcaoMenu == 2) {
			if (tamanhoCobra == 6) {
				tamanhoCobra = 8;
			} else if (tamanhoCobra == 8) {
				tamanhoCobra = 10;
			} else {
				tamanhoCobra = 6;
			}
			salvarConfiguracoes();
			prepararJogo();
		} else if (opcaoMenu == 3) {
			pararJogo();
			if (voltarListener != null) {
				voltarListener.voltar();
			}
		}
	}

	private void carregarConfiguracoes() {
		try {
			RecordStore rs = RecordStore.openRecordStore("snakecfg", true);
			if (rs.getNumRecords() > 0) {
				byte[] dados = rs.getRecord(1);
				String texto = new String(dados);
				int separador = texto.indexOf(";");
				velocidade = Integer.parseInt(texto.substring(0, separador));
				tamanhoCobra = Integer.parseInt(texto.substring(separador + 1));
			} else {
				velocidade = 180;
				tamanhoCobra = 6;
			}
			rs.closeRecordStore();
		} catch (Exception e) {
			velocidade = 180;
			tamanhoCobra = 6;
		}
	}

	private void salvarConfiguracoes() {
		try {
			RecordStore rs = RecordStore.openRecordStore("snakecfg", true);
			String texto = velocidade + ";" + tamanhoCobra;
			byte[] dados = texto.getBytes();
			if (rs.getNumRecords() == 0) {
				rs.addRecord(dados, 0, dados.length);
			} else {
				rs.setRecord(1, dados, 0, dados.length);
			}
			rs.closeRecordStore();
		} catch (Exception e) {
		}
	}

	public void setVoltarListener(VoltarListener listener) {
		voltarListener = listener;
	}

	public interface VoltarListener {
		void voltar();
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == comandoVoltar && voltarListener != null) {
			pararJogo();
			voltarListener.voltar();
		}
	}
}