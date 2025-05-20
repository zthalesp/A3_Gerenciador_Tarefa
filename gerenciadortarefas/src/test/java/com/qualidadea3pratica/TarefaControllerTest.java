package com.qualidadea3pratica;

import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.controller.TarefaController;
import com.qualidadea3pratica.model.StatusTarefa;
import com.qualidadea3pratica.repository.TarefaRepository;
import com.qualidadea3pratica.view.MainFrame;
import com.qualidadea3pratica.view.TarefaDialog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TarefaController.class, TarefaDialog.class})
public class TarefaControllerTest {

    @Mock
    private MainFrame view;

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaController controller;

    @Test
    public void carregarTarefas_deveLimparListaEAdicionarTarefasDoRepositorio() {
        // Arrange
        DefaultListModel<tarefa> mockListModel = new DefaultListModel<>();
        List<tarefa> tarefas = Arrays.asList(
            new tarefa("Tarefa 1", "Desc 1", LocalDate.now()),
            new tarefa("Tarefa 2", "Desc 2", LocalDate.now().plusDays(1))
        );
        when(view.getListModel()).thenReturn(mockListModel);
        when(repository.listarTodos()).thenReturn(tarefas);

        // Act
        controller.carregarTarefas();

        // Assert
        assertEquals(2, mockListModel.size());
        assertEquals(tarefas.get(0), mockListModel.getElementAt(0));
        assertEquals(tarefas.get(1), mockListModel.getElementAt(1));
        verify(view.getListModel(), times(1)).clear();
        verify(repository, times(1)).listarTodos();
    }

    @Test
    public void adicionarTarefa_dialogoConfirmado_deveAdicionarTarefaAoRepositorioELista() throws Exception {
        // Arrange
        tarefa novaTarefa = new tarefa("Nova Tarefa", "Nova Desc", LocalDate.now().plusDays(2));
        TarefaDialog mockDialog = Mockito.mock(TarefaDialog.class);
        when(mockDialog.isConfirmed()).thenReturn(true);
        when(mockDialog.getTarefa()).thenReturn(novaTarefa);
        PowerMockito.whenNew(TarefaDialog.class).withArguments(view, "Nova Tarefa", null).thenReturn(mockDialog);
        DefaultListModel<tarefa> mockListModel = new DefaultListModel<>();
        when(view.getListModel()).thenReturn(mockListModel);

        // Act
        controller.adicionarTarefa();

        // Assert
        verify(repository, times(1)).adicionar(novaTarefa);
        assertEquals(1, mockListModel.size());
        assertEquals(novaTarefa, mockListModel.getElementAt(0));
        verify(mockDialog, times(1)).setVisible(true);
    }

    @Test
    public void adicionarTarefa_dialogoNaoConfirmado_naoDeveAdicionarTarefa() throws Exception {
        // Arrange
        TarefaDialog mockDialog = Mockito.mock(TarefaDialog.class);
        when(mockDialog.isConfirmed()).thenReturn(false);
        PowerMockito.whenNew(TarefaDialog.class).withArguments(view, "Nova Tarefa", null).thenReturn(mockDialog);
        DefaultListModel<tarefa> mockListModel = new DefaultListModel<>();
        when(view.getListModel()).thenReturn(mockListModel);

        // Act
        controller.adicionarTarefa();

        // Assert
        verify(repository, never()).adicionar(any());
        assertEquals(0, mockListModel.size());
        verify(mockDialog, times(1)).setVisible(true);
    }

    @Test
    public void adicionarTarefa_dialogoConfirmado_dataInvalida_deveMostrarMensagemDeErro() throws Exception {
        // Arrange
        TarefaDialog mockDialog = Mockito.mock(TarefaDialog.class);
        when(mockDialog.isConfirmed()).thenReturn(true);
        when(mockDialog.getTarefa()).thenThrow(new DateTimeParseException("Formato inválido", "input", 0));
        PowerMockito.whenNew(TarefaDialog.class).withArguments(view, "Nova Tarefa", null).thenReturn(mockDialog);

        // Act
        controller.adicionarTarefa();

        // Assert
        verify(repository, never()).adicionar(any());
        verify(view, times(1));
        JOptionPane.showMessageDialog(view, "Formato de data inválido! Use dd/mm/aaaa", "Erro", JOptionPane.ERROR_MESSAGE);
        verify(mockDialog, times(1)).setVisible(true);
    }

