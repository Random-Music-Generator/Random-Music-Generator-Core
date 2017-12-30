package de.lep.rmg.out.model;

import java.util.HashMap;
import java.util.Map;

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

	private static Map<Integer, String> instruments = new HashMap<>();

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
		Map<Integer, String> instr = getInstruments();
		switch (type) {
			case ACOUSTIC_BASS:
				return new MidiInstrument(instr.get(ACOUSTIC_BASS), "Bass", type, volume);
			case ACOUSTIC_GUITAR:
				return new MidiInstrument(instr.get(ACOUSTIC_GUITAR), "Git", type, volume);
			case CELLO:
				return new MidiInstrument(instr.get(CELLO), "Cel", type, volume);
			case FLUTE:
				return new MidiInstrument(instr.get(FLUTE), "Flu", type, volume);
			case ORGAN:
				return new MidiInstrument(instr.get(ORGAN), "Org", type, volume);
			case PIANO:
				return new MidiInstrument(instr.get(PIANO), "Pia", type, volume);
			case TRUMPET:
				return new MidiInstrument(instr.get(TRUMPET), "Tru", type, volume);
			case TENOR_SAX:
				return new MidiInstrument(instr.get(TENOR_SAX),
						"TSax", type, volume);
			case XYLOPHONE:
				return new MidiInstrument(instr.get(XYLOPHONE), "Xyl", type, volume);
			default:
				return new MidiInstrument("Unnamed", "Unn", type, volume);
		}
	}

	public static Map<Integer, String> getInstruments() {
		if (instruments.size() == 0) {
			instruments.put(26, "Acoustic Guitar");
			instruments.put(26, "Acoustic Bass");
			instruments.put(26, "Cello");
			instruments.put(26, "Flute");
			instruments.put(26, "Organ");
			instruments.put(26, "Piano");
			instruments.put(26, "Tenor Saxophone");
			instruments.put(26, "Trumpet");
			instruments.put(26, "Xylophone");
		}
		return instruments;
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
