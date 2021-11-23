package karbanovich.fit.bstu.jdbcwork;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding();
    }

    private void binding() {
        dbHelper = new DBHelper();

        findViewById(R.id.btnBatchDataAdding).setOnClickListener(view -> {
            if(dbHelper.batchDataAdding())
                Toast.makeText(this, "Данные успешно добавлены", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnSelect).setOnClickListener(view -> {
            String result = dbHelper.select("select FIRSTNAME, LASTNAME, BIRTHPLACE from T1;");
            if(result != null)
                Log.d("SELECT", result);
        });

        findViewById(R.id.btnSelectWithParam).setOnClickListener(view -> {
            String result = dbHelper.preperSelect("Артём", "Вова");
            if(result != null)
              Log.d("SELECT WITH PARAM", result);
        });

        findViewById(R.id.btnUpdateData).setOnClickListener(view -> {
            if(dbHelper.update())
                Toast.makeText(this, "Данные успешно обновлены", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnDeleteData).setOnClickListener(view -> {
            if(dbHelper.delete())
                Toast.makeText(this, "Данные успешно удалены", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnExecProc).setOnClickListener(view -> {
            int result = dbHelper.execProcedure();
            if(result != -1)
                Log.d("EXEC PROCEDURE", "Количество строк в таблице T1: " + result);
            else
                Toast.makeText(this, "Ошибка выполнения процедуры", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnCallFunc).setOnClickListener(view -> {
            int result = dbHelper.callFunction("Украина");
            if(result != -1)
                Log.d("EXEC PROCEDURE", "Количество людей родившихся в Украине: " + result);
            else
                Toast.makeText(this, "Ошибка вызова функции", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dbHelper.closeConnection();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}