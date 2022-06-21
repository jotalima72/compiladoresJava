package com.mycompany.compiladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jota
 * @author Daniel
 */
public class Compiladores {

    public static ArrayList<Lexeme> reservados = new ArrayList<>();
    public static ArrayList<Lexeme> lexemes = new ArrayList<>();
    public static ArrayList<Atomo> simbolos = new ArrayList<>();
    public static int countLinha = 1;
    public static String identificador = "";
    public static int index = 0;
    public static boolean isComentario = false;
    public static boolean isAspasSimples = false;
    public static boolean isAspasDuplas = false;
    public static int linhaInicial = 0;

    public static void main(String[] args) throws IOException {

        String path = "reservados";//console.nextLine();
        carregar_reservados(path + ".txt");
//        reservados.forEach(lex -> {
//            System.out.println(lex.toString());
//        });
        System.out.println("Nome do arquivo: ");
        Scanner console = new Scanner(System.in);
        path = console.nextLine();
        try ( BufferedReader buffRead = new BufferedReader(new FileReader(path + ".DKS"))) {
            String linha;
            identificador = "";
            char letra;
            while ((linha = buffRead.readLine()) != null) {

                for (index = 0; index < linha.length(); index++) {
                    letra = linha.charAt(index);
                    letra = Character.toUpperCase(letra);
                    boolean criaId = lerAtomo(letra, linha);
                    if (criaId) {
                        preenche_listas();
                    }
                }
                if (identificador.length() > 0 && !isAspasDuplas && !isAspasSimples) {
                    preenche_listas();
                }
                countLinha++;
            }
            lexemes.forEach(lex -> {
                int indice = indice(lex);
                if (indice > 0) {
                    System.out.println(indice + "\t" + lex.toString()+"\n");
                } else {
                    System.out.println("-\t" + lex.toString()+"\n");
                }
            });

            simbolos.forEach(simba -> {
                System.out.println(simba.toString());
                System.out.println("=======================");
            });
        }
        arquivo_lexico(path);
        arquivo_tab(path);
    }

    public static Atomo truncador(Lexeme lexeme) {
        Atomo atomo = new Atomo();
        atomo.setCodigo(lexeme.getCodigo());
        atomo.setIdentificador(lexeme.getIdentificador());
        atomo.setTamanho(atomo.getIdentificador().length());
        atomo.setTamTrucado(atomo.getIdentificador().length());
        atomo.setTipo(retorna_tipo(atomo.getCodigo()));

        if (atomo.getIdentificador().length() >= 35) {
            String id = atomo.getIdentificador();
            String auxId = "";
            switch (atomo.getTipo()) {
                case "STR":
                    for (int i = 0; i < 34; i++) {
                        auxId += id.charAt(i);
                    }
                    auxId += "\"";
                    break;
                case "INT":
                    for (int i = 0; i < 35; i++) {
                        auxId += id.charAt(i);
                    }
                    break;
                case "PFO":
                    int tam = 35;
                    if (id.indexOf(".") == 34) {
                        tam = 34;
                    }
                    for (int i = 0; i < tam; i++) {
                        auxId += id.charAt(i);
                    }
                    if (!auxId.contains(".")) {
                        identificador = auxId;
                        Lexeme lex = cria_identificador();
                        atomo.setCodigo(lex.getCodigo());
                        atomo.setTipo(retorna_tipo(lex.getCodigo()));
                    }
                    break;
                case "VOI":
                    for (int i = 0; i < 35; i++) {
                        auxId += id.charAt(i);
                    }
                    identificador = auxId;
                    Lexeme lex = cria_identificador();
                    atomo.setCodigo(lex.getCodigo());
                    atomo.setTipo(retorna_tipo(lex.getCodigo()));
                    break;
            }
            atomo.setIdentificador(auxId);
            atomo.setTamTrucado(auxId.length());
        }
        return atomo;
    }

    public static int indice(Lexeme lex) {
        for (Atomo simbolo : simbolos) {
            if (simbolo.getIdentificador().contentEquals(lex.getIdentificador())) {
                return simbolos.indexOf(simbolo) + 1;
            }
        }
        return -1;
    }

    public static String retorna_tipo(String cod) {
        switch (cod) {
            case "ID02":
                return "STR";
            case "ID03":
                return "INT";
            case "ID05":
                return "CHC";
            case "ID06":
                return "PFO";
            default:
                return "VOI";
        }
    }