    @Test
    public void editarTarefa_tarefaSelecionada_dialogoConfirmado_deveAtualizarRepositorioELista() throws Exception {
        // Arrange
        int selectedIndex = 0;
        tarefa tarefaOriginal = new tarefa("Tarefa Original", "Desc", LocalDate.now());
        tarefa tarefaEditada = new tarefa("Tarefa Editada", "Nova Desc", LocalDate.now().plusDays(1));
        JList<tarefa> mockList = Mockito.mock(JList.class);
        DefaultListModel<tarefa> mockListModel = new DefaultListModel<>();
        mockListModel.addElement(tarefaOriginal);

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(repository.buscarPorIndice(selectedIndex)).thenReturn(tarefaOriginal);

        TarefaDialog mockDialog = Mockito.mock(TarefaDialog.class);
        when(mockDialog.isConfirmed()).thenReturn(true);
        when(mockDialog.getTarefa()).thenReturn(tarefaEditada);
        PowerMockito.whenNew(TarefaDialog.class).withArguments(view, "Editar Tarefa", tarefaOriginal).thenReturn(mockDialog);
        when(view.getListModel()).thenReturn(mockListModel);

        // Act
        controller.editarTarefa();

        // Assert
        verify(repository, times(1)).atualizar(selectedIndex, tarefaEditada);
        assertEquals(tarefaEditada, mockListModel.getElementAt(selectedIndex));
        verify(mockDialog, times(1)).setVisible(true);
    }

