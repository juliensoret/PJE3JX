package fr.univ_lille1.pje.pje3jx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class BookScanActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    private TextView authorText, titleText, descriptionText, dateText;
    private EditText isbnText;
    private Button addButton;
    private ImageView thumbView;

    private String bTitle, bAuthor, bPublisher, bLanguage, bDescription;

    private int bDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_scan);

        Button isbnButton = (Button)findViewById(R.id.buttonIsbn);
        Button scanButton = (Button)findViewById(R.id.buttonScan);
        addButton = (Button)findViewById(R.id.add_button);
        addButton.setVisibility(View.GONE);

        authorText = (TextView)findViewById(R.id.book_author);
        titleText = (TextView)findViewById(R.id.book_title);
        isbnText = (EditText)findViewById(R.id.editTextIsbn);
        descriptionText = (TextView)findViewById(R.id.book_description);
        dateText = (TextView)findViewById(R.id.book_date);
        thumbView = (ImageView)findViewById(R.id.thumb);

        isbnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String bookSearchString = "https://www.googleapis.com/books/v1/volumes?"+
                        "q=isbn:"+isbnText.getText()+"&key=AIzaSyB8rVQ4ng-hCn_LftJBDR_d689ObOlzR3A";
                new GetBookInfo().execute(bookSearchString);
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(BookScanActivity.this);
                scanIntegrator.initiateScan();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            Log.v("SCAN", "content: " + scanContent + " - format: " + scanFormat);
            isbnText.setText(scanContent);

            if(scanContent!=null && scanFormat!=null && scanFormat.equalsIgnoreCase("EAN_13")){
                String bookSearchString = "https://www.googleapis.com/books/v1/volumes?"+
                        "q=isbn:"+scanContent+"&key=AIzaSyB8rVQ4ng-hCn_LftJBDR_d689ObOlzR3A";
                new GetBookInfo().execute(bookSearchString);
            }
            else{
                Toast.makeText(getApplicationContext(),
                        R.string.error_noscandatareceived, Toast.LENGTH_SHORT
                ).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),
                    R.string.error_noscandatareceived, Toast.LENGTH_SHORT
            ).show();
        }
    }

    private class GetBookInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... bookURLs) {
            StringBuilder bookBuilder = new StringBuilder();
            for (String bookSearchURL : bookURLs) {
                HttpClient bookClient = new DefaultHttpClient();
                try {
                    HttpGet bookGet = new HttpGet(bookSearchURL);
                    HttpResponse bookResponse = bookClient.execute(bookGet);
                    StatusLine bookSearchStatus = bookResponse.getStatusLine();
                    if (bookSearchStatus.getStatusCode()==200) {
                        HttpEntity bookEntity = bookResponse.getEntity();
                        InputStream bookContent = bookEntity.getContent();
                        InputStreamReader bookInput = new InputStreamReader(bookContent);
                        BufferedReader bookReader = new BufferedReader(bookInput);
                        String lineIn;
                        while ((lineIn=bookReader.readLine())!=null) {
                            bookBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){ e.printStackTrace(); }
            }
            return bookBuilder.toString();
        }

        protected void onPostExecute(String result) {

            bTitle = bAuthor = bPublisher = bLanguage = bDescription = "";
            bDate = 0;

            try{
                addButton.setVisibility(View.VISIBLE);
                JSONObject resultObject = new JSONObject(result);
                JSONArray bookArray = resultObject.getJSONArray("items");
                JSONObject bookObject = bookArray.getJSONObject(0);
                JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");

                try{ bTitle = volumeObject.getString("title"); }
                catch(JSONException jse){ jse.printStackTrace(); }

                try{ bTitle += " : " + volumeObject.getString("subtitle"); }
                catch(JSONException jse){ jse.printStackTrace(); }

                StringBuilder authorBuild = new StringBuilder("");
                try{
                    JSONArray authorArray = volumeObject.getJSONArray("authors");
                    for(int a=0; a<authorArray.length(); a++){
                        if(a>0) authorBuild.append(", ");
                        authorBuild.append(authorArray.getString(a));
                    }
                    bAuthor = authorBuild.toString();
                }
                catch(JSONException jse){ jse.printStackTrace(); }

                try{ bPublisher = volumeObject.getString("publisher"); }
                catch(JSONException jse){ jse.printStackTrace(); }

                try{ bLanguage = volumeObject.getString("language"); }
                catch(JSONException jse){ jse.printStackTrace(); }

                try{ bDate = Integer.parseInt(volumeObject.getString("publishedDate").substring(0, 4)); }
                catch(JSONException jse){ jse.printStackTrace(); }

                try{ bDescription = volumeObject.getString("description"); }
                catch(JSONException jse){ jse.printStackTrace(); }

                try{
                    JSONObject imageInfo = volumeObject.getJSONObject("imageLinks");
                    new GetBookThumb().execute(imageInfo.getString("smallThumbnail"));
                }
                catch(JSONException jse){
                    thumbView.setImageBitmap(null);
                    jse.printStackTrace();
                }

                titleText.setText("TITLE: " + bTitle);
                authorText.setText("AUTHOR(S): " + bAuthor);
                dateText.setText("DATE: " + bDate);
                descriptionText.setText("DESCRIPTION: " + bDescription);

                addButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Toast.makeText(
                                    BookScanActivity.this, R.string.text_bookadded, Toast.LENGTH_SHORT
                            ).show();
                            getHelper().getBookDao().create(
                                    new Book(
                                            isbnText.getText().toString(),
                                            bTitle, bAuthor, bPublisher, bDate, bLanguage
                                    )
                                            .setImage()
                                            .setDescription(bDescription)
                            );
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
                addButton.setEnabled(true);
            }
            catch (Exception e) {
                e.printStackTrace();
                titleText.setText(R.string.error_datanotfound);
                authorText.setText("");
                descriptionText.setText("");
                dateText.setText("");
                addButton.setVisibility(View.GONE);
            }
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private class GetBookThumb extends AsyncTask<String, Void, String> {
        private Bitmap thumbImg;

        @Override
        protected String doInBackground(String... thumbURLs) {
            try{
                URL thumbURL = new URL(thumbURLs[0]);
                URLConnection thumbConn = thumbURL.openConnection();
                thumbConn.connect();
                InputStream thumbIn = thumbConn.getInputStream();
                BufferedInputStream thumbBuff = new BufferedInputStream(thumbIn);
                thumbImg = BitmapFactory.decodeStream(thumbBuff);
                thumbBuff.close();
                thumbIn.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            thumbView.setImageBitmap(thumbImg);
        }
    }
}
