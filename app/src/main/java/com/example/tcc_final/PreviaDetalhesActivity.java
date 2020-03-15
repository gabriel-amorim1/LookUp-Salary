package com.example.tcc_final;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PreviaDetalhesActivity extends AppCompatActivity {

    private static final String TAG = "MainDescontos";
    //lista de objetos Descontos
    ArrayList<Desconto> listaDesconto = new ArrayList<Desconto>();
    ArrayList<Desconto> listaBeneficios = new ArrayList<Desconto>();
    String dia;
    String mes;
    String ano;
    String horasDiarias;
    ArrayList<String> diasContabilizados = new ArrayList<>();
    ArrayList<String> horasTrabalhadas = new ArrayList<>();
    int minutosTrabalhados = 0;
    Dados dados;
    double salarioLiquido = 0.00;
    String Uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previa_detalhes);
        Uid = (String) getIntent().getExtras().get("Uid");
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
        retornaDados();
    }

    private void retornaDados(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final boolean[] ok = {false};
        //child deve ser mudado para Uid que vai ser passado por referencia desde a pagina de login
        final DatabaseReference myRefRead = database.getReference("usuarios").child(Uid).child("dados");
        // Read from the database
        myRefRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dados = dataSnapshot.getValue(Dados.class);
                TextView tvSalarioBruto = findViewById(R.id.textView_salarioBruto);
                tvSalarioBruto.setText("R$ " + dados.getSalarioBruto());
                horasDiarias = dados.getHorasDiarias();
                retornaDiasTrabalhados();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
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
                }
                pegaBeneficios();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void pegaBeneficios(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //child deve ser mudado para Uid que vai ser passado por referencia desde a pagina de login
        final DatabaseReference myRefRead = database.getReference("usuarios").child(Uid).child("beneficios");

        // Read from the database
        myRefRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //pega todos os dados em descontos
                String dados = dataSnapshot.getValue().toString();
                salvaBeneficios(dados);
                pegaDescontos();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void pegaDescontos(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //child deve ser mudado para Uid que vai ser passado por referencia desde a pagina de login
        final DatabaseReference myRefRead = database.getReference("usuarios").child(Uid).child("descontos");

        // Read from the database
        myRefRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //pega todos os dados em descontos
                String dados = dataSnapshot.getValue().toString();
                salvaDescontos(dados);
                buscaHorasMinutosTrabalhados();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void salvaBeneficios(String dados){
        String[] aux;
        String[] aux2;

        aux = dados.split(",");

        for(int i=0; i<aux.length; i++){

            String a = aux[i].replace(" ", "");
            aux[i] = a;
            aux2 = aux[i].split("=");
            if(i==0){
                String auxNome = aux2[0].replace("{", "");
                aux2[0] = auxNome;
            }
            if(i==aux.length-1){
                String auxNome = aux2[1].replace("}", "");
                aux2[1] = auxNome;
            }
            listaBeneficios.add(new Desconto(aux2[0], aux2[1]));
            populateList();
        }
    }

    private void salvaDescontos(String dados){
        String[] aux;
        String[] aux2;

        aux = dados.split(",");

        for(int i=0; i<aux.length; i++){

            String a = aux[i].replace(" ", "");
            aux[i] = a;
            aux2 = aux[i].split("=");
            if(i==0){
                String auxNome = aux2[0].replace("{", "");
                aux2[0] = auxNome;
            }
            if(i==aux.length-1){
                String auxNome = aux2[1].replace("}", "");
                aux2[1] = auxNome;
            }
            listaDesconto.add(new Desconto(aux2[0], aux2[1]));
            populateList();
        }
    }

    private void populateList() {
        DescontoAdapter adapterDescontos;
        adapterDescontos = new DescontoAdapter(this,listaDesconto);
        //identifica a ListView no layout
        ListView listviewDescontos = (ListView) findViewById(R.id.list_descontos);
        //seta o adapter à ListView
        listviewDescontos.setAdapter(adapterDescontos);

        DescontoAdapter adapterBeneficios;
        adapterBeneficios = new DescontoAdapter(this,listaBeneficios);
        //identifica a ListView no layout
        ListView listviewBeneficios = (ListView) findViewById(R.id.list_beneficios);
        //seta o adapter à ListView
        listviewBeneficios.setAdapter(adapterBeneficios);
    }

    private void buscaHorasMinutosTrabalhados(){
        double doubleSalarioBruto = Double.parseDouble(dados.getSalarioBruto());
        salarioLiquido = doubleSalarioBruto;
        for (int i = 0; i < listaDesconto.size(); i++) {
            double valor = Double.parseDouble(listaDesconto.get(i).getValor());
            salarioLiquido -= valor;
        }
        TextView tvSalarioLiquido = findViewById(R.id.textView_salarioliquido);
        tvSalarioLiquido.setText("R$ " + salarioLiquido);
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
                        int horasLocais = Integer.parseInt(dados.totalHoras);
                        minutosTrabalhados += Integer.parseInt(dados.totalMinutos);
                        if (minutosTrabalhados >= 60) {
                            minutosTrabalhados -= 60;
                            horasLocais += 1;
                        }
                        String h = "" + horasLocais;
                        horasTrabalhadas.add(h);
                        if (finalI == diasContabilizados.size() - 1) {
                            calculaSalarioLiquidoDescontos();
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

    private void calculaSalarioLiquidoDescontos() {
        int horasSomadas = 0;
        //calcula horas totais trabalhadas no mes
        int horasTotais = retornaQuantidadeDeDiasDoMes(Integer.parseInt(mes)) *  Integer.parseInt(horasDiarias);
        //calcula quanto a pessoa recebe por hora
        double salarioPorHoraDouble = (salarioLiquido)/ horasTotais;
        DecimalFormat formato = new DecimalFormat("#.##");
        //quanto por hora do salario liquido o usuario ganha
        //salarioPorHoraDouble = Double.valueOf(formato.format(salarioPorHoraDouble));
        double salarioLiquidoDias = 0.00;
        for(int i=0; i<horasTrabalhadas.size(); i++){
            salarioLiquidoDias += salarioPorHoraDouble * Integer.parseInt(horasTrabalhadas.get(i));
            horasSomadas += Integer.parseInt(horasTrabalhadas.get(i));
        }
        float floatMinutos= minutosTrabalhados;
        salarioLiquidoDias += salarioPorHoraDouble * (floatMinutos/60);

        TextView tvHoras = findViewById(R.id.textView_horasTrabalhadas);
        tvHoras.setText(horasSomadas +"h:" + minutosTrabalhados +"min");

        //dos dias trabalhados quanto ele ja ganhou baseado nas horas armazenadas
        salarioLiquidoDias = Double.valueOf(formato.format(salarioLiquidoDias));
        TextView tvSalarioLiquidoDias = findViewById(R.id.textView_salarioConquistado);
        tvSalarioLiquidoDias.setText("R$ " + salarioLiquidoDias);

        /*int diaInt = Integer.parseInt(dia);
        int diasRestantes = (retornaQuantidadeDeDiasDoMes(Integer.parseInt(mes)) - Integer.parseInt(diasContabilizados.get(diasContabilizados.size()-1))) - (diaInt - Integer.parseInt(diasContabilizados.get(0)));
        System.out.println(diasRestantes);
        double faltaReceberSeTrabalharNormalmente = diasRestantes * Integer.parseInt(dados.getHorasDiariasCadastradas()) * salarioPorHoraDouble;
        faltaReceberSeTrabalharNormalmente = Double.valueOf(formato.format(faltaReceberSeTrabalharNormalmente));
        System.out.println(faltaReceberSeTrabalharNormalmente);*/
    }

    private int retornaQuantidadeDeDiasDoMes(int mes){
        switch (mes)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            default:
                return 30;
        }
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