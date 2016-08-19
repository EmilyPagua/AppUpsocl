package com.upsocl.upsoclapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.ui.ViewConstants;

import java.util.List;

import cl.upsocl.upsoclapp.DetailPostActivity;

/**
 * Created by upsocl on 18-07-16.
 */
public class BookmarksAdapter  extends ArrayAdapter<News>{

    private ImageView image;
    private TextView name;
    private TextView dataPost;
    private ImageButton ima_bookmarks, ima_share;
    private static SharedPreferences preferences;
    private final Context context;

    public BookmarksAdapter (Context context, List<News> newsList){
        super (context, R.layout.item_news_bookmarks, newsList);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent ) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null== convertView){
            convertView = inflater.inflate(R.layout.item_news_bookmarks,parent,false);
        }
        final News news =  getItem(position);

        image = (ImageView) convertView.findViewById(R.id.img_post_bookmark);
        name = (TextView) convertView.findViewById(R.id.title_post_bookmark);
        dataPost = (TextView) convertView.findViewById(R.id.data_post_bookmark);
        ima_bookmarks = (ImageButton)convertView.findViewById(R.id.ima_bookmarks);
        ima_share = (ImageButton) convertView.findViewById(R.id.ima_share);


        String author = "Por: "+news.getAuthor();
        String  date = "El: "+news.getDate();

        SpannableStringBuilder sBauthor = new SpannableStringBuilder(author);
        sBauthor.setSpan(new ForegroundColorSpan(Color.parseColor("#009688")), 4, author.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        SpannableStringBuilder sBdetail = new SpannableStringBuilder(date);
        sBdetail.setSpan(new ForegroundColorSpan(Color.parseColor("#009688")), 3, date.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        Spanned spanned = (Spanned) TextUtils.concat(sBauthor, ". ", sBdetail);
        SpannableStringBuilder result = new SpannableStringBuilder(spanned);

        if (!(news.getImage() == ""))
            Picasso.with(context)
                    .load(news.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        else
            Picasso.with(context)
                    .load(ViewConstants.PLACEHOLDER_IMAGE)
                    .placeholder(R.drawable.placeholder)
                    .into(image);

        name.setText(news.getTitle());
        dataPost.setText(result, TextView.BufferType.SPANNABLE);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailPostActivity.class);
                Gson gS = new Gson();
                String target = gS.toJson(news);
                intent.putExtra("new", target);
                intent.putExtra("position","0");
                intent.putExtra("isBookmarks",true);
                intent.putExtra("leght",1);
                context.startActivity(intent);


            }
        });

        ima_bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(news);
                preferences = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =  preferences.edit();
                editor.remove(String.valueOf(news.getId())).commit();
                Toast.makeText(context, "No esta como preferido", Toast.LENGTH_SHORT).show();
            }
        });

        ima_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, news.getLink());
                context.startActivity(Intent.createChooser(shareIntent,  "Compartir: " +news.getTitle()));
            }
        });
        return convertView;
    }
}
