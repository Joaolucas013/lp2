package org.example.arm;

import org.example.arm.service.ConsultaService;
import org.example.arm.service.MedicoService;
import org.example.arm.service.PacienteService;

import java.util.Scanner;

public class ConsultasMain {

    public static MedicoService medicoService = new MedicoService();
    public static ConsultaService consultaService = new ConsultaService();
    public static PacienteService pacienteService = new PacienteService();

    public static void main(String[] args) {
       medicoService.retornarMedicoImutaveis();
        boolean continuar = true;
        while (continuar) {
            menu();
            int opcao = new Scanner(System.in).nextInt();
            switch (opcao) {
                case 1:
                    agendar();
                    break;
                case 2:
                   bloquearHorario();
                    break;
                case 3:
                    cadastrarPelaEspecialidade();
                    break;
                case 4:
                    cadastrarMedico();
                    break;
                case 5:
                    cadastrarPaciente();
                    break;
                case 6:
                    salvar();
                    System.out.println("Bye...");
                    continuar= false;
                    break;
            }
        }

    }

    private static void salvar() {
        System.out.println("Salvando consulta!");
        consultaService.finalizar();
    }

    private static void cadastrarPaciente() {
        pacienteService.cadastrarPaciente();
    }

    private static void cadastrarMedico() {
        medicoService.cadastrarMedico();
    }

    private static void cadastrarPelaEspecialidade() {
        consultaService.cadastrarPelaEspecialidade();
    }

    private static void bloquearHorario() {
        medicoService.bloquearHorario();
    }

    private static void agendar() {
       consultaService.agendar();

    }

    private static void menu() {
        System.out.println(""" 
             
                1 - Marcar consulta
                2 - Bloquear horario
                3 - Consultar por especialidade
                4 - cadastrar médico
                5 - cadastrar paciente
                6 - Sair
                """);
    }

}



