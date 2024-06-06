/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import java.io.IOException;
import java.net.ServerSocket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import java.net.Socket;

public class CadastroServer {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
        ProdutoJpaController produtoCtrl = new ProdutoJpaController(emf);
        UsuarioJpaController usuarioCtrl = new UsuarioJpaController(emf);

        try (ServerSocket serverSocket = new ServerSocket(4321)) {
            System.out.println("Servidor iniciado na porta 4321");

            while (true) {
                Socket socket = serverSocket.accept();
                new CadastroThread(socket, produtoCtrl, usuarioCtrl).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}