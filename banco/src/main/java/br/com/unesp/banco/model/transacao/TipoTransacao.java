package br.com.unesp.banco.model.transacao;

/**
 * Enumeração dos tipos de transações aceitos pelo sistema.
 */
public enum TipoTransacao {
    DEPOSITO,
    SAQUE,
    TRANSFERENCIA_ENVIADA,
    TRANSFERENCIA_RECEBIDA,
    PIX,
    BOLETO,
    CARTAO,
    EMPRESTIMO,
    SIMULACAO
}