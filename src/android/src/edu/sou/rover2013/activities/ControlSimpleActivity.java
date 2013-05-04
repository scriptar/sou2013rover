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
 * to RC cars. The Bluetooth Singleton must have a connected 
 */

// For example code, see:
// http://stackoverflow.com/questions/4284224/android-hold-button-to-repeat-action
// Possible Widgets? RepeatButton1, RepeatButton2
// Alternate Solution, Have Start/Stop Functions.
// TODO Show rover telemetry on screen, text and graphics
// TODO Improve user interface... Replace buttons with large, easily pressed
// arrow images that change appearance when pressed.
// TODO Add buttons for controlling the laser.
// TODO Skin Interface
public class ControlSimpleActivity extends BaseActivity {

	// *******************************
	// Variables
	// *******************************
	// UI Elements
	private Button forward;
	private Button reverse;
	private Button left;
	private Button right;

	// Rover Model
	private Rover rover;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_simple);

		// *******************************
		// Assigning UI Elements
		// *******************************
		forward = (Button) findViewById(R.id.button_forward);
		reverse = (Button) findViewById(R.id.button_reverse);
		left = (Button) findViewById(R.id.button_left);
		right = (Button) findViewById(R.id.button_right);

		// *******************************
		// Button Listeners
		// *******************************
		forward.setOnTouchListener(new OnTouchListener() {
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
		reverse.setOnTouchListener(new OnTouchListener() {
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
		left.setOnTouchListener(new OnTouchListener() {
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
		right.setOnTouchListener(new OnTouchListener() {
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
		// Further Activity Setup
		// *******************************
		// check for and get rover model
		if (!BluetoothService.getConnection().isConnected()) {
			Toast.makeText(getApplicationContext(), "Warning: Not Connected",
					Toast.LENGTH_SHORT).show();
		} else {
			rover = BluetoothService.getConnection().getRover();
		}
	}


	// *******************************
	// Button Actions
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