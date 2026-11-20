package br.com.unesp.banco.model.funcionario;

import java.math.BigDecimal;

/**
 * Classe para representar um técnico bancário.
 * Salário final = salário base + (horas extras * 30), conforme o enunciado.
 */
public class TecnicoBancario extends Funcionario {

    /**
     * Valor pago por hora extra trabalhada.
     */
    public static final BigDecimal VALOR_HORA_EXTRA = BigDecimal.valueOf(30);

    /**
     * Setor em que o técnico atende.
     */
    private String setorAtendimento;
    /**
     * Quantidade de horas extras trabalhadas.
     */
    private int horasExtras;

    /**
     * Construtor do técnico bancário.
     * @param nome nome completo.
     * @param cpf CPF.
     * @param endereco endereço e telefone.
     * @param salarioBase salário base.
     * @param setorAtendimento setor de atendimento.
     * @param horasExtras horas extras trabalhadas.
     */
    public TecnicoBancario(String nome, String cpf, EnderecoFuncionario endereco, BigDecimal salarioBase,
        String setorAtendimento, int horasExtras) {
        super(nome, cpf, endereco, salarioBase);

        if(setorAtendimento == null || setorAtendimento.trim().isEmpty()){
            throw new IllegalArgumentException("O setor de atendimento não pode ser vazio");
        }else{
            this.setorAtendimento = setorAtendimento;
        }

        if(horasExtras < 0){
            throw new IllegalArgumentException("A quantidade de horas extras não pode ser negativa");
        }else{
            this.horasExtras = horasExtras;
        }
    }

    /**
     * Método para retornar o setor de atendimento.
     * @return o setor cadastrado.
     */
    public String getSetorAtendimento() {
        return this.setorAtendimento;
    }

    /**
     * Método para retornar as horas extras trabalhadas.
     * @return a quantidade de horas extras.
     */
    public int getHorasExtras() {
        return this.horasExtras;
    }

    /**
     * Método para retornar o nome do cargo.
     * @return Técnico Bancário.
     */
    @Override
    public String getCargo(){
        return "Técnico Bancário";
    }

    /**
     * Método para formatar os dados específicos do técnico.
     * @return texto com setor e horas extras.
     */
    @Override
    public String exibirDadosEspecificos(){
        return "Setor de atendimento: " + this.getSetorAtendimento() + "\n" +
            "Horas extras: " + this.getHorasExtras();
    }

    /**
     * Método para calcular o salário do técnico: base + (horas extras * 30).
     * @return o salário final com duas casas decimais.
     */
    @Override
    public BigDecimal calcularSalario(){
        BigDecimal acrescimo = VALOR_HORA_EXTRA.multiply(BigDecimal.valueOf(this.getHorasExtras()));

        return this.arredondar(this.getSalarioBase().add(acrescimo));
    }
}
