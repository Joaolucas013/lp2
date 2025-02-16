package org.example.arm.service;

import org.example.arm.consulta.Consulta;
import org.example.arm.consulta.DadosConsulta;
import org.example.arm.medico.Especialidade;
import org.example.arm.medico.Medico;
import org.example.arm.paciente.Paciente;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsultaService{


    List<DadosConsulta> dadosConsultas = new ArrayList<>();
    List<Consulta> consultas = new ArrayList<>();
    static MedicoService medicoService = new MedicoService();
    static PacienteService pacienteService = new PacienteService();
    Scanner scanner = new Scanner(System.in);



    public void agendar() {
        System.out.println("Informe o nome do paciente:");
        String nome = scanner.nextLine().trim();
        var paciente = pacienteService.procurarPaciente(nome);

        medicoService.retornarLista();
        System.out.println("Escolha um médico!");
        String name = scanner.nextLine();
        Medico medico = medicoService.procurarMedico(name);

        agendarConsulta(paciente, medico);
    }


    public void cadastrarPelaEspecialidade() {
        System.out.println("Informe o nome do paciente:");
        String name = scanner.nextLine();
        var paciente = pacienteService.procurarPaciente(name);

        medicoService.retornarHashEspecialidade();
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine().toUpperCase());
        var medico = medicoService.procurarMedicoEspecialidade(especialidade);

        agendarConsulta(paciente, medico);
    }

    private void agendarConsulta(Paciente paciente, Medico medico) {

        if(!medico.getHorariosConsultas().isEmpty()){
            List<LocalDateTime> horario = medico.getHorariosConsultas();
            var hr = horario.remove(0);
            medico.setHorariosConsultas(horario);


            Consulta consulta = new Consulta(hr, medico, paciente);
            consultas.add(consulta);
            salvarConsultasEmArquivo(consulta);
            medicoService.salvarAlteracoes();
            System.out.println("Consulta agendada com sucesso!");
        } else if(!medico.getHorariosDisponiveis().isEmpty()){
            medico.getHorariosDisponiveis().stream().forEach(System.out::println);

            System.out.println("Não há mais horários para consultas. Escolha um horario alternativo:");
            LocalDateTime horarioPaciente = LocalDateTime.parse(scanner.nextLine().trim());
            LocalDateTime outroHorario =  validarHorario(horarioPaciente, medico);
            var horarioFinal = validarConsulta(medico, outroHorario);
            medico.getHorariosDisponiveis().remove(horarioFinal);

            Consulta consulta = new Consulta(horarioFinal, medico, paciente);
            medicoService.salvarAlteracoes();

            consultas.add(consulta);
            salvarConsultasEmArquivo(consulta);
            System.out.println("Consulta agendada com sucesso!");
        } else {
            System.out.println("Não há mais horarios para consultas, escolha o melhor horário para voce.");
            LocalDateTime melhorHorario = LocalDateTime.parse(scanner.nextLine().trim());
            validarHorario(melhorHorario, medico);

            Consulta consulta = new Consulta(melhorHorario, medico, paciente);
            medicoService.salvarAlteracoes();

            consultas.add(consulta);
            salvarConsultasEmArquivo(consulta);
            System.out.println("Consulta agendada com sucesso!");

        }

    }

    private LocalDateTime validarHorario(LocalDateTime horarioPaciente, Medico medico) {
        boolean domingo = horarioPaciente.getDayOfWeek() == DayOfWeek.SUNDAY;


        if (medico.getHorariosDescanso().contains(horarioPaciente) ||
                medico.getHorarioBloqueado().contains(horarioPaciente) || domingo) {
            medico.getHorariosDisponiveis().stream().forEach(System.out::println);
            System.out.println("Sugestão: Horario escolhido anteriormente indisponível, escolha um dos horários alternativos abaixo:");
            LocalDateTime outroHorario = LocalDateTime.parse(scanner.nextLine().trim());

            if (medico.getHorariosDisponiveis().contains(outroHorario)) {
                medico.getHorariosDisponiveis().remove(outroHorario);
                return outroHorario;
            }
        }
        return horarioPaciente;
    }

    private LocalDateTime validarConsulta(Medico medico, LocalDateTime outroHorario) {

        for (Consulta dados : consultas) {
            if (dados.getMedico().getNome().trim().equalsIgnoreCase(medico.getNome().trim())) {
                if (dados.getDataConsulta().toLocalDate().equals(outroHorario.toLocalDate())) {
                    System.out.println("Não é permitido consultas no mesmo dia para o mesmo médico. Escolha outro horário:");
                    LocalDateTime hr = LocalDateTime.parse(scanner.nextLine().trim());
                    return validarConsulta(medico, hr);
                }
            }
        }
        return outroHorario;
    }


    public void salvarConsultasEmArquivo(Consulta consulta)  {
        String caminho = "consultas.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true))) {
            writer.write(consulta.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DadosConsulta> retornaConsultas() {
        String caminho = "consultas.txt";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))) {
            String linha =  bufferedReader.readLine();

            while (linha  != null) {
                linha = linha.replace("Consulta{horario consulta=", "")
                        .replace("}", "")
                        .replace("Paciente{", "")
                        .replace("Especialidade=", "")
                        .replace("CRM=", "");

                String[] vetor = linha.split(", ");

                if (vetor.length < 4) {
                    continue;
                }

                String horarioConsulta = vetor[0].trim();
                String nomePaciente = vetor[1].split("=")[2].replace("'", "").trim();
                String nomeMedico = vetor[4].split("=")[1].replace("'", "").trim();
                LocalDateTime dataConsulta = LocalDateTime.parse(horarioConsulta);

                DadosConsulta dados = new DadosConsulta(nomePaciente, nomeMedico, dataConsulta);
                dadosConsultas.add(dados);
                linha = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return dadosConsultas;
    }

    public void finalizar() {
        if (dadosConsultas.isEmpty()) {
            retornaConsultas();
            if (dadosConsultas.isEmpty()) {
                System.out.println("Não há consultas agendadas!");
            } else {
                System.out.println("Consultas salvas no arquivo consultas.txt");
                dadosConsultas.stream().forEach(System.out::println);
            }
        }
    }



}
