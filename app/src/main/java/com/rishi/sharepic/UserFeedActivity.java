package com.rishi.sharepic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserFeedActivity extends AppCompatActivity implements RecyclerOnItemClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<Bitmap> bitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        setTitle(username+"'s Feeds");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        bitmaps = new ArrayList<>();
        recyclerView.setLayoutManager(mLayoutManager);

        final FeedRecyclerViewAdapter recyclerViewAdapter=new FeedRecyclerViewAdapter(bitmaps, new RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {

            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);


        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Image");
        parseQuery.whereEqualTo("username",username);
        parseQuery.orderByDescending("createdAt");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e==null){

                    Log.e("OBJECTS SIZE", Integer.toString(objects.size()));

                    if(objects.size() > 0)
                    {

                        for(ParseObject parseObject : objects){

                            ParseFile file = (ParseFile)parseObject.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {


                                    if(e == null && data !=null){
                                        Log.e("Condition","Satisfied");
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        bitmaps.add(bitmap);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                        Log.e("RECYCLERVIEW SIZE", Integer.toString(recyclerViewAdapter.getItemCount()));
                                    }
                                    else{
                                        Log.e("Condition","Not Satisfied");
                                    }
                                }
                            });
                        }



                    }
                }
            }
        });




        
    }

    @Override
    public void onItemClick(View childView, int position) {

    }


}
