package br.com.unesp.banco.model.funcionario;

import java.math.BigDecimal;

/**
 * Classe para criar funcionários do cargo de diretor.
 * Aplica o padrão Factory Method, implementando a criação definida em FuncionarioFactory.
 */
public class DiretorFactory extends FuncionarioFactory {

    /**
     * Participação nos lucros que será atribuída ao diretor criado.
     */
    private BigDecimal participacaoLucros;
    /**
     * Área de responsabilidade que será atribuída ao diretor criado.
     */
    private String areaResponsabilidade;

    /**
     * Construtor da fábrica de diretores.
     * @param participacaoLucros valor da participação nos lucros.
     * @param areaResponsabilidade área de responsabilidade.
     */
    public DiretorFactory(BigDecimal participacaoLucros, String areaResponsabilidade) {
        this.participacaoLucros = participacaoLucros;
        this.areaResponsabilidade = areaResponsabilidade;
    }

    /**
     * Método para criar um diretor.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param endereco endereço e telefone.
     * @param salarioBase salário base.
     * @return o diretor criado.
     */
    @Override
    public Funcionario criarFuncionario(String nome, String cpf, EnderecoFuncionario endereco, BigDecimal salarioBase){
        return new Diretor(nome, cpf, endereco, salarioBase, this.participacaoLucros, this.areaResponsabilidade);
    }
}
