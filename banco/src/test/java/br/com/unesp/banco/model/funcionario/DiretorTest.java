package br.com.unesp.banco.model.funcionario;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DiretorTest {

    private EnderecoFuncionario endereco(){
        return new EnderecoFuncionario("Rua A", "10", "Centro", "19000-000", "18999999999");
    }

    @Test
    public void salarioBaseMilReais(){
        // Given: 1000 + (1000 * 35) = 36000
        Diretor diretor = new Diretor("Caio", "333", endereco(), new BigDecimal("1000.00"), new BigDecimal("10000"), "Crédito");

        // When
        BigDecimal salario = diretor.calcularSalario();

        // Then
        assertEquals(new BigDecimal("36000.00"), salario);
    }

    @Test
    public void salarioBaseComCentavos(){
        // Given: 1234.56 + (1234.56 * 35) = 44444.16
        Diretor diretor = new Diretor("Caio", "333", endereco(), new BigDecimal("1234.56"), BigDecimal.ZERO, "Crédito");

        // When
        BigDecimal salario = diretor.calcularSalario();

        // Then
        assertEquals(new BigDecimal("44444.16"), salario);
    }

    @Test
    public void salarioBaseZero(){
        // Given
        Diretor diretor = new Diretor("Caio", "333", endereco(), BigDecimal.ZERO, BigDecimal.ZERO, "Crédito");

        // When
        BigDecimal salario = diretor.calcularSalario();

        // Then
        assertEquals(new BigDecimal("0.00"), salario);
    }

    @Test
    public void plrNaoAlteraOSalario(){
        // Given
        Diretor semPlr = new Diretor("Caio", "333", endereco(), new BigDecimal("1000.00"), BigDecimal.ZERO, "Crédito");
        Diretor comPlr = new Diretor("Caio", "333", endereco(), new BigDecimal("1000.00"), new BigDecimal("50000"), "Crédito");

        // When / Then
        assertEquals(semPlr.calcularSalario(), comPlr.calcularSalario());
    }

    @Test
    public void polimorfismoDiretor(){
        // Given
        Funcionario funcionario = new Diretor("Caio", "333", endereco(), new BigDecimal("1000.00"), BigDecimal.ZERO, "Crédito");

        // When
        BigDecimal salario = funcionario.calcularSalario();

        // Then
        assertEquals(new BigDecimal("36000.00"), salario);
    }

    @Test
    public void cargoDoFuncionario(){
        // Given
        Diretor diretor = new Diretor("Caio", "333", endereco(), new BigDecimal("1000.00"), BigDecimal.ZERO, "Crédito");

        // When
        String cargo = diretor.getCargo();

        // Then
        assertEquals("Diretor", cargo);
    }

    @Test
    public void exibirDadosComSalarioFinal(){
        // Given
        Diretor diretor = new Diretor("Caio", "333", endereco(), new BigDecimal("1000.00"), new BigDecimal("10000"), "Crédito");

        // When
        String dados = diretor.exibirDados();

        // Then
        assertTrue(dados.contains("Área de responsabilidade: Crédito"));
        assertTrue(dados.contains("Participação nos lucros: R$ 10000.00"));
        assertTrue(dados.contains("Salário final: R$ 36000.00"));
    }

    @Test
    public void plrNegativa(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new Diretor("Caio", "333", endereco(), new BigDecimal("1000"), new BigDecimal("-1"), "Crédito");
        });

        // Then
        assertEquals("A participação nos lucros não pode ser negativa", e.getMessage());
    }

    @Test
    public void plrNula(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Diretor("Caio", "333", endereco(), new BigDecimal("1000"), null, "Crédito");
        });
    }

    @Test
    public void areaVazia(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Diretor("Caio", "333", endereco(), new BigDecimal("1000"), BigDecimal.ZERO, "");
        });
    }

    @Test
    public void cpfNulo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Diretor("Caio", null, endereco(), new BigDecimal("1000"), BigDecimal.ZERO, "Crédito");
        });
    }
}
