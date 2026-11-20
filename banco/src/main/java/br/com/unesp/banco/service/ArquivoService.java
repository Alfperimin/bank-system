package br.com.unesp.banco.service;

import br.com.unesp.banco.model.conta.Conta;
import br.com.unesp.banco.model.funcionario.*;
import br.com.unesp.banco.model.pessoa.*;
import br.com.unesp.banco.model.transacao.TipoTransacao;
import br.com.unesp.banco.model.transacao.Transacao;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe para salvar e carregar os dados do sistema em arquivos de texto.
 * Cada tipo de dado tem o seu arquivo dentro da pasta configurada: funcionarios.txt,
 * clientes.txt, contas.txt e movimentacoes.txt. Cada linha é um registro, com os campos
 * separados por ponto e vírgula (os caracteres especiais dentro dos campos são escapados
 * com barra invertida). Quando um arquivo ainda não existe, ele é lido como vazio, o que
 * acontece na primeira execução do programa.
 */
public class ArquivoService {

    /**
     * Nome do arquivo dos funcionários.
     */
    public static final String ARQUIVO_FUNCIONARIOS = "funcionarios.txt";
    /**
     * Nome do arquivo dos clientes.
     */
    public static final String ARQUIVO_CLIENTES = "clientes.txt";
    /**
     * Nome do arquivo das contas.
     */
    public static final String ARQUIVO_CONTAS = "contas.txt";
    /**
     * Nome do arquivo das movimentações.
     */
    public static final String ARQUIVO_MOVIMENTACOES = "movimentacoes.txt";

    /**
     * Pasta onde os arquivos são salvos e lidos.
     */
    private String pasta;

    /**
     * Construtor padrão, que usa a pasta "dados" do diretório de execução.
     */
    public ArquivoService() {
        this("dados");
    }

    /**
     * Construtor do serviço de arquivos.
     * @param pasta pasta onde os arquivos serão salvos e lidos.
     */
    public ArquivoService(String pasta) {

        if(pasta == null || pasta.trim().isEmpty()){
            throw new IllegalArgumentException("A pasta não pode ser vazia");
        }else{
            this.pasta = pasta;
        }
    }

    /**
     * Método para retornar a pasta dos arquivos.
     * @return a pasta configurada.
     */
    public String getPasta() {
        return this.pasta;
    }

    /**
     * Método para salvar todos os funcionários no arquivo funcionarios.txt.
     * @param funcionarios lista de funcionários a ser salva.
     */
    public void salvarFuncionarios(List<Funcionario> funcionarios){
        List<String> linhas = new ArrayList<>();

        for(Funcionario f : funcionarios){
            List<String> campos = new ArrayList<>();

            if(f instanceof Gerente){
                campos.add("GERENTE");
            }else if(f instanceof TecnicoBancario){
                campos.add("TECNICO");
            }else if(f instanceof Diretor){
                campos.add("DIRETOR");
            }else{
                throw new IllegalArgumentException("Cargo de funcionário desconhecido: " + f.getCargo());
            }

            campos.add(f.getNome());
            campos.add(f.getCpf());
            campos.add(f.getEndereco().getLogradouro());
            campos.add(f.getEndereco().getNumero());
            campos.add(f.getEndereco().getBairro());
            campos.add(f.getEndereco().getCep());
            campos.add(f.getEndereco().getTelefone());
            campos.add(f.getSalarioBase().toPlainString());

            if(f instanceof Gerente){
                campos.add(((Gerente) f).getBonus().toPlainString());
                campos.add(((Gerente) f).getAgenciaResponsavel());
            }else if(f instanceof TecnicoBancario){
                campos.add(((TecnicoBancario) f).getSetorAtendimento());
                campos.add(String.valueOf(((TecnicoBancario) f).getHorasExtras()));
            }else{
                campos.add(((Diretor) f).getParticipacaoLucros().toPlainString());
                campos.add(((Diretor) f).getAreaResponsabilidade());
            }

            linhas.add(this.juntar(campos));
        }

        this.escrever(ARQUIVO_FUNCIONARIOS, linhas);
    }

