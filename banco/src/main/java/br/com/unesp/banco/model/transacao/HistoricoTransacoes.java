package br.com.unesp.banco.model.transacao;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Classe que guarda o histórico de transações de uma conta.
 */
public class HistoricoTransacoes {
    /**
     * Lista das transações registradas.
     */
    private final List<Transacao> transacoes = new ArrayList<>();

    /**
     * Método para adicionar uma transação ao histórico.
     * @param t transação a ser registrada.
     */
    public void adicionar(Transacao t) {
        if (t != null) transacoes.add(t);
    }

    /**
     * Método para retornar todas as transações registradas.
     * @return cópia da lista de transações.
     */
    public List<Transacao> todas() {
        return List.copyOf(transacoes);
    }

    /**
     * Método para retornar as últimas transações do histórico.
     * @param n quantidade de transações desejadas.
     * @return lista com as transações mais recentes.
     */
    public List<Transacao> ultimas(int n) {
        if (n <= 0 || transacoes.isEmpty()) return new ArrayList<>();
        
        int inicio = Math.max(0, transacoes.size() - n);
        return List.copyOf(transacoes.subList(inicio, transacoes.size()));
    }

    /**
     * Método para retornar as transações dentro de um intervalo de tempo.
     * @param inicio data e hora inicial do período.
     * @param fim data e hora final do período.
     * @return lista de transações no período informado.
     */
    public List<Transacao> porPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Transacao> resultado = new ArrayList<>();
        
        for (Transacao t : transacoes) {
            if (!t.getDataHora().isBefore(inicio) && !t.getDataHora().isAfter(fim)) {
                resultado.add(t);
            }
        }
        
        return resultado;
    }
}