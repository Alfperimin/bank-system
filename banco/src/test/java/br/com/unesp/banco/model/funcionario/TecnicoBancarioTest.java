package br.com.unesp.banco.model.funcionario;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TecnicoBancarioTest {

    private EnderecoFuncionario endereco(){
        return new EnderecoFuncionario("Rua A", "10", "Centro", "19000-000", "18999999999");
    }

    @Test
    public void semHorasExtras(){
        // Given
        TecnicoBancario tecnico = new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000.00"), "Caixa", 0);

        // When
        BigDecimal salario = tecnico.calcularSalario();

        // Then
        assertEquals(new BigDecimal("2000.00"), salario);
    }

    @Test
    public void dezHorasExtras(){
        // Given: 2000 + (10 * 30) = 2300
        TecnicoBancario tecnico = new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000.00"), "Caixa", 10);

        // When
        BigDecimal salario = tecnico.calcularSalario();

        // Then
        assertEquals(new BigDecimal("2300.00"), salario);
    }

    @Test
    public void umaHoraExtra(){
        // Given: 2000.50 + (1 * 30) = 2030.50
        TecnicoBancario tecnico = new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000.50"), "Caixa", 1);

        // When
        BigDecimal salario = tecnico.calcularSalario();

        // Then
        assertEquals(new BigDecimal("2030.50"), salario);
    }

    @Test
    public void polimorfismoTecnico(){
        // Given
        Funcionario funcionario = new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000.00"), "Caixa", 10);

        // When
        BigDecimal salario = funcionario.calcularSalario();

        // Then
        assertEquals(new BigDecimal("2300.00"), salario);
    }

    @Test
    public void cargoDoFuncionario(){
        // Given
        TecnicoBancario tecnico = new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000.00"), "Caixa", 10);

        // When
        String cargo = tecnico.getCargo();

        // Then
        assertEquals("Técnico Bancário", cargo);
    }

    @Test
    public void exibirDadosComSalarioFinal(){
        // Given
        TecnicoBancario tecnico = new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000.00"), "Caixa", 10);

        // When
        String dados = tecnico.exibirDados();

        // Then
        assertTrue(dados.contains("Setor de atendimento: Caixa"));
        assertTrue(dados.contains("Horas extras: 10"));
        assertTrue(dados.contains("Salário final: R$ 2300.00"));
    }

    @Test
    public void horasExtrasNegativas(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000"), "Caixa", -1);
        });

        // Then
        assertEquals("A quantidade de horas extras não pode ser negativa", e.getMessage());
    }

    @Test
    public void setorVazio(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000"), "", 10);
        });
    }

    @Test
    public void setorNulo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("2000"), null, 10);
        });
    }

    @Test
    public void nomeEmBranco(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TecnicoBancario("   ", "222", endereco(), new BigDecimal("2000"), "Caixa", 10);
        });
    }

    @Test
    public void salarioBaseNegativo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TecnicoBancario("Bia", "222", endereco(), new BigDecimal("-10"), "Caixa", 10);
        });
    }
}
