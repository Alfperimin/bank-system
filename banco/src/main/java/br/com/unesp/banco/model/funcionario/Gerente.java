package br.com.unesp.banco.model.funcionario;

import java.math.BigDecimal;

/**
 * Classe para representar um gerente de agência.
 * Salário final = salário base + (salário base * 20), conforme o enunciado.
 */
public class Gerente extends Funcionario {

    /**
     * Multiplicador do enunciado, aplicado sobre o salário base.
     */
    public static final BigDecimal MULTIPLICADOR = BigDecimal.valueOf(20);

    /**
     * Bônus fixo ou variável do gerente (informativo, não entra na fórmula do enunciado).
     */
    private BigDecimal bonus;
    /**
     * Agência pela qual o gerente é responsável.
     */
    private String agenciaResponsavel;

    /**
     * Construtor do gerente.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param endereco endereço e telefone.
     * @param salarioBase salário base.
     * @param bonus bônus do gerente.
     * @param agenciaResponsavel agência sob responsabilidade do gerente.
     */
    public Gerente(String nome, String cpf, EnderecoFuncionario endereco, BigDecimal salarioBase,
        BigDecimal bonus, String agenciaResponsavel) {
        super(nome, cpf, endereco, salarioBase);

        if(bonus == null){
            throw new IllegalArgumentException("O bônus não pode ser nulo");
        }else if(bonus.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("O bônus não pode ser negativo");
        }else{
            this.bonus = bonus;
        }

        if(agenciaResponsavel == null || agenciaResponsavel.trim().isEmpty()){
            throw new IllegalArgumentException("A agência responsável não pode ser vazia");
        }else{
            this.agenciaResponsavel = agenciaResponsavel;
        }
    }

    /**
     * Método para retornar o bônus do gerente.
     * @return o bônus cadastrado.
     */
    public BigDecimal getBonus() {
        return this.bonus;
    }

    /**
     * Método para retornar a agência responsável.
     * @return a agência cadastrada.
     */
    public String getAgenciaResponsavel() {
        return this.agenciaResponsavel;
    }

    /**
     * Método para retornar o nome do cargo.
     * @return Gerente.
     */
    @Override
    public String getCargo(){
        return "Gerente";
    }

    /**
     * Método para formatar os dados específicos do gerente.
     * @return texto com bônus e agência.
     */
    @Override
    public String exibirDadosEspecificos(){
        return "Bônus: R$ " + this.arredondar(this.getBonus()) + "\n" +
            "Agência responsável: " + this.getAgenciaResponsavel();
    }

    /**
     * Método para calcular o salário do gerente: base + (base * 20).
     * @return o salário final com duas casas decimais.
     */
    @Override
    public BigDecimal calcularSalario(){
        BigDecimal acrescimo = this.getSalarioBase().multiply(MULTIPLICADOR);

        return this.arredondar(this.getSalarioBase().add(acrescimo));
    }
}
