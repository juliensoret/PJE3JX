package fr.univ_lille1.pje.pje3jx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class FilteredListBooksActivity extends AppCompatActivity{
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BookFilterCatalog bfc = BookFilterCatalog.getInstance();
        BookFilter filter = bfc.get(this.getIntent().getIntExtra("position", 0));

        setTitle("Filtre : "+filter.getName());

        mListView = (ListView) findViewById(R.id.listView);
        final BookAdapter adapter = new BookAdapter(
                FilteredListBooksActivity.this, filter.getFilteredList(BookLibrary.getInstance())
        );
        mListView.setAdapter(adapter);

    }
}
