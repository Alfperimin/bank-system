package br.com.unesp.banco.model.funcionario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnderecoFuncionarioTest {

    @Test
    public void exibirDados(){
        // Given
        EnderecoFuncionario endereco = new EnderecoFuncionario("Rua A", "10", "Centro", "19000-000", "18999999999");

        // When
        String dados = endereco.exibirDados();

        // Then
        assertEquals("Logradouro: Rua A, Número: 10, Bairro: Centro, CEP: 19000-000, Telefone: 18999999999", dados);
    }

    @Test
    public void logradouroVazio(){
        // Given / When
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new EnderecoFuncionario("", "10", "Centro", "19000-000", "18999999999");
        });

        // Then
        assertEquals("O logradouro não pode ser vazio", e.getMessage());
    }

    @Test
    public void numeroNulo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new EnderecoFuncionario("Rua A", null, "Centro", "19000-000", "18999999999");
        });
    }

    @Test
    public void bairroEmBranco(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new EnderecoFuncionario("Rua A", "10", "   ", "19000-000", "18999999999");
        });
    }

    @Test
    public void cepVazio(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new EnderecoFuncionario("Rua A", "10", "Centro", "", "18999999999");
        });
    }

    @Test
    public void telefoneNulo(){
        // Given / When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            new EnderecoFuncionario("Rua A", "10", "Centro", "19000-000", null);
        });
    }
}
