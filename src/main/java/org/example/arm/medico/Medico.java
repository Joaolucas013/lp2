package org.example.arm.medico;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Medico {

private String nome;
    private String crm;
    private Especialidade especialidade;
    private List<LocalDateTime> horariosConsultas;
    private List<LocalDateTime> horariosDisponiveis;
    private  List<LocalDateTime> horariosDescanso;
    private  List<LocalDateTime> horarioBloqueado;

    public Medico(String nome, String crm, Especialidade especialidade,
                  List<LocalDateTime> horariosConsultas,List<LocalDateTime> horariosDisponiveis, List<LocalDateTime> horariosDescanso, List<LocalDateTime> horarioBloqueado) {
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.horariosConsultas = horariosConsultas;
        this.horariosDisponiveis = horariosDisponiveis;
        this.horariosDescanso = horariosDescanso;
        this.horarioBloqueado = horarioBloqueado;

    }

    public List<LocalDateTime> getHorariosConsultas() {
        return horariosConsultas;
    }

    public void setHorariosConsultas(List<LocalDateTime> horariosConsultas) {
        this.horariosConsultas = horariosConsultas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public List<LocalDateTime> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }

    public void setHorariosDisponiveis(List<LocalDateTime> horariosDisponiveis) {
        this.horariosDisponiveis = horariosDisponiveis;
    }


    public List<LocalDateTime> getHorariosDescanso() {
        return horariosDescanso;
    }

    public void setHorariosDescanso(List<LocalDateTime> horariosDescanso) {
        this.horariosDescanso = horariosDescanso;
    }

    public List<LocalDateTime> getHorarioBloqueado() {
        return horarioBloqueado;
    }

    public void setHorarioBloqueado(List<LocalDateTime> horarioBloqueado) {
        this.horarioBloqueado = horarioBloqueado;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "nome='" + nome + '\'' +
                ", crm='" + crm + '\'' +
                ", especialidade=" + especialidade +
                ", horariosConsultas=" + horariosConsultas +
                ", horariosDisponiveis=" + horariosDisponiveis +
                ", horariosDescanso=" + horariosDescanso +
                ", horarioBloqueado=" + horarioBloqueado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medico medico = (Medico) o;
        return Objects.equals(nome, medico.nome) && Objects.equals(crm, medico.crm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, crm);
    }
}
