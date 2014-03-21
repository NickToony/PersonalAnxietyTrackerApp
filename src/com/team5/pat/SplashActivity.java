package com.team5.pat;

import com.team5.pat.R;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/** Splash screen shown before entering the application **/
public class SplashActivity extends Activity implements OnClickListener {
	// Normal constants
	private final int NUM_BUTTONS = 11;
	private final int PASS_LENTH = 4;
	private final long SPLASH_DURATION = 1500; // 1.5 second
	private final Handler mHandler = new Handler();
	private final String FAIL_TEXT = "Incorrect key. Try again.";
	private final int[] KEYPAD_BUTTON = { R.id.button1, R.id.button2,
			R.id.button3, R.id.button4, R.id.button5, R.id.button6,
			R.id.button7, R.id.button8, R.id.button9, R.id.button0,
			R.id.buttonDelete };
	private final Runnable runnableWithPassword = new Runnable() {
		@Override
		public void run() {
			showTextAndButtons();
		}
	};

	// Coordinates for animation
	private final float FROM_X = 5;
	private final float TO_X = 5;
	private final float FROM_Y = 200;
	private final float TO_Y = 20;

	// Variables
	private String currentInput;
	private String input = "";
	private String inputLast = input;
	private SharedPreferences preference;
	private TranslateAnimation animation;

	// Views
	private ProgressBar mProgressBar;
	private ImageView mLogo;
	private TextView mText;
	private TextView mInformation;
	private Button[] mButtons = new Button[NUM_BUTTONS];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		initialiseComponentsAndSetUpAnimation();

		if (preference.getBoolean("pref_key_lockscreen", true) == true) {
			setUpKeyPadButtons();
			mLogo.setAnimation(animation);
			mHandler.postDelayed(runnableWithPassword, SPLASH_DURATION);
		} else {
			mLogo.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
			Runnable runnable = new Runnable() {
				public void run() {
					Intent intent = new Intent(SplashActivity.this,
							HomeActivity.class);
					startActivity(intent);
					finish();
				}
			};

			mHandler.postDelayed(runnable, SPLASH_DURATION);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		currentInput = (String) ((Button) v).getText();

		// When the user presses "DEL" button after typing the keypad, delete
		// the previous character
		if (currentInput.equals("DEL")) {
			if (input.length() > 0) {
				input = input.substring(0, input.length() - 1);
			}
		} else if (input.length() < PASS_LENTH) {
			input += currentInput;
		}
		handleInputPinNumber();
	}

	private void showTextAndButtons() {
		mLogo.setVisibility(View.VISIBLE);
		mInformation.setVisibility(View.VISIBLE);
		mText.setVisibility(View.VISIBLE);

		for (int i = 0; i < NUM_BUTTONS; i++) {
			mButtons[i].setVisibility(View.VISIBLE);
		}
	}

	private void handleInputPinNumber() {
		if (!input.contentEquals(inputLast)) {
			if (currentInput.equals("DEL")) {
				deletePreviousCharacter();
			} // Start this application if input matches
			else if (input.length() == PASS_LENTH) {
				String correctPin = preference
						.getString("pref_key_pin", "1234");
				if (input.contentEquals(correctPin)) {
					mText.setText(null);
					Intent intent = new Intent(this, HomeActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), FAIL_TEXT,
							Toast.LENGTH_SHORT).show();
					mText.setText(null);
				}
				input = "";
			} else {
				mText.setText(mText.getText() + "*");
			}
			inputLast = input;
		}
	}

	private void initialiseComponentsAndSetUpAnimation() {
		getActionBar().hide();

		animation = new TranslateAnimation(FROM_X, TO_X, FROM_Y, TO_Y);
		animation.setDuration(SPLASH_DURATION);
		animation.setFillAfter(true);

		mProgressBar = (ProgressBar) findViewById(R.id.splashScreenProgressBar);
		mLogo = (ImageView) findViewById(R.id.logo);
		mText = (TextView) findViewById(R.id.keypadInput);
		mInformation = (TextView) findViewById(R.id.keypadInformation);
		preference = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
	}

	private void setUpKeyPadButtons() {
		int pos = 0;
		for (int id : KEYPAD_BUTTON) {
			mButtons[pos] = (Button) findViewById(id);
			mButtons[pos].setText(getResources().getStringArray(
					R.array.button_array)[pos]);
			mButtons[pos].setOnClickListener(this);
			pos++;
		}
	}

	private void deletePreviousCharacter() {
		String before = (String) mText.getText();
		String after = before.substring(0, input.length());
		mText.setText((CharSequence) after);
	}
}