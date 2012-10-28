/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.timers;

/**
 * @author christiangiovanelli
 *
 */
public class SpotTimer {

	
	private String PID;
	private String TID;
	private long startingTime;//TODO is it long? 
	private long timeout;//TODO is it long? 
	private long delay; 
	//TODO IMPLEMENT THE METHODS FOR THE TIMER, I VE JUST SET UP CONSTRUCTOR AND THE PARAMETERS WITH TH EPROPERLY GET AND SET
	
	/**
	 * 
	 * @param PID	process ID
	 * @param TID	timer ID
	 */
	public SpotTimer(String PID,String TID)
	{
		
	}

	

	/**
	 * @return the pID
	 */
	public String getPID() {
		return PID;
	}


	/**
	 * @param pID the pID to set
	 */
	public void setPID(String pID) {
		PID = pID;
	}


	/**
	 * @return the tID
	 */
	public String getTID() {
		return TID;
	}


	/**
	 * @param tID the tID to set
	 */
	public void setTID(String tID) {
		TID = tID;
	}


	/**
	 * @return the startingTime
	 */
	public long getStartingTime() {
		return startingTime;
	}


	/**
	 * @param startingTime the startingTime to set
	 */
	public void setStartingTime(long startingTime) {
		this.startingTime = startingTime;
	}


	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}


	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}


	/**
	 * @return the delay
	 */
	public long getDelay() {
		return delay;
	}


	/**
	 * @param delay the delay to set
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}
}
