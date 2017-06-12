package com.kirilv.android.splitme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kirilv.android.splitme.model.Category;
import com.kirilv.android.splitme.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends Activity implements View.OnClickListener {

    private Integer transactionType;

    private EditText name;

    private EditText value;

    private Button addExpense;

    private Button cancel;

    private Spinner category;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction);

        name = (EditText) findViewById(R.id.transactionName);
        value = (EditText) findViewById(R.id.transactionValue);

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
                //insertTransaction

                Transaction transaction = new Transaction(
                        name.getText().toString(),
                        BudgetApplication.getInstance().getCategoryDBHelper().getCategory(category.getSelectedItem().toString()).getId(),
                        transactionType,
                        new Double(value.getText().toString())
                );
                BudgetApplication.getInstance().getTransactionDBHelper().insertTransaction(transaction);
                ArrayList<Transaction> transactions = BudgetApplication.getInstance().getTransactionDBHelper().getAllTransaction();
                for (Transaction tempTransaction : transactions) {
                    Log.d("CREATION", "All transactions: [" + tempTransaction.getId() + "]:" + tempTransaction.getName() + " = " + tempTransaction.getAmount());
                }
                startActivity(new Intent(TransactionActivity.this, MainActivity.class));
                break;
            case R.id.cancelBtn:
                Log.d("CREATION", "Cancel " + getIntent().getExtras().getString("type"));
                startActivity(new Intent(TransactionActivity.this, MainActivity.class));
                break;
            default:
                break;
        }
        ;
    }

}
