package fr.univ_lille1.pje.pje3jx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FiltersListActivity extends AppCompatActivity {

    ListView filtersListView;
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_filters);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filtersListView = (ListView) findViewById(R.id.filtersListView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                BookFilterCatalog.getInstance().getFilterListNames()
        );
        filtersListView.setAdapter(adapter);

        filtersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.setClass(FiltersListActivity.this, FilteredListBooksActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }

        });

        filtersListView.setLongClickable(true);
        filtersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                position = pos;

                AlertDialog.Builder builder = new AlertDialog.Builder(FiltersListActivity.this);

                builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(FiltersListActivity.this, R.string.text_listdeleted, Toast.LENGTH_SHORT).show();
                        BookFilterCatalog.getInstance().deleteList(position);
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
