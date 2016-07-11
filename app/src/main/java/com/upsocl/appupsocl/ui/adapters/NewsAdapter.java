package com.upsocl.appupsocl.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.appupsocl.DetailActivity;
import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.domain.News;
import com.upsocl.appupsocl.ui.ViewConstants;

import java.util.ArrayList;

/**
 * Created by leninluque on 09-11-15.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    ArrayList<News> news;
    Context context;

    public NewsAdapter(Context context) {
        this.context = context;
        this.news = new ArrayList<>();
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newsView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsHolder(newsView);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        holder.name.setText(news.get(position).getTitle());
        holder.dataPost.setText("Publicado: "+news.get(position).getDatePublicaded());
        holder.setNewsObj(news.get(position));

        if (!(news.get(position).getImage() == "")){
            holder.setImage(news.get(position).getImage());}
        else
            holder.setImage(ViewConstants.PLACEHOLDER_IMAGE);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void addAll(ArrayList<News> newses) {
        if (newses == null)
            throw new NullPointerException("The items cannot be null");

        this.news.addAll(newses);
        notifyDataSetChanged();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private TextView dataPost;
        private News newsObj;

        public NewsHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.img_post);
            name = (TextView) itemView.findViewById(R.id.title_post);
            dataPost = (TextView) itemView.findViewById(R.id.data_post);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    Gson gS = new Gson();
                    String target = gS.toJson(newsObj);
                    intent.putExtra("new", target);
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void setImage(String url) {

            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setNewsObj(News newsObj) {
            this.newsObj = newsObj;
        }

        public void setDataPost(String dataPost) {
            this.dataPost.setText(dataPost);
        }
    }


}
