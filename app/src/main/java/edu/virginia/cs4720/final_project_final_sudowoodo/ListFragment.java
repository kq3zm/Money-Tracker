package edu.virginia.cs4720.final_project_final_sudowoodo;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import org.w3c.dom.Text;
import android.widget.TextView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.text.DecimalFormat;
import android.widget.Toast;

import java.sql.Blob;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private double total=0;
    RecyclerView rView;


    public ListFragment() {
        // Required empty public constructor
    }

    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    public void onViewCreated (View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("List View");
    }


    //Necessary for accessing SQLite table in this activity.
    public static class DataTable implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_DAY = "day";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        // 1. get a reference to recyclerView
        rView = (RecyclerView) view.findViewById(R.id.rvList);

        // 2. set layoutManger
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // this is data from recycler view
        ArrayList<ArrayList<String>> initialList =new ArrayList<ArrayList<String>>();
        // also data to be passed to the adapter (image byte[])
        ArrayList<byte[]> imageList = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
        Log.d(TAG, "dbhelper");
        //Get the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(TAG, "db");
        String selectQuery = "SELECT * FROM " + DataTable.TABLE_NAME;
        Log.d(TAG, "select");
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "cursor");


        //Iterate through the database (to check the database)
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ArrayList<String> displayText = new ArrayList<String>();
            displayText.add(cursor.getString(0));
            Log.d(TAG, cursor.getString(0));
            displayText.add(cursor.getString(1));
            displayText.add(cursor.getString(2));
            total+= Double.valueOf(cursor.getString(2));
            displayText.add(cursor.getString(3));
            displayText.add(cursor.getString(4));
            displayText.add(cursor.getString(5));
            displayText.add(cursor.getString(6));
            displayText.add(cursor.getString(7));
            imageList.add(cursor.getBlob(8));
            displayText.add(cursor.getString(9));
            initialList.add(displayText);
            cursor.moveToNext();
        }

        TextView totalSpent = (TextView) view.findViewById(R.id.totalAmt);
        DecimalFormat decim = new DecimalFormat("#.00");
        total= Double.parseDouble(decim.format(total));
        totalSpent.setText("$"+ Double.toString(total));
        // 3. create an adapter
        ListFragmentAdapter mAdapter = new ListFragmentAdapter(this.getContext(), initialList, imageList);
        // 4. set adapter
        rView.setAdapter(mAdapter);
        return view;
    }



}
