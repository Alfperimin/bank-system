package br.com.unesp.banco.service;

import br.com.unesp.banco.model.pessoa.Endereco;
import br.com.unesp.banco.model.pessoa.PessoaFisica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BancoServiceNotificacaoTest {

    private BancoService banco;
    private NotificacaoService notificacao;
    private int conta1;
    private int conta2;

    @BeforeEach
    public void preparar(){
        this.banco = new BancoService();
        this.notificacao = new NotificacaoService();
        this.banco.setNotificacaoService(this.notificacao);

        Endereco endereco = new Endereco("Rua A", "10", "Centro", "Prudente", "SP", "19000-000");
        this.banco.adicionarCliente(new PessoaFisica("Ana", "111", "1899", "ana@email.com", endereco));
        this.banco.adicionarCliente(new PessoaFisica("Bia", "222", "1898", "bia@email.com", endereco));
        this.conta1 = this.banco.abrirConta("111");
        this.conta2 = this.banco.abrirConta("222");
    }

    @Test
    public void depositoNotifica(){
        // Given / When
        this.banco.depositar(this.conta1, new BigDecimal("100"));

        // Then
        assertEquals(1, this.notificacao.listarNotificacoes().size());
        assertTrue(this.notificacao.listarNotificacoes().get(0).contains("ana@email.com"));
        assertTrue(this.notificacao.listarNotificacoes().get(0).contains("Depósito"));
    }

    @Test
    public void saqueNotifica(){
        // Given
        this.banco.depositar(this.conta1, new BigDecimal("100"));

        // When
        this.banco.sacar(this.conta1, new BigDecimal("30"));

        // Then
        assertEquals(2, this.notificacao.listarNotificacoes().size());
        assertTrue(this.notificacao.listarNotificacoes().get(1).contains("Saque"));
    }

    @Test
    public void transferenciaNotificaOTitularDaOrigem(){
        // Given
        this.banco.depositar(this.conta1, new BigDecimal("100"));

        // When
        this.banco.transferir(this.conta1, this.conta2, new BigDecimal("40"));

        // Then
        assertEquals(2, this.notificacao.listarNotificacoes().size());
        assertTrue(this.notificacao.listarNotificacoes().get(1).contains("ana@email.com"));
        assertTrue(this.notificacao.listarNotificacoes().get(1).contains("Transferência"));
    }

    @Test
    public void pixNotifica(){
        // Given
        this.banco.depositar(this.conta1, new BigDecimal("100"));

        // When
        this.banco.pagar(this.conta1, "PIX", new BigDecimal("20"));

        // Then
        assertEquals(2, this.notificacao.listarNotificacoes().size());
        assertTrue(this.notificacao.listarNotificacoes().get(1).contains("PIX"));
    }

    @Test
    public void cartaoNotifica(){
        // Given
        this.banco.depositar(this.conta1, new BigDecimal("100"));

        // When
        this.banco.pagar(this.conta1, "cartao", new BigDecimal("20"));

        // Then
        assertEquals(2, this.notificacao.listarNotificacoes().size());
        assertTrue(this.notificacao.listarNotificacoes().get(1).contains("CARTAO"));
    }

    @Test
    public void boletoNaoNotifica(){
        // Given
        this.banco.depositar(this.conta1, new BigDecimal("100"));

        // When
        this.banco.pagar(this.conta1, "BOLETO", new BigDecimal("20"));

        // Then
        assertEquals(1, this.notificacao.listarNotificacoes().size());
    }

    @Test
    public void saqueComSaldoInsuficienteNaoNotifica(){
        // Given / When
        assertThrows(IllegalStateException.class, () -> {
            this.banco.sacar(this.conta1, new BigDecimal("50"));
        });

        // Then
        assertEquals(0, this.notificacao.listarNotificacoes().size());
    }

    @Test
    public void transferenciaSemSaldoNaoNotifica(){
        // Given / When
        assertThrows(IllegalStateException.class, () -> {
            this.banco.transferir(this.conta1, this.conta2, new BigDecimal("50"));
        });

        // Then
        assertEquals(0, this.notificacao.listarNotificacoes().size());
    }

    @Test
    public void semServicoConfiguradoNaoQuebra(){
        // Given
        BancoService semNotificacao = new BancoService();
        semNotificacao.adicionarCliente(new PessoaFisica("Ana", "111", "1899", "ana@email.com",
            new Endereco("Rua A", "10", "Centro", "Prudente", "SP", "19000-000")));
        int numero = semNotificacao.abrirConta("111");

        // When
        semNotificacao.depositar(numero, new BigDecimal("100"));

        // Then
        assertEquals(0, new BigDecimal("100").compareTo(semNotificacao.buscarConta(numero).getSaldo()));
    }
}
