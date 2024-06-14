package cadastroserver;

import java.net.ServerSocket;
import java.net.Socket;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import java.io.IOException;

public class CadastroServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4321);
            System.out.println("Servidor iniciado na porta 4321");

            ProdutoJpaController produtoCtrl = new ProdutoJpaController();
            UsuarioJpaController usuarioCtrl = new UsuarioJpaController();
            MovimentoJpaController movimentoCtrl = new MovimentoJpaController();
            PessoaJpaController pessoaCtrl = new PessoaJpaController();

            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new CadastroThreadV2(socket, produtoCtrl, usuarioCtrl, movimentoCtrl, pessoaCtrl);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Adicionar log de erro
        }
    }
}