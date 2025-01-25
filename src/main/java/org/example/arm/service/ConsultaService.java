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

    List<DadosConsulta> consultas = new ArrayList<>();
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

    private LocalDateTime validarHorario(LocalDateTime horarioPaciente, Medico medico) {
        LocalDateTime domingo = LocalDateTime.now().with(DayOfWeek.SUNDAY);

        if (medico.getHorariosDescanso().contains(horarioPaciente) ||
                medico.getHorarioBloqueado().contains(horarioPaciente) || horarioPaciente.equals(domingo)) {
            System.out.println("Horario indisponível escolha outro");
            LocalDateTime outroHorario = LocalDateTime.parse(scanner.nextLine());
            return validarHorario(outroHorario, medico);
        }
        return horarioPaciente;
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

        if(!medico.getHorariosDisponiveis().isEmpty()){
            LocalDateTime horario = medico.getHorariosDisponiveis().remove(0);
            Consulta consulta = new Consulta(horario, medico, paciente);
            medicoService.salvarAlteracoes();
            salvarConsultas(consulta);
            System.out.println("Consuta agendada com sucesso!");
        } else{
            System.out.println("Não há mais horários disponíveis para a semana. Escolha um horario:");
            LocalDateTime horarioPaciente = LocalDateTime.parse(scanner.nextLine().trim());
            LocalDateTime outroHorario =  validarHorario(horarioPaciente, medico);
            List<LocalDateTime> horarioReservado = new ArrayList<>();
            horarioReservado.add(outroHorario);

            Consulta consulta = new Consulta(outroHorario, medico, paciente);
            salvarConsultas(consulta);
            medicoService.salvarAlteracoes();
            System.out.println("Consuta agendada com sucesso!");
        }

    }

    public void salvarConsultas(Consulta consulta)  {
         String caminho = "C:\\meuscode\\consultasLp2\\consultas.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true))) {
                writer.write(consulta.toString());
              writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void finalizar() {
        var consulta = retornaConsultas();

        if (consulta.isEmpty()) {
            System.out.println("Não há consultas agendadas!");
        } else {
            System.out.println("Consultas salvas no arquivo consultas.txt");
            consulta.stream().forEach(System.out::println);

        }
    }


    public List<DadosConsulta> retornaConsultas() {
        DadosConsulta dados = null;
        String caminho = "C:\\meuscode\\consultasLp2\\consultas.txt";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))) {
            String linha = bufferedReader.readLine();

            while (linha != null) {
                linha = linha.replace("Consulta{horario consulta=", "")
                        .replace("}", "")
                        .replace("paciente=", "")
                        .replace("medico=", "");

                String[] vetor = linha.split(", ");
                String horarioConsulta = vetor[0];
                String paciente = vetor[1];
                String medico = vetor[2];

                String nomePaciente = paciente.split(",")[0].split("=")[1].replace("'", "").trim();
                String nomeMedico = medico.trim();
                LocalDateTime dataConsulta = LocalDateTime.parse(horarioConsulta);

                 dados = new DadosConsulta(nomePaciente, nomeMedico, dataConsulta);
                 linha = bufferedReader.readLine();
                 consultas.add(dados);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return  consultas;
    }

}
