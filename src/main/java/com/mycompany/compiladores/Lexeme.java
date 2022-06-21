package com.mycompany.compiladores;

/**
 *
 * @author jota
 */
public final class Lexeme {
    private String codigo;
    private String identificador;

    public Lexeme( String cod, String id) {
        this.setCodigo(cod);
        this.setIdentificador(id);
    }

    public Lexeme() {
    }
    
    @Override
    public String toString() {
        return getCodigo() + "\t" + getIdentificador(); 
    }
    
    
    public String getCodigo() {
        return codigo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    
    
}
