package com.thais.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.thais.whatsapp.R;
import com.thais.whatsapp.config.ConfigFirebase;
import com.thais.whatsapp.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        campoNome = findViewById(R.id.editName);
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginPassword);
    }

    public void salvarUsuarioFirebase(Usuario usuario) {
        auth = ConfigFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
            usuario.getEmail(), usuario.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String exception = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        exception = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "Digite email válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        exception = "Essa conta já foi cadastrada";
                    } catch (Exception e) {
                        exception = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarCadastroUsuario(View view) {
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if (textoNome.isEmpty() || textoEmail.isEmpty() || textoSenha.isEmpty()){
            Toast.makeText(CadastroActivity.this, "Preencha esse campo", Toast.LENGTH_SHORT).show();
        } else {
            Usuario usuario = new Usuario();
            usuario.setName(textoNome);
            usuario.setEmail(textoEmail);
            usuario.setPassword(textoSenha);
            salvarUsuarioFirebase(usuario);
        }
    }

}