package com.kirilv.android.splitme;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.ProgressBar;



public class MainActivity extends Activity implements View.OnClickListener {

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

		findViewById(R.id.categoriesBtn).setOnClickListener(this);
	}

	@Override
	public final void onClick(final View v) {
		switch(v.getId()) {
			case R.id.expenseBtn:
				break;
			case R.id.incomeBtn:
				break;
			case R.id.categoriesBtn:
				setTheme();
				startActivity(new Intent(MainActivity.this, CategoryActivity.class));
				break;
			case R.id.transactionBtn:
				break;
			case R.id.addButton:
				break;
			default: break;
		};
	}
}
