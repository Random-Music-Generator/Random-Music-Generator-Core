package de.lep.rmg.view;

import de.lep.rmg.out.model.MidiSong;

/**
 * Listens for Actions by the {@link ViewController}
 *
 * @author paul
 * @since 30.12.17.
 */
public interface ViewListener {

	/**
	 * A new Song should be generated.
	 * @return The generated Song
	 */
	MidiSong generateSong();

	/**
	 * The Song-Config should be printed
	 */
	void printConfig();

	/**
	 * A Random Song-Config should be created
	 * @return Whether a new Config got created
	 */
	boolean setRandomConfig();

	/**
	 * A Dialog which allows the User to set a new Config should be printed
	 * @return Whether a new Config go created
	 */
	boolean printSetConfigDialog();

	/**
	 * The Interface will be closed
	 */
	void close();
}
