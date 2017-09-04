package com.example.bangbangba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Marshal Gao on 2017/7/20.
 */

public class HomeBottomAdapter extends ArrayAdapter<HomeBottom> {

    private int resourceId;

    public HomeBottomAdapter(Context context, int textViewResourceId,
                             List<HomeBottom> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeBottom homeBottom = getItem(position);
        //View view = LayoutInflater.from(HomePageActivity.this).inflate(resourceId, parent, false);
        View view =LayoutInflater.from(getContext()).inflate(R.layout.homebottom_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView start_time = (TextView) view.findViewById(R.id.start_time);
        TextView initiator_name = (TextView) view.findViewById(R.id.initiator_name);
        TextView credit_value = (TextView) view.findViewById(R.id.credit_value);
        TextView money = (TextView) view.findViewById(R.id.money);
        image.setImageResource(homeBottom.getImageId());
        title.setText(homeBottom.getTitle());
        start_time.setText(homeBottom.getStartTime());
        initiator_name.setText(homeBottom.getInitiator());
        credit_value.setText(homeBottom.getCreditValue());
        money.setText(homeBottom.getMoney());
        return view;
    }
}
