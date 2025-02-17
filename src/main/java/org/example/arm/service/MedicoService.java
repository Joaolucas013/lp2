package org.example.arm.service;

import org.example.arm.exception.ConsultaException;
import org.example.arm.medico.Especialidade;
import org.example.arm.medico.Medico;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class MedicoService {
    String caminho = "medicos.txt";
    
    public static List<Medico> medicoList = new ArrayList<>();
    public static List<Medico> medicoImutaveis = new ArrayList<>();

    public  Scanner scanner = new Scanner(System.in);

    public void cadastrarMedico() {
        String nome = null;
        String crm = null;
        Especialidade especialidade = null;
        List<LocalDateTime> hrsDisponiveis = new ArrayList<>();
        List<LocalDateTime> hrsConsultas = new ArrayList<>();
        List<LocalDateTime> descansoSemanal = new ArrayList<>();
        List<LocalDateTime> horariosbloqueados = new ArrayList<>();
        boolean valido = false;

        while (!valido) {
            System.out.println("Informe o nome do médico:");
            nome = scanner.nextLine().trim();
            if (nome.isEmpty() || nome.matches(".*\\d.*")) {
                System.out.println("Nome não pode ser vazio ou conter números. Tente novamente.");
                continue;
            }

            System.out.println("Informe o CRM (números):");
            crm = scanner.nextLine().trim();
            if (crm.isEmpty() || crm.length() < 6 || crm.matches(".*[a-zA-Z].*")) {
                System.out.println("CRM inválido. Deve conter 6 números. Tente novamente.");
                continue;
            }
            crm = validarCrm(crm);

            System.out.println("Informe a especialidade:");
            try {
                especialidade = Especialidade.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Especialidade inválida. Tente novamente.");
                continue;
            }

            System.out.println("Informe o(s) horários disponíveis para a semana (formato: yyyy-MM-ddTHH:mm):");
            while (true) {
                try {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;
                    LocalDateTime horario = LocalDateTime.parse(input);
                    hrsDisponiveis.add(horario);
                } catch (Exception e) {
                    System.out.println("Formato de data/hora inválido. Tente novamente.");
                    continue;
                }
            }

            System.out.println("Informe o(s) horários de consultas do médico (formato: yyyy-MM-ddTHH:mm):");
            while (true) {
                try {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;
                    LocalDateTime horario = LocalDateTime.parse(input);
                    hrsConsultas.add(horario);
                } catch (Exception e) {
                    System.out.println("Formato de data/hora inválido. Tente novamente.");
                    continue;
                }
            }

            System.out.println("Informe o(s) horários de descanso para a semana (formato: yyyy-MM-ddTHH:mm):");
            while (true) {
                try {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;
                    LocalDateTime horario = LocalDateTime.parse(input);
                    descansoSemanal.add(horario);
                } catch (Exception e) {
                    System.out.println("Formato de data/hora inválido. Tente novamente.");
                    continue;
                }
            }

            System.out.println("Informe o(s) horários bloqueados para consulta na semana (formato: yyyy-MM-ddTHH:mm):");
            while (true) {
                try {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;
                    LocalDateTime horario = LocalDateTime.parse(input);
                    horariosbloqueados.add(horario);
                } catch (Exception e) {
                    System.out.println("Formato de data/hora inválido. Tente novamente.");
                    continue;
                }
            }

            valido = true;
        }

        Medico medico = new Medico(nome, crm, especialidade, hrsDisponiveis, hrsConsultas, descansoSemanal, horariosbloqueados);
        medicoList.add(medico);
        System.out.println("Médico salvo com sucesso! " + medico);
        try {
            salvarMedicoEmArquivo(medico);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void salvarMedicoEmArquivo(Medico medico) throws IOException {
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

    private void salvarTodosMedicos() throws IOException {
        String caminho = "medicos.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
            try {
                for (Medico medico : medicoList) {
                    bw.write(medico.toString());
                    bw.newLine();
                }
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

    public List<Medico> recuperarMedico() {
        String caminho = "medicos.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine();

            while (linha != null) {
                linha = linha.replace("Medico{", "").replace("}", "");
                String[] vetor = linha.split(",\\s*(?![^\\[]*\\])");

                String nome = vetor[0].split("=")[1].replace("'", "");
                String crm = vetor[1].split("=")[1].replace("'", "");
                Especialidade especialidade = Especialidade.valueOf(vetor[2].split("=")[1].replace("'", ""));

                String consultas = vetor[3].split("=")[1];
                List<LocalDateTime> consultashrs = listaHorarios(consultas);


                String horariosDisponiveisStr = vetor[4].split("=")[1];
                List<LocalDateTime> horariosDisponiveis = listaHorarios(horariosDisponiveisStr);


                String horariosDescansoStr = vetor[5].split("=")[1];
                List<LocalDateTime> horariosDescanso = listaHorarios(horariosDescansoStr);


                String horarioBlock = vetor[6].split("=")[1];
                List<LocalDateTime> horarioBloqueado = listaHorarios(horarioBlock);

                Medico medico = new Medico(nome, crm, especialidade,consultashrs,horariosDisponiveis, horariosDescanso, horarioBloqueado);
                medicoList.add(medico);

                if (medicoImutaveis.size() < 5) {
                    medicoImutaveis.add(medico);
                }
                linha = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return medicoList;
    }




    private List<LocalDateTime> listaHorarios(String list) {
        list = list.replace("[", "").replace("]", "");
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        String[] horarios = list.split(",\\s*");
        List<LocalDateTime> listaHorarios = new ArrayList<>();
        for (String horario : horarios) {
            listaHorarios.add(LocalDateTime.parse(horario));
        }
        return listaHorarios;
    }


    public void bloquearHorario() {
        if (medicoList.isEmpty()) {
            recuperarMedico();
        }

        if(medicoList.isEmpty()){
            throw new ConsultaException("Não há medicos disponíveis para bloqueio de horário!");
        }
        medicoList.stream().forEach(System.out::println);
        System.out.println("Informe o nome do médico!");
        String nome = scanner.nextLine();
        var medico = procurarMedico(nome);

        System.out.println("Informe o dia  do bloqueio da consulta");
        LocalDateTime horarioBloqueio = LocalDateTime.parse(scanner.nextLine());
        List<LocalDateTime> todosHorariosBlock = medico.getHorarioBloqueado();
        todosHorariosBlock.add(horarioBloqueio);


        salvarAlteracoes();
    }


    public void salvarAlteracoes() {
        String caminho = "medicos.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
            try {
                for (Medico medico : medicoList) {
                    bw.write(medico.toString());
                    bw.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public  Medico procurarMedicoEspecialidade(Especialidade especialidade) {
        List<Medico> listMedicoEspecialidade = new ArrayList<>();
        Random r = new Random();

        if (medicoList.isEmpty()) {
            recuperarMedico();
        }
        for (Medico medico : medicoList) {
            if (medico.getEspecialidade().equals(especialidade)) {
                Medico medico1 = new Medico(medico.getNome(), medico.getCrm(), medico.getEspecialidade(),
                        medico.getHorariosDisponiveis(), medico.getHorariosConsultas(), medico.getHorariosDescanso(), medico.getHorarioBloqueado());
                listMedicoEspecialidade.add(medico1);
            }
        }
        if(listMedicoEspecialidade.isEmpty()){
            throw new ConsultaException("Nenhum médico encontrado para especialidade: " + especialidade);
        }

        Medico m = listMedicoEspecialidade.get(r.nextInt(listMedicoEspecialidade.size()));
        return m;
    }


    public Medico procurarMedico(String name) {
        if (medicoList.isEmpty()) {
            recuperarMedico();
        }
        for (Medico medico : medicoList) {
            if (medico.getNome().trim().equalsIgnoreCase(name)) {
                return medico;
            }
        }
        System.out.println("Médico não encontrado!");
        return null;
    }



    private void salvarMedicoImutaveis() {
        String caminho = "medicos.txt";
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
                for (Medico medico : medicoImutaveis) {
                    bw.write(medico.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String validarCrm(String crm) {
        if (medicoList.isEmpty()) {
            recuperarMedico();
        }
        String novo = "";
        for (int i = 0; i < medicoList.size(); i++) {
            Medico m = medicoList.get(i);
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

    public void retornarMedicoImutaveis() {
        iniciarMedicos();
        if(medicoList.isEmpty()){
            recuperarMedico();
        }
        medicoImutaveis.stream().forEach(System.out::println);
    }


    public void retornarHashEspecialidade() {
        Set<Especialidade> listEspecialidade = new HashSet<>();
        if(medicoList.isEmpty()){
            recuperarMedico();
        }
        for (Medico medico : medicoList) {
            listEspecialidade.add(medico.getEspecialidade());
        }
        if (listEspecialidade.isEmpty()) {
            throw new ConsultaException("Não há nehhuma especialidade disponível no momento!");
        }
        listEspecialidade.stream().forEach(System.out::println);
        System.out.println("Escolha a especialidade");

    }

    public List<Medico> retornarLista(){
        if(medicoImutaveis.isEmpty()){
            iniciarMedicos();
        }
        medicoList.stream().forEach(System.out::println);
        return medicoList;
    }

    public void iniciarMedicos() {
        Medico med1 = new Medico(
                "Joao Lucas",
                "1234",
                Especialidade.CARDIOLOGIA,
                Arrays.asList(              // consulta
                        LocalDateTime.parse("2025-02-23T07:00"),
                        LocalDateTime.parse("2025-02-24T08:00"),
                        LocalDateTime.parse("2025-02-25T09:00"),
                        LocalDateTime.parse("2025-02-26T10:00"),
                        LocalDateTime.parse("2025-02-27T11:30")

                ),
                Arrays.asList(    // horario disponivel
                        LocalDateTime.parse("2025-03-11T08:30"),
                        LocalDateTime.parse("2025-02-18T14:30"),
                        LocalDateTime.parse("2025-02-19T15:00"),
                        LocalDateTime.parse("2025-03-20T15:30"),
                        LocalDateTime.parse("2025-02-21T16:00")

                ),
                Arrays.asList(  // descanso
                        LocalDateTime.parse("2025-02-11T12:00"),
                        LocalDateTime.parse("2025-02-18T12:00"),
                        LocalDateTime.parse("2025-02-19T12:00"),
                        LocalDateTime.parse("2025-02-20T12:00"),
                        LocalDateTime.parse("2025-02-21T12:00")
                ),
                Arrays.asList(   // bloqueado
                        LocalDateTime.parse("2025-02-11T18:00"),
                        LocalDateTime.parse("2025-02-18T18:00"),
                        LocalDateTime.parse("2025-02-19T18:00"),
                        LocalDateTime.parse("2025-02-20T18:00"),
                        LocalDateTime.parse("2025-02-21T18:00")
                )
        );

        Medico med2 = new Medico(
                "Luiz Gabriel",
                "1122",
                Especialidade.ORTOPEDIA,
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T13:30"),
                        LocalDateTime.parse("2025-02-18T14:00"),
                        LocalDateTime.parse("2025-02-19T14:30"),
                        LocalDateTime.parse("2025-02-20T15:00"),
                        LocalDateTime.parse("2025-02-21T15:30"),
                        LocalDateTime.parse("2025-02-22T16:00")

                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-23T08:00"),
                        LocalDateTime.parse("2025-02-24T08:30"),
                        LocalDateTime.parse("2025-02-25T09:00"),
                        LocalDateTime.parse("2025-02-26T09:30"),
                        LocalDateTime.parse("2025-02-27T10:30")

                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-19T12:00"),
                        LocalDateTime.parse("2025-02-20T12:00"),
                        LocalDateTime.parse("2025-02-21T12:00"),
                        LocalDateTime.parse("2025-02-22T12:00"),
                        LocalDateTime.parse("2025-02-24T12:00"),
                        LocalDateTime.parse("2025-03-26T12:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-19T18:00"),
                        LocalDateTime.parse("2025-02-20T18:00"),
                        LocalDateTime.parse("2025-02-21T18:00"),
                        LocalDateTime.parse("2025-02-22T18:00"),
                        LocalDateTime.parse("2025-02-24T18:00"),
                        LocalDateTime.parse("2025-02-26T18:00")
                )
        );

        Medico med3 = new Medico(
                "Gabriel Silvestre",
                "3344",
                Especialidade.NEUROLOGIA,
                Arrays.asList(
                        LocalDateTime.parse("2025-02-19T16:00"),
                        LocalDateTime.parse("2025-02-20T13:30"),
                        LocalDateTime.parse("2025-02-21T14:00"),
                        LocalDateTime.parse("2025-02-22T14:30"),
                        LocalDateTime.parse("2025-02-24T15:00")

                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-23T08:00"),
                        LocalDateTime.parse("2025-02-24T09:30"),
                        LocalDateTime.parse("2025-02-25T10:00"),
                        LocalDateTime.parse("2025-02-26T10:30"),
                        LocalDateTime.parse("2025-02-27T11:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-19T12:00"),
                        LocalDateTime.parse("2025-02-20T12:00"),
                        LocalDateTime.parse("2025-02-21T12:00"),
                        LocalDateTime.parse("2025-02-24T12:00"),
                        LocalDateTime.parse("2025-02-25T12:00"),
                        LocalDateTime.parse("2025-03-26T12:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-19T18:00"),
                        LocalDateTime.parse("2025-02-20T18:00"),
                        LocalDateTime.parse("2025-02-21T18:50"),
                        LocalDateTime.parse("2025-02-22T18:00"),
                        LocalDateTime.parse("2025-02-24T18:00"),
                        LocalDateTime.parse("2025-02-25T18:00")
                )
        );

        Medico med4 = new Medico(
                "Laiza Kevelly",
                "5108",
                Especialidade.DERMATOLOGIA,
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T13:30"),
                        LocalDateTime.parse("2025-02-18T14:20"),
                        LocalDateTime.parse("2025-02-19T15:00"),
                        LocalDateTime.parse("2025-02-20T15:40"),
                        LocalDateTime.parse("2025-02-21T16:20")

                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-23T08:00"),
                        LocalDateTime.parse("2025-02-24T09:30"),
                        LocalDateTime.parse("2025-02-25T10:00"),
                        LocalDateTime.parse("2025-02-26T10:30"),
                        LocalDateTime.parse("2025-03-03T11:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T12:00"),
                        LocalDateTime.parse("2025-02-18T12:00"),
                        LocalDateTime.parse("2025-02-19T12:00"),
                        LocalDateTime.parse("2025-02-20T12:00"),
                        LocalDateTime.parse("2025-02-21T12:00")

                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-18T18:00"),
                        LocalDateTime.parse("2025-02-19T18:00"),
                        LocalDateTime.parse("2025-02-20T18:00"),
                        LocalDateTime.parse("2025-02-21T18:00")
                )
        );

        Medico med5 = new Medico(
                "Gabriel Barbosa",
                "5678",
                Especialidade.DERMATOLOGIA,
                Arrays.asList(
                        LocalDateTime.parse("2025-02-19T13:00"),
                        LocalDateTime.parse("2025-02-20T13:40"),
                        LocalDateTime.parse("2025-02-21T15:30"),
                        LocalDateTime.parse("2025-02-22T16:00"),
                        LocalDateTime.parse("2025-02-17T17:00")

                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-23T08:00"),
                        LocalDateTime.parse("2025-02-24T09:30"),
                        LocalDateTime.parse("2025-02-25T10:00"),
                        LocalDateTime.parse("2025-02-26T10:30"),
                        LocalDateTime.parse("2025-02-27T11:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T12:00"),
                        LocalDateTime.parse("2025-02-18T12:00"),
                        LocalDateTime.parse("2025-02-19T12:00"),
                        LocalDateTime.parse("2025-02-20T12:00"),
                        LocalDateTime.parse("2025-02-21T12:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-18T18:00"),
                        LocalDateTime.parse("2025-02-19T18:00"),
                        LocalDateTime.parse("2025-02-20T18:00"),
                        LocalDateTime.parse("2025-02-21T18:00")
                )
        );

        medicoImutaveis.add(med1);
        medicoImutaveis.add(med2);
        medicoImutaveis.add(med3);
        medicoImutaveis.add(med4);
        medicoImutaveis.add(med5);
        salvarMedicoImutaveis();
    }



}
