package org.example.arm.consulta;

import org.example.arm.medico.Especialidade;
import org.example.arm.medico.Medico;
import org.example.arm.paciente.Paciente;

import java.time.LocalDateTime;

public record ConsultaDto(String paciente, String medico, Especialidade especialidade, LocalDateTime data) {

    public ConsultaDto(Consulta c){
        this(c.getPaciente().getNome(), c.getMedico().getNome(), c.getMedico().getEspecialidade(), c.getMedico().getHorariosDisponiveis().get(0));
    }
}
