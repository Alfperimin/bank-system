package br.com.unesp.banco.service;

import br.com.unesp.banco.model.conta.Conta;
import br.com.unesp.banco.model.pagamento.*;
import br.com.unesp.banco.model.pessoa.*;
import br.com.unesp.banco.model.transacao.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para gerenciar as operações do banco, como cadastro de clientes,
 * abertura de contas, movimentações financeiras e consulta de extratos.
 * Essa classe aplica o padrão Singleton, pois mantém uma única instância da
 * classe BancoService em toda a aplicação e centraliza o acesso ao estado global
 * do sistema bancário.
 */
public class BancoService {
    /**
     * Instância única da classe, mantendo o padrão Singleton para garantir que
     * exista apenas um objeto BancoService compartilhado por toda a aplicação.
     */
    private static final BancoService INSTANCE = new BancoService();

    BancoService() { }
    public static BancoService getInstance() {
        return INSTANCE;
    }

    /**
     * Lista de clientes e contas cadastradas no sistema.
     * Atributo auxiliar para calcular o numero de novas contas.
     */
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Conta> contas = new ArrayList<>();
    private int nextId = 1;

    /**
     * Serviço usado para notificar o cliente após as transações bem-sucedidas.
     * Enquanto não for configurado, nenhuma notificação é enviada.
     */
    private NotificacaoService notificacao;

    /**
     * Método para configurar o serviço de notificações.
     * @param notificacao serviço que enviará as notificações aos clientes.
     */
    public void setNotificacaoService(NotificacaoService notificacao) {
        this.notificacao = notificacao;
    }

    /**
     * Método interno para notificar o titular da conta após uma transação bem-sucedida.
     * @param conta conta cujo titular será notificado.
     * @param mensagem texto da notificação.
     */
    private void notificar(Conta conta, String mensagem) {
        if (notificacao != null) {
            notificacao.enviarNotificacao(conta.getTitular(), mensagem);
        }
    }


    /**
     * Método para a adição de clientes na lista de clientes registrados.
     * @param cliente recebe um objeto cliente já construído.
     */
    public void adicionarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nulo");
        }
        if (localizarCliente(cliente.getDocumento()) != null) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }

        clientes.add(cliente);
    }

    /**
     * Método para buscar um cliente na lista de clientes registrados
     * @param documento identificador, pode ser um cpf ou cnpj
     * @return o cliente encontrado ou uma execeção (cliente não encontrado)
     */
    public Cliente buscarCliente(String documento) {
        Cliente cliente = localizarCliente(documento);
        
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado: " + documento);
        }

        return cliente;
    }

    /**
     * Método interno para localizar um cliente na lista de clients registrados
     * @param documento identificador, pode ser um cpf ou cnpj
     * @return retorna o cliente encontrado ou null (não encontrado)
     */
    private Cliente localizarCliente(String documento) {
        for (Cliente c : clientes) {
            if (c.getDocumento().equals(documento)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Método para retornar a lista de clientes registrados
     * @return uma cópia da lista no estado atual da aplicação
     */
    public List<Cliente> listarClientes() {
        return List.copyOf(clientes);
    }

    /**
     * Método para abrir uma nova conta para o cliente informado.
     * @param documento identificador do cliente titular da conta.
     * @return o número da conta criada.
     */
    public int abrirConta(String documento) {
        Cliente titular = buscarCliente(documento);
        Conta conta = new Conta(nextId++, titular);
        contas.add(conta);

        return conta.getNumero();
    }

    /**
     * Método para buscar uma conta pelo número informado.
     * @param numero identificador da conta.
     * @return a conta encontrada.
     */
    public Conta buscarConta(int numero) {
        for (Conta c : contas) {
            if (c.getNumero() == numero) {
                return c;
            }
        }

        throw new IllegalArgumentException("Conta não encontrada: " + numero);
    }

    /**
     * Método para retornar a lista de contas registradas.
     * @return uma cópia da lista de contas no estado atual da aplicação.
     */
    public List<Conta> listarContas() {
        return List.copyOf(contas);
    }

    /**
     * Método para realizar o depósito de valor em uma conta específica.
     * @param numeroConta número da conta que receberá o valor.
     * @param valor valor a ser depositado.
     */
    public void depositar(int numeroConta, BigDecimal valor) {
        Conta conta = buscarConta(numeroConta);
        conta.depositar(valor);
        notificar(conta, String.format("Depósito de R$ %.2f realizado na conta %d.", valor, numeroConta));
    }

    /**
     * Método para realizar o saque de valor de uma conta específica.
     * @param numeroConta número da conta que realizará o saque.
     * @param valor valor a ser sacado.
     */
    public void sacar(int numeroConta, BigDecimal valor) {
        Conta conta = buscarConta(numeroConta);
        conta.sacar(valor);
        notificar(conta, String.format("Saque de R$ %.2f realizado na conta %d.", valor, numeroConta));
    }

    /**
     * Método para transferir valor entre duas contas.
     * @param origem número da conta de origem.
     * @param destino número da conta de destino.
     * @param valor valor a ser transferido.
     */
    public void transferir(int origem, int destino, BigDecimal valor) {
        Conta contaOrigem = buscarConta(origem);
        Conta contaDestino = buscarConta(destino);
        contaOrigem.transferir(contaDestino, valor);
        notificar(contaOrigem, String.format("Transferência de R$ %.2f da conta %d para a conta %d realizada.", valor, origem, destino));
    }

    /**
     * Método para realizar um pagamento utilizando um tipo específico de cobrança.
     * @param numeroConta número da conta que realizará o pagamento.
     * @param tipo tipo do pagamento a ser criado.
     * @param valor valor do pagamento.
     */
    public void pagar(int numeroConta, String tipo, BigDecimal valor) {
        Conta conta = buscarConta(numeroConta);
        Pagamento pagamento = PagamentoFactory.criar(tipo);
        pagamento.pagar(conta, valor);

        if (pagamento.notificar()) {
            notificar(conta, String.format("Pagamento via %s de R$ %.2f realizado na conta %d.",
                tipo.trim().toUpperCase(), valor, numeroConta));
        }
    }

    /**
     * Método para retornar as últimas transações registradas em uma conta.
     * @param numeroConta número da conta consultada.
     * @param n quantidade de transações a serem retornadas.
     * @return a lista com as últimas transações da conta.
     */
    public List<Transacao> extratoUltimas(int numeroConta, int n) {
        return buscarConta(numeroConta).extratoUltimas(n);
    }

    /**
     * Método para retornar o extrato de uma conta em um período específico.
     * @param numeroConta número da conta consultada.
     * @param inicio data inicial do período.
     * @param fim data final do período.
     * @return a lista de transações dentro do período informado.
     */
    public List<Transacao> extratoPorPeriodo(int numeroConta, LocalDateTime inicio, LocalDateTime fim) {
        return buscarConta(numeroConta).extratoPorPeriodo(inicio, fim);
    }

    /**
     * Método para restaurar os clientes e contas carregados na aplicação.
     * @param clientesCarregados lista de clientes a ser restaurada.
     * @param contasCarregadas lista de contas a ser restaurada.
     */
    public void restaurar(List<Cliente> clientesCarregados, List<Conta> contasCarregadas) {
        clientes.clear();
        clientes.addAll(clientesCarregados);

        contas.clear();
        contas.addAll(contasCarregadas);

        int maior = 0;
        for (Conta c : contas) {
            if (c.getNumero() > maior) maior = c.getNumero();
        }
        nextId = maior + 1;
    }
}