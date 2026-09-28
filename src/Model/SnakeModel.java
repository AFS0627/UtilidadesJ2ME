package Model;

import javax.microedition.rms.RecordStore;

public class SnakeModel {
	private int recorde;

	public SnakeModel() {
		carregarRecorde();
	}

	public int getRecorde() {
		return recorde;
	}

	public void verificarRecorde(int pontos) {
		if (pontos > recorde) {
			recorde = pontos;
			salvarRecorde();
		}
	}

	private void carregarRecorde() {
		try {
			RecordStore rs = RecordStore.openRecordStore("snake", true);
			if (rs.getNumRecords() > 0) {
				byte[] dados = rs.getRecord(1);
				recorde = Integer.parseInt(new String(dados));
			} else {
				recorde = 0;
			}
			rs.closeRecordStore();
		} catch (Exception e) {
			recorde = 0;
		}
	}

	private void salvarRecorde() {
		try {
			RecordStore rs = RecordStore.openRecordStore("snake", true);
			byte[] dados = Integer.toString(recorde).getBytes();
			if (rs.getNumRecords() == 0) {
				rs.addRecord(dados, 0, dados.length);
			} else {
				rs.setRecord(1, dados, 0, dados.length);
			}
			rs.closeRecordStore();
		} catch (Exception e) {
		}
	}
}