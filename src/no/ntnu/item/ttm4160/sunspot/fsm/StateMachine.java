/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import no.ntnu.item.ttm4160.sunspot.communication.Message;
import no.ntnu.item.ttm4160.sunspot.timers.SpotTimer;

import com.sun.spot.util.Queue;

/**
 * @author christiangiovanelli
 *
 */
public abstract class StateMachine {
	
	protected final String INIT_ST = "init";
	
	protected final State initialState = new State(INIT_ST,0);
	
	protected String ID;
	protected Queue inputQueue;
	protected Queue saveMsgQueue;
	protected State currentState;

	
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @return the inputQueue
	 */
	public Queue getInputQueue() {
		return inputQueue;
	}
	/**
	 * @param inputQueue the inputQueue to set
	 */
	public void setInputQueue(Queue inputQueue) {
		this.inputQueue = inputQueue;
	}
	/**
	 * @return the saveMsgQueue
	 */
	public Queue getSaveMsgQueue() {
		return saveMsgQueue;
	}
	/**
	 * @param saveMsgQueue the saveMsgQueue to set
	 */
	public void setSaveMsgQueue(Queue saveMsgQueue) {
		this.saveMsgQueue = saveMsgQueue;
	}
	
	
	/**
	 * This function is the main function of a state machine that,
	 *  after the arrival o a input makes a transition in the state machine
	 * @param msg
	 */
	public abstract void transition(Message msg);
	
	protected SpotTimer createTimer(String timerID)//TODO I think that timer ID shold be a final variable,bc we know the names of our timers
	{
		return new SpotTimer(this.ID,timerID);
		
	}
	
	

}
