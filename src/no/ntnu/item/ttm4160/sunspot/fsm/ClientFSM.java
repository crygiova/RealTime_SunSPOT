/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import no.ntnu.item.ttm4160.sunspot.communication.Message;

/**
 * @author christiangiovanelli
 *
 */
public class ClientFSM extends StateMachine {

	private final String FREE_ST="free";
	private final String WAIT_ST="wait_approved";
	private final String BUSY_ST="busy";
	
	
	private State free;
	private State wait_app;
	private State busy;


	/**
	 * 
	 */
	public ClientFSM(String iD) {
		//Id of the FSM
		this.ID = iD;
		//initialization of the States of the FSM
		free = new State(FREE_ST,1);
		wait_app = new State(WAIT_ST,2);
		busy = new State(BUSY_ST,3);
		//setting up the initial state
		this.currentState= this.initialState;//TODO don't know actually if this is correct or not
	}

	public void transition(Message msg) {
		// TODO Auto-generated method stub
		switch(this.currentState.getIdName())
		{	
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
		}
	}
}
