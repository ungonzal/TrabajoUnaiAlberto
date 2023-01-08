package trabajo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cliente {

    private static Tablero t = new Tablero();
    private static String[][] tablero = null;

    private static int numJudador;

    private static Scanner sc = new Scanner(System.in);

    private static Socket s;

    public static void main(String[] args) {

        try {

            s = new Socket("localhost", 5555);

            //SE MUESTRA AL USUARIO LAS DIVERSAS OPCIONES
            int opcion=3;
            while(opcion!=1&&opcion!=2) {
                System.out.println("ELEGIR MODO DE JUEGO\r\n\r\n");
                System.out.println("Opcion 1:  VS jugador (online)");
                System.out.println("Opcion 2:  VS maquina");
                //SE RECOGE LA OPCIÓN ESCOGIDA Y SE INICIA EL TABLERO
                try {
                    Scanner cin=new Scanner(System.in);
                    opcion=cin.nextInt();
                }
                catch(InputMismatchException n) {
                    System.err.println("No ha introducido un numero\r\n");
                }
            }
            t.iniciarTablero();
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            if (opcion == 1) {
                System.out.println("Esperando a otro jugador");
            }
            //SE ENVIA LA OPCION ESCOGIDA
            out.writeInt(opcion);
            out.flush();
            //SE OBTIENE EL TABLERO Y SE ENVIA
            tablero = t.getTablero();
            out.writeObject(t.getTablero());
            out.flush();
            //SE LEE EL TURNO Y SE INFORMA AL JUGADOR
            t.setTurno(in.readInt());
            System.out.println("Usted es el Jugador " + t.getTurno());
            numJudador = t.getTurno();

            //SE ELIGE EL MODO EN EL QUE SE EJECUTA DEPENDIENDO DE LA OPCION ESCOGIDA
            switch (opcion) {
                case 1:
                    jugarDos(in, out);
                    break;
                case 2:
                    JugarMaquina(in, out);
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace() ;
        }

    }

    //METODO PARA JUGAR 2 JUGADORES
    private static void jugarDos(InputStream in, OutputStream out) {

        boolean ganado = false;
        int fila;
        int columna;
        boolean ocupada = false;
        boolean jugando = true;

        try {

            //MIENTRAS QUE SE ESTE JUGANDO
            while (jugando) {

                //SE LEE SI SE HA GANADO
                ganado = ((ObjectInputStream) in).readBoolean();

                //SI NO SE HA GANADO SE ENTRA
                if(!ganado){

                    //SE LEE EL TABLERO
                    for (int i = 0; i < tablero.length; i++) {
                        for (int j = 0; j < tablero[i].length; j++) {
                            tablero[i][j] = ((ObjectInputStream) in).readUTF();
                        }
                    }

                    //SE MUESTRA EL TABLERO Y SE MUESTRA TAMBIÉN A QUE JUGADOR PERTENECE
                    System.out.println();
                    System.out.println("Tablero del Jugador " + t.getTurno());
                    System.out.println();
                    t.imprimirTablero(tablero);

                    //SE PREGUNTA POR LA FILA Y LA COLUMNA QUE SE QUIERE INTRODUCIR
                    System.out.println("Introduzca la fila");
                    fila = sc.nextInt();
                    System.out.println("Introduzca la columna");
                    columna = sc.nextInt();

                    //SE ENVIA LA COLUMNA Y LA FILA QUE SE HAN ELEGIDO
                    ((ObjectOutputStream) out).writeInt(fila);
                    ((ObjectOutputStream) out).writeInt(columna);
                    out.flush();

                    //SE LEE SI LA POSICION ESTA OCUPADA O NO
                    ocupada = ((ObjectInputStream) in).readBoolean();

                    //SI NO ESTA OCUPADA SE ENTRA
                    if (!ocupada) {

                        //SE LEE EL TABLERO MODIFICADO
                        for (int i = 0; i < tablero.length; i++) {
                            for (int j = 0; j < tablero[i].length; j++) {
                                tablero[i][j] = ((ObjectInputStream) in).readUTF();
                            }
                        }

                        //SE MUESTRA EL TABLERO Y SE CAMBIA EL TURNO
                        t.imprimirTablero(tablero);
                        t.cambiarTurno();

                    }

                    //SI ESTA OCUPADA SE INFORMA DE QUE LA POSICION ESTA OCUPADA Y SE VUELVE AL BUCLE
                    else {
                        System.out.println("La posicion (" + fila + ", " + columna + ") ya esta ocupada");
                    }

                }

                //SI YA SE HA GANADO SE PONE JUGANDO A FALSE PORQUE SE HA TERMINADO DE JUGAR
                else {
                    jugando = false;
                }

            }

            //SE LEE EL ULTIMO TABLERO
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[i].length; j++) {
                    tablero[i][j] = ((ObjectInputStream) in).readUTF();
                }
            }

            //SE IMPRIME EL TABLERO Y MUESTRA POR PANTALLA SI SE HA GANADO O SE HA PERDIDO
            System.out.println();
            System.out.println();
            t.imprimirTablero(tablero);
            System.out.println();
            System.out.println();
            System.err.println(((ObjectInputStream) in).readUTF());
            System.out.println();
            System.out.println();

            //SE PREGUNTA AL USUARIO SI DESEA VOLVER A JUGAR
            int v=2;
            while(v!=0 && v!=1) {
                try {
                    System.out.println("Desea volver a jugar? 1 SI, 0 NO\r\n\r\n");
                    Scanner cin=new Scanner(System.in);
                    v=cin.nextInt();
                    if (v == 1) { 				//SI ES 1 EL PROGRAMA SE REINICIA CON main(null)
                        main(null);
                    } else if(v==0){
                        s.close();
                        System.err.println("Usted ha salido");
                    }
                }
                catch(InputMismatchException n) {
                    System.err.println("No ha introducido un numero\r\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace() ;
        }

    }

    //METODO PARA JUGAR VS MAQUINA
    private static void JugarMaquina(InputStream in, OutputStream out) {

        boolean ganado = false;
        int fila;
        int columna;
        boolean ocupada = false;
        boolean jugando = true;

        try {

            //MIENTRAS QUE SE ESTÉ JUGANDO
            while (jugando) {

                //SE LEE SI SE HA GANADO O NO
                ganado = ((ObjectInputStream) in).readBoolean();

                //SI NO SE HA GANADO
                if(!ganado){

                    //SI EL TURNO NO ES 2 (TURNO HUMANO)
                    if (t.getTurno() != 2) {

                        //ELECCION DE FILA Y COLUMNA
                        System.out.println("Introduzca la fila");
                        fila = sc.nextInt();
                        System.out.println("Introduzca la columna");
                        columna = sc.nextInt();

                        //SE ENVIAN LA ELECCION DE FILA Y DE COLUMNA
                        ((ObjectOutputStream) out).writeInt(fila);
                        ((ObjectOutputStream) out).writeInt(columna);
                        out.flush();

                    }

                    //SI EL TURNO ES 2 (TURNO MAQUINA)
                    else {

                        System.out.println();
                        System.out.println("Turno de la Maquina\r\n\r\n");

                        //ELECCION DE FILA Y COLUMNA ALEATORIAS
                        fila = (int)((Math.random()*3));
                        columna = (int)((Math.random()*3));

                        //SE ENVIAN LA FILA Y LA COLUMNA
                        ((ObjectOutputStream) out).writeInt(columna);
                        ((ObjectOutputStream) out).writeInt(fila);
                        out.flush();

                    }

                    //SE RECIBE LA INFO DE SI LA POSICIÓN ESTÁ OCUPADA O NO
                    ocupada = ((ObjectInputStream) in).readBoolean();

                    //SI NO ESTA OCUPADA
                    if (!ocupada) {

                        //SE LEE EL TABLERO QUE SE ACABA DE MOSIFICAR
                        for (int i = 0; i < tablero.length; i++) {
                            for (int j = 0; j < tablero[i].length; j++) {
                                tablero[i][j] = ((ObjectInputStream) in).readUTF();
                            }
                        }

                        //SE IMPRIME EL TABLERO Y SE CAMBIA EL TURNO
                        t.imprimirTablero(tablero);
                        t.cambiarTurno();

                    }

                    //SI ESTA OCUPADA
                    else {
                        System.out.println("La posicion (" + fila + ", " + columna + ") ya esta ocupada");
                    }

                }

                //CUANDO YA SE RECIBE QUE SE HA GANADO, SE TERMINA DE JUGAR
                else {
                    jugando = false;
                }

            }

            //SE PREGUNTA AL USUARIO SI DESEA VOLVER A JUGAR
            System.out.println("La partida ha finalizado");
            int v=2;
            while(v!=0 && v!=1) {
                try {
                    System.out.println("Desea volver a jugar? 1 SI, 0 NO\r\n\r\n");
                    Scanner cin=new Scanner(System.in);
                    v=cin.nextInt();
                    if (v == 1) { 				//SI ES 1 EL PROGRAMA SE REINICIA CON main(null)
                        main(null);
                    } else if(v==0){
                        s.close();
                        System.err.println("Usted ha salido");
                    }
                }
                catch(InputMismatchException n) {
                    System.err.println("No ha introducido un numero\r\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
