package edu.sou.rover2013.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.models.Rover;
import edu.sou.rover2013.utility.BluetoothService;

/**
 * This activity allows users to control a connected rover in a manner similar
 * to RC cars.
 */
// TODO Show rover telemetry graphics
// TODO Add buttons for controlling the laser
// TODO Don't allow more than one button press at a time.
public class ControlSimpleActivity extends BaseActivity {

	// *******************************
	// Class Variables
	// *******************************
	// Rover Model
	private Rover rover;
	protected static final long TIME_DELAY = 50;
	Handler handler = new Handler();

	// *******************************
	// UI Element Variables
	// *******************************
	private ImageButton buttonForward;
	private ImageButton buttonReverse;
	private ImageButton buttonLeft;
	private ImageButton buttonRight;
	private static TextView pingForward;
	private static TextView leftWheel;
	private static TextView rightWheel;
	
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
		pingForward = (TextView) findViewById(R.id.text_forward_ping);
		leftWheel = (TextView) findViewById(R.id.text_fl_infrared);
		rightWheel = (TextView) findViewById(R.id.text_fr_infrared);

		// *******************************
		// Button Listeners
		// *******************************
		buttonForward.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					forward();
					buttonForward
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					buttonForward
							.setImageResource(R.drawable.arrow_button_metal_silver_uptransie);
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
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					buttonReverse
							.setImageResource(R.drawable.arrow_button_metal_silver_downtransie);
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
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					buttonLeft
							.setImageResource(R.drawable.arrow_button_metal_silver_lefttransie);
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
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					buttonRight
							.setImageResource(R.drawable.arrow_button_metal_silver_righttransie);
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
			toast("Warning: Not Connected");
		}
		handler.post(updateTextRunnable);

	}

	// *******************************
	// Button Functions
	// *******************************
	private void forward() {
		Log.v("test", "Forward");
		rover.sendDataToRover(String.valueOf('\7'));
		rover.sendDataToRover("fd 999");
	}

	private void reverse() {
		Log.v("test", "Reverse");
		rover.sendDataToRover(String.valueOf('\7'));
		rover.sendDataToRover("bk 999");
	}

	private void left() {
		Log.v("test", "Left");
		rover.sendDataToRover(String.valueOf('\7'));
		rover.sendDataToRover("lt 999");
	}

	private void right() {
		Log.v("test", "Right");
		rover.sendDataToRover(String.valueOf('\7'));
		rover.sendDataToRover("rt 999");
	}

	protected void stop() {
		Log.v("test", "Stop");
		rover.sendDataToRover(String.valueOf('\7'));
	}
	
	Runnable updateTextRunnable = new Runnable() {
		public void run() {
				pingForward.setText(String.valueOf(rover.getPingFront()));
				leftWheel.setText(String.valueOf(rover.getInfaredFrontLeft()));
				rightWheel
						.setText(String.valueOf(rover.getInfaredFrontRight()));
				handler.postDelayed(this, TIME_DELAY);
		}
	};

	
}