/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.compiladores;

/**
 *
 * @author jota
 */
public final class Atomo {
    
    private final String codigo;
    private final String identificador;
    private final int tamanho;
    private final int tamTrucado;
    private final String tipo;
    private final int linhas[] = new int[6];

    public Atomo(String codigo, String identificador, int tamanho, int tamTrucado, String tipo, int linha) {
        this.codigo = codigo;
        this.identificador = identificador;
        this.tamanho = tamanho;
        this.tamTrucado = tamTrucado;
        this.tipo = tipo;
        this.linhas[0] = linha;
    }

    public int[] getLinhas() {
        return linhas;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getTamTrucado() {
        return tamTrucado;
    }

    public String getIdentificador() {
        return identificador;
    }

    public int getTamanho() {
        return tamanho;
    }

    public String getTipo() {
        return tipo;
    }

    public void setLinhas(int linha) {
        if(this.linhas.length < 6){
        this.linhas[this.linhas.length] = linha;
        }
    }
    
    
    
}
