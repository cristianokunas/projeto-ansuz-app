package com.anusuz;

import android.content.Intent;
import android.os.Bundle;

import com.anusuz.config.ConfiguracaoFirebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao(); //recupera instância do firebase

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { /*método que descobre qual item do menu
                                                                        foi clicado */
        switch (item.getItemId()) {
            case R.id.action_getout:
                deslogarUsuario();
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish(); //finaliza activity
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario() {

        try {
            autenticacao.signOut(); //aqui desloga o usuário ativo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
