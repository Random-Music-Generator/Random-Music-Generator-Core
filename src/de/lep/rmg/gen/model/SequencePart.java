package de.lep.rmg.gen.model;

import de.lep.rmg.out.model.MidiInstrument;
import de.lep.rmg.out.model.MidiPart;

import java.util.ArrayList;

/**
 * @author paul
 * @since 02.01.18.
 */
public class SequencePart extends ArrayList<SequenceNote> {

	protected MidiInstrument instrument;


	public SequencePart(MidiInstrument instrument) {
		this.instrument = instrument;
	}

	public int getDuration() {
		int duration = 0;
		for (SequenceNote note : this) {
			duration += note.getDuration();
		}
		return duration;
	}

	public MidiPart convert2Midi() {
		MidiPart midi = new MidiPart(this.instrument);
		int counter = 0;
		for (SequenceNote note : this) {
			midi.add(note.convert2Midi(counter));
			counter += note.getDuration();
		}
		return midi;
	}
}
