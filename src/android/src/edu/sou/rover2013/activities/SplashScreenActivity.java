package edu.sou.rover2013.activities;

import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import android.os.Bundle;

/**
 * Initial Activity with menu options and instructions on connecting rover
 * 
 * @author Ryan Dempsey
 */
// TODO Add Instructions
// TODO Improve theme
public class SplashScreenActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
	}

}