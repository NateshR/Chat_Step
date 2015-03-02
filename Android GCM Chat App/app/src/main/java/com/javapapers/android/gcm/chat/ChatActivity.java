package com.javapapers.android.gcm.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Random;

public class ChatActivity extends Activity {
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
	private ListView listView;
	private EditText chatText;
    private Button buttonSend;

    GoogleCloudMessaging gcm;
    Intent intent;

	private static Random random;
    private String toUserName;
    MessageSender messageSender;

    ChatSQL chatSQL;
    boolean oneTime = true;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Intent i = getIntent();
        toUserName = i.getStringExtra("TOUSER");
		setContentView(R.layout.activity_chat);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        intent = new Intent(this, GCMNotificationIntentService.class);
        registerReceiver(broadcastReceiver, new IntentFilter("com.javapapers.android.gcm.chat.chatmessage"));

		random = new Random();
        messageSender = new MessageSender();
		listView = (ListView) findViewById(R.id.listView1);
        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

		chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);

        if(oneTime) {
            chatSQL = new ChatSQL(ChatActivity.this,"chatDatabase",i.getStringExtra("DATABASE_TABLE"),1);
            chatSQL.CreateTable();
            oneTime=false;
        }

        //Get Values
        String[] chat_text = chatSQL.getChatText();
        String[] chat_gravity_string = chatSQL.getChatGravity();

        for(int j=0;j<chat_text.length;j++)
        {
            boolean chat_gravity_boolean = Boolean.valueOf(chat_gravity_string[j]);
            chatArrayAdapter.add(new ChatMessage(chat_gravity_boolean,chat_text[j]));
            Log.d("Chat_text",chat_text[0]);

        }
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  return sendChatMessage();
                }
                return false;
			}
		});
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
	}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(ChatActivity.this,UserListActivity.class);
        startActivity(intent1);
        finish();
    }

    private boolean sendChatMessage(){
        //sending gcm message to the paired device
        Bundle dataBundle = new Bundle();
        dataBundle.putString("ACTION", "CHAT");
        dataBundle.putString("TOUSER", toUserName);
        dataBundle.putString("CHATMESSAGE", chatText.getText().toString());
        messageSender.sendMessage(dataBundle,gcm);

        //Enter into database
        chatSQL.CreateEntry(chatText.getText().toString(),"false");
        onCreate(new Bundle());




        //updating the current device
        //chatArrayAdapter.add(new ChatMessage(false, chatText.getText().toString()));
        chatText.setText("");
        return true;
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getStringExtra("CHATMESSAGE"));
            //Enter into database
            chatSQL.CreateEntry(intent.getStringExtra("CHATMESSAGE"),"true");
            onCreate(new Bundle());
        }
    };

}