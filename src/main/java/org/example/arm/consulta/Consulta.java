package org.example.arm.consulta;

import org.example.arm.medico.Medico;
import org.example.arm.paciente.Paciente;

import java.time.LocalDateTime;

public class Consulta {

    private LocalDateTime dataConsulta;
    private Medico medico;
    private Paciente paciente;

    public Consulta(LocalDateTime horario, Medico medico, Paciente paciente) {
        this.dataConsulta = horario;
        this.medico = medico;
        this.paciente = paciente;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public String toString() {
        return "Consultas{" +
                "horario consulta=" + dataConsulta +
                ", medico=" + medico +
                ", paciente=" + paciente +
                '}';
    }
}
