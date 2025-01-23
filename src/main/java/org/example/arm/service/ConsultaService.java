package org.example.arm.service;

import org.example.arm.consulta.Consulta;
import org.example.arm.exception.ConsultaException;
import org.example.arm.medico.Medico;

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
        String name = scanner.nextLine().trim();
        var paciente = pacienteService.procurarPaciente(name);
        if(paciente==null){
            pacienteService.cadastrarPaciente();
        }
        medicoService.medicoList.stream().forEach(System.out::println);
        System.out.println("Escolha um médico!");
        String nome = scanner.nextLine();
        Medico medico = medicoService.procurarMedico(nome);
       // Medico  Medicoprocurado = new Medico(medico.getNome(), medico.getCrm(), medico.getEspecialidade(), medico.getHorariosDisponiveis(), medico.getHorariosDescanso(), medico.getHorarioBloqueado());

        if(medico==null){
            throw  new ConsultaException("Médico não encontrado!!!");
        }

        if(!medico.getHorariosDisponiveis().isEmpty()){
            LocalDateTime horario = medico.getHorariosDisponiveis().get(0);
            medico.getHorariosDisponiveis().remove(horario);
            Consulta consulta = new Consulta(horario, medico, paciente);
            salvarConsulta(consulta);
        } else{
            System.out.println("Não há mais horários disponíveis para a semana. Escolha um horario:");
            LocalDateTime horarioPaciente = LocalDateTime.parse(scanner.nextLine());

            // verificando se horario não coincide com o horario de bloqueio ou horario de descanso
            LocalDateTime outroHorario =  validarHorario(horarioPaciente, medico);

            List<LocalDateTime> horarioReservado = new ArrayList<>();
            horarioReservado.add(outroHorario);
            medico.setHorariosDisponiveis(horarioReservado);
            Consulta consulta = new Consulta(outroHorario, medico, paciente);
            salvarConsulta(consulta);
        }


    }

    private LocalDateTime validarHorario(LocalDateTime horarioPaciente, Medico medico) {
        if ((medico.getHorariosDescanso().equals(horarioPaciente)) || (medico.getHorarioBloqueado().equals(horarioPaciente))) {
            System.out.println(horarioPaciente + " Horário indisponível, escolha outro horário!");
            LocalDateTime outroHorario = LocalDateTime.parse(scanner.nextLine());
            return outroHorario;
        }
        return horarioPaciente;
    }


    private void salvarConsulta(Consulta consulta) {
        String path = "C:\\meuscode\\consultasLp2\\consultas.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(consulta.toString());
            bw.newLine();
            System.out.println("Consulta salva com sucesso! " + consulta);
        } catch (IOException e) {
            throw new RuntimeException("Erro: " + e.getMessage());
        }
    }

}
