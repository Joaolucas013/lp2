package org.example.arm.service;

import org.example.arm.paciente.Paciente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PacienteService {

    List<Paciente> pacientes = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public void cadastrarPaciente() {
        System.out.println("Informe o  nome do paciente:");
        String nome = scanner.nextLine().trim();
        System.out.println("Informe a  idade:");
        int idade = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Informe o sexo:");
        String sexo = scanner.nextLine().trim();
        Paciente paciente = new Paciente(nome, sexo, idade);
        salvarPaciente(paciente);
    }


    private void salvarPaciente(Paciente paciente) {
        // aqui é interessante que voce coloque o caminho certo do arquivo e coloque // em vez de / se nao o java entende como quebra de linha

        String path = "C:\\meuscode\\leituraEscrita\\paciente.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(paciente.toString());
            bw.newLine();
         //   bw.close();
            System.out.println("Paciente salvo com sucesso: " + paciente);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar o paciente no arquivo", e);
        }

    }


    private List<Paciente> recuperarPaciente() {
        BufferedReader br = null;
        String path = "C:\\meuscode\\leituraEscrita\\paciente.txt";
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String linha = null;
        try {
            linha = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(linha!=null){

            linha = linha.replace("Paciente{", "").replace("}", ""); // troca Paciente por uma Strin vazia
            String [] vetor = linha.split(","); // armazena o dado ate a virgula
            String nome = vetor[0].split("=")[1].replace("'", "");  // pega o dado da linha 0 coluna 1 ate o = e troca a aspa simples por uma String vazia!!!!
            String sexo = vetor[1].split("=")[1].replace("'", "");
            Integer idade = Integer.valueOf(vetor[2].split("=")[1].replace("'", ""));
            Paciente paciente = new Paciente(nome, sexo, idade);
            pacientes.add(paciente);
            try {
                linha = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
        return pacientes;
    }

}
