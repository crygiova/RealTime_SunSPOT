/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.timers;
import java.util.Vector;

/**
 * @author christiangiovanelli
 *
 */
public class HandleTimer {
	
	private static Vector activeTimers = new Vector();
	
	public static void addTimer(SpotTimer t)
	{
		boolean found = false;
		int index = 0;
		SpotTimer buffer;
		while(activeTimers.size()>index && !found)
		{
			 buffer = (SpotTimer) activeTimers.elementAt(index);
			 if(buffer.getTimeout()>t.getTimeout())
			 {
				 //activeTimers.insertElementAt(obj, index)
			 }
		}
	}
	
	
	//TODO boolean?
	public static void removeTimer(SpotTimer t)
	{	
		activeTimers.removeElement(t);
	}

}
