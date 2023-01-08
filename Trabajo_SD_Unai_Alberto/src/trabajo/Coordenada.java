package trabajo;

import java.util.Scanner;

public class Coordenada {

    private static int fila;
    private static int columna;

    //LEER LAS COORDENADAS DE UN JUGADOR
    public static void leerCoordenada() {

        Scanner sc = new Scanner(System.in);

        System.out.println("Introduzca la fila");
        fila = sc.nextInt();

        System.out.println("Introduzca la columna");
        columna = sc.nextInt();

    }

    //LEER LAS COORDENADAS DE LA MAQUINA
    public static void leerCoordenada2() {

        fila = (int)((Math.random()*3));
        columna = (int)((Math.random()*3));
        System.out.println(fila + " " + columna);

    }


    public static int getFila() {
        return fila;
    }

    public static int getColumna() {
        return columna;
    }

    public static boolean comprobarColumnas(String[][] aux) {

        boolean contadorX = false;
        boolean contadorO = false;
        boolean ganador = false;

        //BUCLE PARA COMPROBAR TODAS LAS POSICIONES
        for (int i = 0; i < aux.length; i++) {
            for (int j = 0; j < aux[i].length; j++) {

                //SE COMPRUEBAN LAS COLUMNAS X
                if (aux[0][j].equals("X")) {
                    if (aux[1][j].equals("X")) {
                        if (aux[2][j].equals("X")) {

                            contadorX = true;
                            ganador = contadorX;

                        }
                    }
                }

                //SE COMPRUEBAN LAS COLUMNAS O
                else if (aux[0][j].equals("O")) {
                    if (aux[1][j].equals("O")) {
                        if (aux[2][j].equals("O")) {

                            contadorO = true;
                            ganador = contadorO;

                        }
                    }
                }
            }
        }

        //SE DEVUELVE SI SE HA GANADO O NO
        return ganador;

    }

    public static boolean comprobarFilas(String[][] aux) {

        boolean contadorX = false;
        boolean contadorO = false;
        boolean ganador = false;

        //BUCLE PARA COMPROBAR TODAS LAS POSICIONES
        for (int i = 0; i < aux.length; i++) {
            for (int j = 0; j < aux[i].length; j++) {

                //SE COMPRUEBAN LAS FILAS X
                if (aux[j][0].equals("X")) {
                    if (aux[j][1].equals("X")) {
                        if (aux[j][2].equals("X")) {

                            contadorX = true;
                            ganador = contadorX;

                        }
                    }
                }

                //SE COMPRUEBAN LAS FILAS O
                else if (aux[j][0].equals("O")) {
                    if (aux[j][1].equals("O")) {
                        if (aux[j][2].equals("O")) {

                            contadorO = true;
                            ganador = contadorO;

                        }
                    }
                }
            }
        }

        //SE DEVUELVE SI SE HA GANADO O NO
        return ganador;

    }

    public static boolean comprobarDiagonal(String[][] aux) {

        boolean contadorX = false;
        boolean contadorO = false;
        boolean ganador = false;

        //SE COMPRUEBAN LA DIAGONAL X
        if (aux[0][0].equals("X")) {
            if (aux[1][1].equals("X")) {
                if (aux[2][2].equals("X")) {

                    contadorX = true;
                    ganador = contadorX;

                }
            }
        }

        //SE COMPRUEBAN LA DIAGONAL O
        else if (aux[0][0].equals("O")) {
            if (aux[1][1].equals("O")) {
                if (aux[2][2].equals("O")) {

                    contadorO = true;
                    ganador = contadorO;

                }
            }
        }

        //SE DEVUELVE SI SE HA GANADO O NO
        return ganador;

    }

    public static boolean comprobarDiagonalInversa(String[][] aux) {

        boolean contadorX = false;
        boolean contadorO = false;
        boolean ganador = false;

        //SE COMPRUEBAN LA DIAGONAL X
        if (aux[2][0].equals("X")) {
            if (aux[1][1].equals("X")) {
                if (aux[0][2].equals("X")) {

                    contadorX = true;
                    ganador = contadorX;

                }
            }
        }

        //SE COMPRUEBAN LA DIAGONAL O
        else if (aux[2][0].equals("O")) {
            if (aux[1][1].equals("O")) {
                if (aux[0][2].equals("O")) {

                    contadorO = true;
                    ganador = contadorO;

                }
            }
        }

        //SE DEVUELVE SI SE HA GANADO O NO
        return ganador;
    }

}
