package Model;

public class MenuModel {

    private String[] opcoes;

    public MenuModel() {
        opcoes = new String[] {
            "Calculadora",
            "Notas",
            "Jogos"
        };
    }

    public String[] getOpcoes() {
        return opcoes;
    }
}