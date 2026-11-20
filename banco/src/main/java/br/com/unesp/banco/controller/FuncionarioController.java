package br.com.unesp.banco.controller;

import br.com.unesp.banco.model.funcionario.*;
import br.com.unesp.banco.service.FuncionarioService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para controlar as operações de cadastro e consulta de funcionários.
 * Recebe dados simples (texto e números) vindos da interface, monta os objetos
 * usando as fábricas e entrega ao FuncionarioService.
 */
public class FuncionarioController {

    /**
     * Instância do serviço responsável pelas regras de negócio dos funcionários.
     */
    private final FuncionarioService service;

    /**
     * Construtor do controlador de funcionários.
     * @param service instância do serviço de funcionários a ser utilizada.
     */
    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    /**
     * Método para cadastrar um gerente no sistema.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param logradouro logradouro do endereço.
     * @param numero número do endereço.
     * @param bairro bairro do endereço.
     * @param cep CEP do endereço.
     * @param telefone telefone de contato.
     * @param salarioBase salário base.
     * @param bonus bônus do gerente.
     * @param agenciaResponsavel agência sob responsabilidade do gerente.
     */
    public void cadastrarGerente(String nome, String cpf, String logradouro, String numero, String bairro,
        String cep, String telefone, double salarioBase, double bonus, String agenciaResponsavel){
        EnderecoFuncionario endereco = new EnderecoFuncionario(logradouro, numero, bairro, cep, telefone);
        FuncionarioFactory fabrica = new GerenteFactory(BigDecimal.valueOf(bonus), agenciaResponsavel);

        this.service.cadastrarFuncionario(fabrica.criarFuncionario(nome, cpf, endereco, BigDecimal.valueOf(salarioBase)));
    }

    /**
     * Método para cadastrar um técnico bancário no sistema.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param logradouro logradouro do endereço.
     * @param numero número do endereço.
     * @param bairro bairro do endereço.
     * @param cep CEP do endereço.
     * @param telefone telefone de contato.
     * @param salarioBase salário base.
     * @param setorAtendimento setor de atendimento.
     * @param horasExtras horas extras trabalhadas.
     */
    public void cadastrarTecnico(String nome, String cpf, String logradouro, String numero, String bairro,
        String cep, String telefone, double salarioBase, String setorAtendimento, int horasExtras){
        EnderecoFuncionario endereco = new EnderecoFuncionario(logradouro, numero, bairro, cep, telefone);
        FuncionarioFactory fabrica = new TecnicoBancarioFactory(setorAtendimento, horasExtras);

        this.service.cadastrarFuncionario(fabrica.criarFuncionario(nome, cpf, endereco, BigDecimal.valueOf(salarioBase)));
    }

    /**
     * Método para cadastrar um diretor no sistema.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param logradouro logradouro do endereço.
     * @param numero número do endereço.
     * @param bairro bairro do endereço.
     * @param cep CEP do endereço.
     * @param telefone telefone de contato.
     * @param salarioBase salário base.
     * @param participacaoLucros participação nos lucros (PLR).
     * @param areaResponsabilidade área de responsabilidade.
     */
    public void cadastrarDiretor(String nome, String cpf, String logradouro, String numero, String bairro,
        String cep, String telefone, double salarioBase, double participacaoLucros, String areaResponsabilidade){
        EnderecoFuncionario endereco = new EnderecoFuncionario(logradouro, numero, bairro, cep, telefone);
        FuncionarioFactory fabrica = new DiretorFactory(BigDecimal.valueOf(participacaoLucros), areaResponsabilidade);

        this.service.cadastrarFuncionario(fabrica.criarFuncionario(nome, cpf, endereco, BigDecimal.valueOf(salarioBase)));
    }

    /**
     * Método para exibir os dados de um funcionário consultado pelo CPF.
     * @param cpf CPF do funcionário.
     * @return os dados completos, incluindo o salário final.
     */
    public String exibirFuncionario(String cpf){
        return this.service.exibirFuncionario(cpf);
    }

    /**
     * Método para listar os funcionários em formato resumido.
     * @return uma lista com CPF, nome, cargo e salário final de cada funcionário.
     */
    public List<String> listarFuncionarios(){
        List<String> resumo = new ArrayList<>();

        for(Funcionario f : this.service.listarFuncionarios()){
            resumo.add(f.getCpf() + " - " + f.getNome() + " (" + f.getCargo() + ") - R$ " + f.calcularSalario());
        }

        return resumo;
    }

    /**
     * Método para gerar o relatório consolidado de todos os funcionários.
     * @return o texto do relatório.
     */
    public String gerarRelatorioFuncionarios(){
        return this.service.gerarRelatorioFuncionarios();
    }
}
