package com.kirilv.android.splitme;


//import android.app.Activity;

import android.app.Activity;
import android.app.DialogFragment;
//import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.ProgressBar;


public class MainActivity extends Activity implements View.OnClickListener, AddExpenseDialogFragment.AddExpenseDialogListener {

    private Button home;

    private Button budget;

    private Button categories;

    private Button transactions;

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
        addIncome.setOnClickListener(this);
        addExpense = (Button) findViewById(R.id.expenseBtn);
        addExpense.setOnClickListener(this);

        home = (Button) findViewById(R.id.homeBtn);
        home.setOnClickListener(this);

        budget = (Button) findViewById(R.id.budgetBtn);
        budget.setOnClickListener(this);

        categories = (Button) findViewById(R.id.categoriesBtn);
        categories.setOnClickListener(this);

        transactions = (Button) findViewById(R.id.transactionBtn);
        transactions.setOnClickListener(this);
    }

    public void showExpenceDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddExpenseDialogFragment();
        dialog.show(getFragmentManager(), "Expense dialog");
    }


    @Override
    public final void onClick(final View v) {
        Intent transactionIntent = new Intent(MainActivity.this, AddTransactionActivity.class);
        Bundle b = new Bundle();
        switch (v.getId()) {
            case R.id.homeBtn:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.expenseBtn:
                b.putString("type", "expense");
                transactionIntent.putExtras(b);
                startActivity(transactionIntent);
                break;
            case R.id.incomeBtn:
                b.putString("type", "income");
                transactionIntent.putExtras(b);
                startActivity(transactionIntent);
                break;
            case R.id.budgetBtn:
                startActivity(new Intent(MainActivity.this, BudgetActivity.class));
                break;
            case R.id.categoriesBtn:
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                break;
            case R.id.transactionBtn:
                startActivity(new Intent(MainActivity.this, AllTransactionsActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onExpenseDialogPositiveClick(DialogFragment dialog) {
        Log.d("CREATION", "Add Expense");

    }

    @Override
    public void onExpenseDialogNegativeClick(DialogFragment dialog) {
        Log.d("CREATION", "Cancel Expense");
    }
}