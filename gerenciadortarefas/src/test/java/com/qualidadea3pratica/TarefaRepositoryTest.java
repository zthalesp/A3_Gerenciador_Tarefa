package com.qualidadea3pratica;

import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.repository.TarefaRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class TarefaRepositoryTest {

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
        tarefa tarefa1 = new tarefa("tarefa 1", "desc 1", LocalDate.now());
        repository.adicionar(tarefa1);
        assertEquals(1, repository.listarTodos().size());
        assertEquals(tarefa1, repository.listarTodos().get(0));
        repository.remover(0);
    }

    @Test
    public void testAtualizarTarefaExistente() {
        tarefa tarefa1 = new tarefa("tarefa 1", "desc 1", LocalDate.now());
        repository.adicionar(tarefa1);
        tarefa tarefaAtualizada = new tarefa("tarefa atualizada", "nova desc", LocalDate.now().plusDays(1));
        repository.atualizar(0, tarefaAtualizada);
        assertEquals(1, repository.listarTodos().size());
        assertEquals(tarefaAtualizada, repository.listarTodos().get(0));
        repository.remover(0);
    }

    @Test
    public void testAtualizarTarefaInexistente() {
        tarefa tarefaAtualizada = new tarefa("tarefa atualizada", "nova desc", LocalDate.now().plusDays(1));
        repository.atualizar(0, tarefaAtualizada);
        assertEquals(0, repository.listarTodos().size());
    }

    @Test
    public void testRemoverTarefaExistente() {
        tarefa tarefa1 = new tarefa("tarefa 1", "desc 1", LocalDate.now());
        tarefa tarefa2 = new tarefa("tarefa 2", "desc 2", LocalDate.now().plusDays(1));
        repository.adicionar(tarefa1);
        repository.adicionar(tarefa2);
        repository.remover(0);
        assertEquals(1, repository.listarTodos().size());
        assertEquals(tarefa2, repository.listarTodos().get(0));
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
        tarefa tarefa1 = new tarefa("tarefa 1", "desc 1", LocalDate.now());
        tarefa tarefa2 = new tarefa("tarefa 2", "desc 2", LocalDate.now().plusDays(1));
        repository.adicionar(tarefa1);
        repository.adicionar(tarefa2);
        List<tarefa> todasTarefas = repository.listarTodos();
        assertEquals(2, todasTarefas.size());
        assertEquals(tarefa1, todasTarefas.get(0));
        assertEquals(tarefa2, todasTarefas.get(1));
        repository.remover(0);
        repository.remover(0);
    }

    @Test
    public void testListarTodosVazio() {
        assertEquals(0, repository.listarTodos().size());
    }

    @Test
    public void testBuscarPorIndiceExistente() {
        tarefa tarefa1 = new tarefa("tarefa 1", "desc 1", LocalDate.now());
        tarefa tarefa2 = new tarefa("tarefa 2", "desc 2", LocalDate.now().plusDays(1));
        repository.adicionar(tarefa1);
        repository.adicionar(tarefa2);
        tarefa tarefaEncontrada = repository.buscarPorIndice(1);
        assertEquals(tarefa2, tarefaEncontrada);
        repository.remover(0);
        repository.remover(0);
    }

    @Test
    public void testBuscarPorIndiceInexistenteNegativo() {
        assertNull(repository.buscarPorIndice(-1));
    }

    @Test
    public void testBuscarPorIndiceInexistenteMaiorQueTamanho() {
        tarefa tarefa1 = new tarefa("tarefa 1", "desc 1", LocalDate.now());
        repository.adicionar(tarefa1);
        assertNull(repository.buscarPorIndice(1));
        repository.remover(0);
    }
}