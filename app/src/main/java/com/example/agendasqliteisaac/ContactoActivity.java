package com.example.agendasqliteisaac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ContactoActivity extends AppCompatActivity {
    private String ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        TextView nombre = (TextView)findViewById(R.id.nombre_contacto);
        TextView apellido = (TextView)findViewById(R.id.apellido_contacto);
        TextView telefono = (TextView)findViewById(R.id.telefono_contacto);
        TextView edad = (TextView)findViewById(R.id.edad_contacto);

        File f = getDatabasePath("agenda.sqlite");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);

        String ape = getIntent().getExtras().getString("apellido",null);

        String query = "SELECT * FROM contactos WHERE apellido='"+ape+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()){
            nombre.setText(cursor.getString(0));
            apellido.setText(cursor.getString(1));
            ap = cursor.getString(1);
            telefono.setText(cursor.getString(2));
            edad.setText(cursor.getString(3));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contacto_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_item:
                File f = getDatabasePath("agenda.sqlite");
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);

                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setMessage("¿Estás seguro?");

                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {
                        String query = "DELETE FROM contactos WHERE apellido='"+ap+"'";
                        db.execSQL(query);
                        Toast.makeText(ContactoActivity.this, "¡Contacto eliminado!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ContactoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton No
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(ContactoActivity.this, "No has eliminado ningún contacto", Toast.LENGTH_SHORT).show();
                    }
                });

                //mostramos el alertbox
                alertbox.show();
                return true;
            case R.id.edit_item:
                Intent intent = new Intent(this, ModificarActivity.class);
                intent.putExtra("apellido",ap);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void volver(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}