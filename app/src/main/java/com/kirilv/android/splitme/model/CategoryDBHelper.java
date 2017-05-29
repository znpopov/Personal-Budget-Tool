package com.kirilv.android.splitme.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CategoryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Category.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CATEGORY_TABLE_NAME = "Category";
    private static final String CATEGORY_COLUMN_ID = "_id";
    private static final String CATEGORY_COLUMN_NAME = "name";
    private static final String CATEGORY_COLUMN_DESCRIPTION = "description";
    private static final String CATEGORY_COLUMN_TYPE_ID = "typeId";

    private Context context;


    public CategoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + "(" +
                        CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        CATEGORY_COLUMN_NAME + " TEXT, " +
                        CATEGORY_COLUMN_DESCRIPTION + " TEXT, " +
                        CATEGORY_COLUMN_TYPE_ID + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertCategory(Category category) {
        SQLiteDatabase db = getWritableDatabase();
        insertCategory(category, db);
        db.close();
        return true;
    }

    private boolean insertCategory(Category category, SQLiteDatabase db) {
        db.insert(CATEGORY_TABLE_NAME, null, setContentValues(category));
        return true;
    }

    boolean updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(CATEGORY_TABLE_NAME, setContentValues(category), CATEGORY_COLUMN_ID + " = ? ", new String[]{category.getId()});
        db.close();
        return true;
    }

    public Category getCategory(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " +
                CATEGORY_COLUMN_NAME + " = " + name;
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        db.close();
        return initializeCategory(c);
    }

    public boolean isCategoryExist(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " +
                CATEGORY_COLUMN_NAME + "=" + name;
        Cursor c = db.rawQuery(selectQuery, null);
        final int cursorSize = c.getCount();
        db.close();
        return cursorSize != 0;
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME, null);
        if (c != null) {
            while (c.moveToNext()) {
                categories.add(initializeCategory(c));
            }
        }
        db.close();
        return categories;
    }

    public Integer deleteCategory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CATEGORY_TABLE_NAME,
                CATEGORY_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    private Category initializeCategory(Cursor c) {
        String id = c.getString(c.getColumnIndex(CATEGORY_COLUMN_ID));
        String name = c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME));
        String description = c.getString(c.getColumnIndex(CATEGORY_COLUMN_DESCRIPTION));
        String typeId = c.getString(c.getColumnIndex(CATEGORY_COLUMN_TYPE_ID));
        return new Category(id, name, description, typeId);
    }

    private ContentValues setContentValues(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_ID, category.getId());
        contentValues.put(CATEGORY_COLUMN_NAME, category.getName());
        contentValues.put(CATEGORY_COLUMN_TYPE_ID, category.getType());
        contentValues.put(CATEGORY_COLUMN_DESCRIPTION, category.getDescription());
        return contentValues;
    }
}