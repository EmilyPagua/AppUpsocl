package com.upsocl.upsoclapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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

import cl.upsocl.upsoclapp.DetailPostActivity;

/**
 * Created by leninluque on 09-11-15.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private ArrayList<News> news;
    private Context context;
    private Boolean isHome;

    public NewsAdapter(Context context, Boolean isHome) {
        this.context = context;
        this.news = new ArrayList<>();
        this.isHome =  isHome;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newsView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsHolder(newsView, news);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        holder.name.setText(news.get(position).getTitle());
        String author = "Por: "+news.get(position).getAuthor();
        String  date = "El: "+news.get(position).getDate();
        String categories = "Categoria: "+news.get(position).getCategories();

        SpannableStringBuilder sBauthor = setStyleText(author, 4,  author.length());
        SpannableStringBuilder sBdetail = setStyleText(date, 3,  date.length());
        Spanned spanned = (Spanned) TextUtils.concat(sBauthor, ". ", sBdetail);
        SpannableStringBuilder result = new SpannableStringBuilder(spanned);

        holder.dataPost.setText(result, TextView.BufferType.SPANNABLE);
        holder.setNewsObj(news.get(position));
        holder.setPosition(position);

        SpannableStringBuilder sCategories = setStyleText(categories, 10,  categories.length());
        holder.categories.setText(sCategories);

        if (!(news.get(position).getImage() == "")){
            holder.setImage(news.get(position).getImage());}
        else
            holder.setImage(ViewConstants.PLACEHOLDER_IMAGE);
    }

    private SpannableStringBuilder setStyleText(String text, int i, int length) {

        SpannableStringBuilder result = new SpannableStringBuilder(text);
        result.setSpan(new ForegroundColorSpan(Color.parseColor("#009688")), i, length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        return result;
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
            dataPost = (TextView) itemView.findViewById(R.id.data_post);
            categories =  (TextView)itemView.findViewById(R.id.categories);
            newsArrayList =  news;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailPostActivity.class);
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


        public void setPosition(int position) {
            this.position = position;
        }

        public void setNewsObj(News newsObj) {
            this.newsObj = newsObj;
        }

    }


}
