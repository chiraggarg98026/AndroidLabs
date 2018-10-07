package com.example.chiraggarg.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    private static final String ACTIVITY_NAME="LoginActivity";

  private SharedPreferences sharedPref;
  private Button button;
  private EditText loginEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences("AndroidLabsPref", Context.MODE_PRIVATE);
        String defaultEmail = sharedPref.getString("DefaultMail", "Chirag@domain.com");

        loginEditText = findViewById(R.id.editText);
        loginEditText.setText(defaultEmail);

        button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEditText.getText().toString();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("DefaultMail", email);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void OnResume(){
        super.onResume();
       Log.i(ACTIVITY_NAME,"In onResume()");
    }
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
