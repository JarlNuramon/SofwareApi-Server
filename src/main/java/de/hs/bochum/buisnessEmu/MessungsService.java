package de.hs.bochum.buisnessEmu;

import java.io.*;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hs.bochum.messung.Messreihe;
import de.hs.bochum.messung.Messung;
import de.hs.bochum.repository.MessungsRepository;
import net.sf.yad2xx.FTDIException;

@Service
public class MessungsService {
	boolean messung = false;
	@Autowired
	private MessungsRepository messRepository;
	
	
	public Optional<Messreihe> getMessungById(int id) {
		return messRepository.findById(id);
	}
	
	
	public Messreihe startMessung(int zeitIntervall, String verbraucher,String messgroesse, int id) {
		messung = true;
		Messreihe m = Messreihe.builder().messreihenId(id).elements(new HashSet<>()).zeitintervall(zeitIntervall)
				.verbraucher(verbraucher).messgroesse(messgroesse).build();
		new Thread() {
			@Override
			public void run() {
				try {
					EmuCheckConnection ecc = new EmuCheckConnection();
					ecc.connect();
					while (messung) {
						ecc.sendRequest(MESSWERT.valueOf(messgroesse));
						Thread.sleep(zeitIntervall * 100);
						m.getElements().add(Messung.builder().parent(m).wert(ecc.gibErgebnisAus()).build());
					}
				} catch (Exception e) {
				}

			}
		}.start();
		return m;
	}
	public void stopMessung(Messreihe m) {
		messung = false;
		messRepository.save(m);
	}

	private static void main(String[] args) {
		try {
			boolean b = true;
			EmuCheckConnection ecc = new EmuCheckConnection();
			BufferedReader ein = new BufferedReader(new InputStreamReader(System.in));
			while (b) {
				String eingabe = ein.readLine();
				switch (eingabe) {
				case "exit":
					ecc.disconnect();
					b = false;
					break;
				case "connect":
					ecc.connect();
					break;
				case "pmode":
					ecc.sendProgrammingMode();
					break;
				case "seriennummer":
					ecc.sendRequest(MESSWERT.Seriennummer);
					break;
				case "text":
					ecc.sendRequest(MESSWERT.Text);
					break;
				case "messen":
					ecc.sendRequest(MESSWERT.Leistung);
					break;
				default:
					System.out.println("Falscher Befehl!");
					break;
				}
			}
		} catch (FTDIException ftdiExc) {
			System.out.println("FTDIException");
			ftdiExc.printStackTrace();
		} catch (IOException ioExc) {
			System.out.println("IOException");
			ioExc.printStackTrace();
		}
	}
}
