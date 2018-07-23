package de.lep.rmg.out.model;

import de.lep.rmg.gen.model.SequenceNote;

/**
 * @author paul
 * @since 29.12.17.
 */
public class MidiNote extends SequenceNote {

	protected int start;


	public MidiNote(int pitch, float volume, int start, int duration) {
		super(pitch, volume, duration);
		this.start = start;
	}

	public MidiNote(int pitch, int start, int duration) {
		super(pitch, duration);
		this.start = start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return start;
	}

	@Override
	public String toString() {
		return "MidiNote{" +
				"start=" + start +
				", duration=" + duration +
				'}';
	}
}
