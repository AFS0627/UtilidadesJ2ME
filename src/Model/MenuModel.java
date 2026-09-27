package Model;

public class MenuModel {

	private String[] nomes;
	private String[] icones;
	private String[] descricoes;

	public MenuModel() {
		nomes = new String[] { "Calculadora", "Notas", "Jogos", "Conversores" , "Ferramentas"};

		icones = new String[] { "/calculadora.png", "/notas.png", "/jogos.png","/Conversor.png","/Ferramenta.png" };

		descricoes = new String[] { "Faça calculos rapidamente",
				"Anote e consulte suas notas", "Divirta-se com alguns jogos","Converta as unidades por aqui", "Informações do Sistema" };
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