package com.knowroaming.shoppinglist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLDataException;


/**
 * @author Raymond Fu
 * @date    2016-03-11
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static String userName;
    EditText edtProductName, edtAmount, edtPrice, edtDesc;

    private OnFragmentInteractionListener mListener;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingFragment newInstance(String param1, String param2) {
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        userName = bundle.getString("userName");
        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);
        TextView textUser = (TextView)rootView.findViewById(R.id.userId);
        textUser.setText(userName);

        edtProductName = (EditText)rootView.findViewById(R.id.productName);
        edtAmount = (EditText)rootView.findViewById(R.id.amount);
        edtPrice = (EditText)rootView.findViewById(R.id.price);
        edtDesc = (EditText)rootView.findViewById(R.id.description);

        Button btnClose = (Button)rootView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        Button btnAddList = (Button)rootView.findViewById(R.id.btnAddToList);
        btnAddList.setOnClickListener(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Log.d("TEST", "clicking...");
        switch(v.getId()) {

            case R.id.btnClose:
                dismiss();
                break;
            case R.id.btnAddToList:
                String productName = edtProductName.getText().toString();
                Integer amount = Integer.parseInt(edtAmount.getText().toString());
                Double price = Double.parseDouble(edtPrice.getText().toString());
                String desc = edtDesc.getText().toString();

                int id = addToList(productName, amount, price, desc);

                ShoppingListActivity activity = (ShoppingListActivity)getActivity();
                productItem item = new productItem(id, productName, price);
                activity.addToAdapter(item);

                cleanText();
                break;

            default: break;
        }
    }

    private void cleanText() {
        edtProductName.setText("");
        edtAmount.setText("");
        edtPrice.setText("");
        edtDesc.setText("");

        edtProductName.requestFocus();
    }

    /**
     * addToList -- to add product information to table
     * normally we should have another table shoppingList, only store userid, productid and status
     * but here I just put user id into product, just save time
     * @param strProduct
     * @param nAmount
     * @param dPrice
     * @param strDescription
     * @author Raymond Fu
     */
    public int addToList(String strProduct, Integer nAmount, Double dPrice, String strDescription)
    {
        String dbName = "login.db";
        JSONObject tableInfo = new JSONObject();
        try {
            tableInfo.put("TableName", "product");
            tableInfo.put("CreateDatabase", "create table if not exists product(id integer not null primary key autoincrement, userid text, name text, amount integer, price double, description text, addedDate datetime, status integer);");
            tableInfo.put("UpdateDatabase", "drop table if exists product");
            JSONObject columns = new JSONObject();
            columns.put("id", "id");
            columns.put("userid", "userid");
            columns.put("name", "name");
            columns.put("amount", "amount");
            columns.put("price", "price");
            columns.put("description", "description");
            tableInfo.put("Columns", columns);

            String sql = String.format("insert into product(userid, name, amount, price, description) values('%s','%s',%d,%.0f,'%s');", userName, strProduct, nAmount, dPrice, strDescription);
            Log.d("TEST", sql);
            HJDatasource hjdb = new HJDatasource(getActivity(), dbName, tableInfo);
            if(hjdb != null) {
                try {
                    hjdb.open();

                    hjdb.executeSql(sql);

                    hjdb.open();
                    sql = "SELECT last_insert_rowid() FROM product;";
                    JSONArray data = hjdb.query(sql);
                    Log.d("TEST", data.toString());
                    if(data.length() > 0) {
                        int id = data.getJSONObject(0).getInt("last_insert_rowid()");

                        return id;
                    }


                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
