package de.lep.rmg.out.midi;

import javax.sound.midi.Sequence;

/**
 * Interface für MidiPlayerObserver. Das implementierende Objekt wird informiert,
 * falls der {@link MidiPlayer} zu spielen beginnt oder stoppt und
 * falls eine {@link Sequence} geladen oder entfernt wird.
 */
public interface IPlayerObserver {
	void playingStateChanged(boolean playing);
	void sequenceStateChanged(boolean loaded);
}
