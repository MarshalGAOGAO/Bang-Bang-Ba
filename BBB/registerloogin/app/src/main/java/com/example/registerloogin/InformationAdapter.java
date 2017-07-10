package com.example.registerloogin;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Marshal Gao on 2017/7/4.
 */

public class InformationAdapter extends ArrayAdapter<OrderInformation> {

    private int resourceId;

    private int m_status;

    OrderPageActivity context;

    private String phone;

    public Handler handler;

    public InformationAdapter(Context context, int textViewResourceId,
                              List<OrderInformation> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        handler = new Handler() {
            public void handleMessage(Message msg)
            {
                if (msg.what == 100)
                {
                    phone = msg.getData().getString("phone");
                }
            }
        };
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        OrderInformation orderInformation = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        ImageView infoImage = (ImageView) view.findViewById(R.id.info_image);
        TextView orderMoney = (TextView) view.findViewById(R.id.order_money);
        TextView orderTitle = (TextView) view.findViewById(R.id.order_title);
        TextView orderstate = (TextView) view.findViewById(R.id.order_state);
        TextView orderStartTime = (TextView) view.findViewById(R.id.order_start_time);
        final Button orderCheck = (Button) view.findViewById(R.id.order_check);
        infoImage.setImageResource(orderInformation.getImageId());
        orderMoney.setText(orderInformation.getMoney());
        orderTitle.setText(orderInformation.getTitleOrderInformation());
        orderstate.setText(orderInformation.getState());
        orderStartTime.setText(orderInformation.getStartTime());
        orderCheck.setText(orderInformation.getButtonText());
        orderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderCheck.getText().toString() == "确认完成") {
                    int need = (int)InformationAdapter.this.getItemId(position);
                    finishService(phone /**, context.token**/, InformationAdapter.this.getItem(need).getId());
                    if (m_status == 200) {
                        orderCheck.setText("评价");
                        int id = InformationAdapter.this.getItem(need).getId();
                        Intent m_intent = new Intent(getContext(), EvaluateActivity.class);
                        m_intent.putExtra("extra_id", id);
                        getContext().startActivity(m_intent);

                    } else {
                        Toast.makeText(getContext(), "确认订单失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                } else if (orderCheck.getText().toString() == "评价") {
                     int need = (int)InformationAdapter.this.getItemId(position);
                     int id = InformationAdapter.this.getItem(need).getId();
                     Intent m_intent = new Intent(getContext(),EvaluateActivity.class);
                     m_intent.putExtra("extra_id", id);
                     getContext().startActivity(m_intent);

                } else if (orderCheck.getText().toString() == "取消订单"){
                    Toast.makeText(getContext(), "HAPPY", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void finishService(final String phone/**, final String token**/, final int id) {

        String baseUrl = "http://Bang.cloudshm.com/order/finishService";
        final String my_url = baseUrl + "?phone=" + phone /**+ "&token=" + token**/ + "&id=" + id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(my_url)
                            .addHeader("phone", phone)
                            //.addHeader("token", token)
                            .addHeader("id","" + id)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int status = jsonObject.getInt("status");
            switch (status) {
                case 200:
                    m_status = 200;
                    break;
                case 400:
                    m_status = 400;
                    break;
                case 404:
                    m_status = 404;
                    break;
                case 402:
                    m_status = 402;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

