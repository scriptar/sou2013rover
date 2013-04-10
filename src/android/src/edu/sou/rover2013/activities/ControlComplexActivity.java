package edu.sou.rover2013.activities;

import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.models.Rover;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

	// Storing this activities context for use in dialogs
	Context activityContext;

	// UI Elements
	private static EditText scriptTextBox;
	private static Button button_laser;
	private static Button button_logic;
	private static Button button_variables;
	private static Button button_program_flow;
	private static Button button_pause;
	private static Button button_tests;
	private static Button button_variable_math;
	private static Button button_arithmetic;
	private static Button button_forward;
	private static Button button_reverse;
	private static Button button_left;
	private static Button button_right;
	private static Button button_save;
	private static Button button_load;
	private static Button button_clear;
	private static Button button_send;

	// Rover Model
	private Rover rover;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Expanding XML
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_complex);

		// *******************************
		// Assigning UI Elements
		// *******************************
		scriptTextBox = (EditText) findViewById(R.id.complex_script);
		button_laser = (Button) findViewById(R.id.complex_laser);
		button_logic = (Button) findViewById(R.id.complex_logic);
		button_variables = (Button) findViewById(R.id.complex_variables);
		button_program_flow = (Button) findViewById(R.id.complex_flow);
		button_pause = (Button) findViewById(R.id.complex_pause);
		button_tests = (Button) findViewById(R.id.complex_tests);
		button_variable_math = (Button) findViewById(R.id.complex_variable_math);
		button_arithmetic = (Button) findViewById(R.id.complex_arithmetic);
		button_forward = (Button) findViewById(R.id.complex_forward);
		button_reverse = (Button) findViewById(R.id.complex_reverse);
		button_left = (Button) findViewById(R.id.complex_left);
		button_right = (Button) findViewById(R.id.complex_right);
		button_save = (Button) findViewById(R.id.complex_save);
		button_load = (Button) findViewById(R.id.complex_load);
		button_clear = (Button) findViewById(R.id.complex_clear);
		button_send = (Button) findViewById(R.id.complex_transmit);

		// *******************************
		// Button Listeners
		// *******************************
		button_laser.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
		button_logic.setOnClickListener(new View.OnClickListener() {
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
		button_variables.setOnClickListener(new View.OnClickListener() {
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
		button_program_flow.setOnClickListener(new View.OnClickListener() {
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
		button_pause.setOnClickListener(new View.OnClickListener() {
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
		button_tests.setOnClickListener(new View.OnClickListener() {
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
		button_variable_math.setOnClickListener(new View.OnClickListener() {
			// Creates a selector Dialog Box
			private final String selectAdd = "Add a value to a Variable";
			private final String selectSub = "Subtract a value from a Variable";
			private final String selectMul = "Multiply a value with a Variable";
			private final String selectDiv = "Divide a Variable by a value";
			final CharSequence[] list = { selectAdd, selectSub, selectMul,
					selectDiv };
			public void onClick(View v) {
				AlertDialog.Builder chooserBox = new AlertDialog.Builder(
						activityContext);
				chooserBox
						.setTitle("Select a math operator to apply to a variable:");
				chooserBox.setItems(list,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								if (list[item].equals(selectAdd)) {
									scriptInsertAtCursor("Add V0 0 ");
								} else if (list[item].equals(selectSub)) {
									scriptInsertAtCursor("Sub V0 0 ");
								} else if (list[item].equals(selectMul)) {
									scriptInsertAtCursor("Mul V0 0 ");
								} else if (list[item].equals(selectDiv)) {
									scriptInsertAtCursor("Div V0 0 ");
								}
							}
						});
				AlertDialog alert = chooserBox.create();
				alert.show();
			}
		});
		button_arithmetic.setOnClickListener(new View.OnClickListener() {
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
		button_forward.setOnClickListener(new View.OnClickListener() {
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
		button_reverse.setOnClickListener(new View.OnClickListener() {
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
		button_left.setOnClickListener(new View.OnClickListener() {
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
		button_right.setOnClickListener(new View.OnClickListener() {
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
		button_save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
		button_load.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
		button_clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				scriptClear();
				Toast toast = Toast.makeText(getApplicationContext(),
						"Script Cleared", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		button_send.setOnClickListener(new View.OnClickListener() {
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
		// Assigning Context Variable
		activityContext = this;
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
	 * Sends the script text-box contents to the rover model. TODO Check for
	 * valid Bluetooth Connection TODO Get results, show on screen
	 */
	private void transmitScript() {
		rover.transmitRogoScript(scriptTextBox.getText().toString());
		Toast toast = Toast.makeText(getApplicationContext(),
				"Script Transmitted", Toast.LENGTH_SHORT);
		toast.show();
	}

}
