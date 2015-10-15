package fr.univ_lille1.pje.pje3jx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ListBooksActivity extends AppCompatActivity {

    ListView mListView;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.listView);

        final BookAdapter adapter = new BookAdapter(ListBooksActivity.this, BookLibrary.getInstance());
        mListView.setAdapter(adapter);

        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                position = pos;

                AlertDialog.Builder builder = new AlertDialog.Builder(ListBooksActivity.this);

                builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(ListBooksActivity.this, R.string.text_bookdeleted, Toast.LENGTH_SHORT).show();
                        BookLibrary.getInstance().deleteBook(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.text_cancel, null);

                builder.setMessage(R.string.text_deletemessage)
                        .setTitle(R.string.action_delete);

                builder.create().show();

                return true;
            }
        });

    }

}
