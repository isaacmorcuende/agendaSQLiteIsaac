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

        File f = getDatabasePath("agenda1.sqlite");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);

        String query = "create table if not exists contactos(id integer primary key autoincrement, nombre text, apellido text, " +
                "telefono text, edad integer)";

        db.execSQL(query);
        String query1 = "SELECT id,nombre,apellido,telefono FROM contactos";
        Cursor cursor = db.rawQuery(query1,null);
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> contactos = new ArrayList<>();

        while(cursor.moveToNext()){
            //para que aparezca en el listview
            String contacto = cursor.getString(1) + " " + cursor.getString(2) +
                    "\n"+cursor.getString(3);
            contactos.add(contacto);

            //para poder pasar el contacto a otra activity
            ids.add(cursor.getString(0));
        }

        searchView = findViewById(R.id.search_bar);
        lv = findViewById(R.id.lista);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactos);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ContactoActivity.class);
                intent.putExtra("id",ids.get(position));
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
                ids.clear();
                contactos.clear();
                Cursor c = null;

                /*si hay texto en el searchbar apareceran los contactos que tengan ese texto en el nombre, apellidos o numero,
                si no hay nada o lo borra volvemos a mostrar todos los contactos*/
                if(newText != null && newText.length()>0){
                    String sql="SELECT id,nombre,apellido,telefono FROM contactos WHERE apellido LIKE '%"+newText+"%' OR nombre LIKE '%"+newText+"%'" +
                            "OR telefono LIKE '%"+newText+"%'";
                    c=db.rawQuery(sql,null);
                }else{
                    String query1 = "SELECT id,nombre,apellido,telefono FROM contactos";
                    c = db.rawQuery(query1,null);
                }

                while(c.moveToNext()){
                    //para que aparezca en el listview
                    String contacto = c.getString(1) + " " + c.getString(2) +
                            "\n"+c.getString(3);
                    contactos.add(contacto);

                    //para poder pasar el contacto a otra activity
                    ids.add(c.getString(0));
                }

                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, contactos);
                lv.setAdapter(adapter);
                return false;
            }
        });
    }

    public void irAlta(View v){
        Intent intent = new Intent(this, AltaActivity.class);
        startActivity(intent);
    }
}