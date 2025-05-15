package com.qualidadea3pratica.controller;

import javax.swing.*;
import com.qualidadea3pratica.model.*;
import com.qualidadea3pratica.repository.TarefaRepository;
import com.qualidadea3pratica.view.MainFrame;
import com.qualidadea3pratica.view.TarefaDialog;
import java.time.format.DateTimeParseException;

public class TarefaController {
    private MainFrame view;
    private TarefaRepository repository;

    public TarefaController(MainFrame view) {
        this.view = view;
        this.repository = TarefaRepository.getInstance();
    }

    public void carregarTarefas() {
        view.getListModel().clear();
        repository.listarTodos().forEach(tarefa -> 
            view.getListModel().addElement(tarefa));
    }

    public void adicionarTarefa() {
        TarefaDialog dialog = new TarefaDialog(view, "Nova Tarefa", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            try {
                tarefa novaTarefa = dialog.getTarefa();
                repository.adicionar(novaTarefa);
                view.getListModel().addElement(novaTarefa);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(view, 
                    "Formato de data inválido! Use dd/mm/aaaa", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void editarTarefa() {
        int selectedIndex = view.getTarefasList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(view, 
                "Selecione uma tarefa para editar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tarefa tarefaSelecionada = repository.buscarPorIndice(selectedIndex);
        TarefaDialog dialog = new TarefaDialog(view, "Editar Tarefa", tarefaSelecionada);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            try {
                tarefa tarefaEditada = dialog.getTarefa();
                repository.atualizar(selectedIndex, tarefaEditada);
                view.getListModel().set(selectedIndex, tarefaEditada);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(view, 
                    "Formato de data inválido! Use dd/mm/aaaa", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void removerTarefa() {
        int selectedIndex = view.getTarefasList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(view, 
                "Selecione uma tarefa para remover!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view, 
            "Tem certeza que deseja remover esta tarefa?", 
            "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            repository.remover(selectedIndex);
            view.getListModel().remove(selectedIndex);
        }
    }

    public void alterarStatus() {
        int selectedIndex = view.getTarefasList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(view, 
                "Selecione uma tarefa para alterar o status!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tarefa tarefa = repository.buscarPorIndice(selectedIndex);
        StatusTarefa[] opcoes = StatusTarefa.values();
        
        StatusTarefa novoStatus = (StatusTarefa) JOptionPane.showInputDialog(
            view, "Selecione o novo status:", "Alterar Status", 
            JOptionPane.PLAIN_MESSAGE, null, opcoes, tarefa.getStatus());
        
        if (novoStatus != null) {
            tarefa.setStatus(novoStatus);
            repository.atualizar(selectedIndex, tarefa);
            view.getListModel().set(selectedIndex, tarefa);
        }
    }
}