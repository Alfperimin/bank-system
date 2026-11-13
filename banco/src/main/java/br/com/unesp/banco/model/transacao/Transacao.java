package br.com.unesp.banco.model.transacao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

/**
 * Classe que representa uma transação financeira registrando data, tipo e valor.
 */
public class Transacao {
    /**
     * Data e hora em que a transação foi realizada.
     */
    private final LocalDateTime dataHora;
    /**
     * Tipo da transação, como depósito, saque ou transferência.
     */
    private final TipoTransacao tipo;
    /**
     * Valor associado à transação.
     */
    private final BigDecimal valor;

    /**
     * Construtor da transação com data e hora explícita.
     * @param dataHora data e hora da transação.
     * @param tipo tipo da transação.
     * @param valor valor da transação.
     */
    public Transacao(LocalDateTime dataHora, TipoTransacao tipo, BigDecimal valor) {
        if (dataHora == null || tipo == null || valor == null) {
            throw new IllegalArgumentException("Data, tipo e valor não podem ser nulos");
        }

        this.dataHora = dataHora;
        this.tipo = tipo;
        this.valor = valor;
    }

    /**
     * Construtor da transação com data e hora atual.
     * @param tipo tipo da transação.
     * @param valor valor da transação.
     */
    public Transacao(TipoTransacao tipo, BigDecimal valor){
        this(LocalDateTime.now(), tipo, valor);
    }

    /**
     * Método para retornar a data e hora da transação.
     * @return a data e hora da transação.
     */
    public LocalDateTime getDataHora() { return dataHora; }
    /**
     * Método para retornar o tipo da transação.
     * @return o tipo da transação.
     */
    public TipoTransacao getTipo() { return tipo; }
    /**
     * Método para retornar o valor da transação.
     * @return o valor da transação.
     */
    public BigDecimal getValor() { return valor; }

    /**
     * Método para representar a transação em texto formatado.
     * @return string com data, tipo e valor da transação.
     */
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("[%s] %-25s R$ %.2f", dataHora.format(fmt), tipo, valor);
    }
}