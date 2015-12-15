package fr.univ_lille1.pje.pje3jx.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import fr.univ_lille1.pje.pje3jx.FiltersList;
import fr.univ_lille1.pje.pje3jx.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import fr.univ_lille1.pje.pje3jx.Book;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 *
 *
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/

    private static final String DATABASE_NAME = "pje3jx.db";
    private static final int DATABASE_VERSION = 9;

    private Dao<Book, Integer> bookDao;
    private Dao<FiltersList, Integer> filtersListDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, Book.class);
            TableUtils.createTable(connectionSource, FiltersList.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create databases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, Book.class, true);
            TableUtils.dropTable(connectionSource, FiltersList.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<Book, Integer> getBookDao() throws SQLException {
        if (bookDao == null) {
            bookDao = getDao(Book.class);
        }
        return bookDao;
    }

    public Dao<FiltersList, Integer> getFiltersListDao() throws SQLException {
        if (filtersListDao == null) {
            filtersListDao = getDao(FiltersList.class);
        }
        return filtersListDao;
    }

    public boolean exportDatabase(String exportFileName) throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        File newDb = new File(getWritableDatabase().getPath());
        File exportedFile = new File(sd, exportFileName);
        if (newDb.exists ()) {
            Log.d("Export DB", exportedFile.getCanonicalPath());
            DatabaseHelper.copyFile(new FileInputStream(newDb), new FileOutputStream(exportedFile));
            return true;
        }
        return false;
    }

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean importDatabase(String path) throws IOException {
        // Close the SQLiteOpenHelper so it will commit the created empty database to internal storage.
        close();
        File newDb = new File(Environment.getExternalStorageDirectory(), path);
        File oldDb = new File(getWritableDatabase().getPath());
        if (newDb.exists()) {
            DatabaseHelper.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

}