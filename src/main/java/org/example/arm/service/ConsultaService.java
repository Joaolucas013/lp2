package org.example.arm.service;

import org.example.arm.consulta.Consulta;
import org.example.arm.consulta.DadosConsulta;
import org.example.arm.medico.Especialidade;
import org.example.arm.medico.Medico;
import org.example.arm.paciente.Paciente;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ConsultaService {

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

        if ((medico.getHorariosDescanso().equals(horarioPaciente)) || (medico.getHorarioBloqueado().equals(horarioPaciente))) {
            System.out.println(horarioPaciente + " Horário indisponivel, escolha outro horário!");
            LocalDateTime outroHorario = LocalDateTime.parse(scanner.nextLine());
            validarHorario(outroHorario, medico); // verifica novamebte se o horario não fere a validacao
            return outroHorario;
        }
        return horarioPaciente;
    }


    public void cadastrarPelaEspecialidade() {
        System.out.println("Informe o nome do paciente:");
        String name = scanner.nextLine();
        var paciente = pacienteService.procurarPaciente(name);

        medicoService.retornarHashEspecialidade();
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine().toUpperCase());
       var medico =  medicoService.procurarMedicoEspecialidade(especialidade);

       agendarConsulta(paciente, medico);

    }



    private void agendarConsulta(Paciente paciente, Medico medico) {

        if(!medico.getHorariosDisponiveis().isEmpty()){
            LocalDateTime horario = medico.getHorariosDisponiveis().remove(0);
            Consulta consulta = new Consulta(horario, medico, paciente);
            DadosConsulta consultaDto = new DadosConsulta(consulta.getPaciente().getNome(), consulta.getMedico().getNome(), consulta.getMedico().getEspecialidade(), horario);
            System.out.println("Consulta agendada com sucesso!\n" + consultaDto);
            medicoService.salvarAlteracoes();
            consultas.add(consultaDto);
            salvarConsultas(consulta);


        } else{
            System.out.println("Não há mais horários disponíveis para a semana. Escolha um horario:");
            LocalDateTime horarioPaciente = LocalDateTime.parse(scanner.nextLine());

            LocalDateTime outroHorario =  validarHorario(horarioPaciente, medico);
            List<LocalDateTime> horarioReservado = new ArrayList<>();
            horarioReservado.add(outroHorario);

            medico.setHorariosDisponiveis(horarioReservado);
            Consulta consulta = new Consulta(outroHorario, medico, paciente);
            DadosConsulta consultaDto = new DadosConsulta(consulta.getPaciente().getNome(), consulta.getMedico().getNome(), consulta.getMedico().getEspecialidade(), outroHorario);
            consultas.add(consultaDto);
            System.out.println("Consulta agendada com sucesso!\n" + consultaDto);
            medicoService.salvarAlteracoes();

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
        System.out.println("Consultas salvas no arquivo consultas.txt");
        consultas.stream().forEach(System.out::println);
    }
}
