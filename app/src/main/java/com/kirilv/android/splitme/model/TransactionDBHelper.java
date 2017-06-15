package com.kirilv.android.splitme.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kirilv.android.splitme.BudgetApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TransactionDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Transactions.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TRANSACTION_TABLE_NAME = "Transactions";
    private static final String TRANSACTION_COLUMN_ID = "_id";
    private static final String TRANSACTION_COLUMN_NAME_ID = "name";
    private static final String TRANSACTION_COLUMN_CATEGORY_ID = "categoryId";
    private static final String TRANSACTION_COLUMN_TYPE_ID = "typeId";
    private static final String TRANSACTION_COLUMN_AMOUNT = "amount";
    private static final String TRANSACTION_COLUMN_DATE = "date";
    private static final String TRANSACTION_COLUMN_CREATED_AT = "created_at";

    private Context context;


    public TransactionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TRANSACTION_TABLE_NAME + "(" +
                TRANSACTION_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                TRANSACTION_COLUMN_NAME_ID + " TEXT, " +
                TRANSACTION_COLUMN_CATEGORY_ID + " INTEGER, " +
                TRANSACTION_COLUMN_TYPE_ID + " INTEGER, " +
                TRANSACTION_COLUMN_AMOUNT + " DOUBLE, " +
                TRANSACTION_COLUMN_DATE + " Long, " +
                TRANSACTION_COLUMN_CREATED_AT + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTransaction(Transaction transaction) {

        SQLiteDatabase db = getWritableDatabase();
        insertTransaction(transaction, db);
        db.close();
        BudgetApplication app = BudgetApplication.getInstance();
        HashMap<Long, Transaction> transactions = app.getTransactions();
        HashMap<Long, Double> categoryAmounts = app.getCategoryAmounts();
        transactions.put(Long.valueOf(transactions.size() + 1), transaction);

        long categoryId = transaction.getCategoryId();
        if (categoryAmounts.get(categoryId) != null) {
            double value = categoryAmounts.get(categoryId);
            value += transaction.getAmount();
            categoryAmounts.put(categoryId, value);
        } else {
            categoryAmounts.put(categoryId, transaction.getAmount());
        }

        BudgetApplication.getInstance().getCategoryAmounts().get(transaction.getCategoryId());
        return true;
    }

    private boolean insertTransaction(Transaction transaction, SQLiteDatabase db) {
        db.insert(TRANSACTION_TABLE_NAME, null, setContentValues(transaction));
        return true;
    }

    boolean updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TRANSACTION_TABLE_NAME, setContentValues(transaction), TRANSACTION_COLUMN_ID + " = ? ", new
                String[]{Long.toString(transaction.getId())});
        db.close();
        return true;
    }

    public Transaction getTransaction(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " WHERE " +
                TRANSACTION_COLUMN_CREATED_AT + " = " + name;
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        db.close();
        return initializeTransaction(c);
    }

    public ArrayList<Transaction> getAllTransaction() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TRANSACTION_TABLE_NAME, null);
        if (c != null) {
            while (c.moveToNext()) {
                transactions.add(initializeTransaction(c));
            }
        }
        db.close();
        return transactions;
    }

    public Integer deleteTransaction(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TRANSACTION_TABLE_NAME,
                TRANSACTION_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    private Transaction initializeTransaction(Cursor c) {
        int id = c.getInt(c.getColumnIndex(TRANSACTION_COLUMN_ID));
        String name = c.getString(c.getColumnIndex(TRANSACTION_COLUMN_NAME_ID));
        long categoryId = c.getLong(c.getColumnIndex(TRANSACTION_COLUMN_CATEGORY_ID));
        int typeId = c.getInt(c.getColumnIndex(TRANSACTION_COLUMN_TYPE_ID));
        double amount = c.getDouble(c.getColumnIndex(TRANSACTION_COLUMN_AMOUNT));
        long date = c.getLong(c.getColumnIndex(TRANSACTION_COLUMN_DATE));
        long createdAt = c.getLong(c.getColumnIndex(TRANSACTION_COLUMN_CREATED_AT));
        return new Transaction(id, name, categoryId, typeId, amount);
    }

    private ContentValues setContentValues(Transaction transaction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_COLUMN_NAME_ID, transaction.getName());
        contentValues.put(TRANSACTION_COLUMN_CATEGORY_ID, transaction.getCategoryId());
        contentValues.put(TRANSACTION_COLUMN_TYPE_ID, transaction.getTypeId());
        contentValues.put(TRANSACTION_COLUMN_AMOUNT, transaction.getAmount());
        contentValues.put(TRANSACTION_COLUMN_DATE, transaction.getDate().getTime());
        contentValues.put(TRANSACTION_COLUMN_CREATED_AT, transaction.getCreatedAt());
        return contentValues;
    }
}
