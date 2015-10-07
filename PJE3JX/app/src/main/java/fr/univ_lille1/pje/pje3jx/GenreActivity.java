package fr.univ_lille1.pje.pje3jx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GenreActivity extends AppCompatActivity {

    ListView listgenre;
    int position;
    Button addButton;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listgenre = (ListView) findViewById(R.id.genrelistView);

        final ArrayAdapter adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                BookFilterCatalog.getFilterLists()
        );
        listgenre.setAdapter(adapter);

        listgenre.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.setClass(GenreActivity.this, BooksCatalogActivity.class);
                intent.putExtra("standard", "Genre");
                intent.putExtra("detail", BookFilterCatalog.getFilterLists().get(position));
                startActivity(intent);
            }

        });

        listgenre.setLongClickable(true);
        listgenre.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                position = pos;

                AlertDialog.Builder builder = new AlertDialog.Builder(GenreActivity.this);

                builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(GenreActivity.this, R.string.text_listdeleted, Toast.LENGTH_SHORT).show();
                        BookFilterCatalog.deleteList(position);
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

        addButton = (Button) findViewById(R.id.buttonAdd);
        editText = (EditText) findViewById(R.id.editText);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BookFilterCatalog.addList(editText.getText().toString());
                Toast.makeText(GenreActivity.this, "Liste ajoutée !", Toast.LENGTH_SHORT).show();
                editText.setText("");
                adapter.notifyDataSetChanged();
            }
        });

    }
}
