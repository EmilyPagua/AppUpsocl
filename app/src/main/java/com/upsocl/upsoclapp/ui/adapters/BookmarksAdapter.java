package com.upsocl.upsoclapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import java.util.Objects;
import cl.upsocl.upsoclapp.NotificationActivity;

/**
 * Created by $emily.pagua on $10-09-16.
 */
public class BookmarksAdapter  extends ArrayAdapter<News>{

    private ImageView image;
    private TextView name;
    private TextView dataPost;
    private ImageButton ima_bookmarks;
    private static SharedPreferences preferences;
    private final Context context;

    public BookmarksAdapter (Context context, List<News> newsList){
        super (context, R.layout.item_news_bookmarks, newsList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent ) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null== convertView){
            convertView = inflater.inflate(R.layout.item_news_bookmarks,parent,false);
        }
        final News news =  getItem(position);

        image = (ImageView) convertView.findViewById(R.id.img_post_bookmark);
        name = (TextView) convertView.findViewById(R.id.title_post_bookmark);
        ima_bookmarks = (ImageButton)convertView.findViewById(R.id.ima_bookmarks);
        ImageButton ima_share = (ImageButton) convertView.findViewById(R.id.ima_share);


        name.setText(news.getTitle());

        if (!(Objects.equals(news.getImage(), "")))
            Picasso.with(context)
                    .load(news.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        else
            Picasso.with(context)
                    .load(ViewConstants.PLACEHOLDER_IMAGE)
                    .placeholder(R.drawable.placeholder)
                    .into(image);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, NotificationActivity.class);
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
                editor.remove(String.valueOf(news.getId())).apply();
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
