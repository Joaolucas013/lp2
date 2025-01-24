package org.example.arm.service;

import org.example.arm.exception.ConsultaException;
import org.example.arm.medico.Especialidade;
import org.example.arm.medico.Medico;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class MedicoService {


   public List<Medico> medicoList = new ArrayList<>();
   public List<Medico> medicoImutaveis = new ArrayList<>();
   public  Scanner scanner = new Scanner(System.in);

    public void cadastrarMedico() {
        System.out.println("Informe o nome do médico:");
        String nome = scanner.nextLine().trim();
        System.out.println("Informe o CRM:");
        String crm = scanner.nextLine();
        String crmValido = validarCrm(crm);
        System.out.println("Informe  a especialidade");
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine());

        System.out.println("Informe o(s) horarios  de consultas do médico!" + nome);
        LocalDateTime consultaHorario = LocalDateTime.parse(scanner.nextLine());
        List<LocalDateTime> disponibilidadeSemanal = new ArrayList<>();
        disponibilidadeSemanal.add(consultaHorario);

        System.out.println("Informe o(s) horarios  disponiveis para a semana: ");
        LocalDateTime descanso = LocalDateTime.parse(scanner.nextLine());
        List<LocalDateTime> descansoSemanal = new ArrayList<>();
        descansoSemanal.add(descanso);

        System.out.println("Informe o(s) horarios  de descanso do médico!");
        LocalDateTime bloqueado = LocalDateTime.parse(scanner.nextLine());
        List<LocalDateTime> horariosbloqueados = new ArrayList<>();
        horariosbloqueados.add(bloqueado);

        Medico medico = new Medico(nome, crmValido, especialidade, disponibilidadeSemanal, descansoSemanal, horariosbloqueados);

        medicoList.add(medico);
        System.out.println("Médico salvo com sucesso! " + medico);
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

    private void salvarTodosMedicos() throws IOException {
        String caminho = "C:\\meuscode\\consultasLp2\\medicos.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho, true))) {
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

    // aqui retorna todos os médicos, os imutaveis, mas n é possivel mudar os dados deles
    public List<Medico> recuperarMedico() {
        String caminho = "C:\\meuscode\\consultasLp2\\medicos.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine();
            while (linha != null) {
                linha = linha.replace("Medico{", "").replace("}", "");
                String[] vetor = linha.split(",\\s*(?![^\\[]*\\])");

                String nome = vetor[0].split("=")[1].replace("'", "");
                String crm = vetor[1].split("=")[1].replace("'", "");
                Especialidade especialidade = Especialidade.valueOf(vetor[2].split("=")[1].replace("'", ""));


                String horariosDisponiveisStr = vetor[3].split("=")[1];
                List<LocalDateTime> horariosDisponiveis = listaHorarios(horariosDisponiveisStr);


                String horariosDescansoStr = vetor[4].split("=")[1];
                List<LocalDateTime> horariosDescanso = listaHorarios(horariosDescansoStr);


                String horarioBlock = vetor[5].split("=")[1];
                List<LocalDateTime> horarioBloqueado = listaHorarios(horarioBlock);

                Medico medico = new Medico(nome, crm, especialidade, horariosDisponiveis, horariosDescanso, horarioBloqueado);
                if (medicoImutaveis.size() < 5) {
                    medicoImutaveis.add(medico);
                }
                medicoList.add(medico);
                linha = br.readLine();

            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo de médicos", e);
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
        String caminho = "C:\\meuscode\\consultasLp2\\medicos.txt";
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
                        medico.getHorariosDisponiveis(), medico.getHorariosDescanso(), medico.getHorarioBloqueado());
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
        String caminho = "C:\\meuscode\\consultasLp2\\medicos.txt";
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
        System.out.println("medico salvo com sucesso!");
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
        recuperarMedico();
        medicoImutaveis.stream().limit(5).forEach(System.out::println);
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
        if(medicoList.isEmpty()){
            recuperarMedico();
        }
        medicoList.stream().forEach(System.out::println);
        return medicoList;
    }


    public void iniciarMedicos() {
        Medico med1 = new Medico(
                "Joao",
                "1234",
                Especialidade.CARDIOLOGIA,
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T09:00"),
                        LocalDateTime.parse("2025-02-18T11:00"),
                        LocalDateTime.parse("2025-02-19T13:40"),
                        LocalDateTime.parse("2025-02-20T14:10"),
                        LocalDateTime.parse("2025-02-21T15:10"),
                        LocalDateTime.parse("2025-02-22T16:10")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T08:00"),
                        LocalDateTime.parse("2025-02-18T09:30"),
                        LocalDateTime.parse("2025-02-19T10:00"),
                        LocalDateTime.parse("2025-02-20T11:30"),
                        LocalDateTime.parse("2025-02-21T16:50")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T18:00"),
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
                        LocalDateTime.parse("2025-02-19T07:00"),
                        LocalDateTime.parse("2025-02-19T08:00"),
                        LocalDateTime.parse("2025-02-19T11:00"),
                        LocalDateTime.parse("2025-02-17T08:30"),
                        LocalDateTime.parse("2025-02-18T09:00"),
                        LocalDateTime.parse("2025-02-19T10:00"),
                        LocalDateTime.parse("2025-02-20T11:25"),
                        LocalDateTime.parse("2025-02-21T13:30")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-23T12:00"),

                        LocalDateTime.parse("2025-02-24T12:00"),
                        LocalDateTime.parse("2025-02-25T12:00"),
                        LocalDateTime.parse("2025-02-26T12:00"),
                        LocalDateTime.parse("2025-02-27T12:00"),
                        LocalDateTime.parse("2025-03-04T12:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00")
                )
        );

        Medico med3 = new Medico(
                "Gabriel Silvestre",
                "3344",
                Especialidade.NEUROLOGIA,
                Arrays.asList(
                        LocalDateTime.parse("2025-02-19T07:00"),
                        LocalDateTime.parse("2025-02-19T08:00"),
                        LocalDateTime.parse("2025-02-19T11:00"),
                        LocalDateTime.parse("2025-02-17T08:30"),
                        LocalDateTime.parse("2025-02-18T09:00"),
                        LocalDateTime.parse("2025-02-19T10:00"),
                        LocalDateTime.parse("2025-02-20T11:25"),
                        LocalDateTime.parse("2025-02-21T13:30")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-23T12:00"),
                        LocalDateTime.parse("2025-02-24T12:00"),
                        LocalDateTime.parse("2025-02-25T12:00"),
                        LocalDateTime.parse("2025-02-26T12:00"),
                        LocalDateTime.parse("2025-02-27T12:00"),
                        LocalDateTime.parse("2025-03-04T12:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00"),
                        LocalDateTime.parse("2025-02-17T18:00")
                )
        );

        Medico med4 = new Medico(
                "Láiza Kevelly",
                "5108",
                Especialidade.DERMATOLOGIA,
                Arrays.asList(
                        LocalDateTime.parse("2025-02-17T08:00"),
                        LocalDateTime.parse("2025-02-18T09:00"),
                        LocalDateTime.parse("2025-02-19T10:00"),
                        LocalDateTime.parse("2025-02-20T11:00"),
                        LocalDateTime.parse("2025-02-21T13:30")
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
                        LocalDateTime.parse("2025-02-17T09:00"),
                        LocalDateTime.parse("2025-02-18T10:00"),
                        LocalDateTime.parse("2025-02-19T13:00"),
                        LocalDateTime.parse("2025-02-20T14:00"),
                        LocalDateTime.parse("2025-02-21T15:00")
                ),
                Arrays.asList(
                        LocalDateTime.parse("2025-02-23T12:00"),

                        LocalDateTime.parse("2025-02-24T12:00"),
                        LocalDateTime.parse("2025-02-25T12:00"),
                        LocalDateTime.parse("2025-02-26T12:00"),
                        LocalDateTime.parse("2025-02-27T12:00"),
                        LocalDateTime.parse("2025-03-04T12:00")
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