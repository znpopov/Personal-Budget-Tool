package com.kirilv.android.splitme;

import android.app.Application;
import android.util.Log;

import com.kirilv.android.splitme.model.Category;
import com.kirilv.android.splitme.model.CategoryDBHelper;
import com.kirilv.android.splitme.model.Transaction;
import com.kirilv.android.splitme.model.TransactionDBHelper;

import java.util.HashMap;

public class BudgetApplication extends Application {
    private static BudgetApplication ourInstance;

    private HashMap<Long, Category> categories;
    private HashMap<Long, Transaction> transactions;
    private CategoryDBHelper categoryDBHelper;
    private TransactionDBHelper transactionDBHelper;
    private HashMap<Long, Double> categoryAmounts;

    public static BudgetApplication getInstance() {
        return ourInstance;
    }

    public HashMap<Long, Category> getCategories() {
        return categories;
    }


    public HashMap<Long, Transaction> getTransactions() {
        return transactions;
    }

    public HashMap<Long, Double> getCategoryAmounts() {
        return categoryAmounts;
    }

    public void updateCategory(Category category) {
        categoryDBHelper.updateCategory(category);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        categoryDBHelper = new CategoryDBHelper(this);
        transactionDBHelper = new TransactionDBHelper(this);

        categories = new HashMap<>();
        transactions = new HashMap<>();
        categoryAmounts = new HashMap<>();

        for (Category category : categoryDBHelper.getAllCategories()) {
            categories.put(category.getId(), category);
        }

        for (Transaction transaction : transactionDBHelper.getAllTransaction()) {
            transactions.put(transaction.getId(), transaction);
            long categoryId = transaction.getCategoryId();
            if (categoryAmounts.get(categoryId) != null) {
                double value = categoryAmounts.get(categoryId);
                value += transaction.getAmount();
                categoryAmounts.put(categoryId, value);
            } else {
                categoryAmounts.put(categoryId, transaction.getAmount());
            }
        }
    }

    public CategoryDBHelper getCategoryDBHelper() {
        return categoryDBHelper;
    }

    public TransactionDBHelper getTransactionDBHelper() {
        return transactionDBHelper;
    }
}
