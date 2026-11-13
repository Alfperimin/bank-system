package br.com.unesp.banco.model.pessoa;

/**
 * Classe para representar uma pessoa jurídica cadastrada no banco.
 */
public class PessoaJuridica extends Cliente {
    /**
     * Razão social da pessoa jurídica.
     */
    private final String razaoSocial;
    /**
     * CNPJ da pessoa jurídica.
     */
    private final String cnpj;

    /**
     * Construtor da pessoa jurídica.
     * @param razaoSocial razão social da empresa.
     * @param cnpj CNPJ da empresa.
     * @param telefone telefone de contato.
     * @param email e-mail de contato.
     * @param endereco endereço cadastrado.
     */
    public PessoaJuridica(String razaoSocial, String cnpj, String telefone, String email, Endereco endereco) {
        super(telefone, email, endereco);
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    /**
     * Método para retornar a razão social da pessoa jurídica.
     * @return a razão social.
     */
    public String getNome() { return razaoSocial; }
    /**
     * Método para retornar o documento da pessoa jurídica.
     * @return o CNPJ.
     */
    public String getDocumento() { return cnpj; }
    /**
     * Método para retornar o tipo do documento.
     * @return CNPJ.
     */
    public String getTipoDocumento() { return "CNPJ"; }
}