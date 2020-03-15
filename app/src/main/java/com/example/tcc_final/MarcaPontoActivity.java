package com.example.tcc_final;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MarcaPontoActivity extends AppCompatActivity {

    Button btnInicio, btnIntervalo, btnVoltar, btnSaida;
    private boolean clicked, clicked2, clicked3, clicked4 = false;
    private static final int v = 1;
    String entrada;
    String entradaHora;
    String entradaMinuto;
    String intervalo;
    String intervaloHora;
    String intervaloMinuto;
    String saidaIntervalo;
    String saidaIntervaloHora;
    String saidaIntervaloMinuto;
    String saida;
    String saidaHora;
    String saidaMinuto;
    String userId = "1";
    String dia;
    String mes;
    String ano;
    int totalHoras;
    int totalMinutos;
    String Uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marca_ponto);
        Uid = (String) getIntent().getExtras().get("Uid");
        System.out.println("AAAAAAAAAAAAmarcapontoA"+Uid);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //child deve ser mudado para Uid que vai ser passado por referencia desde a pagina de login
        final DatabaseReference myRefRead = database.getReference("usuarios").child(Uid).child("dados");
        // Read from the database
        myRefRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Dados dados = dataSnapshot.getValue(Dados.class);

                final TextView nomeUser = findViewById(R.id.nomeUsuario);

                TextView text = (TextView) findViewById(R.id.nomeUsuario);
                text.setText("Olá, " + dados.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnInicio = findViewById(R.id.btnEntrar);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonHorasClicked(view);
                btnIntervalo.setVisibility(view.VISIBLE);
                clicked=true;
            }

        });


        btnIntervalo = findViewById(R.id.btnIntervalo);
        btnIntervalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked2=true;
                // if(btnInicio){
                //   btnIntervalo.setClickable(true);
                onButtonHorasClicked(view);
                btnVoltar.setVisibility(View.VISIBLE);

            } //else{   Toast toast = Toast.makeText(context, "Marque primeiro o inicio do turno", Toast.LENGTH_SHORT);
            //             toast.setGravity(Gravity.CENTER, 0, 0);
            //            toast.show(); }

            // }
        });


        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonHorasClicked(view);
                clicked3=true;
                btnSaida.setVisibility(View.VISIBLE);
            }
        });

        btnSaida = findViewById(R.id.btnSaida);
        btnSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonHorasClicked(view);
                clicked4=true;
                //btnSaida.setVisibility(View.VISIBLE);
            }
        });




        /*

        Button bt = view.findViewById(R.id.bt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getActivity() , "Clicado BT Fragment" , Toast.LENGTH_LONG).show();
            }
        });

         */


    }//oncreate

    private void writeNewUser(String entrada, String intervalo, String saidaIntervalo, String saida, String userId, String data, String totalHoras, String totalMinutos, String totalDia) {
        HorasTrabalhadas horasTrabalhadas = new HorasTrabalhadas(entrada, intervalo, saidaIntervalo, saida, totalHoras, totalMinutos, totalDia);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");
        myRef.child(Uid).child("Calendario").child(ano).child(mes).child(dia).setValue(horasTrabalhadas);
    }

    public String getData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat diaFormat = new SimpleDateFormat("dd");
        SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
        SimpleDateFormat anoFormat = new SimpleDateFormat("yyyy");
        Date data = new Date();
        Calendar  cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        dia = diaFormat.format(data_atual);
        mes = mesFormat.format(data_atual);
        ano = anoFormat.format(data_atual);
        String data_completa = dateFormat.format(data_atual);
        return data_completa;
    }

    public void calculaTotalHoras (String entradaHora, String intervaloHora, String saidaIntervaloHora, String saidaHora){

        int intEntrada = Integer.parseInt(entradaHora);
        int intIntervalo = Integer.parseInt(intervaloHora);
        int intSaidaIntervalo = Integer.parseInt(saidaIntervaloHora);
        int intSaida = Integer.parseInt(saidaHora);
        totalHoras = (intIntervalo - intEntrada) + (intSaida - intSaidaIntervalo);
    }

    public void somaMinutos(String entradaMinuto, String intervaloMinuto, String saidaIntervaloMinuto, String saidaMinuto){

        int intEntrada = Integer.parseInt(entradaMinuto);
        int intIntervalo = Integer.parseInt(intervaloMinuto);
        int intSaidaIntervalo = Integer.parseInt(saidaIntervaloMinuto);
        int intSaida = Integer.parseInt(saidaMinuto);
        totalMinutos = (intIntervalo - intEntrada) + (intSaida - intSaidaIntervalo);
        while(totalMinutos >= 60){
            totalMinutos -= 60;
            totalHoras += 1;
        }
    }

    public void onButtonHorasClicked(View v){
        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH");
        SimpleDateFormat dateFormat_minuto = new SimpleDateFormat("mm");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String hora_atual = dateFormat_hora.format(data_atual);
        String minuto_atual = dateFormat_minuto.format(data_atual);

        switch(v.getId()) {//pega o id da view que chamou o método
            case R.id.btnEntrar:

                (v.findViewById(R.id.btnEntrar)).setEnabled(false);
                //(v.findViewById(R.id.btnIntervalo)).setVisibility(View.VISIBLE);
                TextView textoEntrar = v.findViewById(R.id.btnEntrar);
                textoEntrar.setText(hora_atual + ":" + minuto_atual);
                entradaHora = hora_atual;
                entradaMinuto = minuto_atual;
                entrada = entradaHora + ":" + entradaMinuto;
                clicked = true;
                break;

            case R.id.btnIntervalo:

                ((Button)v.findViewById(R.id.btnIntervalo)).setEnabled(false);
                //((Button)v.findViewById(R.id.btnVoltar)).setVisibility(View.VISIBLE);
                TextView textoIntervalo = v.findViewById(R.id.btnIntervalo);
                textoIntervalo.setText(hora_atual + ":" + minuto_atual);
                intervaloHora = hora_atual;
                intervaloMinuto = minuto_atual;
                intervalo = intervaloHora + ":" + intervaloMinuto;
                break;

            case R.id.btnVoltar:
                ((Button)v.findViewById(R.id.btnVoltar)).setEnabled(false);
                // ((Button)v.findViewById(R.id.btnSaida)).setVisibility(View.VISIBLE);
                TextView textoVoltar = v.findViewById(R.id.btnVoltar);
                textoVoltar.setText(hora_atual + ":" + minuto_atual);
                saidaIntervaloHora = hora_atual;
                saidaIntervaloMinuto = minuto_atual;
                saidaIntervalo = saidaIntervaloHora + ":" + saidaIntervaloMinuto;
                break;

            case R.id.btnSaida:
                ((Button)v.findViewById(R.id.btnSaida)).setEnabled(false);
                TextView textoSair = v.findViewById(R.id.btnSaida);
                textoSair.setText(hora_atual + ":" + minuto_atual);
                saidaHora = hora_atual;
                saidaMinuto = minuto_atual;
                saida = saidaHora + ":" + saidaMinuto;
                somaMinutos(entradaMinuto, intervaloMinuto, saidaIntervaloMinuto, saidaMinuto);
                calculaTotalHoras(entradaHora, intervaloHora, saidaIntervaloHora, saidaHora);
                String totalDia = totalHoras +":"+ totalMinutos;
                String totalHorasString = "" + totalHoras;
                String totalMinutosString = "" + totalMinutos;
                writeNewUser(entrada, intervalo, saidaIntervalo, saida, userId, getData(), totalHorasString, totalMinutosString, totalDia);

                String diaTrabalhado = "dia" + dia;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("usuarios");
                myRef.child(Uid).child("Calendario").child(ano).child(mes).child("diasTrabalhados").child(diaTrabalhado).setValue(dia);
                break;
        }
    }


}
