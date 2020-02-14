package com.telran.a13_02_20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    HttpProvider httpProvider = new HttpProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Gson gson = new Gson();
//
//        Contact contact = gson.fromJson(getJson(),Contact.class);
//        Log.d("MY_TAG", "onCreate: " + contact);
//
//        String json = gson.toJson(contact);
//        Log.d("MY_TAG", "onCreate: " + json);

        findViewById(R.id.regBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegTask("dev.sheygam555@mail.com","Aa12345~").execute();
            }
        });

    }


    public String getJson(){
        return "{\n" +
                "      \"id\": 3241,\n" +
                "      \"name\": \"Dimitriy\",\n" +
                "      \"lastName\": \"Denisovich\",\n" +
                "      \"email\": \"dimitriy@mail.com\",\n" +
                "      \"phone\": \"0544444444\",\n" +
                "      \"address\": \"Ashqelon\",\n" +
                "      \"description\": \"Denis best friend\"\n" +
                "    }";
    }

    class RegTask extends AsyncTask<Void,Void,String>{
        String email,password;
        boolean isSuccess = true;

        public RegTask(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res = "Registration OK!";
            try {
                String token = httpProvider.registration(email,password);
                //save token to sharedpreferances
                Log.d("MY_TAG", "doInBackground: " + token);
            } catch (IOException e) {
                e.printStackTrace();
                isSuccess = false;
                res = "Connection error! Check your internet!";
            } catch (RuntimeException e){
                isSuccess = false;
                res = e.getMessage();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            if(isSuccess){
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }else{
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage(s)
                        .setPositiveButton("Ok",null)
                        .setCancelable(false)
                        .create()
                        .show();
            }
        }
    }
}
