package fr.univ_lille1.pje.pje3jx.data;

import java.io.IOException;
import java.sql.SQLException;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;


public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    public static void main(String[] args) throws SQLException, IOException {

        writeConfigFile("ormlite_config.txt");
    }
}