package PiCamera.Camera;

import jrpicam.RPiCamera;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jrpicam.enums.ImageEffect;
import jrpicam.exceptions.FailedToRunRaspistillException;

public class Shutter{

	/**
	 * original resolution from sensor is 2592x1944
	 */
	private static int width = 2560;

	private static int height = 1440;

	/**
	 * currently only use JPG Encoding
	 */
	//private static String format = "jpg";
	private static String format = "png";

	private static String directory = "/home/pi/Pictures/";

	/**
	 * define the timeout value, the unit is ms
	 */
	private static int timeoutLength = 5000;

	/**
	 * define the rotate angle, only used for specific camera module in case the module itself not in correct angle
	 */
	private static int rotateAngle = 0;

	private RPiCamera piCamera;

	//constructor
	public Shutter(){
		//initial piCamera
		try{
			piCamera = new RPiCamera();
		} catch (FailedToRunRaspistillException e) {
			e.printStackTrace();
		}
	}

	/**
	 * image effects used in application
	 */
	public void setImageEffect(String effectName) {
		/*below are fill in codes*/
		/**
		 * @author Sugita Ikuto
		 * */
		switch(effectName){
			case "NONE":
				piCamera.setImageEffect(ImageEffect.NONE);
				break;
			case "NEGATIVE":
				piCamera.setImageEffect(ImageEffect.NEGATIVE);
				break;
			case "SKETCH":
				piCamera.setImageEffect(ImageEffect.SKETCH);
				break;
			case "EMBOSS":
				piCamera.setImageEffect(ImageEffect.EMBOSS);
				break;
			case "OILPAINT":
				piCamera.setImageEffect(ImageEffect.OILPAINT);
				break;
			case "CARTOON":
				piCamera.setImageEffect(ImageEffect.CARTOON);
				break;
		}
	}

	/**
	 * shoot photo, call function 
	 * takeStill(name : String, width : int, height : int) from jrpicam
	 * name is the generated name
	 */
	public void shootStill() {

		//turn off default preview when customized preview function complete
		//piCamera.turnOffPreview();

		/*below are fill in codes*/
		String name = generateName();

		try{
			piCamera.takeStill(name, width, height);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("saved as " + name);		//print out shot photo in command line for checking

	}

	/**
	 * use date and resolution as name, like:160728074523_2592_1944.jpg
	 * (yyMMddHHmmss_WIDTHxHEIGHT.format)
	 */
	private String generateName() {
		/*below are fill in codes*/
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

		String name = sdf.format(c.getTime()) + "_" + width + "_" + height + "." + format;

		return name;
	}

}
