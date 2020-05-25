package com.example.tcc_final;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewPointActivity extends AppCompatActivity {
    String Uid;
    String pointsMarked;
    String point;
    String dia;
    String mes;
    String ano;
    private FirebaseAuth mAuth;
    EditText etExitHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_point);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Uid = (String) getIntent().getExtras().get("Uid");
        pointsMarked = ""+getIntent().getExtras().get("pointsMarked");
        SimpleDateFormat diaFormat = new SimpleDateFormat("dd");
        SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
        SimpleDateFormat anoFormat = new SimpleDateFormat("yyyy");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        dia = diaFormat.format(data_atual);
        mes = mesFormat.format(data_atual);
        ano = anoFormat.format(data_atual);
        etExitHour = findViewById(R.id.etExitHour);
        checkPoints(pointsMarked);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void checkPoints(String pointsMarked){
        TextView tv_PointMessage = findViewById(R.id.tv_PointMessage);
        switch (pointsMarked){
            case "0":
                tv_PointMessage.setText("Você ainda não bateu nenhum ponto hoje. Caso tenha esquecido de bater ponto quando chegou digite a hora e os minutos em que chegou e clique em Adicionar");
                point = "entrada";
                break;
            case "1":
                tv_PointMessage.setText("Você ainda não bateu o ponto do início do intervalo. Caso tenha esquecido de bater ponto digite a hora e os minutos e clique em Adicionar");
                point = "intervalo";
                break;
            case "2":
                tv_PointMessage.setText("Você ainda não bateu o ponto do término do intervalo. Caso tenha esquecido de bater ponto digite a hora e os minutos e clique em Adicionar");
                point = "saidaIntervalo";
                break;
            case "3":
                tv_PointMessage.setText("Você ainda não bateu o ponto de saída da empresa. Caso tenha esquecido de bater ponto digite a hora e os minutos e clique em Adicionar");
                point = "saida";
                break;
            case "4":
                tv_PointMessage.setText("Você já bateu todos os pontos de hoje, não é possível adicionar mais.");
                Button btnNewPoint = findViewById(R.id.btnNewPoint);
                btnNewPoint.setEnabled(false);
                break;
        }
    }

    public void addNewPoint(View v){
        String exitHour = etExitHour.getText().toString();
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date horaAtual = cal.getTime();
        String hora_atual = parser.format(horaAtual);
        try {
            Date present = parser.parse(hora_atual);
            Date closed = parser.parse(exitHour);
            if (present.after(closed)) {
                sendNewPointToDB(exitHour);
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }

    }

    public void sendNewPointToDB(String exitHour){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");
        myRef.child(Uid).child("Calendario").child(ano).child(mes).child(dia).child(point).setValue(exitHour);
        Toast.makeText(NewPointActivity.this, "Ponto adicionado com sucesso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("Uid", Uid);
        startActivity(intent);
    }
}
