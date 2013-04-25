package edu.sou.rover2013.models;

import java.io.IOException;
import java.util.ArrayList;

import edu.sou.rover2013.utility.Bluetooth;

/**
 * This class represents the Rogo rover. Collected data will be stored here, as
 * well as the current connection. Uses the Bluetooth Service for communication.
 * All rover/bluetooth communication passes through here. The model maintains a
 * log of actions and errors.
 * 
 * @author Ryan Dempsey
 * 
 */
public class Rover {

	private Bluetooth bluetoothConnection;
	private ArrayList<String> roverLog;

	private static final int LOG = 0;
	private static final int LOG_ERROR = 1;

	/**
	 * Rover Constructor
	 */
	public Rover() {
		bluetoothConnection = Bluetooth.getConnection();
		roverLog = new ArrayList<String>(1000);
		log(LOG, "Rover Object Created");
	}

	/**
	 * Sends Rogo Script to the connected Rover. Scripts will have "start" and
	 * "stop" lines appended for use when receiving on rover.
	 * 
	 * @param scriptArg
	 *            Script to send to the Rogo Rover. Must be properly
	 *            constructed.
	 */
	public void transmitRogoScript(String scriptArg) {
		log(LOG, "Sending Command '" + scriptArg + "'");
		if (!bluetoothConnection.isConnected()) {
			log(LOG_ERROR, "Bluetooth not connected");
			return;
		}
		try {
			bluetoothConnection.transmitString(scriptArg);
			// TODO listen to rover for acknowledgment before declaring a
			// success
			log(LOG, "Send Suceeded");
		} catch (IOException e) {
			e.printStackTrace();
			log(LOG_ERROR, "Transmission Failure");
		} catch (Exception e) {
			e.printStackTrace();
			log(LOG_ERROR, "Bluetooth Connection Failure");
		}
	}

	/**
	 * Use to write data to the rover log
	 * 
	 * @param log_type
	 *            int indicating type of log message. LOG and LOG_ERROR
	 * @param string
	 *            String value to write to log
	 */
	private void log(int log_type, String string) {
		Long longTime = System.currentTimeMillis() / 1000;
		String time = longTime.toString();
		roverLog.add(time + " " + log_type + ": " + string);
	}

}