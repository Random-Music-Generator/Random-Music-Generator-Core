package de.lep.rmg.gen.model;

import de.lep.rmg.out.model.MidiNote;

/**
 * SequenceNotes don't store their start.
 * They have to be added to a {@link SequencePart},
 * which will convert them to {@link MidiNote}s and add a start.
 *
 * @author paul
 * @since 02.01.18.
 */
public class SequenceNote extends SimpleNote {

	protected int duration;


	public SequenceNote(int pitch, float volume, int duration) {
		super(pitch, volume);
		this.duration = duration;
	}

	public SequenceNote(int pitch, int duration) {
		super(pitch);
		this.duration = duration;
	}

	public MidiNote convert2Midi(int start) {
		return new MidiNote(pitch, volume, start, duration);
	}
	
	public int getDuration(){
		return duration;
	}

	@Override
	public String toString() {
		return "SequenceNote{" +
				"pitch=" + pitch +
				", volume=" + volume +
				", duration=" + duration +
				'}';
	}
}
