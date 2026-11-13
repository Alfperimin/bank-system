package br.com.unesp.banco.model.pagamento;

import br.com.unesp.banco.model.conta.Conta;
import java.math.BigDecimal;

/**
 * Interface que define a operação de pagamento.
 */
public interface Pagamento {
    /**
     * Método para executar o pagamento em uma conta específica.
     * @param conta conta que será debitada.
     * @param valor valor do pagamento.
     */
    void pagar(Conta conta, BigDecimal valor);
    /**
     * Método para indicar se o pagamento deve disparar notificação.
     * @return true quando houver necessidade de notificação; false em caso contrário.
     */
    boolean notificar();
}