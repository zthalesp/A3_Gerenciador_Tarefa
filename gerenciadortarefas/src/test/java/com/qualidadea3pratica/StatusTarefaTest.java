package com.qualidadea3pratica;

import org.junit.Test;
import com.qualidadea3pratica.model.StatusTarefa;
import static org.junit.Assert.assertEquals;

public class StatusTarefaTest {

    @Test
    public void testGetDescricaoPendente() {
        StatusTarefa status = StatusTarefa.PENDENTE;
        assertEquals("Pendente", status.getDescricao());
    }

    @Test
    public void testGetDescricaoEmAndamento() {
        StatusTarefa status = StatusTarefa.EM_ANDAMENTO;
        assertEquals("Em Andamento", status.getDescricao());
    }

    @Test
    public void testGetDescricaoConcluida() {
        StatusTarefa status = StatusTarefa.CONCLUIDA;
        assertEquals("Concluída", status.getDescricao());
    }
}