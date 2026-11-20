package br.com.unesp.banco.model.notificacao;

import br.com.unesp.banco.model.pessoa.Cliente;

/**
 * Interface para definir um canal de comunicação usado nas notificações.
 * Essa interface aplica o padrão Strategy: cada canal (SMS, E-mail e WhatsApp) é uma
 * estratégia de envio intercambiável. Foi escolhido porque o enunciado pede um canal
 * "configurado", ou seja, que pode ser trocado; assim o NotificacaoService funciona com
 * qualquer canal sem if/else por tipo, e um novo canal pode ser criado sem alterar o service.
 */
public interface CanalNotificacao {

    /**
     * Método para enviar (simular) uma mensagem a um cliente por este canal.
     * @param cliente cliente que receberá a notificação.
     * @param mensagem texto da notificação.
     * @return o registro do envio, com o canal, o destino e a mensagem.
     */
    String enviar(Cliente cliente, String mensagem);

    /**
     * Método para retornar o nome do canal.
     * @return o nome do canal, como SMS, E-mail ou WhatsApp.
     */
    String getNome();
}
