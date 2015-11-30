package fr.univ_lille1.pje.pje3jx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class FiltersListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    private Dao<FiltersList, Integer> filtersListDao;
    private List<FiltersList> filtersListList;
    private List<String> filtersListNames = new ArrayList<>();
    ListView filtersListView;
    Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterslist_list);

        filtersListView = (ListView) findViewById(R.id.filtersListView);
        addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FiltersListActivity.this, FiltersListAddActivity.class);
                startActivity(intent);
            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                filtersListNames
        );
        filtersListView.setAdapter(adapter);

        filtersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view,
                                    int pos, long id) {
                Intent intent = new Intent();
                intent.setClass(FiltersListActivity.this, FiltersListResultsActivity.class);
                intent.putExtra("id", filtersListList.get(pos).getId());
                startActivity(intent);
            }

        });

        filtersListView.setLongClickable(true);
        filtersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                final int position = pos;

                AlertDialog.Builder builder = new AlertDialog.Builder(FiltersListActivity.this);

                builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(FiltersListActivity.this, R.string.text_listdeleted, Toast.LENGTH_SHORT).show();
                        try {
                            filtersListDao.deleteById(filtersListList.get(position).getId());
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public void fillWithExamples() throws SQLException {
        FiltersList bf;

        bf = new FiltersList("Livres non-lus");
        bf.setReadFilter(1);
        filtersListDao.create(bf);

        bf = new FiltersList("Livres de Hergé");
        bf.setAuthorFilter("hergé");
        filtersListDao.create(bf);

        bf = new FiltersList("Pour la Cuisine");
        bf.setGenreFilter("cuisine");
        filtersListDao.create(bf);

        bf = new FiltersList("Mes Romans");
        bf.setGenreFilter("genre");
        filtersListDao.create(bf);

        bf = new FiltersList("Après l'année 2000");
        bf.setStartYearFilter(2000);
        filtersListDao.create(bf);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            filtersListDao = getHelper().getFiltersListDao();
            filtersListList = filtersListDao.queryForAll();
            Log.d("d filterslistlist", filtersListList.size() + "");
            if(filtersListList.isEmpty()) {
                fillWithExamples();
                filtersListList = filtersListDao.queryForAll();
            }
            filtersListNames.clear();
            for (FiltersList fl : filtersListList)
                filtersListNames.add(fl.getListName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
