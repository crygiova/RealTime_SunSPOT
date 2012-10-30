/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.communication;

import com.sun.spot.sensorboard.peripheral.ISwitch;
import com.sun.spot.sensorboard.peripheral.ISwitchListener;

/**
 * @author christiangiovanelli
 *
 */
public class ButtonListener implements ISwitchListener {

	/**
	 * @see com.sun.spot.sensorboard.peripheral.ISwitchListener#switchPressed(com.sun.spot.sensorboard.peripheral.ISwitch)
	 */
	public void switchPressed(ISwitch arg0) {
		// TODO when an event occurred just send one or more msg to the scheduler with the PID interesteed at this event/ or better that r subscriber to the event
		
	}

	/**
	 * @see com.sun.spot.sensorboard.peripheral.ISwitchListener#switchReleased(com.sun.spot.sensorboard.peripheral.ISwitch)
	 */
	public void switchReleased(ISwitch arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * @param PID
	 * @param btn specify at wich button tha process/FSM is interested to be subscribed
	 * @return
	 */
	public static boolean subscribe(String PID,ISwitch btn)
	{
		//TODO for each btn a list of PID that r subscribed
		return false;
		
	}
}
