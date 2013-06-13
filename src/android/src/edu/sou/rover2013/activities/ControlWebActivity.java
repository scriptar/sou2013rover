package edu.sou.rover2013.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.models.Rover;
import edu.sou.rover2013.utility.*;

@SuppressLint("SetJavaScriptEnabled")
public class ControlWebActivity extends BaseActivity {

	private Rover rover;

	// *******************************
	// UI Element Variables
	// *******************************
	private static WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);

		// *******************************
		// Assigning UI Elements
		// *******************************
		webView = (WebView) findViewById(R.id.webView1);

		// *******************************
		// Button Listeners
		// *******************************


		// *******************************
		// Activity Setup
		// *******************************
		// get rover model
		rover = BluetoothService.getConnection().getRover();
		// throw warning if not connected
		if (!BluetoothService.getConnection().isConnected()) {
			toast("Warning: Rover Not Connected");
		}
		// prepare Web View
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.addJavascriptInterface(new WebAppInterface(this, rover), "Android");

		// Load Web View
		webView.loadUrl("http://rogo.sou.edu/droid/");
	}
}