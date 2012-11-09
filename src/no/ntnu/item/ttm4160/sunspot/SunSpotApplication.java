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

import no.ntnu.item.ttm4160.sunspot.fsm.ClientFSM;
import no.ntnu.item.ttm4160.sunspot.fsm.ServerFSM;
import no.ntnu.item.ttm4160.sunspot.runtime.Scheduler;

import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.util.BootloaderListener;

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
	    
	  	//Registration of the Scheduler as a listener of the messages of the ButtonListener
	    btnL.registerAsListener(scheduler);
	    //creating the 2 FSM and adding them to the Scheduler
	    serverFSM = new ServerFSM(getFsmID(),btnL,communicate);
	    scheduler.assFSM(serverFSM);//*/
	    clientFSM = new ClientFSM(getFsmID(),btnL,communicate);
	    scheduler.assFSM(clientFSM);//*/
	    
	    try {
	    	//execution of the Scheduler
			scheduler.execute();
			} catch (IOException e) {e.printStackTrace();}
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
