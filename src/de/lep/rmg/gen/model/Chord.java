package de.lep.rmg.gen.model;

/**
 * A Child of {@link SimpleNoteList} which also stores a base-Note
 * and the {@link Interval}s the notes form.
 *
 * @author paul
 * @since 02.01.18.
 */
public class Chord extends SimpleNoteList {

	public enum ChordType {
		MAJOR, MINOR
	}

	protected SimpleNote base;
	protected IntervalList intervals;


	public Chord(SimpleNote base, ChordType type) {
		this.base = base;
		// TODO set Intervals
		// TODO set notes
	}

	public Chord(SimpleNote base, IntervalList intervals, Chord key) {
		super();
		this.base = base;
		this.intervals = intervals;

		add(base);
		SimpleNote last = base.cloneNote();
		for (Interval interval : intervals) {
			last.add(interval, key);
			add(last);
			last = last.cloneNote();
		}
	}

	public void setBase(SimpleNote base) {
		this.base = base;
	}

	public void setIntervals(IntervalList intervals) {
		this.intervals = intervals;
	}

	public SimpleNote getBase() {
		return base;
	}

	public IntervalList getIntervals() {
		return intervals;
	}

	@Override
	public String toString() {
		return "Chord{" +
				"base=" + base +
				", intervals=" + intervals +
				'}';
	}
}
