package br.com.unesp.banco.model.funcionario;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioFactoryTest {

    private EnderecoFuncionario endereco(){
        return new EnderecoFuncionario("Rua A", "10", "Centro", "19000-000", "18999999999");
    }

    @Test
    public void fabricaDeGerente(){
        // Given
        FuncionarioFactory fabrica = new GerenteFactory(new BigDecimal("500"), "Ag 001");

        // When
        Funcionario funcionario = fabrica.criarFuncionario("Ana", "111", endereco(), new BigDecimal("1000.00"));

        // Then
        assertTrue(funcionario instanceof Gerente);
        assertEquals(new BigDecimal("21000.00"), funcionario.calcularSalario());
    }

    @Test
    public void fabricaDeTecnico(){
        // Given
        FuncionarioFactory fabrica = new TecnicoBancarioFactory("Caixa", 10);

        // When
        Funcionario funcionario = fabrica.criarFuncionario("Bia", "222", endereco(), new BigDecimal("2000.00"));

        // Then
        assertTrue(funcionario instanceof TecnicoBancario);
        assertEquals(new BigDecimal("2300.00"), funcionario.calcularSalario());
    }

    @Test
    public void fabricaDeDiretor(){
        // Given
        FuncionarioFactory fabrica = new DiretorFactory(new BigDecimal("10000"), "Crédito");

        // When
        Funcionario funcionario = fabrica.criarFuncionario("Caio", "333", endereco(), new BigDecimal("1000.00"));

        // Then
        assertTrue(funcionario instanceof Diretor);
        assertEquals(new BigDecimal("36000.00"), funcionario.calcularSalario());
    }

    @Test
    public void fabricaRepassaValidacaoDoFuncionario(){
        // Given
        FuncionarioFactory fabrica = new GerenteFactory(BigDecimal.ZERO, "Ag 001");

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            fabrica.criarFuncionario("", "111", endereco(), new BigDecimal("1000"));
        });
    }
}
