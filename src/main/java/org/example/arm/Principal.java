package org.example.arm;

import org.example.arm.paciente.Paciente;
import org.example.arm.service.MedicoService;
import org.example.arm.service.PacienteService;

import java.util.Scanner;

public class Principal {

  static   MedicoService medicoService = new MedicoService();

    public static void main(String[] args) {
//      medicoService.iniciarMedicos();
//      medicoService.recuperarMedico();
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
                    System.out.println("Bye...");
                    continuar= false;
                    break;
            }
        }

    }

    private static void cadastrarPaciente() {
        PacienteService pacienteService = new PacienteService();
        pacienteService.cadastrarPaciente();
    }

    private static void cadastrarMedico() {
        medicoService.salvarMedico();
    }

    private static void cadastrarPelaEspecialidade() {
        medicoService.buscaEspecialidade();
    }

    private static void bloquearHorario() {
        medicoService.bloquearHorario();
    }

    private static void agendar() {

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



