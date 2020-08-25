package com.example.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class ContactAdapter extends ArrayAdapter {
    private Context context;
    private List<Contact> contacts;
    public ContactAdapter(Context context, List<Contact> list)
    {
        super(context,R.layout.row_layout,list);
        this.context=context;
        this.contacts=list;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView  =inflater.inflate(R.layout.row_layout,parent,false);
        TextView tvchar = convertView.findViewById(R.id.tvchar);
        TextView tvname= convertView.findViewById(R.id.tvname);
        TextView tvmail = convertView.findViewById(R.id.tvmail);

        tvchar.setText(contacts.get(position).getName().toUpperCase().charAt(0)+"");
        tvname.setText(contacts.get(position).getName());
        tvmail.setText(contacts.get(position).getEmail());

        return convertView;
    }
}
