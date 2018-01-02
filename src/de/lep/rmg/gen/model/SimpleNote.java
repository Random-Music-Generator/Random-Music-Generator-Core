package de.lep.rmg.gen.model;

/**
 * @author paul
 * @since 02.01.18.
 */
public class SimpleNote {

	public static final int C = 0, CIS = 1, D = 2, DIS = 3, E = 4, F = 5,
			FIS = 6, G = 7, GIS = 8, A = 9, AIS = 10, B = 11;

	protected int pitch;
	protected float volume;


	public SimpleNote(int pitch, float volume) {
		this.pitch = pitch;
		this.volume = volume;
	}

	public SimpleNote(int pitch) {
		this.pitch = pitch;
	}

	public void add(Interval interval, Chord key) {
		// TODO Add interval to pitch
	}

	public void add(Step step) {
		this.pitch += step.getDistance();
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public int getPitch() {
		return pitch;
	}

	public float getVolume() {
		return volume;
	}

	public SimpleNote cloneNote() {
		return new SimpleNote(pitch, volume);
	}

	@Override
	public String toString() {
		return "SimpleNote{" +
				"pitch=" + pitch +
				", volume=" + volume +
				'}';
	}
}
