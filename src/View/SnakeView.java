package View;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import Model.SnakeModel;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

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
	private Thread thread;
	private static final int CIMA = 0;
	private static final int DIREITA = 1;
	private static final int BAIXO = 2;
	private static final int ESQUERDA = 3;
	private static final int TAMANHO_CELULA = 6;
	private static final int MAX_COBRA = 100;

	public SnakeView(SnakeModel model) {
		this.model = model;
		cobraX = new int[MAX_COBRA];
		cobraY = new int[MAX_COBRA];
		setFullScreenMode(true);
		comandoVoltar = new Command("Voltar", Command.BACK, 1);
		addCommand(comandoVoltar);
		setCommandListener(this);
		prepararJogo();
	}

	private void prepararJogo() {
		int largura = getWidth();
		int altura = getHeight();
		tamanho = 3;
		cobraX[0] = largura / TAMANHO_CELULA / 2;
		cobraY[0] = altura / TAMANHO_CELULA / 2;
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
		criarComida();
		repaint();
	}

	private void iniciarJogo() {
		if (iniciado) {
			return;
		}
		iniciado = true;
		jogando = true;
		thread = new Thread(this);
		thread.start();
		repaint();
	}

	public void run() {
		while (jogando) {
			try {
				Thread.sleep(180);
			} catch (Exception e) {
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
		int largura = getWidth() / TAMANHO_CELULA;
		int altura = (getHeight() - 15) / TAMANHO_CELULA;
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
		int largura = getWidth() / TAMANHO_CELULA;
		int altura = (getHeight() - 15) / TAMANHO_CELULA;
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
		model.verificarRecorde(pontos);
		repaint();
	}

	protected void paint(Graphics g) {
		int largura = getWidth();
		int altura = getHeight();
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, largura, altura);
		g.setColor(0, 0, 0);
		g.drawString("Pontos: " + pontos, 2, 2, Graphics.TOP | Graphics.LEFT);
		g.drawString("Recorde: " + model.getRecorde(), largura - 2, 2,
				Graphics.TOP | Graphics.RIGHT);
		int inicioY = 15;
		g.drawLine(0, inicioY, largura, inicioY);
		for (int i = 0; i < tamanho; i++) {
			int x = cobraX[i] * TAMANHO_CELULA;
			int y = inicioY + cobraY[i] * TAMANHO_CELULA;
			g.fillRect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
		}
		int comidaTelaX = comidaX * TAMANHO_CELULA;
		int comidaTelaY = inicioY + comidaY * TAMANHO_CELULA;
		g.drawRect(comidaTelaX, comidaTelaY, TAMANHO_CELULA - 1,
				TAMANHO_CELULA - 1);
		if (!iniciado) {
			g.setColor(255, 255, 255);
			g.fillRect(10, altura / 2 - 25, largura - 20, 50);
			g.setColor(0, 0, 0);
			g.drawRect(10, altura / 2 - 25, largura - 20, 50);
			g.drawString("SNAKE", largura / 2, altura / 2 - 20, Graphics.TOP
					| Graphics.HCENTER);
			g.drawString("Aperte uma tecla", largura / 2, altura / 2,
					Graphics.TOP | Graphics.HCENTER);
			return;
		}
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

	protected void keyPressed(int keyCode) {
		int acao = getGameAction(keyCode);
		if (!iniciado) {
			iniciarJogo();
			return;
		}
		if (gameOver) {
			if (acao == FIRE) {
				prepararJogo();
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

	public void setVoltarListener(VoltarListener listener) {
		voltarListener = listener;
	}

	public interface VoltarListener {
		void voltar();
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == comandoVoltar && voltarListener != null) {
			jogando = false;
			voltarListener.voltar();
		}
	}
}