package com.kirilv.android.splitme.model;

import java.util.List;

import com.kirilv.android.splitme.CalculationEngine;

public class Section {

	private String name;
	private float amount;
	private List<User> list;

	public Section(String name, float amount, List<User> list) {
		this.name = name;
		this.amount = amount;
		this.list = list;
	}

	public Section calculateDefaults() {
		CalculationEngine.getInstance().resetPercentages(list);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public List<User> getUserList() {
		return list;
	}

	public void setUserList(List<User> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		String s = "";
		for (User u : list) {
			s += u.toString() + "\n";
		}
		return "Section [" + name + ", " + amount + "] -> \n" + s;
	}
}
