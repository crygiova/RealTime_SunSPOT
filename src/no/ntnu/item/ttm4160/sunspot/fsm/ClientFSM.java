/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import com.sun.spot.peripheral.Spot;
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.util.IEEEAddress;

import no.ntnu.item.ttm4160.sunspot.SunSpotUtil;
import no.ntnu.item.ttm4160.sunspot.communication.ButtonListener;
import no.ntnu.item.ttm4160.sunspot.communication.Communications;
import no.ntnu.item.ttm4160.sunspot.communication.Message;
import no.ntnu.item.ttm4160.sunspot.timers.HandleTimer;
import no.ntnu.item.ttm4160.sunspot.timers.SpotTimer;

/**
 * @author christiangiovanelli
 *
 */
public class ClientFSM extends StateMachine {

	private final String FREE_ST="free";
	private final String WAIT_ST="wait_approved";
	private final String BUSY_ST="busy";
	
	private final String TIMEOUT_TIMER= "timeoutTimer";
	private final int TIME_OF_TIMEOUT_TIMER = 10000;
	
	private State free;
	private State wait_app;
	private State busy;

	private String receiver;

	/**
	 * 
	 */
	public ClientFSM(String iD,ButtonListener btn,Communications communicate) {
		super();
		//Id of the FSM
		this.ID = iD;
		//initialization of the States of the FSM
		free = new State(FREE_ST,1);
		wait_app = new State(WAIT_ST,2);
		busy = new State(BUSY_ST,3);
		//setting up the initial state
		this.currentState= this.initialState;
		//initialize the comunications obj
		this.communicate = communicate;
		//first transition from init to Free*/
		btn.subscribe(this.ID, EDemoBoard.getInstance().getSwitches()[1]);//subscribe button 2
		this.currentState = this.free;
	}

	public void transition() {
		Message out;
		Message msg;
		//TODO input queue & saved message queue?? take a look in the teory
		//System.out.println("CLIENTTRANSITION");
		if(!this.saveMsgQueue.isEmpty()) //if the sved queue is not empty
		{
			//7System.out.println("SAVE MESSAGE IS NOT EMPTY");
			if(this.currentState.getName().compareTo(FREE_ST)==0) //if I m in free status
			{
				msg = (Message)inputQueue.get();
				if(msg.getContent().compareTo(Message.CanYouDisplayMyReadings)==0)
				{
					out = new Message(getMySender(),msg.getSender(),Message.ICanDisplayReadings);//sending the message to response I can display ur readings
					communicate.sendRemoteMessage(out);//sending the out message
					this.currentState=this.wait_app;
				}
			}
		}
		else
		if(!this.inputQueue.isEmpty())
		{
			msg = (Message)inputQueue.get();
			//System.out.println("INPUT QUEUE NOT EMPTY, CONTENT MSG: "+msg.getContent());
		    if(this.currentState.getName().compareTo(BUSY_ST)!=0 && msg.getContent().startsWith(Message.Reading))//check the second function
			{
				out = new Message(getMySender(),msg.getSender(),Message.ReceiverDisconnect);
			}
			else
			{
				switch(this.currentState.getIdName())
				{
				
					case 1://free status
						/*if(!saveMsgQueue.isEmpty())//TODO at least 1 can u dispay my readings
						{
							
						}
						else*/
						//if we receive a Can u display message
						System.out.println("Client,STATE:"+this.currentState.toString());
						System.out.println("Client: msg: "+msg.getContent());
						if(msg.getContent().compareTo(Message.CanYouDisplayMyReadings)==0)
						{
							out = new Message(getMySender(),msg.getSender(),Message.ICanDisplayReadings);//sending the message to response I can display ur readings
							communicate.sendRemoteMessage(out);//sending the out message
							this.currentState=this.wait_app;
						}
						System.out.println("AfterTransitionClient,STATE:"+this.currentState.toString());
						break;
					case 2://wait approved status
						System.out.println("Client,STATE:"+this.currentState.toString());
						System.out.println("Client: msg: "+msg.getContent());
						if(msg.getContent().compareTo(Message.Approved)==0)
						{
							SpotTimer t = this.createTimer(TIMEOUT_TIMER,TIME_OF_TIMEOUT_TIMER);
							HandleTimer.addTimer(t);// start timeout
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
						System.out.println("AfterTransitionClient,STATE:"+this.currentState.toString());
						break;
					case 3://busy status
						System.out.println("Client,STATE:"+this.currentState.toString());
						System.out.println("Client: msg: "+msg.getContent());
						if(msg.getContent().compareTo(Message.SenderDisconnect)==0)//if sender disconnect
						{
							if(msg.getSender().compareTo(receiver)==0)
							{
								HandleTimer.removeTimer(this.createTimer(TIMEOUT_TIMER));//TODO stop timer TIMEOUT TIMER
								SunSpotUtil.blinkLeds();
								this.currentState=this.free;
							}
						}
						else if(msg.getContent().compareTo(Message.button2Pressed)==0)//button 2 pressed
						{
							// ReceiverDisconnect message
							HandleTimer.removeTimer(this.createTimer(TIMEOUT_TIMER));// stop timer TIMEOUT TIMER
							
							out = new Message(getMySender(),receiver,Message.ReceiverDisconnect);//sending the message to response ReceiverDisconnect
							communicate.sendRemoteMessage(out);//sending the out message
							SunSpotUtil.blinkLeds();//blinking leds
							this.currentState= this.free;//status free
						}
						else 
						{	
							//probably in reading or nothing
							int indexMsg;
							indexMsg=Message.Reading.indexOf(":");//TAKING THE SUBSTRING BEFORE :
							
							if(indexMsg!=-1) //it's a reading message
							{
								if(msg.getContent().startsWith(Message.Reading))//if is reading msg
								{
									int result = Integer.parseInt(msg.getContent().substring(indexMsg+1, msg.getContent().length()));//verifica che il meno uno que sia corretto quando estrai il number
									System.out.println(result);
									SunSpotUtil.lightToLeds(result);//TODO display
									//RESET THE TIMER, TAKE IT OFF AND PUT IT AGAIN
									HandleTimer.removeTimer(this.createTimer(TIMEOUT_TIMER));//stop timer TIMEOUT TIMER
									SpotTimer t = this.createTimer(TIMEOUT_TIMER,TIME_OF_TIMEOUT_TIMER);
									HandleTimer.addTimer(t);// start timeout
									this.currentState=this.busy;
								}
							}
						}
						System.out.println("AfterTransitionClient,STATE:"+this.currentState.toString());
						break;
				}
			}
					
		}
		//System.out.println("ClientTransition is made");
	}

	public void timeoutTimer(SpotTimer timeout) 
	{
		switch(this.currentState.getIdName())
		{
			case 1://free status
				break;
			case 2://wait approved
				break;
			case 3://busy status
				System.out.println("********************************************************************");
				System.out.println("Server Timeout,STATE:"+this.currentState.toString());
				if(timeout.getPID().compareTo(this.ID)==0)//if is a timeout with my ID
				{
					if(timeout.getTID().compareTo(TIMEOUT_TIMER)==0)//if it's a timeout timer
					{
						this.currentState= this.free;
					}
				}
				System.out.println("Server Timeout,STATE:"+this.currentState.toString());
				System.out.println("********************************************************************");
				break;
		}
	}
}
