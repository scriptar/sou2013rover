package edu.sou.rover2013.utility;

import android.os.Environment;

public class TextStorage {

	public static boolean isStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			return true;
		} else {
			return false;
		}
	}

	public static boolean isStorageWritable() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			return true;
		}
		// We cannot read/write to the media
		return false;
	}
}
