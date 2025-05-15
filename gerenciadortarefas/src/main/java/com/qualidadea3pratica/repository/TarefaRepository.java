package com.qualidadea3pratica.repository;

import com.qualidadea3pratica.model.tarefa;
import java.util.ArrayList;
import java.util.List;

public class TarefaRepository {
    private List<tarefa> tarefas = new ArrayList<>();
    private static TarefaRepository instance;

    private TarefaRepository() {}

    public static TarefaRepository getInstance() {
        if (instance == null) {
            instance = new TarefaRepository();
        }
        return instance;
    }

    public void adicionar(tarefa tarefa) {
        tarefas.add(tarefa);
    }

    public void atualizar(int index, tarefa tarefa) {
        if (index >= 0 && index < tarefas.size()) {
            tarefas.set(index, tarefa);
        }
    }

    public void remover(int index) {
        if (index >= 0 && index < tarefas.size()) {
            tarefas.remove(index);
        }
    }

    public List<tarefa> listarTodos() {
        return new ArrayList<>(tarefas);
    }

    public tarefa buscarPorIndice(int index) {
        if (index >= 0 && index < tarefas.size()) {
            return tarefas.get(index);
        }
        return null;
    }
}
