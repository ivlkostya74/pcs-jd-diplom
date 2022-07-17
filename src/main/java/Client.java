import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String host = "127.0.0.1";
        int port = 8989;

        while (true) {
            try (Socket socket = new Socket(host, port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                System.out.println("1. поиск\n" + "0. выход");
                System.out.print("Выберите: ");
                String action = scanner.nextLine();
                if (action.equals("0")) {
                    System.out.println("«Hasta la vista»");
                    break;
                }
                System.out.print("Что ищем? ");
                String word = scanner.nextLine();
                out.println(word);

                while (true) {
                    String currentLine = in.readLine();
                    if (currentLine == null)
                        break;
                    System.out.println(currentLine);
                }
            }
        }
    }
}