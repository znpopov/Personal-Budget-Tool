package com.kirilv.android.splitme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by user on 11.05.17.
 */

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button home;

    private Button budget;

    private Button categories;

    private Button transactions;

    private RelativeLayout rootLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.rootLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_categories, null);
        setContentView(this.rootLayout);
        this.show();

        home = (Button) findViewById(R.id.homeBtn);
        home.setOnClickListener(this);

        budget = (Button) findViewById(R.id.budgetBtn);
        budget.setOnClickListener(this);

        categories = (Button) findViewById(R.id.categoriesBtn);
        categories.setOnClickListener(this);

        transactions = (Button) findViewById(R.id.transactionBtn);
        transactions.setOnClickListener(this);

        findViewById(R.id.addButton).setOnClickListener(this);
    }

    @Override
    public final void onClick(final View v) {
        switch(v.getId()) {
            case R.id.homeBtn:
                startActivity(new Intent(CategoryActivity.this, MainActivity.class));
                break;
            case R.id.budgetBtn:
                startActivity(new Intent(CategoryActivity.this, MainActivity.class));
                break;
            case R.id.categoriesBtn:
                break;
            case R.id.transactionBtn:
                break;
            case R.id.addButton:
                break;
            default: break;
        }
    }

    public final void hide() {
        for (int i = this.rootLayout.getChildCount();  i-- > 0; ) {
            if (this.rootLayout.getChildAt(i).getId() != R.id.navigation)
                this.rootLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    public final void show() {
        for (int i = this.rootLayout.getChildCount();  i-- > 0; ) {
            if (this.rootLayout.getChildAt(i).getId() != R.id.navigation)
                this.rootLayout.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

}
