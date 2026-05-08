package com.mtrilogic.desktop.demos.easylistadapter;

import com.mtrilogic.desktop.abstracts.SpringPanel;
import com.mtrilogic.desktop.easylistadapter.EasyList;
import com.mtrilogic.desktop.easylistadapter.Item;
import com.mtrilogic.desktop.easylistadapter.ListAdapter;
import com.mtrilogic.desktop.easylistadapter.Model;

import javax.swing.*;
import java.awt.*;

/**
 * EJEMPLO DE USO DE LA API EasyList
 * Demuestra cómo la API simplifica la creación de listas con items de diferente tipo.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        super("EasyList API Demo");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Crear el panel con EasyList (nueva API)
        MainPanel mainPanel = new MainPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

        // Agregar 100 items alternados (50 de cada tipo)
        // Los IDs se generan automáticamente de forma ascendente (1, 2, 3, ...)
        for (int i = 0; i < 50; i++) {
            LocalModel localModel = new LocalModel();
            localModel.setText("Local itemId=" + localModel.getItemId());

            RemoteModel remoteModel = new RemoteModel();
            remoteModel.setText("Remote itemId=" + remoteModel.getItemId());

            // Usando appendItem() de la nueva API
            mainPanel.easyList.appendItem(localModel);
            mainPanel.easyList.appendItem(remoteModel);
        }
    }

    /**
     * MainPanel usando la API EasyList
     */
    private static class MainPanel extends SpringPanel implements ListAdapter.Listener {
        
        private final EasyList easyList;

        public MainPanel() {
            // Crear EasyList implementando ListAdapter.Listener
            easyList = new EasyList(this);
            easyList.setBackground(Color.YELLOW);
            easyList.setFixedCellHeight(-1); // Altura variable por cada Item
            
            // EasyList provee createScrollPane()
            JScrollPane scrollPane = easyList.createScrollPane();
            with(scrollPane).top(0).left(0).right(0).bottom(0);
        }

        @Override
        public Item<? extends Model> getItem(int itemType) {
            // Factory de Items según el tipo
            if (itemType == 1) {
                System.out.println("Creando RemoteItem (tipo 1)");
                return new RemoteItem(); // Altura: 30px
            }
            System.out.println("Creando LocalItem (tipo 0)");
            return new LocalItem(); // Altura: 50px
        }
    }
}
