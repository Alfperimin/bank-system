package br.com.unesp.banco.service;

import br.com.unesp.banco.controller.FuncionarioController;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioServiceTest {

    @Test
    public void singletonRetornaSempreAMesmaInstancia(){
        // Given / When / Then
        assertSame(FuncionarioService.getInstance(), FuncionarioService.getInstance());
    }

    @Test
    public void cadastraEConsultaPorCpf(){
        // Given
        FuncionarioService service = new FuncionarioService();
        FuncionarioController controller = new FuncionarioController(service);

        // When
        controller.cadastrarGerente("Ana", "111", "Rua A", "10", "Centro", "19000-000", "1899", 1000, 500, "Ag 001");

        // Then
        assertTrue(controller.exibirFuncionario("111").contains("Ana"));
        assertEquals(1, service.listarFuncionarios().size());
    }

    @Test
    public void cpfDuplicado(){
        // Given
        FuncionarioService service = new FuncionarioService();
        FuncionarioController controller = new FuncionarioController(service);
        controller.cadastrarTecnico("Bia", "222", "Rua A", "10", "Centro", "19000-000", "1899", 2000, "Caixa", 5);

        // When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            controller.cadastrarTecnico("Outra", "222", "Rua B", "20", "Vila", "19000-001", "1898", 2000, "Caixa", 0);
        });

        // Then
        assertEquals("CPF já cadastrado: 222", e.getMessage());
    }

    @Test
    public void funcionarioNulo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioService().cadastrarFuncionario(null);
        });
    }

    @Test
    public void cpfInexistente(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioService().buscarFuncionario("999");
        });

        // Then
        assertEquals("Funcionário não encontrado: 999", e.getMessage());
    }

    @Test
    public void folhaDeTodosOsCargos(){
        // Given: 21000 + 2300 + 36000 = 59300
        FuncionarioService service = new FuncionarioService();
        FuncionarioController controller = new FuncionarioController(service);
        controller.cadastrarGerente("Ana", "111", "Rua A", "10", "Centro", "19000-000", "1899", 1000, 500, "Ag 001");
        controller.cadastrarTecnico("Bia", "222", "Rua A", "10", "Centro", "19000-000", "1899", 2000, "Caixa", 10);
        controller.cadastrarDiretor("Caio", "333", "Rua A", "10", "Centro", "19000-000", "1899", 1000, 10000, "Crédito");

        // When
        BigDecimal folha = service.calcularFolhaPagamento();

        // Then
        assertEquals(new BigDecimal("59300.00"), folha);
        assertEquals(3, controller.listarFuncionarios().size());
    }

    @Test
    public void relatorioComTodosOsFuncionarios(){
        // Given
        FuncionarioService service = new FuncionarioService();
        FuncionarioController controller = new FuncionarioController(service);
        controller.cadastrarGerente("Ana", "111", "Rua A", "10", "Centro", "19000-000", "1899", 1000, 500, "Ag 001");
        controller.cadastrarTecnico("Bia", "222", "Rua A", "10", "Centro", "19000-000", "1899", 2000, "Caixa", 10);

        // When
        String relatorio = controller.gerarRelatorioFuncionarios();

        // Then
        assertTrue(relatorio.contains("Ana"));
        assertTrue(relatorio.contains("Bia"));
        assertTrue(relatorio.contains("Total de funcionários: 2"));
        assertTrue(relatorio.contains("Total da folha: R$ 23300.00"));
    }

    @Test
    public void relatorioSemFuncionarios(){
        // Given / When
        String relatorio = new FuncionarioService().gerarRelatorioFuncionarios();

        // Then
        assertEquals("Nenhum funcionário cadastrado.", relatorio);
    }

    @Test
    public void restauraFuncionariosCarregados(){
        // Given
        FuncionarioService origem = new FuncionarioService();
        new FuncionarioController(origem).cadastrarDiretor("Caio", "333", "Rua A", "10", "Centro", "19000-000", "1899", 1000, 10000, "Crédito");
        FuncionarioService destino = new FuncionarioService();

        // When
        destino.restaurar(origem.listarFuncionarios());

        // Then
        assertEquals("Caio", destino.buscarFuncionario("333").getNome());
    }
}
