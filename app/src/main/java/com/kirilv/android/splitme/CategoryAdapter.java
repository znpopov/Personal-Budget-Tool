package com.kirilv.android.splitme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kirilv.android.splitme.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoriesViewHolder> {

    private ArrayList<Category> categories;
    private static final int MAX_PROGRESSBAR_VALUE = 100;

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ProgressBar progressBar;

        public CategoriesViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public CategoryAdapter(ArrayList<Category> data) {
        categories = new ArrayList<>(data);
    }

    @Override
    public CategoryAdapter.CategoriesViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        return new CategoriesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, final int position) {
        Category category = categories.get(position);
        Double categoryAmount = 0.0;
        if (BudgetApplication.getInstance().getCategoryAmounts().get(category.getId()) != null) {
            categoryAmount = BudgetApplication.getInstance().getCategoryAmounts().get(category.getId());
        }
        holder.mTextView.setText(category.getName());
        holder.progressBar.setMax(category.getBudget());
        holder.progressBar.setProgress(categoryAmount.intValue());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               TODO here we call the input form for a new Category where we can update an existing one
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}