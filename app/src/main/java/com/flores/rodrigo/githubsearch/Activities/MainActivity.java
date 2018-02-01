package com.flores.rodrigo.githubsearch.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flores.rodrigo.githubsearch.Adapters.RepositoryAdapter;
import com.flores.rodrigo.githubsearch.NetworkUtils;
import com.flores.rodrigo.githubsearch.R;
import com.flores.rodrigo.githubsearch.Class.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        RepositoryAdapter.RepositoryAdapterOnClickHandler {

    private static final String REPO_DATA = "repoData";
    private RepositoryAdapter repositoryAdapter;
    private EditText et_query;
    private Button btn_search;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView msj_busqueda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_query = findViewById(R.id.et_query);
        btn_search = findViewById(R.id.btn_search);
        progressBar = findViewById(R.id.progressBar);
        msj_busqueda=findViewById(R.id.msj_realizaBusqueda);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        repositoryAdapter = new RepositoryAdapter(this, this);
        recyclerView.setAdapter(repositoryAdapter);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        et_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    loadRepoAndHideKeyboard(imm, textView);
                    return true;
                }
                return false;
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRepoAndHideKeyboard(imm, view);
            }
        });
    }

    private void loadRepoAndHideKeyboard(InputMethodManager imm, View v) {
        if (et_query.getText().length() == 0) {
            Snackbar.make(v, getString(R.string.intro_lenguaje), Snackbar.LENGTH_SHORT).show();
        } else {
            loadRepositoriesData(et_query.getText().toString());
            imm.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void loadRepositoriesData(String query) {
        if (NetworkUtils.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            new FetchRepositoriesTask().execute(query);
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, getString(R.string.error), Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(Repository repositorySelected) {
        Log.e("ALGOOO", "SELECTIONADo");
        Context context = this;
        Class destination = RepositoryInfoActivity.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra(REPO_DATA, repositorySelected);
        startActivity(intent);
    }

    public class FetchRepositoriesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            msj_busqueda.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            URL url = NetworkUtils.buildURLQuery(params[0]);
            return NetworkUtils.getJSON(url);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                makeRepoArray(s);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        private void makeRepoArray(String data) throws JSONException {
            String ITEMS = "items";
            String NAME = "name";
            String DESCRIPTION = "description";
            String OWNER = "owner";
            String LOGIN_OWNER = "login";
            String IMAGE_URL = "avatar_url";
            String CONTRIBUTORS_URL = "contributors_url";


            JSONObject json;
            json = new JSONObject(data);
            JSONArray array_results = json.getJSONArray(ITEMS);
            Repository[] repositories = new Repository[array_results.length()];

            for (int i = 0; i < array_results.length(); i++) {
                repositories[i] = new Repository();
                JSONObject info = array_results.getJSONObject(i);
                repositories[i].setName(info.getString(NAME));
                repositories[i].setDescription(info.getString(DESCRIPTION));
                repositories[i].setContributorsURL(info.getString(CONTRIBUTORS_URL));

                JSONObject owner = info.getJSONObject(OWNER);
                repositories[i].setOwner(owner.getString(LOGIN_OWNER));
                repositories[i].setImageURL(owner.getString(IMAGE_URL));
            }
            repositoryAdapter.setRepositoriesData(repositories);
        }
    }


}
