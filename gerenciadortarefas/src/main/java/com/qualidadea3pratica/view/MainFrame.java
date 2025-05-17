package com.qualidadea3pratica.view;

import javax.swing.*;
import java.awt.*;
import com.qualidadea3pratica.controller.TarefaController;
import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.view.componetes.TarefaListRenderer;

public class MainFrame extends JFrame {
    private JList<tarefa> tarefasList;
    private DefaultListModel<tarefa> listModel;
    private TarefaController controller;

    public MainFrame() {
        super("Gerenciador de Tarefas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        controller = new TarefaController(this);
        initializeComponents();
    }

    private void initializeComponents() {
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");
        JButton removeButton = new JButton("Remover");
        JButton statusButton = new JButton("Alterar Status");
        
        addButton.addActionListener(e -> controller.adicionarTarefa());
        editButton.addActionListener(e -> controller.editarTarefa());
        removeButton.addActionListener(e -> controller.removerTarefa());
        statusButton.addActionListener(e -> controller.alterarStatus());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(statusButton);
        
        add(buttonPanel, BorderLayout.NORTH);
        
        
        listModel = new DefaultListModel<>();
        tarefasList = new JList<>(listModel);
        tarefasList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tarefasList.setCellRenderer(new TarefaListRenderer());
        
        add(new JScrollPane(tarefasList), BorderLayout.CENTER);
        
        
        controller.carregarTarefas();
    }

    public DefaultListModel<tarefa> getListModel() {
        return listModel;
    }

    public JList<tarefa> getTarefasList() {
        return tarefasList;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
