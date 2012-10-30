/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import com.sun.spot.peripheral.Spot;
import com.sun.spot.util.IEEEAddress;

import no.ntnu.item.ttm4160.sunspot.SunSpotUtil;
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

	private String receiver;

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
		//TODO input queue & saved message queue?? take a look in the teory
		switch(this.currentState.getIdName())
		{	
			case 0://init status
				//subscribe button 2
				break;
			case 1://free status
				if(msg.getContent().compareTo(Message.CanYouDisplayMyReadings)==0)
				{
					out = new Message(getMySender(),msg.getSender(),Message.ICanDisplayReadings);//sending the message to response I can display ur readings
					communicate.sendRemoteMessage(out);//sending the out message
					this.currentState=this.wait_app;
				}
				break;
			case 2://wait approved status
				if(msg.getContent().compareTo(Message.Approved)==0)
				{
					//TODO start timeout
					this.currentState = this.busy;
					receiver = msg.getSender();
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
							this.saveMsgQueue.put(msg);//put in the save message queue the msg
					}
				}
				break;
			case 3://busy status
				
				if(msg.getContent().compareTo(Message.SenderDisconnect)==0)//if sender disconnect
				{
					if(msg.getSender().compareTo(receiver)==0)
					{
						//TODO stop timer
						//TODO blink led
						this.currentState=this.free;
					}
				}
				else if(msg.getContent().compareTo(Message.button2Pressed)==0)//button 2 pressed
				{
					//TODO stop timer
					// ReceiverDisconnect message
					out = new Message(getMySender(),msg.getSender(),Message.ReceiverDisconnect);//sending the message to response ReceiverDisconnect
					communicate.sendRemoteMessage(out);//sending the out message
					//TODO blink led
					//new stase
					this.currentState= this.free;
				}
			/* TODO timeout timer
			 * 	else 
				if(msg.getContent().compareTo(Message.))
				{
					this.currentState=this.free;
				}
				*/
				else 
				{	//probably in reading or nothing
					int indexMsg, index;
					index=msg.getContent().indexOf(":");
					indexMsg=Message.Reading.indexOf(":");
					
					if(index!=-1 && indexMsg!=-1) //it's a reading message
					{
						if(msg.getContent().substring(0, index).compareTo(Message.Reading.substring(0, indexMsg))==0)//if is reading msg
						{
							int result = Integer.parseInt(msg.getContent().substring(index+1, msg.getContent().length()-1));//verifica che il meno uno que sia corretto quando estrai il number
							SunSpotUtil.lightToLeds(result);//TODO display
							//TODO reset timeout 500ms
							this.currentState=this.busy;
						}
					}
				}
				break;
		}
		
		
	}
}
