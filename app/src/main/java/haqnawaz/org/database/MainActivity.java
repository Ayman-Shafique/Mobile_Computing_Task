package haqnawaz.org.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button buttonAdd, buttonViewAll;
    EditText editName, editAge;
    Switch switchIsActive;
    ListView listViewCustomer;
    ArrayAdapter<CustomerModel> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonViewAll = findViewById(R.id.buttonViewAll);
        editName = findViewById(R.id.editTextName);
        editAge = findViewById(R.id.editTextAge);
        switchIsActive = findViewById(R.id.switchCustomer);
        listViewCustomer = findViewById(R.id.listViewCustomer);
        RefreshData();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            CustomerModel customerModel;

            @Override
            public void onClick(View v) {
                try {
                    customerModel = new CustomerModel(editName.getText().toString(), Integer.parseInt(editAge.getText().toString()), switchIsActive.isChecked(), 1);
                    Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                boolean b = dbHelper.addCustomer(customerModel);
                RefreshData();
            }
        });
        
        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshData();
            }
        });
        listViewCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                DBHelper dbHelper = new DBHelper(MainActivity.this);
                String item = listViewCustomer.getItemAtPosition(i).toString();//the info of clicked customer
                String customer = item.substring(14,item.length()-1);//14 length of 'CustomerModel{' and length-1 for removing '}'
                String[] customerInfo = customer.split(",");//Splitting the data where , is present
                String idString = customerInfo[3];//returns 'id=value'
                String id = idString.substring(4,idString.length());//removing the 'id='
                dbHelper.deleteCustomer(Integer.parseInt(id));//passing id to the delete function
                RefreshData();

            }
        });

    }

    private void RefreshData() {

        DBHelper dbHelper = new DBHelper(MainActivity.this);
        List<CustomerModel> allCustomers = dbHelper.getAllRecords();
        arrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, allCustomers);
        listViewCustomer.setAdapter(arrayAdapter);

    }
}