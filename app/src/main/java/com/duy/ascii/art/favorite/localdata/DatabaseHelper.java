/*
 *     Copyright (C) 2018 Tran Le Duy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.duy.ascii.art.favorite.localdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.duy.ascii.art.favorite.localdata.DatabaseHelper.TextEntry.COLUMN_CONTENT;
import static com.duy.ascii.art.favorite.localdata.DatabaseHelper.TextEntry.COLUMN_TIME;
import static com.duy.ascii.art.favorite.localdata.DatabaseHelper.TextEntry.TABLE_NAME;


/**
 * Created by Duy on 09-Jul-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_TIME + " LONG PRIMARY KEY, " +
                    COLUMN_CONTENT + " TEXT)";

    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDatabase.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean insert(TextItem textItem) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TIME, textItem.getTime());
            contentValues.put(COLUMN_CONTENT, textItem.getText());
            SQLiteDatabase writableDatabase = this.getWritableDatabase();
            writableDatabase.insert(TABLE_NAME, null, contentValues);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<TextItem> getAll() {
        ArrayList<TextItem> list = new ArrayList<>();
        String[] projection = new String[]{COLUMN_TIME, COLUMN_CONTENT};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                long time = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                list.add(new TextItem(time, content));
            } catch (Exception ignored) {
            }
        }
        cursor.close();
        Collections.sort(list, new Comparator<TextItem>() {
            @Override
            public int compare(TextItem textItem, TextItem t1) {
                return Long.valueOf(t1.getTime()).compareTo(textItem.getTime());
            }
        });
        return list;
    }

    public void delete(TextItem item) {
        delete(item.getTime());
    }

    public int delete(long time) {
        try {
            String selection = COLUMN_TIME + " LIKE ?";
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_NAME, selection, new String[]{time + ""});
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static class TextEntry {
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_CONTENT = "content";
        public static final String TABLE_NAME = "tbl_custom";
    }
}
