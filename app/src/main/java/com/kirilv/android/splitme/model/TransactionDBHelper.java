package com.kirilv.android.splitme.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TransactionDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Transactions.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TRANSACTION_TABLE_NAME = "Transactions";
    private static final String TRANSACTION_COLUMN_ID = "_id";
    private static final String TRANSACTION_COLUMN_NAME_ID = "name";
    private static final String TRANSACTION_COLUMN_CATEGORY_ID = "categoryId";
    private static final String TRANSACTION_COLUMN_TYPE_ID = "typeId";
    private static final String TRANSACTION_COLUMN_AMOUNT = "amount";
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
        ArrayList<Transaction> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TRANSACTION_TABLE_NAME, null);
        if (c != null) {
            while (c.moveToNext()) {
                categories.add(initializeTransaction(c));
            }
        }
        db.close();
        return categories;
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
        int categoryId = c.getInt(c.getColumnIndex(TRANSACTION_COLUMN_CATEGORY_ID));
        int typeId = c.getInt(c.getColumnIndex(TRANSACTION_COLUMN_TYPE_ID));
        double amount = c.getDouble(c.getColumnIndex(TRANSACTION_COLUMN_AMOUNT));
        long createdAt = c.getLong(c.getColumnIndex(TRANSACTION_COLUMN_CREATED_AT));
        return new Transaction(id, name, categoryId, typeId, amount);
    }

    private ContentValues setContentValues(Transaction transaction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_COLUMN_NAME_ID, transaction.getName());
        contentValues.put(TRANSACTION_COLUMN_CATEGORY_ID, transaction.getCategoryId());
        contentValues.put(TRANSACTION_COLUMN_TYPE_ID, transaction.getTypeId());
        contentValues.put(TRANSACTION_COLUMN_AMOUNT, transaction.getAmount());
        contentValues.put(TRANSACTION_COLUMN_CREATED_AT, transaction.getCreatedAt());
        return contentValues;
    }
}
