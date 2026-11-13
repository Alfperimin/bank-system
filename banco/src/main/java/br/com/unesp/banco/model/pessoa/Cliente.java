package br.com.unesp.banco.model.pessoa;

/**
 * Classe base para representar um cliente do banco.
 */
public abstract class Cliente {
    /**
     * Telefone de contato do cliente.
     */
    protected final String telefone;
    /**
     * E-mail de contato do cliente.
     */
    protected final String email;
    /**
     * Endereço cadastrado do cliente.
     */
    protected final Endereco endereco;

    /**
     * Construtor da classe de cliente.
     * @param telefone telefone de contato do cliente.
     * @param email e-mail de contato do cliente.
     * @param endereco endereço cadastrado do cliente.
     */
    public Cliente(String telefone, String email, Endereco endereco) {
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    /**
     * Método para retornar o telefone do cliente.
     * @return o telefone cadastrado.
     */
    public String getTelefone() { return telefone; }
    /**
     * Método para retornar o e-mail do cliente.
     * @return o e-mail cadastrado.
     */
    public String getEmail() { return email; }
    /**
     * Método para retornar o endereço do cliente.
     * @return o endereço cadastrado.
     */
    public Endereco getEndereco() { return endereco; }

    /**
     * Método para exibir os dados completos do cliente.
     * @return uma string com os dados pessoais e do endereço.
     */
    public String exibirDados() {
        return String.format("Nome: %s\n%s: %s\nTelefone: %s\nEmail: %s\nEndereço: %s",
            getNome(), getTipoDocumento(), getDocumento(), telefone, email, endereco.toString());
    }

    /**
     * Método para retornar o nome do cliente.
     * @return o nome ou razão social.
     */
    public abstract String getNome();
    /**
     * Método para retornar o documento do cliente.
     * @return o CPF ou CNPJ.
     */
    public abstract String getDocumento();
    /**
     * Método para retornar o tipo do documento do cliente.
     * @return CPF ou CNPJ.
     */
    public abstract String getTipoDocumento();
}