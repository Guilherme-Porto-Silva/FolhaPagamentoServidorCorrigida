package modelos;

import componentes.Departamentos;
import componentes.NomeCargo;

import java.io.Serializable;

public class Cargo implements Serializable {

    private int id;
    
    private double salario;
    
    private Departamento departamento;
    
    private String horarioChegada, horarioSaida;

    private NomeCargo nome;



    public Cargo (NomeCargo nomeCargo) {

        nome = nomeCargo;

        switch (nomeCargo) {

            case NomeCargo.Gerente -> {

                salario = 3660.00;

                horarioChegada = "10:00";

                horarioSaida = "18:00";

                departamento = new Departamento(Departamentos.Administrativo);
            }

            case NomeCargo.Coordenador -> {

                salario = 1830.00;

                horarioChegada = "09:00";

                horarioSaida = "18:00";

                departamento = new Departamento(Departamentos.Administrativo);
            }

            case NomeCargo.Professora -> {

                salario = 1189.50;

                horarioChegada = "07:00";

                horarioSaida = "18:00";

                departamento = new Departamento(Departamentos.Operacional);
            }
        }
    }



    protected double getSalario() {
        
        return salario;
    }

    protected Departamento getDepartamento() {
        
        return departamento;
    }

    protected String getHorarioChegada() {
        
        return horarioChegada;
    }

    protected String getHorarioSaida() {
        
        return horarioSaida;
    }
}