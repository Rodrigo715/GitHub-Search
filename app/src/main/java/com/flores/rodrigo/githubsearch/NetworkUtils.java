package com.flores.rodrigo.githubsearch;


import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils extends Activity {

    private static final String GITHUB_BASE_URL = "https://api.github.com";
    private static final String SEARCH = "search";
    private static final String REPOSITORIES = "repositories";
    private static final String REPOS = "repos";
    private static final String ISSUES = "issues";
    private static final String PARAM_QUERY = "q";
    private static final String PARAM_SORT = "sort";
    private static final String SORTBY = "stars";


    public static boolean isOnline(ConnectivityManager cm) {
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static URL buildURLQuery(String query) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendPath(SEARCH)
                .appendPath(REPOSITORIES)
                .appendQueryParameter(PARAM_QUERY, query)
                .appendQueryParameter(PARAM_SORT, SORTBY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURLContributors(String link){
        URL url=null;
        try {
            url=new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURLIssues(String owner, String repoName){
        Uri builtUri=Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendPath(REPOS)
                .appendPath(owner)
                .appendPath(repoName)
                .appendPath(ISSUES)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }


    public static String getJSON(URL url) {
        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String input;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder responseBuilder = new StringBuilder();
            while ((input = reader.readLine()) != null) {
                responseBuilder.append(input);
            }
            String data = responseBuilder.toString();
            reader.close();
            connection.disconnect();
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
