package com.example.currconv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String c1,c2;
    double conv=1;
    double x = 1;
    double y=1;
    boolean counter;
    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String result="";
            URL url;
            HttpURLConnection c=null;
            try{
                url=new URL(params[0]);
                c=(HttpURLConnection)url.openConnection();
                InputStream inputStream=c.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result=result+current;
                    data=reader.read();
                }
                Log.i("Result",result);
                return result;

            }
            catch(Exception e){
                e.printStackTrace();
                return "FAILED";

            }
        }

    }
    public void onPress()
    {
        TextView t1= (EditText) findViewById(R.id.mt1);
        t1.setText(Double.toString(x));
        TextView t2= (EditText) findViewById(R.id.mt2);
        t2.setText(Double.toString(y));
        t1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                counter=true;
                System.out.println(counter);
                return false;
            }
        });

        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!t1.getText().toString().isEmpty())
                {
                    String text = t1.getText().toString();
                    x=Double.parseDouble(text);
                    y=x*conv;
                    if(counter==true)
                    {

                        t2.setText(Double.toString(y));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        t2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                counter=false;
                return false;
            }
        });
        t2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!t2.getText().toString().isEmpty())
                {
                    String text = t2.getText().toString();
                    y=Double.parseDouble(text);
                    x=y/conv;
                    if(counter==false)
                    {
                        t1.setText(Double.toString(x));
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        System.out.println("asdasfasd");
        char[] c=c1.toCharArray();
        c1="";
        for(int i=0;i<=2;i++)
        {
            c1=c1+c[i];
        }
        c=c2.toCharArray();
        c2="";
        for(int i=0;i<=2;i++)
        {
            c2=c2+c[i];
        }
        System.out.println(c1);
        System.out.println(c2);
        DownloadTask task=new DownloadTask();
        String result=null;
        try {
            result=task.execute("https://v6.exchangerate-api.com/v6/064a21581f9611c988bdb592/pair/"+c1+"/"+c2).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject j=null;
        try {
            j = new JSONObject(result);
            String str=j.getString("conversion_rate");
            String s = str.toString();
            conv= Double.parseDouble(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(conv);
        TextView t=(TextView) findViewById(R.id.mt1);
        String text = t.getText().toString();
        x=Double.parseDouble(text);
        t=(TextView) findViewById(R.id.mt2);
        text=t.getText().toString();
        y=Double.parseDouble(text);
        y=conv*x;
        t.setText(Double.toString(y));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c1 = "Select Currency";
        c2 = "Select Currency";
        SearchableSpinner s1, s2;
        s1 = findViewById(R.id.fspin);
        s2 = findViewById(R.id.sspin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c1 = parent.getItemAtPosition(position).toString();
                Log.i("String1:", c1);
                if ((!c1.equals("Select Currency")) && (!c2.equals("Select Currency")))
                    onPress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        s2.setAdapter(adapter);
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c2 = parent.getItemAtPosition(position).toString();
                Log.i("String2:", c2);
                if ((!c1.equals("Select Currency")) && (!c2.equals("Select Currency")))
                    onPress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}