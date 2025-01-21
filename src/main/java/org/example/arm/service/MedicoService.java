package org.example.arm.service;

import org.example.arm.medico.Especialidade;
import org.example.arm.medico.Medico;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MedicoService {

    List<Medico> medicoList = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public void salvarMedico() {
        System.out.println("Informe o nome do médico:");
        String nome =  scanner.nextLine();
        System.out.println("Informe o CRM: ");
        String crm = scanner.nextLine();
        String crmValido = validarCrm(crm);
        System.out.println("Informe  a especialidade");
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine());

        System.out.println("Informe a data de consulta do médico!");
        LocalDateTime consulta = LocalDateTime.parse(scanner.nextLine());
        System.out.println("Informe outro horário de disponibilidade para a semana");
        LocalDateTime disponibilidade = LocalDateTime.parse(scanner.nextLine());
        System.out.println("Informe o horário de descanso do médico!");
        LocalDateTime horarioDescanso = LocalDateTime.parse(scanner.nextLine());

        Medico medico = new Medico(nome, crmValido, especialidade, consulta, disponibilidade, horarioDescanso);
        try {
            salvarMedicoEmArquivo(medico);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void salvarMedicoEmArquivo(Medico medico) throws IOException {
        String caminho = "C:\\meuscode\\consultasLp2\\medicos.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho, true))) {
            try {
                bw.write(medico.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public Medico procurarMedico() {
        List<Medico> medicos = retornaMedico();
        if (medicos.isEmpty()) {
            return null;
        }
        System.out.println("Informe o nome do médico");
        String nome = scanner.nextLine();
        for (Medico medico : medicos) {
            if (medico.getNome().equalsIgnoreCase(nome)) {
                System.out.println("Médico encontrado:" + medico);
                return medico;
            }
        }
        return null;
    }


    private List<Medico> retornaMedico(){
        String caminho = "C:\\meuscode\\consultasLp2\\medicos.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminho));
            try {
                String linha = br.readLine();
                while (linha!=null){
                    linha = linha.replace("Medico{", "").replace("}", "");
                    String [] vetor = linha.split(",");
                    String nome = vetor[0].split("=")[1].replace("'", "");
                    String crm = vetor[1].split("=")[1].replace("'", "");
                    Especialidade especialidade = Especialidade.valueOf(vetor[2].split("=")[1].replace("'", ""));
                    LocalDateTime consulta  = LocalDateTime.parse(vetor[3].split("=")[1].replace("'", ""));
                    LocalDateTime disponibilidade = LocalDateTime.parse(vetor[4].split("=")[1].replace("'", ""));
                    LocalDateTime bloqueado = LocalDateTime.parse(vetor[5].split("=")[1].replace("'", ""));
                    LocalDateTime descanso = LocalDateTime.parse(vetor[6].split("=")[1].replace("'", ""));

                    Medico medico = new Medico(nome, crm, especialidade, consulta, disponibilidade, bloqueado, descanso);
                    medicoList.add(medico);
                    linha = br.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return medicoList;
    }


    private String validarCrm(String crm) {
        List<Medico> list = retornaMedico();
        String novo = "";

        for (Medico medico : list) {
            if (medico.getCrm().equals(crm) || crm.length() < 4) {
                System.out.println("CRM inválido! informe  corretamente");
                String validado = novoCrm(crm);
                novo = validarCrm(validado);
                return novo;
            }
        }
        return novo += crm;
    }

    private String novoCrm(String crm) {
        System.out.println("Informe o novo crm");
        String novoCrm = scanner.nextLine().trim();
        return novoCrm;
    }

    private void bloquearHorarioMedico(){
         Medico medico = procurarMedico();
         if(medico==null){
             throw new RuntimeException();
         }
        System.out.println("Informe o horario de bloqueio");
        LocalDateTime bloqueio = LocalDateTime.parse(scanner.nextLine());
        medico.setBloqueado(bloqueio);

    }


}
