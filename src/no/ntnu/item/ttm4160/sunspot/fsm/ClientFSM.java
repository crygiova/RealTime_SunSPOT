/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

/**
 * @author christiangiovanelli
 *
 */
public class ClientFSM extends StateMachine {

	/**
	 * 
	 */
	public ClientFSM(String iD) {
		// TODO Auto-generated constructor stub
		this.ID = iD;
		this.currentState= this.initialState;//TODO don't know actually if this is correct or not
	}

}
