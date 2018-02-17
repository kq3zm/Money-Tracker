package edu.virginia.cs4720.final_project_final_sudowoodo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static android.content.ContentValues.TAG;
import android.support.design.widget.FloatingActionButton;
import java.util.ArrayList;
import android.widget.TextView;
import android.util.Log;
import android.provider.BaseColumns;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    private double total=0;
    public OverviewFragment() {
        // Required empty public constructor
    }

    public void onViewCreated (View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Overview");
        setHasOptionsMenu(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        ArrayList<ArrayList<String>> list =new ArrayList<ArrayList<String>>();
        DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
        Log.d(TAG, "dbhelper");
        //Get the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(TAG, "db");
        String selectQuery = "SELECT * FROM " + ListFragment.DataTable.TABLE_NAME;
        Log.d(TAG, "select");
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "cursor");

        final StringBuilder emailText = new StringBuilder();
        //Iterate through the database (to check the database)
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emailText.append(cursor.getString(0) + " (" + cursor.getString(1) + "): $"+ cursor.getString(2)+ " on " +cursor.getString(4)+"\n");
            total+= Double.valueOf(cursor.getString(2));
            cursor.moveToNext();
        }

        final String email = emailText.toString();
        TextView totalSpent = (TextView) view.findViewById(R.id.overall_spending);
        totalSpent.setText(" $" + Double.toString(total) + " ");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Money Tracker Summary");
                intent.putExtra(Intent.EXTRA_TEXT, email);
                intent.setData(Uri.parse("mailto:")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });
        return view;
    }

    public void sendInfo (View view ) {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        i.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
            getActivity().finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

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

}
