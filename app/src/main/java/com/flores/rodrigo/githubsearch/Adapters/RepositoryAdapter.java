package com.flores.rodrigo.githubsearch.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flores.rodrigo.githubsearch.R;
import com.flores.rodrigo.githubsearch.Class.Repository;

public class RepositoryAdapter extends RecyclerView.Adapter{

    private Context context;
    private Repository[] repositories;

    private final RepositoryAdapterOnClickHandler mClickHandler;

    public RepositoryAdapter(Context context, RepositoryAdapterOnClickHandler mClickHandler){
        this.context=context;
        this.mClickHandler=mClickHandler;
    }

    public interface RepositoryAdapterOnClickHandler{
        void onClick(Repository repositorySelected);
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_image;
        TextView tv_repo_namelist;

        public Holder(View itemView) {
            super(itemView);
            iv_image=itemView.findViewById(R.id.iv_image_list);
            tv_repo_namelist=itemView.findViewById(R.id.tv_repo_name_list);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Repository repositorySelected=repositories[adapterPosition];
            mClickHandler.onClick(repositorySelected);
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.repository_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder h= (Holder) holder;
        Repository repository= repositories[position];
        Glide.with(context).load(repository.getImageURL()).into(h.iv_image);
        h.tv_repo_namelist.setText(repository.getName());
    }

    @Override
    public int getItemCount() {
        if(repositories==null) return 0;
        return repositories.length;
    }


    public void setRepositoriesData(Repository[] repositoriesData){
        repositories=repositoriesData;
        notifyDataSetChanged();
    }

}
