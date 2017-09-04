package com.example.bangbangba;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomePageActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private List<HomeBottom> homeBottomList = new ArrayList<>();

    private HomeBottomAdapter adapter;

    private ListView listView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<HomeUp> homeUpList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获得token
        getTokenOfIM();

        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshGetAllOrders();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        getAllOrders();
        initGuide();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        HomeUpAdapter homeUpAdapter = new HomeUpAdapter(homeUpList);
        recyclerView.setAdapter(homeUpAdapter);
        homeUpAdapter.setOnItemClickListener(new HomeUpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HomeUp homeUp = homeUpList.get(position);
                if (homeUp.getGuide() == "全部") {
                    refreshGetAllOrders();
                } else if (homeUp.getGuide() == "跑腿") {
                    refreshGetRunOrders();
                } else if (homeUp.getGuide() == "悬赏提问") {
                    refreshGetAskOrders();
                } else if (homeUp.getGuide() == "学习辅导") {
                    refreshGetLearnOrders();
                } else if (homeUp.getGuide() == "技术服务") {
                    refreshGetTechniqueOrders();
                } else if (homeUp.getGuide() == "生活服务") {
                    refreshGetLifeOrders();
                } else {
                    refreshGetOtherOrders();
                }
            }
        });
        Intent intent = getIntent();
        final String phone = intent.getStringExtra("extra_phone");
        Button button2 = (Button) findViewById(R.id.star2);
        Button button3 = (Button) findViewById(R.id.star3);
        Button button4 = (Button) findViewById(R.id.star4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HomePageActivity.this, HelpModule.class);
                startActivity(intent1);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(HomePageActivity.this, OrderPageActivity.class);
                intent2.putExtra("extra_phone", phone);
                startActivity(intent2);
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

    //====================================================================================
    private void getTokenOfIM(){
        //得到token
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestbody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://bang.cloudshm.com/create")
                            .addHeader("phone",DemoCache.getAccount())
                            .post(requestbody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject2(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject2(String jsonData){
        try {

            Log.d("InTheParseMethod2",jsonData);

            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.getInt("code");

            //说明已经注册,需要更新token
            if (code == 414 ){
                updateToken();
            }else if(code == 200){
                JSONObject info = jsonObject.getJSONObject("info");
                String token = info.getString("token");
                String accid = info.getString("accid");
                DemoCache.setToken(token);
                DemoCache.setAccid(accid);
                login();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseJSONWithJSONObject3(String jsonData){
        try {
            Log.d("InTheParseMethod3",jsonData);

            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.getInt("code");

            if (code == 200){
                JSONObject info = jsonObject.getJSONObject("info");
                String token = info.getString("token");
                String accid = info.getString("accid");
                DemoCache.setToken(token );
                DemoCache.setAccid(accid);
                login();
            }

            Log.d("InTheParseMethod3",DemoCache.getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void login(){


        Log.d("InTheLogin",DemoCache.getAccid());
        Log.d("InTheLogin",DemoCache.getToken());

        LoginInfo info = new LoginInfo(DemoCache.getAccid(),DemoCache.getToken()); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        //Toast.makeText(HomePageActivity.this,"success!!!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        Toast.makeText(HomePageActivity.this,"聊天功能暂时不可用"+code,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    private void updateToken(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestbody = new FormBody.Builder()
                            .add("phone",DemoCache.getAccount())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://bang.cloudshm.com/update")
                            .put(requestbody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject3(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

//========================================================================================

    private void getAllOrders() {
        final String baseUrl = "http://bang.cloudshm.com/helpOthers/getAllOrders";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl)
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
            if (status == 200) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject new_jsonObject = data.getJSONObject(i);
                    int id = new_jsonObject.getInt("id");
                    String title = new_jsonObject.getString("title");
                    int category = new_jsonObject.getInt("category");
                    int state = new_jsonObject.getInt("state");
                    String money = new_jsonObject.getString("money");
                    String created_at = new_jsonObject.getString("created_at");
                    String name = new_jsonObject.getString("name");
                    int credit = new_jsonObject.getInt("credit");
                    HomeBottom homeBottom = new HomeBottom(R.drawable.img1, title,
                            "创建时间：" + created_at, "发起人：" + name, "信誉值：" + credit,
                            state, id, "￥" + money);
                    homeBottomList.add(homeBottom);
                    show();
                }
            } else {
                Toast.makeText(HomePageActivity.this, "异常！请重试！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new HomeBottomAdapter(HomePageActivity.this, R.layout.activity_home_page, homeBottomList);
                listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HomeBottom homeBottom = homeBottomList.get(position);
                        int m_id = homeBottom.getId();
                        Intent intent123 = new Intent(HomePageActivity.this, showDetail.class);
                        intent123.putExtra("extra_id", m_id);
                        Log.d("MARSHAL" , "" + m_id);
                        startActivity(intent123);
                        /**
                         * 启动点击活动代码在这里，把上面的intent更改以启动活动
                          */
                    }
                });
            }
        });
   }

    private void refreshGetAllOrders() {
        homeBottomList.clear();
        getAllOrders();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initGuide() {
        HomeUp homeUp1 = new HomeUp("全部");
        homeUpList.add(homeUp1);
        HomeUp homeUp2 = new HomeUp("跑腿");
        homeUpList.add(homeUp2);
        HomeUp homeUp3 = new HomeUp("悬赏提问");
        homeUpList.add(homeUp3);
        HomeUp homeUp4 = new HomeUp("学习辅导");
        homeUpList.add(homeUp4);
        HomeUp homeUp5 = new HomeUp("技术服务");
        homeUpList.add(homeUp5);
        HomeUp homeUp6 = new HomeUp("生活服务");
        homeUpList.add(homeUp6);
        HomeUp homeUp7 = new HomeUp("其他");
        homeUpList.add(homeUp7);
    }

    private void getAskOrders() {
        final String baseUrl = "http://bang.cloudshm.com/helpOthers/getAskOrders";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl)
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

    private void refreshGetAskOrders() {
        homeBottomList.clear();
        getAskOrders();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getLearnOrders() {
        final String baseUrl = "http://bang.cloudshm.com/helpOthers/getLearnOrders";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl)
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

    private void refreshGetLearnOrders() {
        homeBottomList.clear();
        getLearnOrders();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getTechniqueOrders() {
        final String baseUrl = "http://bang.cloudshm.com/helpOthers/getTechniqueOrders";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl)
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

    private void refreshGetTechniqueOrders() {
        homeBottomList.clear();
        getTechniqueOrders();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getLifeOrders() {
        final String baseUrl = "http://bang.cloudshm.com/helpOthers/getLifeOrders";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl)
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

    private void refreshGetLifeOrders() {
        homeBottomList.clear();
        getLifeOrders();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getOtherOrders() {
        final String baseUrl = "http://bang.cloudshm.com/helpOthers/getOtherOrders";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl)
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

    private void refreshGetOtherOrders() {
        homeBottomList.clear();
        getOtherOrders();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getRunOrders() {
        final String baseUrl = "http://bang.cloudshm.com/helpOthers/getRunOrders";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl)
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

    private void refreshGetRunOrders() {
        homeBottomList.clear();
        getRunOrders();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

}

