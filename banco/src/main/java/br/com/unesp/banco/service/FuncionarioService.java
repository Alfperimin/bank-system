package br.com.unesp.banco.service;

import br.com.unesp.banco.model.funcionario.Funcionario;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para gerenciar os funcionários do banco: cadastro, consulta e relatório.
 * Essa classe aplica o padrão Singleton, pois mantém uma única instância compartilhada
 * por toda a aplicação. Foi escolhido porque a lista de funcionários é um estado global
 * do sistema: se a interface gráfica, a persistência e os testes criassem cada um o seu
 * próprio service, os dados ficariam divergentes entre si.
 */
public class FuncionarioService {

    /**
     * Instância única da classe, garantindo que exista apenas um FuncionarioService.
     */
    private static final FuncionarioService INSTANCE = new FuncionarioService();

    /**
     * Lista de funcionários cadastrados no sistema.
     */
    private final List<Funcionario> funcionarios = new ArrayList<>();

    /**
     * Construtor com visibilidade de pacote: impede a criação externa, mas permite
     * que os testes criem instâncias isoladas.
     */
    FuncionarioService() { }

    /**
     * Método para obter a instância única do service.
     * @return a instância compartilhada de FuncionarioService.
     */
    public static FuncionarioService getInstance() {
        return INSTANCE;
    }

    /**
     * Método para cadastrar um funcionário já construído.
     * @param funcionario funcionário a ser cadastrado.
     */
    public void cadastrarFuncionario(Funcionario funcionario){
        if(funcionario == null){
            throw new IllegalArgumentException("O funcionário não pode ser nulo");
        }

        if(this.localizarFuncionario(funcionario.getCpf()) != null){
            throw new IllegalArgumentException("CPF já cadastrado: " + funcionario.getCpf());
        }

        this.funcionarios.add(funcionario);
    }

    /**
     * Método para buscar um funcionário pelo CPF.
     * @param cpf CPF do funcionário.
     * @return o funcionário encontrado ou uma exceção (funcionário não encontrado).
     */
    public Funcionario buscarFuncionario(String cpf){
        Funcionario funcionario = this.localizarFuncionario(cpf);

        if(funcionario == null){
            throw new IllegalArgumentException("Funcionário não encontrado: " + cpf);
        }

        return funcionario;
    }

    /**
     * Método interno para localizar um funcionário na lista.
     * @param cpf CPF do funcionário.
     * @return o funcionário encontrado ou null (não encontrado).
     */
    private Funcionario localizarFuncionario(String cpf){
        for(Funcionario f : this.funcionarios){
            if(f.getCpf().equals(cpf)){
                return f;
            }
        }

        return null;
    }

    /**
     * Método para exibir os dados detalhados de um funcionário, com o salário final.
     * @param cpf CPF do funcionário.
     * @return os dados completos do funcionário.
     */
    public String exibirFuncionario(String cpf){
        return this.buscarFuncionario(cpf).exibirDados();
    }

    /**
     * Método para retornar a lista de funcionários cadastrados.
     * @return uma cópia da lista no estado atual da aplicação.
     */
    public List<Funcionario> listarFuncionarios(){
        return List.copyOf(this.funcionarios);
    }

    /**
     * Método para somar o salário final de todos os funcionários.
     * @return o total da folha de pagamento.
     */
    public BigDecimal calcularFolhaPagamento(){
        BigDecimal total = BigDecimal.ZERO;

        for(Funcionario f : this.funcionarios){
            total = total.add(f.calcularSalario());
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Método para gerar o relatório consolidado com todos os funcionários.
     * @return texto com os dados de cada funcionário e o total da folha.
     */
    public String gerarRelatorioFuncionarios(){
        if(this.funcionarios.isEmpty()){
            return "Nenhum funcionário cadastrado.";
        }

        String relatorio = "===== RELATÓRIO DE FUNCIONÁRIOS =====\n";

        for(Funcionario f : this.funcionarios){
            relatorio += f.exibirDados() + "\n-----------------------------------\n";
        }

        relatorio += "Total de funcionários: " + this.funcionarios.size() + "\n";
        relatorio += "Total da folha: R$ " + this.calcularFolhaPagamento();

        return relatorio;
    }

    /**
     * Método para restaurar os funcionários carregados de arquivo.
     * @param carregados lista de funcionários a ser restaurada.
     */
    public void restaurar(List<Funcionario> carregados){
        this.funcionarios.clear();
        this.funcionarios.addAll(carregados);
    }
}
