package de.hs.bochum.buisness.services;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hs.bochum.buisness.emu.EmuCheckConnection;
import de.hs.bochum.buisness.emu.MESSWERT;
import de.hs.bochum.entities.messung.Messreihe;
import de.hs.bochum.entities.messung.Messung;
import de.hs.bochum.repository.MessungsRepository;
import net.sf.yad2xx.FTDIException;

@Service
public class MessungsService {
	boolean messung = false;
	@Autowired
	private MessungsRepository messRepository;
	
	
	public List<Messreihe> getAlleMessungen(){
		return messRepository.findAll();
	}
	public Optional<Messreihe> getMessungById(int id) {
		return messRepository.findById(id);
	}
	
	public void erstellMessung(int zeitIntervall, String verbraucher,String messgroesse, int id) {
		if(messRepository.findById(id).isPresent())
			messRepository.deleteById(id);
		Messreihe m = Messreihe.builder().messreihenId(id).elements(new ArrayList<>()).zeitintervall(zeitIntervall)
				.verbraucher(verbraucher).messgroesse(messgroesse).build();
		messRepository.save(m);
	}
	public Messreihe startMessung(Messreihe m) {
		messung = true;
		new Thread() {
			@Override
			public void run() {
				EmuCheckConnection ecc = null;
				try {
					ecc= new EmuCheckConnection();
					ecc.connect();
					Thread.sleep(1000);
					ecc.sendProgrammingMode();
					Thread.sleep(1000);
					while (messung) {
						ecc.sendRequest(MESSWERT.valueOf(m.getMessgroesse()));
						Thread.sleep(m.getZeitintervall() * 100);
						m.getElements().add(Messung.builder().parent(m).wert(ecc.gibErgebnisAus()).laufendeNummer(m.getElements().size()).build());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if(ecc!=null)
						try {
							ecc.disconnect();
						} catch (FTDIException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}

			}
		}.start();
		return m;
	}
	public void stopMessung(Messreihe m) {
		messung = false;
		messRepository.save(m);
	}

//	public static void main(String[] args) {
//		try {
//			boolean b = true;
//			EmuCheckConnection ecc = new EmuCheckConnection();
//			BufferedReader ein = new BufferedReader(new InputStreamReader(System.in));
//			while (b) {
//				String eingabe = ein.readLine();
//				switch (eingabe) {
//				case "exit":
//					ecc.disconnect();
//					b = false;
//					break;
//				case "connect":
//					ecc.connect();
//					break;
//				case "pmode":
//					ecc.sendProgrammingMode();
//					break;
//				case "seriennummer":
//					ecc.sendRequest(MESSWERT.Seriennummer);
//					break;
//				case "text":
//					ecc.sendRequest(MESSWERT.Text);
//					break;
//				case "messen":
//					ecc.sendRequest(MESSWERT.Scheinleistung);
//					break;
//				default:
//					System.out.println("Falscher Befehl!");
//					break;
//				}
//			}
//		} catch (FTDIException ftdiExc) {
//			System.out.println("FTDIException");
//			ftdiExc.printStackTrace();
//		} catch (IOException ioExc) {
//			System.out.println("IOException");
//			ioExc.printStackTrace();
//		}
	
	public void deleteMessreihe(int id) {
		messRepository.deleteById(id);
		
	}
}
