package edu.sou.rover2013.activities;

import android.os.Bundle;
import android.util.Log;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.utility.TCPService;

/**
 * Simple Test Screen that triggers wifi mode. Will remove/hide/etc later on.
 * 
 * @author Ryan Dempsey
 *
 */
public class WiFiServerActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wi_fi_server);
		Log.d(getClass().getSimpleName(), "Wifi Page Opened");

		Thread sThread = new Thread(new TCPService());
		sThread.start();
	}

}
