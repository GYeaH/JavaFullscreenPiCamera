package PiCamera.Camera;

import jrpicam.RPiCamera;
import jrpicam.exceptions.FailedToRunRaspistillException;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

//public class Preview {
public class Preview implements Runnable{

	/**
	 * x coordinate planned to bas on the location of panel
	 */
	private int x = 0;

	/**
	 * y coordinate planned to bas on the location of panel
	 */
	private int y = 0;

	/**
	 * width planned to bas on width of panel
	 */
	private int width = 400;

	/**
	 * height planned to bas on width of panel
	 */
	private int height = 300;

	private RPiCamera piCamera;

	/*ADDITION*/
	private BufferedImage bufferImg = null;
	private ImageIcon previewBufferedImg = null;

	private JLabel displayLabel;

	private boolean previewTrigger = false;

	//constructor
	public Preview(JLabel displayLabel){

		this.displayLabel = displayLabel;

		//initial piCamera
		try{
			piCamera = new RPiCamera();
		} catch (FailedToRunRaspistillException e) {
			e.printStackTrace();
		}
	}
	/**
	 * call from jrpicam
	 * piCamera.turnOnPreview(x, y, width, height);
	 */
	/*
	public void enablePreview() {

	}
*/
	/*ADDITION*/
	public void enablePreview() {
		//turn off default preview
		piCamera.turnOffPreview();

		previewTrigger ^=true;

		while(!previewTrigger){
			try {
				bufferImg = piCamera.takeBufferedStill(width, height);
				previewBufferedImg = new ImageIcon(bufferImg);
				displayLabel.setIcon(previewBufferedImg);
				displayLabel.repaint();

			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}

	}


	/**
	 * call from jrpicam
	 * piCamera.turnOffPreview();
	 */
	public void disablePreview() {
		/*below are fill in codes*/
		previewTrigger ^=true;
		//stop bufferedImage
		bufferImg = null;
	}


	@Override
	public void run() {
		enablePreview();
	}
}
