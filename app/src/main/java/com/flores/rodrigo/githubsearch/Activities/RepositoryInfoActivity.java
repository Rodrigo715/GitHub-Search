package com.flores.rodrigo.githubsearch.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flores.rodrigo.githubsearch.Adapters.ContributorAdapter;
import com.flores.rodrigo.githubsearch.Adapters.IssueAdapter;
import com.flores.rodrigo.githubsearch.Class.Contributor;
import com.flores.rodrigo.githubsearch.Class.Issue;
import com.flores.rodrigo.githubsearch.Class.Repository;
import com.flores.rodrigo.githubsearch.NetworkUtils;
import com.flores.rodrigo.githubsearch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class RepositoryInfoActivity extends AppCompatActivity {

    private static final String REPO_DATA = "repoData";
    private static final int NUM_ISSUES=3;
    private static final int NUM_CONTRIBUTORS=3;

    private Repository repository;
    private ContributorAdapter contributorAdapter;
    private IssueAdapter issueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_info);

        RecyclerView recyclerViewContributors = findViewById(R.id.recyclerView_contributors);
        RecyclerView recyclerViewIssues = findViewById(R.id.recyclerView_issues);
        ImageView imageCollapse = findViewById(R.id.image_collapse);
        TextView descRepo=findViewById(R.id.description_repo);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                repository = intent.getParcelableExtra(REPO_DATA);
                collapsingToolbarLayout.setTitle(repository.getName());
                collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorAccent));
                Glide.with(this).load(repository.getImageURL()).into(imageCollapse);
                descRepo.setText(repository.getDescription());
            }
        }

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewContributors.setLayoutManager(linearLayoutManager);
        recyclerViewContributors.setHasFixedSize(true);
        contributorAdapter = new ContributorAdapter(this);
        recyclerViewContributors.setAdapter(contributorAdapter);


        LinearLayoutManager linearLayoutManager1
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewIssues.setLayoutManager(linearLayoutManager1);
        recyclerViewIssues.setHasFixedSize(true);
        issueAdapter = new IssueAdapter(this);
        recyclerViewIssues.setAdapter(issueAdapter);

        loadContributorIssueData();
    }


    private void loadContributorIssueData() {
        if (NetworkUtils.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            new FetchContributorData().execute(repository.getContributorsURL());
            new FetchIssuesData().execute(repository.getOwner(),repository.getName());
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, getString(R.string.error), Snackbar.LENGTH_SHORT).show();
        }
    }


    public class FetchContributorData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL urlContributors = NetworkUtils.buildURLContributors(params[0]);
            return NetworkUtils.getJSON(urlContributors);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null)
                    makeContributorsArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        private void makeContributorsArray(String data) throws JSONException {
            String LOGIN="login";
            String AVATAR_URL="avatar_url";

            JSONArray json;
            json=new JSONArray(data);

            Contributor[] contributors=new Contributor[NUM_CONTRIBUTORS];

            for (int i=0;i<NUM_CONTRIBUTORS;i++){
                contributors[i]=new Contributor();
                JSONObject info= json.getJSONObject(i);
                contributors[i].setName(info.getString(LOGIN));
                contributors[i].setImage_url(info.getString(AVATAR_URL));
            }
            contributorAdapter.setContributorsData(contributors);
        }
    }


    public class FetchIssuesData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL urlContributors = NetworkUtils.buildURLIssues(params[0],params[1]);
            return NetworkUtils.getJSON(urlContributors);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null)
                    makeIssuesArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        private void makeIssuesArray(String data) throws JSONException {
            String USER="user";
            String LOGIN="login";
            String TITLE="title";
            String BODY="body";

            JSONArray json;
            json=new JSONArray(data);

            Issue[] issues= new Issue[NUM_ISSUES];

            for (int i=0;i<NUM_ISSUES;i++){
                issues[i]=new Issue();
                JSONObject info=json.getJSONObject(i);
                issues[i].setTitle(info.getString(TITLE));
                issues[i].setBody(info.getString(BODY));
                JSONObject owner= info.getJSONObject(USER);
                issues[i].setUser(owner.getString(LOGIN));
            }
            issueAdapter.setIssuesData(issues);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
