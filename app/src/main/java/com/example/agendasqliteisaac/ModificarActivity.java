package com.example.agendasqliteisaac;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class ModificarActivity extends AppCompatActivity {
    private String ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        EditText nombre = (EditText)findViewById(R.id.mod_nombre);
        EditText apellido = (EditText)findViewById(R.id.mod_apellido);
        EditText telefono = (EditText)findViewById(R.id.mod_telefono);
        EditText edad = (EditText)findViewById(R.id.mod_edad);

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


    public void modificar(View v){
        EditText nombre = (EditText)findViewById(R.id.mod_nombre);
        EditText apellido = (EditText)findViewById(R.id.mod_apellido);
        EditText telefono = (EditText)findViewById(R.id.mod_telefono);
        EditText edad = (EditText)findViewById(R.id.mod_edad);

        if(nombre.getText().length()>0 && apellido.getText().length()>0 && telefono.getText().length()>0 && edad.getText().length()>0){
            File f = getDatabasePath("agenda.sqlite");
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);
            String consulta = "SELECT * FROM contactos WHERE apellido='"+apellido.getText()+"'";
            Cursor cursor = db.rawQuery(consulta, null);

            if(cursor.moveToNext()){
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setMessage("¿Estás seguro de que quieres modificarlo?");

                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContentValues contactoMod = new ContentValues();
                        contactoMod.put("nombre",nombre.getText().toString());
                        contactoMod.put("telefono",telefono.getText().toString());
                        contactoMod.put("edad",edad.getText().toString());

                        db.update("contactos",contactoMod,"apellido='"+apellido.getText()+"'",null);
                        Toast.makeText(ModificarActivity.this, "¡Contacto modificado!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ModificarActivity.this, ContactoActivity.class);
                        intent.putExtra("apellido",ap);
                        startActivity(intent);
                    }
                });

                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton No
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(ModificarActivity.this, "No has modificado ningún contacto", Toast.LENGTH_SHORT).show();
                    }
                });
                alertbox.show();


            }else{
                Toast.makeText(this, "No hay ningun contacto con ese apellido", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_SHORT).show();
        }
    }

    public void volver(View v){
        Intent intent = new Intent(this, ContactoActivity.class);
        intent.putExtra("apellido",ap);
        startActivity(intent);
    }
}