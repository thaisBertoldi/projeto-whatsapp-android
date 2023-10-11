package com.thais.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.thais.whatsapp.R;
import com.thais.whatsapp.config.ConfigFirebase;
import com.thais.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = ConfigFirebase.getFirebaseAuth();

        campoEmail = findViewById(R.id.editLoginEmail);
        campoPassword = findViewById(R.id.editLoginPassword);
    }

    public void login(View view) {
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoPassword.getText().toString();

        if (textoEmail.isEmpty() || textoSenha.isEmpty()){
            Toast.makeText(LoginActivity.this, "Preencha esse campo", Toast.LENGTH_SHORT).show();
        } else {
            Usuario usuario = new Usuario();
            usuario.setEmail(textoEmail);
            usuario.setPassword(textoSenha);
            authentication(usuario);
        }
    }

    private void authentication(Usuario usuario) {
        auth.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    openViewMain();
                } else {
                    String exception = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        exception = "Usuário nao cadastrado";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "Email e senha nao estão corretos";
                    } catch (FirebaseAuthUserCollisionException e) {
                        exception = "Essa conta já foi cadastrada";
                    } catch (Exception e) {
                        exception = "Erro ao efetuar o login: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openViewCadastro(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    public void openViewMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}