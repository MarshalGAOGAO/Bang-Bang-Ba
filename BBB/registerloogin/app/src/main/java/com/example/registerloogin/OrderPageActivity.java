package com.example.registerloogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Marshal Gao on 2017/7/4.
 */

public class OrderPageActivity extends AppCompatActivity {

    private List<OrderInformation> orderInformationList = new ArrayList<>();

    private ListView listView;

    private DrawerLayout mDrawerLayout;

    private String phone;

    private int m_id;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        Intent intent = getIntent();
        phone = intent.getStringExtra("extra_phone");
        getOrders(phone/**, token**/);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    private void getOrders(final String phone/**, final String token**/) {

        String baseUrl = "http://Bang.cloudshm.com/order/getOrders";
        final String my_url = baseUrl + "?phone=" + phone /**+ "&token=" + token**/;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(my_url)
                            .addHeader("phone", phone)
                            //.addHeader("token", token)
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

    private void show() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InformationAdapter adapter = new InformationAdapter(OrderPageActivity.this,
                        R.layout.order_information, orderInformationList);
                listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);
                listView.setItemsCanFocus(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        OrderInformation orderInfo = orderInformationList.get(position);
                        m_id = orderInfo.getId();
                        /**泽豪把活动填在这里
                         * Intent new_intent = new Intent(OrderPageActivity.this, ??????);
                         * new_intent.putExtra("extra_id", m_id);
                         * startActivity(new_intent);
                         */
                    }
                });
                Message msg = new Message();
                msg.what = 100;
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                bundle.putInt("id", m_id);
                msg.setData(bundle);
                adapter.handler.sendMessage(msg);
            }
        });
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            String stringState = "";
            String buttonText = "";
            JSONObject jsonObject = new JSONObject(jsonData);
            int status = jsonObject.getInt("status");
            if (status == 200) {
                JSONArray data = jsonObject.getJSONArray("data");
                //Log.d("MARSHAL", "" + data);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject new_jsonObject = data.getJSONObject(i);
                    int id = new_jsonObject.getInt("id");
                    String title = new_jsonObject.getString("title");
                    int category = new_jsonObject.getInt("category");
                    int state = new_jsonObject.getInt("state");
                    switch (state) {
                        case 0:
                            stringState = "等待接单";
                            buttonText = "取消订单";
                            break;
                        case 1:
                            stringState = "正在服务";
                            buttonText = "确认完成";
                            break;
                        case 2:
                            stringState = "服务完成";
                            buttonText = "评价";
                            break;
                        default:
                            stringState = "服务完成";
                            buttonText = "已完成";
                            break;
                    }
                    String content = new_jsonObject.getString("content");
                    String close_time = new_jsonObject.getString("close_time");
                    String money = new_jsonObject.getString("money");
                    String created_at = new_jsonObject.getString("created_at");
                    String updated_at = new_jsonObject.getString("updated_at");
                    String applicant = new_jsonObject.getString("applicant");
                    String servant = new_jsonObject.getString("servant");
                    OrderInformation orderInformation = new OrderInformation(R.drawable.img1, title,
                            stringState, "创建时间：" + created_at, "￥" + money, buttonText, id);
                    orderInformationList.add(orderInformation);
                    show();
                }
            } else {
                Toast.makeText(OrderPageActivity.this, "该用户不存在！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
