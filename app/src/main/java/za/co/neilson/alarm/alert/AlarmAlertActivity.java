/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package za.co.neilson.alarm.alert;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import za.co.neilson.alarm.Alarm;


public class AlarmAlertActivity extends Activity implements OnClickListener {

	private Alarm alarm;
	private MediaPlayer mediaPlayer;
	private static final String check_intake_med_url = "http://192.168.43.216/test/check_intake_med.php";
	private StringBuilder answerBuilder = new StringBuilder();

	//private MathProblem mathProblem;
	private Vibrator vibrator;

	private boolean alarmActive;

	private TextView problemView;
	private TextView answerView;
	private String answerString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.alarm_alert);

		Bundle bundle = this.getIntent().getExtras();
		alarm = (Alarm) bundle.getSerializable("alarm");

		this.setTitle(alarm.getAlarmName());

		/*switch (alarm.getDifficulty()) {
		case EASY:
			mathProblem = new MathProblem(3);
			break;
		case MEDIUM:
			mathProblem = new MathProblem(4);
			break;
		case HARD:
			mathProblem = new MathProblem(5);
			break;
		}
		*/

		answerString = "1";

		problemView = (TextView) findViewById(R.id.textView1);
		problemView.setText("take the medication??");

		answerView = (TextView) findViewById(R.id.textView2);
		answerView.setText("=");

		//((Button) findViewById(R.id.Button0)).setOnClickListener(this);
		((Button) findViewById(R.id.Button1)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button2)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button3)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button4)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button5)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button6)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button7)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button8)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button9)).setOnClickListener(this);
		((Button) findViewById(R.id.Button_clear)).setOnClickListener(this);
		((Button) findViewById(R.id.Button_decimal)).setOnClickListener(this);
		//((Button) findViewById(R.id.Button_minus)).setOnClickListener(this);

		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		PhoneStateListener phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:
					Log.d(getClass().getSimpleName(), "Incoming call: "
							+ incomingNumber);
					try {
						mediaPlayer.pause();
					} catch (IllegalStateException e) {

					}
					break;
				case TelephonyManager.CALL_STATE_IDLE:
					Log.d(getClass().getSimpleName(), "Call State Idle");
					try {
						mediaPlayer.start();
					} catch (IllegalStateException e) {

					}
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}
		};

		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		// Toast.makeText(this, answerString, Toast.LENGTH_LONG).show();

		startAlarm();

	}

	@Override
	protected void onResume() {
		super.onResume();
		alarmActive = true;
	}

	private void startAlarm() {

		if (alarm.getAlarmTonePath() != "") {
			mediaPlayer = new MediaPlayer();
			if (alarm.getVibrate()) {
				vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				long[] pattern = { 1000, 200, 200, 200 };
				vibrator.vibrate(pattern, 0);
			}
			try {
				mediaPlayer.setVolume(1.0f, 1.0f);
				mediaPlayer.setDataSource(this,
						Uri.parse(alarm.getAlarmTonePath()));
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mediaPlayer.setLooping(true);
				mediaPlayer.prepare();
				mediaPlayer.start();

			} catch (Exception e) {
				mediaPlayer.release();
				alarmActive = false;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (!alarmActive)
			super.onBackPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		StaticWakeLock.lockOff(this);
	}

	@Override
	protected void onDestroy() {
		try {
			if (vibrator != null)
				vibrator.cancel();
		} catch (Exception e) {

		}
		try {
			mediaPlayer.stop();
		} catch (Exception e) {

		}
		try {
			mediaPlayer.release();
		} catch (Exception e) {

		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (!alarmActive)
			return;
		String button = (String) v.getTag();
		v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		if (button.equalsIgnoreCase("clear")) {
			if (answerBuilder.length() > 0) {
				answerBuilder.setLength(answerBuilder.length() - 1);
				answerView.setText(answerBuilder.toString());
			}
		} else if (button.equalsIgnoreCase(".")) {
			if (!answerBuilder.toString().contains(button)) {
				if (answerBuilder.length() == 0)
					answerBuilder.append(0);
				answerBuilder.append(button);
				answerView.setText(answerBuilder.toString());
			}
		} else if (button.equalsIgnoreCase("-")) {
			if (answerBuilder.length() == 0) {
				answerBuilder.append(button);
				answerView.setText(answerBuilder.toString());
			}
		} else {
			answerBuilder.append(button);
			answerView.setText(answerBuilder.toString());
			if (isAnswerCorrect()) {

				alarmActive = false;
				if (vibrator != null)
					vibrator.cancel();
				try {
					mediaPlayer.stop();
				} catch (IllegalStateException ise) {

				}
				try {
					mediaPlayer.release();
				} catch (Exception e) {

				}

				this.finish();

			}
		}
		if (answerView.getText().length() >= answerString.length()
				&& !isAnswerCorrect()) {
			answerView.setTextColor(Color.RED);
		} else {
			answerView.setTextColor(Color.BLACK);
		}
	}

	public boolean isAnswerCorrect() {
		boolean correct = false;
		try {
			correct = 1 == Float.parseFloat(answerBuilder
					.toString());
		} catch (NumberFormatException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(correct==true)
		{
			final String uid = "P001";
			//final String image=p.getThumbnailUrl();

			StringRequest postRequest = new StringRequest(Request.Method.POST, check_intake_med_url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							// response
							Log.d("Response", response.toString());
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// error
							//Log.d("Error.Response", response);
						}
					}
			) {
				@Override
				protected Map<String, String> getParams() {
					HashMap<String, String> jsonParams = new HashMap<String, String>();
					//String imagepost = image.substring(image.lastIndexOf('/')+1,image.length());
					jsonParams.put("uid", uid);
					Log.d("uid", uid);

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
					String date = formatter.format(curDate);
					jsonParams.put("date",date);
					Log.d("date",date);
					formatter = new SimpleDateFormat("HH:mm:ss");
					curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
					String time = formatter.format(curDate);
					jsonParams.put("time",time);
					Log.d("time",time);

					return jsonParams;
				}
			};

			AppController.getInstance().addToRequestQueue(postRequest);
		}

		return correct;
	}

}
