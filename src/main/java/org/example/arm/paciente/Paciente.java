package org.example.arm.paciente;

import java.util.Objects;

public class Paciente {
    private String nome;
    private String sexo;
    private Integer idade;

    public Paciente(String nome, String sexo, Integer idade) {
        this.nome = nome;
        this.sexo = sexo;
        this.idade = idade;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nome='" + nome + '\'' +
                ", sexo='" + sexo + '\'' +
                ", idade=" + idade +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "Paciente{" +
//                "nome='" + nome +
//                ", sexo='" + sexo +
//                ", idade=" + idade +
//                '}';
//    }

    public Paciente(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Paciente paciente = (Paciente) o;
        return idade == paciente.idade && Objects.equals(nome, paciente.nome) && Objects.equals(sexo, paciente.sexo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, sexo, idade);
    }


}
