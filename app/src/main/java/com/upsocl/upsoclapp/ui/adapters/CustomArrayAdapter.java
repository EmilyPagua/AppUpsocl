package com.upsocl.upsoclapp.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.Interests;


import java.util.List;

/**
 * Created by upsocl on 22-08-16.
 */
public class CustomArrayAdapter extends ArrayAdapter<Interests> implements View.OnClickListener{

    private LayoutInflater layoutInflater;
    private SharedPreferences preferences;


    public CustomArrayAdapter(Context context, List<Interests> objects) {
        super(context,0,objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (convertView ==null)
        {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.listview_row, null);
            holder.setTextViewTitle((TextView)convertView.findViewById(R.id.textViewTitle));
            holder.setCheckBox((CheckBox) convertView.findViewById(R.id.checkBox));
            holder.setImageView((ImageView) convertView.findViewById(R.id.img_interest));
            convertView.setTag(holder);
        }
        else{
            holder =  (Holder) convertView.getTag();
        }

        final Interests row = getItem(position);
        holder.getTextViewTitle().setText(row.getTitle());

        holder.getCheckBox().setTag(position);
        holder.getCheckBox().setChecked(row.getIsCheck());
        holder.getCheckBox().setOnClickListener(this);

        holder.getImageView().setImageResource(row.getImagen());

        return convertView;
    }

    @Override
    public void onClick(View view) {

        CheckBox checkBox =  (CheckBox) view;
        int position  = (Integer) view.getTag();
        getItem(position).setIsCheck(checkBox.isChecked());

        Toast.makeText(this.getContext(), "Ha seleccionado: " + getItem(position).getTitle(), Toast.LENGTH_LONG).show();
        savePreferences(getItem(position).getId(),getItem(position).getIsCheck());

    }

    private void savePreferences(int id, Boolean flag) {
        SharedPreferences.Editor editor =  preferences.edit();
        editor.putBoolean(String.valueOf(id), flag).commit();

    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    static class Holder{

        TextView textViewTitle;
        CheckBox checkBox;
        ImageView imageView;

        public TextView getTextViewTitle() {
            return textViewTitle;
        }

        public void setTextViewTitle(TextView textViewTitle) {
            this.textViewTitle = textViewTitle;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }


}
