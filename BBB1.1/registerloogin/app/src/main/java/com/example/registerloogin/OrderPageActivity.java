package com.example.registerloogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

    private InformationAdapter adapter;

    private ListView listView;

    private DrawerLayout mDrawerLayout;

    private SwipeRefreshLayout swipeRefresh;

    private String phone;

    private int m_id;

    private String token;

    private int my_state;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshOrders();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        Intent intent = getIntent();
        phone = intent.getStringExtra("extra_phone");
        getOrders(phone/**, token**/);
        Button button1 = (Button) findViewById(R.id.star1);
        Button button2 = (Button) findViewById(R.id.star2);
        Button button4 = (Button) findViewById(R.id.star4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderPageActivity.this, HomePageActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(OrderPageActivity.this, HelpModule.class);
                startActivity(intent2);
                finish();
            }
        });
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
                adapter = new InformationAdapter(OrderPageActivity.this,
                        R.layout.order_information, orderInformationList);
                listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);
                listView.setItemsCanFocus(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        OrderInformation orderInfo = orderInformationList.get(position);
                        m_id = orderInfo.getId();
                        Intent new_intent = new Intent(OrderPageActivity.this, orderDetail1.class);
                        new_intent.putExtra("extra_id", m_id);
                        new_intent.putExtra("extra_phone", phone);
                        new_intent.putExtra("extra_state", my_state);
                        startActivity(new_intent);
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
                        case 1:
                            stringState = "等待接单";
                            buttonText = "可取消";
                            break;
                        case 2:
                            stringState = "正在服务";
                            buttonText = "确认完成";
                            break;
                        case 3:
                            stringState = "服务完成";
                            buttonText = "评价";
                            break;
                        case 4:
                            stringState = "服务完成";
                            buttonText = "已完成";
                        default:
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
                            stringState, "创建时间：" + created_at, "￥" + money, buttonText, id, state);
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

    public void refreshOrders() {
        orderInformationList.clear();
        getOrders(phone/**, token**/);
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

}
