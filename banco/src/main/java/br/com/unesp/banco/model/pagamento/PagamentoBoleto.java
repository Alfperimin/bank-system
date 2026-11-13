package br.com.unesp.banco.model.pagamento;

import br.com.unesp.banco.model.conta.Conta;
import br.com.unesp.banco.model.transacao.TipoTransacao;
import java.math.BigDecimal;

/**
 * Implementação do pagamento via boleto.
 */
public class PagamentoBoleto implements Pagamento {
    /**
     * Método para registrar o pagamento com o tipo BOLETO.
     * @param conta conta que será debitada.
     * @param valor valor do pagamento.
     */
    @Override
    public void pagar(Conta conta, BigDecimal valor) {
        conta.pagar(TipoTransacao.BOLETO, valor);
    }

    /**
     * Método para informar se o sistema deve notificar o pagamento.
     * @return false para pagamento por boleto.
     */
    @Override
    public boolean notificar() {
        return false;
    }
}