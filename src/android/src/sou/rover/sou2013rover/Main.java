package sou.rover.sou2013rover;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


//Initial Activity -- Will be main screen, with menu options and instructions on connecting rover.
public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//called when activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles menu item selection
        switch (item.getItemId()) {
            case R.id.bluetooth_settings:
                //TODO Open Bluetooth Activity
            	Log.d("test", "Open Bluetooth Activity");
            	//Return true if successfully handled
                return true;
            case R.id.telemetry_settings:
                //TODO Open Console & Sensor Activity
            	Log.d("test", "Open Console & Sensor Activity");
            	return true;
            case R.id.control_mode_settings:
            	//TODO Open Mode Selection Menu
            	Log.d("test", "Open Mode Selection Menu");
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
}
