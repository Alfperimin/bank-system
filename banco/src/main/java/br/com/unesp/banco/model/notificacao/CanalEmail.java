package br.com.unesp.banco.model.notificacao;

import br.com.unesp.banco.model.pessoa.Cliente;

/**
 * Classe para simular o envio de notificações por e-mail, usando o e-mail do cliente.
 */
public class CanalEmail implements CanalNotificacao {

    /**
     * Método para simular o envio de um e-mail.
     * @param cliente cliente que receberá o e-mail.
     * @param mensagem texto da notificação.
     * @return o registro do envio.
     */
    @Override
    public String enviar(Cliente cliente, String mensagem){
        return "[E-MAIL] Para " + cliente.getEmail() + ": " + mensagem;
    }

    /**
     * Método para retornar o nome do canal.
     * @return E-mail.
     */
    @Override
    public String getNome(){
        return "E-mail";
    }
}
