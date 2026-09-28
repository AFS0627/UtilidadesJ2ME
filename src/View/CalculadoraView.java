package View;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import Model.CalculadoraModel;

public class CalculadoraView extends Canvas implements CommandListener {
	private CalculadoraModel model;
	private CalculadoraListener listener;
	private Command comandoVoltar;
	private int selecionado;
	private int coluna;
	private int linha;
	private String[][] botoes = { { "7", "8", "9", "/" },
			{ "4", "5", "6", "*" }, { "1", "2", "3", "-" },
			{ "0", ".", "=", "+" }, { "C", "", "", "" } };

	public CalculadoraView(CalculadoraModel model) {
		this.model = model;
		setFullScreenMode(true);
		comandoVoltar = new Command("Voltar", Command.BACK, 1);
		addCommand(comandoVoltar);
		setCommandListener(this);
		selecionado = 0;
		coluna = 0;
		linha = 0;
	}

	protected void paint(Graphics g) {
		int largura = getWidth();
		int altura = getHeight();
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, largura, altura);
		desenharVisor(g, largura);
		desenharBotoes(g, largura, altura);
	}

	private void desenharVisor(Graphics g, int largura) {
		int alturaVisor = 42;
		g.setColor(235, 235, 235);
		g.fillRect(4, 4, largura - 8, alturaVisor);
		g.setColor(0, 0, 0);
		g.drawRect(4, 4, largura - 8, alturaVisor);
		Font fonte = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
				Font.SIZE_LARGE);
		g.setFont(fonte);
		String texto = model.getNumeroAtual();
		int textoX = largura - 9 - fonte.stringWidth(texto);
		if (textoX < 9) {
			textoX = 9;
		}
		g.drawString(texto, textoX, 10, Graphics.TOP | Graphics.LEFT);
	}

	private void desenharBotoes(Graphics g, int largura, int altura) {
		int topo = 52;
		int espaco = 3;
		int larguraBotao = (largura - 10 - espaco * 3) / 4;
		int alturaBotao = (altura - topo - 6 - espaco * 4) / 5;
		if (alturaBotao < 20) {
			alturaBotao = 20;
		}
		for (int l = 0; l < botoes.length; l++) {
			for (int c = 0; c < 4; c++) {
				String texto = botoes[l][c];
				if (texto.equals("")) {
					continue;
				}
				int x = 5 + c * (larguraBotao + espaco);
				int y = topo + l * (alturaBotao + espaco);
				if (l == linha && c == coluna) {
					g.setColor(80, 180, 80);
					g.fillRect(x, y, larguraBotao, alturaBotao);
					g.setColor(255, 255, 255);
				} else {
					g.setColor(220, 220, 220);
					g.fillRect(x, y, larguraBotao, alturaBotao);
					g.setColor(0, 0, 0);
				}
				g.drawRect(x, y, larguraBotao, alturaBotao);
				Font fonte = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
						Font.SIZE_MEDIUM);
				g.setFont(fonte);
				int textoX = x + (larguraBotao - fonte.stringWidth(texto)) / 2;
				int textoY = y + (alturaBotao - fonte.getHeight()) / 2;
				g.drawString(texto, textoX, textoY, Graphics.TOP
						| Graphics.LEFT);
			}
		}
	}

	protected void keyPressed(int keyCode) {
		int acao = getGameAction(keyCode);
		if (keyCode >= KEY_NUM0 && keyCode <= KEY_NUM9) {
			char numero = (char) ('0' + (keyCode - KEY_NUM0));
			model.adicionarNumero(numero);
			repaint();
			return;
		}
		if (acao == UP) {
			mover(0, -1);
		} else if (acao == DOWN) {
			mover(0, 1);
		} else if (acao == LEFT) {
			mover(-1, 0);
		} else if (acao == RIGHT) {
			mover(1, 0);
		} else if (acao == FIRE) {
			pressionarBotao();
		}
		repaint();
	}

	private void mover(int x, int y) {
		int novaLinha = linha + y;
		int novaColuna = coluna + x;
		while (novaLinha >= 0 && novaLinha < botoes.length && novaColuna >= 0
				&& novaColuna < 4 && botoes[novaLinha][novaColuna].equals("")) {
			novaColuna += x;
			novaLinha += y;
		}
		if (novaLinha >= 0 && novaLinha < botoes.length && novaColuna >= 0
				&& novaColuna < 4 && !botoes[novaLinha][novaColuna].equals("")) {
			linha = novaLinha;
			coluna = novaColuna;
		}
	}

	private void pressionarBotao() {
		String botao = botoes[linha][coluna];
		if (botao.equals("C")) {
			model.limpar();
		} else if (botao.equals(".")) {
			model.adicionarDecimal();
		} else if (botao.equals("=")) {
			model.calcular();
		} else if (botao.equals("+") || botao.equals("-") || botao.equals("*")
				|| botao.equals("/")) {
			model.definirOperador(botao.charAt(0));
		} else {
			model.adicionarNumero(botao.charAt(0));
		}
	}

	public void setListener(CalculadoraListener listener) {
		this.listener = listener;
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == comandoVoltar && listener != null) {
			listener.voltar();
		}
	}

	public interface CalculadoraListener {
		void voltar();
	}
}