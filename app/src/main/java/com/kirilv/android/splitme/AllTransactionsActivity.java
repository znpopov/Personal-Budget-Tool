package com.kirilv.android.splitme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kirilv.android.splitme.model.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllTransactionsActivity extends Activity {
    private RecyclerView mRecyclerView;
    private TransactionAdapter transactionAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_categories);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ArrayList<Transaction> transactionsList = new ArrayList<>(BudgetApplication.getInstance().getTransactions().values());

        transactionAdapter = new TransactionAdapter(transactionsList);
        mRecyclerView.setAdapter(transactionAdapter);
    }

}
