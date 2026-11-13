package br.com.unesp.banco.controller;

import br.com.unesp.banco.model.pessoa.*;
import br.com.unesp.banco.service.BancoService;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para controlar as operações de cadastro e consulta de clientes.
 */
public class ClienteController {
    /**
     * Instância do serviço bancário responsável pelas regras de negócio.
     */
    private final BancoService banco;
    /**
     * Fábrica de clientes físicos para criação dos objetos de pessoa física.
     */
    private final PessoaFisicaFactory fabricaPF = new PessoaFisicaFactory();
    /**
     * Fábrica de clientes jurídicos para criação dos objetos de pessoa jurídica.
     */
    private final PessoaJuridicaFactory fabricaPJ = new PessoaJuridicaFactory();

    /**
     * Construtor do controlador de clientes.
     * @param banco instância do serviço bancário a ser utilizada.
     */
    public ClienteController(BancoService banco) {
        this.banco = banco;
    }

    /**
     * Método para cadastrar um cliente pessoa física no sistema.
     * @param nome nome do cliente.
     * @param cpf documento do cliente.
     * @param telefone telefone de contato.
     * @param email e-mail de contato.
     * @param rua rua do endereço.
     * @param numero número do endereço.
     * @param bairro bairro do endereço.
     * @param cidade cidade do endereço.
     * @param estado estado do endereço.
     * @param cep cep do endereço.
     */
    public void cadastrarPF(String nome, String cpf, String telefone, String email, 
        String rua, String numero, String bairro, String cidade, String estado, String cep) {
        Endereco end = new Endereco(rua, numero, bairro, cidade, estado, cep);
        Cliente c = fabricaPF.criarCliente(nome, cpf, telefone, email, end);
        banco.adicionarCliente(c);
    }

    /**
     * Método para cadastrar um cliente pessoa jurídica no sistema.
     * @param razaoSocial razão social da empresa.
     * @param cnpj documento da empresa.
     * @param telefone telefone de contato.
     * @param email e-mail de contato.
     * @param rua rua do endereço.
     * @param numero número do endereço.
     * @param bairro bairro do endereço.
     * @param cidade cidade do endereço.
     * @param estado estado do endereço.
     * @param cep cep do endereço.
     */
    public void cadastrarPJ(String razaoSocial, String cnpj, String telefone, String email,
        String rua, String numero, String bairro, String cidade, String estado, String cep) {
        Endereco end = new Endereco(rua, numero, bairro, cidade, estado, cep);
        Cliente c = fabricaPJ.criarCliente(razaoSocial, cnpj, telefone, email, end);
        banco.adicionarCliente(c);
    }

    /**
     * Método para exibir os dados de um cliente consultado pelo documento.
     * @param documento identificador do cliente, pode ser cpf ou cnpj.
     * @return os dados completos do cliente encontrado.
     */
    public String exibirCliente(String documento) {
        return banco.buscarCliente(documento).exibirDados();
    }

    /**
     * Método para listar os clientes registrados no sistema em formato resumido.
     * @return uma lista com o documento e o nome de cada cliente.
     */
    public List<String> listarClientes() {
        List<String> resumo = new ArrayList<>();
        for (Cliente c : banco.listarClientes()) {
            resumo.add(c.getDocumento() + " - " + c.getNome());
        }
        return resumo;
    }
}