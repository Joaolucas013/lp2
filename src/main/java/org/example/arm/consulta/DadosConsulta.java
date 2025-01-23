package org.example.arm.consulta;

import org.example.arm.medico.Especialidade;

import java.time.LocalDateTime;

public record DadosConsulta(String paciente, String medico, Especialidade especialidade, LocalDateTime data) {

    public DadosConsulta(Consulta c){
        this(c.getPaciente().getNome(), c.getMedico().getNome(), c.getMedico().getEspecialidade(), c.getMedico().getHorariosDisponiveis().get(0));
    }
}
