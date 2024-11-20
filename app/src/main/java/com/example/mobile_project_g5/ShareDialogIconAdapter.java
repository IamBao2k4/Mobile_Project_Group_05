package com.example.mobile_project_g5;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ShareDialogIconAdapter extends BaseAdapter {
    Context context;
    Icon[] icon;

    public ShareDialogIconAdapter(Context context, Icon[] icon) {
        this.context = context;
        this.icon = icon;
    }
    @Override
    public int getCount() {
        return icon.length;
    }

    @Override
    public Object getItem(int position) {
        return icon[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.share_dialog_icon, parent, false);
        }

        ImageView img = convertView.findViewById(R.id.share_icon);
        TextView name = convertView.findViewById(R.id.share_name);
        // Load icon
        Uri uri = Uri.parse(icon[position].getIcon());
        Glide.with(context)
                .load(uri)
                .into(img);
        // Set name
        name.setText(icon[position].getName());

        return convertView;
    }
}
