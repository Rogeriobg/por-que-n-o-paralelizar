package cadastroclientv2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import model.Produto;
import java.util.List;

public class ThreadClient extends Thread {
    private ObjectInputStream entrada;
    private JTextArea textArea;

    public ThreadClient(ObjectInputStream entrada, JTextArea textArea) {
        this.entrada = entrada;
        this.textArea = textArea;
    }

    @Override
public void run() {
    try {
        while (true) {
            Object obj = entrada.readObject();
            if (obj instanceof String) {
                String message = (String) obj;
                SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
            } else if (obj instanceof List) {
                List<?> list = (List<?>) obj;
                SwingUtilities.invokeLater(() -> {
                    for (Object item : list) {
                        if (item instanceof Produto) {
                            Produto produto = (Produto) item;
                            textArea.append(produto.getNome() + " - " + produto.getQuantidade() + "\n");
                        }
                    }
                });
            } else if (obj instanceof Produto) {
                Produto produto = (Produto) obj;
                SwingUtilities.invokeLater(() -> textArea.append("Produto atualizado: " + produto.getNome() + " - " + produto.getQuantidade() + "\n"));
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}