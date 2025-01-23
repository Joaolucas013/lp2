package org.example.arm.consulta;

import org.example.arm.medico.Medico;
import org.example.arm.paciente.Paciente;

import java.time.LocalDateTime;

public record ConsultaDto(Paciente paciente, Medico medico, LocalDateTime dataConsulta) {

    public ConsultaDto(Consulta c){
        this(c.getPaciente(), c.getMedico(), c.getDataConsulta());
    }
}
