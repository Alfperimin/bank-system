package br.com.unesp.banco.model.pessoa;

/**
 * Classe para representar uma pessoa física cadastrada no banco.
 */
public class PessoaFisica extends Cliente {
    /**
     * Nome completo da pessoa física.
     */
    private final String nome;
    /**
     * CPF da pessoa física.
     */
    private final String cpf;

    /**
     * Construtor da pessoa física.
     * @param nome nome completo da pessoa.
     * @param cpf CPF da pessoa.
     * @param telefone telefone de contato.
     * @param email e-mail de contato.
     * @param endereco endereço cadastrado.
     */
    public PessoaFisica(String nome, String cpf, String telefone, String email, Endereco endereco) {
        super(telefone, email, endereco);
        this.nome = nome;
        this.cpf = cpf;
    }

    /**
     * Método para retornar o nome da pessoa física.
     * @return o nome completo.
     */
    public String getNome() { return nome; }
    /**
     * Método para retornar o documento da pessoa física.
     * @return o CPF.
     */
    public String getDocumento() { return cpf; }
    /**
     * Método para retornar o tipo do documento.
     * @return CPF.
     */
    public String getTipoDocumento() { return "CPF"; }
}