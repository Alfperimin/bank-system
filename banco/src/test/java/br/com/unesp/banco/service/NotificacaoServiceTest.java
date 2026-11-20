package br.com.unesp.banco.service;

import br.com.unesp.banco.model.notificacao.CanalSms;
import br.com.unesp.banco.model.notificacao.CanalWhatsApp;
import br.com.unesp.banco.model.pessoa.Cliente;
import br.com.unesp.banco.model.pessoa.Endereco;
import br.com.unesp.banco.model.pessoa.PessoaFisica;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotificacaoServiceTest {

    private Cliente cliente(){
        return new PessoaFisica("Ana", "111", "18999990000", "ana@email.com",
            new Endereco("Rua A", "10", "Centro", "Prudente", "SP", "19000-000"));
    }

    @Test
    public void singletonRetornaSempreAMesmaInstancia(){
        // Given / When / Then
        assertSame(NotificacaoService.getInstance(), NotificacaoService.getInstance());
    }

    @Test
    public void canalPadraoEEmail(){
        // Given
        NotificacaoService service = new NotificacaoService();

        // When
        String nome = service.getCanal().getNome();

        // Then
        assertEquals("E-mail", nome);
    }

    @Test
    public void enviaPorEmailParaOEmailDoCliente(){
        // Given
        NotificacaoService service = new NotificacaoService();

        // When
        service.enviarNotificacao(cliente(), "Depósito realizado");

        // Then
        assertEquals("[E-MAIL] Para ana@email.com: Depósito realizado", service.listarNotificacoes().get(0));
    }

    @Test
    public void enviaPorSmsParaOTelefoneDoCliente(){
        // Given
        NotificacaoService service = new NotificacaoService();
        service.definirCanal(new CanalSms());

        // When
        service.enviarNotificacao(cliente(), "Saque realizado");

        // Then
        assertEquals("[SMS] Para 18999990000: Saque realizado", service.listarNotificacoes().get(0));
    }

    @Test
    public void enviaPorWhatsAppParaOTelefoneDoCliente(){
        // Given
        NotificacaoService service = new NotificacaoService();
        service.definirCanal(new CanalWhatsApp());

        // When
        service.enviarNotificacao(cliente(), "Pix realizado");

        // Then
        assertEquals("[WHATSAPP] Para 18999990000: Pix realizado", service.listarNotificacoes().get(0));
    }

    @Test
    public void trocaDeCanalValeParaAsProximasNotificacoes(){
        // Given
        NotificacaoService service = new NotificacaoService();
        service.enviarNotificacao(cliente(), "Primeira");

        // When
        service.definirCanal(new CanalSms());
        service.enviarNotificacao(cliente(), "Segunda");

        // Then
        assertEquals(2, service.listarNotificacoes().size());
        assertTrue(service.listarNotificacoes().get(0).startsWith("[E-MAIL]"));
        assertTrue(service.listarNotificacoes().get(1).startsWith("[SMS]"));
    }

    @Test
    public void canalNulo(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new NotificacaoService().definirCanal(null);
        });

        // Then
        assertEquals("O canal não pode ser nulo", e.getMessage());
    }

    @Test
    public void clienteNulo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new NotificacaoService().enviarNotificacao(null, "Mensagem");
        });
    }

    @Test
    public void mensagemVazia(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new NotificacaoService().enviarNotificacao(cliente(), "   ");
        });

        // Then
        assertEquals("A mensagem não pode ser vazia", e.getMessage());
    }
}
