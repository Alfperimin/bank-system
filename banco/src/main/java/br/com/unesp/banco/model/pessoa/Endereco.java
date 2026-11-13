package br.com.unesp.banco.model.pessoa;

/**
 * Classe para representar o endereço de um cliente.
 */
public class Endereco {
    /**
     * Nome da rua do endereço.
     */
    private final String rua;
    /**
     * Número do endereço.
     */
    private final String numero;
    /**
     * Bairro do endereço.
     */
    private final String bairro;
    /**
     * Cidade do endereço.
     */
    private final String cidade;
    /**
     * Estado do endereço.
     */
    private final String estado;
    /**
     * CEP do endereço.
     */
    private final String cep;

    /**
     * Construtor do endereço.
     * @param rua nome da rua.
     * @param numero número do endereço.
     * @param bairro bairro do endereço.
     * @param cidade cidade do endereço.
     * @param estado estado do endereço.
     * @param cep CEP do endereço.
     */
    public Endereco(String rua, String numero, String bairro, String cidade, String estado, String cep) {
        if (rua == null || rua.isBlank() || numero == null || numero.isBlank() || bairro == null || bairro.isBlank() ||
            cidade == null || cidade.isBlank() || estado == null || estado.isBlank() || cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("Todos os campos do endereço devem ser preenchidos.");
        }
        
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    /**
     * Método para retornar a rua do endereço.
     * @return a rua cadastrada.
     */
    public String getRua() { return rua; }
    /**
     * Método para retornar o número do endereço.
     * @return o número cadastrado.
     */
    public String getNumero() { return numero; }
    /**
     * Método para retornar o bairro do endereço.
     * @return o bairro cadastrado.
     */
    public String getBairro() { return bairro; }
    /**
     * Método para retornar a cidade do endereço.
     * @return a cidade cadastrada.
     */
    public String getCidade() { return cidade; }
    /**
     * Método para retornar o estado do endereço.
     * @return o estado cadastrado.
     */
    public String getEstado() { return estado; }
    /**
     * Método para retornar o CEP do endereço.
     * @return o CEP cadastrado.
     */
    public String getCep() { return cep; }

    /**
     * Método para representar o endereço em texto.
     * @return descrição formatada do endereço.
     */
    @Override
    public String toString() {
        return String.format("Rua: %s, Número: %s, Bairro: %s, Cidade: %s, Estado: %s, CEP: %s",
            rua, numero, bairro, cidade, estado, cep);
    }
}