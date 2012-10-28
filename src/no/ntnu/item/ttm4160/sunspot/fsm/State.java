/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

/**
 * @author christiangiovanelli
 *
 */
class State {
	/**This variable rappresent the stases of a FSM*/
	private String name;
	
	/**
	 * @param name of the status
	 * */
	public State(String name) {
		// TODO Auto-generated constructor stub
		this.setName(name);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
