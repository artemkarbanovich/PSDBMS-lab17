package karbanovich.fit.bstu.jdbcwork;

import android.os.StrictMode;
import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

public class DBHelper {

    private static final String SERVER_IP = "10.0.2.2";
    private static final String SERVER_PORT = "1433";
    private static final String DATABASE_NAME = "JDBCWork";
    private static final String SERVER_USERNAME = "JDBCWorkAdmin";
    private static final String SERVER_PASSWORD = "1234";
    private static final String SERVER_URL = "jdbc:jtds:sqlserver://" + SERVER_IP + ":" + SERVER_PORT + "/" + DATABASE_NAME;
    private Connection connection = null;


    private Connection getConnection() {
        if (connection != null) return connection;
        else {
            try {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                connection = DriverManager.getConnection(SERVER_URL, SERVER_USERNAME, SERVER_PASSWORD);
                return connection;
            } catch (Exception e) {
                Log.d("ERROR server connection", e.getMessage());
                return null;
            }
        }
    }

    public boolean batchDataAdding() {
        if (getConnection() == null) return false;
        try {
            String query = "" +
                    "insert T1 (FIRSTNAME, LASTNAME, BIRTHPLACE) values ('Артём', 'Карбанович', 'Беларусь, Минск');     " +
                    "insert T1 (FIRSTNAME, LASTNAME, BIRTHPLACE) values ('Вова', 'Петров', 'Беларусь, Гомель');         " +
                    "insert T1 (FIRSTNAME, LASTNAME, BIRTHPLACE) values ('Сережа', 'Булыжник', 'Беларусь, Серебрянка'); " +
                    "insert T1 (FIRSTNAME, LASTNAME, BIRTHPLACE) values ('Генадий', 'Горин', 'Украина, Киев');          " +
                    "insert T1 (FIRSTNAME, LASTNAME, BIRTHPLACE) values ('Антон', 'Долин', 'Россия, Москва');           " +
                    "insert T1 (FIRSTNAME, LASTNAME, BIRTHPLACE) values ('Илья', 'Пивцаев', 'Россия, Чита');            " +
                    "insert T1 (FIRSTNAME, LASTNAME, BIRTHPLACE) values ('Григорий', 'Антонов', 'Украина, Крым');       " +
                    "insert T1 (FIRSTNAME, LASTNAME, BIRTHPLACE) values ('Василий', 'Иванов', 'Украина, Киев');         ";

            getConnection().createStatement().executeUpdate(query);
            return true;
        } catch (Exception e ) {
            Log.d("ERROR data adding", e.getMessage());
            return false;
        }
    }

    public String select(String query) {
        if (getConnection() == null) return null;
        try {
            String result = "Result set:\n";
            ResultSet resultSet = getConnection().createStatement().executeQuery(query);

            while (resultSet.next())
                result += "\t" + resultSet.getString(1) +
                        " " + resultSet.getString(2) +
                        " - " + resultSet.getString(3) + "\n";
            resultSet.close();

            return result;
        } catch (Exception e) {
            Log.d("ERROR select", e.getMessage());
            return null;
        }
    }

    public String preperSelect(String firstName1, String firstName2) {
        if (getConnection() == null) return null;
        try {
            String result = "Result set:\n";

            String sql = "select FIRSTNAME, LASTNAME, BIRTHPLACE from T1 where FIRSTNAME = ? or FIRSTNAME = ?;";

            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            preparedStatement.setString(1, firstName1);
            preparedStatement.setString(2, firstName2);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                result += "\t" + resultSet.getString(1) +
                        " " + resultSet.getString(2) +
                        " - " + resultSet.getString(3) + "\n";
            resultSet.close();

            return result;
        } catch (Exception e) {
            Log.d("ERROR select", e.getMessage());
            return null;
        }
    }

    public boolean update() {
        if (getConnection() == null) return false;
        try {
            String query = "" +
                    "update T1 set FIRSTNAME = 'Епифан', LASTNAME = 'Бугатти' where LASTNAME = 'Карбанович' " +
                    "update T1 set FIRSTNAME = 'Иван', LASTNAME = 'Иванов' where BIRTHPLACE like 'Россия'   ";

            getConnection().createStatement().executeUpdate(query);
            return true;
        } catch (Exception e ) {
            Log.d("ERROR update data", e.getMessage());
            return false;
        }
    }

    public boolean delete() {
        if (getConnection() == null) return false;
        try {
            getConnection().createStatement().executeUpdate("delete from T1");
            return true;
        } catch (Exception e ) {
            Log.d("ERROR delete data", e.getMessage());
            return false;
        }
    }

    public int execProcedure() {
        if (getConnection() == null) return -1;
        try {
            CallableStatement stm = getConnection().prepareCall("{ call ROW_COUNT(?) }");
            stm.registerOutParameter(1, Types.INTEGER);
            stm.execute();

            int result = stm.getInt(1);
            stm.close();

            return result;
        } catch (Exception e) {
            Log.d("ERROR exec procedure", e.getMessage());
            return -1;
        }
    }

    public int callFunction(String country) {
        if (getConnection() == null) return -1;
        try {
            CallableStatement stm = getConnection().prepareCall("{? = call COUNT_PEOPLE_FROM(?) }");
            stm.registerOutParameter(1, Types.INTEGER);
            stm.setString(2, country);
            stm.execute();

            int result = stm.getInt(1);
            stm.close();

            return result;
        } catch (Exception e) {
            Log.d("ERROR call function", e.getMessage());
            return -1;
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            Log.d("ERROR close connection", e.getMessage());
        }
    }
}
