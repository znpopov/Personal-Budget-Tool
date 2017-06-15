package com.kirilv.android.splitme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.kirilv.android.splitme.model.Category;

import java.util.ArrayList;

/**
 * Created by zdravkop on 6/15/2017.
 */

public class BudgetActivity extends Activity {

    private RecyclerView mRecyclerView;
    private BudgetAdapter budgetAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_budget);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        ArrayList<Category> categoriesList = new ArrayList<>(BudgetApplication.getInstance().getCategories().values());

        budgetAdapter = new BudgetAdapter(categoriesList);
        mRecyclerView.setAdapter(budgetAdapter);
        /*Button addCategoryBtn = (Button) findViewById(R.id.addCategoryBtn);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                startActivity(new Intent(BudgetActivity.this, AddCategoryActivity.class));

            }
        });*/
    }
}
