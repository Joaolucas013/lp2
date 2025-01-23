package org.example.arm.consulta;

import org.example.arm.medico.Medico;
import org.example.arm.paciente.Paciente;

import java.time.LocalDateTime;
import java.util.Objects;

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
        return "Consulta{" +
                "horario consulta=" + dataConsulta +
                ", medico=" + medico +
                ", paciente=" + paciente +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Consulta consulta = (Consulta) o;
        return Objects.equals(dataConsulta, consulta.dataConsulta) && Objects.equals(medico, consulta.medico) && Objects.equals(paciente, consulta.paciente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataConsulta, medico, paciente);
    }
}
