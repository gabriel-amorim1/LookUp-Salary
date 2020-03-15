package com.example.tcc_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DescontoAdapter extends ArrayAdapter<Desconto> {
    public DescontoAdapter(Context context, ArrayList<Desconto> listaDescontos) {
        super(context, 0, listaDescontos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Verifica se já existe uma view criada e que pode ser reutilizada
        View listDescontoView = convertView;
        // Cria a nova view, associando ao layout do item criado - parent é o layout pai em que o item está inserido
        if(listDescontoView == null) {
            listDescontoView = LayoutInflater.from(getContext()).inflate(R.layout.item_descontos, parent, false);
        }
        // Obtem o objeto atual do ArrayList Filme
        Desconto descontoAtual = getItem(position);

        // Identifica a TextView no layout item_descontos.xml, cuja ID é textViewNomeDesconto
        TextView nomeTextView = (TextView) listDescontoView.findViewById(R.id.textViewNomeDesconto);
        // Recupera o nome do desconto relacionado ao objeto descontoAtual e atribuiu o texto na TextView nomeFilme
        nomeTextView.setText(descontoAtual.getNome());
        // Identifica a TextView no layout item_descontos.xml, cuja a ID é textView_textViewValorDesconto
        TextView valorTextView = (TextView) listDescontoView.findViewById(R.id.textView_textViewValorDesconto);
        // Recupera o valor do desconto relacionado ao objeto descontoAtual e atribuiu o texto na TextView genero
        valorTextView.setText("R$ "+ descontoAtual.getValor());
        return listDescontoView;

    }

}