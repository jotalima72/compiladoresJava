package com.mycompany.compiladores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import sun.util.locale.LocaleUtils;

/**
 *
 * @author jota
 */
public class Compiladores {

    public static ArrayList<Lexeme> reservados = new ArrayList<>();
    public static ArrayList<Lexeme> lexemes = new ArrayList<>();
    public static int countLinha = 1;
    public static boolean isAspasSimples = false;
    public static boolean isAspasDuplas = false;
    public static String idSocorro = "";
    public static boolean isComentario = false;

    public static void main(String[] args) throws IOException {

        String path = "reservados";//console.nextLine();
        leitor(path + ".txt");
//        reservados.forEach(lex -> {
//            System.out.println(lex.toString());
//        });
        System.out.println("Nome do arquivo: ");
        Scanner console = new Scanner(System.in);
        path = console.nextLine();
        try ( BufferedReader buffRead = new BufferedReader(new FileReader(path + ".DKS"))) {
            String line;
            while ((line = buffRead.readLine()) != null) {
                //System.out.println(line);
                countLinha++;
                lerAtomo(line);
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

    public static Lexeme cria_identificador(String identificador) {
        return null;
    }

    public static Lexeme lerAtomo(String linha) {
        int index;
        char letra;
        Lexeme lexeme = null;
        String identificador = "";
        for (index = 0; index < linha.length(); index++) {
            letra = linha.charAt(index);
            letra = Character.toUpperCase(letra);
            if (Character.isSpaceChar(letra) && !(identificador.length() > 0) && !checkSocorro()) {
            } else if (Character.isDigit(letra) && !(identificador.length() > 0) && !checkSocorro()) {
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
                                lexeme = cria_identificador(identificador);
                                return lexeme;
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
            } else if ((letra == '\'' && !(identificador.length() > 0)) && !checkSocorro()) {
                if (!isAspasSimples) {
                    identificador += letra;
                } else {
                    identificador = idSocorro;
                    idSocorro = "";
                    isAspasSimples = false;
                }
                boolean temChar = false;
                while (index < linha.length()) {
                    index++;
                    if (!(index < linha.length())) {
                        idSocorro = identificador;
                        isAspasSimples = true;
                        break;
                    }
                    letra = linha.charAt(index);
                    letra = Character.toUpperCase(letra);
//                    if (letra == '\n') {
//                        countLinha++;
//                    }
                    if (Character.isAlphabetic(letra) && !temChar) {
                        identificador += letra;
                        temChar = true;
                    } else if (letra == '\'') {
                        identificador += letra;
                        lexeme = cria_identificador(identificador);
                        return lexeme;
                    }
                }
            } else if (letra == '\"' && !(identificador.length() > 0)) {
                identificador += letra;
                while (index < linha.length()) {
                    index++;
                    if (!(index < linha.length())) {
                        break;
                    }
                    letra = linha.charAt(index);
                    letra = Character.toUpperCase(letra);

                    if (letra == '\n') {
                        countLinha++;
                    }
                    if (Character.isSpaceChar(letra) || Character.isLetterOrDigit(letra) || letra == '$' || letra == '_' || letra == '.') {
                        identificador += letra;
                    } else if (letra == '\"') {
                        identificador += letra;
                        lexeme = cria_identificador(identificador);
                        return lexeme;
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
//                                    posBarra = linha.charAt(index);
//                                    posBarra = Character.toUpperCase(posBarra);
//                                    if (letra[0] == 10) {
//                                        ( * countLinha)++;
//                                        break;
//                                    }
                                }
                            } else if (posBarra == '*') {
                                while (index < linha.length()) {
                                    index++;
                                    if (!(index < linha.length())) {
                                        isComentario = true;
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
                                lexeme = cria_identificador(identificador);
                                return lexeme;
                            }
                        }
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
                                break;
                            }
                            posDuplicavel = linha.charAt(index);
                            posDuplicavel = Character.toUpperCase(posDuplicavel);
                            if (posDuplicavel == '=') {
                                identificador += posDuplicavel;
                            } else {
                                index--;
                            }
                        }
                        lexeme = cria_identificador(identificador);
                        return lexeme;
                    }
                }
                if (Character.isSpaceChar(letra) || letra == '.') {
                    lexeme = cria_identificador(identificador);
                    return lexeme;
                } else if (!Character.isSpaceChar(letra) && aceita_punct(letra)) {
                    index--;
                    lexeme = cria_identificador(identificador);
                    return lexeme;
                }
            } else {
                identificador += letra;
            }
        }
        if ((identificador.length() > 0)) {
            lexeme = cria_identificador(identificador);
        }
        return lexeme;
    }

    public static boolean checkSocorro() {
        return (isAspasDuplas || isAspasSimples);
    }

    public boolean ispunct_duplicado(char character) {
        return character == '!' || character == '=' || character == '>' || character == '<';
    }

    public boolean aceita_punct(char punct) {
        return punct == '_' || punct == '!' || punct == '#' || punct == '=' || punct == '&' || punct == '(' || punct == ')' || punct == ';' || punct == '{' || punct == '}' || punct == '\"' || punct == '\'' || punct == '[' || punct == ']' || punct == ',' || punct == '<' || punct == '>' || punct == '%' || punct == '/' || punct == '|' || punct == '*' || punct == '+' || punct == '-';
    }
}
