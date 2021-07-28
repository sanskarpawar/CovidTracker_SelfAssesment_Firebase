package com.example.covid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;



public class SelfAssessmentActivity extends Fragment {
    private View SelfAssessmentActivityView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    //private DatabaseReference reference = firebaseDatabase.getReference();
    private DatabaseReference childReference ;


    public WebView mwebView;
    ProgressBar bar;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    //String url ="https://www.pdfdrive.com/";
    //make HTML upload button work in Webview


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SelfAssessmentActivityView = inflater.inflate(R.layout.fragment_india, null);


        bar = (ProgressBar) SelfAssessmentActivityView.findViewById(R.id.progressBar);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) SelfAssessmentActivityView.findViewById(R.id.main_swipe);
        // initialize bar
        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.refresh);

        mwebView = (WebView) SelfAssessmentActivityView.findViewById(R.id.webview);
        // mwebView.loadUrl("https://www.pdfdrive.com/");

        mwebView.getSettings().setJavaScriptEnabled(true);

        mwebView.setWebViewClient(new MyWebViewClient());
        mwebView.getSettings().setDomStorageEnabled(true);
        childReference = FirebaseDatabase.getInstance().getReference().child("selfurl");

        childReference.keepSynced(true);



        mwebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        mwebView.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && mwebView.canGoBack())
                {
                    handler.sendEmptyMessage(1);

                    return true;
                }

                return false;
            }

        });
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.BLACK);
        // mWaveSwipeRefreshLayout.setColorSchemeResources();

        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener()
        {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                mwebView.reload();
                new Taska().execute();
                mp.start();
            }
        });



        return SelfAssessmentActivityView;


    }

    @Override
    public void onStart() {
        super.onStart();
        childReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String message = dataSnapshot.getValue(String.class);
                mwebView.loadUrl(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void webViewGoBack(){
        mwebView.goBack();
    }

    private class Taska extends AsyncTask<Void , Void ,String[]>
    {
        @Override
        protected String[] doInBackground(Void... voids)
        {
            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    private class MyWebViewClient extends WebViewClient
    {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
            bar.setVisibility(View.VISIBLE);
            // ^^^ use it as it is

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            bar.setVisibility(View.GONE);
            // ^^^ use it as it is
            super.onPageFinished(view, url);


        }


    }

}