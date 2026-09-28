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
	private int deslocamento;
	private MenuListener listener;

	public MenuView(String[] nomes, String[] icones, String[] descricoes) {
		this.nomes = nomes;
		this.icones = icones;
		this.descricoes = descricoes;
		selecionado = 0;
		deslocamento = 0;
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
		int yInicial = 20;
		int espacamento = 35;
		int larguraBarra = getWidth() / 2 - 15;
		for (int i = 0; i < nomes.length; i++) {
			int y = yInicial + (i * espacamento) - deslocamento;
			if (y + 25 < 0 || y > getHeight()) {
				continue;
			}
			if (i == selecionado) {
				g.setColor(80, 180, 80);
				g.fillRect(x, y - 2, larguraBarra, 25);
			}

			g.setColor(0, 0, 0);
			g.drawString(nomes[i], x + 5, y, Graphics.TOP | Graphics.LEFT);
		}
	}

	private void desenharDetalhes(Graphics g) {
		int xImagem = getWidth() / 2 + 5;
		int larguraArea = getWidth() - xImagem - 5;
		int yImagem = 20;
		int alturaImagem = 55;

		if (imagens[selecionado] != null) {
			Image imagem = imagens[selecionado];
			int x = xImagem + (larguraArea - imagem.getWidth()) / 2;
			int y = yImagem + (alturaImagem - imagem.getHeight()) / 2;

			g.drawImage(imagem, x, y, Graphics.TOP | Graphics.LEFT);
		}
		g.setColor(0, 0, 0);
		desenharTexto(g, descricoes[selecionado], xImagem, 90, larguraArea);
	}

	private void desenharTexto(Graphics g, String texto, int x, int y,
			int largura) {
		String linha = "";
		int linhaAtual = 0;
		int altura = g.getFont().getHeight();
		int maxLinhas = (getHeight() - y - 5) / altura;
		String[] palavras = separarPalavras(texto);
		for (int i = 0; i < palavras.length; i++) {
			String teste = linha;
			if (teste.length() > 0) {
				teste += " ";
			}
			teste += palavras[i];
			if (g.getFont().stringWidth(teste) > largura) {
				if (linha.length() > 0) {
					if (linhaAtual >= maxLinhas) {
						break;
					}
					g.drawString(linha, x, y + linhaAtual * altura,
							Graphics.TOP | Graphics.LEFT);
					linhaAtual++;
				}
				linha = palavras[i];

			} else {
				linha = teste;
			}
		}
		if (linha.length() > 0 && linhaAtual < maxLinhas) {
			g.drawString(linha, x, y + linhaAtual * altura, Graphics.TOP
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
			atualizarRolagem();
			repaint();
		} else if (acao == Canvas.DOWN) {

			selecionado++;

			if (selecionado >= nomes.length) {
				selecionado = 0;
			}

			atualizarRolagem();
			repaint();

		} else if (acao == Canvas.FIRE) {

			if (listener != null) {
				listener.selecionar(selecionado);
			}
		}
	}

	private void atualizarRolagem() {

		if (selecionado >= 3) {
			deslocamento = (selecionado - 2) * 35;
		} else {
			deslocamento = 0;
		}
	}

	public interface MenuListener {
		void selecionar(int opcao);
	}

	private String[] separarPalavras(String texto) {
		int quantidade = 1;
		for (int i = 0; i < texto.length(); i++) {
			if (texto.charAt(i) == ' ') {
				quantidade++;
			}
		}
		String[] palavras = new String[quantidade];
		int indice = 0;
		String palavra = "";
		for (int i = 0; i < texto.length(); i++) {
			char caractere = texto.charAt(i);
			if (caractere == ' ') {
				if (palavra.length() > 0) {
					palavras[indice] = palavra;
					indice++;
					palavra = "";
				}
			} else {
				palavra += caractere;
			}
		}
		if (palavra.length() > 0) {
			palavras[indice] = palavra;
		}
		return palavras;
	}
}