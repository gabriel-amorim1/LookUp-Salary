package com.example.tcc_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarioActivity extends AppCompatActivity {
    private static final String TAG = "CalendarioActivity";
    Button btn_analisar;
    final static String ARG_POSITION = "position";
    // lista de objetos Dia
    ArrayList<Dia> listaDia = new ArrayList<>();
    ArrayList<String> diasContabilizados = new ArrayList<>();
    ArrayList<HorasTrabalhadas> info = new ArrayList<>();
    String Uid;
    String dia;
    String mes;
    String ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        Uid = (String)getIntent().getExtras().get("Uid");
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
        retornaDiasTrabalhados();

    }

    private void retornaDiasTrabalhados(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //path deve ser mudado para "usuarios" e child "1" deve ser mudado para Uid que vai ser passado por referencia desde a pagina de login
        final DatabaseReference myRefRead = database.getReference("usuarios").child(Uid).child("Calendario").child(ano).child(mes).child("diasTrabalhados");

        // Read from the database
        myRefRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //pega todos os dados em descontos
                if(dataSnapshot.getValue()!=null) {
                    String dados = dataSnapshot.getValue().toString();
                    String[] aux = dados.split(",");
                    for (int i = 0; i < aux.length; i++) {
                        diasContabilizados.add(separaDados(aux[i], i, aux.length)[1]);
                    }
                    recebeDadosDia();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void recebeDadosDia(){
        final boolean[] ok = {false};
        //path deve ser mudado para "usuarios" e child "1" deve ser mudado para Uid que vai ser passado por referencia desde a pagina de login
        for(int i = 0; i<diasContabilizados.size(); i++) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRefRead = database.getReference("usuarios").child(Uid).child("Calendario").child(ano).child(mes).child(diasContabilizados.get(i));
            // Read from the database
            final int finalI = i;
            myRefRead.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    //pega todos os dados em descontos
                    HorasTrabalhadas dados = dataSnapshot.getValue(HorasTrabalhadas.class);
                    if(dados!=null) {
                        info.add(dados);
                        if(finalI+1 == diasContabilizados.size()){
                            populaLista();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }

    public void populaLista(){
        for(int i = 0; i<diasContabilizados.size(); i++){
            Dia aux = new Dia(diasContabilizados.get(i), info.get(i).totalDia);
            aux.setEntrada(info.get(i).entrada);
            aux.setIntervalo(info.get(i).intervalo);
            aux.setVoltaIntervalo(info.get(i).saidaIntervalo);
            aux.setSaida(info.get(i).saida);
            aux.setTotal(info.get(i).totalDia);
            listaDia.add(aux);
        }
        //cria um objeto do tipo FilmeAdapter (ArrayAdapter)
        final DiaAdapter adapter = new DiaAdapter(this,listaDia);
        //identifica a ListView no layout
        ListView listview = (ListView) findViewById(R.id.list_dias);
        //seta o adapter à ListView
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dia atual = adapter.getItem(position);
                Intent intent = new Intent(CalendarioActivity.this, InfoActivity.class);
                intent.putExtra("dadosDia", atual);
                System.out.println("AQUUUUUUUUUIIIIIIIIIIIII");
                startActivity(intent);

                //AÇÃO QUE DEVE ACONTECER
            }
        });


    }


    private String[] separaDados(String dados, int i, int tamanhoAux){
        String[] aux;

        String a = dados.replace(" ", "");
        dados = a;
        aux = dados.split("=");
        if(i==0){
            String auxNome = aux[0].replace("{", "");
            aux[0] = auxNome;
        }
        if(i==tamanhoAux-1){
            String auxNome = aux[1].replace("}", "");
            aux[1] = auxNome;
        }
        return aux;
    }

}
