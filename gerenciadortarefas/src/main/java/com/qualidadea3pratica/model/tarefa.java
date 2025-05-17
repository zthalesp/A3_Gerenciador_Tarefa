package com.qualidadea3pratica.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class tarefa{
    private String titulo;
    private String descricao;
    private LocalDate dataVencimento;
    private StatusTarefa status;

    public tarefa(String titulo, String descricao, LocalDate dataVencimento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.status = StatusTarefa.PENDENTE;
    }

    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { 
        this.dataVencimento = dataVencimento; 
    }

    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }

    public String getDataFormatada() {
        return dataVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String toString() {
        return titulo;
    }
}