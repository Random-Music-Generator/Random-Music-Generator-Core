package de.lep.rmg.out.model;

/**
 * @author paul
 * @since 29.12.17.
 */
public class MidiConfig {

	private int measureDivision;


	public MidiConfig(int measureDivision) {
		this.measureDivision = measureDivision;
	}

	public int getMeasureDivision() {
		return measureDivision;
	}
}
