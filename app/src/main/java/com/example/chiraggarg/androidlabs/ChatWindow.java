package com.example.chiraggarg.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import java.util.ArrayList;

public class ChatWindow extends Activity {
private EditText Edit;
private Button buttonSend;
private ListView list;
private TextView message;
ArrayList<String> messageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        list =(ListView) findViewById( R.id.ListView);
        Edit = (EditText) findViewById(R.id.editText2);
        buttonSend =(findViewById(R.id.button4));
        final ChatAdapter messageAdapter = new ChatAdapter(this, messageList);
     list.setAdapter(messageAdapter);
        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String message = Edit.getText().toString();
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();
                Edit.setText("");
            }
        });
        }
    private class ChatAdapter extends BaseAdapter {
        private ArrayList chatMessages;
        private Context ctx;

        public ChatAdapter(Context ctx, ArrayList chatMessages) {
            this.ctx = ctx;
            this.chatMessages = chatMessages;
        }

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public String getItem(int position) {
            return messageList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }









}
