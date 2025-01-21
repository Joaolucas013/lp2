package org.example.arm.service;

import org.example.arm.medico.Especialidade;
import org.example.arm.medico.Medico;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class MedicoService {

    Set<Medico> medicoList = new HashSet<>();
    List<Medico> medicoImutaveis = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public void salvarMedico() {
        System.out.println("Informe o nome do médico:");
        String nome = scanner.nextLine();
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


    private Set<Medico> recuperarMedico() {
        String caminho = "C:\\meuscode\\consultasLp2\\medicos.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminho));
            try {
                String linha = br.readLine();
                while (linha != null) {
                    linha = linha.replace("Medico{", "").replace("}", "");
                    String[] vetor = linha.split(",");
                    String nome = vetor[0].split("=")[1].replace("'", "");
                    String crm = vetor[1].split("=")[1].replace("'", "");
                    Especialidade especialidade = Especialidade.valueOf(vetor[2].split("=")[1].replace("'", ""));
                    LocalDateTime consulta = LocalDateTime.parse(vetor[3].split("=")[1].replace("'", ""));
                    LocalDateTime disponibilidade = LocalDateTime.parse(vetor[4].split("=")[1].replace("'", ""));
                    LocalDateTime bloqueado = LocalDateTime.parse(vetor[5].split("=")[1].replace("'", ""));
                    LocalDateTime descanso = LocalDateTime.parse(vetor[6].split("=")[1].replace("'", ""));

                    Medico medico = new Medico(nome, crm, especialidade, consulta, disponibilidade, bloqueado, descanso);
                    medicoList.add(medico);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return medicoList;
    }

    public void bloquearHorario() {
        System.out.println("Informe o nome do médico!");
        String nome = scanner.nextLine().trim();
        var medico = procurarMedico(nome);
        if (medico == null) {
            throw new RuntimeException();
        }
        System.out.println("Informe o horário de bloqueio para consulta");
        LocalDateTime horarioBloqueio = LocalDateTime.parse(scanner.nextLine());
        medico.setBloqueado(horarioBloqueio);

    }


    private Medico procurarMedico(String nome) {
        List<Medico> medicos = recuperarMedico().stream().toList();
        for (Medico medico : medicos) {
            if (medico.getNome().equalsIgnoreCase(nome)) {
                return medico;
            }
        }
        System.out.println("Médico não encontrado!");
        return null;
    }


    public void iniciarMedicos() {
        Medico med1 = new Medico("Joao", "1234", Especialidade.CARDIOLOGIA,
                LocalDateTime.parse("2025-02-16T09:00"),
                LocalDateTime.parse("2025-02-16T16:40"),
                LocalDateTime.parse("2025-02-16T11:00"),
                LocalDateTime.parse("2025-02-28T16:00")

        );
        Medico med2 = new Medico(
                "Luiz Gabriel",
                "1122",
                Especialidade.ORTOPEDIA,
                LocalDateTime.parse("2025-02-18T11:00"),
                LocalDateTime.parse("2025-02-18T15:00"),
                LocalDateTime.parse("2025-02-18T13:00:00"),
                LocalDateTime.parse("2025-02-17T09:00")
        );

        Medico med3 = new Medico(
                "Gabriel Silvestre",
                "3344",
                Especialidade.NEUROLOGIA,
                LocalDateTime.parse("2025-02-19T07:00"),
                LocalDateTime.parse("2025-02-19T11:00"),
                LocalDateTime.parse("2025-02-19T09:00"),
                LocalDateTime.parse("2025-02-23T16:00")
        );
        Medico med4 = new Medico(
                "Láiza Kevelly",
                "5678",
                Especialidade.DERMATOLOGIA,
                LocalDateTime.parse("2025-02-16T09:00"),
                LocalDateTime.parse("2025-02-16T13:00"),
                LocalDateTime.parse("2025-02-16T11:00"),
                LocalDateTime.parse("2025-02-17T15:00")
        );

        Medico med5 = new Medico(
                "Gabriel Barbosa",
                "5678",
                Especialidade.DERMATOLOGIA,
                LocalDateTime.parse("2025-02-17T09:00"),
                LocalDateTime.parse("2025-02-17T13:00"),
                LocalDateTime.parse("2025-02-17T1:00"),
                LocalDateTime.parse("2025-02-17T16:00")
        );
        medicoImutaveis.add(med1);
        medicoImutaveis.add(med2);
        medicoImutaveis.add(med3);
        medicoImutaveis.add(med4);
        medicoImutaveis.add(med5);
        salvarMedicoImutaveis();
    }

    private void salvarMedicoImutaveis() {
        String caminho = "C:\\meuscode\\consultasLp2\\medicos.txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(caminho));
            for (Medico medico : medicoImutaveis) {
                bw.write(bw.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("medico salvo com sucesso!");
    }

    private String validarCrm(String crm) {
        List<Medico> list = medicoList.stream().toList();
        String novo = "";

        for (int i = 0; i < list.size(); i++) {
            Medico m = list.get(i);

            if (m.getCrm().equals(crm) || crm.length() < 4) {
                System.out.println("CRM inválido! informe novamente corretamente");
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

}