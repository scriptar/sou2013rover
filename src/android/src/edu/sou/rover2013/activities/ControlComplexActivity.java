package edu.sou.rover2013.activities;

import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.models.Rover;
import edu.sou.rover2013.utility.BluetoothService;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * This activity handles the construction of rogo scripts. Buttons, displays,
 * transmits. Must have valid bluetooth connection though
 * 
 * @author Ryan Dempsey
 * 
 */
public class ControlComplexActivity extends BaseActivity {

	// Storing this activity's context for use in dialogs
	Context activityContext;

	// UI Elements
	private static EditText scriptTextBox;
	private static Button buttonLaser;
	private static Button buttonLogic;
	private static Button buttonVariables;
	private static Button buttonProgramFlow;
	private static Button buttonPause;
	private static Button buttonTests;
	private static Button buttonArithmetic;
	private static Button buttonForward;
	private static Button buttonReverse;
	private static Button buttonLeft;
	private static Button buttonRight;
	private static Button buttonSave;
	private static Button buttonLoad;
	private static Button buttonClear;
	private static Button buttonSend;
	private static ListView listviewRoverOutput;
	private Button buttonUpdateRoverOutput;
	
	// *******************************
	// Class Variables
	// *******************************
	// Rover Model
	private Rover rover;
	//Rover Output Display
	private ArrayAdapter<String> roverOutputArrayAdapter;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Expanding XML
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_complex);

		// *******************************
		// Assigning UI Elements
		// *******************************
		scriptTextBox = (EditText) findViewById(R.id.complex_script);
		buttonLaser = (Button) findViewById(R.id.complex_laser);
		buttonLogic = (Button) findViewById(R.id.complex_logic);
		buttonVariables = (Button) findViewById(R.id.complex_variables);
		buttonProgramFlow = (Button) findViewById(R.id.complex_flow);
		buttonPause = (Button) findViewById(R.id.complex_pause);
		buttonTests = (Button) findViewById(R.id.complex_tests);
		buttonArithmetic = (Button) findViewById(R.id.complex_arithmetic);
		buttonForward = (Button) findViewById(R.id.complex_forward);
		buttonReverse = (Button) findViewById(R.id.complex_reverse);
		buttonLeft = (Button) findViewById(R.id.complex_left);
		buttonRight = (Button) findViewById(R.id.complex_right);
		buttonSave = (Button) findViewById(R.id.complex_save);
		buttonLoad = (Button) findViewById(R.id.complex_load);
		buttonClear = (Button) findViewById(R.id.complex_clear);
		buttonSend = (Button) findViewById(R.id.complex_transmit);
		listviewRoverOutput = (ListView) findViewById(R.id.listview_rover_output);
		buttonUpdateRoverOutput = (Button) findViewById(R.id.button_update_rover_output);

		// *******************************
		// Button Listeners
		// *******************************
		buttonLaser.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
		buttonLogic.setOnClickListener(new View.OnClickListener() {
			// Creates a selector Dialog Box
			final String selectTrue = "True";
			final String selectFalse = "False";
			final String selectAnd = "And";
			final String selectOr = "Or";
			final CharSequence[] list = { selectTrue, selectFalse, selectAnd,
					selectOr };

			public void onClick(View v) {
				AlertDialog.Builder chooserBox = new AlertDialog.Builder(
						activityContext);
				chooserBox.setTitle("Select a logical operator:");
				chooserBox.setItems(list,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								if (list[item].equals(selectTrue)) {
									scriptInsertAtCursor("True ");
								} else if (list[item].equals(selectFalse)) {
									scriptInsertAtCursor("False ");
								} else if (list[item].equals(selectAnd)) {
									scriptInsertAtCursor("And ");
								} else if (list[item].equals(selectOr)) {
									scriptInsertAtCursor("Or ");
								}
							}
						});
				AlertDialog alert = chooserBox.create();
				alert.show();
			}
		});
		buttonVariables.setOnClickListener(new View.OnClickListener() {
			// Creates a selector Dialog Box
			final CharSequence[] list = { "V0", "V1", "V2", "V3", "V4", "V5",
					"V6", "V7", "V8", "V9", "V10" };

			public void onClick(View v) {
				AlertDialog.Builder chooserBox = new AlertDialog.Builder(
						activityContext);
				chooserBox.setTitle("Select a variable:");
				chooserBox.setItems(list,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								scriptInsertAtCursor(list[item].toString()
										+ " ");
							}
						});
				AlertDialog alert = chooserBox.create();
				alert.show();
			}
		});
		buttonProgramFlow.setOnClickListener(new View.OnClickListener() {
			// Creates a selector Dialog Box
			private final String selectIf = "If (Test) [Code]";
			private final String selectElse = "If Else (Test) [Code]";
			private final String selectRepeat = "Repeat (Value) [Code]";
			final CharSequence[] list = { selectIf, selectElse, selectRepeat };

			public void onClick(View v) {
				AlertDialog.Builder chooserBox = new AlertDialog.Builder(
						activityContext);
				chooserBox.setTitle("Select a program flow structure:");
				chooserBox.setItems(list,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								if (list[item].equals(selectIf)) {
									scriptInsertAtCursor("If ( ) [ ] ");
								} else if (list[item].equals(selectElse)) {
									scriptInsertAtCursor("If Else ( ) [ ] ");
								} else if (list[item].equals(selectRepeat)) {
									scriptInsertAtCursor("Repeat value [ ] ");
								}
							}
						});
				AlertDialog alert = chooserBox.create();
				alert.show();
			}
		});
		buttonPause.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						activityContext);
				alert.setTitle("Pause/Wait");
				alert.setMessage("How long should the rover pause, in milliseconds?");
				// Set an EditText view to get user input
				final EditText input = new EditText(activityContext);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String value = input.getText().toString();
								scriptInsertAtCursor("Pause " + value + " ");
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Nothing Happens if closed
							}
						});

				alert.show();
			}
		});
		buttonTests.setOnClickListener(new View.OnClickListener() {
			// Creates a selector Dialog Box
			private final String selectEquals = "=";
			private final String selectNotEquals = "<>";
			private final String selectGreater = ">";
			private final String selectLess = "<";
			private final String selectGreaterEqual = ">=";
			private final String selectLessEqual = "<=";
			final CharSequence[] list = { selectEquals, selectNotEquals,
					selectGreater, selectLess, selectGreaterEqual,
					selectLessEqual };

			public void onClick(View v) {
				AlertDialog.Builder chooserBox = new AlertDialog.Builder(
						activityContext);
				chooserBox.setTitle("Select a comparison test operator:");
				chooserBox.setItems(list,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								if (list[item].equals(selectEquals)) {
									scriptInsertAtCursor("= ");
								} else if (list[item].equals(selectNotEquals)) {
									scriptInsertAtCursor("<> ");
								} else if (list[item].equals(selectGreater)) {
									scriptInsertAtCursor("> ");
								} else if (list[item].equals(selectLess)) {
									scriptInsertAtCursor("< ");
								} else if (list[item]
										.equals(selectGreaterEqual)) {
									scriptInsertAtCursor(">= ");
								} else if (list[item].equals(selectLessEqual)) {
									scriptInsertAtCursor("<= ");
								}
							}
						});
				AlertDialog alert = chooserBox.create();
				alert.show();
			}
		});
		buttonArithmetic.setOnClickListener(new View.OnClickListener() {
			// Creates a selector Dialog Box
			final CharSequence[] list = { "+", "-", "*", "/" };
			public void onClick(View v) {
				AlertDialog.Builder chooserBox = new AlertDialog.Builder(
						activityContext);
				chooserBox.setTitle("Select a math operator:");
				chooserBox.setItems(list,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								scriptInsertAtCursor(list[item]+" ");
							}
						});
				AlertDialog alert = chooserBox.create();
				alert.show();
			}
		});
		buttonForward.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						activityContext);
				alert.setTitle("Forward");
				alert.setMessage("Enter the number of units to roll forward:");
				// Set an EditText view to get user input
				final EditText input = new EditText(activityContext);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String value = input.getText().toString();
								scriptInsertAtCursor("fd " + value + " ");
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Nothing Happens if closed
							}
						});
				alert.show();
			}
		});
		buttonReverse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						activityContext);
				alert.setTitle("Forward");
				alert.setMessage("Enter the number of units to roll in reverse:");
				// Set an EditText view to get user input
				final EditText input = new EditText(activityContext);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String value = input.getText().toString();
								scriptInsertAtCursor("bk " + value + " ");
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Nothing Happens if closed
							}
						});
				alert.show();
			}
		});	
		buttonLeft.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						activityContext);
				alert.setTitle("Left");
				alert.setMessage("Enter the number of units to rotate left:");
				// Set an EditText view to get user input
				final EditText input = new EditText(activityContext);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String value = input.getText().toString();
								scriptInsertAtCursor("lt " + value + " ");
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Nothing Happens if closed
							}
						});
				alert.show();
			}
		});
		buttonRight.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						activityContext);
				alert.setTitle("Forward");
				alert.setMessage("Enter the number of units to rotate right:");
				// Set an EditText view to get user input
				final EditText input = new EditText(activityContext);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String value = input.getText().toString();
								scriptInsertAtCursor("rt " + value + " ");
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Nothing Happens if closed
							}
						});
				alert.show();
			}
		});
		buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
		buttonLoad.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
		buttonClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				scriptClear();
				Toast toast = Toast.makeText(getApplicationContext(),
						"Script Cleared", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		buttonSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				transmitScript();
				roverOutputArrayAdapter.notifyDataSetChanged();
			}
		});
		buttonUpdateRoverOutput.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				roverOutputArrayAdapter.notifyDataSetChanged();
			}
		});
		// *******************************
		// Further Activity Setup
		// *******************************
		// Prevent keyboard auto-show
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// Assigning Context Variable
		activityContext = this;
		// Clear TextBox
		scriptClear();
		// set bluetooth context for ui updates

		
		// check for and get rover model
		if(!BluetoothService.getConnection().isConnected()){
			Toast.makeText(getApplicationContext(), "Warning: Not Connected" ,Toast.LENGTH_SHORT).show();
			rover = null;
			listviewRoverOutput.setAdapter(null);
		}else {
			rover = BluetoothService.getConnection().getRover();
			// Attach rover output to listView
			roverOutputArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, rover.getRoverOutput());
			
			listviewRoverOutput.setAdapter(roverOutputArrayAdapter);
		}
		
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
	@SuppressWarnings("unused")
	private void scriptAppend(String string) {
		scriptTextBox.getEditableText().append(string);
	}

	/**
	 * Inserts the passed-in string to the script at the current cursor
	 * location.
	 * 
	 * @param string
	 *            Value to Insert
	 */
	private void scriptInsertAtCursor(String string) {
		Editable str = scriptTextBox.getText();
		String text = string;
		int len = text.length();
		str.insert(scriptTextBox.getSelectionStart(), text, 0, len);
	}

	/**
	 * Sends the script text-box contents to the rover model. 
	 */
	private void transmitScript() {
		if(rover!=null){
			rover.sendDataToRover(scriptTextBox.getText().toString());
			Toast.makeText(getApplicationContext(),"Script Transmitted", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(),"Rover Not Connected", Toast.LENGTH_SHORT).show();
		}
	}

}
