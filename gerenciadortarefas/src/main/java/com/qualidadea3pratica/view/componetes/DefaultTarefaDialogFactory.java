package com.qualidadea3pratica.view.componetes;

import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.view.TarefaDialog;

import javax.swing.JFrame;

public class DefaultTarefaDialogFactory implements TarefaDialogFactory {
    @Override
    public TarefaDialog createTarefaDialog(JFrame parent, String title, tarefa task) {
        return new TarefaDialog(parent, title, task);
    }
}