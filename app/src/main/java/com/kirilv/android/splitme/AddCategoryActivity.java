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

public class AddCategoryActivity extends Activity implements View.OnClickListener {

    private EditText name;

    private EditText categoryDescription;

    private Spinner categoriesType;

    private Button cancel;
    private Button save;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_category);

        name = (EditText) findViewById(R.id.categoryName);

        categoryDescription = (EditText) findViewById(R.id.categoryDescription);


        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        String[] types = {"Expenses", "Incomes"};

        categoriesType = (Spinner) findViewById(R.id.categoriesType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, types);
        categoriesType.setAdapter(adapter);

    }

    @Override
    public final void onClick(final View v) {
        switch (v.getId()) {
            case R.id.save:

                int categoryType = 1;
                if (categoriesType.getSelectedItem().toString() == "Incomes") {
                    categoryType = 2;
                }
                Category category = new Category(name.getText().toString(), categoryDescription.getText().toString(), categoryType);
                BudgetApplication.getInstance().getCategoryDBHelper().insertCategory(category);

                startActivity(new Intent(AddCategoryActivity.this, MainActivity.class));
                break;
            case R.id.cancel:
                startActivity(new Intent(AddCategoryActivity.this, CategoryActivity.class));
                break;
            default:
                break;
        }
    }

}
