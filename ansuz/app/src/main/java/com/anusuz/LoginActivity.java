package com.anusuz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anusuz.config.ConfiguracaoFirebase;
import com.anusuz.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
//    private TextInputEditText campoEmail, campoSenha;
//    private FirebaseAuth autenticacao;
//    private ProgressBar progressoCarregandoLogin;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!/])(?=\\S+$).{6,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //recuperando instância do firebase para realizar a autenticação
        this.mViewHolder.firebaseAuthAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        this.mViewHolder.editTextEmail = findViewById(R.id.editLoginEmail);
        this.mViewHolder.editTextSenha = findViewById(R.id.editLoginSenha);
        this.mViewHolder.progressBarLogin = findViewById(R.id.progressBarLogin);
        this.mViewHolder.progressBarLogin.setVisibility(View.GONE);

        //Valida campo de email da tela de Login
        this.mViewHolder.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String message = getString(R.string.invalid_email);
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    mViewHolder.editTextEmail.setError(message);
                }
            }
        });
    }

    private static class ViewHolder{
        TextInputEditText editTextEmail, editTextSenha;
        FirebaseAuth firebaseAuthAutenticacao;
        ProgressBar progressBarLogin;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!/])(?=\\S+$).{6,}$";

    }

    public void loginUsuario(Usuario usuario) {
        this.mViewHolder.firebaseAuthAutenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mViewHolder.progressBarLogin.setVisibility(View.GONE);
                            abrirTelaPrincipal();
                        } else {
                            String excecao = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                excecao = "Usuário não cadastrado.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "E-mail ou senha incorretos.";
                            } catch (Exception e) {
                                excecao = "Erro ao realizar login: " + e.getMessage();
                                e.printStackTrace();
                            }
                            mViewHolder.progressBarLogin.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, excecao,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void validarAutenticacaoUsuario(View view) {
        this.mViewHolder.progressBarLogin.setVisibility(View.VISIBLE);
        String textoEmail = this.mViewHolder.editTextEmail.getText().toString();
        String textoSenha = this.mViewHolder.editTextSenha.getText().toString();

        //validando os dados de entrada
        if (!textoEmail.isEmpty()) { //verifica email
            if (!textoSenha.isEmpty()) { //verifica senha
                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);

                loginUsuario(usuario);
            } else {
                Toast.makeText(LoginActivity.this, "Preencha a sua senha!", Toast.LENGTH_SHORT).show();
                this.mViewHolder.progressBarLogin.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(LoginActivity.this, "Preencha o seu e-mail!", Toast.LENGTH_SHORT).show();
            this.mViewHolder.progressBarLogin.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = this.mViewHolder.firebaseAuthAutenticacao.getCurrentUser();

        //se um usuário já estiver logado direciona direto para a página inicial ao abrir o aplicativo
        if (usuarioAtual != null) {
            abrirTelaPrincipal();
        }
    }

//    public void abrirTelaCadastro(View view) {
//        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
//        startActivity(intent);
//    }

    public void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
