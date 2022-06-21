package com.mycompany.compiladores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author jota
 * @author Daniel
 */
public class Compiladores {

    public static ArrayList<Lexeme> reservados = new ArrayList<>();
    public static ArrayList<Lexeme> lexemes = new ArrayList<>();
    public static int countLinha = 1;
    public static String identificador = "";
    public static int index = 0;
    public static boolean isComentario = false;

    public static void main(String[] args) throws IOException {

        String path = "reservados";//console.nextLine();
        leitor(path + ".txt");
//        reservados.forEach(lex -> {
//            System.out.println(lex.toString());
//        });
        System.out.println("Nome do arquivo: ");
        //Scanner console = new Scanner(System.in);
        path = "macaco";//console.nextLine();
        try ( BufferedReader buffRead = new BufferedReader(new FileReader(path + ".DKS"))) {
            String linha;
            identificador = "";
            char letra;
            while ((linha = buffRead.readLine()) != null) {
                //System.out.println(line);
                countLinha++;
                for (index = 0; index < linha.length(); index++) {
                    letra = linha.charAt(index);
                    letra = Character.toUpperCase(letra);
                    boolean criaId = lerAtomo(letra, linha);
                    if (criaId) {
                        Lexeme lex = cria_identificador();
                        System.out.println(lex.toString());
                    }
                }
            }
        }
    }

    public static void leitor(String path) throws IOException {
        try ( BufferedReader buffRead = new BufferedReader(new FileReader(path))) {
            String linha;
            String aux[];
            Lexeme lex;
            while (true) {
                linha = buffRead.readLine();
                if (linha != null) {
                    aux = linha.split(" ");

                    lex = new Lexeme(aux[1], aux[0]);
                    reservados.add(lex);
                } else {
                    break;
                }

            }
        }
    }

    public static Lexeme cria_identificador() {
        Lexeme atominho = null;
        for (Lexeme reservado : reservados) {
            if (reservado.getIdentificador().contentEquals(identificador)) {
                atominho = reservado;
                break;
            }
        }
        if (atominho == null) {
            atominho = new Lexeme();
            String aux;
            atominho.setIdentificador(identificador);
            if (Character.isDigit(identificador.charAt(0))) {
                if (identificador.contains(".")) {
                    aux = "FLOAT-NUMBER";
                } else {
                    aux = "INTEGER-NUMBER";
                }
            } else if (identificador.charAt(0) == '\'') {
                aux = "CHARACTER";
            } else if (identificador.charAt(0) == '\"') {
                aux = "CONSTANT-STRING";
            } else if (identificador.contains("_")) {
                aux = "IDENTIFIER";
            } else {
                aux = "FUNCTION";
            }
            for (Lexeme reservado : reservados) {
                if (reservado.getIdentificador().contentEquals(aux)) {
                    atominho.setCodigo(reservado.getCodigo());
                    break;
                }
            }
        }
        identificador = "";
        return atominho;
    }

