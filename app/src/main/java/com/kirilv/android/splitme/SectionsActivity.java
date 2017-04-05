package com.kirilv.android.splitme;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.kirilv.android.splitme.model.Section;
import com.kirilv.android.splitme.model.User;

public class SectionsActivity extends BaseActivity {

	private SectionArrayAdapter adapter;
	private SparseArray<Section> sectionsMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sections);
		setTitle(getResources().getString(R.string.lbl_title_sections)
				.toString());
		getActionBar().setDisplayHomeAsUpEnabled(true);

		sectionsMap = application.getSectionsMap();

		previousSeekBarValue = application.getSectionsMap().size();

		if (application.isChanged()) {
			if (application.getCompletedSteps() > 1) {
				new AlertDialog.Builder(SectionsActivity.this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(getResources().getString(R.string.msg_reset))
						.setCancelable(false)
						.setMessage(
								getResources().getString(
										R.string.msg_reset_members_changes))
						.setPositiveButton(
								getResources().getString(R.string.btn_ok), null)
						.show();
			}
			/* We now have to restore the default users' state in all sections. */
			int size = sectionsMap.size();
			for (int i = 0; i < size; i++) {
				Section section = sectionsMap.get(i);
				section.setUserList(getUsersList());
				section.calculateDefaults();
			}
		}

		application.setCompletedSteps(2);
		application.setChanged(false);

		seekBar = (SeekBar) findViewById(R.id.seekbar);
		if (previousSeekBarValue > 0) {
			seekBar.setProgress(previousSeekBarValue);
			seekBar.setSecondaryProgress(previousSeekBarValue);
		}
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Integer currentSeekBarValue = seekBar.getProgress();
				previousSeekBarValue = sectionsMap.size();
				seekBar.setSecondaryProgress(previousSeekBarValue);
				// always reset the currently selected users for delete
				objectsToRemove = 0;
				/*
				 * We do not want to enter this code if the current map size is
				 * the same the newly entered number.
				 */
				if (currentSeekBarValue > previousSeekBarValue) {
					/* We already have entered some names. */
					if (previousSeekBarValue > 0) {
						/* The new value is bigger than the map. */
						int newSections = currentSeekBarValue
								- previousSeekBarValue;
						for (int idx = 0; idx < newSections; idx++) {
							sectionsMap.put(previousSeekBarValue + idx,
									new Section("", 0.0f, getUsersList())
											.calculateDefaults());
						}
					} else {
						/*
						 * We enter here only the first time a sectionsCount is
						 * entered.
						 */
						for (int idx = 0; idx < currentSeekBarValue; idx++) {
							sectionsMap.put(idx, new Section("", 0.0f,
									getUsersList()).calculateDefaults());
						}
					}
				} else {
					int toRemove = previousSeekBarValue - currentSeekBarValue;
					SparseArray<Section> map = new SparseArray<Section>();
					int index = 0;
					// remove the empty names first
					for (int i = 0; i < sectionsMap.size(); i++) {
						Section user = sectionsMap.get(i);
						if ("".equals(user.getName().trim())) {
							if (toRemove > 0) {
								toRemove--;
							} else {
								map.put(index, user);
								index++;
							}
						} else {
							map.put(index, user);
							index++;
						}
					}
					application.setSectionsMap(map);
					sectionsMap = application.getSectionsMap();
					// if there are some non-empty names left
					objectsToRemove = toRemove;
					if (objectsToRemove > 0) {
						if (objectsToRemove == 1) {
							showToast(getResources().getString(
									R.string.msg_select_remove_section)
									.toString());
						} else {
							showToast(getResources()
									.getString(
											R.string.msg_select_remove_sections)
									.toString()
									.replaceAll("###",
											String.valueOf(objectsToRemove)));
						}
					}
				}
				seekBarTitle.setText(getResources().getString(
						R.string.lbl_bills).toString()
						+ " " + sectionsMap.size());
				adapter = new SectionArrayAdapter(SectionsActivity.this);
				listView.setAdapter(adapter);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				seekBarValue.setText(String.valueOf(progress));
			}
		});

		seekBarTitle = (TextView) findViewById(R.id.seekbar_title);
		seekBarTitle.setText(getResources().getString(R.string.lbl_bills)
				.toString() + " " + sectionsMap.size());

		seekBarValue = (TextView) findViewById(R.id.seekbar_value);
		seekBarValue.setText(String.valueOf(seekBar.getProgress()));

		listView = (ListView) findViewById(R.id.listview);
		adapter = new SectionArrayAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnTouchListener(swipeDetector);
	}

	@Override
	public void getNextActivity() {
		if (sectionsMap.size() == 0) {
			showToast(getResources().getString(R.string.msg_select_section).toString());
		} else {
			boolean emptyName = false;
			boolean equalNames = false;
			float amount = 0f;
			for (int i = 0; i < sectionsMap.size(); i++) {
				Section si = sectionsMap.get(i);
				amount += si.getAmount();
				if ("".equals(si.getName().trim())) {
					emptyName = true;
					break;
				}
				for (int j = 0; j < sectionsMap.size(); j++) {
					if (i != j) {
						Section sj = sectionsMap.get(j);
						if (si.getName().trim().equals(sj.getName().trim())) {
							equalNames = true;
						}
					}
				}
			}
			if (emptyName) {
				showToast(getResources().getString(R.string.msg_select_section_names).toString());
			} else if (equalNames) {
				showToast(getResources().getString(R.string.msg_select_section_unique_names).toString());
			} else if (amount == 0f) {
				showToast(getResources().getString(R.string.msg_select_section_amount).toString());
			} else {
				Intent intent = new Intent(SectionsActivity.this,SummaryActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}
	}

	private List<User> getUsersList() {
		SparseArray<User> usersMap = application.getUsersMap();
		List<User> usersList = new ArrayList<User>();
		for (int i = 0; i < usersMap.size(); i++) {
			User u = usersMap.get(i);
			usersList.add(new User(u.getName(), u.getIndex()));
		}
		return usersList;
	}

	private void removeSection(int position) {
		SparseArray<Section> map = new SparseArray<Section>();
		int index = 0;
		for (int i = 0; i < sectionsMap.size(); i++) {
			if (i != position) {
				map.put(index, sectionsMap.get(i));
				index++;
			}
		}
		application.setSectionsMap(map);
		sectionsMap = application.getSectionsMap();
		adapter = new SectionArrayAdapter(this);
		listView.setAdapter(adapter);
		listView.setSelection(position);
		seekBar.setSecondaryProgress(sectionsMap.size());
		seekBarTitle.setText(getResources().getString(R.string.lbl_bills).toString() + " " + sectionsMap.size());
		objectsToRemove--;
	}

	@Override
	protected void onActivityResult(int request_code, int result_code, Intent i) {
		if (result_code == RESULT_OK) {
			listView.setSelection(request_code);
		}
		super.onActivityResult(request_code, result_code, i);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("requestCode", String.valueOf(requestCode));
		super.startActivityForResult(intent, requestCode);
	}

	private class SectionArrayAdapter extends ArrayAdapter<Section> {

		private Context context;

		public SectionArrayAdapter(Context context) {
			super(context, R.layout.row_section);
			for (int i = 0; i < sectionsMap.size(); i++) {
				add(sectionsMap.get(i));
			}
			this.context = context;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.row_section, parent,
					false);

			final Section section = sectionsMap.get(position);

			final ImageView imageView = (ImageView) rowView
					.findViewById(R.id.row_section_image_delete);
			if (objectsToRemove > 0) {
				imageView.setVisibility(android.view.View.VISIBLE);
				imageView.setImageResource(R.drawable.ic_delete);
			} else {
				imageView.setVisibility(android.view.View.GONE);
			}

			imageView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (objectsToRemove > 0) {
						if (MotionEvent.ACTION_CANCEL == event.getAction()) {
							rowView.setBackgroundResource(R.drawable.bg_row_section);
							imageView.setImageResource(R.drawable.ic_delete);
						} else {
							rowView.setBackgroundColor(Color.DKGRAY);
							imageView.setImageResource(R.drawable.ic_delete_active);
						}
					}
					return false;
				}
			});

			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (objectsToRemove > 0) {
						removeSection(position);
					}
				}
			});

			final Button btnEdit = (Button) rowView.findViewById(R.id.row_section_section_btn_edit);
			btnEdit.setEnabled(section.getName().length() > 0);
			btnEdit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					editSection(position);
				}
			});

			final EditText sectionNameEdit = (EditText) rowView.findViewById(R.id.row_section_section_name);
			sectionNameEdit.setOnTouchListener(swipeDetector);
			sectionNameEdit.setText(section.getName());
			sectionNameEdit.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start, int before, int count) {
					Section sec = sectionsMap.get(position);
					if (s != null) {
						String name = s.toString();
						sec.setName(name);
						sectionsMap.append(position, sec);
						btnEdit.setEnabled(name.length() > 0);
					}
				}
			});

			final EditText sectionAmountEdit = (EditText) rowView.findViewById(R.id.row_section_section_amount);
			sectionAmountEdit.setOnTouchListener(swipeDetector);
			if (section.getAmount() != 0) {
				sectionAmountEdit.setText("" + section.getAmount());
			}
			sectionAmountEdit.setOnEditorActionListener(new OnEditorActionListener() {

						@Override
						public boolean onEditorAction(TextView v, int actionId,
								KeyEvent event) {
							boolean handled = false;
							if (actionId == EditorInfo.IME_ACTION_GO) {
								editSection(position);
								handled = true;
							}
							return handled;
						}
					});
			sectionAmountEdit.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					Section sec = sectionsMap.get(position);
					if (s != null) {
						String text = s.toString();
						float amount = 0.0f;
						try {
							if ("".equals(text.trim())) {
								amount = 0.0f;
							} else {
								amount = Float.valueOf(text);
							}
						} catch (NumberFormatException e) {
							amount = 0.0f;
						}
						sec.setAmount(amount);
						sectionsMap.append(position, sec);
					}
				}
			});

			return rowView;
		}

		private void editSection(int position) {
			Intent intent = new Intent(SectionsActivity.this, PercentageActivity.class);
			startActivityForResult(intent, position);
		}
	}
}
