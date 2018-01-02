package de.lep.rmg.gen.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author paul
 * @since 02.01.18.
 */
public class SimpleNoteList extends ArrayList<SimpleNote>{


	public SimpleNoteList() {}

	public SimpleNoteList(ArrayList<SimpleNote> notes) {
		super(notes);
	}

	public SimpleNoteList(SimpleNote[] notes) {
		super(Arrays.asList(notes));
	}
}
