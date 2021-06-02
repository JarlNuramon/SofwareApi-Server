package de.hs.bochum.entities.messung;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessreiheResponse {
	private List<Messreihe> messungen;
}
