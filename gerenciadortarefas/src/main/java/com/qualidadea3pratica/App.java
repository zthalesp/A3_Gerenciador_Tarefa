package com.qualidadea3pratica;

import com.qualidadea3pratica.controller.TarefaController;
import com.qualidadea3pratica.repository.TarefaRepository;
import com.qualidadea3pratica.view.MainFrame;
import com.qualidadea3pratica.view.componetes.DefaultTarefaDialogFactory;
import com.qualidadea3pratica.view.componetes.DialogService;
import com.qualidadea3pratica.view.componetes.SwingDialogService;
import com.qualidadea3pratica.view.componetes.TarefaDialogFactory;

public class App 
{
    public static void main( String[] args )
    {
        TarefaRepository repository = new TarefaRepository();

        MainFrame view = new MainFrame();

        TarefaDialogFactory dialogFactory = new DefaultTarefaDialogFactory();
        DialogService dialogService = new SwingDialogService();

        TarefaController controller = new TarefaController(view, repository, dialogFactory, dialogService);

        view.setVisible(true);
    }
}
