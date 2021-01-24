package com.example.agendasqliteisaac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class AltaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);
    }

    public void alta(View v){
        EditText nombre = (EditText)findViewById(R.id.et_nombre);
        EditText apellido = (EditText)findViewById(R.id.et_apellido);
        EditText telefono = (EditText)findViewById(R.id.et_telefono);
        EditText edad = (EditText)findViewById(R.id.et_edad);

        if(nombre.getText().length()>0 && apellido.getText().length()>0 && telefono.getText().length()>0 && edad.getText().length()>0){
            File f = getDatabasePath("agenda.sqlite");
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);

            String query = "INSERT INTO contactos VALUES('"+nombre.getText()+"','"+apellido.getText()+"','"+ telefono.getText()+"',"+edad.getText()+ ")";

            db.execSQL(query);
            Toast.makeText(this, "¡Contacto añadido!", Toast.LENGTH_SHORT).show();

            nombre.setText("");
            apellido.setText("");
            telefono.setText("");
            edad.setText("");
        }else{
            Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_SHORT).show();
        }
    }

    public void volver(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}