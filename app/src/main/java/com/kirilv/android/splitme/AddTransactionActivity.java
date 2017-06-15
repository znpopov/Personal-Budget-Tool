package com.kirilv.android.splitme;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.kirilv.android.splitme.model.Category;
import com.kirilv.android.splitme.model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AddTransactionActivity extends FragmentActivity implements View.OnClickListener {

    private Integer transactionType;
    private EditText name;
    private EditText value;
    public EditText datePicker;
    private Button addExpense;
    private Button cancel;
    private Spinner category;
    static final int DATE_DIALOG_ID = 0;
    private int mYear, mMonth, mDay;
    public Date transactionDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_transaction);

        name = (EditText) findViewById(R.id.transactionName);
        value = (EditText) findViewById(R.id.transactionValue);
        datePicker = (EditText) findViewById(R.id.datePicker);


        datePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });


        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        addExpense = (Button) findViewById(R.id.saveBtn);
        addExpense.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(this);

        Bundle b = getIntent().getExtras();

        Log.d("CREATION", "Transaction type: " + b.getString("type"));

        if (b.getString("type").equals("expense")) {
            transactionType = 1;
        } else if (b.getString("type").equals("income")) {
            transactionType = 2;
        } else {
            //throw new Exception("No Transaction Type");
        }

        Category[] categories = BudgetApplication.getInstance().getCategories().values().toArray(new Category[0]);


        List<String> categoryList = new ArrayList<String>();


        for (Category category : categories) {

            if (category.getType() == transactionType) {
                categoryList.add(category.getName());
            }

        }

        category = (Spinner) findViewById(R.id.categories_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryList.toArray(new String[categoryList.size()]));
        category.setAdapter(adapter);

    }


    @Override
    public final void onClick(final View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                Log.d("CREATION", "Add Expense {name:" + name.getText() + " value:" + value.getText() + " category:" + category.getSelectedItem().toString() + "}");


                Transaction transaction = new Transaction(
                        name.getText().toString(),
                        BudgetApplication.getInstance().getCategoryDBHelper().getCategory(category.getSelectedItem().toString()).getId(),
                        transactionType,
                        new Double(value.getText().toString()), transactionDate);

                BudgetApplication.getInstance().getTransactionDBHelper().insertTransaction(transaction);
                ArrayList<Transaction> transactions = BudgetApplication.getInstance().getTransactionDBHelper().getAllTransaction();
                for (Transaction tempTransaction : transactions) {
                    Log.d("CREATION", "All transactions: [" + tempTransaction.getId() + "]:" + tempTransaction.getName() + " = " + tempTransaction.getAmount());
                }
                startActivity(new Intent(AddTransactionActivity.this, MainActivity.class));
                break;
            case R.id.cancelBtn:
                Log.d("CREATION", "Cancel " + getIntent().getExtras().getString("type"));
                startActivity(new Intent(AddTransactionActivity.this, MainActivity.class));
                break;
            default:
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            transactionDate = new GregorianCalendar(mYear, mMonth, mDay).getTime();

            datePicker.setText(new StringBuilder().append(mDay).append("/").append(mMonth + 1).append("/").append(mYear));
        }
    };
}
