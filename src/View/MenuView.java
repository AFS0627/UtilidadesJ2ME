package View;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class MenuView extends Canvas {

	private String[] nomes;
	private String[] icones;
	private String[] descricoes;

	private Image[] imagens;

	private int selecionado;

	private MenuListener listener;

	public MenuView(String[] nomes, String[] icones, String[] descricoes) {
		this.nomes = nomes;
		this.icones = icones;
		this.descricoes = descricoes;

		selecionado = 0;

		carregarImagens();
	}

	private void carregarImagens() {
		imagens = new Image[icones.length];

		for (int i = 0; i < icones.length; i++) {
			try {
				imagens[i] = Image.createImage(icones[i]);
			} catch (Exception e) {
				imagens[i] = null;
			}
		}
	}

	public void setListener(MenuListener listener) {
		this.listener = listener;
	}

	protected void paint(Graphics g) {

		int largura = getWidth();
		int altura = getHeight();

		g.setColor(255, 255, 255);
		g.fillRect(0, 0, largura, altura);

		desenharOpcoes(g);
		desenharDetalhes(g);
	}

	private void desenharOpcoes(Graphics g) {

		int x = 10;
		int y = 20;
		int larguraBarra = 95;
		int alturaBarra = 25;

		for (int i = 0; i < nomes.length; i++) {

			if (i == selecionado) {
				g.setColor(80, 180, 80);
				g.fillRect(x, y - 2, larguraBarra, alturaBarra);
			}

			g.setColor(0, 0, 0);

			g.drawString(nomes[i], x + 5, y, Graphics.TOP | Graphics.LEFT);

			y += 35;
		}
	}

	private void desenharDetalhes(Graphics g) {

		int xImagem = 120;
		int yImagem = 20;

		if (imagens[selecionado] != null) {

			Image imagem = imagens[selecionado];

			int x = xImagem + (80 - imagem.getWidth()) / 2;
			int y = yImagem + (65 - imagem.getHeight()) / 2;

			g.drawImage(imagem, x, y, Graphics.TOP | Graphics.LEFT);
		}

		g.setColor(0, 0, 0);

		desenharTexto(g, descricoes[selecionado], xImagem, 100, 60);
	}

	private void desenharTexto(Graphics g, String texto, int x, int y,
			int largura) {

		String palavra = "";
		int linha = 0;

		for (int i = 0; i < texto.length(); i++) {

			char caractere = texto.charAt(i);

			if (caractere == ' ') {

				if (g.getFont().stringWidth(palavra + " ") > largura) {

					g.drawString(palavra, x, y + linha * 15, Graphics.TOP
							| Graphics.LEFT);

					linha++;
					palavra = "";
				} else {
					palavra += " ";
				}

			} else {
				palavra += caractere;
			}
		}

		if (palavra.length() > 0) {
			g.drawString(palavra, x, y + linha * 15, Graphics.TOP
					| Graphics.LEFT);
		}
	}

	protected void keyPressed(int keyCode) {

		int acao = getGameAction(keyCode);

		if (acao == Canvas.UP) {

			selecionado--;

			if (selecionado < 0) {
				selecionado = nomes.length - 1;
			}

			repaint();

		} else if (acao == Canvas.DOWN) {

			selecionado++;

			if (selecionado >= nomes.length) {
				selecionado = 0;
			}

			repaint();

		} else if (acao == Canvas.FIRE) {

			if (listener != null) {
				listener.selecionar(selecionado);
			}
		}
	}

	public interface MenuListener {
		void selecionar(int opcao);
	}
}