    /**
     * Método para carregar os funcionários do arquivo funcionarios.txt.
     * Os objetos são criados pelas fábricas, então passam pelas mesmas validações do cadastro.
     * @return a lista de funcionários carregados (vazia se o arquivo não existir).
     */
    public List<Funcionario> carregarFuncionarios(){
        List<Funcionario> funcionarios = new ArrayList<>();
        List<String> linhas = this.ler(ARQUIVO_FUNCIONARIOS);

        for(int i = 0; i < linhas.size(); i++){
            try{
                List<String> c = this.dividir(linhas.get(i));
                this.exigirCampos(c, 11);

                EnderecoFuncionario endereco = new EnderecoFuncionario(c.get(3), c.get(4), c.get(5), c.get(6), c.get(7));
                BigDecimal salarioBase = new BigDecimal(c.get(8));
                FuncionarioFactory fabrica;

                if(c.get(0).equals("GERENTE")){
                    fabrica = new GerenteFactory(new BigDecimal(c.get(9)), c.get(10));
                }else if(c.get(0).equals("TECNICO")){
                    fabrica = new TecnicoBancarioFactory(c.get(9), Integer.parseInt(c.get(10)));
                }else if(c.get(0).equals("DIRETOR")){
                    fabrica = new DiretorFactory(new BigDecimal(c.get(9)), c.get(10));
                }else{
                    throw new IllegalArgumentException("Tipo desconhecido: " + c.get(0));
                }

                funcionarios.add(fabrica.criarFuncionario(c.get(1), c.get(2), endereco, salarioBase));
            }catch(RuntimeException e){
                throw this.linhaInvalida(ARQUIVO_FUNCIONARIOS, i + 1, e);
            }
        }

        return funcionarios;
    }

    /**
     * Método para salvar todos os clientes no arquivo clientes.txt.
     * @param clientes lista de clientes (pessoas físicas e jurídicas) a ser salva.
     */
    public void salvarClientes(List<Cliente> clientes){
        List<String> linhas = new ArrayList<>();

        for(Cliente c : clientes){
            List<String> campos = new ArrayList<>();

            if(c instanceof PessoaFisica){
                campos.add("PF");
            }else if(c instanceof PessoaJuridica){
                campos.add("PJ");
            }else{
                throw new IllegalArgumentException("Tipo de cliente desconhecido: " + c.getTipoDocumento());
            }

            campos.add(c.getNome());
            campos.add(c.getDocumento());
            campos.add(c.getTelefone());
            campos.add(c.getEmail());
            campos.add(c.getEndereco().getRua());
            campos.add(c.getEndereco().getNumero());
            campos.add(c.getEndereco().getBairro());
            campos.add(c.getEndereco().getCidade());
            campos.add(c.getEndereco().getEstado());
            campos.add(c.getEndereco().getCep());

            linhas.add(this.juntar(campos));
        }

        this.escrever(ARQUIVO_CLIENTES, linhas);
    }

    /**
     * Método para carregar os clientes do arquivo clientes.txt.
     * Os objetos são criados pelas fábricas, então passam pelas mesmas validações do cadastro.
     * @return a lista de clientes carregados (vazia se o arquivo não existir).
     */
    public List<Cliente> carregarClientes(){
        List<Cliente> clientes = new ArrayList<>();
        List<String> linhas = this.ler(ARQUIVO_CLIENTES);

        for(int i = 0; i < linhas.size(); i++){
            try{
                List<String> c = this.dividir(linhas.get(i));
                this.exigirCampos(c, 11);

                Endereco endereco = new Endereco(c.get(5), c.get(6), c.get(7), c.get(8), c.get(9), c.get(10));
                ClienteFactory fabrica;

                if(c.get(0).equals("PF")){
                    fabrica = new PessoaFisicaFactory();
                }else if(c.get(0).equals("PJ")){
                    fabrica = new PessoaJuridicaFactory();
                }else{
                    throw new IllegalArgumentException("Tipo desconhecido: " + c.get(0));
                }

                clientes.add(fabrica.criarCliente(c.get(1), c.get(2), c.get(3), c.get(4), endereco));
            }catch(RuntimeException e){
                throw this.linhaInvalida(ARQUIVO_CLIENTES, i + 1, e);
            }
        }

        return clientes;
    }

