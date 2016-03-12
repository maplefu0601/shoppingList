package com.knowroaming.shoppinglist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLDataException;
import java.util.ArrayList;

/**
 * @author Raymond Fu
 * @date    2016-03-11
 */
public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        JSONArray data = getUserList();
        showUserList(data);
    }

    /**
     * showUserList simplely show user list
     * @param data
     */
    private void showUserList(JSONArray data) {

        if(data == null) {
            return;
        }
        ArrayList<UserItem> userData = new ArrayList<UserItem>();
        for(int i=0;i<data.length();++i) {
            try {
                JSONObject obj = data.getJSONObject(i);
                String name = obj.getString("USERNAME");
                int id = obj.getInt("ID");
                userData.add(new UserItem(id, name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("TEST", userData.toString());
        ArrayAdapter adapter = new FuListAdapter(this, R.layout.user_list_item, userData);

        ListView listView = (ListView) findViewById(R.id.listUsers);
        listView.setAdapter(adapter);
    }

    /**
     * getuserList  get all users name
     * @return array of users
     */
    private JSONArray getUserList() {

        String dbName = "login.db";

        JSONObject tableInfo = TableInfo.getInstance().getTableInfo("user");
        String sql = String.format("select ID,USERNAME, createdDate, status from user;");
        Log.d("TEST", sql);
        HJDatasource hjdb = new HJDatasource(this, dbName, tableInfo);
        if(hjdb != null) {
            try {
                hjdb.open();

                JSONArray data = hjdb.query(sql);

                return data;


            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

}
