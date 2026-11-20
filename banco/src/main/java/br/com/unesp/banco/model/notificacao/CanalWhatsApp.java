package br.com.unesp.banco.model.notificacao;

import br.com.unesp.banco.model.pessoa.Cliente;

/**
 * Classe para simular o envio de notificações por WhatsApp, usando o telefone do cliente.
 */
public class CanalWhatsApp implements CanalNotificacao {

    /**
     * Método para simular o envio de uma mensagem de WhatsApp.
     * @param cliente cliente que receberá a mensagem.
     * @param mensagem texto da notificação.
     * @return o registro do envio.
     */
    @Override
    public String enviar(Cliente cliente, String mensagem){
        return "[WHATSAPP] Para " + cliente.getTelefone() + ": " + mensagem;
    }

    /**
     * Método para retornar o nome do canal.
     * @return WhatsApp.
     */
    @Override
    public String getNome(){
        return "WhatsApp";
    }
}
