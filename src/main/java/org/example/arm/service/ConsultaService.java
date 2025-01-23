package org.example.arm.service;

import org.example.arm.consulta.Consulta;
import org.example.arm.consulta.ConsultaDto;
import org.example.arm.exception.ConsultaException;
import org.example.arm.medico.Especialidade;
import org.example.arm.medico.Medico;
import org.example.arm.paciente.Paciente;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsultaService {

    List<Consulta> consultas = new ArrayList<>();
   static MedicoService medicoService = new MedicoService();
   static PacienteService pacienteService = new PacienteService();
    Scanner scanner = new Scanner(System.in);


    public void agendar() {
        System.out.println("Informe o nome do paciente:");
        String nome = scanner.nextLine().trim();
        var paciente = pacienteService.procurarPaciente(nome);

        medicoService.medicoList.stream().forEach(System.out::println);
        System.out.println("Escolha um médico!");
        String name = scanner.nextLine();
        Medico medico = medicoService.procurarMedico(name);

        agendarConsulta(paciente, medico);
    }

    private LocalDateTime validarHorario(LocalDateTime horarioPaciente, Medico medico) {

        if ((medico.getHorariosDescanso().equals(horarioPaciente)) || (medico.getHorarioBloqueado().equals(horarioPaciente))) {
            System.out.println(horarioPaciente + " Horário indisponível, escolha outro horário!");
            LocalDateTime outroHorario = LocalDateTime.parse(scanner.nextLine());
            validarHorario(outroHorario, medico); // verifica novamert se o horario não fere a validacao
            return outroHorario;
        }
        return horarioPaciente;
    }


    public  void salvarConsulta() {
        String path = "C:\\meuscode\\consultasLp2\\consultas.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            for (Consulta c: consultas){
                bw.write(c.toString());
                bw.newLine();
            }
            System.out.println("Consultas salvas com sucesso!");
        } catch (IOException e) {
            throw new RuntimeException("Erro: " + e.getMessage());
        }
    }

    public void cadastrarPelaEspecialidade() {
        System.out.println("Informe o nome do paciente:");
        String name = scanner.nextLine().trim();

        var paciente = pacienteService.procurarPaciente(name);
        if(paciente==null){
            pacienteService.cadastrarPaciente();
        }
        medicoService.retornarEspecialidade();
        System.out.println("escolha a especialidade:");
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine());
       var medico =  medicoService.procurarMedicoEspecialidade(especialidade);

       agendarConsulta(paciente, medico);
    }

    private void agendarConsulta(Paciente paciente, Medico medico) {

        if(!medico.getHorariosDisponiveis().isEmpty()){
            LocalDateTime horario = medico.getHorariosDisponiveis().get(0);
            Consulta consulta = new Consulta(horario, medico, paciente);
            ConsultaDto consultaDto = new ConsultaDto(consulta.getPaciente().getNome(), consulta.getMedico().getNome(), consulta.getMedico().getEspecialidade(), horario);
            System.out.println("Consulta agendada com sucesso!\n" + consultaDto);
            medico.getHorariosDisponiveis().remove(horario);

            consultas.add(consulta);
            medicoService.salvarAlteracoes();

        } else{
            System.out.println("Não há mais horários disponíveis para a semana. Escolha um horario:");
            LocalDateTime horarioPaciente = LocalDateTime.parse(scanner.nextLine());

            LocalDateTime outroHorario =  validarHorario(horarioPaciente, medico);
            List<LocalDateTime> horarioReservado = new ArrayList<>();
            horarioReservado.add(outroHorario);

            medico.setHorariosDisponiveis(horarioReservado);
            Consulta consulta = new Consulta(outroHorario, medico, paciente);
            ConsultaDto consultaDto = new ConsultaDto(consulta.getPaciente().getNome(), consulta.getMedico().getNome(), consulta.getMedico().getEspecialidade(), outroHorario);

            System.out.println("Consulta agendada com sucesso!\n" + consultaDto);
            medicoService.salvarAlteracoes();
            consultas.add(consulta);


        }

    }
}
