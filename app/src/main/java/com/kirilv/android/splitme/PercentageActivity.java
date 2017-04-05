package com.kirilv.android.splitme;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kirilv.android.splitme.model.Section;
import com.kirilv.android.splitme.model.User;

public class PercentageActivity extends SwipeActivity {

	private ListView listView;
	private GridView gridView;
	private PercentageAdapter adapter;
	private ApplicationHolder application;
	private Section section;
	private List<User> list;
	
	private Menu menu;
	private int totalLockedValues = 0;
	private int numberOfLockedUsers = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_percentage);
		getActionBar().setDisplayHomeAsUpEnabled(true);		

		application = ApplicationHolder.getInstance();

		String requestCode = getIntent().getExtras().getString("requestCode");
		section = application.getSectionsMap().get(Integer.valueOf(requestCode));
		
		setTitle(section.getName());

		gridView = (GridView) findViewById(R.id.gridview);
		listView = (ListView) findViewById(R.id.listview);
		gridView.setOnTouchListener(swipeDetector);
		listView.setOnTouchListener(swipeDetector);
		
		list = section.getUserList();
		
		for(User u : list) {
			if(u.isLocked()) {
				totalLockedValues += u.getPercentage();
				numberOfLockedUsers++;
			}
		}

		adapter = new PercentageAdapter(this);
		listView.setAdapter(adapter);
		gridView.setAdapter(adapter);
		if (application.isPercentageViewInListView()) {
			listView.setVisibility(android.view.View.VISIBLE);
			gridView.setVisibility(android.view.View.GONE);
		} else {
			listView.setVisibility(android.view.View.GONE);
			gridView.setVisibility(android.view.View.VISIBLE);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    // Respond to the action bar's Up/Home button
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		    case R.id.action_refresh:
		    	new AlertDialog.Builder(PercentageActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(getResources().getString(R.string.msg_reset))
				.setMessage(getResources().getString(R.string.msg_reset_percentages))
				.setPositiveButton(getResources().getString(R.string.btn_yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								section.calculateDefaults();
								totalLockedValues = 0;
								numberOfLockedUsers = 0;
								for(User u : list) {
									u.setLocked(false);
								}
								invalidateViews();
							}
						})
				.setNegativeButton(getResources().getString(R.string.btn_no), null)
				.show();
		        return true;
		    case R.id.action_switch:
		    	application.setPercentageViewInListView(!application.isPercentageViewInListView());
		    	fixSwitchMenuItem();
				invalidateViews();
		    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_percentage, menu);
		this.menu = menu;
		fixSwitchMenuItem();
		return true;
	}

	private void fixSwitchMenuItem() {
		MenuItem menuItem = menu.findItem(R.id.action_switch);
		if (application.isPercentageViewInListView()) {
			menuItem.setIcon(R.drawable.ic_action_view_as_grid);
		} else {
			menuItem.setIcon(R.drawable.ic_action_view_as_list);
		}
	}

	private void invalidateViews() {
		if (application.isPercentageViewInListView()) {
			listView.invalidateViews();
			gridView.setVisibility(android.view.View.GONE);
			listView.setVisibility(android.view.View.VISIBLE);
		} else {
			gridView.invalidateViews();
			listView.setVisibility(android.view.View.GONE);
			gridView.setVisibility(android.view.View.VISIBLE);
		}
	}

	private void redrawPercentages() {
		if (application.isPercentageViewInListView()) {
			listView.invalidateViews();
		} else {
			gridView.invalidateViews();
		}
	}

	private class PercentageAdapter extends ArrayAdapter<User> {

		private Context context;

		public PercentageAdapter(Context context) {
			super(context, R.layout.row_percentage);
			for (int i = 0; i < list.size(); i++) {
				add(list.get(i));
			}
			this.context = context;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.row_percentage, parent, false);

			final User user = list.get(position);
			
			final SeekBar seekbar = (SeekBar) rowView.findViewById(R.id.row_percentage_seekbar);
			seekbar.setProgress(user.getPercentage());
			
			final Drawable drawableLockedTop = getResources().getDrawable(R.drawable.ic_action_locked);
			final Drawable drawableUnlockedTop = getResources().getDrawable(R.drawable.ic_action_unlocked);
			
			final ToggleButton toggle = (ToggleButton) rowView.findViewById(R.id.toggleLock);
			toggle.setChecked(!user.isLocked());
			if(user.isLocked()) {
				toggle.setCompoundDrawablesWithIntrinsicBounds(null, drawableLockedTop , null, null);
			} else {				
				toggle.setCompoundDrawablesWithIntrinsicBounds(null, drawableUnlockedTop , null, null);
			}
			
			toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(!isChecked) {
						user.setLocked(true);
						toggle.setCompoundDrawablesWithIntrinsicBounds(null, drawableLockedTop , null, null);
						seekbar.setEnabled(false);
						totalLockedValues += user.getPercentage();
						numberOfLockedUsers++;
					} else {
						user.setLocked(false);
						toggle.setCompoundDrawablesWithIntrinsicBounds(null, drawableUnlockedTop , null, null);
						seekbar.setEnabled(true);
						totalLockedValues -= user.getPercentage();
						numberOfLockedUsers--;
					}
				}
			});

			TextView price = (TextView) rowView.findViewById(R.id.row_percentage_user_amount);
			float p = CalculationEngine.getPrice(user.getPercentage(), section.getAmount());
			if (p == 0f) {
				price.setTextColor(Color.parseColor("#90a5ab"));
			} else {
				price.setTextColor(Color.parseColor("#1cbae6"));
			}
			price.setText("" + p); 
			
			TextView sectionAmount = (TextView) rowView.findViewById(R.id.row_percentage_user_total_amount);
			sectionAmount.setText("/ " + section.getAmount());

			TextView userName = (TextView) rowView.findViewById(R.id.row_percentage_user_name);
			userName.setText(user.getName());

			final TextView seekBarValue = (TextView) rowView.findViewById(R.id.row_percentage_seekbar_value);
			seekBarValue.setText(((float) user.getPercentage() / CalculationEngine.PERCENTAGE_DIVIDER) + "%");

			if (list.size() == 1 || user.isLocked()) {
				seekbar.setEnabled(false);
			}
			seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					if (allowSeekbarChange()) {
						CalculationEngine.getInstance().recalculatePercentages(section, position, seekBar.getProgress());
						redrawPercentages();						
					} else {
						seekBar.setProgress(user.getPercentage());
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					if (!allowSeekbarChange()) {
						seekBar.setProgress(user.getPercentage());
					}
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					if (allowSeekbarChange()) {
						final int maxProgressAllowed = CalculationEngine.PERCENTAGE_BASE - totalLockedValues;
						if (progress > maxProgressAllowed) {
							seekBar.setProgress(maxProgressAllowed);
							float value = (((float) maxProgressAllowed) / CalculationEngine.PERCENTAGE_DIVIDER);
							seekBarValue.setText(String.valueOf(value) + "%");
						} else {
							float value = (((float) progress) / CalculationEngine.PERCENTAGE_DIVIDER);
							seekBarValue.setText(String.valueOf(value) + "%");
						}
					} else {
						seekBar.setProgress(user.getPercentage()); 
					}
				}
				
				protected boolean allowSeekbarChange() {
					// the user should not be locked and the minimum number of unlocked users should be > 1
					return !user.isLocked() && numberOfLockedUsers < list.size() - 1;
				}
			});

			return rowView;
		}
	}

	@Override
	public void getNextActivity() {
	}
}
