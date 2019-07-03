/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protoircomparer02.resources;

import java.util.Base64;
import javax.swing.JOptionPane;

/**
 *
 * @author Aaron
 */
public class UserAuth extends javax.swing.JFrame {

    protected static String key;

    /**
     * Creates new form UserAuth
     */
    public UserAuth() {
        initComponents();
        isOffline.setSelected(false);
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userLab = new javax.swing.JLabel();
        passLab = new javax.swing.JLabel();
        isOffline = new javax.swing.JCheckBox();
        userName = new javax.swing.JTextField();
        enterBut = new javax.swing.JButton();
        passName = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Enter OSF User Credentials");
        setAlwaysOnTop(true);
        setResizable(false);

        userLab.setText("username:");

        passLab.setText("password:");

        isOffline.setText("work offline");
        isOffline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isOfflineActionPerformed(evt);
            }
        });

        enterBut.setText("Enter");
        enterBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(isOffline)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                        .addComponent(enterBut))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userLab)
                            .addComponent(passLab))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userName)
                            .addComponent(passName))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userLab))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passLab)
                    .addComponent(passName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(isOffline)
                    .addComponent(enterBut))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void isOfflineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isOfflineActionPerformed
        //get rid of the prompt in case the user does not have access to OSF
        if (isOffline.isSelected()) {
            dispose();
        }
    }//GEN-LAST:event_isOfflineActionPerformed

    private void enterButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterButActionPerformed
        //read in what is written in the username and password boxes
        String temp = userName.getText() + ":" + passName.getText();

        Base64.Encoder enc = Base64.getEncoder();

        //convert concatenated username and password to base 64
        try {
            byte[] out = enc.encode(temp.getBytes("UTF-8"));
            temp = null;
            key = new String(out, "UTF-8");
            out = null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error Encoding "
                    + "Auth Key",
                    JOptionPane.ERROR_MESSAGE);
        }

        try {
            UpdateDefs update = new UpdateDefs();
            update.Organize();
            update.AddDummies();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Invalid User"
                    + "name or Password",
                    JOptionPane.ERROR_MESSAGE);
        }
        
        dispose();
    }//GEN-LAST:event_enterButActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton enterBut;
    protected static javax.swing.JCheckBox isOffline;
    private javax.swing.JLabel passLab;
    private javax.swing.JPasswordField passName;
    private javax.swing.JLabel userLab;
    private javax.swing.JTextField userName;
    // End of variables declaration//GEN-END:variables
}