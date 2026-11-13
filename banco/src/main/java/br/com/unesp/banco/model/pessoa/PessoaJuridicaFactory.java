package br.com.unesp.banco.model.pessoa;

/**
 * Classe para criar clientes do tipo pessoa jurídica.
 * Essa classe aplica o padrão Factory Method, pois define a criação da instância
 * de PessoaJuridica conforme a abstração da fábrica base ClienteFactory.
 */
public class PessoaJuridicaFactory extends ClienteFactory {
    /**
     * Método para criar um cliente pessoa jurídica validando os dados informados.
     * @param razaoSocial razão social da empresa.
     * @param cnpj CNPJ da empresa.
     * @param telefone telefone de contato.
     * @param email e-mail de contato.
     * @param endereco endereço cadastrado.
     * @return o cliente pessoa jurídica criado.
     */
    @Override
    public Cliente criarCliente(String razaoSocial, String cnpj, 
        String telefone, String email, Endereco endereco) {
        if (razaoSocial == null || razaoSocial.isBlank()) {
            throw new IllegalArgumentException("Razão social não pode ser nula ou vazia.");
        }
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ não pode ser nulo ou vazio.");
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

        return new PessoaJuridica(razaoSocial, cnpj, telefone, email, endereco);
    }
}