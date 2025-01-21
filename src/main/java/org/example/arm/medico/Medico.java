package org.example.arm.medico;

import java.time.LocalDateTime;
import java.util.Objects;

public class Medico {
    private String nome;
    private String crm;
    private Especialidade especialidade;
    private LocalDateTime consulta;
    private LocalDateTime disponibilidade;
    private LocalDateTime bloqueado;
    private LocalDateTime descanso;


    public Medico(String nome, String crm, Especialidade especialidade, LocalDateTime consulta, LocalDateTime disponibilidade, LocalDateTime descanso) {
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.consulta = consulta;
        this.disponibilidade = disponibilidade;
       this.bloqueado = LocalDateTime.now();
        this.descanso = descanso;
    }

    public Medico(String nome, String crm, Especialidade especialidade, LocalDateTime consulta, LocalDateTime disponibilidade, LocalDateTime bloqueado, LocalDateTime descanso) {
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.consulta = consulta;
        this.disponibilidade = disponibilidade;
        this.bloqueado = bloqueado;
        this.descanso = descanso;
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

    public LocalDateTime getConsulta() {
        return consulta;
    }

    public void setConsulta(LocalDateTime consulta) {
        this.consulta = consulta;
    }

    public LocalDateTime getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(LocalDateTime disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public LocalDateTime getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(LocalDateTime bloqueado) {
        this.bloqueado = bloqueado;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "nome='" + nome + '\'' +
                ", crm='" + crm + '\'' +
                ", especialidade=" + especialidade +
                ", consulta=" + consulta +
                ", disponibilidade=" + disponibilidade +
                ", bloqueado=" + bloqueado +
                ", descanso=" + descanso +
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
