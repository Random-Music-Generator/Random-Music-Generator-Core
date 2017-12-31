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

	private static MidiInstrument[] instruments = null;

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
		if (instruments == null) {
			getInstruments();
		}
		for (MidiInstrument inst : instruments) {
			if (inst.getMidiProgram() == type) {
				return new MidiInstrument(inst.getName(),
						inst.getShortName(), type, volume);
			}
		}
		return new MidiInstrument("Unnamed", "Unn", type, volume);
	}

	public static MidiInstrument[] getInstruments() {
		if (instruments == null) {
			instruments = new MidiInstrument[9];
			instruments[0] = new MidiInstrument(
					"Acoustic Bass", "Bass", ACOUSTIC_BASS, 100);
			instruments[1] = new MidiInstrument(
					"Acoustic Guitar", "Guit", ACOUSTIC_GUITAR, 100);
			instruments[2] = new MidiInstrument(
					"Cello", "Cell", CELLO, 100);
			instruments[3] = new MidiInstrument(
					"Flute", "Flut", FLUTE, 100);
			instruments[4] = new MidiInstrument(
					"Organ", "Orga", ORGAN, 100);
			instruments[5] = new MidiInstrument(
					"Piano", "Pian", PIANO, 100);
			instruments[6] = new MidiInstrument(
					"Trumpet", "Trum", TRUMPET, 100);
			instruments[7] = new MidiInstrument(
					"Tenor Saxophone", "TSax", TENOR_SAX, 100);
			instruments[8] = new MidiInstrument(
					"Xylophone", "Xylo", XYLOPHONE, 100);
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
				"Instrument: {%s (%s); Midi-Program: %d, Volume: %d}",
				name, shortName, midiProgram, (int) volume
		);
	}
}
