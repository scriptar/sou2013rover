package edu.sou.rover2013.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
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
// TODO Don't allow more than one button press at a time.
public class ControlSimpleActivity extends BaseActivity {

	// *******************************
	// Class Variables
	// *******************************
	// Rover Model
	private Rover rover;

	// *******************************
	// UI Element Variables
	// *******************************
	private ImageButton buttonForward;
	private ImageButton buttonReverse;
	private ImageButton buttonLeft;
	private ImageButton buttonRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_simple);

		// *******************************
		// Assigning UI Elements
		// *******************************
		buttonForward = (ImageButton) findViewById(R.id.button_forward);
		buttonReverse = (ImageButton) findViewById(R.id.button_reverse);
		buttonLeft = (ImageButton) findViewById(R.id.button_left);
		buttonRight = (ImageButton) findViewById(R.id.button_right);

		// *******************************
		// Button Listeners
		// *******************************
		buttonForward.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					forward();
					buttonForward
							.setBackgroundResource(R.drawable.arrow_button_metal_green_blank);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					buttonForward
							.setBackgroundResource(R.drawable.arrow_button_metal_green_up);
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
					buttonReverse
							.setBackgroundResource(R.drawable.arrow_button_metal_green_blank);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					buttonReverse
							.setBackgroundResource(R.drawable.arrow_button_metal_green_down);
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
					buttonLeft
							.setBackgroundResource(R.drawable.arrow_button_metal_green_blank);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					buttonLeft
							.setBackgroundResource(R.drawable.arrow_button_metal_green_left);
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
					buttonRight
							.setBackgroundResource(R.drawable.arrow_button_metal_green_blank);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					buttonRight
							.setBackgroundResource(R.drawable.arrow_button_metal_green_right);
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