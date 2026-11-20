package br.com.unesp.banco.service;

import br.com.unesp.banco.model.conta.Conta;
import br.com.unesp.banco.model.funcionario.*;
import br.com.unesp.banco.model.pessoa.*;
import br.com.unesp.banco.model.transacao.TipoTransacao;
import br.com.unesp.banco.model.transacao.Transacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ArquivoServiceTest {

    @TempDir
    Path pasta;

    private EnderecoFuncionario enderecoFuncionario(){
        return new EnderecoFuncionario("Rua A", "10", "Centro", "19000-000", "18999999999");
    }

    private Endereco endereco(){
        return new Endereco("Rua B", "20", "Vila", "Prudente", "SP", "19000-111");
    }

    @Test
    public void salvaECarregaFuncionariosDeTodosOsCargos(){
        // Given
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Gerente("Ana", "111", enderecoFuncionario(), new BigDecimal("1000.00"), new BigDecimal("500"), "Ag 001"));
        funcionarios.add(new TecnicoBancario("Bia", "222", enderecoFuncionario(), new BigDecimal("2000.00"), "Caixa", 10));
        funcionarios.add(new Diretor("Caio", "333", enderecoFuncionario(), new BigDecimal("1000.00"), new BigDecimal("10000"), "Crédito"));

        // When
        arquivo.salvarFuncionarios(funcionarios);
        List<Funcionario> carregados = arquivo.carregarFuncionarios();

        // Then
        assertEquals(3, carregados.size());
        assertTrue(carregados.get(0) instanceof Gerente);
        assertTrue(carregados.get(1) instanceof TecnicoBancario);
        assertTrue(carregados.get(2) instanceof Diretor);
        assertEquals(new BigDecimal("21000.00"), carregados.get(0).calcularSalario());
        assertEquals(new BigDecimal("2300.00"), carregados.get(1).calcularSalario());
        assertEquals(new BigDecimal("36000.00"), carregados.get(2).calcularSalario());
        assertEquals("Ag 001", ((Gerente) carregados.get(0)).getAgenciaResponsavel());
        assertEquals(10, ((TecnicoBancario) carregados.get(1)).getHorasExtras());
        assertEquals("Crédito", ((Diretor) carregados.get(2)).getAreaResponsabilidade());
        assertEquals("19000-000", carregados.get(0).getEndereco().getCep());
    }

    @Test
    public void camposComPontoEVirgulaEBarraSobrevivemAoSalvar(){
        // Given
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Gerente("Ana; da Silva \\ Souza", "111", enderecoFuncionario(), new BigDecimal("1000"), BigDecimal.ZERO, "Ag; 001"));

        // When
        arquivo.salvarFuncionarios(funcionarios);
        List<Funcionario> carregados = arquivo.carregarFuncionarios();

        // Then
        assertEquals("Ana; da Silva \\ Souza", carregados.get(0).getNome());
        assertEquals("Ag; 001", ((Gerente) carregados.get(0)).getAgenciaResponsavel());
    }

    @Test
    public void salvaECarregaClientesPFEPJ(){
        // Given
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new PessoaFisica("Ana", "111", "1899", "ana@email.com", endereco()));
        clientes.add(new PessoaJuridica("Padaria LTDA", "99.999.999/0001-99", "1898", "padaria@email.com", endereco()));

        // When
        arquivo.salvarClientes(clientes);
        List<Cliente> carregados = arquivo.carregarClientes();

        // Then
        assertEquals(2, carregados.size());
        assertTrue(carregados.get(0) instanceof PessoaFisica);
        assertTrue(carregados.get(1) instanceof PessoaJuridica);
        assertEquals("Ana", carregados.get(0).getNome());
        assertEquals("99.999.999/0001-99", carregados.get(1).getDocumento());
        assertEquals("Prudente", carregados.get(1).getEndereco().getCidade());
    }

    @Test
    public void salvaECarregaContasComSaldoEHistorico(){
        // Given
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());
        Cliente ana = new PessoaFisica("Ana", "111", "1899", "ana@email.com", endereco());
        Conta conta = new Conta(1, ana);
        conta.depositar(new BigDecimal("100.50"));
        conta.sacar(new BigDecimal("30.25"));
        List<Conta> contas = new ArrayList<>();
        contas.add(conta);

        // When
        arquivo.salvarContas(contas);
        arquivo.salvarMovimentacoes(contas);
        List<Conta> carregadas = arquivo.carregarContas(List.of(ana), arquivo.carregarMovimentacoes());

        // Then
        assertEquals(1, carregadas.size());
        assertEquals(new BigDecimal("70.25"), carregadas.get(0).getSaldo());
        assertSame(ana, carregadas.get(0).getTitular());
        assertEquals(2, carregadas.get(0).getTransacoes().size());
        assertEquals(TipoTransacao.DEPOSITO, carregadas.get(0).getTransacoes().get(0).getTipo());
        assertEquals(new BigDecimal("30.25"), carregadas.get(0).getTransacoes().get(1).getValor());
    }

    @Test
    public void movimentacoesMantemDataHoraETipo(){
        // Given
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());
        Cliente ana = new PessoaFisica("Ana", "111", "1899", "ana@email.com", endereco());
        Conta conta = new Conta(1, ana);
        conta.depositar(new BigDecimal("100"));
        conta.registrarSimulacao(new BigDecimal("5000"));
        List<Conta> contas = List.of(conta);

        // When
        arquivo.salvarMovimentacoes(contas);
        Map<Integer, List<Transacao>> carregadas = arquivo.carregarMovimentacoes();

        // Then
        assertEquals(conta.getTransacoes().get(0).getDataHora(), carregadas.get(1).get(0).getDataHora());
        assertEquals(TipoTransacao.SIMULACAO, carregadas.get(1).get(1).getTipo());
    }

    @Test
    public void semArquivosRetornaListasVazias(){
        // Given
        ArquivoService arquivo = new ArquivoService(this.pasta.resolve("nao-existe").toString());

        // When / Then
        assertTrue(arquivo.carregarFuncionarios().isEmpty());
        assertTrue(arquivo.carregarClientes().isEmpty());
        assertTrue(arquivo.carregarMovimentacoes().isEmpty());
        assertTrue(arquivo.carregarContas(new ArrayList<>(), arquivo.carregarMovimentacoes()).isEmpty());
    }

    @Test
    public void salvarCriaAPastaSeNaoExistir(){
        // Given
        Path nova = this.pasta.resolve("sub").resolve("dados");
        ArquivoService arquivo = new ArquivoService(nova.toString());

        // When
        arquivo.salvarFuncionarios(new ArrayList<>());

        // Then
        assertTrue(Files.exists(nova.resolve(ArquivoService.ARQUIVO_FUNCIONARIOS)));
    }

    @Test
    public void linhaComCamposFaltandoInformaALinha() throws IOException {
        // Given
        Files.write(this.pasta.resolve(ArquivoService.ARQUIVO_FUNCIONARIOS), List.of("GERENTE;Ana;111"), StandardCharsets.UTF_8);
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());

        // When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            arquivo.carregarFuncionarios();
        });

        // Then
        assertTrue(e.getMessage().contains("Linha 1 inválida em funcionarios.txt"));
    }

    @Test
    public void tipoDeFuncionarioDesconhecido() throws IOException {
        // Given
        Files.write(this.pasta.resolve(ArquivoService.ARQUIVO_FUNCIONARIOS),
            List.of("ESTAGIARIO;Ana;111;Rua;1;Centro;19000;1899;1000;0;X"), StandardCharsets.UTF_8);
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            arquivo.carregarFuncionarios();
        });
    }

    @Test
    public void contaComTitularInexistente() throws IOException {
        // Given
        Files.write(this.pasta.resolve(ArquivoService.ARQUIVO_CONTAS), List.of("1;999;100"), StandardCharsets.UTF_8);
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());

        // When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            arquivo.carregarContas(new ArrayList<>(), arquivo.carregarMovimentacoes());
        });

        // Then
        assertTrue(e.getMessage().contains("Titular não encontrado: 999"));
    }

    @Test
    public void movimentacaoComTipoInvalido() throws IOException {
        // Given
        Files.write(this.pasta.resolve(ArquivoService.ARQUIVO_MOVIMENTACOES),
            List.of("1;2026-11-16T10:30:00;INEXISTENTE;10"), StandardCharsets.UTF_8);
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            arquivo.carregarMovimentacoes();
        });
    }

    @Test
    public void pastaVazia(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new ArquivoService("  ");
        });
    }

    @Test
    public void salvarECarregarTodosOsDadosRestauraOsServices(){
        // Given
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());
        BancoService banco = new BancoService();
        FuncionarioService funcionarios = new FuncionarioService();

        banco.adicionarCliente(new PessoaFisica("Ana", "111", "1899", "ana@email.com", endereco()));
        banco.adicionarCliente(new PessoaJuridica("Padaria LTDA", "222", "1898", "padaria@email.com", endereco()));
        int conta1 = banco.abrirConta("111");
        int conta2 = banco.abrirConta("222");
        banco.depositar(conta1, new BigDecimal("500"));
        banco.transferir(conta1, conta2, new BigDecimal("200"));
        banco.pagar(conta2, "PIX", new BigDecimal("50"));
        funcionarios.cadastrarFuncionario(new Diretor("Caio", "333", enderecoFuncionario(), new BigDecimal("1000"), BigDecimal.ZERO, "Crédito"));

        // When
        arquivo.salvarDados(banco, funcionarios);

        BancoService bancoNovo = new BancoService();
        FuncionarioService funcionariosNovo = new FuncionarioService();
        arquivo.carregarDados(bancoNovo, funcionariosNovo);

        // Then
        assertEquals(2, bancoNovo.listarClientes().size());
        assertEquals(2, bancoNovo.listarContas().size());
        assertEquals(0, new BigDecimal("300").compareTo(bancoNovo.buscarConta(conta1).getSaldo()));
        assertEquals(0, new BigDecimal("150").compareTo(bancoNovo.buscarConta(conta2).getSaldo()));
        assertEquals(2, bancoNovo.buscarConta(conta1).getTransacoes().size());
        assertEquals(2, bancoNovo.buscarConta(conta2).getTransacoes().size());
        assertEquals("Caio", funcionariosNovo.buscarFuncionario("333").getNome());
    }

    @Test
    public void numeroDaProximaContaContinuaDepoisDeCarregar(){
        // Given
        ArquivoService arquivo = new ArquivoService(this.pasta.toString());
        BancoService banco = new BancoService();
        banco.adicionarCliente(new PessoaFisica("Ana", "111", "1899", "ana@email.com", endereco()));
        banco.abrirConta("111");
        banco.abrirConta("111");
        arquivo.salvarDados(banco, new FuncionarioService());

        // When
        BancoService bancoNovo = new BancoService();
        arquivo.carregarDados(bancoNovo, new FuncionarioService());
        int novaConta = bancoNovo.abrirConta("111");

        // Then
        assertEquals(3, novaConta);
    }
}
