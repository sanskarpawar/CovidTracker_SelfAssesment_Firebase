package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;


import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);


        View aboutPage =new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.infoimp5)
                .setDescription("STUDENT OF: JSPM NARHE TECHNICAL CAMPUS,PUNE" +
                        ""
                        )
                .setDescription("Stand Against FAKE News and WhatsApp Forwards! Do NOT forward a message until you verify the contains")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("pawarsanskar50@gmail.com","Contact me on Email")
                .addFacebook("sanskar.pawar.94","Add me on Facebook")
                .addTwitter("SanskarPawar2","Follow me on Twitter")
                .addGitHub("sanskarpawar","Follow me on GitHub")

                .addInstagram("sanskar__pawar","Follow me on Instagram")
                .create();
        setContentView(aboutPage);

    }
}
