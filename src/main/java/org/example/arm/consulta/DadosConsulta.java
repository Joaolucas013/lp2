package org.example.arm.consulta;

import org.example.arm.medico.Especialidade;

import java.time.LocalDateTime;

public record DadosConsulta(String paciente, String medico, LocalDateTime data) {

    public DadosConsulta(String paciente, String medico, LocalDateTime data) {
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
    }
}
