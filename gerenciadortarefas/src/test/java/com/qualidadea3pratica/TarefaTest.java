package com.qualidadea3pratica;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;

import org.junit.Test;

import com.qualidadea3pratica.model.StatusTarefa;
import com.qualidadea3pratica.model.tarefa;

public class TarefaTest {
    
    @Test
    public void testConstruirTarefa() {
        LocalDate dataVencimento = LocalDate.now().plusDays(7);
        tarefa novaTarefa = new tarefa("Escola", "Completar trabalho escola", dataVencimento);
        assertEquals("Escola", novaTarefa.getTitulo());
        assertEquals("Completar trabalho escola", novaTarefa.getDescricao());
        assertEquals(dataVencimento, novaTarefa.getDataVencimento());
        assertEquals(StatusTarefa.PENDENTE, novaTarefa.getStatus());
    }

    @Test
    public void testGetESet() {
        LocalDate dataVencimentoInicial = LocalDate.now().plusDays(7);
        tarefa novaTarefa = new tarefa("Nova tarefa", "Descrição", dataVencimentoInicial);

        String novoTitulo = "Atualizar tarefa";
        String novoDescricao = "Atualizar descricao";
        LocalDate novaDataVencimento = LocalDate.now().plusMonths(1);
        StatusTarefa novoStatus = StatusTarefa.CONCLUIDA;

        novaTarefa.setTitulo(novoTitulo);
        novaTarefa.setDescricao(novoDescricao);
        novaTarefa.setDataVencimento(novaDataVencimento);
        novaTarefa.setStatus(novoStatus);

        assertEquals(novoTitulo, novaTarefa.getTitulo());
        assertEquals(novoDescricao, novaTarefa.getDescricao());
        assertEquals(novaDataVencimento, novaTarefa.getDataVencimento());
        assertEquals(novoStatus, novaTarefa.getStatus());
    }

    @Test
    public void testGetDataFormatada() {
        LocalDate dataVencimento = LocalDate.of(2025, 12, 31);
        tarefa novaTarefa = new tarefa("Evento", "Fim de ano", dataVencimento);
        assertEquals("31/12/2025", novaTarefa.getDataFormatada());
    }

    @Test
    public void testToString() {
        tarefa novaTarefa = new tarefa("Reunião", "Alinhamento da equipe", LocalDate.now());
        assertEquals("Reunião", novaTarefa.toString());
    }

    @Test
    public void testConstruirTarefaNula() {
        tarefa novaTarefa = new tarefa(null, null, null);
        assertNull(novaTarefa.getTitulo());
        assertNull(novaTarefa.getDescricao());
        assertNull(novaTarefa.getDataVencimento());
        assertEquals(StatusTarefa.PENDENTE, novaTarefa.getStatus());
    }

}
