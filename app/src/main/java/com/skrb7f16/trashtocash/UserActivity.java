package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.skrb7f16.trashtocash.databinding.ActivityUserBinding;
import com.skrb7f16.trashtocash.utilities.AddOtherTypesFragment;
import com.skrb7f16.trashtocash.utilities.LogoutAsk;
import com.skrb7f16.trashtocash.models.Feeds;
import com.skrb7f16.trashtocash.models.PARAMS;
import com.skrb7f16.trashtocash.models.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1;
    List<String> types;
    List<String> pics;
    List<String> beforeUploading;
    ActivityUserBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    User user;
    Boolean sell=false;
    int countImage=0;
    ImageView [] images=new ImageView[3];

    private static final int PICK_IMAGE=1;

    String filePath;
    Map config = new HashMap();

    private void configCloudinary() {
        config.put("cloud_name", "your cloud");
        config.put("api_key", "your api key");
        config.put("api_secret", "your api secret");
        MediaManager.init(UserActivity.this, config);
        PARAMS.initializedCloudinary=true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        binding= ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        getSupportActionBar().setTitle(auth.getCurrentUser().getDisplayName());
        database=FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        if(!PARAMS.initializedCloudinary)
        configCloudinary();
        progressDialog=new ProgressDialog(UserActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Your pics are uploading");
        user=new User(auth.getCurrentUser().getUid(),auth.getCurrentUser().getDisplayName());
        types= new ArrayList<>();
        pics=new ArrayList<>();
        beforeUploading=new ArrayList<>();
        types.add("Clothes");
        types.add("Food");
        types.add("Phone");
        types.add("Others");
        images[0]=binding.pic1;
        images[1]=binding.pic2;
        images[2]=binding.pic3;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,types);
        binding.dropStatus.setAdapter(adapter);
        binding.dropStatus.setPrompt("Select type");
        binding.dropStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(types.get(position).equals("Others")){
                    String t = "";
                    AddOtherTypesFragment dialog=new AddOtherTypesFragment(UserActivity.this);
                    dialog.show();
                    dialog.setDialogResult(new AddOtherTypesFragment.OnMyDialogResult(){
                        public void finish(String result){
                            types.add(result);
                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.price.setVisibility(View.VISIBLE);
                sell=true;
            }
        });
        binding.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.price.setVisibility(View.GONE);
                sell=false;
            }
        });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(checkFields()){
                    progressDialog.setMessage("Post uploading ");
                    progressDialog.show();
                    Feeds feeds=new Feeds();
                    feeds.setAddress(binding.postAddress.getText().toString());
                    feeds.setCity(binding.postCity.getText().toString());
                    feeds.setDesc(binding.postDesc.getText().toString());
                    feeds.setTitle(binding.postTitle.getText().toString());
                    feeds.setType(types.get(binding.dropStatus.getSelectedItemPosition()));
                    feeds.setDonate(!sell);
                    feeds.setAt(LocalDateTime.now().toString());

                    if(sell==true){
                        feeds.setPrice(Integer.parseInt(binding.price.getText().toString()));
                    }
                    else{
                        feeds.setPrice(0);
                    }
                    feeds.setPics(pics);
                    feeds.setBy(user.getUsername());
                    feeds.setById(user.getUserId());
                    String temp=database.getReference().child("feeds").push().getKey();
                    feeds.setId(temp);
                    database.getReference().child("feeds").child(temp).setValue(feeds).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.hide();
                            images[0].setVisibility(View.GONE);
                            images[1].setVisibility(View.GONE);
                            images[2].setVisibility(View.GONE);
                            pics.clear();
                            beforeUploading.clear();
                            binding.postAddress.setText("");
                            binding.postTitle.setText("");
                            binding.postDesc.setText("");
                            binding.postCity.setText("");
                            binding.price.setText(null);
                            binding.price.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        binding.addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countImage!=3){
                    countImage++;
                    requestPermission();

                }
            }
        });

        binding.myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this,UserFeedListActivity.class));
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutAsk logoutAsk=new LogoutAsk(UserActivity.this);
                logoutAsk.showDialog(UserActivity.this);
            }
        });

        binding.myCreds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this,MyCreditsActivity.class));
            }
        });
    }
    private void requestPermission(){
        if(ContextCompat.checkSelfPermission
                (UserActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ){
            accessTheGallery();

        } else {
            ActivityCompat.requestPermissions(
                    UserActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery();
            } else {
                Toast.makeText(UserActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void accessTheGallery(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    public boolean checkFields(){
        if(binding.postTitle.getText().toString().length()==0){
            binding.postTitle.setError("Title is required");
            return false;
        }
        if(binding.postCity.getText().toString().length()==0){
            binding.postCity.setError("City is required");
            return false;
        }
        if(binding.postAddress.getText().toString().length()==0){
            binding.postAddress.setError("Address is required");
            return false;
        }
        if(binding.postDesc.getText().toString().length()==0){
            binding.postTitle.setError("Description is required");
            return false;
        }

        if(pics.size()==0){
            Toast.makeText(UserActivity.this,"Pics required",Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get the image's file location
        filePath = getRealPathFromUri(data.getData(), UserActivity.this);
        beforeUploading.add(filePath);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            try {
                //set picked image to the mProfile
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                images[countImage-1].setImageBitmap(bitmap);
                images[countImage-1].setVisibility(View.VISIBLE);
                uploadToCloudinary(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity){
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);

        if(cursor==null) {
            return imageUri.getPath();
        }else{
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void uploadToCloudinary(String path){
        progressDialog.show();
        MediaManager.get().upload(path).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                progressDialog.setMessage("Staring uploading "+path);
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                progressDialog.setMessage("Uploading...("+bytes/1000+"/"+totalBytes/1000+")kb");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                pics.add(resultData.get("secure_url").toString());
                progressDialog.setMessage("Uploaded");
                progressDialog.hide();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {

            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }
}