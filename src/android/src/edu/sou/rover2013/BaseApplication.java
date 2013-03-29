package edu.sou.rover2013;

import edu.sou.rover2013.utility.BluetoothService;
import android.app.Application;

/**
 * Extends application class, allowing for singletons to persist across all
 * activities. Without being set here, singletons are discarded by
 * garbage collection.
 * 
 * @author Ryan Dempsey
 * 
 */

public class BaseApplication extends Application {

	// Calling Bluetooth singleton here to prevent garbage collection
	@SuppressWarnings("unused")
	private BluetoothService wirelessConnection = BluetoothService
			.getConnection();

}