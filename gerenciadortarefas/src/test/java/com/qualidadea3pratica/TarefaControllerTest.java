package com.qualidadea3pratica;

import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.repository.TarefaRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class TarefaControllerTest {

    private TarefaRepository repository;

    @Before
    public void setUp() {
        repository = TarefaRepository.getInstance();
    }

    @Test
    public void testGetInstance() {
        TarefaRepository instance1 = TarefaRepository.getInstance();
        TarefaRepository instance2 = TarefaRepository.getInstance();
        assertNotNull(instance1);
        assertEquals(instance1, instance2);
    }

    @Test
    public void testAdicionarTarefa() {
        tarefa tarefa1 = new tarefa("Tarefa 1", "Desc 1", LocalDate.now());
        tarefa tarefa2 = new tarefa("Tarefa 2", "Desc 2", LocalDate.now());
        repository.adicionar(tarefa1);
        repository.adicionar(tarefa2);
        assertEquals(2, repository.listarTodos().size());
        assertEquals(tarefa1, repository.listarTodos().get(0));
        assertEquals(tarefa2, repository.listarTodos().get(1));
        // Limpar para outros testes (se necessário)
        repository.remover(0);
        repository.remover(1);
    }

    @Test
    public void testAtualizarTarefaExistente() {
        tarefa tarefa1 = new tarefa("Tarefa 1", "Desc 1", LocalDate.now());
        tarefa tarefa2 = new tarefa("Tarefa 2", "Desc 2", LocalDate.now());
        repository.adicionar(tarefa1);
        repository.adicionar(tarefa2);
        tarefa tarefaAtualizada = new tarefa("Tarefa Atualizada", "Nova Desc", LocalDate.now().plusDays(1));
        repository.atualizar(0, tarefaAtualizada);
        assertEquals(2, repository.listarTodos().size());
        assertEquals(tarefaAtualizada, repository.listarTodos().get(0));
        // Limpar
        repository.remover(0);
        repository.remover(1);
    }

    @Test
    public void testAtualizarTarefaInexistente() {
        tarefa tarefaAtualizada = new tarefa("Tarefa Atualizada", "Nova Desc", LocalDate.now().plusDays(1));
        repository.atualizar(0, tarefaAtualizada);
        assertEquals(0, repository.listarTodos().size());
    }

    @Test
    public void testRemoverTarefaExistente() {
        tarefa tarefa1 = new tarefa("Tarefa 1", "Desc 1", LocalDate.now());
        tarefa tarefa2 = new tarefa("Tarefa 2", "Desc 2", LocalDate.now().plusDays(1));
        repository.adicionar(tarefa1);
        repository.adicionar(tarefa2);
        repository.remover(0);
        assertEquals(1, repository.listarTodos().size());
        assertEquals(tarefa2, repository.listarTodos().get(0));
        // Limpar
        repository.remover(0);
        repository.remover(0);
    }

    @Test
    public void testRemoverTarefaInexistente() {
        repository.remover(0);
        assertEquals(0, repository.listarTodos().size());
    }

    @Test
    public void testListarTodos() {
        tarefa tarefa1 = new tarefa("Tarefa 1", "Desc 1", LocalDate.now());
        tarefa tarefa2 = new tarefa("Tarefa 2", "Desc 2", LocalDate.now().plusDays(1));
        repository.adicionar(tarefa1);
        repository.adicionar(tarefa2);
        List<tarefa> todasTarefas = repository.listarTodos();
        assertEquals(2, todasTarefas.size());
        assertEquals(tarefa1, todasTarefas.get(0));
        assertEquals(tarefa2, todasTarefas.get(1));
        // Limpar
        repository.remover(0);
        repository.remover(0);
    }

    @Test
    public void testListarTodosVazio() {
        assertEquals(0, repository.listarTodos().size());
    }

    @Test
    public void testBuscarPorIndiceExistente() {
        tarefa tarefa1 = new tarefa("Tarefa 1", "Desc 1", LocalDate.now());
        tarefa tarefa2 = new tarefa("Tarefa 2", "Desc 2", LocalDate.now().plusDays(1));
        repository.adicionar(tarefa1);
        repository.adicionar(tarefa2);
        tarefa tarefaEncontrada = repository.buscarPorIndice(1);
        assertEquals(tarefa2, tarefaEncontrada);
        // Limpar
        repository.remover(0);
        repository.remover(0);
    }

    @Test
    public void testBuscarPorIndiceInexistenteNegativo() {
        assertNull(repository.buscarPorIndice(-1));
    }

    @Test
    public void testBuscarPorIndiceInexistenteMaiorQueTamanho() {
        tarefa tarefa1 = new tarefa("Tarefa 1", "Desc 1", LocalDate.now());
        repository.adicionar(tarefa1);
        assertNull(repository.buscarPorIndice(1));
        // Limpar
        repository.remover(0);
    }
}