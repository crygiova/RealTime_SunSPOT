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
		this.transition(null);
	}

	public void transition(Message msg) throws IOException {
		Message out;
		switch(this.currentState.getIdName())
		{	
			case 0://init status
				ButtonListener.subscribe(this.ID, EDemoBoard.getInstance().getSwitches()[0]);//subscribe button 1
				ButtonListener.subscribe(this.ID, EDemoBoard.getInstance().getSwitches()[1]);//subscribe button 2
				this.currentState = this.ready;
				break;
			case 1://ready status
				if(msg.getContent().compareTo(Message.button1Pressed)==0)//button 1 pressed
				{
					SpotTimer t =this.createTimer(GIVE_UP_TIMER,500);//TODO maybe a constructor with included the start function
					HandleTimer.addTimer(t);//TODO set up timer 500 ms
					out = new Message(getMySender(),Message.BROADCAST_ADDRESS,Message.CanYouDisplayMyReadings);//editing the msn of can u display my readings
					communicate.sendRemoteMessage(out);//sending a broadcast message using the receiver as a broadcast
					this.currentState=this.wait_resp;
				}
				break;
			case 2://wait_response
				//TODO denied message
				if(msg.getContent().compareTo(Message.ICanDisplayReadings)==0)//I can display u readings
				{
					out = new Message(getMySender(),msg.getSender(),Message.Approved);
					communicate.sendRemoteMessage(out);//sending approved message
					receiver = msg.getSender();//
					HandleTimer.removeTimer(this.createTimer(GIVE_UP_TIMER));
					SpotTimer t =this.createTimer(SEND_AGAIN_TIMER,100);//TODO maybe a constructor with included the start function
					HandleTimer.addTimer(t);//TODO START AGAIN TIMER
					this.currentState=this.send;
				}
				else 
				if(msg.getContent().compareTo(Message.Timeout+GIVE_UP_TIMER)==0)//TODO give up timer
				{
					SunSpotUtil.blinkLeds();
					this.currentState=this.ready;
				}
				break;
			case 3://sending
				if(msg.getContent().compareTo(Message.Timeout+SEND_AGAIN_TIMER)==0)//TODO send again timer
				{
					SpotTimer t = this.createTimer(SEND_AGAIN_TIMER,100);//TODO maybe a constructor with included the start function
					HandleTimer.addTimer(t);//TODO //start send again timer 100 ms
					int result = SunSpotUtil.getLightAvg();//reading the light sensors
					out = new Message(getMySender(),receiver,Message.Reading+result);
					communicate.sendRemoteMessage(out);
					this.currentState= this.send;
				}
				else
					if(msg.getContent().compareTo(Message.button2Pressed)==0)//button 2 pressed
					{
						out = new Message(getMySender(),msg.getSender(),Message.SenderDisconnect);
						communicate.sendRemoteMessage(out);
						SunSpotUtil.blinkLeds();
						this.currentState=this.ready;
					}
					else 
						if(msg.getContent().compareTo(Message.ReceiverDisconnect)==0)
						{
							if(receiver.compareTo(msg.getReceiver())==0)//if I receive the disconnect from my receiver
							{
								HandleTimer.removeTimer(this.createTimer(SEND_AGAIN_TIMER));//TODO stop timer send again
								SunSpotUtil.blinkLeds();
								this.currentState=this.ready;
							}
						}
				break;	
		}
	}

}
