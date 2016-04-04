package com.rahul.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.rahul.myapplication.R.id.content_title;

public class ContentViewActivity extends AppCompatActivity {

    String link,desc,title="Please Wait ...",image_link;
    Document document;
    TextView textView, titleTextView;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ImageView imageView;
    List<Information> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        setContentView(R.layout.activity_content_view);
        textView= (TextView)findViewById(R.id.desc);
        titleTextView = (TextView) findViewById(content_title);
        textView.setTypeface(tf);
        imageView = (ImageView) findViewById(R.id.imageView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        titleTextView.setText(intent.getStringExtra("title"));
        titleTextView.setTypeface(tf);
        link = intent.getStringExtra("next_link");
        desc = intent.getStringExtra("desc");
        image_link = intent.getStringExtra("image_link");
        textView.setText(desc);

        textView.setMovementMethod(new ScrollingMovementMethod());
        ((AppCompatActivity) this).getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);

        isNetworkAvailable();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Open Webpage Link", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(android.R.color.white))
                        .setAction("OPEN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                startActivity(intent);
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class JsoupAsyncTask extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.GONE);
            dialog = new ProgressDialog(ContentViewActivity.this);
            dialog.setMessage("Please Wait !");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                document = Jsoup.connect(link).get();
                Elements elements = document.select("div.postcontent > *");
                title = elements.get(1).text();
                if (title.isEmpty())
                    title = elements.get(2).text();
                //desc = elements.get(2).text();
//                image_link = elements.get(0).select("img[src]").attr("src");
//                if (image_link.isEmpty())
//                    image_link = elements.get(1).select("img[src]").attr("src");
                for (Element element : elements.select("blockquote")){
                    for (Element dwn_lnk : element.select("a[href^=http]")) {
                        Information information = new Information();
                        information.title = dwn_lnk.text();
                        information.link = dwn_lnk.attr("href");
                        if (!information.title.isEmpty())
                            list.add(information);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean th){
            ((AppCompatActivity)ContentViewActivity.this).getSupportActionBar().setTitle(title);
            findViewById(R.id.recyclerList).setVisibility(View.VISIBLE);
//            titleTextView.setText(title);
            myAdapter = new MyAdapter(getApplicationContext(), list, false);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            if (dialog.isShowing())
                dialog.dismiss();
            Picasso.with(ContentViewActivity.this).load(image_link).into(imageView);

        }
    }

    public void isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        }
        else showDialogBox();
    }

    public boolean showDialogBox(){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ContentViewActivity.this);
        dialog.setTitle("Network Connectivity");
        dialog.setMessage("No internet connection detected please try again");
        dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isNetworkAvailable();
            }
        });
        dialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                System.exit(0);
            }
        });
//        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setCancelable(false);
        dialog.show();
        return true;
    }

}
