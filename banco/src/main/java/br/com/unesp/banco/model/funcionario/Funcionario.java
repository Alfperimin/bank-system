package br.com.unesp.banco.model.funcionario;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe base para representar um funcionário do banco.
 * Cada cargo herda desta classe e define a sua própria regra de cálculo salarial,
 * o que aplica polimorfismo: quem usa um Funcionario chama calcularSalario()
 * sem precisar saber qual é o cargo.
 */
public abstract class Funcionario {

    /**
     * Nome completo do funcionário.
     */
    private String nome;
    /**
     * CPF do funcionário, usado como identificador único.
     */
    private String cpf;
    /**
     * Endereço e telefone do funcionário.
     */
    private EnderecoFuncionario endereco;
    /**
     * Salário base do funcionário, antes das regras de cada cargo.
     */
    private BigDecimal salarioBase;

    /**
     * Construtor da classe de funcionário.
     * @param nome nome completo do funcionário.
     * @param cpf CPF do funcionário.
     * @param endereco endereço e telefone do funcionário.
     * @param salarioBase salário base, maior ou igual a zero.
     */
    public Funcionario(String nome, String cpf, EnderecoFuncionario endereco, BigDecimal salarioBase) {

        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O nome não pode ser vazio");
        }else{
            this.nome = nome;
        }

        if(cpf == null || cpf.trim().isEmpty()){
            throw new IllegalArgumentException("O CPF não pode ser vazio");
        }else{
            this.cpf = cpf;
        }

        if(endereco == null){
            throw new IllegalArgumentException("O endereço não pode ser nulo");
        }else{
            this.endereco = endereco;
        }

        if(salarioBase == null){
            throw new IllegalArgumentException("O salário base não pode ser nulo");
        }else if(salarioBase.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("O salário base não pode ser negativo");
        }else{
            this.salarioBase = salarioBase;
        }
    }

    /**
     * Método para retornar o nome do funcionário.
     * @return o nome cadastrado.
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Método para retornar o CPF do funcionário.
     * @return o CPF cadastrado.
     */
    public String getCpf() {
        return this.cpf;
    }

    /**
     * Método para retornar o endereço do funcionário.
     * @return o endereço cadastrado.
     */
    public EnderecoFuncionario getEndereco() {
        return this.endereco;
    }

    /**
     * Método para retornar o salário base do funcionário.
     * @return o salário base.
     */
    public BigDecimal getSalarioBase() {
        return this.salarioBase;
    }

    /**
     * Método para retornar o nome do cargo do funcionário.
     * @return o nome do cargo, como Gerente, Técnico Bancário ou Diretor.
     */
    public abstract String getCargo();

    /**
     * Método para formatar os dados que existem só no cargo.
     * @return texto com os atributos específicos do cargo.
     */
    abstract String exibirDadosEspecificos();

    /**
     * Método para calcular o salário final conforme a regra do cargo.
     * @return o salário final, com duas casas decimais.
     */
    public abstract BigDecimal calcularSalario();

    /**
     * Método auxiliar para arredondar um valor monetário para duas casas.
     * @param valor valor a ser arredondado.
     * @return o valor com duas casas decimais (arredondamento HALF_UP).
     */
    protected BigDecimal arredondar(BigDecimal valor){
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Método para exibir os dados completos do funcionário, incluindo o salário final.
     * @return uma string com os dados pessoais, do cargo e o salário calculado.
     */
    public String exibirDados(){
        return "Cargo: " + this.getCargo() + "\n" +
            "Nome: " + this.getNome() + "\n" +
            "CPF: " + this.getCpf() + "\n" +
            "Endereço: " + this.getEndereco().exibirDados() + "\n" +
            "Salário base: R$ " + this.arredondar(this.getSalarioBase()) + "\n" +
            this.exibirDadosEspecificos() + "\n" +
            "Salário final: R$ " + this.calcularSalario();
    }
}
