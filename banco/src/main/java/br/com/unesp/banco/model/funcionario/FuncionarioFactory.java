package br.com.unesp.banco.model.funcionario;

import java.math.BigDecimal;

/**
 * Classe abstrata para definir a criação de funcionários de diferentes cargos.
 * Essa classe aplica o padrão Factory Method: a criação do objeto é delegada às
 * subclasses (GerenteFactory, TecnicoBancarioFactory e DiretorFactory), que sabem montar
 * cada cargo. Foi escolhido porque cada cargo tem atributos diferentes; assim, quem
 * cadastra funcionários trabalha só com a abstração e não precisa conhecer o construtor
 * de cada cargo. Os dados específicos do cargo são informados no construtor da fábrica
 * concreta, e a validação fica dentro das próprias classes de funcionário.
 */
public abstract class FuncionarioFactory {

    /**
     * Método abstrato para criar um funcionário do cargo da fábrica.
     * @param nome nome completo do funcionário.
     * @param cpf CPF do funcionário.
     * @param endereco endereço e telefone do funcionário.
     * @param salarioBase salário base do funcionário.
     * @return o funcionário criado.
     */
    public abstract Funcionario criarFuncionario(String nome, String cpf,
        EnderecoFuncionario endereco, BigDecimal salarioBase);
}
