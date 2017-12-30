package de.lep.rmg.out.model;

/**
 * @author paul
 * @since 29.12.17.
 */
public class MidiInstrument {

	public static final int ACOUSTIC_GUITAR = 26,
							ACOUSTIC_BASS = 33,
							CELLO = 43,
							FLUTE = 74,
							ORGAN = 20,
							PIANO = 1,
							TENOR_SAX = 67,
							TRUMPET = 57,
							XYLOPHONE = 14;

	private String name, shortName;
	private int midiProgram;
	private float volume;


	public MidiInstrument(String name, String shortName, int midiProgram, float volume) {
		this.name = name;
		this.shortName = shortName;
		this.midiProgram = midiProgram;
		this.volume = volume;
	}

	public MidiInstrument(String name, String shortName, int midiProgram) {
		this.name = name;
		this.shortName = shortName;
		this.midiProgram = midiProgram;
		this.volume = 80;
	}

	public static MidiInstrument getInstrument(int type) {
		return getInstrument(type, 80);
	}

	public static MidiInstrument getInstrument(int type, float volume) {
		switch (type) {
			case ACOUSTIC_BASS:
				return new MidiInstrument("Acoustic Bass", "Bass", type, volume);
			case ACOUSTIC_GUITAR:
				return new MidiInstrument("Acoustic Guitar", "Git", type, volume);
			case CELLO:
				return new MidiInstrument("Cello", "Cel", type, volume);
			case FLUTE:
				return new MidiInstrument("Flute", "Flu", type, volume);
			case ORGAN:
				return new MidiInstrument("Organ", "Org", type, volume);
			case PIANO:
				return new MidiInstrument("Piano", "Pia", type, volume);
			case TRUMPET:
				return new MidiInstrument("Trumpet", "Tru", type, volume);
			case TENOR_SAX:
				return new MidiInstrument("Tenor Saxophone",
						"TSax", type, volume);
			case XYLOPHONE:
				return new MidiInstrument("Xylophone", "Xyl", type, volume);
			default:
				return new MidiInstrument("Unnamed", "Unn", type, volume);
		}
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public int getMidiProgram() {
		return midiProgram;
	}

	public float getVolume() {
		return volume;
	}

	@Override
	public String toString() {
		return String.format(
				"Instrument: {%s (%s); Midi-Program: %d, Volume: %f}",
				name, shortName, midiProgram, volume
		);
	}
}
