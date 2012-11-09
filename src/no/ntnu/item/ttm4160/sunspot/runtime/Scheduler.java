package no.ntnu.item.ttm4160.sunspot.runtime;


import java.io.IOException;
import java.util.Vector;

import com.sun.spot.util.Utils;

import no.ntnu.item.ttm4160.sunspot.SunSpotUtil;
import no.ntnu.item.ttm4160.sunspot.communication.ICommunicationLayerListener;
import no.ntnu.item.ttm4160.sunspot.communication.Message;
import no.ntnu.item.ttm4160.sunspot.fsm.StateMachine;
import no.ntnu.item.ttm4160.sunspot.timers.HandleTimer;
import no.ntnu.item.ttm4160.sunspot.timers.SpotTimer;

public class Scheduler implements ICommunicationLayerListener {
    
	private static Vector stMachines;
	
	public Scheduler()
	{
		stMachines = new Vector();
	}
	
	public void assFSM(StateMachine stateMachine)
	{
		stMachines.addElement(stateMachine);
	}
	
	/**This method returns the number if status machine in the Scheduler*/
	public int getNumStateMachine()
	{
		return stMachines.size();
	}
	
	/**The main method of the scheduler that has to give the resource to the FSM
	 * @throws IOException */
	public void execute() throws IOException
	{ 	SpotTimer timerExp;//rapresent the expired timers
		while(true)
		{//TODO change this now is just for testing
			timerExp = HandleTimer.isTimeExpired();
			if(timerExp!=null) //if there's a timer expired
			{
				//System.out.print("TIMEOUT TIMER");
				int index = existFSM(timerExp.getPID());
				if(index!=-1) //if the FSM exist
				{
					//System.out.println("  Timer id:"+timerExp.getTID()+" PID: "+timerExp.getPID());
					((StateMachine)stMachines.elementAt(index)).timeoutTimer(timerExp);
				}
			}
			
			
			// giving te resources to the FSM
			for(int j =0;j<stMachines.size();j++)
			{
				((StateMachine)stMachines.elementAt(j)).transition();
			}
			Utils.sleep(500);
		}
		
	}
	
	
	/****************************************************************************************************
	 * this method is called only for outside messages and button messages
	 * 
	 * every message has a proper receiver, just for the broadcast messages u have to forwad them to al FSM
	 * 
	 * */
	
	public void inputReceived(Message msg) 
	{
		int index;
		//if it's a broadcast message
		if(msg.getReceiver().equals(Message.BROADCAST_ADDRESS))
		{
			//forward to every FSM
			for(int j =0;j<stMachines.size();j++)
			{
				((StateMachine)stMachines.elementAt(j)).addInputQueue(msg);
			}
		}
		else //if is not a broadcast message is a message for a status machines
		{
			index = existFSM(msg.getReceiverFSM());
			//verify which FSM has to receive the msg
			if(index!=-1)
			{
				//System.out.println("FSM found, ReceiverFSM : "+ msg.getReceiverFSM()+" Index "+index);
				((StateMachine)stMachines.elementAt(index)).addInputQueue(msg);
			}
			else //if i can not find a state machine
			{
				System.out.println("FSM not found, Receiver : "+ msg.getReceiverFSM());
			}
			
			/*if(msg.getContent().compareTo(Message.button1Pressed)==0)
			{
				System.out.println("Scheduler : InputRecived : "+msg.getContent()+" Receiver: "+msg.getReceiver());
			}
			if(msg.getContent().compareTo(Message.button2Pressed)==0)
			{
				System.out.println("Scheduler : InputRecived : "+msg.getContent()+" Receiver: "+msg.getReceiver());
			}
			/*if(msg.getContent().compareTo(Message.Approved)==0)
			{
				System.out.println("Scheduler : InputRecived : "+msg.getContent()+" Receiver: "+msg.getReceiver()+" Sender: "+msg.getSender());
			}
			*/
		}
	}
	
	/**given an Id this function return the index of the FSM if this exist in the Scheduler
	 * 
	 * */
	public int existFSM(String IdToFind)
	{
		for(int j =0;j<stMachines.size();j++)
		{
			if(((StateMachine)stMachines.elementAt(j)).getID().equals(IdToFind))
			{
				return j;
			}
		}
		return -1;
	}
	
	public String toString()
	{ return "I'm the Scheduler";}

}
