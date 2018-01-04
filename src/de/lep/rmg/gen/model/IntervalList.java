package de.lep.rmg.gen.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author paul
 * @since 02.01.18.
 */
public class IntervalList extends ArrayList<Interval> {


	public IntervalList() {}

	public IntervalList(ArrayList<Interval> intervals) {
		super(intervals);
	}

	public IntervalList(Interval[] intervals) {
		super(Arrays.asList(intervals));
	}
}
