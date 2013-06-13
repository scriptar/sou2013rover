package edu.sou.rover2013.activities;

import android.os.Bundle;
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
import edu.sou.rover2013.widgets.DigitalTextBox;

/**
 * 
 * This activity allows users to control a connected rover in a manner similar
 * to RC cars.
 * 
 */
// TODO Show rover telemetry graphics
// TODO Have laser slowly move up and down on button hold
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
	private ImageButton buttonLaserFire;
	private ImageButton buttonLaserUp;
	private ImageButton buttonLaserDown;

	private static TextView pingForward;
	private static TextView leftWheel;
	private static TextView rightWheel;
	private static TextView battHigh;
	private static TextView battLow;
	private static TextView freeRam;
	private static TextView xCoord;
	private static TextView yCoord;
	private static TextView heading;

	private UIUpdater mUIUpdater;

	private static DigitalTextBox textLaserAngle;

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
		buttonLaserFire = (ImageButton) findViewById(R.id.button_laserfire);
		buttonLaserUp = (ImageButton) findViewById(R.id.button_laserup);
		buttonLaserDown = (ImageButton) findViewById(R.id.button_laserdown);
		buttonRight = (ImageButton) findViewById(R.id.button_right);
		
		pingForward = (TextView) findViewById(R.id.text_forward_ping);
		leftWheel = (TextView) findViewById(R.id.text_fl_infrared);
		rightWheel = (TextView) findViewById(R.id.text_fr_infrared);
		textLaserAngle = (DigitalTextBox) findViewById(R.id.text_laser_angle);
		battHigh = (TextView) findViewById(R.id.text_high_batt);
		battLow = (TextView) findViewById(R.id.text_low_batt);
		freeRam = (TextView) findViewById(R.id.text_free_mem);
		xCoord = (TextView) findViewById(R.id.text_x_loc);
		yCoord = (TextView) findViewById(R.id.text_y_loc);
		heading = (TextView) findViewById(R.id.text_heading);

		// *******************************
		// Button Listeners
		// *******************************
		buttonForward.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					forward();
					Log.v("test", "Forward");

					buttonForward
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					Log.v("test", "Forward Stop");
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
					Log.v("test", "Reverse");
					buttonReverse
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					Log.v("test", "Reverse Stop");
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
					Log.v("test", "Left");
					buttonLeft
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					Log.v("test", "Left Stop");
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
					Log.v("test", "Right");
					buttonRight
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stop();
					Log.v("test", "Right Stop");
					buttonRight
							.setImageResource(R.drawable.arrow_button_metal_silver_righttransie);
					return true;
				}
				return true;
			}
		});
		buttonLaserFire.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					startFire();
					Log.v("test", "Fire");
					buttonLaserFire
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					stopFire();
					Log.v("test", "Fire Stop");
					buttonLaserFire
							.setImageResource(R.drawable.arrow_button_metal_silver_lasertransie);
					return true;
				}
				return true;
			}
		});
		buttonLaserUp.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					laserUp();
					Log.v("test", "Aim Up: " + (rover.getLaserAngle()));
					buttonLaserUp
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					buttonLaserUp
							.setImageResource(R.drawable.arrow_button_metal_silver_aimuptransie);
					return true;
				}
				return true;
			}
		});
		buttonLaserDown.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					laserDown();
					Log.v("test", "Aim Down: " + (rover.getLaserAngle()));
					buttonLaserDown
							.setImageResource(R.drawable.arrow_button_metal_silver_blanktransie);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					buttonLaserDown
							.setImageResource(R.drawable.arrow_button_metal_silver_aimdowntransie);
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
			toast("Warning: Rover Not Connected");
		}
		// Begin updating
		mUIUpdater = new UIUpdater(new Runnable() {
			@Override
			public void run() {
				updateGUI();
			}
		}, 90);

		// Fill Laser Field
		textLaserAngle.setText(String.valueOf(rover.getLaserAngle()));
	}

	// *******************************
	// Button Functions
	// *******************************
	private void forward() {
		stop();
		rover.sendDataToRover("fd 999");
	}

	private void reverse() {
		stop();
		rover.sendDataToRover("bk 999");
	}

	private void left() {
		stop();
		rover.sendDataToRover("lt 999");
	}

	private void right() {
		stop();
		rover.sendDataToRover("rt 999");
	}

	private void stop() {
		rover.sendDataToRover(String.valueOf('\7'));
	}

	private void startFire() {
		stop();
		rover.sendDataToRover("lzfire(1)");
	}

	private void stopFire() {
		stop();
		rover.sendDataToRover("lzfire(0)");
	}

	private void laserUp() {
		stop();
		int laserAngle = rover.getLaserAngle();
		laserAngle += 5;
		if (laserAngle > 90) {
			laserAngle = 90;
		}
		rover.setLaserAngle(laserAngle);
		textLaserAngle.setText(String.valueOf(laserAngle));
		rover.sendDataToRover("lzaim(" + laserAngle + ")");
	}

	private void laserDown() {
		stop();
		int laserAngle = rover.getLaserAngle();
		laserAngle -= 5;
		if (laserAngle < 0) {
			laserAngle = 0;
		}
		rover.setLaserAngle(laserAngle);
		textLaserAngle.setText(String.valueOf(laserAngle));
		rover.sendDataToRover("lzaim(" + laserAngle + ")");
	}

	// GUI Updater
	public void updateGUI() {
		int ping = rover.getPingFront();
		if (ping > 200) {
			ping = 0;
		}
		pingForward.setText(String.valueOf(ping));
		leftWheel.setText(String.valueOf(rover.getInfaredFrontLeft()));
		rightWheel.setText(String.valueOf(rover.getInfaredFrontRight()));
		battLow.setText(String.valueOf(rover.getBattLow()));
		battHigh.setText(String.valueOf(rover.getBattHigh()));
		freeRam.setText(String.valueOf(rover.getFreeRam()));
		xCoord.setText(String.valueOf(rover.getXCoord()));
		yCoord.setText(String.valueOf(rover.getYCoord()));
		heading.setText(String.valueOf(rover.getHeading()));
	}

	@Override
	protected void onResume() {
		super.onResume();
		mUIUpdater.startUpdates();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mUIUpdater.stopUpdates();
	}
}
