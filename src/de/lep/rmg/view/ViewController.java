package de.lep.rmg.view;

import de.lep.rmg.out.midi.MidiPlayer;
import de.lep.rmg.out.midi.SequenceGenerator;
import de.lep.rmg.out.model.MidiSong;

import javax.sound.midi.Sequence;
import java.io.File;
import java.util.Arrays;

/**
 * Controls the IOInterface and MidiPlayer.<br>
 * Actions regarding the Generation of a Song will be passed to a registered {@link ViewListener}.<br>
 * Has to be initialized with {@link ViewController#init()}
 *
 * @author paul
 * @since 29.12.17.
 */
public class ViewController implements IOInterface.IOListener {

	private static IOInterface.Command genSong, setConfig, playSong, stopSong, saveSong;

	private static boolean hasConfig = false;
	private static MidiPlayer player;
	private static Sequence seq;
	private static ViewListener listener;


	/**
	 * Initializes the View.
	 */
	public static void init() {
		genSong = new IOInterface.Command("generate",
				"Generates a new Song with the last given configuration");
		setConfig = new IOInterface.Command("config",
				"Opens a dialog for setting the Song-Configuration");
		playSong = new IOInterface.Command("play",
				"Plays the generated Song");
		stopSong = new IOInterface.Command("stop",
				"Stops the generated Song");
		saveSong = new IOInterface.Command("save",
				"Saves the generated Song as Midi");
		saveSong.addStringParam("file",
				"The location where the song should be saved. " +
						"Format: path/to/file/filename.mid");

		IOInterface.Command[] commands = {
				genSong, setConfig, playSong, stopSong, saveSong
		};
		player = new MidiPlayer();

		IOInterface.addCommands(Arrays.asList(commands));
		IOInterface.setListener(new ViewController());
		IOInterface.init();
		IOInterface.run();
	}

	/**
	 * Sets a new Listener
	 * @param listener The Listener to set
	 */
	public static void setViewListener(ViewListener listener) {
		ViewController.listener = listener;
	}

	@Override
	public void command(IOInterface.Command command, IOInterface.ValueParam[] params) {

		// Switch does not work ):
		if (command.equals(genSong)) {

			if (!hasConfig) {
				IOInterface.printError("You have to set a configuration first");
			} else {
				if (listener != null) {
					MidiSong song = listener.generateSong();
					seq = SequenceGenerator.createSequence(song);
					player.loadSequence(seq);
				}
			}
		} else if (command.equals(setConfig)) {
			if (listener != null && hasConfig) {
				listener.printConfig();
			}
			if (!hasConfig ||
					IOInterface.getInputBoolean(
							"Do you want to override the old configuration? (y/n)")) {
				if (IOInterface.getInputBoolean(
						"Do you want to generate a random configuration? (y/n)")) {
					if (listener != null) {
						hasConfig = listener.setRandomConfig();
					}
				} else {
					if (listener != null) {
						hasConfig = listener.printSetConfigDialog();
					}
				}
			}
		} else if (command.equals(playSong)) {
			if (player.sequenceLoaded()) {
				player.start();
			} else {
				IOInterface.printError("No Song loaded. Generate a Song first.");
			}
		} else if (command.equals(stopSong)) {
			if (player.sequenceLoaded()) {
				player.stop();
			} else {
				IOInterface.printError("No Song loaded. Generate a Song first.");
			}
		} else if (command.equals(saveSong)) {
			String location = params[0].getValueS();
			File file = new File(location);
			SequenceGenerator.saveSequence(seq, file);
			IOInterface.printMessage("Saved Sequence");
		} else {
			IOInterface.printError("Invalid command");
		}
	}

	@Override
	public void close() {
		if (listener != null) {
			listener.close();
		}
		player.stop();
		player.close();
	}
}
