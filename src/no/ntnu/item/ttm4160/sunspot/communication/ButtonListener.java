/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.communication;

import java.util.Vector;

import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ISwitch;
import com.sun.spot.sensorboard.peripheral.ISwitchListener;
import com.sun.spot.sensorboard.peripheral.LEDColor;

/**
 * @author christiangiovanelli
 *
 */
public class ButtonListener implements ISwitchListener {

	private static Vector subsBtn1 = new  Vector();
	private static Vector subsBtn2 = new  Vector();
	private static ISwitch[] button = EDemoBoard.getInstance().getSwitches();
	/**
	 * @see com.sun.spot.sensorboard.peripheral.ISwitchListener#switchPressed(com.sun.spot.sensorboard.peripheral.ISwitch)
	 */
	public void switchPressed(ISwitch arg0) 
	{
		if(arg0.equals(button[0])) //button 1 pressed
		{
			//TODO send a message or more messages to the machines that r subscribed to the btn1 vector
		}
		else //button 2 pressed
		{
			//TODO send a message or more messages to the machines that r subscribed to the btn2 vector
		}
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
			//TODO add to the vector of btn1 the PID of the FSM that wants to be subscribed
		}
		else //button 2
		{
			//TODO add to the vector of btn2 the PID of the FSM that wants to be subscribed
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
			
		}
		else //button 2
		{
			
		}
		return false;
	}
}
