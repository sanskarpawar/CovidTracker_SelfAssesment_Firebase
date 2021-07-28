package com.example.covid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.vrgsoft.layoutmanager.RollingLayoutManager;

public class NewsActivity extends Fragment {

    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase, Newsref,mDatabasetext;
    private FirebaseRecyclerAdapter<News, NewsViewHolder> mPeopleRVAdapter;
    ImageButton SearchButton;
    EditText SearchInputText;
    private DatabaseReference UserRef, RootRef, personsRef, UserRef1,updateref;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private View NewsFragment;
    private TextView mPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        NewsFragment = inflater.inflate(R.layout.activity_news, null);

        mPlayer = (TextView) NewsFragment.findViewById(R.id.news_textView_field);;

        UserRef = FirebaseDatabase.getInstance().getReference().child("News");
        updateref = FirebaseDatabase.getInstance().getReference().child("update");

        UserRef.keepSynced(true);
        updateref.keepSynced(true);
        mDatabasetext = FirebaseDatabase.getInstance().getReference().child("Text");
        mDatabasetext.keepSynced(true);

        //UserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth = FirebaseAuth.getInstance();

        RootRef = FirebaseDatabase.getInstance().getReference();

        //"News" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
        mDatabase.keepSynced(true);

        mPeopleRV = (RecyclerView) NewsFragment.findViewById(R.id.myRecycleView);

        RollingLayoutManager rollingLayoutManager = new RollingLayoutManager(getContext());
        mPeopleRV.setLayoutManager(rollingLayoutManager);

        personsRef = FirebaseDatabase.getInstance().getReference().child("News");
        // Query personsQuery = personsRef.orderByKey();
        //TextView tv = getView().findViewById(R.id.news_textView_field);
        //Tvg.change(tv, Color.parseColor("#800CDD"),  Color.parseColor("#3BA3F2"));







        RetriveUserInfo();
        RetriveText();
                mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<News>().setQuery(mDatabase, News.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<News, NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(NewsActivity.NewsViewHolder holder, final int position, final News model) {
                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());
                holder.setImage(getActivity().getBaseContext(), model.getImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String url = model.getUrl();
                        Intent intent = new Intent(getActivity().getApplicationContext(), NewsWebView.class);
                        intent.putExtra("id", url);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public NewsActivity.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_row, parent, false);

                return new NewsActivity.NewsViewHolder(view);
            }
        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);
return NewsFragment;

    }

   /* @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }*/


    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);
        }

    }

    @Override
    public void onStart() {
        super.onStart();


        mPeopleRVAdapter.startListening();

    }

    private void RetriveUserInfo()
    {
        updateref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if((dataSnapshot.exists()))
                        {
                            final String retrivetip =dataSnapshot.getValue().toString();


                           mPlayer.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v){


                                    Uri uri = Uri.parse(retrivetip);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    startActivity(intent);

                                }


                            });


                        }

                        else
                        {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void RetriveText()
    {
        mDatabasetext.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if((dataSnapshot.exists()))
                {
                    final String retrivetip =dataSnapshot.getValue().toString();


                    String playerName = dataSnapshot.getValue().toString();
                    // String names=dataSnapshot.child("userUsername").getValue().toString();

                    mPlayer.setText(playerName);







                }

                else
                {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}



