package com.upsocl.upsoclapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.ui.ViewConstants;

import java.util.ArrayList;

import cl.upsocl.upsoclapp.DetailPagerAdapter;

/**
 * Created by emily.pagua on 09-11-15.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private ArrayList<News> news;
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
        this.news = new ArrayList<>();
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try{
            View newsView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            return new NewsHolder(newsView, news);
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {

        try{
            holder.setNewsObj(news.get(position));
            holder.setPosition(position);

            holder.name.setText(news.get(position).getTitle());

            if (!(news.get(position).getImage() == "")){
                holder.setImage(news.get(position).getImage());}
            else
                holder.setImage(ViewConstants.PLACEHOLDER_IMAGE);
            Log.e("NewsAdapter", "bien");
        }catch (Exception e){
            Log.e("NewsAdapter", e.getMessage());
        }

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
        private TextView categories;
        private News newsObj;
        private ArrayList<News> newsArrayList;
        private int position;

        public NewsHolder(final View itemView, ArrayList<News> news) {

            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.img_post);
            name = (TextView) itemView.findViewById(R.id.title_post);

            newsArrayList =  news;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), DetailPagerAdapter.class);
                    Gson gS = new Gson();
                    int lenght = 1;
                    String target = gS.toJson(newsObj);
                    intent.putExtra("new", target);
                    intent.putExtra("position",position);
                    intent.putExtra("isBookmarks",false);

                    if (newsArrayList.size()>position+1 && newsArrayList.get(position+1)!=null){
                        intent.putExtra("newsSegundary", gS.toJson(newsArrayList.get(position+1)));
                        lenght++;}

                    if (newsArrayList.size()>position+2 && newsArrayList.get(position+2)!=null){
                        intent.putExtra("newsThree", gS.toJson(newsArrayList.get(position+2)));lenght++;}

                    if (newsArrayList.size()>position+3 && newsArrayList.get(position+3)!=null){
                        intent.putExtra("newsFour", gS.toJson(newsArrayList.get(position+3)));lenght++;}

                    if (newsArrayList.size()>position+4 && newsArrayList.get(position+4)!=null){
                        intent.putExtra("newsFive", gS.toJson(newsArrayList.get(position+4)));lenght++;}

                    intent.putExtra("leght",lenght);
                    view.getContext().startActivity(intent);


                    /*Intent intent = new Intent(view.getContext(), DetailPostActivity.class);
                    int lenght = 1;
                    Gson gS = new Gson();
                    String target = gS.toJson(newsObj);
                    intent.putExtra("new", target);
                    intent.putExtra("position",position);
                    intent.putExtra("isBookmarks",false);

                    if (newsArrayList.size()>position+1 && newsArrayList.get(position+1)!=null){
                        intent.putExtra("newsSegundary", gS.toJson(newsArrayList.get(position+1)));
                        lenght++;}

                    if (newsArrayList.size()>position+2 && newsArrayList.get(position+2)!=null){
                        intent.putExtra("newsThree", gS.toJson(newsArrayList.get(position+2)));lenght++;}

                    if (newsArrayList.size()>position+3 && newsArrayList.get(position+3)!=null){
                        intent.putExtra("newsFour", gS.toJson(newsArrayList.get(position+3)));lenght++;}

                    if (newsArrayList.size()>position+4 && newsArrayList.get(position+4)!=null){
                        intent.putExtra("newsFive", gS.toJson(newsArrayList.get(position+4)));lenght++;}

                    intent.putExtra("leght",lenght);
                    view.getContext().startActivity(intent);*/
                }
            });
        }

        public void setImage(String url) {

            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        }


        public void setPosition(int position) {
            this.position = position;
        }

        public void setNewsObj(News newsObj) {
            this.newsObj = newsObj;
        }

    }


}
