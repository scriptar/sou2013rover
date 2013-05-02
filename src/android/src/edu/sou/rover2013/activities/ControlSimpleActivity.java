package edu.sou.rover2013.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.models.Rover;
import edu.sou.rover2013.utility.BluetoothService;

/**
 * This activity allows users to control a connected rover in a manner similar
 * to RC cars. The Bluetooth Singleton must have a connected
 * 
 * @author Ryan Dempsey
 * 
 */
// TODO change from single transmit on press to continuous movement when button
// is held.
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
		forward.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				forward();
			}
		});
		reverse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				reverse();
			}
		});
		left.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				left();
			}
		});
		right.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				right();
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
	}

	private void reverse() {
	}

	private void left() {
	}

	private void right() {
	}
}