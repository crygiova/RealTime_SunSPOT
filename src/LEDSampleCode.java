/*
* Copyright (c) 2006 Sun Microsystems, Inc.
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to 
* deal in the Software without restriction, including without limitation the 
* rights to use, copy, modify, merge, publish, distribute, sublicense, and/or 
* sell copies of the Software, and to permit persons to whom the Software is 
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in 
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
* DEALINGS IN THE SOFTWARE.
 **/       

import java.util.*;

/*
 * LEDSampleCode.java
 *
 * Some simple uses of the LEDs on the Sun SPOT.
 *
 * author: Ron Goldman  
 * date: August 14, 2006 
 */

import java.io.IOException;

import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ILightSensor;
import com.sun.spot.sensorboard.peripheral.ISwitch;
import com.sun.spot.sensorboard.peripheral.ISwitchListener;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.sensorboard.peripheral.LightSensor;
import com.sun.spot.sensorboard.peripheral.TemperatureInput;
import com.sun.spot.util.Utils;


import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Sample code snippet to show how to use the tricolor LEDs
 * on the Sun SPOT General Purpose Sensor Board.
 *
 * @author Ron Goldman
 */
public class LEDSampleCode extends MIDlet implements ISwitchListener {

    private ITriColorLED [] leds = EDemoBoard.getInstance().getLEDs();
    private LightSensor sens = (LightSensor) EDemoBoard.getInstance().getLightSensor();
    private TemperatureInput temp = (TemperatureInput) EDemoBoard.getInstance().getADCTemperature();
    private ISwitch[] button = EDemoBoard.getInstance().getSwitches();
    /**
     * Simple LED demo that repeats forever.
     *
     * First, blink the leftmost LED on & off 5 times. 
     * Second, have a moving lit LED sweep from left to right.
     * Third, pulse one LED from dim to bright, repeat 3 times.
     * @throws IOException 
     */
    public void demoLEDs() throws IOException {
        for (int i = 0; i < 8; i++) {
            leds[i].setOff();			// turn off all LEDs
        }
       
        int sensor=0;
        while (true) {
        	
        	/*
        	try {
				sensor=sens.getAverageValue();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	for (int i = 0; i < 5; i++ ) {
                leds[i].setOn();
                
            }*/
        	// first demo - blink LED 0 on & off 5 times
          /*  leds[0].setColor(LEDColor.MAGENTA);    // set it to one of the predefined colors
            for (int i = 0; i < 5; i++ ) {
                leds[0].setOn();
                Utils.sleep(250);               // on for 1/4 second
                leds[0].setOff();
                Utils.sleep(750);               // off for 3/4 second
            }*/
			
        	int avg = sens.getAverageValue();
			//int ledds = (int) (sens.getHighThreshold()-sens.getLowThreshold())%8;
            /// second demo - move the lit LED - go from LED 0 to LED 7
            Random gen = new Random();
            int counter;
            counter = avg/84; //for light
            
            
            /*int misura=(int)temp.getCelsius();
            if(misura<=0)
            {
            	counter =0;
            }
            else
            {	if(misura>24)
            	{
            		counter = 8;
            	}
            	else
            	{
            		counter=misura/3;
            	}//TEMP
            }*/
            int prev=0;
            for (int i = 0; i < 8; i++) {
            	switch(counter)
            	{
            		case 0: leds[i].setColor(LEDColor.GREEN);leds[7-i].setColor(LEDColor.RED);break;
            		case 1:leds[i].setColor(LEDColor.MAGENTA);break;
            		case 2:leds[i].setColor(LEDColor.WHITE);break;
            		case 3:leds[i].setColor(LEDColor.RED);break;
            		case 4:leds[i].setColor(LEDColor.ORANGE);break;
            		case 5:leds[i].setColor(LEDColor.YELLOW);break;
            		case 6:leds[i].setColor(LEDColor.GREEN);break;
            		case 7:leds[i].setColor(LEDColor.CHARTREUSE);break;
            		case 8:leds[i].setColor(LEDColor.CYAN);break;
            		//case 9:break;
            		
            	}
            	
            	if(counter==0)
            	{
            		leds[i].setOn();
            		leds[7-i].setOn();
            		
            		if(i==0 || i==4)
            		{
            			prev=7;
            		}
            		else
            		{
            			prev = i-1;
                		leds[prev].setOff();
                		leds[7-prev].setOff();
            		}
            		Utils.sleep(200);
            		leds[0].setOff();
            		leds[7].setOff();
            	}
            	else
            	{
	            	if(i<counter)
	            	{
	            		leds[i].setOn();
	            	}
	            	else
	            	{
	            		leds[i].setOff();
	            	}
	            	Utils.sleep(200);
            	}	
            	
                //leds[i].setOn();
            	               // on for 1/5 second
                //leds[i].setOff();
            }
            
    		

            
            /*for (int i = 0; i < 8; i++) {
                leds[i].setOff();
            }*/
            

            Utils.sleep(300);
/*
        	for (int i = 7; i >=0 ; i--) {
                leds[i].setRGB(gen.nextInt(255),gen.nextInt(255),gen.nextInt(255));
                leds[i].setOn();
                Utils.sleep(200);               // on for 1/5 second
                leds[i].setOff();
            }
            // third demo - pulse LED 3 so it gets brighter - do so 3 times
            /*for (int i = 0; i < 3; i++) {
                leds[3].setRGB(0, 0, 0);	// start it off dim
                leds[3].setOn();
                Utils.sleep(100);
                for (int j = 0; j < 255; j += 5) {
                    leds[3].setRGB(j, 0, 0);	// make it get brighter red
                    Utils.sleep(50);	        // change every 1/20 second
                }
            }
            leds[3].setOff();*/
        }
    }

    /**
     * MIDlet call to start our application.
     */
    protected void startApp() throws MIDletStateChangeException {
        for(int i=0;i<button.length;i++)
		{
			button[i].addISwitchListener(this);
		}
        try {
			demoLEDs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
        for (int i = 0; i < 8; i++) {
            leds[i].setOff();
        }
    }

	public void switchPressed(ISwitch arg0) {
		// TODO Auto-generated method stub
		
		if(arg0.equals(button[0]))
		{
			 blink3Color(LEDColor.RED,LEDColor.WHITE,LEDColor.GREEN);
            button[0].removeISwitchListener(this);
            button[1].addISwitchListener(this);
		}
		else
		{
			 blink3Color(LEDColor.YELLOW,LEDColor.ORANGE,LEDColor.RED);
			 button[1].removeISwitchListener(this);
             button[0].addISwitchListener(this);
		}
		Utils.sleep(2000);
	}

	public void blink3Color(LEDColor one,LEDColor two, LEDColor tree )
	{
		for (int j = 2; j >=0 ; j--) 
		{
            for (int i = 7; i >=0 ; i--)
            {
            	switch(j)
            	{
            		case 2:leds[i].setColor(one);break;
            		case 1:leds[i].setColor(two);break;
            		case 0: leds[i].setColor(tree);break;
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
	
	public void switchReleased(ISwitch arg0) {
		// TODO Auto-generated method stub
		
	}
}
