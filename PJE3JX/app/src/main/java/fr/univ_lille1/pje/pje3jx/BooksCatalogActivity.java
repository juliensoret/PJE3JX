package fr.univ_lille1.pje.pje3jx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class BooksCatalogActivity extends AppCompatActivity{
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent =this.getIntent();
        String standard = intent.getStringExtra("standard");
        String detail = intent.getStringExtra("detail");

        mListView = (ListView) findViewById(R.id.listView);

        BookFilterCatalog bookFilterCatalog = new BookFilterCatalog(standard,detail);

        final BookAdapter adapter = new BookAdapter(
                BooksCatalogActivity.this, bookFilterCatalog.getFilteredList()
        );
        mListView.setAdapter(adapter);

    }
}
