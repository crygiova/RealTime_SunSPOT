/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.communication;

import java.util.Vector;

import no.ntnu.item.ttm4160.sunspot.fsm.StateMachine;
import no.ntnu.item.ttm4160.sunspot.runtime.Scheduler;
import no.ntnu.item.ttm4160.sunspot.*;
import com.sun.spot.peripheral.Spot;
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ISwitch;
import com.sun.spot.sensorboard.peripheral.ISwitchListener;
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.util.IEEEAddress;

/**
 * @author christiangiovanelli
 *
 */
public class ButtonListener implements ISwitchListener {

	private static Vector subsBtn1 = new  Vector();
	private static Vector subsBtn2 = new  Vector();
	private static ISwitch[] button = EDemoBoard.getInstance().getSwitches();
	private static Message msg;
	
	private static Scheduler s;
	/**
	 * @see com.sun.spot.sensorboard.peripheral.ISwitchListener#switchPressed(com.sun.spot.sensorboard.peripheral.ISwitch)
	 */
	public void switchPressed(ISwitch arg0) 
	{
		if(arg0.equals(button[0])) //button 1 pressed
		{
			for(int i =0;i<subsBtn1.size();i++)
			{
				String receiver = SunSpotUtil.getMyMac()+":"+((String)subsBtn1.elementAt(i));
				//printf if a FSM is subscribed
				//System.out.println(receiver+"  btn1");
				msg = new Message("",receiver,Message.button1Pressed);//for every FSM subscribed I crate a msg with empty sender, receiver with just the PID and the conten bt1pressed
				s.inputReceived(msg);//just call the scheduler function
			}
		}
		else //button 2 pressed
		{
			for(int i =0;i<subsBtn2.size();i++)
			{
				String receiver = SunSpotUtil.getMyMac()+":"+((String)subsBtn2.elementAt(i));
				//printf if a FSM is subscribed
				//System.out.println(receiver+"  btn2");
				msg = new Message("",receiver,Message.button2Pressed);//for every FSM subscribed I crate a msg with empty sender, receiver with just the PID and the conten bt2pressed
				s.inputReceived(msg);
			}
		}
	}
	//the scheduler should register to receive the messages
	public void registerAsListener(Scheduler s)
	{
		this.s=s;//TODO delete if using the communication
	}

	
	/**
	 * @see com.sun.spot.sensorboard.peripheral.ISwitchListener#switchReleased(com.sun.spot.sensorboard.peripheral.ISwitch)
	 */
	public void switchReleased(ISwitch arg0) 
	{
		// TODO it has not to be implemented
	}

	/**
	 * 
	 * @param PID
	 * @param btn specify at wich button tha process/FSM is interested to be subscribed
	 * @return
	 */
	public boolean subscribe(String PID,ISwitch btn)//TODO not static pass to the init of a FSM the btn listener
	{
		if(btn.equals(button[0])) //button 1
		{
			//add to the vector of btn1 the PID of the FSM that wants to be subscribed
			if(!subsBtn1.contains(PID))//if the subscribe not contain the PID
			{
				subsBtn1.addElement(PID);//add the PID to the subscribe list
				return true;
			}
		}
		else //button 2
		{
			//add to the vector of btn2 the PID of the FSM that wants to be subscribed
			if(!subsBtn2.contains(PID))//if the subscribe not contain the PID
			{
				subsBtn2.addElement(PID);//add the PID to the subscribe list
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean unsubscribe(String PID,ISwitch btn)//TODO not static pass to the init of a FSM the btn listener
	{
		if(btn.equals(button[0])) //button 1
		{
			if(subsBtn1.contains(PID))//if the subscribe contain the PID
			{
				subsBtn1.removeElement(PID);//add the PID to the subscribe list
				return true;
			}
		}
		else //button 2
		{
			if(subsBtn2.contains(PID))//if the subscribe contain the PID
			{
				subsBtn2.removeElement(PID);//add the PID to the subscribe list
				return true;
			}
		}
		return false;
	}
}
