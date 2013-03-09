package edu.sou.rover2013;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 
 * Class is used as a simple interface to transmit commands to the rover.
 * 
 * @author Ryan Dempsey
 * 
 */
// TODO change from single transmit to "transmit continuously when button held.
// For example code, see:
// http://stackoverflow.com/questions/4284224/android-hold-button-to-repeat-action
// TODO Have rover commands appear in text box as well.
public class ControlSimpleActivity extends BaseActivity {

	private RogoApplication app;
	private WirelessConnection connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Calling super, and expanding view
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_simple);

		// Catch parent and connection class for usage
		app = (RogoApplication) getApplication();
		connection = app.getWirelessConnection();

		// Establishing button references
		final Button forward = (Button) findViewById(R.id.forward);
		final Button reverse = (Button) findViewById(R.id.reverse);
		final Button left = (Button) findViewById(R.id.left);
		final Button right = (Button) findViewById(R.id.right);

		// Inline Button Listeners
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
	}

	public void forward() {
		connection.transmitByte((byte) 10);
	}

	private void reverse() {
		connection.transmitByte((byte) 20);
	}

	private void left() {
		connection.transmitByte((byte) 30);
	}

	private void right() {
		connection.transmitByte((byte) 40);
	}
}