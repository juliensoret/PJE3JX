package fr.univ_lille1.pje.pje3jx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 15/10/6.
 */
public class GenreActivity extends AppCompatActivity {
    ListView listgenre;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listgenre = (ListView) findViewById(R.id.genrelistView);

        listgenre.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));

        listgenre.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                switch (getData().get(position)) {
                    case "Cuisine" :
                        Intent intent = new Intent();
                        intent.setClass(GenreActivity.this, BooksCatalogActivity.class);
                        intent.putExtra("standard", "Genre");
                        intent.putExtra("detail","Cuisine");
                        startActivity(intent);
                        break;
                    case "Technologie" :
                        Intent intent1 = new Intent();
                        intent1.setClass(GenreActivity.this, BooksCatalogActivity.class);
                        intent1.putExtra("standard", "Genre");
                        intent1.putExtra("detail", "Technologie");
                        startActivity(intent1);
                        break;
                    case "Roman" :
                        Intent intent2 = new Intent();
                        intent2.setClass(GenreActivity.this, BooksCatalogActivity.class);
                        intent2.putExtra("standard", "Genre");
                        intent2.putExtra("detail", "Roman");
                        startActivity(intent2);
                        break;
                    case "..." :
                        Intent intent3 = new Intent();
                        intent3.setClass(GenreActivity.this, BooksCatalogActivity.class);
                        intent3.putExtra("standard","Genre");
                        intent3.putExtra("detail", "...");
                        startActivity(intent3);
                        break;
                }

            }

        });


    }

    protected List<String> getData(){

        List<String> data = new ArrayList<String>();
        data.add("Cuisine");
        data.add("Technologie");
        data.add("Roman");
        data.add("...");

        return data;
    }
}
