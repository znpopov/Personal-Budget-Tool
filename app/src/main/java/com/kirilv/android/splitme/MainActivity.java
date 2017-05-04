package com.kirilv.android.splitme;


import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.ProgressBar;



public class MainActivity extends Activity {

	private ProgressBar overallBar;

	private ProgressBar dailyBar;

	private Button addIncome;

	private Button addExpense;

	@Override

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		overallBar = (ProgressBar) findViewById(R.id.overallBar);
		overallBar.setVisibility(View.VISIBLE);
		overallBar.setMax(100);
		overallBar.setProgress(50);

		dailyBar = (ProgressBar) findViewById(R.id.dailyBar);

		addIncome = (Button) findViewById(R.id.incomeBtn);

		addExpense = (Button) findViewById(R.id.expenseBtn);

	}

}
