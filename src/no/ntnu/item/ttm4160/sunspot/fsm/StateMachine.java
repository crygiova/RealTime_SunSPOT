/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import java.io.IOException;

import no.ntnu.item.ttm4160.sunspot.communication.*;
import no.ntnu.item.ttm4160.sunspot.timers.SpotTimer;

import com.sun.spot.peripheral.Spot;
import com.sun.spot.util.IEEEAddress;
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
	protected Communications communicate; 
	
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
	 * 
	 * @param msg
	 */
	public void addInputQueue(Message msg)
	{
		this.inputQueue.put(msg);
	}
	
	/**
	 * 
	 * @param msg
	 */
	protected void addSavedQueue(Message msg)
	{
		this.saveMsgQueue.put(msg);
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
	 * 
	 * @throws IOException 
	 */
	public abstract void transition() throws IOException;
	
	public abstract void timeoutTimer(SpotTimer timeout) throws IOException;
	
	protected SpotTimer createTimer(String timerID)//TODO I think that timer ID should be a final variable in each the FSM,bc we know the names of our timers
	{
		return new SpotTimer(this.ID,timerID);
		
	}
	
	protected SpotTimer createTimer(String timerID,long delay)//TODO I think that timer ID should be a final variable in each the FSM,bc we know the names of our timers
	{
		return new SpotTimer(this.ID,timerID,delay);
		
	}
	
	/**
	 * 
	 * @return the format of the strinn that has to be in the sender part of the msg
	 */
	protected String getMySender()
	{
		return new IEEEAddress(Spot.getInstance().getRadioPolicyManager().getIEEEAddress()).asDottedHex()+":"+this.ID;
	}
	
	

}
