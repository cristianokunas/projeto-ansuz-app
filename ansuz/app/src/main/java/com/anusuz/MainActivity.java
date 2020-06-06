package com.anusuz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anusuz.config.ConfiguracaoFirebase;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity::";
    private FirebaseAuth autenticacao;
    private DatabaseReference myRef;
    private TextInputEditText fieldConfidence, fieldDistance, fieldFrameWidth, fieldLocal,
            fieldMaxDisappear, fieldMaxDistance, fieldSpeedLimit, fieldTrackObject;
    private ProgressBar progressoCarregandoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao(); //recupera instância do firebase
        myRef = ConfiguracaoFirebase.getFirebaseDatabase();

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
        progressoCarregandoMain = findViewById(R.id.progressBarMain);

        progressoCarregandoMain.setVisibility(View.VISIBLE);
        Log.i(TAG, "Activity Main iniciada.");

        if (!isOnline()) {
            progressoCarregandoMain.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Você não está conectado a Internet.", Toast.LENGTH_SHORT).show();
        }

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        progressoCarregandoMain.setVisibility(View.VISIBLE);
                        if (isOnline()) {
                            if (dataSnapshot.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("confidence").getValue() != null)
                                fieldConfidence.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(autenticacao.getCurrentUser().getUid()).child("confidence").getValue()));
                            if (dataSnapshot.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("distance").getValue() != null)
                                fieldDistance.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(autenticacao.getCurrentUser().getUid()).child("distance").getValue()));
                            if (dataSnapshot.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("frame_width").getValue() != null)
                                fieldFrameWidth.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(autenticacao.getCurrentUser().getUid()).child("frame_width").getValue()));
                            if (dataSnapshot.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("local").getValue() != null)
                                fieldLocal.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(autenticacao.getCurrentUser().getUid()).child("local").getValue()));
                            if (dataSnapshot.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("max_disappear").getValue() != null)
                                fieldMaxDisappear.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(autenticacao.getCurrentUser().getUid()).child("max_disappear").getValue()));
                            if (dataSnapshot.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("max_distance").getValue() != null)
                                fieldMaxDistance.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(autenticacao.getCurrentUser().getUid()).child("max_distance").getValue()));
                            if (dataSnapshot.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("speed_limit").getValue() != null)
                                fieldSpeedLimit.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(autenticacao.getCurrentUser().getUid()).child("speed_limit").getValue()));
                            if (dataSnapshot.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("track_object").getValue() != null)
                                fieldTrackObject.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(autenticacao.getCurrentUser().getUid()).child("track_object").getValue()));
                            Log.i(TAG, "Dados obtidos com sucesso.");
                            Toast.makeText(MainActivity.this, "Dados obtidos com sucesso!.", Toast.LENGTH_SHORT).show();
                            progressoCarregandoMain.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(MainActivity.this, "Você não está conectado a Internet.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Erro ao obter dados.");
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Não foi possível obter dados do usuário.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Erro ao obter dados.");
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Cancelado!");
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
            case R.id.btnSaveConfig:
                progressoCarregandoMain.setVisibility(View.VISIBLE);
                SalvarConfigs();
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

    public void SalvarConfigs(){
        try {
            if (isOnline()) {
                if (!fieldConfidence.getText().toString().isEmpty())
                    myRef.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("confidence").setValue(fieldConfidence.getText().toString());
                if (!fieldDistance.getText().toString().isEmpty())
                    myRef.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("distance").setValue(fieldDistance.getText().toString());
                if (!fieldFrameWidth.getText().toString().isEmpty())
                    myRef.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("frame_width").setValue(fieldFrameWidth.getText().toString());
                if (!fieldLocal.getText().toString().isEmpty())
                    myRef.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("local").setValue(fieldLocal.getText().toString());
                if (!fieldMaxDisappear.getText().toString().isEmpty())
                    myRef.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("max_disappear").setValue(fieldMaxDisappear.getText().toString());
                if (!fieldMaxDistance.getText().toString().isEmpty())
                    myRef.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("max_distance").setValue(fieldMaxDistance.getText().toString());
                if (!fieldSpeedLimit.getText().toString().isEmpty())
                    myRef.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("speed_limit").setValue(fieldSpeedLimit.getText().toString());
                if (!fieldTrackObject.getText().toString().isEmpty())
                    myRef.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("track_object").setValue(fieldTrackObject.getText().toString());
                Log.i(TAG, "Configuração atualizada.");
                Toast.makeText(MainActivity.this, "Configuração atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                progressoCarregandoMain.setVisibility(View.GONE);
            }else {
                progressoCarregandoMain.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Você não está conectado a Internet.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erro ao obter dados.");
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Não foi possível obter dados do usuário.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Erro ao obter dados.");
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
