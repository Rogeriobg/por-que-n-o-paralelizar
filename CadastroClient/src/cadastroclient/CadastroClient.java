package cadastroclient;

import java.io.*;
import static java.lang.System.out;
import java.net.*;
import java.util.List;
import model.Produto;

public class CadastroClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4321);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Enviar login e senha para o servidor
            out.writeObject("op1"); // Substituir "op1" pelo login real
            out.flush();
            out.writeObject("op2"); // Substituir "op1" pela senha real
            out.flush();

            // Receber resposta de login do servidor
            String loginResponse = (String) in.readObject();
            if (!loginResponse.equals("Usuário não encontrado")) {
                // Enviar comando 'L' para solicitar a lista de produtos
                out.writeObject("L");
                out.flush();

                // Receber lista de produtos do servidor
                List<Produto> produtos = (List<Produto>) in.readObject();
                for (Produto produto : produtos) {
                    System.out.println(produto.getNome());
                }
            } else {
                System.out.println(loginResponse);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Fechar os fluxos de saída
            // Isso garante que os recursos sejam liberados corretamente
            // Mesmo que ocorra uma exceção durante a execução do código
            out.close();
        }
    }
}