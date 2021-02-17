package com.internship.networkingjava;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btn);
        editText = findViewById(R.id.nameText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView();
            }
        });
    }

    private void updateTextView() {
        NetworkTask networkTask = new NetworkTask();
        String url = "https://api.github.com/search/users?q=";
        String username = editText.getText().toString();
        networkTask.execute(url.concat(username));
    }

    class NetworkTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A"); //allows to read the entire content of input stream in one go
                if (scanner.hasNext()){
                    String s = scanner.next();
                    return s;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Failed to load";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<GithubUser> users = parseJSON(s);
            Log.d(TAG, "onPostExecute: "+ users.size());
            GithubUserAdapter githubUserAdapter = new GithubUserAdapter(users);
            RecyclerView recyclerView = findViewById(R.id.rvUsers);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerView.setAdapter(githubUserAdapter);
        }
    }

    ArrayList<GithubUser> parseJSON(String s){
        ArrayList<GithubUser> githubUsers = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(s);
            JSONArray items = root.getJSONArray("items");
            for(int i =0; i<items.length(); i++){
                JSONObject object = items.getJSONObject(i);
                String login = object.getString("login");
                Integer id = object.getInt("id");
                String html_url = object.getString("html_url");
                String avatar_url = object.getString("avatar_url");

                GithubUser githubUser = new GithubUser(login, id, html_url, avatar_url);
                githubUsers.add(githubUser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return githubUsers;
    }
}