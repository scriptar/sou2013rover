package edu.sou.rover2013.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.models.Rover;

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

		rover = new Rover();

		// *******************************
		// Assigning UI Elements
		// *******************************
		forward = (Button) findViewById(R.id.forward);
		reverse = (Button) findViewById(R.id.reverse);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);

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

		// TODO Show alert if rover not connected by bluetooth.
	}

	private void forward() {
//		rover.transmitRogoScript("fd 1");
	}

	private void reverse() {
//		rover.transmitRogoScript("bk 1");
	}

	private void left() {
//		rover.transmitRogoScript("lt 1");
	}

	private void right() {
//		rover.transmitRogoScript("rt 1");
	}
}