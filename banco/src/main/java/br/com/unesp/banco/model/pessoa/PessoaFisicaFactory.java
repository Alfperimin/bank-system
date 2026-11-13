package br.com.unesp.banco.model.pessoa;

/**
 * Classe para criar clientes do tipo pessoa física.
 * Essa classe aplica o padrão Factory Method, pois implementa a criação do objeto
 * PessoaFisica de acordo com o método definido pela classe base ClienteFactory.
 */
public class PessoaFisicaFactory extends ClienteFactory {
    /**
     * Método para criar um cliente pessoa física validando os dados informados.
     * @param nome nome completo da pessoa.
     * @param cpf CPF da pessoa.
     * @param telefone telefone de contato.
     * @param email e-mail de contato.
     * @param endereco endereço cadastrado.
     * @return o cliente pessoa física criado.
     */
    @Override
    public Cliente criarCliente(String nome, String cpf, 
        String telefone, String email, Endereco endereco) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio.");
        }
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone não pode ser nulo ou vazio.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo.");
        }

        return new PessoaFisica(nome, cpf, telefone, email, endereco);
    }
}