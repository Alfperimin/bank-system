package br.com.unesp.banco.model.funcionario;

import java.math.BigDecimal;

/**
 * Classe para criar funcionários do cargo de técnico bancário.
 * Aplica o padrão Factory Method, implementando a criação definida em FuncionarioFactory.
 */
public class TecnicoBancarioFactory extends FuncionarioFactory {

    /**
     * Setor que será atribuído ao técnico criado.
     */
    private String setorAtendimento;
    /**
     * Horas extras que serão atribuídas ao técnico criado.
     */
    private int horasExtras;

    /**
     * Construtor da fábrica de técnicos bancários.
     * @param setorAtendimento setor de atendimento.
     * @param horasExtras horas extras trabalhadas.
     */
    public TecnicoBancarioFactory(String setorAtendimento, int horasExtras) {
        this.setorAtendimento = setorAtendimento;
        this.horasExtras = horasExtras;
    }

    /**
     * Método para criar um técnico bancário.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param endereco endereço e telefone.
     * @param salarioBase salário base.
     * @return o técnico bancário criado.
     */
    @Override
    public Funcionario criarFuncionario(String nome, String cpf, EnderecoFuncionario endereco, BigDecimal salarioBase){
        return new TecnicoBancario(nome, cpf, endereco, salarioBase, this.setorAtendimento, this.horasExtras);
    }
}
