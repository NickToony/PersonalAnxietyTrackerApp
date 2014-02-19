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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LockScreenActivity extends Activity implements OnClickListener {
	// Constants
	private int stage = 0;
	private final int NUM_BUTTONS = 11;
	private final int KEYPAD_SIZE = 300;
	private final int KEYPAD_MARGIN = 100;
	private final int MOVE_SPEED = 5;
	private final int WAIT_TIME = 60; // 1 second
	private final int PASS_LENTH = 4;
	private final String DEFAULT_TEXT = "Enter Passkey";
	private final String FAIL_TEXT = "Incorrect key. Try again.";
	private final String SUCCESS_TEXT = "Success";
	private final int[] KEYPAD_BUTTON = { R.id.button1, R.id.button2,
			R.id.button3, R.id.button4, R.id.button5, R.id.button6,
			R.id.button7, R.id.button8, R.id.button9, R.id.button0,
			R.id.buttonDelete };

	// Variables
	private int position = 0;
	private String currentInput;
	private String input = "";
	private String inputLast = input;
	private SharedPreferences preference;

	// Views
	private ImageView mLogo;
	private TextView mText;
	private Button[] mButtons = new Button[NUM_BUTTONS];

	// Tick event
	private Handler mHandler = new Handler();
	private Runnable mTick = new Runnable() {
		public void run() {
			tickEvent();
			mHandler.postDelayed(this, 20); // 60fps
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);
		getActionBar().hide();
		mLogo = (ImageView) findViewById(R.id.logo);
		mText = (TextView) findViewById(R.id.keypadInput);

		preference = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (preference.getBoolean("pref_key_lockscreen", true) == true) {
			setUpKeyPadButtons();

			// Reset the handler
			mHandler.removeCallbacks(mTick);
			// Give it the task
			mHandler.post(mTick);
		} else {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
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
	}

	private void tickEvent() {
		switch (stage) {
		case 0:
			if (position >= WAIT_TIME) {
				stage++;
				position = 0;
			} else {
				position++;
			}
			break;
		case 1:
			if (position >= (KEYPAD_SIZE + KEYPAD_MARGIN) / 2) {
				stage++;
				position = 0;
				for (int i = 0; i < 9; i++) {
					mButtons[i].setVisibility(View.VISIBLE);
					mText.setVisibility(View.VISIBLE);
				}
			} else {
				position += MOVE_SPEED;
				mLogo.setTranslationY(-position);
			}
			break;

		case 2: // awaiting input
			if (!input.contentEquals(inputLast)) {
				clearTextIfUserTypeAgain();

				// The user has not yet typed a word
				if (input.length() == 0) {
					mText.setText(DEFAULT_TEXT);
				} else if (currentInput.equals("DEL")) {
					deletePreviousCharacter();
				} // Start this application if input matches
				else if (input.length() >= PASS_LENTH) {
					String correctPin = preference.getString("pref_key_pin",
							"1234");
					if (input.contentEquals(correctPin)) {
						mText.setText(SUCCESS_TEXT);
						Intent intent = new Intent(this, HomeActivity.class);
						startActivity(intent);
						finish();
					} else {
						mText.setText(FAIL_TEXT);
					}
					input = "";
				} else {
					mText.setText(mText.getText() + "*");
				}
				inputLast = input;
			}
			break;

		}
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

	private void clearTextIfUserTypeAgain() {
		if ((mText.getText().equals(DEFAULT_TEXT))
				|| (mText.getText().equals(FAIL_TEXT))
				|| (mText.getText().equals(SUCCESS_TEXT))) {
			mText.setText(null);
		}
	}

	private void deletePreviousCharacter() {
		String before = (String) mText.getText();
		String after = before.substring(0, input.length());
		mText.setText((CharSequence) after);
	}
}