package com.example.bangbangba.Message.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bangbangba.Message.Adapter.MsgAdapter;
import com.example.bangbangba.Message.Msg;
import com.example.bangbangba.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;



public class ChattingActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    private String targetID;

    private String  messageInIntent;

    private void parseNotifyIntent(Intent intent){
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
        //Toast.makeText(ChattingActivity.this,"进入了intent解析",Toast.LENGTH_SHORT).show();

        for(IMMessage message: messages ){

           //Log.d("testIntent", message.getSessionId());
           // Log.d("testIntent", message.getContent());

            targetID = message.getSessionId();

            messageInIntent = message.getContent();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
    }

    Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {

                @Override
                public void onEvent(List<IMMessage> messages) {
                    // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。

                    for(IMMessage message: messages ){
                        Msg msg = new Msg(message.getContent(), Msg.TYPE_RECEIVED);
                        msgList.add(msg);
                        adapter.notifyItemInserted(msgList.size() - 1);
                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    }
                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)){

            parseNotifyIntent(intent);
        }else{
            targetID = intent.getStringExtra("targetID");
        }




        TextView myChatter = (TextView)findViewById(R.id.my_chatter);
        myChatter.setText(targetID);

        //注册消息观察者
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);



        //这里是写聊天界面的toolbaar的
        Toolbar toobar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toobar);

        //接下来设置一下左上角的返回图标
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }


        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button)findViewById(R.id.send_message);
        msgRecyclerView = (RecyclerView) findViewById(R.id.chatting_panel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        if (messageInIntent != null){
            Msg msg = new Msg(messageInIntent, Msg.TYPE_RECEIVED);
            msgList.add(msg);
            adapter.notifyItemInserted(msgList.size() - 1);
            msgRecyclerView.scrollToPosition(msgList.size() - 1);
        }



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if ( !"".equals(content)){


                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    inputText.setText("");

                    SessionTypeEnum sessionType = SessionTypeEnum.P2P;
                    IMMessage textMessage = MessageBuilder.createTextMessage(targetID, sessionType, content);
                    NIMClient.getService(MsgService.class).sendMessage(textMessage, false).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            Toast.makeText(ChattingActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            Toast.makeText(ChattingActivity.this,"发送失败:"+code,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });

                }
            }
        });

    }

    /*private void initMsgs(){
        Msg msg1 = new Msg("Hello", Msg.TYPE_RECEIVED);
        msgList.add(msg1);

        Msg msg2 = new Msg("Hello", Msg.TYPE_SENT);
        msgList.add(msg2);

        Msg msg3 = new Msg("Hello", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }*/

        //设置了返回的功能,添加toobar上面右边的删除好友 更改备注项的点击功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.delete_friend:
                break;
            case R.id.change_nickname:
                break;

            default:
        }
        return true;
    }

    //添加toobar上面右边的删除好友 更改备注项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chattingmessagetoolbar,menu);
        return true;
    }





}
