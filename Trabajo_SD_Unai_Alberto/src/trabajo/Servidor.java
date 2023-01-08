package trabajo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main ( String [] args) {

        Socket s = null;
        Socket s2 = null;

        try(ServerSocket ss = new ServerSocket(5555)) {

            while (true){

                try {

                    //SE ACEPTA EL CLIENTE Y SE ABREN LOS STREAMS
                    s = ss.accept();
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    System.out.println("Acepto conexion 1");

                    //SE LEE LA OPCION ESCOGIDA
                    int opcion = in.readInt();

                    //SI LA OPCION ES 1
                    if (opcion == 1) {

                        //SE ACEPTA AL SEGUNDO CLIENTE Y SE ABREN LOS STREMAS
                        s2 = ss.accept();
                        ObjectInputStream in2 = new ObjectInputStream(s2.getInputStream());
                        ObjectOutputStream out2 = new ObjectOutputStream(s2.getOutputStream());
                        System.out.println("Acepto conexion 2");

                        //SE LEE LA OPCION ESCOGIDA POR EL SEGUNDO CLIENTE
                        opcion = in2.readInt();

                        //SE CREA UN HILO PARA 2 JUGADORES
                        TratarPeticion a = new TratarPeticion(in, out, in2, out2, s, s2);
                        a.start();

                    }

                    //SE CREA UN HILO PARA JUGADOR VS MAQUINA
                    else {
                        TratarPeticion a = new TratarPeticion(in, out, s);
                        a.start();
                    }


                } catch (IOException e){
                    e.printStackTrace();
                }


            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
