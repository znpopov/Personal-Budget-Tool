package com.kirilv.android.splitme;

import java.util.LinkedHashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kirilv.android.splitme.model.User;

public class MainActivity extends BaseActivity {

	private UserArrayAdapter adapter;
	private SparseArray<User> usersMap;
	private ArrayAdapter<String> suggestAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle(getResources().getString(R.string.lbl_title_members).toString());
		
		suggestAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);

		previousSeekBarValue = application.getUsersMap().size();

		seekBar = (SeekBar) findViewById(R.id.seekbar);
		if (previousSeekBarValue > 0) {
			seekBar.setProgress(previousSeekBarValue);
			seekBar.setSecondaryProgress(previousSeekBarValue);
		}

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Integer currentSeekBarValue = seekBar.getProgress();
				previousSeekBarValue = usersMap.size();
				seekBar.setSecondaryProgress(previousSeekBarValue);
				// always reset the currently selected users for delete
				objectsToRemove = 0;
				/*
				 * We do not want to enter this code if the current map size is
				 * the same the newly entered number.
				 */
				if (currentSeekBarValue > previousSeekBarValue) {
					application.setChanged(true);

					/* We already have entered some names. */
					if (previousSeekBarValue > 0) {
						/* The new value is bigger than the map. */
						int newUsers = currentSeekBarValue
								- previousSeekBarValue;
						/*
						 * Add the rest of the empty cells with the first
						 * default names.
						 */
						for (int idx = 0; idx < newUsers; idx++) {
							usersMap.put(previousSeekBarValue + idx, new User(
									"", previousSeekBarValue + idx));
						}
					} else {
						/*
						 * We have to set some default names. We enter here only
						 * the first time a userCount is entered.
						 */
						for (int idx = 0; idx < currentSeekBarValue; idx++) {
							usersMap.put(idx, new User("", idx));
						}
					}
				} else {
					int toRemove = previousSeekBarValue - currentSeekBarValue;
					SparseArray<User> map = new SparseArray<User>();
					int index = 0;
					// remove the empty names first
					for (int i = 0; i < usersMap.size(); i++) {
						User user = usersMap.get(i);
						if ("".equals(user.getName().trim())) {
							if (toRemove > 0) {
								toRemove--;
							} else {
								user.setIndex(index);
								map.put(index, user);
								index++;
							}
						} else {
							user.setIndex(index);
							map.put(index, user);
							index++;
						}
					}
					application.setUsersMap(map);
					usersMap = application.getUsersMap();
					// if there are some non-empty names left
					objectsToRemove = toRemove;
					if (toRemove > 0) {
						if (toRemove == 1) {
							showToast(getResources().getString(
									R.string.msg_select_remove_member)
									.toString());
						} else {
							showToast(getResources()
									.getString(
											R.string.msg_select_remove_members)
									.toString()
									.replaceAll("###",
											String.valueOf(objectsToRemove)));
						}
					}
				}
				seekBarTitle.setText(getResources().getString(
						R.string.lbl_users).toString()
						+ " " + usersMap.size());
				adapter = new UserArrayAdapter(MainActivity.this);
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

		/* Initialize the users list if we load this Activity again. */
		usersMap = application.getUsersMap();

		seekBarTitle = (TextView) findViewById(R.id.seekbar_title);
		seekBarTitle.setText(getResources().getString(R.string.lbl_users)
				.toString() + " " + usersMap.size());

		seekBarValue = (TextView) findViewById(R.id.seekbar_value);
		seekBarValue.setText(String.valueOf(seekBar.getProgress()));

		listView = (ListView) findViewById(R.id.listview);
		adapter = new UserArrayAdapter(MainActivity.this);
		listView.setAdapter(adapter);
		listView.setOnTouchListener(swipeDetector);
	}
	
	protected boolean isAdvancedMenuVisible() {
		return true;
	}
	
	protected void fillEmptyNames() {
		for (int i = 0; i < usersMap.size(); i++) {
			User u = usersMap.get(i);
			if ("".equals(u.getName().trim())) {
				u.setName(getResources().getString(R.string.hint_name) + " " + ( 1 + i));
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	protected void removeAllNames() {
		application.setUsersMap(new SparseArray<User>());
		usersMap = application.getUsersMap();
		adapter = new UserArrayAdapter(MainActivity.this);
		listView.setAdapter(adapter);
		previousSeekBarValue = 0;
		seekBar.setProgress(0);
		seekBar.setSecondaryProgress(0);
		application.setChanged(true);
		seekBarTitle.setText(getResources().getString(R.string.lbl_users).toString() + " " + usersMap.size());
		objectsToRemove = 0;
	}

	@Override
	public void getNextActivity() {
		boolean emptyName = false;
		for (int i = 0; i < usersMap.size(); i++) {
			User u = usersMap.get(i);
			if ("".equals(u.getName().trim())) {
				emptyName = true;
				break;
			}
		}
		if (emptyName) {
			showToast(getResources()
					.getString(R.string.msg_select_member_names).toString());
		} else if (usersMap.size() == 0) {
			showToast(getResources().getString(R.string.msg_select_member)
					.toString());
		} else {
			Intent intent = new Intent(MainActivity.this,
					SectionsActivity.class);
			startActivity(intent);
		}
	}

	private void removeUser(int position) {
		SparseArray<User> map = new SparseArray<User>();
		int index = 0;
		for (int i = 0; i < usersMap.size(); i++) {
			if (i != position) {
				User user = usersMap.get(i);
				user.setIndex(index);
				map.put(index, user);
				index++;
			}
		}
		application.setUsersMap(map);
		usersMap = application.getUsersMap();
		adapter = new UserArrayAdapter(MainActivity.this);
		listView.setAdapter(adapter);
		listView.setSelection(position);
		seekBar.setSecondaryProgress(usersMap.size());
		application.setChanged(true);
		seekBarTitle.setText(getResources().getString(R.string.lbl_users)
				.toString() + " " + usersMap.size());
		objectsToRemove--;
	}
	
	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					finish();
					System.exit(0);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				getResources().getString(R.string.message_exit_application))
				.setPositiveButton(getResources().getString(R.string.btn_yes), dialogClickListener)
				.setNegativeButton(getResources().getString(R.string.btn_no), dialogClickListener).show();
	}

	private class UserArrayAdapter extends ArrayAdapter<User> {

		private Context context;

		public UserArrayAdapter(Context context) {
			super(context, R.layout.row_user);
			for (int i = 0; i < usersMap.size(); i++) {
				add(usersMap.get(i));
			}
			this.context = context;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.row_user, parent, false);

			User user = usersMap.get(position);

			final ImageView imageView = (ImageView) rowView
					.findViewById(R.id.row_user_icon);
			if (objectsToRemove > 0) {
				imageView.setImageResource(R.drawable.ic_delete);
			} else {
				imageView.setImageResource(R.drawable.ic_action_person);
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
						removeUser(position);
					}
				}
			});

			final AutoCompleteTextView userEdit = (AutoCompleteTextView) rowView.findViewById(R.id.row_user_edit_user);
			userEdit.setOnTouchListener(swipeDetector);
			userEdit.setText(user.getName());
			userEdit.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start, int before, int count) {
					suggestAdapter.clear();
					User user = usersMap.get(position);
					if (s != null) {
						String text = s.toString();
						user.setName(text);
						usersMap.append(position, user);
						application.setChanged(true);
						try {
							Set<String> set = new LinkedHashSet<String>();
							String[] projection = new String[] {
					                ContactsContract.Contacts.DISPLAY_NAME
					        };
							String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
							Uri lkup = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, text);
							Cursor cursor = getContentResolver().query(lkup, projection, null, null, sortOrder);
							if (cursor != null && cursor.moveToFirst()) {
								do {
									String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
									set.add(name);
								} while (cursor.moveToNext());
							}
							suggestAdapter.addAll(set);
						} catch(Exception e) {
							Log.d("", e.getMessage());
						}
					}
				}
			});
			userEdit.setAdapter(suggestAdapter);

			Button btnClear = (Button) rowView.findViewById(R.id.row_user_btn_clear);
			btnClear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					userEdit.setText("");
				}
			});

			return rowView;
		}
	}
}
