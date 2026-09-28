package Model;

public class CalculadoraModel {
	private String numeroAtual;
	private double primeiroNumero;
	private char operador;
	private boolean novoNumero;

	public CalculadoraModel() {
		limpar();
	}

	public void adicionarNumero(char numero) {
		if (novoNumero) {
			numeroAtual = "";
			novoNumero = false;
		}
		if (numeroAtual.equals("0")) {
			numeroAtual = "";
		}
		numeroAtual += numero;
	}

	public void adicionarDecimal() {
		if (novoNumero) {
			numeroAtual = "0";
			novoNumero = false;
		}
		if (numeroAtual.indexOf('.') == -1) {
			numeroAtual += ".";
		}
	}

	public void definirOperador(char operador) {
		if (numeroAtual.length() == 0) {
			return;
		}
		primeiroNumero = Double.parseDouble(numeroAtual);
		this.operador = operador;
		novoNumero = true;
	}

	public boolean calcular() {
		if (numeroAtual.length() == 0) {
			return false;
		}
		double segundoNumero = Double.parseDouble(numeroAtual);
		double resultado = 0;
		if (operador == '+') {
			resultado = primeiroNumero + segundoNumero;
		} else if (operador == '-') {
			resultado = primeiroNumero - segundoNumero;
		} else if (operador == '*') {
			resultado = primeiroNumero * segundoNumero;
		} else if (operador == '/') {
			if (segundoNumero == 0) {
				numeroAtual = "Erro";
				novoNumero = true;
				return false;
			}
			resultado = primeiroNumero / segundoNumero;
		} else {
			return false;
		}
		numeroAtual = formatar(resultado);
		novoNumero = true;
		return true;
	}

	public void limpar() {
		numeroAtual = "0";
		primeiroNumero = 0;
		operador = 0;
		novoNumero = false;
	}

	public String getNumeroAtual() {
		return numeroAtual;
	}

	private String formatar(double numero) {
		if (numero == (long) numero) {
			return Long.toString((long) numero);
		}
		String texto = Double.toString(numero);
		if (texto.indexOf('E') != -1 || texto.indexOf('e') != -1) {
			return texto;
		}
		while (texto.endsWith("0")) {
			texto = texto.substring(0, texto.length() - 1);
		}
		if (texto.endsWith(".")) {
			texto = texto.substring(0, texto.length() - 1);
		}
		return texto;
	}
}