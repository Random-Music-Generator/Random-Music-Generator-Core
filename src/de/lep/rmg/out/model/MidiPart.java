package de.lep.rmg.out.model;

import java.util.ArrayList;

/**
 * @author paul
 * @since 29.12.17.
 */
public class MidiPart extends ArrayList<MidiNote> {

	private MidiInstrument instrument;


	public MidiPart(MidiInstrument instrument) {
		this.instrument = instrument;
	}

	public MidiInstrument getInstrument() {
		return instrument;
	}
}
