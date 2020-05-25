package com.example.tcc_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void confereLogin(final View v) {
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Carregando. Por favor aguarde...", true);
        findViewById(R.id.botaoentrar).setEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        //pega email digitado pelo usuario
        final String emailDigitado = ((EditText) findViewById(R.id.boxusuario)).getText().toString(); //pega nome digitado na pagina login
        //pega senha digitada pelo usuario
        final String senhaDigitada = ((EditText) findViewById(R.id.boxsenha)).getText().toString(); //pega senha digitada na pagina login
        final boolean[] ok = {false};
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("usuarios");
        try {
            //confere se o email e senha digitados confere com o autenticado no banco de dados
            mAuth.signInWithEmailAndPassword(emailDigitado, senhaDigitada)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                //user tem todas as informações do usuario
                                FirebaseUser user = mAuth.getCurrentUser();
                                //Esse user.getUid() é aquela chave do usuario, tem que achar um jeito de passar
                                //ela pelos parametros pra que a gente nao precise buscar ela toda hora
                                findViewById(R.id.botaoentrar).setEnabled(true);
                                dialog.dismiss();
                                openNavigation(user.getUid());
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                findViewById(R.id.botaoentrar).setEnabled(true);
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Email e/ou senha incorretos.", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }catch (Exception e){
            System.out.println(e);
            findViewById(R.id.botaoentrar).setEnabled(true);
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Insira seu email e senha.", Toast.LENGTH_SHORT).show();
        }

    }

    public void openNavigation(String Uid) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("Uid", Uid);
        startActivity(intent);
    }

    public void semCadastro(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}
