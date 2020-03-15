package com.example.tcc_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Dia atual = (Dia) getIntent().getSerializableExtra("dadosDia");

        TextView diaTextView = findViewById(R.id.numero_dia);
        TextView entradaTextView = findViewById(R.id.texto_entrada);
        TextView intervaloTextView = findViewById(R.id.texto_intervalo);
        TextView voltaIntervaloTextView = findViewById(R.id.texto_voltaIntervalo);
        TextView saidaTextView = findViewById(R.id.texto_saida);
        TextView totalTextView = findViewById(R.id.texto_total);
        if (atual != null){ //verifica se recebeu o objeto de filme

            this.setTitle("DIA"); //muda o título da Activity
            //atribuiu os valores para cada view
            diaTextView.setText(atual.getDia());
            entradaTextView.setText(atual.getEntrada());
            intervaloTextView.setText(atual.getIntervalo());
            voltaIntervaloTextView.setText(atual.getVoltaIntervalo());
            saidaTextView.setText(atual.getSaida());
            totalTextView.setText(atual.getTotal());
            System.out.println("OLAAAAAAAAAAAA");
        }else{
            //finaliza a Intent
            this.finish();
        }
    }
}
