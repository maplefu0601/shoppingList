package com.knowroaming.shoppinglist;

/**
 * Created by Daddy on 2016-03-11.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Raymond FuXing
 * JSONObject {"CreateDatabase":"create table if not exists test(id integer, name varchar);",
 *              "UpdateDatabase": "drop table if exists test;",
 *              "Columns" : {"colId":"id",       //column name id
 *                           "colName": "name"   //column name name
 *                          },
 *
 *
 * }
 *
 */
public class HjDbHelper extends SQLiteOpenHelper {

    private String TAG = "Database";
    private String mDbName = "";
    private JSONObject mColumns = null;
    private JSONObject mTableInfo = null;

    public HjDbHelper(Context context, String databaseName, JSONObject tableInfo) throws JSONException {
        super(context, databaseName, null, 1);
        mDbName = databaseName;
        mTableInfo = tableInfo;
        mColumns = tableInfo.getJSONObject("Columns");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        if(mTableInfo != null) {
            try {
                Log.i(TAG, mTableInfo.getString("CreateDatabase"));
                db.execSQL(mTableInfo.getString("CreateDatabase"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if(mTableInfo != null) {
            try {
                Log.i(TAG, mTableInfo.getString("CreateDatabase"));
                db.execSQL(mTableInfo.getString("CreateDatabase"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(mTableInfo != null) {
            try {
                db.execSQL(mTableInfo.getString("UpdateDatabase"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onCreate(db);
        }
    }

    public void getColumns()
    {
        Iterator<?> keys = mColumns.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            try {
                if ( mColumns.get(key) instanceof JSONObject ) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
