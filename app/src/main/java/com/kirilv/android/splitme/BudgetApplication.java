package com.kirilv.android.splitme;

import android.app.Application;
import android.util.Log;

import com.kirilv.android.splitme.model.Category;
import com.kirilv.android.splitme.model.CategoryDBHelper;
import com.kirilv.android.splitme.model.Transaction;
import com.kirilv.android.splitme.model.TransactionDBHelper;

import java.util.HashMap;

public class BudgetApplication extends Application
{
    private static BudgetApplication ourInstance;

    private HashMap<Long, Category> categories;
    private HashMap<Long, Transaction> transactions;
    private CategoryDBHelper categoryDBHelper;
    private TransactionDBHelper transactionDBHelper;

    public static BudgetApplication getInstance()
    {
//        if(ourInstance == null){
//            ourInstance = new BudgetApplication();
//        }
        return ourInstance;
    }

    public HashMap<Long, Category> getCategories(){
        return categories;
    }

    public HashMap<Long, Transaction> getTransactions(){
        return transactions;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        categoryDBHelper = new CategoryDBHelper(this);
        transactionDBHelper = new TransactionDBHelper(this);

        categories = new HashMap<>();
        transactions = new HashMap<>();

        for(Category category : categoryDBHelper.getAllCategories()){
            categories.put(category.getId(), category);
        }

        for(Transaction transaction : transactionDBHelper.getAllTransaction()){
            transactions.put(transaction.getId(), transaction);
        }

        int transactionSize = transactions.size();
    }
}
