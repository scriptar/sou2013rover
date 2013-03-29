package edu.sou.rover2013.activities;

import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.models.Rover;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * This activity handles the construction of rogo scripts. Buttons, displays,
 * transmits. Must have valid bluetooth connection though
 * 
 * @author Ryan Dempsey
 * 
 */
public class ControlComplexActivity extends BaseActivity {

	// UI Elements
	private EditText scriptTextBox;
	private Button forward;
	private Button reverse;
	private Button left;
	private Button right;
	private Button clear;
	private Button sample1;
	private Button sample2;
	private Button sample3;
	private Button transmit;

	// Rover Model
	private Rover rover;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_complex);

		// *******************************
		// Assigning UI Elements
		// *******************************
		scriptTextBox = (EditText) findViewById(R.id.complex_script);
		forward = (Button) findViewById(R.id.complex_forward);
		reverse = (Button) findViewById(R.id.complex_reverse);
		left = (Button) findViewById(R.id.complex_left);
		right = (Button) findViewById(R.id.complex_right);
		clear = (Button) findViewById(R.id.complex_clear);
		sample1 = (Button) findViewById(R.id.complex_sample_one);
		sample2 = (Button) findViewById(R.id.complex_sample_two);
		sample3 = (Button) findViewById(R.id.complex_sample_three);
		transmit = (Button) findViewById(R.id.complex_transmit);
		
		// *******************************
		// Button Listeners
		// *******************************
		forward.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				scriptAppend("fd 2\n");
			}
		});
		reverse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				scriptAppend("bk 2\n");
			}
		});
		left.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				scriptAppend("lt 2\n");
			}
		});
		right.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				scriptAppend("rt 2\n");
			}
		});
		clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				scriptClear();
			}
		});
		sample1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String string = "if [1==not 0] [fd 100]";
				scriptClear();
				scriptAppend(string);
			}
		});
		sample2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String string = "v0=3.2 v1=7 v2=v0 v3=45 v4=0 repeat v0[fd v2 rt v3 repeat 4[fd v1]] if 1==-1+2 and 1 or not 1 [fd 100 v4=not v4] if v4 [bk 100 lt v3] if 1>2 [fd 1] if 2>1 [fd 2] if 1<2 [fd 3] if 2<1 [fd 4] if 1<>2 [fd 5] repeat 3 [v4=not v4 if v4<>1 [fd 6]] ;repeat 3 [fd 0 v4=not v4 if v4<>1 [fd 6]] repeat 1.18920711500 * 1.18920711500 * 1.41421356237 fd 3.141592653589793238462";
				scriptClear();
				scriptAppend(string);
			}
		});
		sample3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String string = "v0=1 v1=2 v2=3 v4=0 repeat [[v1+v2]] [v4=not v4 if [v4 or 0] [fd v0] v0=v0+1]";
				scriptClear();
				scriptAppend(string);
			}
		});
		transmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				transmitScript();
			}
		});

		// *******************************
		// Activity Initialization
		// *******************************
		// Prevent keyboard auto-show
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// Clear TextBox
		scriptClear();
		// Create Rover Model
		rover = new Rover();
	}

	/**
	 * Clears the script text box
	 */
	private void scriptClear() {
		scriptTextBox.getText().clear();
	}

	/**
	 * Appends the passed in string to the script text box.
	 * 
	 * @param string
	 *            Value to append
	 */
	private void scriptAppend(String string) {
		scriptTextBox.getEditableText().append(string);
	}

	/**
	 * Sends the script text-box contents to the rover model.
	 */
	private void transmitScript(){
		rover.transmitRogoScript(scriptTextBox.getText().toString());
		// TODO Get Output, show on screen
	}
	
}
