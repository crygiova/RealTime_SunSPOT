/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import java.io.IOException;


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
public class ServerFSM extends StateMachine {

	//Id of the ready status
	private final String READY_ST = "ready";
	private final int READY_ST_NUM =1;
	//Id of the wait response status
	private final String WAIT_ST = "wait_response";
	private final int WAIT_ST_NUM =2;
	//Id of the sending status
	private final String SEND_ST = "sending";
	private final int SEND_ST_NUM =3;
	
	//give up timer
	private final String GIVE_UP_TIMER= "giveUpTimer";
	//time of give up timer
	private final int TIME_OF_GIVE_UP_TIMER = 5000;
	//send Again timer
	private final String SEND_AGAIN_TIMER= "sendAgainTimer";
	//time of send again timer
	private final int TIME_OF_SEND_AGAIN_TIMER = 100;
	
	//ready status
	private State ready;
	//wait response status
	private State wait_resp;
	//send status
	private State send;
	
	//The ID of the receiver address of the state machine in the other SUNSpot
	private String receiver;
	//giveUp Timer
	SpotTimer giveUp=null;
	//send again timer
	SpotTimer sendAgain;
	
	/***
	 * The constructor receive the Id of the FSM, the button listener for making the Subscribe, and the communication obj
	 * */
	public ServerFSM(String iD,ButtonListener btn,Communications communicate) 
	{
		super();
		//initialization of the id 
		this.ID = iD;//assign the id to the state machine
		//initialization of the States of the FSM
		ready = new State(READY_ST,READY_ST_NUM);
		wait_resp = new State(WAIT_ST,WAIT_ST_NUM);
		send = new State(SEND_ST,SEND_ST_NUM);
		//setting the initial state
		this.currentState=this.initialState;
		//initialize the coomunications obj
		this.communicate = communicate;
		//first transition from init to Ready
		btn.subscribe(this.ID, SunSpotUtil.getButton(0));//subscribe button 1
		btn.subscribe(this.ID, SunSpotUtil.getButton(1));//subscribe button 2*/
		this.currentState = this.ready;
	}
	
	/**
	 * This class makes a transition of the state machines
	 */
	public void transition() throws IOException {
		//output message
		Message out;
		//first message in the queue
		Message msg;
		//if the queue of input messages is not empty
		if(!this.inputQueue.isEmpty()) 
		{
			msg = (Message)this.inputQueue.get();
			//if the current state is not the wait and the msg is I can Display your reading
			if(this.currentState.getName().compareTo(WAIT_ST)!=0 && msg.getContent().compareTo(Message.ICanDisplayReadings)==0)
			{
				//send Denied Message
				out = new Message(getMySender(),msg.getSender(),Message.Denied);
				System.out.println("SENDING: Denied");
				communicate.sendRemoteMessage(out);
			}
			else
			{
				//switch states
				switch(this.currentState.getIdName())
				{	
					
					case READY_ST_NUM://ready state
						if(msg.getContent().compareTo(Message.button1Pressed)==0)//button 1 pressed
						{
							//start giveUp Timer
							giveUp =this.createTimer(GIVE_UP_TIMER,TIME_OF_GIVE_UP_TIMER);
							HandleTimer.addTimer(giveUp);
							//sending CanYouDisplayMyReadings
							out = new Message(getMySender(),Message.BROADCAST_ADDRESS,Message.CanYouDisplayMyReadings);//editing the msn of can u display my readings
							System.out.println("SENDING: Can u display my readings??");
							communicate.sendRemoteMessage(out);//sending a broadcast message using the receiver as a broadcast
							this.currentState=this.wait_resp;//chenge the status in wait
						}
						break;
					case WAIT_ST_NUM://wait_response state
						//I can display u readings
						if(msg.getContent().compareTo(Message.ICanDisplayReadings)==0)
						{
							System.out.println("RECEIVING: I can");
							out = new Message(getMySender(),msg.getSender(),Message.Approved);
							System.out.println("SENDING: Approved");
							communicate.sendRemoteMessage(out);//sending approved message
							receiver = msg.getSender();//setting the reciver in this communication
							HandleTimer.removeTimer(giveUp);//stopping the giveup timer
							sendAgain =this.createTimer(SEND_AGAIN_TIMER,TIME_OF_SEND_AGAIN_TIMER);// start timer
							HandleTimer.addTimer(sendAgain);//add to the Handler
							this.currentState=this.send;//change status in sending
						}
						break;
					case SEND_ST_NUM://sending
						
						// if button 2 pressed
						if(msg.getContent().compareTo(Message.button2Pressed)==0)
						{
							out = new Message(getMySender(),receiver,Message.SenderDisconnect);
							System.out.println("SENDING: sender disconnect");
							communicate.sendRemoteMessage(out);
							SunSpotUtil.blinkLeds();
							this.currentState=this.ready;//change the status
						}
						else 
							//ReceiverDisconnect
							if(msg.getContent().compareTo(Message.ReceiverDisconnect)==0)
							{
								System.out.println("RECEIVING: receiver disconnect");
								if(receiver.compareTo(msg.getSender())==0)//if I receive the disconnect from my receiver
								{
									HandleTimer.removeTimer(sendAgain);// stop timer send again
									SunSpotUtil.blinkLeds();//blinking leds
									this.currentState=this.ready;//change the status
								}
							}
						break;	
				}
			}
		}
	}

	/**
	 * This function is called when a timer is expired
	 * */
	public void timeoutTimer(SpotTimer timeout) throws IOException 
	{
		//switch states
		switch(this.currentState.getIdName())
		{
			case READY_ST_NUM://ready status
				break;
			case WAIT_ST_NUM://wait response
				//control to be sure that the PID of the Timer is the ID of the FSM
				if(timeout.getPID().compareTo(this.ID)==0)
				{
					//if is a GiveUp Timer
					if(timeout.getTID().compareTo(GIVE_UP_TIMER)==0)
					{
						SunSpotUtil.blinkLeds();//blink leds
						this.currentState= this.ready;//go in ready status
					}
				}
				break;
			case SEND_ST_NUM://sending
				if(timeout.getPID().compareTo(this.ID)==0)
				{
					//if is a send again timer
					if(timeout.getTID().compareTo(SEND_AGAIN_TIMER)==0)
					{
						//start send again timer
						sendAgain = this.createTimer(SEND_AGAIN_TIMER,TIME_OF_SEND_AGAIN_TIMER);
						//add to Handler
						HandleTimer.addTimer(sendAgain);
						//reading the light sensors
						int result = SunSpotUtil.getLightAvg();
						Message out = new Message(getMySender(),receiver,Message.Reading+result);
						//System.out.println(Message.Reading+result);
						communicate.sendRemoteMessage(out);
						//change the state
						this.currentState= this.send;
					}
				}
				break;
		}
		
	}
}
