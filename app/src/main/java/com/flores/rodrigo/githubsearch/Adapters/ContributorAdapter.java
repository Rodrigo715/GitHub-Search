package com.flores.rodrigo.githubsearch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flores.rodrigo.githubsearch.Class.Contributor;
import com.flores.rodrigo.githubsearch.R;

public class ContributorAdapter extends RecyclerView.Adapter {
    private Context context;
    private Contributor[] contributors;

    public ContributorAdapter(Context context){
        this.context=context;
    }

   public class Holder extends RecyclerView.ViewHolder{
        TextView tv_name;
        ImageView iv_image;

        public Holder(View itemView) {
           super(itemView);
           tv_name=itemView.findViewById(R.id.tv_contributorName);
           iv_image=itemView.findViewById(R.id.iv_contributorImage);
       }
   }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.contributor_item,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder h= (Holder) holder;
        Contributor contributor= contributors[position];
        h.tv_name.setText(contributor.getName());
        Glide.with(context).load(contributor.getImage_url()).into(h.iv_image);
    }

    @Override
    public int getItemCount() {
        if (contributors==null)return 0;
        return contributors.length;
    }

    public void setContributorsData(Contributor[] contributorsData){
        contributors=contributorsData;
        notifyDataSetChanged();
    }

}