    public static boolean lerAtomo(char letra, String linha) {
        if (Character.isSpaceChar(letra) && !(identificador.length() > 0) && !isComentario) {
        } else if (Character.isDigit(letra) && !(identificador.length() > 0) && !isComentario) {
            char posponto, pose;
            boolean temPonto = false, temE = false;
            identificador += letra;
            while (index < linha.length()) {
                index++;
                if (!(index < linha.length())) {
                    break;
                }
                letra = linha.charAt(index);
                letra = Character.toUpperCase(letra);
                if (Character.isDigit(letra)) {
                    identificador += letra;
                } else if (letra == '.' && !temPonto) {
                    index++;
                    if (!(index < linha.length())) {
                        break;
                    }
                    posponto = linha.charAt(index);
                    posponto = Character.toUpperCase(posponto);

                    if (Character.isDigit(posponto)) {
                        identificador += letra;
                        identificador += posponto;
                        temPonto = true;
                    } else {
                        index--;
                        break;
                    }
                } else if (letra == 'E' && temPonto && !temE) {
                    index++;
                    if (!(index < linha.length())) {
                        break;
                    }
                    posponto = linha.charAt(index);
                    posponto = Character.toUpperCase(posponto);
                    if (Character.isDigit(posponto)) {
                        identificador += letra;
                        identificador += posponto;
                        temE = true;
                    } else if (posponto == '-' || posponto == '+') {
                        index++;
                        if (!(index < linha.length())) {
                            break;
                        }
                        pose = linha.charAt(index);
                        pose = Character.toUpperCase(pose);
                        if (Character.isDigit(pose)) {
                            identificador += letra;
                            identificador += posponto;
                            identificador += pose;
                            temE = true;
                        } else {
                            index -= 3;
                            return true;
                        }
                    } else {
                        index--;
                        break;
                    }
                } else {
                    index--;
                    break;
                }
            }
        } else if ((letra == '\'' && !(identificador.length() > 0)) && !isComentario) {

            identificador += letra;
            boolean temChar = false;
            while (index < linha.length()) {
                index++;
                if (!(index < linha.length())) {
                    break;
                }
                letra = linha.charAt(index);
                letra = Character.toUpperCase(letra);
                if (Character.isAlphabetic(letra) && !temChar) {
                    identificador += letra;
                    temChar = true;
                } else if (letra == '\'') {
                    identificador += letra;
                    return true;
                }
            }
        } else if (letra == '\"' && !(identificador.length() > 0) && !isComentario) {
            identificador += letra;
            while (index < linha.length()) {
                index++;
                if (!(index < linha.length())) {
                    break;
                }
                letra = linha.charAt(index);
                letra = Character.toUpperCase(letra);
                if (Character.isSpaceChar(letra) || Character.isLetterOrDigit(letra) || letra == '$' || letra == '_' || letra == '.') {
                    identificador += letra;
                } else if (letra == '\"') {
                    identificador += letra;
                    return true;
                }
            }
        } else if (isComentario) {
            char posBarra;
            if (letra == '*') {
                index++;
                if (!(index < linha.length())) {
                    return false;
                }
                posBarra = linha.charAt(index);
                posBarra = Character.toUpperCase(posBarra);
                if (posBarra == '/') {
                    isComentario = false;
                    return false;
                } else {
                    index--;
                }
            }
        } else if (letra == '/' && !(identificador.length() > 0)) {
            char posBarra;
            while (index < linha.length()) {
                index++;
                if (!(index < linha.length())) {
                    break;
                }
                posBarra = linha.charAt(index);
                posBarra = Character.toUpperCase(posBarra);
                if (posBarra == '/') {
                    while (index < linha.length()) {
                        index++;
                        if (!(index < linha.length())) {
                            break;
                        }
                    }
                } else if (posBarra == '*') {
                    isComentario = true;
                    while (index < linha.length()) {
                        index++;
                        if (!(index < linha.length())) {
                            break;
                        }
                        letra = linha.charAt(index);
                        letra = Character.toUpperCase(letra);
//                                    if (letra == 10) {
//                                        ( * countLinha)++;
//                                    }
                        if (letra == '*') {
                            index++;
                            if (!(index < linha.length())) {
                                break;
                            }
                            posBarra = linha.charAt(index);
                            posBarra = Character.toUpperCase(posBarra);
                            if (posBarra == '/') {
                                isComentario = false;
                                break;
                            } else {
                                index--;
                            }
                        }
                    }
                } else {
                    index--;
                    identificador += letra;
                    return true;
                }
            }
        } else if (!Character.isLetterOrDigit(letra) && letra != '_') {
            if (aceita_punct(letra)) {
                if (!(identificador.length() > 0)) {
                    identificador += letra;
                    if (ispunct_duplicado(letra)) {
                        char posDuplicavel;
                        index++;
                        if (!(index < linha.length())) {
                            return false;
                        }
                        posDuplicavel = linha.charAt(index);
                        posDuplicavel = Character.toUpperCase(posDuplicavel);
                        if (posDuplicavel == '=') {
                            identificador += posDuplicavel;
                        } else {
                            index--;
                        }
                    }
                    return true;
                }
            }
            if (Character.isSpaceChar(letra) || letra == '.') {
                return true;
            } else if (!Character.isSpaceChar(letra) && aceita_punct(letra)) {
                index--;
                return true;
            }
        } else {
            identificador += letra;
        }
        return false;
    }

    public static boolean ispunct_duplicado(char character) {
        return character == '!' || character == '=' || character == '>' || character == '<';
    }

    public static boolean aceita_punct(char punct) {
        return punct == '_' || punct == '!' || punct == '#' || punct == '=' || punct == '&' || punct == '(' || punct == ')' || punct == ';' || punct == '{' || punct == '}' || punct == '\"' || punct == '\'' || punct == '[' || punct == ']' || punct == ',' || punct == '<' || punct == '>' || punct == '%' || punct == '/' || punct == '|' || punct == '*' || punct == '+' || punct == '-';
    }
}