    /**
     * Método para salvar as contas (número, titular e saldo) no arquivo contas.txt.
     * O histórico de cada conta é salvo à parte, em salvarMovimentacoes().
     * @param contas lista de contas a ser salva.
     */
    public void salvarContas(List<Conta> contas){
        List<String> linhas = new ArrayList<>();

        for(Conta conta : contas){
            List<String> campos = new ArrayList<>();

            campos.add(String.valueOf(conta.getNumero()));
            campos.add(conta.getTitular().getDocumento());
            campos.add(conta.getSaldo().toPlainString());

            linhas.add(this.juntar(campos));
        }

        this.escrever(ARQUIVO_CONTAS, linhas);
    }

    /**
     * Método para carregar as contas do arquivo contas.txt.
     * @param clientes clientes já carregados, usados para encontrar o titular de cada conta.
     * @param movimentacoes movimentações já carregadas, por número da conta.
     * @return a lista de contas carregadas, com saldo e histórico (vazia se o arquivo não existir).
     */
    public List<Conta> carregarContas(List<Cliente> clientes, Map<Integer, List<Transacao>> movimentacoes){
        List<Conta> contas = new ArrayList<>();
        List<String> linhas = this.ler(ARQUIVO_CONTAS);

        for(int i = 0; i < linhas.size(); i++){
            try{
                List<String> c = this.dividir(linhas.get(i));
                this.exigirCampos(c, 3);

                int numero = Integer.parseInt(c.get(0));
                Cliente titular = null;

                for(Cliente cliente : clientes){
                    if(cliente.getDocumento().equals(c.get(1))){
                        titular = cliente;
                    }
                }

                if(titular == null){
                    throw new IllegalArgumentException("Titular não encontrado: " + c.get(1));
                }

                contas.add(new Conta(numero, titular, new BigDecimal(c.get(2)), movimentacoes.get(numero)));
            }catch(RuntimeException e){
                throw this.linhaInvalida(ARQUIVO_CONTAS, i + 1, e);
            }
        }

        return contas;
    }

    /**
     * Método para salvar as movimentações de todas as contas no arquivo movimentacoes.txt.
     * Cada linha guarda o número da conta, a data e hora, o tipo e o valor.
     * @param contas lista de contas cujos históricos serão salvos.
     */
    public void salvarMovimentacoes(List<Conta> contas){
        List<String> linhas = new ArrayList<>();

        for(Conta conta : contas){
            for(Transacao t : conta.getTransacoes()){
                List<String> campos = new ArrayList<>();

                campos.add(String.valueOf(conta.getNumero()));
                campos.add(t.getDataHora().toString());
                campos.add(t.getTipo().name());
                campos.add(t.getValor().toPlainString());

                linhas.add(this.juntar(campos));
            }
        }

        this.escrever(ARQUIVO_MOVIMENTACOES, linhas);
    }

    /**
     * Método para carregar as movimentações do arquivo movimentacoes.txt.
     * @return as movimentações agrupadas por número da conta, na ordem do arquivo (vazio se o arquivo não existir).
     */
    public Map<Integer, List<Transacao>> carregarMovimentacoes(){
        Map<Integer, List<Transacao>> movimentacoes = new LinkedHashMap<>();
        List<String> linhas = this.ler(ARQUIVO_MOVIMENTACOES);

        for(int i = 0; i < linhas.size(); i++){
            try{
                List<String> c = this.dividir(linhas.get(i));
                this.exigirCampos(c, 4);

                int numero = Integer.parseInt(c.get(0));
                Transacao transacao = new Transacao(LocalDateTime.parse(c.get(1)),
                    TipoTransacao.valueOf(c.get(2)), new BigDecimal(c.get(3)));

                if(!movimentacoes.containsKey(numero)){
                    movimentacoes.put(numero, new ArrayList<>());
                }

                movimentacoes.get(numero).add(transacao);
            }catch(RuntimeException e){
                throw this.linhaInvalida(ARQUIVO_MOVIMENTACOES, i + 1, e);
            }
        }

        return movimentacoes;
    }

    /**
     * Método para salvar todos os dados do sistema (funcionários, clientes, contas e movimentações).
     * @param banco serviço com os clientes e as contas.
     * @param funcionarios serviço com os funcionários.
     */
    public void salvarDados(BancoService banco, FuncionarioService funcionarios){
        this.salvarFuncionarios(funcionarios.listarFuncionarios());
        this.salvarClientes(banco.listarClientes());
        this.salvarContas(banco.listarContas());
        this.salvarMovimentacoes(banco.listarContas());
    }

