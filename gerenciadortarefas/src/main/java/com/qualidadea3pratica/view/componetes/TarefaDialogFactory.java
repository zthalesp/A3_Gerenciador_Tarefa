package com.qualidadea3pratica.view.componetes;

import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.view.TarefaDialog;

import javax.swing.JFrame;

public interface TarefaDialogFactory {
    TarefaDialog createTarefaDialog(JFrame parent, String title, tarefa task);
}