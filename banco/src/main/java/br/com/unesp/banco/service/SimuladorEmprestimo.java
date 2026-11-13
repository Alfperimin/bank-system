package br.com.unesp.banco.service;

import br.com.unesp.banco.model.conta.Conta;
import br.com.unesp.banco.model.transacao.Emprestimo;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe para simular e contratar empréstimos em uma conta específica.
 */
public class SimuladorEmprestimo {
    /**
     * Instância do serviço do banco para acessar as contas e operações financeiras.
     */
    private final BancoService banco;

    /**
     * Construtor da classe de simulação de empréstimo.
     * @param banco instância do serviço bancário responsável pelas operações da conta.
     */
    public SimuladorEmprestimo(BancoService banco) {
        this.banco = banco;
    }

    /**
     * Método para simular o valor das parcelas de um empréstimo antes da contratação.
     * @param numeroConta número da conta que será simulada.
     * @param valor valor solicitado do empréstimo.
     * @param taxa taxa de juros do empréstimo.
     * @param parcelas quantidade de parcelas do empréstimo.
     * @return o valor calculado de cada parcela.
     */
    public BigDecimal simular(int numeroConta, BigDecimal valor, BigDecimal taxa, int parcelas) {
        Conta conta = banco.buscarConta(numeroConta);
        Emprestimo e = new Emprestimo(valor, taxa, parcelas);
        BigDecimal parcela = e.calcularParcela().setScale(2, RoundingMode.HALF_UP);
        conta.registrarSimulacao(valor);

        return parcela;
    }

    /**
     * Método para contratar um empréstimo e liberar o valor na conta informada.
     * @param numeroConta número da conta que receberá o empréstimo.
     * @param valor valor solicitado do empréstimo.
     * @param taxa taxa de juros do empréstimo.
     * @param parcelas quantidade de parcelas do empréstimo.
     * @return o valor calculado de cada parcela.
     */
    public BigDecimal contratar(int numeroConta, BigDecimal valor, BigDecimal taxa, int parcelas) {
        Conta conta = banco.buscarConta(numeroConta);
        Emprestimo e = new Emprestimo(valor, taxa, parcelas);
        BigDecimal parcela = e.calcularParcela().setScale(2, RoundingMode.HALF_UP);
        conta.receberEmprestimo(valor);

        return parcela;
    }
}