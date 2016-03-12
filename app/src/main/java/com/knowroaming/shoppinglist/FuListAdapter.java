package com.knowroaming.shoppinglist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.sql.SQLDataException;
import java.util.ArrayList;

/**
 * Created by Daddy on 2016-03-11.
 */

public class FuListAdapter extends ArrayAdapter<UserItem> {

    private ArrayList<UserItem> list = new ArrayList<UserItem>();
    private Context context;
    private int layoutResourceId;


    public FuListAdapter(Context context, int layoutResourceId, ArrayList<UserItem> list) {
        super(context, layoutResourceId, list);
        this.list = list;
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final UserItem user = getItem(position);
        final int pos = position;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.layoutResourceId, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.userNameList);
        listItemText.setText(user.getName());

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.btnRemoveUserList);
        if(deleteBtn != null) {
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    list.remove(pos);

                    removeUser(user.getId());
                    notifyDataSetChanged();
                }
            });
        }


        return view;
    }

    private void removeUser(int id) {
        String dbName = "login.db";
        JSONObject tableInfo = TableInfo.getInstance().getTableInfo("user");

        String sql = String.format("delete from user where id=%d;", id);
        Log.d("TEST", sql);
        HJDatasource hjdb = new HJDatasource(getContext(), dbName, tableInfo);
        if(hjdb != null) {
            try {
                hjdb.open();

                hjdb.executeSql(sql);


            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }


    }
}