package com.kirilv.android.splitme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kirilv.android.splitme.model.Category;

import java.util.ArrayList;

/**
 * Created by zdravkop on 6/15/2017.
 */

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.CategoriesViewHolder> {

    private ArrayList<Category> categories;
    private static final int MAX_PROGRESSBAR_VALUE = 100;

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public EditText budgetView;
        public Button saveCategoryBudget;

        public CategoriesViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.budgetCategoryName);
            budgetView = (EditText) v.findViewById(R.id.budgetCategoryValue);
            saveCategoryBudget = (Button) v.findViewById(R.id.saveCategoryBudgetBtn);
        }
    }

    public BudgetAdapter(ArrayList<Category> data) {
        categories = new ArrayList<>(data);
    }

    @Override
    public BudgetAdapter.CategoriesViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        return new BudgetAdapter.CategoriesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final BudgetAdapter.CategoriesViewHolder holder, final int position) {
        final Category category = categories.get(position);
        Double categoryAmount = 0.0;
//        if (BudgetApplication.getInstance().getCategoryAmounts().get(category.getId()) != null) {
//            categoryAmount = BudgetApplication.getInstance().getCategoryAmounts().get(category.getId());
//        }
        holder.mTextView.setText(category.getName());
        holder.budgetView.setText("" + category.getBudget());

        holder.saveCategoryBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int budgetValue = Integer.parseInt(holder.budgetView.getText().toString());
                category.setBudget(budgetValue);
                BudgetApplication.getInstance().updateCategory(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
