package cadastroserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import model.Produto;
import model.Usuario;
import model.Movimento;

public class CadastroThreadV2 extends Thread {
    private Socket s1;
    private ProdutoJpaController ctrlProd;
    private UsuarioJpaController ctrlUsu;
    private MovimentoJpaController ctrlMov;
    private PessoaJpaController ctrlPessoa;

    public CadastroThreadV2(Socket s1, ProdutoJpaController ctrlProd, UsuarioJpaController ctrlUsu, MovimentoJpaController ctrlMov, PessoaJpaController ctrlPessoa) {
        this.s1 = s1;
        this.ctrlProd = ctrlProd;
        this.ctrlUsu = ctrlUsu;
        this.ctrlMov = ctrlMov;
        this.ctrlPessoa = ctrlPessoa;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s1.getInputStream())) {

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
                    List<Produto> produtos = ctrlProd.findProdutoEntities();
                    out.writeObject(produtos);
                } else if (command.equals("E") || command.equals("S")) {
                    char tipoMovimento;
                    if (command.equals("E")) {
                        tipoMovimento = 'E'; // Se o comando for "E", o tipo de movimento será "E" (entrada)
                    } else {
                        tipoMovimento = 'S'; // Se o comando for "S", o tipo de movimento será "S" (saída)
                    }

                    // Processar entrada ou saída de produtos
                    Movimento movimento = new Movimento();
                    movimento.setUsuario(usuario);
                    movimento.setTipo(tipoMovimento);

                    // Receber e configurar os dados do movimento
                    int pessoaId = (Integer) in.readObject();
                    int produtoId = (Integer) in.readObject();
                    int quantidade = (Integer) in.readObject();
                    double valorUnitario = (Double) in.readObject();

                    movimento.setPessoaID(ctrlPessoa.findPessoa(pessoaId));
                    Produto produto = ctrlProd.findProduto(produtoId);
                    movimento.setProdutoID(produto);
                    movimento.setQuantidade(quantidade);
                    movimento.setPrecoUnitario(BigDecimal.valueOf(valorUnitario));

                    // Persistir o movimento
                    ctrlMov.create(movimento);

                    // Atualizar a quantidade de produtos
                    if (command.equals("E")) {
                        produto.setQuantidade(produto.getQuantidade() + quantidade);
                    } else if (command.equals("S")) {
                        produto.setQuantidade(produto.getQuantidade() - quantidade);
                    }
                    ctrlProd.edit(produto);

                    // Confirmar a operação para o cliente
                    out.writeObject("Movimento registrado com sucesso");

                    // Enviar objeto Produto atualizado para o cliente
                    out.writeObject(produto);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Adicionar log de erro
        } catch (Exception ex) {
            Logger.getLogger(CadastroThreadV2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}