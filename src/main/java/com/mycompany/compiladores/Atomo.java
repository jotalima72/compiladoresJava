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

    private String codigo;
    private String identificador;
    private int tamanho;
    private int tamTrucado;
    private String tipo;
    private final int linhas[] = new int[5];

    public Atomo() {
        this.codigo = null;
        this.identificador = null;
        this.tamanho = 0;
        this.tamTrucado = 0;
        this.tipo = null;
    }

    public Atomo(String codigo, String identificador, int tamanho, int tamTrucado, String tipo, int linha) {
        this.codigo = codigo;
        this.identificador = identificador;
        this.tamanho = tamanho;
        this.tamTrucado = tamTrucado;
        this.tipo = tipo;
        this.linhas[0] = linha;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Override
    public String toString() {
        return this.codigo + "\t"+ this.tipo + "\t" + this.tamanho + "\t"  + this.tamTrucado + "\t"+ this.getLinhas()+"\t\t"+this.identificador;
    }

    public void setTamTrucado(int tamTrucado) {
        this.tamTrucado = tamTrucado;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getLinhas() {
        String retorno = "";
        int i = 0;
        for (int linha : linhas) {
            if (i == 0) {
                retorno += linha;
            } else if (linha != 0) {
                retorno += ", " + linha;
            }
            i++;
        }
        return retorno;
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
        if (this.linhas[4] == 0) {
            for (int i = 0; i < this.linhas.length; i++) {
                if (this.linhas[i] == 0) {
                    this.linhas[i] = linha;
                    return;
                }
            }
        }
    }
}
