package ej;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
public class Main {

    private Vista vista;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.vista.getFrame().setVisible(true);
        });
    }

    public Main() {
        vista = new Vista();

        // ActionListener per al botó "Buscar..."
        vista.addBtnChooseDirListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = vista.getFileChooser().showOpenDialog(vista.getFrame());
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = vista.getFileChooser().getSelectedFile();
                    vista.updateTree(selectedFile); // Actualitza el arbre
                    vista.setTextFieldBuscador(selectedFile.getPath()); //indicar la ruta en el Textfield
                }
            }
        });
        vista.getFrame().setVisible(true);

        // ActionListener  per al botó "Filtrar"
        vista.addBtnFiltrarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String busqueda = vista.getTextFieldFiltrar().getText();
                vista.buscarCoincidencias(busqueda);
            	
            	
            }
        });

        // ActionListener per al botó "Reemplaçar"
        vista.addBtnReemplaçarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String nuevaPalabra = vista.getTextFieldReemplazar().getText();
                if (nuevaPalabra.isEmpty()) {
                    JOptionPane.showMessageDialog(vista.getFrame(), "Por favor, introduce una palabra para reemplazar.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                File directory = vista.getFileChooser().getSelectedFile();
                if (directory != null && directory.isDirectory()) {
                    vista.reemplazarCoincidenciasEnDirectorio(directory, nuevaPalabra);
                } else {
                    JOptionPane.showMessageDialog(vista.getFrame(), "Por favor, selecciona un directorio válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}