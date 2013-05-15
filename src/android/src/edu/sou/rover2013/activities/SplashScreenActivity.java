package edu.sou.rover2013.activities;

import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.sou.rover2013.activities.ConnectionActivity;
import edu.sou.rover2013.activities.ControlComplexActivity;
import edu.sou.rover2013.activities.ControlSimpleActivity;

/**
 * Initial Activity with menu options and instructions on connecting rover
 */

public class SplashScreenActivity extends BaseActivity {

	// *******************************
	// UI Element Variables
	// *******************************
	private static Button buttonConnect;
	private static Button buttonControl;
	private static Button buttonProgram;
	private static Button buttonWeb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		// *******************************
		// Assigning UI Elements
		// *******************************
		buttonConnect = (Button) findViewById(R.id.button_connect);
		buttonControl = (Button) findViewById(R.id.button_control);
		buttonProgram = (Button) findViewById(R.id.button_program);
		buttonWeb = (Button) findViewById(R.id.button_web);
		// *******************************
		// Button Listeners
		// *******************************
		buttonConnect.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ConnectionActivity.class);
				startActivity(intent);
			}
		});
		buttonControl.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ControlSimpleActivity.class);
				startActivity(intent);
			}
		});
		buttonProgram.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ControlComplexActivity.class);
				startActivity(intent);
			}
		});
		buttonWeb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ControlWebActivity.class);
				startActivity(intent);
			}
		});
	}

}