    /**
     * Método para carregar todos os dados salvos e restaurá-los nos serviços.
     * Deve ser chamado ao iniciar o programa.
     * @param banco serviço que receberá os clientes e as contas.
     * @param funcionarios serviço que receberá os funcionários.
     */
    public void carregarDados(BancoService banco, FuncionarioService funcionarios){
        List<Cliente> clientes = this.carregarClientes();
        List<Conta> contas = this.carregarContas(clientes, this.carregarMovimentacoes());

        banco.restaurar(clientes, contas);
        funcionarios.restaurar(this.carregarFuncionarios());
    }

    /**
     * Método interno para escrever as linhas em um arquivo da pasta, criando a pasta se preciso.
     * @param nome nome do arquivo.
     * @param linhas linhas a serem escritas.
     */
    private void escrever(String nome, List<String> linhas){
        try{
            Files.createDirectories(Path.of(this.pasta));
            Files.write(Path.of(this.pasta, nome), linhas, StandardCharsets.UTF_8);
        }catch(IOException e){
            throw new IllegalStateException("Erro ao salvar o arquivo " + nome, e);
        }
    }

    /**
     * Método interno para ler as linhas de um arquivo da pasta.
     * @param nome nome do arquivo.
     * @return as linhas não vazias do arquivo, ou uma lista vazia se ele não existir.
     */
    private List<String> ler(String nome){
        Path caminho = Path.of(this.pasta, nome);
        List<String> linhas = new ArrayList<>();

        if(!Files.exists(caminho)){
            return linhas;
        }

        try{
            for(String linha : Files.readAllLines(caminho, StandardCharsets.UTF_8)){
                if(!linha.trim().isEmpty()){
                    linhas.add(linha);
                }
            }
        }catch(IOException e){
            throw new IllegalStateException("Erro ao ler o arquivo " + nome, e);
        }

        return linhas;
    }

    /**
     * Método interno para juntar os campos em uma linha, escapando os caracteres especiais.
     * @param campos campos do registro.
     * @return a linha com os campos separados por ponto e vírgula.
     */
    private String juntar(List<String> campos){
        List<String> escapados = new ArrayList<>();

        for(String campo : campos){
            escapados.add(campo.replace("\\", "\\\\").replace(";", "\\;")
                .replace("\n", "\\n").replace("\r", "\\r"));
        }

        return String.join(";", escapados);
    }

    /**
     * Método interno para dividir uma linha nos seus campos, desfazendo o escape.
     * @param linha linha lida do arquivo.
     * @return os campos do registro.
     */
    private List<String> dividir(String linha){
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();

        for(int i = 0; i < linha.length(); i++){
            char c = linha.charAt(i);

            if(c == '\\' && i + 1 < linha.length()){
                i++;
                char proximo = linha.charAt(i);

                if(proximo == 'n'){
                    atual.append('\n');
                }else if(proximo == 'r'){
                    atual.append('\r');
                }else{
                    atual.append(proximo);
                }
            }else if(c == ';'){
                campos.add(atual.toString());
                atual = new StringBuilder();
            }else{
                atual.append(c);
            }
        }

        campos.add(atual.toString());

        return campos;
    }

    /**
     * Método interno para garantir que a linha tem a quantidade esperada de campos.
     * @param campos campos da linha.
     * @param quantidade quantidade esperada.
     */
    private void exigirCampos(List<String> campos, int quantidade){
        if(campos.size() != quantidade){
            throw new IllegalArgumentException("Esperados " + quantidade + " campos, encontrados " + campos.size());
        }
    }

    /**
     * Método interno para montar o erro de uma linha inválida, dizendo o arquivo e a linha.
     * @param arquivo nome do arquivo.
     * @param numeroLinha número da linha com problema (começando em 1).
     * @param causa erro original.
     * @return a exceção a ser lançada.
     */
    private IllegalArgumentException linhaInvalida(String arquivo, int numeroLinha, RuntimeException causa){
        return new IllegalArgumentException("Linha " + numeroLinha + " inválida em " + arquivo + ": " + causa.getMessage(), causa);
    }
}
