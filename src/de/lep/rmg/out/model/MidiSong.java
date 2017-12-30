package de.lep.rmg.out.model;

import java.util.ArrayList;

/**
 * @author paul
 * @since 29.12.17.
 */
public class MidiSong extends ArrayList<MidiPart> {

	private MidiConfig config;


	public MidiSong(MidiConfig config) {
		this.config = config;
	}

	public MidiConfig getConfig() {
		return config;
	}
}
