package br.com.unesp.banco.model.funcionario;

/**
 * Classe para representar o endereço de um funcionário do banco.
 * Possui o telefone junto do endereço, conforme definido no enunciado.
 */
public class EnderecoFuncionario {

    /**
     * Logradouro do endereço.
     */
    private String logradouro;
    /**
     * Número do endereço.
     */
    private String numero;
    /**
     * Bairro do endereço.
     */
    private String bairro;
    /**
     * CEP do endereço.
     */
    private String cep;
    /**
     * Telefone de contato do funcionário.
     */
    private String telefone;

    /**
     * Construtor do endereço do funcionário.
     * @param logradouro logradouro do endereço.
     * @param numero número do endereço.
     * @param bairro bairro do endereço.
     * @param cep CEP do endereço.
     * @param telefone telefone de contato.
     */
    public EnderecoFuncionario(String logradouro, String numero, String bairro, String cep, String telefone) {

        if(logradouro == null || logradouro.trim().isEmpty()){
            throw new IllegalArgumentException("O logradouro não pode ser vazio");
        }else{
            this.logradouro = logradouro;
        }

        if(numero == null || numero.trim().isEmpty()){
            throw new IllegalArgumentException("O número não pode ser vazio");
        }else{
            this.numero = numero;
        }

        if(bairro == null || bairro.trim().isEmpty()){
            throw new IllegalArgumentException("O bairro não pode ser vazio");
        }else{
            this.bairro = bairro;
        }

        if(cep == null || cep.trim().isEmpty()){
            throw new IllegalArgumentException("O CEP não pode ser vazio");
        }else{
            this.cep = cep;
        }

        if(telefone == null || telefone.trim().isEmpty()){
            throw new IllegalArgumentException("O telefone não pode ser vazio");
        }else{
            this.telefone = telefone;
        }
    }

    /**
     * Método para retornar o logradouro.
     * @return o logradouro cadastrado.
     */
    public String getLogradouro() {
        return this.logradouro;
    }

    /**
     * Método para retornar o número do endereço.
     * @return o número cadastrado.
     */
    public String getNumero() {
        return this.numero;
    }

    /**
     * Método para retornar o bairro.
     * @return o bairro cadastrado.
     */
    public String getBairro() {
        return this.bairro;
    }

    /**
     * Método para retornar o CEP.
     * @return o CEP cadastrado.
     */
    public String getCep() {
        return this.cep;
    }

    /**
     * Método para retornar o telefone.
     * @return o telefone cadastrado.
     */
    public String getTelefone() {
        return this.telefone;
    }

    /**
     * Método para exibir os dados do endereço em texto.
     * @return descrição formatada do endereço e do telefone.
     */
    public String exibirDados(){
        return "Logradouro: " + this.getLogradouro() + ", Número: " + this.getNumero() +
            ", Bairro: " + this.getBairro() + ", CEP: " + this.getCep() +
            ", Telefone: " + this.getTelefone();
    }
}
