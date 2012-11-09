/*
 * Copyright (c) 2006 Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package no.ntnu.item.ttm4160.sunspot;

import java.io.IOException;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import no.ntnu.item.ttm4160.sunspot.communication.ButtonListener;
import no.ntnu.item.ttm4160.sunspot.communication.Communications;
import no.ntnu.item.ttm4160.sunspot.communication.Message;
import no.ntnu.item.ttm4160.sunspot.fsm.ClientFSM;
import no.ntnu.item.ttm4160.sunspot.fsm.ServerFSM;
import no.ntnu.item.ttm4160.sunspot.runtime.Scheduler;

import com.sun.spot.peripheral.Spot;
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.sensorboard.peripheral.LightSensor;
import com.sun.spot.sensorboard.peripheral.Switch;
import com.sun.spot.util.BootloaderListener;
import com.sun.spot.util.IEEEAddress;
import com.sun.spot.util.Utils;

/*
 * The startApp method of this class is called by the VM to start the
 * application.
 *
 * The manifest specifies this class as MIDlet-1, which means it will
 * be selected for execution.
 */
public class SunSpotApplication extends MIDlet {
	
	/**Scheduler*/
	Scheduler scheduler;
	private static ITriColorLED [] leds = EDemoBoard.getInstance().getLEDs();
	/**Button Listener*/
	private static  ButtonListener btnL = new ButtonListener();
	/**Server FSM*/
	private static ServerFSM serverFSM;
	/**Client FSM*/
	private static ClientFSM clientFSM;
	/**This is the counter for the status machines*/
	private static int counter =-1;
	/**This is the communication object used to comunications between SUNSPOT*/
	private static Communications communicate;
	
    protected void startApp() throws MIDletStateChangeException 
    {
    	
        new BootloaderListener().start();   // monitor the USB (if connected) and recognize commands from host
        // So you don't have to reset SPOT to deploy new code on it.
        scheduler = new Scheduler();//declaring the Scheduler
        
        EDemoBoard.getInstance().getSwitches()[0].addISwitchListener(btnL);//add the ButtonListener as a listener of the button 1
	    EDemoBoard.getInstance().getSwitches()[1].addISwitchListener(btnL);//add the ButtonListener as a listener of the button 2
	    communicate = new Communications(SunSpotUtil.getMyMac());//init the communication obj passing tha MAC address
	    //registration of the scheduler as a communication listener
	    communicate.registerListener(scheduler);
	    
	    btnL.registerAsListener(scheduler);//Registration of the Scheduler as a listener of the messages of the ButtonListener
	    /*serverFSM = new ServerFSM(getFsmID(),btnL,communicate);
	    scheduler.assFSM(serverFSM);//*/
	    clientFSM = new ClientFSM(getFsmID(),btnL,communicate);
	    scheduler.assFSM(clientFSM);//*/
	    System.out.println("Number of state machine: "+scheduler.getNumStateMachine());
	    try {
			scheduler.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //TEST of sending, delete it for the client and add it for the server SUNSPOT for testing
	   /* while(true)
	    {
	    	Utils.sleep(3000);
	    	System.out.println("Sending");
	    	Message out = new Message(SunSpotUtil.getMyMac(),Message.BROADCAST_ADDRESS,Message.Approved);//editing the msn of can u display my readings
			communicate.sendRemoteMessage(out);//sending a broadcast message using the receiver as a broadcast
	    }
	    
	    
	    
	    /*
         * Instantiate the scheduler and the state machines, then start the scheduler.
/*         */
        
  /*      Scheduler s = new Scheduler();
        FSMClient client = new FSMClient("name");
        
        
        
        
        s.addFSM(client);
        
        s.loopfunction();
     */  
        
        /*ButtonListener btn = new ButtonListener();
        EDemoBoard.getInstance().getSwitches()[0].addISwitchListener(btn); 
        EDemoBoard.getInstance().getSwitches()[1].addISwitchListener(btn);//registration of the liestener
*/
       /* while(true)
        {
        	btn.switchPressed(EDemoBoard.getInstance().getSwitches()[0]);
        }
       /* while(true)
        {
        	int avg=500;
        	Utils.sleep(1000);
        	SunSpotUtil.blinkLeds();
        	Utils.sleep(1000);
     /*   	try {
        		avg=SunSpotUtil.getLightAvg();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	SunSpotUtil.lightToLeds(avg);
      	
        	for(int i = 0;i<8;i++)
        	{
            	Utils.sleep(3000);
            //	SunSpotUtil.clearLeds();
            	SunSpotUtil.testMethod(i);
            	Utils.sleep(1000);
        	}
        	
        }*/
        
        
    }
    
    
    
   
    
    public String getFsmID()
    {
    	return Integer.toString(++counter);
    }
    
    protected void pauseApp() {
        // This will never be called by the Squawk VM
    }
    
    /**
     * Called if the MIDlet is terminated by the system.
     * I.e. if startApp throws any exception other than MIDletStateChangeException,
     * if the isolate running the MIDlet is killed with Isolate.exit(), or
     * if VM.stopVM() is called.
     * 
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true when this method is called, the MIDlet must
     *    cleanup and release all resources. If false the MIDlet may throw
     *    MIDletStateChangeException  to indicate it does not want to be destroyed
     *    at this time.
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    	
    	
    }
}
