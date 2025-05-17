package com.qualidadea3pratica.view.componetes;

import javax.swing.*;
import java.awt.*;
import com.qualidadea3pratica.model.tarefa;
import com.qualidadea3pratica.model.StatusTarefa;

public class TarefaListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
            boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof tarefa) {
            tarefa tarefa = (tarefa) value;
            setText("<html><b>" + tarefa.getTitulo() + "</b><br>" +
                   "Vencimento: " + tarefa.getDataFormatada() + "<br>" +
                   "Status: " + tarefa.getStatus().getDescricao() + "</html>");
            
            // Cores baseadas no status
            if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
                setBackground(new Color(200, 255, 200));
            } else if (tarefa.getStatus() == StatusTarefa.EM_ANDAMENTO) {
                setBackground(new Color(255, 255, 200));
            } else {
                setBackground(new Color(255, 200, 200));
            }
        }
        return this;
    }
}
