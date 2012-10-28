/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

/**
 * @author christiangiovanelli
 *
 */
public class ServerFSM extends StateMachine {

	/**
	 * 
	 */
	public ServerFSM(String iD) {
		// TODO Auto-generated constructor stub
		
		this.ID = iD;//assign the id to the state machine
		this.currentState= this.initialState;//TODO don't know actually if this is correct or not
	}

}
