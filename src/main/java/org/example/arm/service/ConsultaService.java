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
        System.out.println(medico);
        Medico  Medicoprocurado = new Medico(medico.getNome(), medico.getCrm(), medico.getEspecialidade(), medico.getHorariosDisponiveis(), medico.getHorariosDescanso(), medico.getHorarioBloqueado());
        if(medico==null){
            throw  new ConsultaException("Médico não encontrado!!!");
        }

        // lista todos os horarios disponiveis na semana!
        medico.getHorariosDisponiveis().stream().forEach(System.out::println);

        if(medico.getHorariosDisponiveis().isEmpty()){
            throw new ConsultaException("Não há horários disponíveis na semana!");
        }
        LocalDateTime horario = medico.getHorariosDisponiveis().get(0);
        medico.getHorariosDisponiveis().remove(horario);

        Consulta consulta = new Consulta(horario, medico, paciente);
        salvarConsulta(consulta);

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