    public static void carregar_reservados(String path) throws IOException {
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
            buffRead.close();
        }
    }

    public static void arquivo_lexico(String path) throws IOException {
        try ( BufferedWriter buffWriter = new BufferedWriter(new FileWriter(path+".LEX"))) {
            String cabecalho = "*************************************************************\n"+
                               "*\t EQUIPE 03                                    \n"+
                               "*\tAluno Daniel Barbosa Bastos - (75) 99808-5390 \n"+
                               "*\tEmail daniel.b@aln.senaicimatec.edu.br        \n"+
                               "*\tAluno Joao Pedro de Lima O. - (74) 99963-3047 \n"+
                               "*\tEmail joao.o@aln.senaicimatec.edu.br          \n"+
                               "*************************************************************\n"+
                               "\n\n"+
                               "=============================================================\n"+
                               "INDICE\tCODIGO\t\tLEXEME\n"+
                               "=============================================================\n";
            buffWriter.append(cabecalho);
            lexemes.forEach(lex -> {
                int indice = indice(lex);
                if (indice > 0) {
                    try {
                        buffWriter.append(indice + "\t" + lex.toString()+"\n");
                    } catch (IOException ex) {
                        Logger.getLogger(Compiladores.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        buffWriter.append("-\t" + lex.toString()+"\n");
                    } catch (IOException ex) {
                        Logger.getLogger(Compiladores.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            buffWriter.append("=============================================================\n");
            
            buffWriter.close();
        }
    }
    
    public static void arquivo_tab(String path) throws IOException {
        try ( BufferedWriter buffWriter = new BufferedWriter(new FileWriter(path+".TAB"))) {
            String cabecalho = "*************************************************************\n"+
                               "*\t EQUIPE 03                                    \n"+
                               "*\tAluno Daniel Barbosa Bastos - (75) 99808-5390 \n"+
                               "*\tEmail daniel.b@aln.senaicimatec.edu.br        \n"+
                               "*\tAluno Joao Pedro de Lima O. - (74) 99963-3047 \n"+
                               "*\tEmail joao.o@aln.senaicimatec.edu.br          \n"+
                               "*************************************************************\n"+
                               "\n\n"+
                               "============================================================================\n"+
                               "INDICE\tCODIGO\tTIPO\tTAM\tTRUNC\tLINHAS\t\tLEXEME\n"+
                               "============================================================================\n";
            buffWriter.append(cabecalho);
            simbolos.forEach(simbolo -> {
                Lexeme lex = new Lexeme(simbolo.getCodigo(), simbolo.getIdentificador());
                int indice = indice(lex);
                    try {
                        buffWriter.append(indice + "\t" + simbolo.toString()+"\n");
                    } catch (IOException ex) {
                        Logger.getLogger(Compiladores.class.getName()).log(Level.SEVERE, null, ex);
                    }
            });
            buffWriter.append("============================================================================\n");
            
            buffWriter.close();
        }
    }

    public static void preenche_listas() {
        Lexeme lex = cria_identificador();
        Atomo simbolo = truncador(lex);
        lex.setCodigo(simbolo.getCodigo());
        lex.setIdentificador(simbolo.getIdentificador());
        lexemes.add(lex);
        if (lex.getCodigo().contains("ID")) {
            int indice = indice(lex);
            if (indice < 0) {
                int tamanho = simbolo.getTamanho();
                int tamTrunc = simbolo.getTamTrucado();
                String tipo = simbolo.getTipo();
                int linha = countLinha;
                if (linhaInicial > 0) {
                    linha = linhaInicial;
                    linhaInicial = 0;
                }
                simbolo = new Atomo(lex.getCodigo(), lex.getIdentificador(), tamanho, tamTrunc, tipo, linha);
                simbolos.add(simbolo);
            } else {
                Atomo s = simbolos.remove(indice - 1);
                int linha = countLinha;
                if (linhaInicial > 0) {
                    linha = linhaInicial;
                    linhaInicial = 0;
                }
                s.setLinhas(linha);
                if (s.getTamanho() != simbolo.getTamanho()) {
                    s.setTamanho(simbolo.getTamanho());
                }
                simbolos.add(indice - 1, s);
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
        } else if (((letra == '\'' && !(identificador.length() > 0)) && !isComentario && !isAspasDuplas) || isAspasSimples) {
            boolean temChar = false;
            if (!isAspasSimples) {
                identificador += letra;
                isAspasSimples = true;
                linhaInicial = countLinha;
            }
            if (identificador.length() > 1) {
                temChar = true;
            }
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
                    isAspasSimples = false;
                    return true;
                }
            }
        } else if ((letra == '\"' && !(identificador.length() > 0) && !isComentario && !isAspasSimples) || isAspasDuplas) {
            if (!isAspasDuplas) {
                identificador += letra;
                isAspasDuplas = true;
                linhaInicial = countLinha;
            }
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
                    isAspasDuplas = false;
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
