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
	
	public static void addTimer(SpotTimer spotTimer)
	{
		//System.out.println("ADDDING TIMER "+spotTimer.getTID()+" Machine"+spotTimer.getPID()+" activeTimers.size: "+activeTimers.size());
		long timeOut = spotTimer.getTimeout();
		boolean found= false;
		if (activeTimers.size() < 1){
			activeTimers.addElement(spotTimer);
		}else{
			for (int i = 0; i < activeTimers.size() && !found;i++)
			{
				if(((SpotTimer)activeTimers.elementAt(i)).getTimeout() >= timeOut)
				{
					activeTimers.insertElementAt(spotTimer, i);
					found=true;		
				}
			}
			if (!found)
			{
				activeTimers.addElement(spotTimer);
			}
		}
		//System.out.println("ADDDING TIMER "+spotTimer.getTID()+" Machine"+spotTimer.getPID()+" activeTimers.size: "+activeTimers.size());
		//System.out.println("TIMER-ADDED TID: "+spotTimer.getTID()+" PID: "+spotTimer.getPID());
	}
	
	
	public static void removeTimer(SpotTimer spotTimer)
	{	//System.out.println("$$$$"+activeTimers.size());	
		//System.out.println("Removing TIMER "+spotTimer.getTID()+" Machine"+spotTimer.getPID()+" activeTimers.size: "+activeTimers.size());
		activeTimers.removeElement(spotTimer);
		//System.out.println("Removing TIMER "+spotTimer.getTID()+" Machine"+spotTimer.getPID()+" activeTimers.size: "+activeTimers.size());
		//System.out.println("$$$$"+activeTimers.size());
	}
	
	
	/*
	 * This method return null if the Vector is empty or the is no any Timer which is expired. Otherwise it will return SpotTimer. 
	 */
	public static SpotTimer isTimeExpired()
	{
		//System.out.println("$$$$"+activeTimers.size());
		long currentTime = System.currentTimeMillis();
		
		if (activeTimers.size() == 0){
			return null;
		}
		if (((SpotTimer)activeTimers.elementAt(0)).getTimeout() <= currentTime)
		{
			SpotTimer timeout = (SpotTimer)activeTimers.elementAt(0);//i ll take the timer in a object
			removeTimer(timeout);//removing the timer
			return timeout;
		}
		return null;
	}

}
