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
	private int idName;
	/**
	 * @param name of the status
	 * */
	public State(String name,int idName) {
		// TODO Auto-generated constructor stub
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

}
