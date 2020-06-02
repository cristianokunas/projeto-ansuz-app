package com.anusuz;

import android.content.Intent;
import android.os.Bundle;

import com.anusuz.config.ConfiguracaoFirebase;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = ConfiguracaoFirebase.getFirebaseDatabase();

    private TextInputEditText fieldConfidence, fieldDistance, fieldFrameWidth, fieldLocal,
            fieldMaxDisappear, fieldMaxDistance, fieldSpeedLimit, fieldTrackObject;


    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao(); //recupera instância do firebase

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fieldConfidence = findViewById(R.id.textConfidence);
        fieldDistance = findViewById(R.id.textDistance);
        fieldFrameWidth = findViewById(R.id.textFrameWidth);
        fieldLocal = findViewById(R.id.textLocal);
        fieldMaxDisappear = findViewById(R.id.textDisappear);
        fieldMaxDistance = findViewById(R.id.textMaxDistance);
        fieldSpeedLimit = findViewById(R.id.textSpeedLimit);
        fieldTrackObject = findViewById(R.id.textTrackObject);

        Log.i("Teste1",autenticacao.getUid());


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fieldConfidence.setText(String.valueOf(dataSnapshot.child("usuarios").
                        child(autenticacao.getCurrentUser().getUid()).child("confidence").getValue()));
                fieldDistance.setText(String.valueOf(dataSnapshot.child("usuarios").
                        child(autenticacao.getCurrentUser().getUid()).child("distance").getValue()));
                fieldFrameWidth.setText(String.valueOf(dataSnapshot.child("usuarios").
                        child(autenticacao.getCurrentUser().getUid()).child("frame_width").getValue()));
                fieldLocal.setText(String.valueOf(dataSnapshot.child("usuarios").
                        child(autenticacao.getCurrentUser().getUid()).child("local").getValue()));
                fieldMaxDisappear.setText(String.valueOf(dataSnapshot.child("usuarios").
                        child(autenticacao.getCurrentUser().getUid()).child("max_disappear").getValue()));
                fieldMaxDistance.setText(String.valueOf(dataSnapshot.child("usuarios").
                        child(autenticacao.getCurrentUser().getUid()).child("max_distance").getValue()));
                fieldSpeedLimit.setText(String.valueOf(dataSnapshot.child("usuarios").
                        child(autenticacao.getCurrentUser().getUid()).child("speed_limit").getValue()));
                fieldTrackObject.setText(String.valueOf(dataSnapshot.child("usuarios").
                        child(autenticacao.getCurrentUser().getUid()).child("track_object").getValue()));
                Log.i("Teste1",autenticacao.getUid());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
