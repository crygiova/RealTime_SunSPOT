/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import com.sun.spot.peripheral.Spot;
import com.sun.spot.util.IEEEAddress;

import no.ntnu.item.ttm4160.sunspot.communication.Communications;
import no.ntnu.item.ttm4160.sunspot.communication.Message;

/**
 * @author christiangiovanelli
 *
 */
public class ServerFSM extends StateMachine {

	
	private final String READY_ST = "ready";
	private final String WAIT_ST = "wait_response";
	private final String SEND_ST = "sending";
	
	private State ready;
	private State wait_resp;
	private State send;
	/**
	 * 
	 */
	public ServerFSM(String iD) {
		//initialization of the id 
		this.ID = iD;//assign the id to the state machine
		//initialization of the States of the FSM
		ready = new State(READY_ST,1);
		wait_resp = new State(WAIT_ST,2);
		send = new State(SEND_ST,3);
		//setting the initial state
		this.currentState=this.initialState;
		//initialize the coomunications obj
		this.communicate = new Communications (new IEEEAddress(Spot.getInstance().getRadioPolicyManager().getIEEEAddress()).asDottedHex());
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
