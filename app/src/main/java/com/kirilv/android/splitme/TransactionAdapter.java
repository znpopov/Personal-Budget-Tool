package com.kirilv.android.splitme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirilv.android.splitme.model.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionsViewHolder> {

    private ArrayList<Transaction> transactions;

    public static class TransactionsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public TransactionsViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
        }
    }

    public TransactionAdapter(ArrayList<Transaction> data) {
        transactions = new ArrayList<>(data);
    }

    @Override
    public TransactionAdapter.TransactionsViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        return new TransactionsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TransactionsViewHolder holder, final int position) {
        Transaction transaction = BudgetApplication.getInstance().getTransactions().get(Long.valueOf(position));
        if (transaction == null) {
            return;
        }

        holder.mTextView.setText(transaction.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//               TODO here we call the input form for a new Transaction where we can update an existing one
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}