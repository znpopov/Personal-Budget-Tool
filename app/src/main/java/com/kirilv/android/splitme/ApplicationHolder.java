package com.kirilv.android.splitme;

import android.app.Application;
import android.util.SparseArray;

import com.kirilv.android.splitme.model.Section;
import com.kirilv.android.splitme.model.User;

public class ApplicationHolder extends Application {

	private static final ApplicationHolder instance = new ApplicationHolder();
	// Test commit
	private boolean changed = true;
	private boolean percentageViewInListView = true;
	private int completedSteps = 1;

	/* Application holder of the users. */
	private SparseArray<User> usersMap = new SparseArray<User>();
	/* Application holder of the sections. */
	private SparseArray<Section> sectionsMap = new SparseArray<Section>();
	
	private String[] advertismentIds = new String[] {
		"313779658",	// Offer Wall (Green)
		"706562375",	// Offer Wall (Blue)
		"623281713", 	// Interstitial (Auto Stretching)
		"289728111",	// Alert (Green) 
		"947480765"		// Alert (Blue) 
		
	};
	
	private long adLastAccessed = 0;

	private ApplicationHolder() {
	}

	public static ApplicationHolder getInstance() {
		return instance;
	}

	public SparseArray<User> getUsersMap() {
		return usersMap;
	}

	public SparseArray<Section> getSectionsMap() {
		return sectionsMap;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean isPercentageViewInListView() {
		return percentageViewInListView;
	}

	public void setPercentageViewInListView(boolean percentageViewInListView) {
		this.percentageViewInListView = percentageViewInListView;
	}

	public void setUsersMap(SparseArray<User> usersMap) {
		this.usersMap = usersMap;
	}

	public void setSectionsMap(SparseArray<Section> sectionsMap) {
		this.sectionsMap = sectionsMap;
	}

	public int getCompletedSteps() {
		return completedSteps;
	}

	public void setCompletedSteps(int completedSteps) {
		this.completedSteps = completedSteps;
	}
	
	public String[] getAdvertismentIds() {
		return advertismentIds;
	}

	public long getAdLastAccessed() {
		return adLastAccessed;
	}

	public void setAdLastAccessed(long adLastAccessed) {
		this.adLastAccessed = adLastAccessed;
	}
}
