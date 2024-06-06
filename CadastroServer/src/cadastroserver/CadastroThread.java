package cadastroserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import model.Produto;
import model.Usuario;

public class CadastroThread extends Thread {
    private Socket s1;
    private ProdutoJpaController ctrl;
    private UsuarioJpaController ctrlUsu;

    public CadastroThread(Socket s1, ProdutoJpaController ctrl, UsuarioJpaController ctrlUsu) {
        this.s1 = s1;
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(s1.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream())) {

            // Obter o login e a senha do cliente
            String login = (String) in.readObject();
            String senha = (String) in.readObject();

            // Verificar o login do usuário
            Usuario usuario = ctrlUsu.findUsuario(login, senha);
            if (usuario == null) {
                out.writeObject("Usuário não encontrado");
                return; // Finalizar a thread se o usuário não for encontrado
            }

            // Enviar resposta de login bem-sucedido para o cliente
            out.writeObject("Login bem-sucedido");

            // Loop para processar comandos do cliente
            while (true) {
                String command = (String) in.readObject();
                if (command.equals("L")) {
                    // Enviar lista de produtos para o cliente
                    List<Produto> produtos = ctrl.findProdutoEntities();
                    out.writeObject(produtos);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}