package edu.sou.rover2013;

import android.app.Application;

/**
 * Extends the Android application class, allowing for singletons to persist
 * across all activities.
 */

public class BaseApplication extends Application {

	// Using an alternate solution, should be able to return to base App class.
	// // Calling Bluetooth singleton here to prevent garbage collection
	// // Connection will persist until app closes completely
	// private BluetoothService wirelessConnection = BluetoothService
	// .getConnection();

}