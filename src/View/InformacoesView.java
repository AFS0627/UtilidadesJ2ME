package View;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import Model.InformacoesModel;

public class InformacoesView extends Canvas implements CommandListener {
	private InformacoesModel model;
	private Command voltar;
	private VoltarListener listener;
	private int pagina;
	private int deslocamento;
	private boolean dadosCarregados;

	public InformacoesView(InformacoesModel model) {
		this.model = model;
		voltar = new Command("Voltar", Command.BACK, 1);
		addCommand(voltar);
		setCommandListener(this);
	}

	public void setListener(VoltarListener listener) {
		this.listener = listener;
	}

	public void atualizar() {
		dadosCarregados = true;
		repaint();
	}

	protected void paint(Graphics g) {
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, getWidth(), getHeight());
		desenharTitulo(g);
		switch (pagina) {
		case 0:
			desenharAparelho(g);
			break;
		case 1:
			desenharJava(g);
			break;
		case 2:
			desenharMemoria(g);
			break;
		case 3:
			desenharArmazenamento(g);
			break;
		case 4:
			desenharAPIs(g);
			break;
		case 5:
			desenharRede(g);
			break;
		case 6:
			desenharDiagnostico(g);
			break;
		}
		desenharRodape(g);
	}

	private void desenharTitulo(Graphics g) {
		g.setColor(60, 160, 70);
		g.fillRect(0, 0, getWidth(), 25);
		g.setColor(255, 255, 255);
		g.drawString("Informacoes " + (pagina + 1) + "/7", 5, 5, Graphics.TOP
				| Graphics.LEFT);
	}

	private void desenharAparelho(Graphics g) {
		int y = 32 - deslocamento;
		y = desenharLinha(g, "Modelo", model.getModelo(), y, 40);
		y = desenharLinha(g, "Software", model.getSoftware(), y, 40);
		y = desenharLinha(g, "Plataforma", model.getPlataforma(), y, 40);
		y = desenharLinha(g, "Tela", getWidth() + " x " + getHeight(), y, 40);
		y = desenharLinha(g, "Idioma", model.getIdioma(), y, 40);
		desenharLinha(g, "Codificacao", model.getCodificacao(), y, 40);
	}

	private void desenharJava(Graphics g) {
		int y = 32 - deslocamento;
		y = desenharLinha(g, "Configuracao", model.getConfiguracao(), y, 45);
		y = desenharLinha(g, "Perfil", model.getPerfil(), y, 45);
		y = desenharLinha(g, "Java ME", model.getConfiguracao() + " / "
				+ model.getPerfil(), y, 45);
		y = desenharLinha(g, "Idioma", model.getIdioma(), y, 45);
		desenharLinha(g, "Codificacao", model.getCodificacao(), y, 45);
	}

	private void desenharMemoria(Graphics g) {
		int y = 32 - deslocamento;
		y = desenharLinha(g, "Livre", model.getMemoriaLivreKB(), y, 43);
		y = desenharLinha(g, "Total", model.getMemoriaTotalKB(), y, 43);
		y = desenharLinha(g, "Usada", model.getMemoriaUsadaKB(), y, 43);
		y = desenharLinha(g, "Uso", model.getMemoriaUsoPercentual() + "%", y,
				35);
		desenharBarraMemoria(g, y);
		y += 55;
		desenharTexto(g, "Memoria utilizada pelo", y);
		desenharTexto(g, "ambiente Java ME.", y + 18);
	}

	private void desenharBarraMemoria(Graphics g, int y) {
		int x = 5;
		int largura = getWidth() - 10;
		int altura = 12;
		int percentual = model.getMemoriaUsoPercentual();
		g.setColor(220, 220, 220);
		g.fillRect(x, y, largura, altura);
		g.setColor(60, 160, 70);
		g.fillRect(x, y, largura * percentual / 100, altura);
		g.setColor(80, 80, 80);
		g.drawRect(x, y, largura - 1, altura - 1);
	}

	private void desenharArmazenamento(Graphics g) {
		int y = 32 - deslocamento;
		if (!dadosCarregados) {
			desenharTexto(g, "Lendo armazenamento...", y);
			return;
		}
		int quantidade = model.getQuantidadeArmazenamento();
		if (quantidade == 0) {
			desenharTexto(g, "Nenhum armazenamento", y);
			desenharTexto(g, "disponivel.", y + 18);
			return;
		}
		for (int i = 0; i < quantidade; i++) {
			y = desenharLinha(g, model.getNomeArmazenamento(i), "", y, 30);
			y = desenharLinha(g, "Livre", model.getLivreArmazenamento(i), y, 38);
			y = desenharLinha(g, "Total", model.getTotalArmazenamento(i), y, 55);
		}
	}

	private void desenharAPIs(Graphics g) {
		int y = 32 - deslocamento;
		y = desenharLinha(g, "Bluetooth", simNao(model.temBluetooth()), y, 39);
		y = desenharLinha(g, "Multimedia", simNao(model.temMultimedia()), y, 39);
		y = desenharLinha(g, "Mensagens", simNao(model.temMensagens()), y, 39);
		y = desenharLinha(g, "Localizacao", simNao(model.temLocalizacao()), y,
				39);
		y = desenharLinha(g, "PIM", simNao(model.temPIM()), y, 39);
		y = desenharLinha(g, "FileConnection",
				simNao(model.temFileConnection()), y, 39);
		desenharLinha(g, "RMS", "SIM", y, 39);
	}

	private void desenharRede(Graphics g) {
		int y = 32 - deslocamento;
		y = desenharLinha(g, "HTTP", simNao(model.temHTTP()), y, 43);
		y = desenharLinha(g, "Socket", simNao(model.temSocket()), y, 43);
		y = desenharLinha(g, "Datagram", simNao(model.temDatagram()), y, 43);
		y = desenharLinha(g, "Bluetooth", simNao(model.temBluetooth()), y, 50);
		desenharTexto(g, "APIs disponiveis no ambiente", y);
		desenharTexto(g, "Java ME deste aparelho.", y + 18);
	}

	private void desenharDiagnostico(Graphics g) {
		int y = 32 - deslocamento;
		y = desenharLinha(g, "Java ME", "OK", y, 40);
		y = desenharLinha(g, "Canvas", "OK", y, 40);
		y = desenharLinha(g, "RMS", "OK", y, 40);
		y = desenharLinha(g, "FileConnection",
				simNao(model.temFileConnection()), y, 40);
		y = desenharLinha(g, "Multimedia", simNao(model.temMultimedia()), y, 40);
		y = desenharLinha(g, "Bluetooth", simNao(model.temBluetooth()), y, 40);
		y = desenharLinha(g, "Mensagens", simNao(model.temMensagens()), y, 40);
		desenharLinha(g, "RMS Stores",
				String.valueOf(model.getQuantidadeRMS()), y, 40);
	}

	private int desenharLinha(Graphics g, String nome, String valor, int y,
			int espacamento) {
		if (y >= 26 && y <= getHeight() - 20) {
			g.setColor(0, 0, 0);
			g.drawString(nome, 5, y, Graphics.TOP | Graphics.LEFT);
			if (valor != null && valor.length() > 0) {
				g.drawString(valor, 5, y + 14, Graphics.TOP | Graphics.LEFT);
			}
		}
		return y + espacamento;
	}

	private void desenharTexto(Graphics g, String texto, int y) {
		if (y < 26 || y > getHeight() - 20) {
			return;
		}
		g.setColor(0, 0, 0);
		g.drawString(texto, 5, y, Graphics.TOP | Graphics.LEFT);
	}

	private void desenharRodape(Graphics g) {
		int y = getHeight() - 16;
		g.setColor(255, 255, 255);
		g.fillRect(0, y - 2, getWidth(), 18);
		g.setColor(100, 100, 100);
		g
				.drawString("< > Pagina ^ v Rolar", 5, y, Graphics.TOP
						| Graphics.LEFT);
	}

	protected void keyPressed(int keyCode) {
		int acao = getGameAction(keyCode);
		if (acao == Canvas.RIGHT) {
			mudarPagina(1);
		} else if (acao == Canvas.LEFT) {
			mudarPagina(-1);
		} else if (acao == Canvas.DOWN) {
			rolar(18);
		} else if (acao == Canvas.UP) {
			rolar(-18);
		}
	}

	private void mudarPagina(int direcao) {
		pagina += direcao;
		if (pagina > 6) {
			pagina = 0;
		} else if (pagina < 0) {
			pagina = 6;
		}
		deslocamento = 0;
		repaint();
	}

	private void rolar(int quantidade) {
		deslocamento += quantidade;
		if (deslocamento < 0) {
			deslocamento = 0;
		}
		int maximo = calcularDeslocamentoMaximo();
		if (deslocamento > maximo) {
			deslocamento = maximo;
		}
		repaint();
	}

	private int calcularDeslocamentoMaximo() {
		int alturaConteudo;
		switch (pagina) {
		case 0:
			alturaConteudo = 32 + 6 * 40;
			break;
		case 1:
			alturaConteudo = 32 + 5 * 45;
			break;
		case 2:
			alturaConteudo = 32 + 4 * 43 + 55 + 36;
			break;
		case 3:
			alturaConteudo = model.getQuantidadeArmazenamento() > 0 ? 32 + model
					.getQuantidadeArmazenamento() * 123
					: 100;
			break;
		case 4:
			alturaConteudo = 32 + 7 * 39;
			break;
		case 5:
			alturaConteudo = 32 + 4 * 43 + 50 + 36;
			break;
		case 6:
			alturaConteudo = 32 + 8 * 40;
			break;
		default:
			alturaConteudo = 0;
		}
		int alturaDisponivel = getHeight() - 43;
		int maximo = alturaConteudo - alturaDisponivel;
		return maximo > 0 ? maximo : 0;
	}

	private String simNao(boolean valor) {
		return valor ? "SIM" : "NAO";
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == voltar && listener != null) {
			listener.voltar();
		}
	}

	public interface VoltarListener {
		void voltar();
	}
}