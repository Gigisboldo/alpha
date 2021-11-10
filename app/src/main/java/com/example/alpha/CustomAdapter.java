package com.example.alpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Users>{

    public CustomAdapter(Context context, int textViewResourceId,
                         List<Users> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.users_log_on_custom, null);
        TextView user = (TextView)convertView.findViewById(R.id.textViewUser);
        TextView email =(TextView)convertView.findViewById(R.id.textViewEmail);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.ImageViewUser);
        Users c = getItem(position);
        user.setText(c.getUser());
        email.setText(c.getEmail());
        imageView.setImageResource(c.getImage());
        return convertView;
    }

}
