package com.example.chiraggarg.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    private static final String ACTIVITY_NAME = "ChatWindow";
private EditText Edit;
private Button buttonSend;
private ListView list;
private TextView message;
ArrayList<String> messageList = new ArrayList<>();
    private static SQLiteDatabase chatDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Resources resources = getResources();
        Context context = getApplicationContext();

        list =(ListView) findViewById( R.id.ListView);
        Edit = (EditText) findViewById(R.id.editText2);
        buttonSend =(findViewById(R.id.button4));
        final ChatAdapter messageAdapter = new ChatAdapter(this, messageList);
         list.setAdapter(messageAdapter);

        ChatDatabaseHelper chatDBHelper = new ChatDatabaseHelper(context);
        chatDB = chatDBHelper.getWritableDatabase();
        final ContentValues cValues = new ContentValues();

        try (Cursor cursor = chatDB.query(ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE}, null, null, null, null, null)) {

            if (cursor.moveToFirst()) {
                do {
                    String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                    messageList.add(message);
                    Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }

            Log.i(ACTIVITY_NAME, "Cursor's column count=" + cursor.getColumnCount());
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
            }
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String message = Edit.getText().toString();
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();
                Edit.setText("");
                cValues.put("message", message);
                chatDB.insert(ChatDatabaseHelper.TABLE_NAME, null, cValues);

            }
        });
        }
    protected void onDestroy() {
        super.onDestroy();
        chatDB.close();
    }
    private class ChatAdapter extends BaseAdapter {
        private ArrayList chatMessages;
        private Context ctx;

        public ChatAdapter(Context ctx, ArrayList chatMessages) {
            this.ctx = ctx;
            this.chatMessages = chatMessages;
        }

        @Override
        public int getCount()
        {

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
