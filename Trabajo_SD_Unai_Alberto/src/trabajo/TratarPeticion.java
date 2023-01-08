package trabajo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TratarPeticion extends Thread{

    private static String[][] tablero = null;
    private String[][] tablero2 = null;
    private static boolean[][] ocupadas = null;
    private static boolean ganado = false;
    private static int fila;
    private static int columna;
    private static boolean ocupada = false;

    Socket s = null;
    InputStream in;
    OutputStream out;
    Socket s2 = null;
    InputStream in2;
    OutputStream out2;

    static Tablero t = new Tablero();

    //CONSTRUCTOR PARA 2 CLIENTES
    public TratarPeticion(InputStream in, OutputStream out, InputStream in2, OutputStream out2, Socket s, Socket s2) {

        this.in = in;
        this.out = out;

        this.in2 = in2;
        this.out2 = out2;

        this.s = s;
        this.s2 = s2;

    }

    //CONSTRUCTOR PARA 1 CLIENTE
    public TratarPeticion(InputStream in, OutputStream out, Socket s) {

        this.in = in;
        this.out = out;
        this.s = s;

    }

    //METODO RUN
    public void run() {

        try {

            //SE CREA EL VECTOR DE OCUPADAS
            ocupadas = new boolean[3][3];

            //SI HAY 2 CLIENTES
            if (s2 != null) {

                //SE LEE EL TABLERO DEL PRIMERO
                try {
                    tablero = (String[][]) ((ObjectInputStream) in).readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //SE LEE EL TABLERO DEL SEGUNDO
                try {
                    tablero2 = (String[][]) ((ObjectInputStream) in2).readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //SE ESCRIBE AL PRIMER CLIENTE QUE EL ES EL 1
                ((ObjectOutputStream) out).writeInt(1);
                out.flush();
                //SE ESCRIBE AL SEGUNDO CLIENTE QUE EL ES EL 2
                ((ObjectOutputStream) out2).writeInt(2);
                out.flush();

                //SE INVOCA AL METODO PARA JUGAR 2 CLIENTES
                jugarDos(in, out, in2, out2);

            }

            //SI HAY 1 CLIENTE
            else {

                //SE LEE EL TABLERO DEL PRIMER CLIENTE
                try {
                    tablero = (String[][]) ((ObjectInputStream) in).readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //SE ESCRIBE AL CLIENTE QUE EL ES EL PIRMER CLIENTE
                ((ObjectOutputStream) out).writeInt(1);
                out.flush();

                //SE INVOCA AL METODO PARA JUGAR VS MAQUINA
                jugarMaquina(in, out);

            }
            s.close();

            if (s2 != null) {
                s2.close();

            }

        } catch(IOException e) {
            e.printStackTrace() ;
        }

    }

    //METODO PARA JUGAR 2 CLIENTES
    private static void jugarDos(InputStream in, OutputStream out, InputStream in2, OutputStream out2) {

        int contador = 0;
        t.setTurno(1);

        try {

            //SE COMPRUEBA QUE NO SE HA TERMINADO
            while (!Coordenada.comprobarColumnas(tablero) && !Coordenada.comprobarFilas(tablero)
                    && !Coordenada.comprobarDiagonal(tablero) && !Coordenada.comprobarDiagonalInversa(tablero) && contador < 9 ) {

                //SI ES EL TURNO DEL PRIMER CLIENTE
                if (t.getTurno() == 1) {

                    //SE ENVIA AL PRIMER CLIENTE QUE NO SE HA GANADO AUN
                    ganado = false;
                    ((ObjectOutputStream) out).writeBoolean(ganado);
                    out.flush();

                    //SE LE ENVIA EL TABLERO
                    for (int i = 0; i < tablero.length; i++) {
                        for (int j = 0; j < tablero[i].length; j++) {
                            ((ObjectOutputStream) out).writeUTF(tablero[i][j]);
                            out.flush();
                        }
                    }

                    //SE LEE LA FILA Y LA COLUMNA QUE HA ESCOGIDO
                    fila = ((ObjectInputStream) in).readInt();
                    columna = ((ObjectInputStream) in).readInt();

                    //SE COMPRUEBA QUE NO ESTEN OCUPADAS, SI NO LO ESTAN SE OCUPA ESA POSICION Y SE ENVIA AL CLIENTE QUE NO ESTABA OCUPADA
                    if (!ocupadas[fila][columna]) {
                        tablero[fila][columna] = t.colocarFicha() + "";
                        ocupadas[fila][columna] = true;
                        ocupada = false;
                        ((ObjectOutputStream) out).writeBoolean(ocupada);
                        out.flush();

                        //SE ENVIA AL CLIENTE EL TABLERO
                        for (int i = 0; i < tablero.length; i++) {
                            for (int j = 0; j < tablero[i].length; j++) {
                                ((ObjectOutputStream) out).writeUTF(tablero[i][j]);
                                out.flush();
                            }
                        }

                        //SE SUMA 1 AL CONTADOR Y SE CAMBIA EL TURNO
                        contador++;
                        t.cambiarTurno();

                    }

                    //SI ESTA OCUPADA SE ENVIA AL CLIENTE QUE LA POSICION ESTABA OCUPADA
                    else {

                        ocupada = true;
                        ((ObjectOutputStream) out).writeBoolean(ocupada);
                        out.flush();

                    }

                }

                //MISMO PROCESO ANTERIOR PARA EL SEGUNDO CLIENTE
                else {

                    ganado = false;

                    ((ObjectOutputStream) out2).writeBoolean(ganado);
                    out2.flush();

                    for (int i = 0; i < tablero.length; i++) {
                        for (int j = 0; j < tablero[i].length; j++) {
                            ((ObjectOutputStream) out2).writeUTF(tablero[i][j]);
                            out2.flush();
                        }
                    }

                    fila = ((ObjectInputStream) in2).readInt();
                    columna = ((ObjectInputStream) in2).readInt();

                    if (!ocupadas[fila][columna]) {
                        tablero[fila][columna] = t.colocarFicha() + "";
                        ocupadas[fila][columna] = true;

                        ocupada = false;
                        ((ObjectOutputStream) out2).writeBoolean(ocupada);
                        out2.flush();

                        for (int i = 0; i < tablero.length; i++) {
                            for (int j = 0; j < tablero[i].length; j++) {
                                ((ObjectOutputStream) out2).writeUTF(tablero[i][j]);
                                out2.flush();
                            }
                        }

                        contador++;
                        t.cambiarTurno();

                    } else {

                        ocupada = true;
                        ((ObjectOutputStream) out2).writeBoolean(ocupada);
                        out2.flush();

                    }
                }
            }

            //CUANDO SE HA FINALIZADO SE ENVIA A LOS CLIENTES QUE YA SE HA ACABADO EL JUEGO
            ganado = true;
            ((ObjectOutputStream) out).writeBoolean(ganado);
            out.flush();
            ((ObjectOutputStream) out2).writeBoolean(ganado);
            out2.flush();

            //SE LES ENVIA EL TABLERO FINAL
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[i].length; j++) {
                    ((ObjectOutputStream) out).writeUTF(tablero[i][j]);
                    out.flush();
                    ((ObjectOutputStream) out2).writeUTF(tablero[i][j]);
                    out2.flush();
                }
            }

            //SE COMPRUEBA SI HA SIDO EMPATE SI ES ASI SE LES ECRIBE QUE HA SIDO EMPATE
            if (ganado && contador == 9) {
                System.out.println();
                ((ObjectOutputStream) out).writeUTF("EMPATE");
                out.flush();
                ((ObjectOutputStream) out2).writeUTF("EMPATE");
                out2.flush();
            }

            //SI NO HA SIDO EMPATE
            else {
                System.out.println();
                System.out.println();

                //SI HA GANADO EL JUGADOR 1 SE LE DICE QUE EL ES EL GANADOR Y AL CLIENTE 2 QUE EL ES EL PERDEDOR
                if (t.getTurno() == 1) {
                    ((ObjectOutputStream) out).writeUTF("Ha ganado el jugador " + t.getTurno());
                    out.flush();
                    t.cambiarTurno();
                    ((ObjectOutputStream) out2).writeUTF("Ha perdido el jugador " + t.getTurno());
                    out2.flush();
                }
                //SI HA GANADO EL JUGADOR 2 SE LE DICE QUE EL ES EL GANADOR Y AL CLIENTE 1 QUE EL ES EL PERDEDOR
                else {
                    ((ObjectOutputStream) out).writeUTF("Ha perdido el jugador " + t.getTurno());
                    out.flush();
                    t.cambiarTurno();
                    ((ObjectOutputStream) out2).writeUTF("Ha ganado el jugador " + t.getTurno());
                    out2.flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //METODO DE 1 JUGADOR VS MAQUINA
    private static void jugarMaquina(InputStream in, OutputStream out) {

        int contador = 0;
        t.setTurno(1);

        try {

            //SE COMPRUEBA QUE NO SE HA LLEGADO AL FINAL
            while (!Coordenada.comprobarFilas(tablero) && !Coordenada.comprobarColumnas(tablero)
                    && !Coordenada.comprobarDiagonal(tablero) && !Coordenada.comprobarDiagonalInversa(tablero) && contador < 9 ) {

                //SE ENVIA AL CLIENTE QUE NO SE HA GANADO AUN
                ganado = false;
                ((ObjectOutputStream) out).writeBoolean(ganado);
                out.flush();

                //SE LEEN LA COLUMNA Y LA FILA QUE HA ESCOGIDO EL CLIENTE
                fila = ((ObjectInputStream) in).readInt();
                columna = ((ObjectInputStream) in).readInt();

                //SE COMPRUEBA QUE NO ESTEN OCUPADAS, SI NO LO ESTAN SE OCUPA ESA POSICION Y SE ENVIA AL CLIENTE QUE NO ESTABA OCUPADA
                if (!ocupadas[fila][columna]) {
                    tablero[fila][columna] = t.colocarFicha() + "";
                    ocupadas[fila][columna] = true;
                    ocupada = false;
                    ((ObjectOutputStream) out).writeBoolean(ocupada);
                    out.flush();

                    //SE ENVIA EL TABLERO ACTUALIZADO AL CLIENTE
                    for (int i = 0; i < tablero.length; i++) {
                        for (int j = 0; j < tablero[i].length; j++) {
                            ((ObjectOutputStream) out).writeUTF(tablero[i][j]);
                            out.flush();
                        }
                    }

                    //SE AUMENTA EL CONTADOR Y SE CAMBIA EL TURNO
                    t.cambiarTurno();
                    contador++;

                }

                //SI ESTABA LA POSICION OCUPADA SE LE AVISA AL CLIENTE
                else {

                    ocupada = true;
                    ((ObjectOutputStream) out).writeBoolean(ocupada);
                    out.flush();

                }

            }

            //CUANDO SE HA LLEGADO AL FINAL SE LE AVISA AL CLIENTE
            ganado = true;
            ((ObjectOutputStream) out).writeBoolean(ganado);
            out.flush();

            //SE COMPRUEBA SI HA SIDO EMPATE
            if (ganado && contador == 9) {

                ((ObjectOutputStream) out).writeUTF("EMPATE");
                out.flush();

            }

            //SI NO HA SIDO EMPATE SE INFORMA DE QUE JUGADOR HA GANADO
            else {

                ((ObjectOutputStream) out).writeUTF("Ha ganado el jugador " + t.getTurno());
                out.flush();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
