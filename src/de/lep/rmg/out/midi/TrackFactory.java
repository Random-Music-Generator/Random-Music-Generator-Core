package de.lep.rmg.out.midi;

import de.lep.rmg.out.model.MidiNote;
import de.lep.rmg.out.model.MidiPart;
import de.lep.rmg.out.model.MidiSong;

import javax.sound.midi.*;

/**
 * stellt Factorymethoden und Änderungsmethoden für {@link Track}s zur Verfügung.
 * 
 * @author Lukas
 * 
 */
class TrackFactory {
	
	/**
	 * Macht einen neuen {@link Track} in dem die übergebenen {@link MidiPart}s
	 * (In Form eines {@link MidiSong}s) repräsentiert sind.
	 *
	 * @param seq Die Sequence auf der der Track erstellt wird.
	 * @param song Der {@link MidiSong} aus dem der Track erstellt werden soll.
	 */
	static void createTrack(Sequence seq, MidiSong song){
		Track track = seq.createTrack();
		TrackFactory.MidiSongIteration(song, track);
	}
	
	/**
	 * Erstellt einen neuen Track auf der Sequence für jeden {@link MidiPart} des {@link MidiSong}s
	 *
	 * @param seq Die Sequence auf der der Track erstellt wird.
	 * @param song Der {@link MidiSong} aus dem der Track erstellt werden soll.
	 */
	static void createTracks(Sequence seq, MidiSong song){
		byte channel = 0;
		for(MidiPart part : song){
			TrackFactory.createTrack(seq, part, channel);
			channel++;
		}
	}

	/**
	 * Macht einen neuen {@link Track} auf der {@link Sequence} und fügt den {@link MidiPart}
	 * auf dem angegebenen Channel hinzu.
	 *
	 * @param seq Die Sequence auf der der Track erstellt wird.
	 * @param part Der {@link MidiPart} aus dem der Track erstellt werden soll.
	 * @param channel Der Channel, auf den der Part hinzugefügt werden soll.
	 */
	private static void createTrack(Sequence seq, MidiPart part, byte channel){
		Track track = seq.createTrack();
		TrackFactory.eventMaking(track, part, channel);
	}
	
	/**
	 * Iterates through a {@link MidiSong} and adds every {@link MidiPart}
	 * on a new channel of the given track.
	 *
	 * @param song Der {@link MidiSong} aus dem der Track erstellt werden soll.
	 * @param track Der Track auf dem der Song gespeichert wird.
	 */
	private static void MidiSongIteration(MidiSong song, Track track){
		byte channel = 1;
		for (MidiPart part : song) {
			TrackFactory.eventMaking(track, part, channel);
			channel++;
		}
	}
	
	/**
	 * Adds a {@link MidiPart} on a specific channel of the given track
	 *
	 * @param track The Track where the Part will be written
	 * @param part The Part to write
	 * @param channel The Channel of the Part
	 */
	private static void eventMaking(Track track, MidiPart part, byte channel){
		float volume = part.getInstrument().getVolume();
		
		ShortMessage sm = new ShortMessage();
		try {
			sm.setMessage(192, channel, part.getInstrument().getMidiProgram(), 0);
			track.add(new MidiEvent(sm, 0));
		} catch (InvalidMidiDataException e) {
			System.err.println("Failed to build MIDI-Track: Could not set Midi-Program");
			e.printStackTrace();
		}
		
		try {
			for (MidiNote note : part) {
				track.add(new MidiEvent(
						new ShortMessage(144, channel, note.getPitch(),
								(int) (note.getVolume() * volume) / 100),
						note.getStart()));
				track.add(new MidiEvent(
						new ShortMessage(128, channel, note.getPitch(),
								(int) (note.getVolume() * volume) / 100),
						note.getStart() + note.getDuration()));
			}
		} catch (InvalidMidiDataException e) {
			System.err.println("Failed to build MIDI-Track: Could not add Notes");
			e.printStackTrace();
		}
	}
	
}
