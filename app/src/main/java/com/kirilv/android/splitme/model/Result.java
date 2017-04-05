package com.kirilv.android.splitme.model;

public class Result {
	
	private String name;
	private float amount;
	private int index;

	public Result() {
	}
		
	public Result(String name, float amount, int index) {	
		this.name = name;
		this.amount = amount;
		this.index = index;
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
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
