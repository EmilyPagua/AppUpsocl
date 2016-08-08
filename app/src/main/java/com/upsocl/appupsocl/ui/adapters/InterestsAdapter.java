package com.upsocl.appupsocl.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.domain.Interests;

import java.util.ArrayList;

/**
 * Created by upsocl on 19-07-16.
 */
public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestsHolder> {

    private ArrayList<Interests>  interests;
    private Context context;
    private SharedPreferences prefs;

    public InterestsAdapter(Context context){
        this.context = context;
        this.interests = new ArrayList<>();
    }


    @Override
    public InterestsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View newsView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interests, parent, false);
        return new InterestsHolder(newsView, interests);
    }

    @Override
    public void onBindViewHolder(InterestsHolder holder, int position) {
        holder.setPosition(position);

        holder.image.setImageResource(interests.get(position).getImagen());
        holder.txtCategory.setText(interests.get(position).getTitle());
        holder.setInterestObj(interests.get(position));
        if (interests.get(position).getIsCheck())
            holder.checkBox.setChecked(interests.get(position).getIsCheck());
        else

            holder.checkBox.setVisibility(View.INVISIBLE);
    }


    @Override
    public int getItemCount() {
        return interests.size();
    }

    public void addAll(ArrayList<Interests> interests) {
        if (interests == null)
            throw new NullPointerException("The items cannot be null");

        this.interests.addAll(interests);
        notifyDataSetChanged();
    }

    public void addPreferences(SharedPreferences prefs) {
        if (prefs == null)
            throw new NullPointerException("The preferences cannot be null");

        this.prefs = prefs;
        notifyDataSetChanged();
    }


    public class InterestsHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private CheckBox checkBox;
        private Interests objet;
        private ArrayList<Interests> newsArrayList;
        private int position;
        private TextView txtCategory;

        public InterestsHolder(final View itemView, ArrayList<Interests> interestses) {

            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.idCategory);
            newsArrayList =  interestses;
            image = (ImageView) itemView.findViewById(R.id.img_interest);
            txtCategory =(TextView) itemView.findViewById(R.id.txtCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean flag =  true;
                    if (objet.getIsCheck())
                        flag =  false;

                    newsArrayList.get(position).setIsCheck(flag);
                    savePreferences(objet.getId(), flag);
                    if (flag)
                        checkBox.setVisibility(View.VISIBLE);

                    notifyDataSetChanged();
                }
            });
        }

        private void savePreferences(int id, Boolean flag) {
            SharedPreferences.Editor editor =  prefs.edit();
            editor.putBoolean(String.valueOf(id), flag).commit();
        }


        public void setPosition(int position) {
            this.position = position;
        }

        public void setInterestObj(Interests objet) {
            this.objet = objet;
        }

    }
}
