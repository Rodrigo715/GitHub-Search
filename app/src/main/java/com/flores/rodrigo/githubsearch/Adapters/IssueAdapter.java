package com.flores.rodrigo.githubsearch.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flores.rodrigo.githubsearch.Class.Issue;
import com.flores.rodrigo.githubsearch.R;

public class IssueAdapter extends RecyclerView.Adapter{
    private Context context;
    private Issue[] issues;

    public IssueAdapter(Context context) {
        this.context = context;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tv_username;
        TextView tv_title;
        TextView tv_body;

        public Holder(View itemView) {
            super(itemView);
            tv_username=itemView.findViewById(R.id.tv_issue_username);
            tv_title=itemView.findViewById(R.id.tv_issue_title);
            tv_body=itemView.findViewById(R.id.body_issue);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.issue_item, parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder h= (Holder) holder;
        Issue issue= issues[position];
        h.tv_title.setText(issue.getTitle());
        h.tv_username.setText(issue.getUser());
        h.tv_body.setText(issue.getBody());

    }

    @Override
    public int getItemCount() {
        if (issues==null)return 0;
        return issues.length;
    }


    public void setIssuesData(Issue[] issuesData){
        issues=issuesData;
        notifyDataSetChanged();
    }

}
