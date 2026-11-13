package br.com.unesp.banco.model.pagamento;

/**
 * Fábrica responsável por criar as implementações de pagamento conforme o tipo informado.
 * Essa classe aplica o padrão Factory, pois centraliza a criação dos objetos de pagamento
 * e decide qual implementação deve ser retornada com base no tipo informado.
 */
public class PagamentoFactory {
    /**
     * Método para criar a estratégia de pagamento correta.
     * @param tipo tipo do pagamento solicitado.
     * @return a implementação correspondente ao tipo.
     */
    public static Pagamento criar(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de pagamento não pode ser nulo");
        }

        switch (tipo.trim().toUpperCase()) {
            case "PIX": return new PagamentoPix();
            case "CARTAO": return new PagamentoCartao();
            case "BOLETO": return new PagamentoBoleto();
            default: 
                throw new IllegalArgumentException("Tipo de pagamento desconhecido: " + tipo);
        }
    }
}