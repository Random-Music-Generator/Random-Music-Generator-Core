package de.lep.rmg.out.model;

/**
 * @author paul
 * @since 29.12.17.
 */
public class MidiNote {

	private int start, duration;
	private int pitch;
	private float volume;


	public MidiNote(int start, int duration, int pitch, float volume) {
		this.start = start;
		this.duration = duration;
		this.pitch = pitch;
		this.volume = volume;
	}

	public MidiNote(int start, int duration, int pitch) {
		this.start = start;
		this.duration = duration;
		this.pitch = pitch;
		this.volume = 100f;
	}

	public int getStart() {
		return start;
	}

	public int getDuration() {
		return duration;
	}

	public int getPitch() {
		return pitch;
	}

	public float getVolume() {
		return volume;
	}
}
