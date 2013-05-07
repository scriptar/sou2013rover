package edu.sou.rover2013.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.models.Rover;
import edu.sou.rover2013.utility.BluetoothService;

/**
 * This activity allows users to control a connected rover in a manner similar
 * to RC cars.
 */
// TODO Show rover telemetry on screen, text and graphics
// TODO Improve user interface... Replace buttons with large images
// TODO Add buttons for controlling the laser
public class ControlSimpleActivity extends BaseActivity {

	// *******************************
	// Class Variables
	// *******************************
	// Rover Model
	private Rover rover;

	// *******************************
	// UI Element Variables
	// *******************************
	private Button buttonForward;
	private Button buttonReverse;
	private Button buttonLeft;
	private Button buttonRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_simple);

		// *******************************
		// Assigning UI Elements
		// *******************************
		buttonForward = (Button) findViewById(R.id.button_forward);
		buttonReverse = (Button) findViewById(R.id.button_reverse);
		buttonLeft = (Button) findViewById(R.id.button_left);
		buttonRight = (Button) findViewById(R.id.button_right);

		// *******************************
		// Button Listeners
		// *******************************
		buttonForward.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					forward();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					return true;
				}
				return true;
			}
		});
		buttonReverse.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					reverse();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					return true;
				}
				return true;
			}
		});
		buttonLeft.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					left();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					return true;
				}
				return true;
			}
		});
		buttonRight.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					right();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					return true;
				}
				return true;
			}
		});

		// *******************************
		// Activity Setup
		// *******************************
		// get rover model
		rover = BluetoothService.getConnection().getRover();
		// throw warning if not connected
		if (!BluetoothService.getConnection().isConnected()) {
			Toast.makeText(getApplicationContext(), "Warning: Not Connected",
					Toast.LENGTH_LONG).show();
		}
	}

	// *******************************
	// Button Functions
	// *******************************
	private void forward() {
		Log.v("test", "Forward");
		rover.sendDataToRover("fd 999");
	}

	private void reverse() {
		Log.v("test", "Reverse");
		rover.sendDataToRover("bk 999");
	}

	private void left() {
		Log.v("test", "Left");
		rover.sendDataToRover("lt 999");
	}

	private void right() {
		Log.v("test", "Right");
		rover.sendDataToRover("rt 999");
	}

	protected void stop() {
		Log.v("test", "Stop");
		rover.sendDataToRover("fd 0");
	}

}