/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client1.GUI;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Luismi
 */
public class Chat extends javax.swing.JFrame { 
    /**
     * Creates new form Chat
     */
    public Chat() {
        initComponents();
        //this.setDefaultCloseOperation();
        addWindowListener(new java.awt.event.WindowAdapter(){

        public void windowOpened(java.awt.event.WindowEvent evt){
            JOptionPane.showMessageDialog(null, "Bienvenido a BirdBeep");
            cargarLista();
            //Color JFrame
            //Color fondo = new Color( hex("FF7373") );
            getContentPane().setBackground(Color.LIGHT_GRAY);
            //Color JPanel
            ///Color info=new Color(hex("FFAEAE"));
            PanelInfo.setBackground(Color.DARK_GRAY);
           //Color fondo Lista
            //Color lista=new Color(hex("FFAEAE"));
            RecientesList.setBackground(Color.DARK_GRAY);
            Color fuente =new Color(hex("000000"));
            txtChats.setForeground(fuente);
            txtInformacion.setForeground(fuente);
            txtNombre.setForeground(fuente);
            RecientesList.setForeground(fuente);
            Usuario.setForeground(fuente);
        }

        public void windowClosing(java.awt.event.WindowEvent evt){
        }

        public void windowActivated(java.awt.event.WindowEvent evt){
            
        }
    });
                 }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        PanelInfo = new javax.swing.JPanel();
        txtInformacion = new javax.swing.JLabel();
        Usuario = new javax.swing.JLabel();
        txtNombre = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        RecientesList = new javax.swing.JList();
        txtChats = new javax.swing.JLabel();
        btnConversacion = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("BirBeep");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtInformacion.setText("Informacion Usuario");

        Usuario.setText("jLabel2");

        txtNombre.setText("Usuario");

        javax.swing.GroupLayout PanelInfoLayout = new javax.swing.GroupLayout(PanelInfo);
        PanelInfo.setLayout(PanelInfoLayout);
        PanelInfoLayout.setHorizontalGroup(
            PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNombre)
                .addGroup(PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelInfoLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(txtInformacion))
                    .addGroup(PanelInfoLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(Usuario)))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        PanelInfoLayout.setVerticalGroup(
            PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtInformacion)
                .addGap(43, 43, 43)
                .addGroup(PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Usuario)
                    .addComponent(txtNombre))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(RecientesList);

        txtChats.setText("Chat Recientes");

        btnConversacion.setText("Iniciar conversacion");
        btnConversacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConversacionActionPerformed(evt);
            }
        });

        jButton1.setText("Nueva Conversacion");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtChats)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnConversacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtChats)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(btnConversacion)
                        .addGap(104, 104, 104)
                        .addComponent(jButton1)))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        NuevaConversacion();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnConversacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConversacionActionPerformed
        cargarchat();
    }//GEN-LAST:event_btnConversacionActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cerrar();
    }//GEN-LAST:event_formWindowClosing

    public void cerrar(){
    Object [] opciones ={"Aceptar","Cancelar"};
    int eleccion = JOptionPane.showOptionDialog(rootPane,"�Desea salir de Birdbeep?","Birdbeep",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
    if (eleccion == JOptionPane.YES_OPTION){
        System.exit(0);
    }else{}
    }
    
    private int hex( String color_hex )
    {
        return Integer.parseInt(color_hex,  16 );
    }
    
    
    public void cargarLista(){      
        ArrayList<String>elementos=new ArrayList<>();
        elementos.add("Jose");
        elementos.add("Juan");
        elementos.add("Alvaro");
        elementos.add("Sebastian");
        elementos.add("Ruben");
        elementos.add("Pedro");
        DefaultListModel modelo = new DefaultListModel();
        
        for (String elemento : elementos) {
            modelo.addElement(elemento);
        }

        RecientesList.setModel(modelo);
    }
    
    public void cargarchat(){
       String contacto=(String) RecientesList.getSelectedValue();
        if (contacto==null){
            JOptionPane.showMessageDialog(null, "Marca un contacto o inicia una nueva conversaci�n");
        }else{ 
            Conversacion h1 = new Conversacion();
            h1.setVisible(true);
            h1.txtUsuario.setText(contacto);
        }
       
    }
    
    public void NuevaConversacion(){
        NuevaConversacion h1 = new NuevaConversacion();
        h1.setVisible(true);
    }
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chat().setVisible(true);               
            }
        });

    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelInfo;
    private javax.swing.JList RecientesList;
    public static javax.swing.JLabel Usuario;
    private javax.swing.JButton btnConversacion;
    private javax.swing.JButton jButton1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel txtChats;
    private javax.swing.JLabel txtInformacion;
    private javax.swing.JLabel txtNombre;
    // End of variables declaration//GEN-END:variables

}