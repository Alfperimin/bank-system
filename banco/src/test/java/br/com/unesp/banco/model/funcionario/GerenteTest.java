package br.com.unesp.banco.model.funcionario;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class GerenteTest {

    private EnderecoFuncionario endereco(){
        return new EnderecoFuncionario("Rua A", "10", "Centro", "19000-000", "18999999999");
    }

    @Test
    public void salarioBaseMilReais(){
        // Given: 1000 + (1000 * 20) = 21000
        Gerente gerente = new Gerente("Ana", "111", endereco(), new BigDecimal("1000.00"), new BigDecimal("500"), "Ag 001");

        // When
        BigDecimal salario = gerente.calcularSalario();

        // Then
        assertEquals(new BigDecimal("21000.00"), salario);
    }

    @Test
    public void salarioBaseComCentavos(){
        // Given: 1234.56 + (1234.56 * 20) = 25925.76
        Gerente gerente = new Gerente("Ana", "111", endereco(), new BigDecimal("1234.56"), BigDecimal.ZERO, "Ag 001");

        // When
        BigDecimal salario = gerente.calcularSalario();

        // Then
        assertEquals(new BigDecimal("25925.76"), salario);
    }

    @Test
    public void salarioBaseZero(){
        // Given
        Gerente gerente = new Gerente("Ana", "111", endereco(), BigDecimal.ZERO, BigDecimal.ZERO, "Ag 001");

        // When
        BigDecimal salario = gerente.calcularSalario();

        // Then
        assertEquals(new BigDecimal("0.00"), salario);
    }

    @Test
    public void bonusNaoAlteraOSalario(){
        // Given
        Gerente semBonus = new Gerente("Ana", "111", endereco(), new BigDecimal("1000.00"), BigDecimal.ZERO, "Ag 001");
        Gerente comBonus = new Gerente("Ana", "111", endereco(), new BigDecimal("1000.00"), new BigDecimal("9999"), "Ag 001");

        // When / Then
        assertEquals(semBonus.calcularSalario(), comBonus.calcularSalario());
    }

    @Test
    public void polimorfismoGerente(){
        // Given
        Funcionario funcionario = new Gerente("Ana", "111", endereco(), new BigDecimal("1000.00"), BigDecimal.ZERO, "Ag 001");

        // When
        BigDecimal salario = funcionario.calcularSalario();

        // Then
        assertEquals(new BigDecimal("21000.00"), salario);
    }

    @Test
    public void cargoDoFuncionario(){
        // Given
        Gerente gerente = new Gerente("Ana", "111", endereco(), new BigDecimal("1000.00"), BigDecimal.ZERO, "Ag 001");

        // When
        String cargo = gerente.getCargo();

        // Then
        assertEquals("Gerente", cargo);
    }

    @Test
    public void exibirDadosComSalarioFinal(){
        // Given
        Gerente gerente = new Gerente("Ana", "111", endereco(), new BigDecimal("1000.00"), new BigDecimal("500"), "Ag 001");

        // When
        String dados = gerente.exibirDados();

        // Then
        assertTrue(dados.contains("Nome: Ana"));
        assertTrue(dados.contains("Agência responsável: Ag 001"));
        assertTrue(dados.contains("Salário final: R$ 21000.00"));
    }

    @Test
    public void nomeNulo(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new Gerente(null, "111", endereco(), new BigDecimal("1000"), BigDecimal.ZERO, "Ag 001");
        });

        // Then
        assertEquals("O nome não pode ser vazio", e.getMessage());
    }

    @Test
    public void cpfVazio(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Gerente("Ana", "", endereco(), new BigDecimal("1000"), BigDecimal.ZERO, "Ag 001");
        });
    }

    @Test
    public void enderecoNulo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Gerente("Ana", "111", null, new BigDecimal("1000"), BigDecimal.ZERO, "Ag 001");
        });
    }

    @Test
    public void salarioBaseNegativo(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new Gerente("Ana", "111", endereco(), new BigDecimal("-1"), BigDecimal.ZERO, "Ag 001");
        });

        // Then
        assertEquals("O salário base não pode ser negativo", e.getMessage());
    }

    @Test
    public void salarioBaseNulo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Gerente("Ana", "111", endereco(), null, BigDecimal.ZERO, "Ag 001");
        });
    }

    @Test
    public void bonusNegativo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Gerente("Ana", "111", endereco(), new BigDecimal("1000"), new BigDecimal("-1"), "Ag 001");
        });
    }

    @Test
    public void agenciaEmBranco(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new Gerente("Ana", "111", endereco(), new BigDecimal("1000"), BigDecimal.ZERO, "   ");
        });

        // Then
        assertEquals("A agência responsável não pode ser vazia", e.getMessage());
    }
}
