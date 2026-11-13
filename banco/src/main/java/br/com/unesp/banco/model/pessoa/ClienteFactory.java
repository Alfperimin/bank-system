package br.com.unesp.banco.model.pessoa;

/**
 * Classe abstrata para definir a criação de clientes de diferentes tipos.
 * Essa classe define o padrão Factory Method, pois a criação do objeto cliente é 
 * delegada para subclasses que implementam a lógica de montagem de cada tipo.
 */
public abstract class ClienteFactory {

    /**
     * Método abstrato para criar um cliente com base no tipo de pessoa informado.
     * @param nomeOuRazao nome da pessoa física ou razão social da pessoa jurídica.
     * @param documento documento do cliente, como CPF ou CNPJ.
     * @param telefone telefone de contato do cliente.
     * @param email e-mail de contato do cliente.
     * @param endereco endereço cadastrado do cliente.
     * @return o cliente criado.
     */
    public abstract Cliente criarCliente(String nomeOuRazao, String documento, 
        String telefone, String email, Endereco endereco);
}