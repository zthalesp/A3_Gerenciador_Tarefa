package com.qualidadea3pratica;

import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.controller.TarefaController;
import com.qualidadea3pratica.model.StatusTarefa;
import com.qualidadea3pratica.repository.TarefaRepository;
import com.qualidadea3pratica.view.MainFrame;
import com.qualidadea3pratica.view.TarefaDialog;
import com.qualidadea3pratica.view.componetes.TarefaDialogFactory;
import com.qualidadea3pratica.view.componetes.DialogService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TarefaControllerTest {

    @Mock
    private MainFrame view;

    @Mock
    private TarefaRepository repository;

    @Mock
    private TarefaDialogFactory mockDialogFactory;

    @Mock
    private DialogService mockDialogService;

    private TarefaController controller;

    @Before
    public void setUp() {
        controller = new TarefaController(view, repository, mockDialogFactory, mockDialogService);
    }

    @Test
    public void carregarTarefas_deveLimparListaEAdicionarTarefasDoRepositorio() {
        @SuppressWarnings("unchecked")
        DefaultListModel<tarefa> mockListModel = mock(DefaultListModel.class);
        List<tarefa> tarefas = Arrays.asList(
                new tarefa("Tarefa 1", "Desc 1", LocalDate.now()),
                new tarefa("Tarefa 2", "Desc 2", LocalDate.now().plusDays(1))
        );
        when(view.getListModel()).thenReturn(mockListModel);
        when(repository.listarTodos()).thenReturn(tarefas);

        controller.carregarTarefas();

        verify(mockListModel, times(1)).clear();
        verify(mockListModel, times(1)).addElement(tarefas.get(0));
        verify(mockListModel, times(1)).addElement(tarefas.get(1));
        verify(repository, times(1)).listarTodos();
    }

    @Test
    public void adicionarTarefa_dialogoConfirmado_deveAdicionarTarefaAoRepositorioELista() {
        tarefa novaTarefa = new tarefa("Nova Tarefa", "Nova Desc", LocalDate.now().plusDays(2));
        TarefaDialog mockDialog = Mockito.mock(TarefaDialog.class);

        when(mockDialogFactory.createTarefaDialog(view, "Nova Tarefa", null)).thenReturn(mockDialog);

        when(mockDialog.isConfirmed()).thenReturn(true);
        when(mockDialog.getTarefa()).thenReturn(novaTarefa);

        @SuppressWarnings("unchecked")
        DefaultListModel<tarefa> mockListModel = mock(DefaultListModel.class);
        when(view.getListModel()).thenReturn(mockListModel);

        controller.adicionarTarefa();

        verify(repository, times(1)).adicionar(novaTarefa);
        verify(mockListModel, times(1)).addElement(novaTarefa);
        verify(mockDialog, times(1)).setVisible(true);
    }

    @Test
    public void adicionarTarefa_dialogoNaoConfirmado_naoDeveAdicionarTarefa() {
        TarefaDialog mockDialog = Mockito.mock(TarefaDialog.class);
        when(mockDialogFactory.createTarefaDialog(view, "Nova Tarefa", null)).thenReturn(mockDialog);

        when(mockDialog.isConfirmed()).thenReturn(false);

        @SuppressWarnings("unchecked")
        DefaultListModel<tarefa> mockListModel = mock(DefaultListModel.class);
        when(view.getListModel()).thenReturn(mockListModel);

        controller.adicionarTarefa();

        verify(repository, never()).adicionar(any());
        verify(mockListModel, never()).addElement(any());
        verify(mockDialog, times(1)).setVisible(true);
    }

    @Test
    public void adicionarTarefa_dialogoConfirmado_dataInvalida_deveMostrarMensagemDeErro() {
        TarefaDialog mockDialog = Mockito.mock(TarefaDialog.class);
        when(mockDialogFactory.createTarefaDialog(view, "Nova Tarefa", null)).thenReturn(mockDialog);

        when(mockDialog.isConfirmed()).thenReturn(true);
        when(mockDialog.getTarefa()).thenThrow(new DateTimeParseException("Formato inválido", "input", 0));

        controller.adicionarTarefa();

        verify(repository, never()).adicionar(any());
        verify(mockDialogService, times(1))
            .showMessage(view, "Formato de data inválido! Use dd/mm/aaaa", "Erro", JOptionPane.ERROR_MESSAGE);
        verify(mockDialog, times(1)).setVisible(true);
    }

    @Test
    public void editarTarefa_tarefaSelecionada_dialogoConfirmado_deveAtualizarRepositorioELista() {
        int selectedIndex = 0;
        tarefa tarefaOriginal = new tarefa("Tarefa Original", "Desc", LocalDate.now());
        tarefa tarefaEditada = new tarefa("Tarefa Editada", "Nova Desc", LocalDate.now().plusDays(1));
        JList<tarefa> mockList = Mockito.mock(JList.class);

        @SuppressWarnings("unchecked")
        DefaultListModel<tarefa> mockListModel = mock(DefaultListModel.class);
        when(mockListModel.getElementAt(selectedIndex)).thenReturn(tarefaOriginal);

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(repository.buscarPorIndice(selectedIndex)).thenReturn(tarefaOriginal);

        TarefaDialog mockDialog = Mockito.mock(TarefaDialog.class);
        when(mockDialogFactory.createTarefaDialog(view, "Editar Tarefa", tarefaOriginal)).thenReturn(mockDialog);

        when(mockDialog.isConfirmed()).thenReturn(true);
        when(mockDialog.getTarefa()).thenReturn(tarefaEditada);
        when(view.getListModel()).thenReturn(mockListModel);

        controller.editarTarefa();

        verify(repository, times(1)).atualizar(selectedIndex, tarefaEditada);
        verify(mockListModel, times(1)).setElementAt(tarefaEditada, selectedIndex);
        verify(mockDialog, times(1)).setVisible(true);
    }

    @Test
    public void editarTarefa_nenhumaTarefaSelecionada_deveMostrarMensagemDeAviso() {
        JList<tarefa> mockList = Mockito.mock(JList.class);
        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(-1);

        controller.editarTarefa();

        verify(repository, never()).buscarPorIndice(anyInt());
        verify(mockDialogService, times(1))
            .showMessage(view, "Selecione uma tarefa para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        verifyNoInteractions(mockDialogFactory);
    }

    @Test
    public void removerTarefa_tarefaSelecionada_confirmado_deveRemoverDoRepositorioELista() {
        int selectedIndex = 0;
        JList<tarefa> mockList = Mockito.mock(JList.class);

        @SuppressWarnings("unchecked")
        DefaultListModel<tarefa> mockListModel = mock(DefaultListModel.class);

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(view.getListModel()).thenReturn(mockListModel);
        when(mockDialogService.showConfirmDialog(view, "Tem certeza que deseja remover esta tarefa?", "Confirmar", JOptionPane.YES_NO_OPTION))
                .thenReturn(JOptionPane.YES_OPTION);

        controller.removerTarefa();

        verify(repository, times(1)).remover(selectedIndex);
        verify(mockListModel, times(1)).remove(selectedIndex);
    }

    @Test
    public void removerTarefa_tarefaSelecionada_naoConfirmado_naoDeveRemover() {
        int selectedIndex = 0;
        JList<tarefa> mockList = Mockito.mock(JList.class);

        @SuppressWarnings("unchecked")
        DefaultListModel<tarefa> mockListModel = mock(DefaultListModel.class);

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(view.getListModel()).thenReturn(mockListModel);
        when(mockDialogService.showConfirmDialog(view, "Tem certeza que deseja remover esta tarefa?", "Confirmar", JOptionPane.YES_NO_OPTION))
                .thenReturn(JOptionPane.NO_OPTION);

        controller.removerTarefa();

        verify(repository, never()).remover(anyInt());
        verify(mockListModel, never()).remove(anyInt());
    }

    @Test
    public void removerTarefa_nenhumaTarefaSelecionada_deveMostrarMensagemDeAviso() {
        JList<tarefa> mockList = Mockito.mock(JList.class);
        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(-1);

        controller.removerTarefa();

        verify(repository, never()).buscarPorIndice(anyInt());
        verify(mockDialogService, times(1))
            .showMessage(view, "Selecione uma tarefa para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    @Test
    public void alterarStatus_tarefaSelecionada_novoStatusSelecionado_deveAtualizarRepositorioELista() {
        int selectedIndex = 0;
        JList<tarefa> mockList = Mockito.mock(JList.class);

        @SuppressWarnings("unchecked")
        DefaultListModel<tarefa> mockListModel = mock(DefaultListModel.class);
        tarefa tarefaOriginal = new tarefa("Tarefa", "Desc", LocalDate.now());

        StatusTarefa novoStatus = StatusTarefa.CONCLUIDA;

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(repository.buscarPorIndice(selectedIndex)).thenReturn(tarefaOriginal);
        when(mockDialogService.showInputDialog(
                eq(view), eq("Selecione o novo status:"), eq("Alterar Status"),
                eq(JOptionPane.PLAIN_MESSAGE), eq(null), eq(StatusTarefa.values()), eq(tarefaOriginal.getStatus())))
                .thenReturn(novoStatus);
        when(view.getListModel()).thenReturn(mockListModel);

        controller.alterarStatus();

        assertEquals(novoStatus, tarefaOriginal.getStatus());
        verify(repository, times(1)).atualizar(selectedIndex, tarefaOriginal);
        verify(mockListModel, times(1)).setElementAt(tarefaOriginal, selectedIndex);
    }

    @Test
    public void alterarStatus_tarefaSelecionada_novoStatusNulo_naoDeveAtualizar() {
        int selectedIndex = 0;
        JList<tarefa> mockList = Mockito.mock(JList.class);

        @SuppressWarnings("unchecked")
        DefaultListModel<tarefa> mockListModel = mock(DefaultListModel.class);
        tarefa tarefaOriginal = new tarefa("Tarefa", "Desc", LocalDate.now());

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(repository.buscarPorIndice(selectedIndex)).thenReturn(tarefaOriginal);
        when(mockDialogService.showInputDialog(
                eq(view), eq("Selecione o novo status:"), eq("Alterar Status"),
                eq(JOptionPane.PLAIN_MESSAGE), eq(null), eq(StatusTarefa.values()), eq(tarefaOriginal.getStatus())))
                .thenReturn(null);
        when(view.getListModel()).thenReturn(mockListModel);

        controller.alterarStatus();

        assertEquals(StatusTarefa.PENDENTE, tarefaOriginal.getStatus());
        verify(repository, never()).atualizar(anyInt(), any());
        verify(mockListModel, never()).setElementAt(any(), anyInt());
    }

    @Test
    public void alterarStatus_nenhumaTarefaSelecionada_deveMostrarMensagemDeAviso() {
        JList<tarefa> mockList = Mockito.mock(JList.class);
        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(-1);

        controller.alterarStatus();

        verify(repository, never()).buscarPorIndice(anyInt());
        verify(mockDialogService, times(1))
            .showMessage(view, "Selecione uma tarefa para alterar o status!", "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}