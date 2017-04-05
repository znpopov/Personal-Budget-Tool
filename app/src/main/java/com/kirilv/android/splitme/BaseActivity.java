package com.kirilv.android.splitme;

import java.lang.reflect.Field;
import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appfireworks.android.track.AppTracker;
import com.dbtuukyjbsp.AdController;
import com.kirilv.android.splitme.SwipeDetector.ISwipe;

public abstract class BaseActivity extends SwipeActivity implements ISwipe {

	protected ListView listView;
	protected SeekBar seekBar;
	protected TextView seekBarTitle;
	protected TextView seekBarValue;	
	protected int previousSeekBarValue = 0;
	protected int objectsToRemove = 0;
	
	private static final boolean IS_DEBUG_MODE = true;
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// ACTIVATE TEST MODE IN:  https://portal.leadbolt.com/publisher/app 
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	
	// IS_DEBUG_MODE = TRUE =>  We are referencing the 'Splitme-Test' - DEV Application
	// IS_DEBUG_MODE = FALSE => We are referencing the 'Splitme' Production Application
	
	private static final long AD_TIMER = IS_DEBUG_MODE ? 90 * 1000 : 240 * 1000;	// 240 seconds = 4min.
	
	private AdController ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* On some devices the menu does not display properly. This is a hack. */
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}
		
		String[] advertismentIds = application.getAdvertismentIds();
		
		int low = 0;
		int high = advertismentIds.length;
		Random r = new Random();
		int idx = r.nextInt(high - low) + low;	// random number between 0 and (advertismentIds.length - 1)
		
		long currentTime = System.currentTimeMillis();
		if(currentTime - application.getAdLastAccessed() >=  AD_TIMER) {
			application.setAdLastAccessed(currentTime);
			if(IS_DEBUG_MODE) {
				ad = new AdController(this, "624695203");
			} else {
				ad = new AdController(this, advertismentIds[idx]);
			}
			ad.loadAd();
		}
		
		String fireworksSessionKey = IS_DEBUG_MODE ? "4mgQilCggSydC65MyaKIs4DM7Kbg3VuF" : "lrb6QzWmsimZQ0Zq03nVltzvEkpEdjbV";
		AppTracker.startSession(this, fireworksSessionKey);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_main, menu);
		MenuItem itemOverflow = menu.findItem(R.id.overflow);
		itemOverflow.setVisible(isAdvancedMenuVisible());
		return true;
	}
	
	protected boolean isAdvancedMenuVisible() {
		return false;
	}
	
	protected void fillEmptyNames() {
	}
	
	protected void removeAllNames() {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			cancelToast();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_fill:
			fillEmptyNames();
			return true;
		case R.id.action_delete_all:
			removeAllNames();
			return true;	
		case R.id.action_next:
			cancelToast();
			getNextActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onPause() {
		super.onPause(); 
		if(ad != null) { 
			ad.destroyAd(); 
		}
		if(!isFinishing()) { 
			AppTracker.pause(getApplicationContext()); 
		} 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		AppTracker.resume(getApplicationContext());
	}
	
	@Override 
	public void onDestroy() {
		super.onDestroy();
		if(ad != null) {
			ad.destroyAd();
		}
		AppTracker.closeSession(getApplicationContext(), true);
	}
}
