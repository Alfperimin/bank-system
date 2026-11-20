package br.com.unesp.banco.model.funcionario;

import java.math.BigDecimal;

/**
 * Classe para criar funcionários do cargo de gerente.
 * Aplica o padrão Factory Method, implementando a criação definida em FuncionarioFactory.
 */
public class GerenteFactory extends FuncionarioFactory {

    /**
     * Bônus que será atribuído ao gerente criado.
     */
    private BigDecimal bonus;
    /**
     * Agência que será atribuída ao gerente criado.
     */
    private String agenciaResponsavel;

    /**
     * Construtor da fábrica de gerentes.
     * @param bonus bônus do gerente.
     * @param agenciaResponsavel agência sob responsabilidade do gerente.
     */
    public GerenteFactory(BigDecimal bonus, String agenciaResponsavel) {
        this.bonus = bonus;
        this.agenciaResponsavel = agenciaResponsavel;
    }

    /**
     * Método para criar um gerente.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param endereco endereço e telefone.
     * @param salarioBase salário base.
     * @return o gerente criado.
     */
    @Override
    public Funcionario criarFuncionario(String nome, String cpf, EnderecoFuncionario endereco, BigDecimal salarioBase){
        return new Gerente(nome, cpf, endereco, salarioBase, this.bonus, this.agenciaResponsavel);
    }
}
