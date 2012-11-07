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
	
	public void inputReceived(Message msg) 
	{
		
		if(msg.getContent().compareTo(Message.button1Pressed)==0)
		{
			System.out.println("Scheduler : InputRecived : "+msg.getContent()+" Receiver: "+msg.getReceiver());
		}
		if(msg.getContent().compareTo(Message.button2Pressed)==0)
		{
			System.out.println("Scheduler : InputRecived : "+msg.getContent()+" Receiver: "+msg.getReceiver());
		}
		if(msg.getContent().compareTo(Message.Approved)==0)
		{
			System.out.println("Scheduler : InputRecived : "+msg.getContent()+" Receiver: "+msg.getReceiver()+" Sender: "+msg.getSender());	
		}
		
	}
	
	public String toString()
	{ return "I'm the Scheduler";}

}
