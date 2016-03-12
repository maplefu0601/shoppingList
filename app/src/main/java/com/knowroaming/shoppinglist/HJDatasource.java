package com.knowroaming.shoppinglist;

/**
 * Created by Daddy on 2016-03-11.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLDataException;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Raymond FuXing on 11/03/2016.
 */
public class HJDatasource {

    private SQLiteDatabase db;
    private HjDbHelper dbHelper;
    private String dbName = "login.db";
    private String tableName = "";
    private JSONObject tableInfo = null;
    private String TAG = "HJDatabase";

    public HJDatasource(Context context, String dbName, JSONObject tableInfo)
    {
        try {
            this.tableInfo = tableInfo;
            this.tableName = tableInfo.getString("TableName");
            dbHelper = new HjDbHelper(context, dbName, tableInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void open() throws SQLDataException
    {
        db = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public void insert(JSONObject object)
    {
        try {

            if(db == null) {
                try {
                    this.open();
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
            ContentValues values = new ContentValues();
            Iterator<String> it = object.keys();
            while(it.hasNext()) {
                String key = it.next();
                Object value = object.get(key);

                values.put(key, (String)value);
            }

            db.insert(tableName, null, values);
            Log.i(TAG, "insert record into " + tableName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void update(String where, JSONObject object)
    {

        try {

            if(db == null) {
                try {
                    this.open();
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
            ContentValues values = new ContentValues();
            Iterator<String> it = object.keys();
            while(it.hasNext()) {
                String key = it.next();
                Object value = object.get(key);

                values.put(key, (String)value);
            }

            db.update(tableName, values, where, null);
            Log.i(TAG, "update record for " + tableName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer id)
    {
        try {
            if(db == null) {
                try {
                    this.open();
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
            JSONObject columns = tableInfo.getJSONObject("Columns");
            String strId = columns.getString("id");
            if(db != null) {
                db.delete(tableName, strId+"=?", new String[] {Integer.toString(id)});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll()
    {
        if(db == null) {
            try {
                this.open();
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        if(db != null) {
            db.execSQL("delete from "+tableName);
        }
    }

    /**
     * execute only select statement
     * @param sql
     * @return JSON Object
     */
    public JSONArray query(String sql)
    {
        sql = sql.toLowerCase();
        JSONArray buffer=new JSONArray();
        try {
            if(db == null) {
                this.open();
            }
        } catch (SQLDataException e) {
            e.printStackTrace();
        }

        Cursor c=db.rawQuery(sql, null);
        if(c.getCount()==0)
        {
            return buffer;
        }
        try {
            String[] columns = getColumns(sql);
            Boolean bAllColumns = false;
            if(sql.indexOf("* ") != -1) {
                bAllColumns = true;
            }

            while(c.moveToNext())
            {
                int i = 0;
                JSONObject row = new JSONObject();
                if(bAllColumns) {
                    while (i < c.getColumnCount()) {

                        String key = c.getColumnName(i);
                        Object value = c.getString(i);

                        row.put(key, value);

                        i++;
                    }
                } else {
                    while (i < columns.length) {

                        String key = c.getColumnName(i);
                        Object value = c.getString(i);

                        row.put(key, value);

                        i++;
                    }

                }
                buffer.put(row);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, buffer.toString());
        return buffer;
    }

    /**
     * Execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE
     * @param sql
     * @return none
     * @author Raymond FuXing
     */
    public void executeSql(String sql)
    {
        try {
            if(db == null) {
                this.open();
            }
        } catch (SQLDataException e) {
            e.printStackTrace();
        }

        db.execSQL(sql);

    }

    private String[] getColumns(String sql)
    {
        String[] ret;
        sql = sql.toLowerCase();
        if(sql.substring(0, 6).equalsIgnoreCase("select")) {
            String str = sql.substring(6, sql.indexOf("from"));
            ret = str.split("\\s*,\\s*");

            return ret;
        }

        return null;
    }
}
