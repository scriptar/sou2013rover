package edu.sou.rover2013;

import edu.sou.rover2013.models.Rover;
import edu.sou.rover2013.utility.BluetoothService;
import android.app.Application;

/**
 *  Extends application class, allowing for values/items to exist across all actions.
 *  To access, use "connection = ((RogoApplication) getBluetoothConnection());"
 *
 * @author Ryan Dempsey
 *
 */

public class BaseApplication extends Application {

	private BluetoothService wirelessConnection = null;
	private Rover rogoRover = null;

	/**
	 * Returns current wireless connection, instantiates if does not exist.
	 */
	// TODO Move into Bluetooth Service as a Singleton
	public BluetoothService getWirelessConnection() {
		if (wirelessConnection == null) {
			wirelessConnection = new BluetoothService();
		}
		return wirelessConnection;
	}

	/**
	 * Returns current rover object.
	 */
	// TODO Move into Rover Class as Singleton
	public Rover getRogoRover() {
		return rogoRover;
	}
}
