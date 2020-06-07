package com.anusuz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anusuz.config.ConfiguracaoFirebase;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity::";
    private ViewHolder mViewHolder = new ViewHolder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mViewHolder.firebaseAuthAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao(); //recupera instância do firebase
        this.mViewHolder.databaseReferenceProjetoAnsuz = ConfiguracaoFirebase.getFirebaseDatabase();

        this.mViewHolder.toolbarMain = findViewById(R.id.toolbar);
        setSupportActionBar(this.mViewHolder.toolbarMain);

        this.mViewHolder.editTextFieldConfidence = findViewById(R.id.textConfidence);
        this.mViewHolder.editTextFieldDistance = findViewById(R.id.textDistance);
        this.mViewHolder.editTextFieldFrameWidth = findViewById(R.id.textFrameWidth);
        this.mViewHolder.editTextFieldLocal = findViewById(R.id.textLocal);
        this.mViewHolder.editTextFieldMaxDisappear = findViewById(R.id.textDisappear);
        this.mViewHolder.editTextFieldMaxDistance = findViewById(R.id.textMaxDistance);
        this.mViewHolder.editTextFieldSpeedLimit = findViewById(R.id.textSpeedLimit);
        this.mViewHolder.editTextFieldTrackObject = findViewById(R.id.textTrackObject);
        this.mViewHolder.progressBarMain = findViewById(R.id.progressBarMain);
        this.mViewHolder.dialogSobre = new Dialog(this);

        this.mViewHolder.progressBarMain.setVisibility(View.VISIBLE);
        Log.i(TAG, "Activity Main iniciada.");

        if (!isOnline()) {
            this.mViewHolder.progressBarMain.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Você não está conectado a Internet.", Toast.LENGTH_SHORT).show();
        }

        // Read from the database
        this.mViewHolder.databaseReferenceProjetoAnsuz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        mViewHolder.progressBarMain.setVisibility(View.VISIBLE);
                        if (isOnline()) {
                            if (dataSnapshot.child("usuarios").child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("confidence").getValue() != null)
                                mViewHolder.editTextFieldConfidence.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("confidence").getValue()));
                            if (dataSnapshot.child("usuarios").child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("distance").getValue() != null)
                                mViewHolder.editTextFieldDistance.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("distance").getValue()));
                            if (dataSnapshot.child("usuarios").child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("frame_width").getValue() != null)
                                mViewHolder.editTextFieldFrameWidth.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("frame_width").getValue()));
                            if (dataSnapshot.child("usuarios").child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("local").getValue() != null)
                                mViewHolder.editTextFieldLocal.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("local").getValue()));
                            if (dataSnapshot.child("usuarios").child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("max_disappear").getValue() != null)
                                mViewHolder.editTextFieldMaxDisappear.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("max_disappear").getValue()));
                            if (dataSnapshot.child("usuarios").child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("max_distance").getValue() != null)
                                mViewHolder.editTextFieldMaxDistance.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("max_distance").getValue()));
                            if (dataSnapshot.child("usuarios").child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("speed_limit").getValue() != null)
                                mViewHolder.editTextFieldSpeedLimit.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("speed_limit").getValue()));
                            if (dataSnapshot.child("usuarios").child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("track_object").getValue() != null)
                                mViewHolder.editTextFieldTrackObject.setText(String.valueOf(dataSnapshot.child("usuarios").
                                        child(mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).child("track_object").getValue()));
                            Log.i(TAG, "Dados obtidos com sucesso.");
                            Toast.makeText(MainActivity.this, "Dados obtidos com sucesso!.", Toast.LENGTH_SHORT).show();
                            mViewHolder.progressBarMain.setVisibility(View.GONE);
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

    private static class ViewHolder{
        Toolbar toolbarMain;
        FirebaseAuth firebaseAuthAutenticacao;
        DatabaseReference databaseReferenceProjetoAnsuz;
        TextInputEditText editTextFieldConfidence, editTextFieldDistance,
                editTextFieldFrameWidth, editTextFieldLocal, editTextFieldMaxDisappear,
                editTextFieldMaxDistance, editTextFieldSpeedLimit, editTextFieldTrackObject;
        ProgressBar progressBarMain;
        Dialog dialogSobre;
        TextView textVersion;
        ImageView imageViewClose;
        Snackbar snackBarAtt;
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
            case R.id.btnSaveConfig:
                this.mViewHolder.progressBarMain.setVisibility(View.VISIBLE);
                SalvarConfigs();
                break;
            case R.id.action_sobre:
                mViewHolder.dialogSobre.setContentView(R.layout.popup_sobre);
                mViewHolder.textVersion = this.mViewHolder.dialogSobre.findViewById(R.id.textVersion);
                mViewHolder.imageViewClose = this.mViewHolder.dialogSobre.findViewById(R.id.imageViewClose);

                try {
                    this.mViewHolder.textVersion.setText("Versão do Aplicativo: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                }catch (PackageManager.NameNotFoundException e){
                    Log.e(TAG, "No name version");
                }

                this.mViewHolder.imageViewClose.setOnClickListener(this);

                this.mViewHolder.dialogSobre.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                this.mViewHolder.dialogSobre.show();
                break;
            case R.id.action_getout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final AlertDialog alert = builder.setTitle(R.string.sair_conta)
                        .setMessage(R.string.deseja_sair)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deslogarUsuario();
                                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(login);
                                finish(); //finaliza activity
                            }
                        })
                        .setNegativeButton(R.string.cancelar, null)
                        .create();

                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                });
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewClose:
                this.mViewHolder.dialogSobre.dismiss();
        }
    }

    public void deslogarUsuario() {
        try {
            this.mViewHolder.firebaseAuthAutenticacao.signOut(); //aqui desloga o usuário ativo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SalvarConfigs(){
        try {
            if (isOnline()) {
                if (!this.mViewHolder.editTextFieldConfidence.getText().toString().isEmpty())
                    this.mViewHolder.databaseReferenceProjetoAnsuz.child("usuarios").
                            child(this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).
                            child("confidence").setValue(this.mViewHolder.editTextFieldConfidence.getText().toString());
                if (!this.mViewHolder.editTextFieldDistance.getText().toString().isEmpty())
                    this.mViewHolder.databaseReferenceProjetoAnsuz.child("usuarios").
                            child(this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).
                            child("distance").setValue(this.mViewHolder.editTextFieldDistance.getText().toString());
                if (!this.mViewHolder.editTextFieldFrameWidth.getText().toString().isEmpty())
                    this.mViewHolder.databaseReferenceProjetoAnsuz.child("usuarios").
                            child(this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).
                            child("frame_width").setValue(this.mViewHolder.editTextFieldFrameWidth.getText().toString());
                if (!this.mViewHolder.editTextFieldLocal.getText().toString().isEmpty())
                    this.mViewHolder.databaseReferenceProjetoAnsuz.child("usuarios").
                            child(this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).
                            child("local").setValue(this.mViewHolder.editTextFieldLocal.getText().toString());
                if (!this.mViewHolder.editTextFieldMaxDisappear.getText().toString().isEmpty())
                    this.mViewHolder.databaseReferenceProjetoAnsuz.child("usuarios").
                            child(this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).
                            child("max_disappear").setValue(this.mViewHolder.editTextFieldMaxDisappear.getText().toString());
                if (!this.mViewHolder.editTextFieldMaxDistance.getText().toString().isEmpty())
                    this.mViewHolder.databaseReferenceProjetoAnsuz.child("usuarios").
                            child(this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).
                            child("max_distance").setValue(this.mViewHolder.editTextFieldMaxDistance.getText().toString());
                if (!this.mViewHolder.editTextFieldSpeedLimit.getText().toString().isEmpty())
                    this.mViewHolder.databaseReferenceProjetoAnsuz.child("usuarios").
                            child(this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).
                            child("speed_limit").setValue(this.mViewHolder.editTextFieldSpeedLimit.getText().toString());
                if (!this.mViewHolder.editTextFieldTrackObject.getText().toString().isEmpty())
                    this.mViewHolder.databaseReferenceProjetoAnsuz.child("usuarios").
                            child(this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser().getUid()).
                            child("track_object").setValue(this.mViewHolder.editTextFieldTrackObject.getText().toString());
                this.mViewHolder.snackBarAtt = Snackbar.make(findViewById(R.id.linearLayout), R.string.configuracao_att, Snackbar.LENGTH_LONG);
                this.mViewHolder.snackBarAtt.show();
                Log.i(TAG, "Configuração atualizada.");
//                Toast.makeText(MainActivity.this, "Configuração atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                this.mViewHolder.progressBarMain.setVisibility(View.GONE);
            }else {
                this.mViewHolder.progressBarMain.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Você não está conectado a Internet.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erro ao obter dados.");
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Não foi possível obter dados do usuário.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Erro ao obter dados.");
        }
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
