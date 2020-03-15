package com.example.tcc_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DiaAdapter extends ArrayAdapter<Dia> {
    public DiaAdapter(Context context, ArrayList<Dia> listaDias) {
        super(context, 0, listaDias);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Verifica se já existe uma view criada e que pode ser reutilizada
        View listDiaView = convertView;
        // Cria a nova view, associando ao layout do item criado - parent é o layout pai em que o item está inserido
        if(listDiaView == null) {
            listDiaView = LayoutInflater.from(getContext()).inflate(R.layout.item_dia, parent, false);
        }
        // Obtem o objeto atual do ArrayList Filme
        Dia diaAtual = getItem(position);
        // Identifica a TextView no layout item_filme.xml, cuja ID é nome_filme
        TextView diaTextView = (TextView) listDiaView.findViewById(R.id.numero_dia);
        // Recupera o nome do filme relacionado ao objeto filmeAtual e atribuiu o texto na TextView nomeFilme
        diaTextView.setText("Dia " +diaAtual.getDia()+": ");
        // Identifica a TextView no layout item_filme.xml cuja a ID é genero_filme
        TextView horaTextView = (TextView) listDiaView.findViewById(R.id.quantidade_horas);
        // Recupera o gênero do filme relacionado ao objeto filmeAtual e atribuiu o texto na TextView genero
        horaTextView.setText(diaAtual.getHoras());

        return listDiaView;
    }
}
