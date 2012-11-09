/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import java.io.IOException;

import no.ntnu.item.ttm4160.sunspot.SunSpotUtil;
import no.ntnu.item.ttm4160.sunspot.communication.*;
import no.ntnu.item.ttm4160.sunspot.timers.SpotTimer;

import com.sun.spot.util.Queue;

/**
 * @author christiangiovanelli
 *
 */
public abstract class StateMachine {
	
	protected final String INIT_ST = "init";
	
	protected final State initialState = new State(INIT_ST,0);
	
	/**Id of the FSM*/
	protected String ID;
	/**Input Queue*/
	protected Queue inputQueue;
	/**Save messages Queue*/
	protected Queue saveMsgQueue;
	/**Current state*/
	protected State currentState;
	/**Communication object*/
	protected Communications communicate; 
	
	
	public StateMachine()
	{
		//initializing the queues
		inputQueue= new Queue();
		saveMsgQueue= new Queue();
	}
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
	//	System.out.println("ADD-INPUT-QUEUE: ID: "+this.ID+" Size: "+inputQueue.size());
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
	
	protected SpotTimer createTimer(String timerID)
	{
		return new SpotTimer(this.ID,timerID);
		
	}
	
	protected SpotTimer createTimer(String timerID,long delay)
	{
		return new SpotTimer(this.ID,timerID,delay);
		
	}
	
	/**
	 * 
	 * @return the format of the strinn that has to be in the sender part of the msg
	 */
	protected String getMySender()
	{
		return SunSpotUtil.getMyMac()+":"+this.ID;
	}	

}
