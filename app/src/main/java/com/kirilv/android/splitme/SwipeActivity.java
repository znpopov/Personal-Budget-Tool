package com.kirilv.android.splitme;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kirilv.android.splitme.SwipeDetector.Action;
import com.kirilv.android.splitme.SwipeDetector.ISwipe;

public abstract class SwipeActivity extends Activity implements ISwipe {

	protected ApplicationHolder application;
	protected SwipeDetector swipeDetector;
	protected Toast toast;
	private Handler handler;
	private Runnable exec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = ApplicationHolder.getInstance();
		handler = new Handler();
		swipeDetector = new SwipeDetector(this, this);
		exec = new Runnable() {

			@Override
			public void run() {
				Action action = swipeDetector.getAction();
				if (Action.LR.equals(action)) {
					if (NavUtils.getParentActivityIntent(SwipeActivity.this) != null) {
						cancelToast();
						NavUtils.navigateUpFromSameTask(SwipeActivity.this);
					}
				} else if (Action.RL.equals(action)) {
					cancelToast();
					getNextActivity();
				}
			}
		};
	}

	protected void showToast(String message) {
		cancelToast();
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.toast_text);
		text.setText(message); 

		toast = Toast.makeText(getApplicationContext(),
				R.string.msg_select_member_names, Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	@Override
	public void onSwipe() {
		handler.removeCallbacks(exec);
		handler.postDelayed(exec, 200);
	}

	protected void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}

	public abstract void getNextActivity();
}
