package br.com.unesp.banco.model.transacao;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe que representa um empréstimo com valor, taxa e quantidade de parcelas.
 */
public class Emprestimo {
    /**
     * Valor solicitado do empréstimo.
     */
    private final BigDecimal valor;
    /**
     * Taxa de juros aplicada ao empréstimo.
     */
    private final BigDecimal taxa;
    /**
     * Quantidade total de parcelas do empréstimo.
     */
    private final int parcelas;

    /**
     * Construtor do empréstimo.
     * @param valor valor solicitado.
     * @param taxa taxa de juros.
     * @param parcelas quantidade de parcelas.
     */
    public Emprestimo(BigDecimal valor, BigDecimal taxa, int parcelas) {
        if (valor == null || taxa == null) {
            throw new IllegalArgumentException("Valor e taxa não podem ser nulos");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0 || taxa.compareTo(BigDecimal.ZERO) < 0 || parcelas <= 0) {
            throw new IllegalArgumentException("Parâmetros de empréstimo inválidos");
        }
        
        this.valor = valor;
        this.taxa = taxa;
        this.parcelas = parcelas;
    }

    /**
     * Método para retornar o valor do empréstimo.
     * @return o valor solicitado.
     */
    public BigDecimal getValor() { return valor; }
    /**
     * Método para retornar a taxa de juros do empréstimo.
     * @return a taxa aplicada.
     */
    public BigDecimal getTaxa() { return taxa; }
    /**
     * Método para retornar a quantidade de parcelas do empréstimo.
     * @return o número de parcelas.
     */
    public int getParcelas() { return parcelas; }

    /**
     * Método para calcular o valor de cada parcela do empréstimo.
     * @return o valor da parcela calculada.
     */
    public BigDecimal calcularParcela() {
        if(taxa.compareTo(BigDecimal.ZERO) == 0){
            return valor.divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.HALF_UP);
        }

        double i = taxa.doubleValue(); 
        BigDecimal fator = BigDecimal.valueOf(1 - Math.pow(1 + i, -parcelas));

        return valor.multiply(taxa).divide(fator, 2, RoundingMode.HALF_UP);
    }
}