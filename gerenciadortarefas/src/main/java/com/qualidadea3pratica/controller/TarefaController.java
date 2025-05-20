package com.qualidadea3pratica.controller;

import com.qualidadea3pratica.model.StatusTarefa;
import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.repository.TarefaRepository;
import com.qualidadea3pratica.view.componetes.DialogService;
import com.qualidadea3pratica.view.MainFrame;
import com.qualidadea3pratica.view.TarefaDialog;
import com.qualidadea3pratica.view.componetes.TarefaDialogFactory;

import javax.swing.*;
import java.time.format.DateTimeParseException;

public class TarefaController {

    private final MainFrame view;
    private final TarefaRepository repository;
    private final TarefaDialogFactory dialogFactory; // Nova dependência
    private final DialogService dialogService;       // Nova dependência

    // Construtor com injeção de todas as dependências
    public TarefaController(MainFrame view, TarefaRepository repository,
                            TarefaDialogFactory dialogFactory, DialogService dialogService) {
        this.view = view;
        this.repository = repository;
        this.dialogFactory = dialogFactory;
        this.dialogService = dialogService;
        view.setController(this); // Assume que MainFrame precisa do controller
    }

    public void carregarTarefas() {
        view.getListModel().clear();
        repository.listarTodos().forEach(view.getListModel()::addElement);
    }

    public void adicionarTarefa() {
        // Usa a fábrica para criar o diálogo
        TarefaDialog dialog = dialogFactory.createTarefaDialog(view, "Nova Tarefa", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                tarefa novaTarefa = dialog.getTarefa();
                repository.adicionar(novaTarefa);
                view.getListModel().addElement(novaTarefa);
            } catch (DateTimeParseException e) {
                dialogService.showMessage(view, "Formato de data inválido! Use dd/mm/aaaa", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void editarTarefa() {
        int selectedIndex = view.getTarefasList().getSelectedIndex();
        if (selectedIndex != -1) {
            tarefa tarefaOriginal = repository.buscarPorIndice(selectedIndex);
            // Cria o diálogo com a tarefa original para edição
            TarefaDialog dialog = dialogFactory.createTarefaDialog(view, "Editar Tarefa", tarefaOriginal);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                try {
                    tarefa tarefaEditada = dialog.getTarefa();
                    repository.atualizar(selectedIndex, tarefaEditada);
                    view.getListModel().setElementAt(tarefaEditada, selectedIndex);
                } catch (DateTimeParseException e) {
                    dialogService.showMessage(view, "Formato de data inválido! Use dd/mm/aaaa", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            dialogService.showMessage(view, "Selecione uma tarefa para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void removerTarefa() {
        int selectedIndex = view.getTarefasList().getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = dialogService.showConfirmDialog(view, "Tem certeza que deseja remover esta tarefa?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                repository.remover(selectedIndex);
                view.getListModel().remove(selectedIndex);
            }
        } else {
            dialogService.showMessage(view, "Selecione uma tarefa para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void alterarStatus() {
        int selectedIndex = view.getTarefasList().getSelectedIndex();
        if (selectedIndex != -1) {
            tarefa tarefaOriginal = repository.buscarPorIndice(selectedIndex);
            Object novoStatusObj = dialogService.showInputDialog(
                    view, "Selecione o novo status:", "Alterar Status",
                    JOptionPane.PLAIN_MESSAGE, null, StatusTarefa.values(), tarefaOriginal.getStatus());

            if (novoStatusObj != null) {
                StatusTarefa novoStatus = (StatusTarefa) novoStatusObj;
                tarefaOriginal.setStatus(novoStatus);
                repository.atualizar(selectedIndex, tarefaOriginal); // Atualiza no repositório
                view.getListModel().setElementAt(tarefaOriginal, selectedIndex); // Atualiza na lista da view
            }
        } else {
            dialogService.showMessage(view, "Selecione uma tarefa para alterar o status!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}