package cadastroclientv2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CadastroClientV2 {
    public static void main(String[] args) {
        try {
            // Conectar ao servidor
            Socket socket = new Socket("localhost", 4321);

            // Encapsular os canais de entrada e saída
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Escrever login e senha
            out.writeObject("op1");
            out.writeObject("op2");

            // Instanciar a janela para apresentação de mensagens
            SaidaFrame saidaFrame = new SaidaFrame();
            saidaFrame.setVisible(true);

            // Instanciar a Thread para preenchimento assíncrono
            ThreadClient threadClient = new ThreadClient(in, saidaFrame.texto);
            threadClient.start();

            // Encapsular a leitura do teclado
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Apresentar o menu
            String command;
            do {
                System.out.println("Menu:");
                System.out.println("L - Listar");
                System.out.println("E - Entrada");
                System.out.println("S - Saída");
                System.out.println("X - Finalizar");

                command = reader.readLine();
                out.writeObject(command);

                if (command.equals("E") || command.equals("S")) {
                    System.out.print("Id da pessoa: ");
                    int pessoaId = Integer.parseInt(reader.readLine());
                    out.writeObject(pessoaId);

                    System.out.print("Id do produto: ");
                    int produtoId = Integer.parseInt(reader.readLine());
                    out.writeObject(produtoId);

                    System.out.print("Quantidade: ");
                    int quantidade = Integer.parseInt(reader.readLine());
                    out.writeObject(quantidade);

                    System.out.print("Valor unitário: ");
                    double valorUnitario = Double.parseDouble(reader.readLine());
                    out.writeObject(valorUnitario);
                }
            } while (!command.equals("X"));

            // Fechar os streams e o socket
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}