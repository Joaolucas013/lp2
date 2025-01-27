package org.example.arm.service;

import org.example.arm.paciente.Paciente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PacienteService {

    List<Paciente> pacientes = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public Paciente  cadastrarPaciente() {
        System.out.println("Informe o  nome do paciente:");
        String nome = scanner.nextLine().trim();
        System.out.println("Informe a  idade:");
        int idade = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Informe o sexo:");
        String sexo = scanner.nextLine().trim();
        Paciente paciente = new Paciente(nome, sexo, idade);
        salvarPaciente(paciente);
        return paciente;
    }


    private void salvarPaciente(Paciente paciente) {
        // aqui é interessante que voce coloque o caminho certo do arquivo e coloque // em vez de / se nao o java entende como quebra de linha
        String path = "C:\\meuscode\\consultasLp2\\paciente.txt";

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
        String caminho = "C:\\meuscode\\teste\\pacientes.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = null;
            while ((linha=br.readLine()) != null) {
                linha = linha.replace("Paciente{", "")
                        .replace("}", "");

                String[] vetor = linha.split(", ");
                if (vetor.length < 3) {
                    System.out.println("Paciente não cadastrado. Cadastre-see!");
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
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage(), e);
        }
    }


    public Paciente procurarPaciente(String nome) {
        if (pacientes.isEmpty()) {
            recuperarPaciente();
            if (pacientes.isEmpty()) {
                return cadastrarPaciente();
            }
        }
        for (Paciente p : pacientes) {
            if (p.getNome().trim().equalsIgnoreCase(nome.trim())) {
                return p;
            }
        }
        System.out.println("Paciente não encontrado. cadastre-se");
        var p = cadastrarPaciente();
        return p;
    }

}