    @Test
    public void editarTarefa_nenhumaTarefaSelecionada_deveMostrarMensagemDeAviso() {
        // Arrange
        JList<tarefa> mockList = Mockito.mock(JList.class);
        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(-1);

        // Act
        controller.editarTarefa();

        // Assert
        verify(repository, never()).buscarPorIndice(anyInt());
        verify(view, times(1));
        JOptionPane.showMessageDialog(view, "Selecione uma tarefa para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        verifyNoInteractions(Mockito.mock(TarefaDialog.class));
    }

    @Test
    public void removerTarefa_tarefaSelecionada_confirmado_deveRemoverDoRepositorioELista() {
        // Arrange
        int selectedIndex = 0;
        JList<tarefa> mockList = Mockito.mock(JList.class);
        DefaultListModel<tarefa> mockListModel = new DefaultListModel<>();
        tarefa tarefaRemover = new tarefa("Tarefa Remover", "Desc", LocalDate.now());
        mockListModel.addElement(tarefaRemover);

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(view.getListModel()).thenReturn(mockListModel);
        when(JOptionPane.showConfirmDialog(view, "Tem certeza que deseja remover esta tarefa?", "Confirmar", JOptionPane.YES_NO_OPTION))
                .thenReturn(JOptionPane.YES_OPTION);

        // Act
        controller.removerTarefa();

        // Assert
        verify(repository, times(1)).remover(selectedIndex);
        assertEquals(0, mockListModel.size());
    }

    @Test
    public void removerTarefa_tarefaSelecionada_naoConfirmado_naoDeveRemover() {
        // Arrange
        int selectedIndex = 0;
        JList<tarefa> mockList = Mockito.mock(JList.class);
        DefaultListModel<tarefa> mockListModel = new DefaultListModel<>();
        tarefa tarefaRemover = new tarefa("Tarefa Remover", "Desc", LocalDate.now());
        mockListModel.addElement(tarefaRemover);

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(view.getListModel()).thenReturn(mockListModel);
        when(JOptionPane.showConfirmDialog(view, "Tem certeza que deseja remover esta tarefa?", "Confirmar", JOptionPane.YES_NO_OPTION))
                .thenReturn(JOptionPane.NO_OPTION);

        // Act
        controller.removerTarefa();

        // Assert
        verify(repository, never()).remover(anyInt());
        assertEquals(1, mockListModel.size());
    }

    @Test
    public void removerTarefa_nenhumaTarefaSelecionada_deveMostrarMensagemDeAviso() {
        // Arrange
        JList<tarefa> mockList = Mockito.mock(JList.class);
        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(-1);

        // Act
        controller.removerTarefa();

        // Assert
        verify(repository, never()).remover(anyInt());
        verify(view, times(1));
        JOptionPane.showMessageDialog(view, "Selecione uma tarefa para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    @Test
    public void alterarStatus_tarefaSelecionada_novoStatusSelecionado_deveAtualizarRepositorioELista() {
        // Arrange
        int selectedIndex = 0;
        JList<tarefa> mockList = Mockito.mock(JList.class);
        DefaultListModel<tarefa> mockListModel = new DefaultListModel<>();
        tarefa tarefaOriginal = new tarefa("Tarefa", "Desc", LocalDate.now());
        mockListModel.addElement(tarefaOriginal);
        StatusTarefa novoStatus = StatusTarefa.CONCLUIDA;

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(repository.buscarPorIndice(selectedIndex)).thenReturn(tarefaOriginal);
        when(JOptionPane.showInputDialog(
                view, "Selecione o novo status:", "Alterar Status",
                JOptionPane.PLAIN_MESSAGE, null, StatusTarefa.values(), tarefaOriginal.getStatus()))
                .thenReturn(novoStatus);
        when(view.getListModel()).thenReturn(mockListModel);

        // Act
        controller.alterarStatus();

        // Assert
        assertEquals(novoStatus, tarefaOriginal.getStatus());
        verify(repository, times(1)).atualizar(selectedIndex, tarefaOriginal);
        assertEquals(novoStatus, mockListModel.getElementAt(selectedIndex).getStatus());
    }

    @Test
    public void alterarStatus_tarefaSelecionada_novoStatusNulo_naoDeveAtualizar() {
        // Arrange
        int selectedIndex = 0;
        JList<tarefa> mockList = Mockito.mock(JList.class);
        DefaultListModel<tarefa> mockListModel = new DefaultListModel<>();
        tarefa tarefaOriginal = new tarefa("Tarefa", "Desc", LocalDate.now());
        mockListModel.addElement(tarefaOriginal);

        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(selectedIndex);
        when(repository.buscarPorIndice(selectedIndex)).thenReturn(tarefaOriginal);
        when(JOptionPane.showInputDialog(
                view, "Selecione o novo status:", "Alterar Status",
                JOptionPane.PLAIN_MESSAGE, null, StatusTarefa.values(), tarefaOriginal.getStatus()))
                .thenReturn(null);
        when(view.getListModel()).thenReturn(mockListModel);

        // Act
        controller.alterarStatus();

        // Assert
        assertEquals(StatusTarefa.PENDENTE, tarefaOriginal.getStatus());
        verify(repository, never()).atualizar(anyInt(), any());
        assertEquals(StatusTarefa.PENDENTE, mockListModel.getElementAt(selectedIndex).getStatus());
    }

    @Test
    public void alterarStatus_nenhumaTarefaSelecionada_deveMostrarMensagemDeAviso() {
        // Arrange
        JList<tarefa> mockList = Mockito.mock(JList.class);
        when(view.getTarefasList()).thenReturn(mockList);
        when(mockList.getSelectedIndex()).thenReturn(-1);

        // Act
        controller.alterarStatus();

        // Assert
        verify(repository, never()).buscarPorIndice(anyInt());
        verify(view, times(1));
        JOptionPane.showMessageDialog(view, "Selecione uma tarefa para alterar o status!", "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}