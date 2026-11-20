package br.com.unesp.banco.model.notificacao;

import br.com.unesp.banco.model.pessoa.Cliente;

/**
 * Classe para simular o envio de notificações por SMS, usando o telefone do cliente.
 */
public class CanalSms implements CanalNotificacao {

    /**
     * Método para simular o envio de um SMS.
     * @param cliente cliente que receberá o SMS.
     * @param mensagem texto da notificação.
     * @return o registro do envio.
     */
    @Override
    public String enviar(Cliente cliente, String mensagem){
        return "[SMS] Para " + cliente.getTelefone() + ": " + mensagem;
    }

    /**
     * Método para retornar o nome do canal.
     * @return SMS.
     */
    @Override
    public String getNome(){
        return "SMS";
    }
}
