package com.example.covid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.material.tabs.TabLayout;

import androidx.core.app.SharedElementCallback;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pd.chocobar.ChocoBar;
import com.rahimlis.badgedtablayout.BadgedTabLayout;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.trenzlr.firebasenotificationhelper.FirebaseNotificationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.trenzlr.firebasenotificationhelper.Constants.KEY_TEXT;
import static com.trenzlr.firebasenotificationhelper.Constants.KEY_TITLE;


public class MainActivity extends AppCompatActivity{
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;
    BadgedTabLayout badgedTabLayout;
    private DatabaseReference mDatabasepop,mDatabasepop2,mDatabaseDonate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mToolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Covid Tracker");
        getSupportActionBar().setIcon(R.drawable.ic_collaboration);

        mDatabasepop = FirebaseDatabase.getInstance().getReference().child("dialog");
        mDatabasepop2 = FirebaseDatabase.getInstance().getReference().child("popnoti");
        mDatabaseDonate = FirebaseDatabase.getInstance().getReference().child("donate");
        mDatabasepop.keepSynced(true);
        mDatabasepop2.keepSynced(true);
        mDatabaseDonate.keepSynced(true);

        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        //myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        // myTabLayout.setupWithViewPager(myViewPager);
        final BadgedTabLayout myTabLayout = (BadgedTabLayout) findViewById(R.id.tabs);
        myTabLayout.setupWithViewPager(myViewPager);

        // myTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //myTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        myTabLayout.setIcon(0, R.drawable.dashboard);
        myTabLayout.setIcon(2, R.drawable.news);
        myTabLayout.setIcon(1, R.drawable.research);


        if (InternetConnection.checkConnection(getApplicationContext())) {
            // Internet Available...
        }
        else
        {
            // Internet Not Available...
            ChocoBar.builder().setActivity(MainActivity.this)
                    .setText("Network Error")
                    .setDuration(ChocoBar.LENGTH_INDEFINITE)
                    .setActionText(android.R.string.ok)
                    .red()   // in built red ChocoBar
                    .show();
        }







      RetriveText2();

























    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {




        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;






    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


       if (item.getItemId() == R.id.mybutton)
        {
            SendUserToAboutUsActiviy();
        }
        if (item.getItemId() == R.id.mybutton2) {
            RetriveText();
        }
        if (item.getItemId() == R.id.mybutton3) {
            dialogbox();
        }
return true;
    }



    public static class InternetConnection {

        /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
            return false;
        }
    }
    private void SendUserToAboutUsActiviy()
    {
        Intent AboutUs = new Intent(MainActivity.this,About.class);
        startActivity(AboutUs);

    }
    private void RetriveText()
    {
        mDatabasepop.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if((dataSnapshot.exists()))
                {
                    String title = dataSnapshot.child("tit").getValue().toString();

                    String info = dataSnapshot.child("info").getValue().toString();
                    String update = dataSnapshot.child("btn").getValue().toString();
                    final String link = dataSnapshot.child("link").getValue().toString();



                    new FancyGifDialog.Builder(MainActivity.this)
                            .setTitle(title)
                            .setMessage(info)
                            .setNegativeBtnText("Cancel")
                            .setPositiveBtnBackground("#FF4081")
                            .setPositiveBtnText(update)
                            .setNegativeBtnBackground("#FFA9A7A8")
                            .setGifResource(R.drawable.infoimp4)   //Pass your Gif here
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {

                                    mDatabasepop.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot)
                                        {
                                            if((dataSnapshot.exists()))
                                            {




                                                        Uri uri = Uri.parse(link);
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                                        startActivity(intent);




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
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();







                }

                else
                {
                    Toast.makeText(MainActivity.this,"Update Not Available",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void RetriveText2()
    {
        mDatabasepop2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if((dataSnapshot.exists()))
                {
                    String playerName = dataSnapshot.getValue().toString();
                    ChocoBar.builder().setActivity(MainActivity.this)
                            .setText(playerName)
                            .setDuration(ChocoBar.LENGTH_INDEFINITE)
                            .setActionText(android.R.string.ok)
                            .orange() // in built red ChocoBar
                            .show();






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







    private void dialogbox()
    {









        mDatabaseDonate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if((dataSnapshot.exists()))
                {
                     final String upi = dataSnapshot.child("upi").getValue().toString();
                    String title=dataSnapshot.child("title").getValue().toString();
                    String msg=dataSnapshot.child("msg").getValue().toString();
                    final String note=dataSnapshot.child("note").getValue().toString();
                    final String amt=dataSnapshot.child("amt").getValue().toString();


                    new FancyGifDialog.Builder(MainActivity.this)
                            .setTitle(title)
                            .setMessage(msg)
                            .setNegativeBtnText("Cancel")
                            .setPositiveBtnBackground("#FF4081")
                            .setPositiveBtnText("Donate")
                            .setNegativeBtnBackground("#FFA9A7A8")
                            .setGifResource(R.drawable.covidgif)   //Pass your Gif here
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    //Toast.makeText(MainActivity.this,"Ok",Toast.LENGTH_SHORT).show();


                                    String transactionId = "TID" + System.currentTimeMillis();

                                    final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                                            .with(MainActivity.this)
                                            .setPayeeVpa(upi)
                                            .setPayeeName("PAYEE_NAME")
                                            .setTransactionId(transactionId)
                                            .setTransactionRefId(transactionId)
                                            .setDescription(note)
                                            .setAmount(amt+".00")
                                            .build();
                                    easyUpiPayment.startPayment();

                                    //easyUpiPayment.setPaymentStatusListener(PaymentStatusListener);

                                    if (easyUpiPayment.isDefaultAppExist()) {
                                        onAppNotFound();
                                        return;
                                    }



                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();









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






    public void onAppNotFound() {
        Toast.makeText(this, "UPI payment Application Not Found", Toast.LENGTH_SHORT).show();
    }










}
