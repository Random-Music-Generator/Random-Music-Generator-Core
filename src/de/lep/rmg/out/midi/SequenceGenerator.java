package de.lep.rmg.out.midi;

import de.lep.rmg.out.model.MidiSong;
import de.lep.rmg.out.model.MidiPart;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import java.io.File;
import java.io.IOException;

/**
 * Bietet Methoden zum Erschaffen, Speichern und Laden von {@link Sequence}n
 * 
 * @author Lukas
 */
public class SequenceGenerator {
	
	/**
	 * Erschafft eine {@link Sequence} aus dem gegebenen {@link MidiSong},
	 * falls <code>miditype == false</code><br>
	 * Jeder {@link MidiPart} wird als eigener {@link Track} gespeichert<br>
	 * falls miditype == true<br>
	 * 	jeder {@link MidiPart} wird auf dem selben {@link Track} aber in einem anderen Channel gespeichert.
	 *
	 * @param song Der Song der zur Sequence gemacht werden soll.
	 * @return Sequence
	 */
	public static Sequence createSequence(MidiSong song){
		Sequence seq = null;
		try {
			seq = new Sequence(Sequence.PPQ, song.getConfig().getMeasureDivision());
			TrackFactory.createTracks(seq, song);
		} catch (InvalidMidiDataException e) {
			System.out.println("Failed to create Sequence");
			e.printStackTrace();
		}
		return seq;
	}
	
	/**
	 * speichert eine {@link Sequence}, als midi0- oder midi1-Datei, je nach Anzahl an {@link Track}s auf der Sequence
	 * @param seq The Sequence to save
	 * @param outputFile The File to save to
	 */
	public static void saveSequence(Sequence seq, File outputFile){
		if (seq.getTracks().length == 1){
			try {
				MidiSystem.write(seq, 0, outputFile);
			} catch (IOException e) {
				System.out.println("Failed to save Sequence " + seq + " in File "+ outputFile);
				e.printStackTrace();
			}
		} else {
			try {
				MidiSystem.write(seq, 1, outputFile);
			} catch (IOException e) {
				System.out.println("Failed to save Sequence " + seq + " in File "+ outputFile);
				e.printStackTrace();
			}
		}
	}
	
}
