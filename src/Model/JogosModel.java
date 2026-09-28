package Model;

public class JogosModel {
	private String[] nomes;
	private String[] icones;
	private String[] descricoes;

	public JogosModel() {
		nomes = new String[] { "Snake" };
		icones = new String[] { "/snake.png" };
		descricoes = new String[] { "Jogue o classico jogo da cobrinha" };
	}

	public String[] getNomes() {
		return nomes;
	}

	public String[] getIcones() {
		return icones;
	}

	public String[] getDescricoes() {
		return descricoes;
	}
}