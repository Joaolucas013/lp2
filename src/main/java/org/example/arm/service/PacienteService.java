package org.example.arm.service;

import org.example.arm.paciente.Paciente;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PacienteService {

    //EDITE ESSA PARTE COLOCANDO O CAMINHO DO SEU ARQUIVO paciente.txt -----------------------------------------------
    String path = "C:\\Users\\Lailly\\OneDrive\\Documentos\\Faculdade\\Linguagem de Programação II\\Trabalho_Agenda\\lp2\\paciente.txt";

    Set<Paciente> pacientes = new HashSet<>();
    Scanner scanner = new Scanner(System.in);
    String sexo;

    public Paciente cadastrarPaciente() {
        String nome = null;
        int idade = 0;
        String sexo = null;
        boolean valido = false;

        while (!valido) {
            System.out.println("Informe o nome do paciente:");
            nome = scanner.nextLine().trim();
            if (nome.isEmpty() || nome.matches(".*\\d.*")) {
                System.out.println("Nome não pode ser vazio ou conter números. Tente novamente.");
                continue;
            }

            System.out.println("Informe a idade:");
            try {
                idade = Integer.parseInt(scanner.nextLine().trim());
                if (idade <= 0) {
                    System.out.println("Idade deve ser um número positivo. Tente novamente.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Idade inválida. Tente novamente.");
                continue;
            }

            System.out.println("Informe o sexo (M/F):");
            sexo = scanner.nextLine().trim().toUpperCase();
            if (!sexo.equals("M") && !sexo.equals("F")) {
                System.out.println("Sexo inválido. Deve ser 'M' ou 'F'. Tente novamente.");
                continue;
            }

            valido = true;
        }

        Paciente paciente = new Paciente(nome, sexo, idade);
        salvarPaciente(paciente);
        return paciente;
    }

    private void salvarPaciente(Paciente paciente) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(paciente.toString());
            bw.newLine();
            System.out.println("Paciente salvo com sucesso: " + paciente);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar o paciente no arquivo", e);
        }

    }

    public void recuperarPaciente() {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

                String linha = null;
                while ((linha = br.readLine()) != null) {
                    linha = linha.replace("Paciente{", "")
                            .replace("}", "");

                    String[] vetor = linha.split(", ");

                    if (vetor.length < 3) {
                    System.out.println("Não há nenhum paciente cadastrado, cadastre-se!");
                    cadastrarPaciente();
                    continue;
                }

                    String nome = vetor[0].split("=")[1].replace("'", "");
                    String sexo = vetor[1].split("=")[1].replace("'", "");
                    Integer idade = Integer.valueOf(vetor[2].split("=")[1].replace("'", ""));

                    Paciente paciente = new Paciente(nome, sexo, idade);
                    pacientes.add(paciente);

                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }


    public Paciente procurarPaciente(String nome) {
        recuperarPaciente();
        pacientes.stream().collect(Collectors.toSet());

        for (Paciente p : pacientes) {
            if (p.getNome().trim().equalsIgnoreCase(nome.trim())) {
                Paciente pc = new Paciente(p.getNome(), p.getSexo(), p.getIdade());
                return pc;
            }
        }
        
        pacientes.stream().forEach(System.out::println);
        System.out.println("Paciente não encontrado. CADASTRE-SE: ");
        var p = cadastrarPaciente();
        return p;
    }
}
