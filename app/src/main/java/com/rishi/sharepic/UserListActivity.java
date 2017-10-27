package com.rishi.sharepic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity  {

    private static final int READ_EXT = 3;
    private static final int GET_PHOTO = 4;
    ArrayList <String> list;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXT);
            }
            else
            {
                    getPhoto();
            }

        }
        if(item.getItemId() == R.id.logout){
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getApplicationContext(),"Successfully Logged Out",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UserListActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Failed to Log out" ,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return  super.onOptionsItemSelected(item);
    }

    private void getPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GET_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_PHOTO && resultCode==RESULT_OK && data!=null){
            Uri selectedImage = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);


                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();


                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteStream);

                byte [] byteArray = byteStream.toByteArray();

                ParseFile file = new ParseFile("image.png",byteArray);
                ParseObject object= new ParseObject("Image");
                object.put("image", file);
                object.put("username", ParseUser.getCurrentUser().getUsername());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Failure" ,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                Log.e("TAG CATCH", e.toString());
            } catch (IOException e) {
                Log.e("TAG CATCH", e.toString());
            }
        }

        else{

            Log.e("TAG" , "onActivityResult Else");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            getPhoto();
        }
        else{

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list = new ArrayList<>();
        recyclerView.setLayoutManager(mLayoutManager);
        final UserRecyclerViewAdapter recyclerViewAdapter=new UserRecyclerViewAdapter(list, new RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                Log.e("TAG",position +"");
                Intent intent = new Intent(UserListActivity.this, UserFeedActivity.class);
                intent.putExtra("username",list.get(position));
                startActivity(intent);
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);


        ParseQuery<ParseUser> query= ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
                                   @Override
                                   public void done(List<ParseUser> objects, ParseException e) {
                                            if(e == null){
                                                if(objects.size() > 0){
                                                    Log.e("TAG","No error found");
                                                    for (ParseUser parseUser : objects){
                                                        list.add(parseUser.getUsername());
                                                    }
                                                    recyclerViewAdapter.notifyDataSetChanged();

                                                }
                                            }
                                            else{
                                                Log.e("TAG","error found");
                                            }
                                   }
                               });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();;
    }
}
