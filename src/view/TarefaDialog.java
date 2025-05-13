package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import model.tarefa;

public class TarefaDialog extends JDialog {
    private JTextField tituloField;
    private JTextArea descricaoArea;
    private JTextField dataField;
    private boolean confirmed;
    private tarefa tarefa;

    public TarefaDialog(JFrame parent, String title, tarefa tarefaExistente) {
        super(parent, title, true);
        this.tarefa = tarefaExistente;
        this.confirmed = false;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 300);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        tituloField = new JTextField();
        descricaoArea = new JTextArea();
        JScrollPane descricaoScroll = new JScrollPane(descricaoArea);
        dataField = new JTextField();
        
        if (tarefa != null) {
            tituloField.setText(tarefa.getTitulo());
            descricaoArea.setText(tarefa.getDescricao());
            dataField.setText(tarefa.getDataFormatada());
        }

        formPanel.add(new JLabel("Título:"));
        formPanel.add(tituloField);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(descricaoScroll);
        formPanel.add(new JLabel("Data (dd/mm/aaaa):"));
        formPanel.add(dataField);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancelar");
        
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public tarefa getTarefa() throws DateTimeParseException {
        String titulo = tituloField.getText().trim();
        String descricao = descricaoArea.getText().trim();
        LocalDate data = LocalDate.parse(dataField.getText(), 
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        if (tarefa == null) {
            return new tarefa(titulo, descricao, data);
        } else {
            tarefa.setTitulo(titulo);
            tarefa.setDescricao(descricao);
            tarefa.setDataVencimento(data);
            return tarefa;
        }
    }
}