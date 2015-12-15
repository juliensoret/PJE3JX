package fr.univ_lille1.pje.pje3jx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    Button button, button2, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

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
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(R.array.action_database_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            String fileName = "pje3jx_" + new Date().getTime() + ".db";
                            try {
                                OpenHelperManager.getHelper(MainActivity.this, DatabaseHelper.class).exportDatabase(fileName);
                                String message = getString(R.string.text_databasesaved, fileName);
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            // Find all files starting with pje3jx and ending by .db
                            final File[] foundFiles = Environment.getExternalStorageDirectory().listFiles(new FilenameFilter() {
                                @Override
                                public boolean accept(File file, String s) {
                                    return (s.endsWith(".db") && s.startsWith("pje3jx"));
                                }
                            });
                            // Sort the files to make the new one up in the list
                            Arrays.sort(foundFiles);
                            Collections.reverse(Arrays.asList(foundFiles));
                            // Add information in Strings
                            String[] filesInfo = new String[foundFiles.length];
                            for (int i=0;i<foundFiles.length;++i) {
                                filesInfo[i] = foundFiles[i].getName()
                                        + "\n － (" + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date(foundFiles[i].lastModified()))
                                        + ") － " + (foundFiles[i].length()/1024) + "ko";
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            if(filesInfo.length>0) {
                                builder.setItems(filesInfo, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String path = foundFiles[which].getName();
                                        try {
                                            OpenHelperManager.getHelper(MainActivity.this, DatabaseHelper.class).importDatabase(path);
                                            Toast.makeText(MainActivity.this, R.string.text_databaseimported, Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else
                                builder.setMessage(R.string.error_savesnotfound);
                            builder.setTitle(R.string.text_savesfound).create().show();
                        }
                    }
                });
                builder.setTitle(R.string.action_database).create().show();
            }
        });
    }
}