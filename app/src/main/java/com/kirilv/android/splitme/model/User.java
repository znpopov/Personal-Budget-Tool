package com.kirilv.android.splitme.model;

public class User implements Comparable<User> {

	private String name;
	private int percentage;
	private int index;
	private boolean locked = false;

	public User(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public String toString() {
		return "User [" + name + ", " + percentage + "%]";
	}

	@Override
	public int compareTo(User t) {
		int p1 = t.percentage;
		int p2 = this.percentage;
		if (p1 > p2) {
			return 1;
		} else if (p1 < p2) {
			return -1;
		} else {
			return 0;
		}
	}
}