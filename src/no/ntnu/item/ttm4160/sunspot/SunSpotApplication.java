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

import no.ntnu.item.ttm4160.sunspot.runtime.Scheduler;

import com.sun.spot.peripheral.Spot;
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.sensorboard.peripheral.LightSensor;
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
	
	Scheduler scheduler;
	private static ITriColorLED [] leds = EDemoBoard.getInstance().getLEDs();
	
	public static void blinkLeds()
	{ 
		for (int j = 2; j >=0 ; j--) 
		{
            for (int i = 7; i >=0 ; i--)
            {
            	switch(j)
            	{
            		case 2:leds[i].setColor(LEDColor.RED);break;
            		case 1:leds[i].setColor(LEDColor.WHITE);break;
            		case 0: leds[i].setColor(LEDColor.GREEN);break;
            	}
            	leds[i].setOn();
            }
            Utils.sleep(400);
            for (int i = 7; i >=0 ; i--)
            {
            	leds[i].setOff();
            }
            Utils.sleep(200);           
        }
	}
	
    protected void startApp() throws MIDletStateChangeException {
    	
        new BootloaderListener().start();   // monitor the USB (if connected) and recognize commands from host
        // So you don't have to reset SPOT to deploy new code on it.

        /*
         * Instantiate the scheduler and the state machines, then start the scheduler.
/*         */
        
  /*�      Scheduler s = new Scheduler();
        FSMClient client = new FSMClient("name");
        
        
        
        s.addFSM(client);
        
        s.loopfunction();
     */   		
        while(true)
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
       */ 	
        	for(int i = 0;i<8;i++)
        	{
            	Utils.sleep(3000);
            //	SunSpotUtil.clearLeds();
            	SunSpotUtil.testMethod(i);
            	Utils.sleep(1000);
        	}
        	
        }
        
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
