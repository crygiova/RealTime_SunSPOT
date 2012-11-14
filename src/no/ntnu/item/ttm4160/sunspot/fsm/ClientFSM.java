/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

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

	//Id of the free status
	private final String FREE_ST="free";
	private final int FREE_ST_NUM =1;
	//Id of the wait approved status
	private final String WAIT_ST="wait_approved";
	private final int WAIT_ST_NUM =2;
	//Id of the busy status
	private final String BUSY_ST="busy";
	private final int BUSY_ST_NUM =3;
	
	//timeout timer
	private final String TIMEOUT_TIMER= "timeoutTimer";
	//time fo time out timer
	private final int TIME_OF_TIMEOUT_TIMER = 5000;
	
	//state free
	private State free;
	//state wait approved
	private State wait_app;
	//state busy
	private State busy;
	//receiver
	private String receiver;
	//timeout
	private SpotTimer timeout;

	/**
	 * The constructor receive the Id of the FSM, the button listener for making the Subscribe, and the communication obj
	 */
	public ClientFSM(String iD,ButtonListener btn,Communications communicate) {
		super();
		//Id of the FSM
		this.ID = iD;
		//initialization of the States of the FSM
		free = new State(FREE_ST,FREE_ST_NUM);
		wait_app = new State(WAIT_ST,WAIT_ST_NUM);
		busy = new State(BUSY_ST,BUSY_ST_NUM);
		//setting up the initial state
		this.currentState= this.initialState;
		//initialize the comunications obj
		this.communicate = communicate;
		//first transition from init to Free*/
		btn.subscribe(this.ID, SunSpotUtil.getButton(1));//subscribe button 2
		//go in the free state
		this.currentState = this.free;
	}

	public void transition() 
	{
		//output message
		Message out;
		//first message in the queue
		Message msg;
		if(!this.saveMsgQueue.isEmpty()) //if the saved queue is not empty
		{
			//System.out.println("SAVE MESSAGE IS NOT EMPTY");
			if(this.currentState.getName().compareTo(FREE_ST)==0) //if I m in free status
			{
				//taking the message from the saveMSgqueue
				msg = (Message)saveMsgQueue.get();
				//if the message is a Can u display my readings
				if(msg.getContent().compareTo(Message.CanYouDisplayMyReadings)==0)
				{
					out = new Message(getMySender(),msg.getSender(),Message.ICanDisplayReadings);//sending the message to response I can display ur readings
					communicate.sendRemoteMessage(out);//sending the out message
					this.currentState=this.wait_app;//change state
				}
			}
		}
		else
			//if input queue is not empty
		if(!this.inputQueue.isEmpty())
		{
			msg = (Message)inputQueue.get();
			//if the state is not busy and the content of the content of the message is Reading
		    if(this.currentState.getName().compareTo(BUSY_ST)!=0 && msg.getContent().startsWith(Message.Reading))
			{
		    	//send the receiver disconnect
				out = new Message(getMySender(),msg.getSender(),Message.ReceiverDisconnect);
			}
			else
			{
				//System.out.println("Client,STATE:"+this.currentState.toString());
				switch(this.currentState.getIdName())
				{
				
					case FREE_ST_NUM://free status
						//if we receive a Can u display message
						if(msg.getContent().compareTo(Message.CanYouDisplayMyReadings)==0)
						{
							//System.out.println("RECEIVING: CAN u display my readings");
							out = new Message(getMySender(),msg.getSender(),Message.ICanDisplayReadings);//sending the message to response I can display ur readings
							//System.out.println("SENDING: I caN");
							communicate.sendRemoteMessage(out);//sending the out message
							this.currentState=this.wait_app;
						}
						break;
					case WAIT_ST_NUM://wait approved status
						//if the message is Approved
						if(msg.getContent().compareTo(Message.Approved)==0)
						{
							//System.out.println("RECEIVING: Approved");
							// start timeout timer
							timeout = this.createTimer(TIMEOUT_TIMER,TIME_OF_TIMEOUT_TIMER);
							HandleTimer.addTimer(timeout);
							this.currentState = this.busy;
							//set up the Fsm of the other sun Spot
							receiver = msg.getSender();
						}
						else
						{
							//if the message id Denied
							if(msg.getContent().compareTo(Message.Denied)==0)
							{
								//System.out.println("RECEIVING: DEnied");
								this.currentState= this.free;
							}
							else 
								//if the message is can u display my readings
							if(msg.getContent().compareTo(Message.CanYouDisplayMyReadings)==0)
							{
									this.saveMsgQueue.put(msg);//put in the save message queue the msg
							}
						}
						break;
					case BUSY_ST_NUM://busy status
						if(msg.getContent().compareTo(Message.SenderDisconnect)==0)//if sender disconnect
						{
							System.out.println("REceiving: sender disconnect");
							if(msg.getSender().compareTo(receiver)==0)//if the message is from my receiver
							{
								HandleTimer.removeTimer(timeout);//stop timer TIMEOUT TIMER
								SunSpotUtil.blinkLeds();//blink leds
								this.currentState=this.free;
							}
						}
						else 
						if(msg.getContent().compareTo(Message.button2Pressed)==0)//button 2 pressed
						{
							// ReceiverDisconnect message
							HandleTimer.removeTimer(timeout);// stop timer TIMEOUT TIMER
							
							out = new Message(getMySender(),receiver,Message.ReceiverDisconnect);//sending the message to response ReceiverDisconnect
							communicate.sendRemoteMessage(out);//sending the out message
							//System.out.println("SENDINDg: receiver disconnect");
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
									//System.out.println(result);
									SunSpotUtil.lightToLeds(result);//TODO display
									//RESET THE TIMER, TAKE IT OFF AND PUT IT AGAIN
									HandleTimer.removeTimer(timeout);//stop timer TIMEOUT TIMER
									timeout = this.createTimer(TIMEOUT_TIMER,TIME_OF_TIMEOUT_TIMER);
									HandleTimer.addTimer(timeout);// start again the timeout timer
									this.currentState=this.busy;
								}
							}
						}
						break;
				}
			}
					
		}
	}

	public void timeoutTimer(SpotTimer timeout) 
	{
		//switch the states
		switch(this.currentState.getIdName())
		{
			case FREE_ST_NUM://free status
				break;
			case WAIT_ST_NUM://wait approved
				break;
			case BUSY_ST_NUM://busy status
				if(timeout.getPID().compareTo(this.ID)==0)//if is a timeout with my ID
				{
					if(timeout.getTID().compareTo(TIMEOUT_TIMER)==0)//if it's a timeout timer
					{
						this.currentState= this.free;
					}
				}
				break;
		}
	}
}
