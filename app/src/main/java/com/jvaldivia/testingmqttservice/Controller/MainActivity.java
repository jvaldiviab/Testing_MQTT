package com.jvaldivia.testingmqttservice.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jvaldivia.testingmqttservice.R;
import com.jvaldivia.testingmqttservice.Utilities.ToolHelper;

import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements MainActivityListener {

    private final static String TAG = "MainActivity";
    public final static String CLIENT_ID = UUID.randomUUID().toString();
    private MqttBroadcastReceiver mqttBroadcastReceiver;
    private TextView txtDisplay;
    private TextView txtAction;
    private EditText edtTopic;
    private EditText edtQos;
    private EditText edtNumMessage;
    private EditText edtDelay;

    /* Nuevos atributos */
    private EditText edtType;
    private EditText edtMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttBroadcastReceiver = new MqttBroadcastReceiver(MainActivity.this);

        txtAction = (TextView) findViewById(R.id.txtAction);
        txtDisplay = (TextView) findViewById(R.id.txtDisplay);
        edtTopic = (EditText) findViewById(R.id.edtTopic);
        edtQos = (EditText) findViewById(R.id.edtQos);
        edtNumMessage = (EditText) findViewById(R.id.edtNumMessage);
        edtDelay = (EditText) findViewById(R.id.edtDelay);

        /* Nuevos atributos */
        edtType = (EditText) findViewById(R.id.edtType);
        edtMessage =(EditText) findViewById(R.id.edtMessage);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnPublish = (Button) findViewById(R.id.btnPublish);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "stoping...");
                    initMqttService(MqttIntentService.ACTION_STOP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    initMqttService(MqttIntentService.ACTION_START);
                    txtAction.setText("Connected");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    initMqttService(MqttIntentService.ACTION_PUBLISH);
                    String datetime2 = ToolHelper.getDateTime();
                    ToolHelper.setPublishBegin(getApplicationContext(), datetime2);
                    txtAction.setText("Started at: " + datetime2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    initMqttService(MqttIntentService.ACTION_SAVE);
                    txtAction.setText("Saved");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MqttBroadcastReceiver.ACTION_MESSAGE);
        registerReceiver(mqttBroadcastReceiver, intentFilter);

        String datetime2 = ToolHelper.getPublishBegin(getApplicationContext());
        txtAction.setText(datetime2);

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mqttBroadcastReceiver);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void display(String data) {
        txtDisplay.setText(data);
    }


    private void initMqttService(String action) {
        String topic = edtType.getText().toString();
        int qos = Integer.parseInt(edtQos.getText().toString());
        int delay = Integer.parseInt(edtDelay.getText().toString());
        int size = Integer.parseInt(edtNumMessage.getText().toString());

        /*Nuevos atributos*/
        String type = edtType.getText().toString();
        String message = edtMessage.getText().toString();


        Intent intent = new Intent(MainActivity.this, MqttHelperService.class);
        intent.putExtra(MqttIntentService.TOPIC, topic);
        intent.putExtra(MqttIntentService.QOS, qos);
        intent.putExtra(MqttIntentService.DELAY, delay);
        intent.putExtra(MqttIntentService.DATA, size);

        intent.putExtra(MqttIntentService.ELEMENT, type);
        intent.putExtra(MqttIntentService.MESSAGE, message);

        intent.setAction(action);
        startService(intent);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }*/

    }


}
