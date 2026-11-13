package br.com.unesp.banco.controller;

import br.com.unesp.banco.model.transacao.Transacao;
import br.com.unesp.banco.service.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para controlar as operações de transações financeiras do sistema.
 */
public class TransacaoController {
    /**
     * Instância do serviço bancário para execução das operações.
     */
    private final BancoService banco;
    /**
     * Simulador de empréstimos utilizado nas operações relacionadas a crédito.
     */
    private final SimuladorEmprestimo simulador;

    /**
     * Construtor do controlador de transações.
     * @param banco instância do serviço bancário.
     * @param simulador instância do simulador de empréstimos.
     */
    public TransacaoController(BancoService banco, SimuladorEmprestimo simulador) {
        this.banco = banco;
        this.simulador = simulador;
    }

    /**
     * Método para abrir uma conta para o cliente informado.
     * @param documentoCliente documento do cliente titular da conta.
     * @return o número da conta criada.
     */
    public int abrirConta(String documentoCliente) {
        return banco.abrirConta(documentoCliente);
    }

    /**
     * Método para realizar o depósito em uma conta específica.
     * @param numeroConta número da conta que receberá o valor.
     * @param valor valor a ser depositado.
     */
    public void depositar(int numeroConta, double valor) {
        banco.depositar(numeroConta, BigDecimal.valueOf(valor));
    }

    /**
     * Método para realizar o saque de uma conta específica.
     * @param numeroConta número da conta que realizará o saque.
     * @param valor valor a ser sacado.
     */
    public void sacar(int numeroConta, double valor) {
        banco.sacar(numeroConta, BigDecimal.valueOf(valor));
    }

    /**
     * Método para transferir valor entre duas contas.
     * @param origem número da conta de origem.
     * @param destino número da conta de destino.
     * @param valor valor a ser transferido.
     */
    public void transferir(int origem, int destino, double valor) {
        banco.transferir(origem, destino, BigDecimal.valueOf(valor));
    }

    /**
     * Método para realizar um pagamento em uma conta específica.
     * @param numeroConta número da conta que realizará o pagamento.
     * @param tipo tipo do pagamento.
     * @param valor valor do pagamento.
     */
    public void pagar(int numeroConta, String tipo, double valor) {
        banco.pagar(numeroConta, tipo, BigDecimal.valueOf(valor));
    }

    /**
     * Método para simular o valor da parcela de um empréstimo.
     * @param numeroConta número da conta envolvida na simulação.
     * @param valor valor solicitado do empréstimo.
     * @param taxa taxa de juros aplicada.
     * @param parcelas quantidade de parcelas.
     * @return o valor calculado da parcela.
     */
    public double simularEmprestimo(int numeroConta, double valor, double taxa, int parcelas) {
        return simulador.simular(numeroConta, BigDecimal.valueOf(valor), BigDecimal.valueOf(taxa), parcelas).doubleValue();
    }

    /**
     * Método para contratar um empréstimo e liberar o valor na conta.
     * @param numeroConta número da conta que receberá o empréstimo.
     * @param valor valor solicitado do empréstimo.
     * @param taxa taxa de juros aplicada.
     * @param parcelas quantidade de parcelas.
     * @return o valor calculado da parcela.
     */
    public double contratarEmprestimo(int numeroConta, double valor, double taxa, int parcelas) {
        return simulador.contratar(numeroConta, BigDecimal.valueOf(valor), BigDecimal.valueOf(taxa), parcelas).doubleValue();
    }

    /**
     * Método para consultar as últimas transações de uma conta.
     * @param numeroConta número da conta consultada.
     * @param n quantidade de transações a serem retornadas.
     * @return lista em texto com as transações mais recentes.
     */
    public List<String> extratoUltimas(int numeroConta, int n) {
        List<String> linhas = new ArrayList<>();
        for (Transacao t : banco.extratoUltimas(numeroConta, n)) {
            linhas.add(t.toString());
        }

        return linhas;
    }

    /**
     * Método para consultar o extrato de uma conta em um período específico.
     * @param numeroConta número da conta consultada.
     * @param inicio data inicial do período.
     * @param fim data final do período.
     * @return lista em texto com as transações do período informado.
     */
    public List<String> extratoPorPeriodo(int numeroConta, LocalDate inicio, LocalDate fim) {
        LocalDateTime i = inicio.atStartOfDay();
        LocalDateTime f = fim.atTime(LocalTime.MAX);

        List<String> linhas = new ArrayList<>();
        for (Transacao t : banco.extratoPorPeriodo(numeroConta, i, f)) {
            linhas.add(t.toString());
        }

        return linhas;
    }
}