package cadastroclientv2;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class SaidaFrame extends JDialog {
    public JTextArea texto;

    public SaidaFrame() {
        setBounds(100, 100, 500, 400); // Definir as dimensões da janela
        setModal(false); // Definir o status modal como false

        texto = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(texto);
        add(scrollPane); // Acrescentar o componente JTextArea na janela
    }
}