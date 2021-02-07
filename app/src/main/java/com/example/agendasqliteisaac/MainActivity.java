package com.example.agendasqliteisaac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    SearchView searchView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File f = getDatabasePath("agenda.sqlite");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);

        String query = "create table if not exists contactos(nombre text, apellido text primary key, telefono text, edad integer)";

        db.execSQL(query);

        String query1 = "SELECT * FROM contactos";
        Cursor cursor = db.rawQuery(query1,null);
        ArrayList<String> apellidos = new ArrayList<>();
        ArrayList<String> contactos = new ArrayList<>();

        while(cursor.moveToNext()){
            //para que aparezca en el listview
            String contacto = cursor.getString(0) + " " + cursor.getString(1) +
                    "\n"+cursor.getString(2);
            contactos.add(contacto);

            //para poder pasar el contacto a otra activity
            apellidos.add(cursor.getString(1));
        }

        searchView = findViewById(R.id.search_bar);
        lv = findViewById(R.id.lista);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactos);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ContactoActivity.class);
                intent.putExtra("apellido",apellidos.get(position));
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void irAlta(View v){
        Intent intent = new Intent(this, AltaActivity.class);
        startActivity(intent);
    }
}