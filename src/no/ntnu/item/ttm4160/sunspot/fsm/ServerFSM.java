/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

import java.io.IOException;

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
public class ServerFSM extends StateMachine {

	
	private final String READY_ST = "ready";
	private final String WAIT_ST = "wait_response";
	private final String SEND_ST = "sending";
	
	private final String GIVE_UP_TIMER= "giveUpTimer";
	private final String SEND_AGAIN_TIMER= "sendAgainTimer";
	
	private State ready;
	private State wait_resp;
	private State send;
	
	private String receiver;
	/**
	 * @throws IOException 
	 * 
	 */
	public ServerFSM(String iD) throws IOException {
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
		//first transition from init to Ready
		ButtonListener.subscribe(this.ID, EDemoBoard.getInstance().getSwitches()[0]);//subscribe button 1
		ButtonListener.subscribe(this.ID, EDemoBoard.getInstance().getSwitches()[1]);//subscribe button 2
		this.currentState = this.ready;
	}
	
	public void transition() throws IOException {
		Message out;
		Message msg;
		if(!this.inputQueue.isEmpty())//if the queue of input messages is not empty 
		{
			msg = (Message)this.inputQueue.get();
			//if the current state is not the wait and the msg is I can Display your reading
			if(this.currentState.getName().compareTo(WAIT_ST)!=0 && msg.getContent().compareTo(Message.ICanDisplayReadings)==0)
			{
				//send Denied Message
				out = new Message(getMySender(),msg.getSender(),Message.Denied);
				communicate.sendRemoteMessage(out);
			}
			else
			{
				switch(this.currentState.getIdName())//switch state
				{	
					case 1://ready status
						if(msg.getContent().compareTo(Message.button1Pressed)==0)//button 1 pressed
						{
							SpotTimer t =this.createTimer(GIVE_UP_TIMER,500);//TODO maybe a constructor with included the start function
							HandleTimer.addTimer(t);//TODO set up timer 500 ms
							out = new Message(getMySender(),Message.BROADCAST_ADDRESS,Message.CanYouDisplayMyReadings);//editing the msn of can u display my readings
							communicate.sendRemoteMessage(out);//sending a broadcast message using the receiver as a broadcast
							this.currentState=this.wait_resp;//chenge the status in wait
						}
						break;
					case 2://wait_response
						if(msg.getContent().compareTo(Message.ICanDisplayReadings)==0)//I can display u readings
						{
							out = new Message(getMySender(),msg.getSender(),Message.Approved);
							communicate.sendRemoteMessage(out);//sending approved message
							receiver = msg.getSender();//
							HandleTimer.removeTimer(this.createTimer(GIVE_UP_TIMER));//stopping the giveup timer
							SpotTimer t =this.createTimer(SEND_AGAIN_TIMER,100);// start timer
							HandleTimer.addTimer(t);
							this.currentState=this.send;//change status in sending
						}
						/*else 
						if(msg.getContent().compareTo(Message.Timeout+GIVE_UP_TIMER)==0)//TODO give up timer
						{
							SunSpotUtil.blinkLeds();
							this.currentState=this.ready;
						}*/
						break;
					case 3://sending
						
						/*if(msg.getContent().compareTo(Message.Timeout+SEND_AGAIN_TIMER)==0)//TODO send again timer
						{
							SpotTimer t = this.createTimer(SEND_AGAIN_TIMER,100);
							HandleTimer.addTimer(t);//start send again timer 100 ms
							int result = SunSpotUtil.getLightAvg();//reading the light sensors
							out = new Message(getMySender(),receiver,Message.Reading+result);
							communicate.sendRemoteMessage(out);
							this.currentState= this.send;
						}
						else*/
							if(msg.getContent().compareTo(Message.button2Pressed)==0)//button 2 pressed
							{
								out = new Message(getMySender(),msg.getSender(),Message.SenderDisconnect);
								communicate.sendRemoteMessage(out);
								SunSpotUtil.blinkLeds();
								this.currentState=this.ready;//change the status
							}
							else 
								if(msg.getContent().compareTo(Message.ReceiverDisconnect)==0)
								{
									if(receiver.compareTo(msg.getReceiver())==0)//if I receive the disconnect from my receiver
									{
										HandleTimer.removeTimer(this.createTimer(SEND_AGAIN_TIMER));// stop timer send again
										SunSpotUtil.blinkLeds();
										this.currentState=this.ready;//change the status
									}
								}
						break;	
				}
			}
		}
	}
}
