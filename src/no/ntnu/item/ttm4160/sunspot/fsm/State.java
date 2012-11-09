/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot.fsm;

/**
 * @author christiangiovanelli
 *
 */
class State {
	/**This variable rappresent the stases of a FSM
	 * we have rapresent a state with a name and a number
	 * */
	//name of the State
	private String name;
	//number of the state
	private int idName;
	/**
	 * @param name of the status
	 * */
	public State(String name,int idName) {
		this.setName(name);
		this.setIdName(idName);
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

	/**
	 * @return the idName
	 */
	public int getIdName() {
		return idName;
	}

	/**
	 * @param idName the idName to set
	 */
	public void setIdName(int idName) {
		this.idName = idName;
	}

	public String toString()
	{
		return this.name;
	}
}
