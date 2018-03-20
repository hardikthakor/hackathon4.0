package com.example.dell.authorityapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.view.ChatView;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

public class watson_chat extends AppCompatActivity {

    ChatView mChatView;

    Bitmap icon1, icon2;
    String myName = "Authority";
    String yourName = "Reacto Bot";

    int myId = 0;
    int yourId = 1;

    UserWatson you, me;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watson_chat);

        mChatView = findViewById(R.id.msg_view);

        icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person_black_24dp);
        icon2 = BitmapFactory.decodeResource(getResources(), R.drawable.dribblinkbot);

        me = new UserWatson(myId, myName, icon1);
        you = new UserWatson(yourId, yourName, icon2);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        //mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setBackground(getResources().getDrawable(R.drawable.bg));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("type a message");
        mChatView.setMessageMarginTop(2);
        mChatView.setMessageMarginBottom(2);

        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });
    }

    public void sendmessage(){

        final ConversationService myConversationService = new ConversationService("2017-05-26", getString(R.string.username), getString(R.string.password));

        //new message
        com.github.bassaer.chatmessageview.model.Message message = new com.github.bassaer.chatmessageview.model.Message.Builder()
                .setUser(me)
                .setRightMessage(true)
                .setMessageText(mChatView.getInputText())
                .hideIcon(true)
                .build();
        //Set to chat view
        mChatView.send(message);
        // Optionally, clear edittext
        MessageRequest request = new MessageRequest.Builder()
                .inputText(message.getMessageText())
                .build();
        mChatView.setInputText("");

        myConversationService.message(getString(R.string.workspace), request).enqueue(new ServiceCallback<MessageResponse>() {
            @Override
            public void onResponse(MessageResponse response) {
                // More code here
                final String outputText = response.getText().get(0);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        final com.github.bassaer.chatmessageview.model.Message receivedMessage = new com.github.bassaer.chatmessageview.model.Message.Builder()
                                .setUser(you)
                                .setRightMessage(false)
                                .setMessageText(outputText)
                                .build();
                        mChatView.receive(receivedMessage);
                    }
                });

                if(response.getIntents().get(0).getIntent().endsWith("RequestQuote")) {
                    // More code here
                }
            }

            @Override
            public void onFailure(Exception e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}
