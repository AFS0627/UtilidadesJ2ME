package Model;

import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.rms.RecordStore;

public class InformacoesModel {
	private String[] nomesArmazenamento = new String[0];
	private String[] livresArmazenamento = new String[0];
	private String[] totaisArmazenamento = new String[0];
	private boolean bluetooth;
	private boolean localizacao;
	private boolean multimedia;
	private boolean mensagens;
	private boolean pim;
	private boolean fileConnection;
	private boolean http;
	private boolean socket;
	private boolean datagram;
	private int quantidadeRMS;

	public InformacoesModel() {
		bluetooth = existeClasse("javax.bluetooth.LocalDevice");
		localizacao = existeClasse("javax.microedition.location.LocationProvider");
		multimedia = existeClasse("javax.microedition.media.Manager");
		mensagens = existeClasse("javax.wireless.messaging.MessageConnection");
		pim = existeClasse("javax.microedition.pim.PIM");
		fileConnection = existeClasse("javax.microedition.io.file.FileConnection");
		http = existeClasse("javax.microedition.io.HttpConnection");
		socket = existeClasse("javax.microedition.io.SocketConnection");
		datagram = existeClasse("javax.microedition.io.DatagramConnection");
	}

	public String getModelo() {
		String modelo = System.getProperty("device.model");
		if (modelo == null) {
			modelo = System.getProperty("microedition.platform");
		}
		return valorSeguro(modelo);
	}

	public String getSoftware() {
		return valorSeguro(System.getProperty("device.software.version"));
	}

	public String getPlataforma() {
		return valorSeguro(System.getProperty("microedition.platform"));
	}

	public String getConfiguracao() {
		return valorSeguro(System.getProperty("microedition.configuration"));
	}

	public String getPerfil() {
		return valorSeguro(System.getProperty("microedition.profiles"));
	}

	public String getIdioma() {
		return valorSeguro(System.getProperty("microedition.locale"));
	}

	public String getCodificacao() {
		return valorSeguro(System.getProperty("microedition.encoding"));
	}

	public long getMemoriaLivre() {
		return Runtime.getRuntime().freeMemory();
	}

	public long getMemoriaTotal() {
		return Runtime.getRuntime().totalMemory();
	}

	public long getMemoriaUsada() {
		long usada = getMemoriaTotal() - getMemoriaLivre();
		return usada < 0 ? 0 : usada;
	}

	public int getMemoriaUsoPercentual() {
		long total = getMemoriaTotal();
		if (total <= 0) {
			return 0;
		}
		return (int) (getMemoriaUsada() * 100 / total);
	}

	public String getMemoriaLivreKB() {
		return formatarKB(getMemoriaLivre());
	}

	public String getMemoriaTotalKB() {
		return formatarKB(getMemoriaTotal());
	}

	public String getMemoriaUsadaKB() {
		return formatarKB(getMemoriaUsada());
	}

	public boolean isColorido(int cores) {
		return cores > 2;
	}

	public void carregarArmazenamento() {
		if (!fileConnection) {
			limparArmazenamento();
			return;
		}
		try {
			Enumeration raizes = FileSystemRegistry.listRoots();
			int quantidade = contarRaizes(raizes);
			nomesArmazenamento = new String[quantidade];
			livresArmazenamento = new String[quantidade];
			totaisArmazenamento = new String[quantidade];
			raizes = FileSystemRegistry.listRoots();
			int indice = 0;
			while (raizes.hasMoreElements()) {
				String raiz = (String) raizes.nextElement();
				FileConnection conexao = null;
				try {
					conexao = (FileConnection) Connector
							.open("file:///" + raiz);
					nomesArmazenamento[indice] = "Armazenamento " + raiz;
					livresArmazenamento[indice] = formatarTamanho(conexao
							.availableSize());
					totaisArmazenamento[indice] = formatarTamanho(conexao
							.totalSize());
					indice++;
				} finally {
					if (conexao != null) {
						try {
							conexao.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			limparArmazenamento();
		}
	}

	public void carregarRMS() {
		try {
			String[] nomes = RecordStore.listRecordStores();
			quantidadeRMS = nomes == null ? 0 : nomes.length;
		} catch (Exception e) {
			quantidadeRMS = 0;
		}
	}

	public int getQuantidadeArmazenamento() {
		return nomesArmazenamento.length;
	}

	public String getNomeArmazenamento(int indice) {
		return nomesArmazenamento[indice];
	}

	public String getLivreArmazenamento(int indice) {
		return livresArmazenamento[indice];
	}

	public String getTotalArmazenamento(int indice) {
		return totaisArmazenamento[indice];
	}

	public int getQuantidadeRMS() {
		return quantidadeRMS;
	}

	public boolean temBluetooth() {
		return bluetooth;
	}

	public boolean temLocalizacao() {
		return localizacao;
	}

	public boolean temMultimedia() {
		return multimedia;
	}

	public boolean temMensagens() {
		return mensagens;
	}

	public boolean temPIM() {
		return pim;
	}

	public boolean temFileConnection() {
		return fileConnection;
	}

	public boolean temHTTP() {
		return http;
	}

	public boolean temSocket() {
		return socket;
	}

	public boolean temDatagram() {
		return datagram;
	}

	private int contarRaizes(Enumeration raizes) {
		int quantidade = 0;
		while (raizes.hasMoreElements()) {
			raizes.nextElement();
			quantidade++;
		}
		return quantidade;
	}

	private void limparArmazenamento() {
		nomesArmazenamento = new String[0];
		livresArmazenamento = new String[0];
		totaisArmazenamento = new String[0];
	}

	private String formatarKB(long bytes) {
		return bytes / 1024 + " KB";
	}

	private String formatarTamanho(long bytes) {
		if (bytes < 1024) {
			return bytes + " B";
		}
		if (bytes < 1024L * 1024L) {
			return bytes / 1024 + " KB";
		}
		if (bytes < 1024L * 1024L * 1024L) {
			return bytes / 1024 / 1024 + " MB";
		}
		return bytes / 1024 / 1024 / 1024 + " GB";
	}

	private boolean existeClasse(String nome) {
		try {
			Class.forName(nome);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private String valorSeguro(String valor) {
		return valor == null || valor.length() == 0 ? "Desconhecido" : valor;
	}
}