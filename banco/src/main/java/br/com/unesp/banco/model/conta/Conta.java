package br.com.unesp.banco.model.conta;

import br.com.unesp.banco.model.pessoa.*;
import br.com.unesp.banco.model.transacao.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * Classe que representa uma conta bancária do cliente.
 */
public class Conta {
    /**
     * Número único da conta.
     */
    private final int numero;
    /**
     * Cliente titular da conta.
     */
    private final Cliente titular;
    /**
     * Saldo atual da conta.
     */
    private BigDecimal saldo;
    /**
     * Histórico de transações da conta.
     */
    private final HistoricoTransacoes historico;

    /**
     * Construtor da conta com saldo inicial zerado.
     * @param numero número da conta.
     * @param titular cliente titular.
     */
    public Conta(int numero, Cliente titular) {
        if (titular == null) {
            throw new IllegalArgumentException("Titular não pode ser nulo");
        }

        this.numero = numero;
        this.titular = titular;
        this.saldo = BigDecimal.ZERO;
        this.historico = new HistoricoTransacoes();
    }

    /**
     * Construtor da conta com saldo e transações restauradas.
     * @param numero número da conta.
     * @param titular cliente titular.
     * @param saldo saldo atual.
     * @param transacoes lista de transações para restaurar.
     */
    public Conta(int numero, Cliente titular, BigDecimal saldo, List<Transacao> transacoes) {
        if (titular == null) {
            throw new IllegalArgumentException("Titular não pode ser nulo");
        }

        this.numero = numero;
        this.titular = titular;
        this.saldo = (saldo == null) ? BigDecimal.ZERO : saldo;
        this.historico = new HistoricoTransacoes();

        if (transacoes != null) {
            for (Transacao t : transacoes) {
                this.historico.adicionar(t);
            }
        }
    }

    /**
     * Método para retornar o número da conta.
     * @return o número da conta.
     */
    public int getNumero() { return numero; }
    /**
     * Método para retornar o titular da conta.
     * @return o cliente titular.
     */
    public Cliente getTitular() { return titular; }
    /**
     * Método para retornar o saldo atual da conta.
     * @return o saldo atual.
     */
    public BigDecimal getSaldo() { return saldo; }

    /**
     * Método para validar o valor informado para operações financeiras.
     * @param valor valor a ser validado.
     */
    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
    }

    /**
     * Método interno para creditar um valor à conta.
     * @param valor valor a ser creditado.
     * @param tipo tipo da transação de crédito.
     */
    private void creditar(BigDecimal valor, TipoTransacao tipo) {
        validarValor(valor);
        saldo = saldo.add(valor);
        historico.adicionar(new Transacao(tipo, valor));
    }

    /**
     * Método interno para debitar um valor da conta.
     * @param valor valor a ser debitado.
     * @param tipo tipo da transação de débito.
     */
    private void debitar(BigDecimal valor, TipoTransacao tipo) {
        validarValor(valor);

        if (saldo.compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }

        saldo = saldo.subtract(valor);
        historico.adicionar(new Transacao(tipo, valor));
    }

    /**
     * Método para realizar depósito na conta.
     * @param valor valor a ser depositado.
     */
    public void depositar(BigDecimal valor) {
        creditar(valor, TipoTransacao.DEPOSITO);
    }

    /**
     * Método para realizar saque da conta.
     * @param valor valor a ser sacado.
     */
    public void sacar(BigDecimal valor) {
        debitar(valor, TipoTransacao.SAQUE);
    }

    /**
     * Método para transferir valor para outra conta.
     * @param destino conta de destino.
     * @param valor valor a ser transferido.
     */
    public void transferir(Conta destino, BigDecimal valor) {
        if (destino == null) {
            throw new IllegalArgumentException("Destino não pode ser nulo");
        }
        if (destino == this) {
            throw new IllegalArgumentException("Destino não pode ser a própria conta");
        }

        this.debitar(valor, TipoTransacao.TRANSFERENCIA_ENVIADA);
        destino.creditar(valor, TipoTransacao.TRANSFERENCIA_RECEBIDA);
    }

    /**
     * Método para realizar pagamento com um tipo específico.
     * @param tipo tipo do pagamento.
     * @param valor valor do pagamento.
     */
    public void pagar(TipoTransacao tipo, BigDecimal valor) {
        if (tipo != TipoTransacao.PIX && tipo != TipoTransacao.BOLETO && tipo != TipoTransacao.CARTAO) {
            throw new IllegalArgumentException("Tipo de pagamento inválido");
        }

        debitar(valor, tipo);
    }

    /**
     * Método para receber um valor relacionado a empréstimo.
     * @param valor valor recebido do empréstimo.
     */
    public void receberEmprestimo(BigDecimal valor) {
        creditar(valor, TipoTransacao.EMPRESTIMO);
    }

    /**
     * Método para registrar uma simulação de empréstimo no histórico da conta.
     * @param valor valor simulado.
     */
    public void registrarSimulacao(BigDecimal valor) {
        validarValor(valor);
        historico.adicionar(new Transacao(TipoTransacao.SIMULACAO, valor));
    }

    /**
     * Método para retornar as últimas transações da conta.
     * @param n quantidade de transações desejadas.
     * @return lista com as transações mais recentes.
     */
    public List<Transacao> extratoUltimas(int n) {
        return historico.ultimas(n);
    }

    /**
     * Método para retornar as transações da conta em um período específico.
     * @param inicio data e hora inicial do período.
     * @param fim data e hora final do período.
     * @return lista com as transações do período informado.
     */
    public List<Transacao> extratoPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return historico.porPeriodo(inicio, fim);
    }

    /**
     * Método para retornar todas as transações da conta.
     * @return lista com todas as transações registradas.
     */
    public List<Transacao> getTransacoes() {
        return historico.todas();
    }

    /**
     * Método para representar a conta em texto.
     * @return descrição da conta com titular e saldo.
     */
    @Override
    public String toString() {
        return String.format("Conta %d | Titular: %s (%s) | Saldo: R$ %.2f",
                numero, titular.getNome(), titular.getDocumento(), saldo);
    }
}