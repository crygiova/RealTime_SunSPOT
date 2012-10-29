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
		//initialize the coomunications obj
		this.communicate = new Communications (new IEEEAddress(Spot.getInstance().getRadioPolicyManager().getIEEEAddress()).asDottedHex());
	}

	public void transition(Message msg) {
		Message out;
		switch(this.currentState.getIdName())
		{	
			case 0://init status
				//subscribe button 2
				break;
			case 1://free status
				if(msg.getContent().compareTo(Message.CanYouDisplayMyReadings)==0)
				{
					out = new Message(getMySender(),msg.getSender(),Message.ICanDisplayReadings);//sending the message to response I can display ur readings
					this.currentState=this.wait_app;
				}
				break;
			case 2://wait approved status
				if(msg.getContent().compareTo(Message.Approved)==0)
				{
					//TODO start timeout
					this.currentState = this.busy;
				}
				else
				{
					if(msg.getContent().compareTo(Message.Denied)==0)
					{
						this.currentState= this.free;
					}
					else 
					if(msg.getContent().compareTo(Message.CanYouDisplayMyReadings)==0)
					{
//							this.saveMsgQueue.
					}
					
					
					
				}
					
				break;
			case 3://busy status
				break;
		}
		
		
	}
	
	private String getMySender()
	{
		return new IEEEAddress(Spot.getInstance().getRadioPolicyManager().getIEEEAddress()).asDottedHex()+":"+this.ID;
	}
}
