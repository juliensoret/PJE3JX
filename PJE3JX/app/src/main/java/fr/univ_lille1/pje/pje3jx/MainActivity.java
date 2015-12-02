package fr.univ_lille1.pje.pje3jx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(R.array.add_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(MainActivity.this, BookAddActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(MainActivity.this, BookScanActivity.class);
                            if (which == 1)
                                intent.putExtra("scan", true);
                            startActivity(intent);
                        }
                    }
                });
                builder.setTitle(R.string.title_activity_add_book).create().show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FiltersListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(this, BookAddActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_list) {
            Intent intent = new Intent(this, BookListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_filter) {
            Intent intent = new Intent(this, FiltersListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}