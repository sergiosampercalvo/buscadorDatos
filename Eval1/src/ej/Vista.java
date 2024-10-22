 package ej;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class Vista {

    private JFrame frmGestorDeFitxers;
    private JTextField textFieldBuscador;
    private JTextField textFieldFiltrar;
    private JTextField textFieldReemplazar;
    private JButton btnChooseDir;
    private JButton btnFiltrar;
    private JButton btnReemplaçar;
    private JCheckBox chckbxMajuscules;
    private JCheckBox chckbxAccents;
    private JTree tree;
    private JFileChooser fileChooser;

    public static void main(String[] args) {
        Vista window = new Vista();
        window.frmGestorDeFitxers.setVisible(true);
    }

    public Vista() {
        initialize();
    }

    private void initialize() {
        frmGestorDeFitxers = new JFrame();
        frmGestorDeFitxers.setTitle("Gestor de Fitxers");
        frmGestorDeFitxers.getContentPane().setBackground(new Color(125, 208, 232));
        frmGestorDeFitxers.setBounds(100, 100, 827, 440);
        frmGestorDeFitxers.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmGestorDeFitxers.getContentPane().setLayout(null);
        textFieldBuscador = new JTextField();
        textFieldBuscador.setEnabled(false);
        textFieldBuscador.setEditable(false);
        textFieldBuscador.setBounds(36, 24, 581, 27);
        frmGestorDeFitxers.getContentPane().add(textFieldBuscador);
        textFieldBuscador.setColumns(10);
        btnChooseDir = new JButton("Buscar...");
        btnChooseDir.setBounds(647, 24, 85, 27);
        frmGestorDeFitxers.getContentPane().add(btnChooseDir);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 73, 532, 328);
        frmGestorDeFitxers.getContentPane().add(scrollPane);
        tree = new JTree();
        tree.setEditable(true);
        tree.setModel(new DefaultTreeModel(
            new DefaultMutableTreeNode("Archivos") {
				private static final long serialVersionUID = 1L;  //Aço el recomanaba la aplicació, no se molt be que fa en la versió
				{
                    add(new DefaultMutableTreeNode("Carregant..."));
                }
            }
        ));
        scrollPane.setViewportView(tree);
        textFieldFiltrar = new JTextField();
        textFieldFiltrar.setBounds(598, 129, 162, 27);
        frmGestorDeFitxers.getContentPane().add(textFieldFiltrar);
        textFieldFiltrar.setColumns(10);
        chckbxMajuscules = new JCheckBox("Respectar Majúscules");
        chckbxMajuscules.setBounds(598, 174, 162, 21);
        frmGestorDeFitxers.getContentPane().add(chckbxMajuscules);
        chckbxAccents = new JCheckBox("Respectar Accents");
        chckbxAccents.setBounds(598, 197, 162, 21);
        frmGestorDeFitxers.getContentPane().add(chckbxAccents);
        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBounds(647, 224, 85, 21);
        frmGestorDeFitxers.getContentPane().add(btnFiltrar);
        textFieldReemplazar = new JTextField();
        textFieldReemplazar.setColumns(10);
        textFieldReemplazar.setBounds(598, 277, 162, 27);
        frmGestorDeFitxers.getContentPane().add(textFieldReemplazar);
        btnReemplaçar = new JButton("Reemplaçar");
        btnReemplaçar.setBounds(647, 314, 85, 21);
        frmGestorDeFitxers.getContentPane().add(btnReemplaçar);
        JLabel lblNewLabel = new JLabel("Filtrar");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel.setBounds(598, 103, 68, 27);
        frmGestorDeFitxers.getContentPane().add(lblNewLabel);
        JLabel lblReemplazar = new JLabel("Reemplaçar");
        lblReemplazar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblReemplazar.setBounds(598, 249, 138, 27);
        frmGestorDeFitxers.getContentPane().add(lblReemplazar);
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
    
    public void updateTree(File directory) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(directory.getName());
        populateTree(rootNode, directory);
        tree.setModel(new DefaultTreeModel(rootNode));
    }
    
    private void populateTree(DefaultMutableTreeNode node, File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(file.getName());
                    node.add(subNode);
                    populateTree(subNode, file);
                } else {
                    //Açi se obten la informació del archiu y se exposa
                    long fileSize = file.length(); 
                    String lastModified = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(file.lastModified()); // Fecha de última modificación
                    String fileInfo = String.format("%s ( %d bytes,  %s)", file.getName(), fileSize, lastModified);
                    DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(fileInfo);
                    node.add(fileNode);
                }
            }
        }
    }
   
    
    public void buscarCoincidencias(String busqueda) {
        DefaultMutableTreeNode resultadosNode = new DefaultMutableTreeNode("Resultados de búsqueda"); //En el JTree soles he pogut crear un nou
        																//archiu y posar ahi les coincidencies, es un problema en el Jtree que no sabia solucioanar
        File directory = fileChooser.getSelectedFile(); 
        if (directory != null && directory.isDirectory()) {
            buscarCoincidenciasEnDirectorio(directory, busqueda, resultadosNode);
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(directory.getName());
            populateTree(rootNode, directory); 
            rootNode.add(resultadosNode); 
            tree.setModel(new DefaultTreeModel(rootNode)); 
        } else {
            JOptionPane.showMessageDialog(frmGestorDeFitxers, "Por favor, selecciona un directorio válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarCoincidenciasEnDirectorio(File directory, String busqueda, DefaultMutableTreeNode resultadosNode) {
        File[] archivos = directory.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.canRead()) {
                    int coincidencias = contarCoincidencias(archivo, busqueda);
                    if (coincidencias > 0) {
                        resultadosNode.add(new DefaultMutableTreeNode(archivo.getName() + " - Coincidencias: " + coincidencias));
                    }
                } else if (archivo.isDirectory()) {
                    buscarCoincidenciasEnDirectorio(archivo, busqueda, resultadosNode);
                }
            }
        }
    }
    
    
    private int contarCoincidencias(File archivo, String busqueda) {
        int contador = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contador += contarCoincidenciasEnLinea(linea, busqueda);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + archivo.getPath() + ": " + e.getMessage());
        }
        return contador;
    }
    
    
    private int contarCoincidenciasEnLinea(String linea, String busqueda) {
        int contador = 0;
        int index = 0;
        while ((index = linea.indexOf(busqueda, index)) != -1) {
            contador++;
            index += busqueda.length(); 
        }
        return contador;
    }
    
    
    void reemplazarCoincidenciasEnDirectorio(File directory, String nuevaPalabra) {
        File[] archivos = directory.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.canRead()) {
                    StringBuilder contenido = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                        String linea;
                        while ((linea = br.readLine()) != null) {
                            String nuevaLinea = linea.replaceAll(getTextFieldFiltrar().getText(), nuevaPalabra);
                            contenido.append(nuevaLinea).append(System.lineSeparator());
                        }
                    } catch (IOException e) {
                        e.printStackTrace(); 
                    }

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                        bw.write(contenido.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (archivo.isDirectory()) {
                    reemplazarCoincidenciasEnDirectorio(archivo, nuevaPalabra);
                }
            }
        }
    }


    // Métodes Getters y setters per a accedir als components
    public JFrame getFrame() {
        return frmGestorDeFitxers;
    }
    
    public void setTextFieldBuscador(String ruta) {
    	textFieldBuscador.setText(ruta);
    }

    public JTextField getTextFieldBuscador() {
        return textFieldBuscador;
    }

    public JTextField getTextFieldFiltrar() {
        return textFieldFiltrar;
    }

    public JTextField getTextFieldReemplazar() {
        return textFieldReemplazar;
    }

    public JCheckBox getChckbxMajuscules() {
        return chckbxMajuscules;
    }

    public JCheckBox getChckbxAccents() {
        return chckbxAccents;
    }

    public JButton getBtnChooseDir() {
        return btnChooseDir;
    }

    public JButton getBtnFiltrar() {
        return btnFiltrar;
    }

    public JButton getBtnReemplaçar() {
        return btnReemplaçar;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public JTree getTree() {
        return tree;
    }

    // Método per a agregar ActionListeners als botons
    public void addBtnChooseDirListener(ActionListener listenForBtnChooseDir) {
        btnChooseDir.addActionListener(listenForBtnChooseDir);
    }

    public void addBtnFiltrarListener(ActionListener listenForBtnFiltrar) {
        btnFiltrar.addActionListener(listenForBtnFiltrar);
    }

    public void addBtnReemplaçarListener(ActionListener listenForBtnReemplaçar) {
        btnReemplaçar.addActionListener(listenForBtnReemplaçar);
    }
}
