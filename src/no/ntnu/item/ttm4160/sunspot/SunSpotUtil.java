/**
 * 
 */
package no.ntnu.item.ttm4160.sunspot;

import java.io.IOException;

import com.sun.spot.peripheral.Spot;
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ISwitch;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.sensorboard.peripheral.LightSensor;
import com.sun.spot.util.IEEEAddress;
import com.sun.spot.util.Utils;

/**
 * @author christiangiovanelli
 *
 */
public class SunSpotUtil {
	
	private static ITriColorLED [] leds = EDemoBoard.getInstance().getLEDs();
	private static LightSensor sens = (LightSensor) EDemoBoard.getInstance().getLightSensor();
	
	
	/**
	 * this function blinks the leds of the Sunspot
	 * */
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
	
	/**
	 * 
	 * @return the avg of the light sensor
	 * @throws IOException
	 */
	public static int getLightAvg() throws IOException
	{
		return sens.getAverageValue();
	}
	
	public static void clearLeds()
	{
		for (int i = 8; i >=0 ; i--)
        {
        	leds[i].setOff();
        }
	}
	
	/**
	 * with a given avg it print on the leds the corresponding value
	 * @param avg
	 */
	public static void lightToLeds(int avg)
	{
		int counter;
        counter = avg/84;
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
  //      		Utils.sleep(200);
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
//            	Utils.sleep(200);
        	}	
        }
	}
	
	public static void testMethod(int led)
	{
		int number;
		if(led>=0 && led <8)
		{
			number = led;
		}
		else
		{
			number = 7;
		}
		
		for (int j = 2; j >=0 ; j--) 
		{
            for (int i = number; i >=0 ; i--)
            {
            	switch(j)
            	{
            		case 2:leds[i].setColor(LEDColor.RED);break;
            		case 1:leds[i].setColor(LEDColor.WHITE);break;
            		case 0: leds[i].setColor(LEDColor.GREEN);break;
            	}
            	leds[i].setOn();
            }
            Utils.sleep(300);
            for (int i = number; i >=0 ; i--)
            {
            	leds[i].setOff();
            }
            Utils.sleep(200);           
        }
	}
	
	/**
	 * 
	 * @return the MAc address of the device
	 */
	public static String getMyMac()
	{
		return new IEEEAddress(Spot.getInstance().getRadioPolicyManager().getIEEEAddress()).asDottedHex();
	}
	
	public static ISwitch getButton(int number)
	{
		if(number ==0)
		{
			return EDemoBoard.getInstance().getSwitches()[0];
		}
		else
		{
			return EDemoBoard.getInstance().getSwitches()[1];
		}
	}
}


