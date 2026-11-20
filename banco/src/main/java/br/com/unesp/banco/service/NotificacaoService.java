package br.com.unesp.banco.service;

import br.com.unesp.banco.model.notificacao.CanalEmail;
import br.com.unesp.banco.model.notificacao.CanalNotificacao;
import br.com.unesp.banco.model.pessoa.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para enviar (simular) as notificações dos clientes após as transações.
 * Essa classe aplica o padrão Singleton, pois o canal de comunicação é uma configuração
 * global do banco: se cada parte do sistema tivesse o seu próprio service, o canal
 * configurado na tela não seria o mesmo usado nas transações, e o histórico de
 * notificações ficaria espalhado.
 */
public class NotificacaoService {

    /**
     * Instância única da classe, garantindo que exista apenas um NotificacaoService.
     */
    private static final NotificacaoService INSTANCE = new NotificacaoService();

    /**
     * Canal de comunicação configurado no momento (o padrão é o e-mail).
     */
    private CanalNotificacao canal;

    /**
     * Histórico dos registros de envio, na ordem em que foram enviados.
     */
    private final List<String> enviadas = new ArrayList<>();

    /**
     * Construtor com visibilidade de pacote: impede a criação externa, mas permite
     * que os testes criem instâncias isoladas.
     */
    NotificacaoService() {
        this.canal = new CanalEmail();
    }

    /**
     * Método para obter a instância única do service.
     * @return a instância compartilhada de NotificacaoService.
     */
    public static NotificacaoService getInstance() {
        return INSTANCE;
    }

    /**
     * Método para configurar o canal usado nas próximas notificações.
     * @param canal canal de comunicação (SMS, e-mail ou WhatsApp).
     */
    public void definirCanal(CanalNotificacao canal){
        if(canal == null){
            throw new IllegalArgumentException("O canal não pode ser nulo");
        }

        this.canal = canal;
    }

    /**
     * Método para retornar o canal configurado.
     * @return o canal de comunicação atual.
     */
    public CanalNotificacao getCanal() {
        return this.canal;
    }

    /**
     * Método para enviar (simular) uma notificação ao cliente pelo canal configurado.
     * O registro é guardado no histórico e exibido no console.
     * @param cliente cliente que receberá a notificação.
     * @param mensagem texto da notificação.
     */
    public void enviarNotificacao(Cliente cliente, String mensagem){
        if(cliente == null){
            throw new IllegalArgumentException("O cliente não pode ser nulo");
        }

        if(mensagem == null || mensagem.trim().isEmpty()){
            throw new IllegalArgumentException("A mensagem não pode ser vazia");
        }

        String registro = this.canal.enviar(cliente, mensagem);

        this.enviadas.add(registro);
        System.out.println(registro);
    }

    /**
     * Método para listar as notificações já enviadas.
     * @return uma cópia do histórico de notificações.
     */
    public List<String> listarNotificacoes(){
        return List.copyOf(this.enviadas);
    }
}
