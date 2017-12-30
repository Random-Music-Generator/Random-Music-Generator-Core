package de.lep.rmg.out.midi;

import javax.sound.midi.*;
import java.util.ArrayList;

/**
 * Wrapperklasse für einen {@link Sequencer}, der vom {@link MidiSystem} übergeben wird.<br>
 * Verwaltet eine Beobachterliste. Alle Beobachter({@link IPlayerObserver}) werden informiert,
 * wenn eine Sequence geladen wird oder wenn der {@link Sequencer} zu spielen beginnt oder stoppt.<br>
 * Diese Klasse bietet keinen direkten Zugriff auf den {@link Sequencer},
 * sondern nur indirekt durch ihre Methoden.
 */
public class MidiPlayer{

	/**
	 * das Objekt das die Musik spielt
	 * @see Sequencer
	 */
	private Sequencer player;
	
	//Beobachter die bei Zustandsänderung informiert werden
	private ArrayList<IPlayerObserver> observers;
	
	//Standardkonstruktor
	public MidiPlayer(){
		try {
			//fordert ein Sequencerobjekt vom MidiSystem an
			player = MidiSystem.getSequencer();
			//startet den Sequncer
			player.open();
		} catch (MidiUnavailableException e) {
			//sollte im normalen Programmablauf nicht passieren, da nur ein MidiPlayer benötigt und benutzt wird
			System.out.println("Unable to get Sequencer!");
			e.printStackTrace();
		}
		observers = new ArrayList<IPlayerObserver>();
	}
	
	/**
	 * fängt an, die übergebene {@link Sequence} zu spielen
	 * @param seq
	 */
	public void play(Sequence seq){
		try {
			player.setSequence(seq);
			notifySequenceState();
			
			try {
				Thread.sleep( 150 );//results in better sound quality
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
			
			player.start();
			notifyRunningState();
			
			playingCheck();
			
		} catch (InvalidMidiDataException e) {
			System.out.println("Invalid Sequence " + seq);
			e.printStackTrace();
		}
	}
	
	/**
	 * lädt eine {@link Sequence} in den MidiPlayer
	 * @param seq
	 * @return boolean true bei Erfolg
	 */
	public boolean loadSequence(Sequence seq){
		if(player.isRunning()){
			return false;
		}else{
			try {
				player.setSequence(seq);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
	
	/**
	 * startet den MidiPlayer falls eine Sequence geladen ist
	 */
	public void start(){
		player.start();
		notifyRunningState();
		playingCheck();
	}
	
	/**
	 * überprüft, ob der Player immer noch spielt oder das ende der Sequence erreciht hat. Dann wird er gestoppt.
	 */
	private void playingCheck() {
		new Thread(new PlayingCheckJob()).start();
	}
	
	private class PlayingCheckJob implements Runnable {

		@Override
		public void run() {
			while( player.isRunning() ){
				try {
					//stops the thread for at least 500ms, to prevent unnecessary examination
					Thread.sleep( 500 );
				} catch ( InterruptedException e ) {
//					stop();
//					setTickPosition(0);
					System.out.println("System Error: sleeping Thread interupted");
					e.printStackTrace();
				}
				//as long the player is running continue the loop
				continue;
			}
			//when the music has stopped:
			stop();
			//'rewind' the Sequence
			setTickPosition(0);
		}
		
	}
	
	/**
	 * stoppt den MidiPlayer und benachrichtigt Beobachter
	 */
	public void stop(){
		if(player.isOpen()){
			player.stop();
			notifyRunningState();
		}
	}
	
	public boolean isOpen(){
		return player.isOpen();
	}
	
	/**
	 * setzt die Position an welcher der MidiPlayer zu spielen anfängt
	 * 0 = Anfang
	 * @param tick
	 */
	public void setTickPosition(long tick){
		player.setTickPosition(tick);
	}
	
	public long getTickPosition(){
		return player.getTickPosition();
	}
	
	/**
	 * schließt den MidiPlayer
	 */
	public void close(){
		player.close();
	}
	
	/**
	 * setzt die Abspielgeschwindigkeit in Beats pro Minute
	 * @param bpm
	 */
	public void setTempoInBPM(float bpm){
		player.setTempoInBPM(bpm);
	}
	
	/**
	 * @return current musictempo in beats per minute
	 */
	public float getTempoInBPM(){
		return player.getTempoInBPM();
	}
	
	/** 
	 * @return running state
	 */
	public boolean isRunning(){
		return player.isRunning();
	}
	
	/**
	 * @return true if as Sequence is loaded
	 */
	public boolean sequenceLoaded(){
		if(player.getSequence() == null)
			return false;
		else
			return true;
	}
	
	/**
	 * 
	 * @return a String indicating how many of all Measures are played
	 */
	public String getPlayingTime(){
		if(!player.isRunning()){
			return new String("-- / --");
		}else{
			long totalLength = 0;
			if(player.getSequence() != null){
				for(Track track: player.getSequence().getTracks()){
					if(track.ticks() > totalLength){
						totalLength = track.ticks();
					}
				}
			}
			return new String((int)player.getTickPosition()/32/**player.getTickLength()/1000*/ + " / " + (int)totalLength/32/**player.getTickLength()/1000*/);
		}
	}
	
	/**
	 * fügt playOb der Beobachterliste hinzu
	 * @param playOb
	 */
	public void addObserver(IPlayerObserver playOb){
		observers.add(playOb);
	}
	
	/**
	 * entfernt playOb aus der Beobachterliste
	 * @param playOb
	 */
	public void removeObserver(IPlayerObserver playOb){
		observers.remove(playOb);
	}
	
	/**
	 * benachrichtigt Beobachter darüber, ob der MidiPlayer spielt 
	 */
	private void notifyRunningState(){
		for(IPlayerObserver playobs: observers){
			playobs.playingStateChanged(player.isRunning());
		}
	}
	
	/**
	 * benachrichtigt Beobachter darüber, ob eine Sequence geladen ist
	 */
	private void notifySequenceState(){
		for(IPlayerObserver playobs: observers){
			playobs.sequenceStateChanged(sequenceLoaded());
		}
	}
}
