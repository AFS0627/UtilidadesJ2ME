package Model;

import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public class NotasModel {
	private Vector notas;

	public NotasModel() {
		notas = new Vector();
		carregar();
	}

	public void adicionar(String titulo, String texto) {
		notas.addElement(new Nota(titulo, texto));
		salvar();
	}

	public void editar(int indice, String titulo, String texto) {
		if (indice >= 0 && indice < notas.size()) {
			Nota nota = (Nota) notas.elementAt(indice);
			nota.titulo = titulo;
			nota.texto = texto;
			salvar();
		}
	}

	public void excluir(int indice) {
		if (indice >= 0 && indice < notas.size()) {
			notas.removeElementAt(indice);
			salvar();
		}
	}

	public Nota getNota(int indice) {
		if (indice >= 0 && indice < notas.size()) {
			return (Nota) notas.elementAt(indice);
		}
		return null;
	}

	public int getQuantidade() {
		return notas.size();
	}

	private void carregar() {
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore("notas", true);
			RecordEnumeration registros = recordStore.enumerateRecords(null,
					null, false);
			while (registros.hasNextElement()) {
				int id = registros.nextRecordId();
				byte[] dados = recordStore.getRecord(id);
				String registro = new String(dados);
				int separador = registro.indexOf("|");
				if (separador != -1) {
					String titulo = registro.substring(0, separador);
					String texto = registro.substring(separador + 1);
					notas.addElement(new Nota(titulo, texto));
				}
			}
			registros.destroy();
		} catch (Exception e) {
		} finally {
			if (recordStore != null) {
				try {
					recordStore.closeRecordStore();
				} catch (Exception e) {
				}
			}
		}
	}

	private void salvar() {
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore("notas", true);
			RecordEnumeration registros = recordStore.enumerateRecords(null,
					null, false);
			while (registros.hasNextElement()) {
				int id = registros.nextRecordId();
				recordStore.deleteRecord(id);
			}
			registros.destroy();
			for (int i = 0; i < notas.size(); i++) {
				Nota nota = (Nota) notas.elementAt(i);
				String registro = nota.titulo + "|" + nota.texto;
				byte[] dados = registro.getBytes();
				recordStore.addRecord(dados, 0, dados.length);
			}
		} catch (Exception e) {
		} finally {
			if (recordStore != null) {
				try {
					recordStore.closeRecordStore();
				} catch (Exception e) {
				}
			}
		}
	}

	public static class Nota {
		private String titulo;
		private String texto;

		public Nota(String titulo, String texto) {
			this.titulo = titulo;
			this.texto = texto;
		}

		public String getTitulo() {
			return titulo;
		}

		public String getTexto() {
			return texto;
		}
	}
}