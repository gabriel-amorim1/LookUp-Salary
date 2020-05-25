package com.example.tcc_final;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String Uid;
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
    int pointsMarked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
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
        Uid = (String) getIntent().getExtras().get("Uid");
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
        retornaDiasTrabalhados();

        btnInicio = findViewById(R.id.btnEntrar);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonHorasClicked(view);
                btnIntervalo.setVisibility(view.VISIBLE);
                clicked=true;
                pointsMarked=1;
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
                pointsMarked=2;
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
                pointsMarked=3;
            }
        });

        btnSaida = findViewById(R.id.btnSaida);
        btnSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonHorasClicked(view);
                clicked4=true;
                //btnSaida.setVisibility(View.VISIBLE);
                pointsMarked=4;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(NavigationActivity.this, NewPointActivity.class);
                Uid = (String) getIntent().getExtras().get("Uid");
                intent.putExtra("Uid", Uid);
                intent.putExtra("pointsMarked", pointsMarked);
                startActivity(intent);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        retornaDiasTrabalhados();
    }

    private void retornaDiasTrabalhados() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //path deve ser mudado para "usuarios" e child "1" deve ser mudado para Uid que vai ser passado por referencia desde a pagina de login
        final DatabaseReference myRefRead = database.getReference("usuarios").child(Uid).child("Calendario").child(ano).child(mes).child(dia);

        // Read from the database
        myRefRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //pega todos os dados em descontos
                if (dataSnapshot.getValue() != null) {
                    try {
                        HorasTrabalhadas dados = dataSnapshot.getValue(HorasTrabalhadas.class);
                        if (dados.entrada != null) {
                            findViewById(R.id.btnEntrar).setEnabled(false);
                            TextView textoEntrar = findViewById(R.id.btnEntrar);
                            textoEntrar.setText(dados.entrada);
                            btnIntervalo.setVisibility(View.VISIBLE);
                            clicked = true;
                            pointsMarked = 1;
                        }
                        if (dados.intervalo != null) {
                            findViewById(R.id.btnIntervalo).setEnabled(false);
                            TextView textoIntervalo = findViewById(R.id.btnIntervalo);
                            textoIntervalo.setText(dados.intervalo);
                            clicked2 = true;
                            btnVoltar.setVisibility(View.VISIBLE);
                            pointsMarked = 2;
                        }
                        if (dados.saidaIntervalo != null) {
                            findViewById(R.id.btnVoltar).setEnabled(false);
                            TextView textoVoltar = findViewById(R.id.btnVoltar);
                            textoVoltar.setText(dados.saidaIntervalo);
                            clicked3 = true;
                            btnSaida.setVisibility(View.VISIBLE);
                            pointsMarked = 3;
                        }
                        if (dados.saida != null) {
                            findViewById(R.id.btnSaida).setEnabled(false);
                            TextView textoSair = findViewById(R.id.btnSaida);
                            textoSair.setText(dados.saida);
                            clicked4 = true;
                            pointsMarked = 4;
                        }
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("NavigationActivity", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Uid = (String) getIntent().getExtras().get("Uid");
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra("Uid", Uid);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Uid = (String) getIntent().getExtras().get("Uid");
            Intent intent = new Intent(this, CalendarioActivity.class);
            intent.putExtra("Uid", Uid);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
//            Uid = (String) getIntent().getExtras().get("Uid");
//            Intent intent = new Intent(this, PreviaDetalhesActivity.class);
//            intent.putExtra("Uid", Uid);
//            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void writeNewUser(String point, String hora){
        //HorasTrabalhadas horasTrabalhadas = new HorasTrabalhadas(entrada, intervalo, saidaIntervalo, saida, totalHoras, totalMinutos, totalDia);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");
        myRef.child(Uid).child("Calendario").child(ano).child(mes).child(dia).child(point).setValue(hora);
    }

    public String getData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
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
                writeNewUser("entrada", entrada);
                break;

            case R.id.btnIntervalo:

                ((Button)v.findViewById(R.id.btnIntervalo)).setEnabled(false);
                //((Button)v.findViewById(R.id.btnVoltar)).setVisibility(View.VISIBLE);
                TextView textoIntervalo = v.findViewById(R.id.btnIntervalo);
                textoIntervalo.setText(hora_atual + ":" + minuto_atual);
                intervaloHora = hora_atual;
                intervaloMinuto = minuto_atual;
                intervalo = intervaloHora + ":" + intervaloMinuto;
                writeNewUser("intervalo", intervalo);
                break;

            case R.id.btnVoltar:
                ((Button)v.findViewById(R.id.btnVoltar)).setEnabled(false);
                // ((Button)v.findViewById(R.id.btnSaida)).setVisibility(View.VISIBLE);
                TextView textoVoltar = v.findViewById(R.id.btnVoltar);
                textoVoltar.setText(hora_atual + ":" + minuto_atual);
                saidaIntervaloHora = hora_atual;
                saidaIntervaloMinuto = minuto_atual;
                saidaIntervalo = saidaIntervaloHora + ":" + saidaIntervaloMinuto;
                writeNewUser("saidaIntervalo", saidaIntervalo);
                break;

            case R.id.btnSaida:
                ((Button)v.findViewById(R.id.btnSaida)).setEnabled(false);
                TextView textoSair = v.findViewById(R.id.btnSaida);
                textoSair.setText(hora_atual + ":" + minuto_atual);
                saidaHora = hora_atual;
                saidaMinuto = minuto_atual;
                saida = saidaHora + ":" + saidaMinuto;
                //somaMinutos(entradaMinuto, intervaloMinuto, saidaIntervaloMinuto, saidaMinuto);
                //calculaTotalHoras(entradaHora, intervaloHora, saidaIntervaloHora, saidaHora);
                String totalDia = totalHoras +":"+ totalMinutos;
                String totalHorasString = "" + totalHoras;
                String totalMinutosString = "" + totalMinutos;
                writeNewUser("saida", saida);

                String diaTrabalhado = "dia" + dia;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("usuarios");
                myRef.child(Uid).child("Calendario").child(ano).child(mes).child("diasTrabalhados").child(diaTrabalhado).setValue(dia);
                break;
        }
    }
}
