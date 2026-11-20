package br.com.unesp.banco.model.funcionario;

import java.math.BigDecimal;

/**
 * Classe para representar um diretor do banco.
 * Salário final = salário base + (salário base * 35), conforme o enunciado.
 */
public class Diretor extends Funcionario {

    /**
     * Multiplicador do enunciado, aplicado sobre o salário base.
     */
    public static final BigDecimal MULTIPLICADOR = BigDecimal.valueOf(35);

    /**
     * Participação nos lucros (PLR) do diretor (informativa, não entra na fórmula do enunciado).
     */
    private BigDecimal participacaoLucros;
    /**
     * Área de atuação ou responsabilidade do diretor.
     */
    private String areaResponsabilidade;

    /**
     * Construtor do diretor.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param endereco endereço e telefone.
     * @param salarioBase salário base.
     * @param participacaoLucros valor da participação nos lucros.
     * @param areaResponsabilidade área de responsabilidade.
     */
    public Diretor(String nome, String cpf, EnderecoFuncionario endereco, BigDecimal salarioBase,
        BigDecimal participacaoLucros, String areaResponsabilidade) {
        super(nome, cpf, endereco, salarioBase);

        if(participacaoLucros == null){
            throw new IllegalArgumentException("A participação nos lucros não pode ser nula");
        }else if(participacaoLucros.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("A participação nos lucros não pode ser negativa");
        }else{
            this.participacaoLucros = participacaoLucros;
        }

        if(areaResponsabilidade == null || areaResponsabilidade.trim().isEmpty()){
            throw new IllegalArgumentException("A área de responsabilidade não pode ser vazia");
        }else{
            this.areaResponsabilidade = areaResponsabilidade;
        }
    }

    /**
     * Método para retornar a participação nos lucros.
     * @return o valor de PLR cadastrado.
     */
    public BigDecimal getParticipacaoLucros() {
        return this.participacaoLucros;
    }

    /**
     * Método para retornar a área de responsabilidade.
     * @return a área cadastrada.
     */
    public String getAreaResponsabilidade() {
        return this.areaResponsabilidade;
    }

    /**
     * Método para retornar o nome do cargo.
     * @return Diretor.
     */
    @Override
    public String getCargo(){
        return "Diretor";
    }

    /**
     * Método para formatar os dados específicos do diretor.
     * @return texto com PLR e área de responsabilidade.
     */
    @Override
    public String exibirDadosEspecificos(){
        return "Participação nos lucros: R$ " + this.arredondar(this.getParticipacaoLucros()) + "\n" +
            "Área de responsabilidade: " + this.getAreaResponsabilidade();
    }

    /**
     * Método para calcular o salário do diretor: base + (base * 35).
     * @return o salário final com duas casas decimais.
     */
    @Override
    public BigDecimal calcularSalario(){
        BigDecimal acrescimo = this.getSalarioBase().multiply(MULTIPLICADOR);

        return this.arredondar(this.getSalarioBase().add(acrescimo));
    }
}
