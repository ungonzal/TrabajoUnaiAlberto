package trabajo;

public class Tablero {

    private String[][] tablero;
    private int filas;
    private int columnas;
    private int turno;
    private char x = 'X';
    private char o = 'O';

    public Tablero() {

        this.filas = 3;
        this.columnas = 3;
        tablero = new String[filas][columnas];
        turno = 1;

    }

    public String[][] getTablero() {
        return tablero;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getFilas() {
        return filas;
    }

    public void iniciarTablero() {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = "_";
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }

    }

    public int getTurno() {
        return turno;
    }

    public int cambiarTurno() {

        if (turno == 1) {
            turno = 2;
        } else {
            turno = 1;
        }
        return turno;
    }

    public char colocarFicha() {

        if (turno == 1) {
            return x;
        } else {
            return o;
        }

    }

    public void imprimirTablero(String[][] tablero) {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                System.out.print(tablero[i][j] +" ");
            }
            System.out.println();
        }

    }

    public void setTurno(int turno) {
        this.turno=turno;
    }

}
