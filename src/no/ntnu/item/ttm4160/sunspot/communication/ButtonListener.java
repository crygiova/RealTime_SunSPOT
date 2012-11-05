/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.communication;

import java.util.Vector;

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
	private static Communications comm = new Communications(new IEEEAddress(Spot.getInstance().getRadioPolicyManager().getIEEEAddress()).asDottedHex());
	
	
	/**
	 * @see com.sun.spot.sensorboard.peripheral.ISwitchListener#switchPressed(com.sun.spot.sensorboard.peripheral.ISwitch)
	 */
	public void switchPressed(ISwitch arg0) 
	{
		if(arg0.equals(button[0])) //button 1 pressed
		{
			for(int i =0;i<subsBtn1.size();i++)
			{
				msg = new Message("",subsBtn1.elementAt(i).toString(),Message.button1Pressed);//for every FSM subscribed I crate a msg with empty sender, receiver with just the PID and the conten bt1pressed
				comm.sendRemoteMessage(msg);
			}
		}
		else //button 2 pressed
		{
			for(int i =0;i<subsBtn2.size();i++)
			{
				msg = new Message("",subsBtn2.elementAt(i).toString(),Message.button2Pressed);//for every FSM subscribed I crate a msg with empty sender, receiver with just the PID and the conten bt2pressed
				comm.sendRemoteMessage(msg);
			}
			
		}
	}
	//the scheduler should register to receive the messages
	public static void registerAsListener(Scheduler s)
	{
		comm.registerListener(s);
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
	public static boolean subscribe(String PID,ISwitch btn)
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
	public static boolean unsubscribe(String PID,ISwitch btn)
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
