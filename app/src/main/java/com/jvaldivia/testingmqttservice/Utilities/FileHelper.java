package com.jvaldivia.testingmqttservice.Utilities;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jvaldivia.testingmqttservice.Controller.MainActivity;
import com.jvaldivia.testingmqttservice.Model.MqttMessageWrapper;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FileHelper {
    private  static final String TAG = "FileHelper";
    private Context context;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public FileHelper(Context context){
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public String save(MqttMessageWrapper[] data, int size){
        String msg0 = "";
        try{
            int stamp = (int) (System.currentTimeMillis() /1000);
            String filename = "mqtt-" + MainActivity.CLIENT_ID + "-" + stamp + ".csv";
            File path = context.getFilesDir();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (int i = 0; i < data.length; i++){
                MqttMessageWrapper loc = data[i];
                if (loc != null) {
                    String line = loc.getUid() + ";" + loc.getOrderSend() + ";" + loc.getOrderArrive() + ";" + loc.getTimeInit() + ";"
                            + loc.getTimeEnd() + "\n";

                    byte[] strToBytes = line.getBytes();
                    outputStream.write(strToBytes);
                }
            }
            outputStream.close();
            uploadFile(outputStream.toByteArray(), filename);

        }catch(Exception e){
            e.printStackTrace();
        }
        return msg0;
    }

    private void uploadFile(byte[] data, String filename) {
        StorageReference ref = storageReference.child("iot/" + filename);
        ref.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
