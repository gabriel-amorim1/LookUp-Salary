package com.example.tcc_final;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.lang.Double;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Dados dados;
    ArrayList<Desconto> listaDecontos = new ArrayList<Desconto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void onClickCadastrar(View v){
        EditText editTextEmail = findViewById(R.id.editText_email);
        String email = editTextEmail.getText().toString();
        EditText editTextSenha = findViewById(R.id.editText_senha);
        String senha = editTextSenha.getText().toString();
        EditText editTextNome = findViewById(R.id.editText_nome);
        String nome = editTextNome.getText().toString();
        EditText editTextSobrenome = findViewById(R.id.editText_sobrenome);
        String sobrenome = editTextSobrenome.getText().toString();
        EditText editTextSalarioBruto = findViewById(R.id.editText_salarioBruto);
        String salarioBruto = editTextSalarioBruto.getText().toString();
        EditText editTextHorasDiarias = findViewById(R.id.editText_horasDiarias);
        String horasDiarias = editTextHorasDiarias.getText().toString();

        dados = new Dados(email, senha,  nome, sobrenome, salarioBruto,horasDiarias);
        createNewUser();
    }

    public void createNewUser(){
        mAuth.createUserWithEmailAndPassword(dados.getEmail(), dados.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("CadastroActivity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeOnDatabase(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("CadastroActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CadastroActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void writeOnDatabase(String Uid){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");
        myRef.child(Uid).child("dados").setValue(dados);
        //calculaDescontos(Uid);
        openNavigation(Uid);
    }

    public void calculaDescontos(String Uid){
        listaDecontos.add(new Desconto("INSS", calculaINSS()));
        listaDecontos.add(new Desconto("IRRF", calculaIRRF()));
        cadastraDescontos(Uid);
    }

    public void cadastraDescontos(String Uid){
        EditText editTextValeTransporte = findViewById(R.id.editText_valetransporte);
        String valeTransporte = editTextValeTransporte.getText().toString();
        valeTransporte = valeTransporte.replace(",", ".");
        double VT = Double.parseDouble(valeTransporte);
        if(VT>calculaValeTransporte()){
            listaDecontos.add(new Desconto("Vale transporte", ""+calculaValeTransporte()));
        }else{
            listaDecontos.add(new Desconto("Vale transporte", ""+VT));
        }

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");
        for(int i=0; i<listaDecontos.size(); i++) {
            myRef.child(Uid).child("descontos").child(listaDecontos.get(i).getNome()).setValue(listaDecontos.get(i).getValor());
        }
        cadastraBeneficios(Uid);
    }

    public void cadastraBeneficios(String Uid){
        EditText editTextValeRefeicao = findViewById(R.id.editText_valerefeicao);
        String valeRefeicao = editTextValeRefeicao.getText().toString();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");

        myRef.child(Uid).child("beneficios").child("Vale Refeicao").setValue(valeRefeicao);
        openNavigation(Uid);

    }

    public void openNavigation(String Uid) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("Uid", Uid);
        startActivity(intent);
    }

    public String calculaINSS(){
        double INSS = 0.00;
        double salarioBruto = Double.parseDouble(dados.getSalarioBruto());
        DecimalFormat formato = new DecimalFormat("#.##");
        salarioBruto = Double.valueOf(formato.format(salarioBruto));
        if(salarioBruto <= 1693.72){
            INSS = (salarioBruto * 8) / 100;
        }
        if(salarioBruto >= 1693.73 && salarioBruto <= 2822.90){
            INSS = (salarioBruto * 9) / 100;
        }
        if(salarioBruto >= 2822.91){
            INSS = (salarioBruto * 11) / 100;
        }
        INSS = Double.valueOf(formato.format(INSS));
        return ""+INSS;
    }

    public String calculaIRRF(){
        double IRRF = 0.00;
        double salarioBruto = Double.parseDouble(dados.getSalarioBruto());
        DecimalFormat formato = new DecimalFormat("#.##");
        salarioBruto = Double.valueOf(formato.format(salarioBruto));
        if(salarioBruto <= 1903.98){
            IRRF = 0;
        }
        if(salarioBruto >= 1903.99 && salarioBruto <= 2826.65){
            IRRF = (salarioBruto * 7.5) / 100;
        }
        if(salarioBruto >= 2826.66 && salarioBruto <= 3751.05){
            IRRF = (salarioBruto * 15) / 100;
        }
        if(salarioBruto >= 3751.06 && salarioBruto <= 4664.68){
            IRRF = (salarioBruto * 22.5) / 100;
        }
        if(salarioBruto > 4664.68){
            IRRF = (salarioBruto * 27.5) / 100;
        }
        IRRF = Double.valueOf(formato.format(IRRF));
        return ""+IRRF;
    }

    public double calculaValeTransporte(){
        double valeTransporte = 0.00;
        double salarioBruto = Double.parseDouble(dados.getSalarioBruto());
        DecimalFormat formato = new DecimalFormat("#.##");
        salarioBruto = Double.valueOf(formato.format(salarioBruto));

        valeTransporte = (salarioBruto * 6) / 100;

        valeTransporte = Double.valueOf(formato.format(valeTransporte));
        return valeTransporte;
    }

}