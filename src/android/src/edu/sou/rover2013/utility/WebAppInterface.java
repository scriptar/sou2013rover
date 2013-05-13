package edu.sou.rover2013.utility;

import edu.sou.rover2013.models.Rover;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
	Context mContext;
	private Rover rover;

	/** Instantiate the interface and set the context */
	public WebAppInterface(Context c, Rover rover) {
		mContext = c;
		this.rover = rover;
	}

	/** Takes a string and shows it as a quick alert on the Android */
	@JavascriptInterface
	public void showAlert(String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}

	/** Takes a string and sends it to the connected rover */
	@JavascriptInterface
	public void sendSourceToRover(String stringArg) {
		rover.sendDataToRover(stringArg);
	}

	/** Takes a string and places it into advanced mode */
	@JavascriptInterface
	public void sendSourceToAdvancedMode(String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}
}