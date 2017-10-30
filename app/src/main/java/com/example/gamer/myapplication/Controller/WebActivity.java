package com.example.gamer.myapplication.Controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gamer.myapplication.Data.Match;
import com.example.gamer.myapplication.Data.MyAdapter;
import com.example.gamer.myapplication.R;
import com.example.gamer.myapplication.Utility.DatabaseHelper;


import java.util.ArrayList;

public class WebActivity extends AppCompatActivity {
    TextView txt;
    LinearLayout container;
    String username, admin = "admin";
    GridView grid;
    ArrayList<Match> matches = new ArrayList<>();
    ArrayList<String> favTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        matches = MainActivity.webSearch.matches;

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        organizeData(MainActivity.webSearch.matches);
    }


    void organizeData(ArrayList<Match> matches){
        DatabaseHelper myDb = new DatabaseHelper(getBaseContext());
        Cursor res = myDb.getAllData();
        favTeams = new ArrayList<>();

        while (res.moveToNext()){
            if(res.getString(1).equalsIgnoreCase(username))
            favTeams.add(res.getString(2));
        }

        if(username.equalsIgnoreCase(admin)){
            setTextView(favTeams, matches);
        }else {
            setGridView(favTeams, matches, favTeams);
        }
    }

    private void setGridView(ArrayList<String> favTeams, ArrayList<Match> matches, ArrayList<String> teams){
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new MyAdapter(getBaseContext(), matches, favTeams));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(),grid.getAdapter().getItem(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTextView(ArrayList<String> favTeams, ArrayList<Match> matches) {
        txt = new TextView(getBaseContext());
        txt.setTextSize(14);


        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "TEST", Toast.LENGTH_SHORT).show();
            }
        });

        if(username.equalsIgnoreCase(admin)){
            txt.setText(MainActivity.webSearch.words.toString());
            container.addView(txt);
        }else {
            String date = null;
            for (Match m : matches) {

                if (!m.getDate().equalsIgnoreCase(date) && m.toString() != null) {
                    txt.append("\n[" + m.getDate() + "]\n");
                }

                if (m.toString() != null) {

                    if (favTeams.contains(m.getTeamOneName().toLowerCase()) || favTeams.contains(m.getTeamTwoName().toLowerCase())) {
                        txt.append(Html.fromHtml("<font color='#EE0000'>" + m.toString() + "</font>"));
                        txt.append("\n");
                    } else {
                        txt.append(m.toString() + "\n");
                    }
                }
                date = m.getDate();
            }
            container.addView(txt);
        }
    }

    public void teamSearch(View view) {
        ArrayList<Match> specMatches = new ArrayList<>();
        EditText searchTxt = (EditText) findViewById(R.id.searchTeamTxt);
        for (Match m : matches){
            if(m.getTeamOneName().equalsIgnoreCase(searchTxt.getText().toString()) || m.getTeamTwoName().equalsIgnoreCase(searchTxt.getText().toString())){
                specMatches.add(m);
            }
        }

        if(searchTxt.getText().toString().isEmpty()){
            grid.setAdapter(new MyAdapter(getBaseContext(), matches, favTeams));
        }else {
            grid.setAdapter(new MyAdapter(getBaseContext(), specMatches, favTeams));
            searchTxt.setText("");
        }
    }


}

