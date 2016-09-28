package com.example.zeynep.e_ticaret;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ZEYNEP on 30.08.2016.
 */
public class urungosteradapter extends BaseAdapter {

    private LayoutInflater inf;
    private List<urunOzellik> urls;
    private Activity ac;

    public urungosteradapter(Activity ac, List<urunOzellik> urls) {
        inf = (LayoutInflater) ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.urls = urls;
        this.ac = ac;}
    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;

        view1 = inf.inflate(R.layout.urundetayrow, null);
        TextView urunBasliktxt = (TextView) view1.findViewById(R.id.urundetaytxt);
        TextView urunFiyattxt=(TextView)view1.findViewById(R.id.urunfiyattxt);
        ImageView vresim = (ImageView) view1.findViewById(R.id.urundetayresim);
        urunOzellik ul = urls.get(i);
        urunBasliktxt.setText(ul.getProductName());
        urunFiyattxt.setText(ul.getPrice());
        Picasso.with(ac).load(ul.getResim()).into(vresim);
        return view1;

    }
}
