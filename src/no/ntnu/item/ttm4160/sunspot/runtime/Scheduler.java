package no.ntnu.item.ttm4160.sunspot.runtime;

import no.ntnu.item.ttm4160.sunspot.communication.ButtonListener;
import no.ntnu.item.ttm4160.sunspot.communication.ICommunicationLayerListener;
import no.ntnu.item.ttm4160.sunspot.communication.Message;

public class Scheduler implements ICommunicationLayerListener {

	public Scheduler()
	{
		
		//the last istruction of the constructor as sto be the registration to the button listener
		ButtonListener.registerAsListener(this);
	}
	
	public void inputReceived(Message msg) {
		// TODO Auto-generated method stub

	}

}
