package com.example.tomas.tamz2_projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainPageAdapter extends ArrayAdapter<MainPage> {

    Context context;
    List<MainPage> data = null;
    int layoutResourceId;

    public MainPageAdapter(Context context, int resource, List<MainPage> data) {
        super(context, resource, data);

        this.context = context;
        this.data= data;
        this.layoutResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        mainPageHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new mainPageHolder();
            holder.txtNadpis = (TextView)row.findViewById(R.id.txtNadpis);
            holder.txtPopis = (TextView)row.findViewById(R.id.txtPopis);
            holder.imgSymbol = (ImageView) row.findViewById(R.id.imageFlag);

            row.setTag(holder);
        }
        else
        {
            holder = (mainPageHolder)row.getTag();
        }

        MainPage entryData = data.get(position);
        holder.txtNadpis.setText(entryData.nadpis);
        holder.txtPopis.setText(entryData.popis);
        holder.imgSymbol.setImageResource(getImageId(context, entryData.imgSymbol));

        return row;
    }

    public static int getImageId(Context context, String imgSymbol) {
        return context.getResources().getIdentifier("drawable/" + imgSymbol.toLowerCase(), null, context.getPackageName());
    }

    static class mainPageHolder{
        TextView txtNadpis;
        TextView txtPopis;
        ImageView imgSymbol;
    }
}
