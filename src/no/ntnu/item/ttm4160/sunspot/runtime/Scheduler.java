package no.ntnu.item.ttm4160.sunspot.runtime;


import com.sun.spot.util.Utils;

import no.ntnu.item.ttm4160.sunspot.SunSpotUtil;
import no.ntnu.item.ttm4160.sunspot.communication.ICommunicationLayerListener;
import no.ntnu.item.ttm4160.sunspot.communication.Message;
import no.ntnu.item.ttm4160.sunspot.fsm.StateMachine;

public class Scheduler implements ICommunicationLayerListener {
    
	public Scheduler()
	{

	}
	
	public void assFSM(StateMachine stateMachine)
	{
		
	}
	
	public void execute()
	{
		
	}
	
	public void inputReceived(Message msg) {
		// TODO Auto-generated method stub
		
		if(msg.getContent().compareTo(Message.button1Pressed)==0)
		{
			SunSpotUtil.testMethod(1);
			Utils.sleep(1000);
			SunSpotUtil.testMethod(1);
			Utils.sleep(1000);
			SunSpotUtil.testMethod(1);
			Utils.sleep(1000);

		}
		if(msg.getContent().compareTo(Message.button2Pressed)==0)
		{
			SunSpotUtil.testMethod(2);
			Utils.sleep(1000);
			SunSpotUtil.testMethod(2);
			Utils.sleep(1000);
			SunSpotUtil.testMethod(2);
			Utils.sleep(1000);

		}
	}

}
