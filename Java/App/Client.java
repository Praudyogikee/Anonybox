package App;
import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;
public class Client extends javax.swing.JFrame {
    /** Creates new form main */
    public Client() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        ip_label = new javax.swing.JLabel();
        key_label = new javax.swing.JLabel();
        ip = new javax.swing.JTextField();
        key = new javax.swing.JTextField();
        connect = new javax.swing.JButton();
        username = new javax.swing.JTextField();
        password = new javax.swing.JTextField();
        username_label = new javax.swing.JLabel();
        password_label = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Anonybox R Client");
        setResizable(false);
        ip_label.setText("IP Address");
        key_label.setText("Encryption Key");
        ip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ipActionPerformed(evt);
            }
        });
        connect.setText("Connect");
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });
        username_label.setText("Username");
        password_label.setText("Password");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(key_label)
                            .addComponent(ip_label)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(password_label)
                                    .addComponent(username_label))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ip)
                            .addComponent(key, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(connect)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ip_label)
                    .addComponent(ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(key_label)
                    .addComponent(key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(username_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(connect)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }//GEN-END:initComponents
    private void ipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ipActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ipActionPerformed
    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        connection c = new connection(ip.getText(),key.getText(),username.getText(),password.getText());
        c.start();
    }//GEN-LAST:event_connectActionPerformed
    static class connection extends Thread {
        public String ip;
        public String key;
        public String username;
        public String password;
        connection(String ip, String key, String username, String password) {
            this.ip = ip;
            this.key = key;
            this.username = username;
            this.password = password;
            }
        @Override
        public void run() {
            try {
            byte[] iv = anonybox.EMPTY_IV;
            Socket s = new Socket(ip,1111);
            DataInputStream DIS = new DataInputStream(s.getInputStream());
            DataOutputStream DOS = new DataOutputStream(s.getOutputStream());
            anonybox.write(DOS, "TEST", key, iv);
                if(anonybox.read(DIS, key, iv).equals("DONE")) {
                    // DO NOTHING
                }else {
                    JOptionPane.showMessageDialog(null,"Invalid Encryption Key");
                    }
            anonybox.write(DOS, "IVGENERATE", key, iv);
                int length = DIS.readInt();
                if(length>0) {
                    iv = new byte[length];
                    DIS.readFully(iv, 0, iv.length);
                }
            anonybox.write(DOS, "TEST", key, iv);
                if (anonybox.read(DIS, key, iv).equals("DONE")) {
                    System.out.println("Secure Connection Established");
                    anonybox.write(DOS, "user "+username, key, iv);
                    if(anonybox.read(DIS, key, iv).contains("entered")) {
                        anonybox.write(DOS, "pass "+password, key, iv);
                        if(anonybox.read(DIS, key, iv).contains("Welcome")) {
                            JOptionPane.showMessageDialog(null, "Logged in !");
                            while (true) {
                                String cmd = JOptionPane.showInputDialog("Enter command");
                                anonybox.write(DOS, cmd, key, iv);
                                JOptionPane.showMessageDialog(null, anonybox.read(DIS, key, iv));
                                }
                        }else {
                            JOptionPane.showMessageDialog(null, "Wrong Password");
                            }
                    }else {
                        JOptionPane.showMessageDialog(null, "No username found");
                        }
                    }
            }catch(Exception e) {
                e.printStackTrace();
                }
            }
        }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing
                                                                   .UIManager
                                                                   .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing
                         .UIManager
                         .setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util
                .logging
                .Logger
                .getLogger(App.Client.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util
                .logging
                .Logger
                .getLogger(App.Client.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util
                .logging
                .Logger
                .getLogger(App.Client.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util
                .logging
                .Logger
                .getLogger(App.Client.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt
            .EventQueue
            .invokeLater(new Runnable() {
                public void run() {
                    new App.Client().setVisible(true);
                }
            });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connect;
    private javax.swing.JTextField ip;
    private javax.swing.JLabel ip_label;
    private javax.swing.JTextField key;
    private javax.swing.JLabel key_label;
    private javax.swing.JTextField password;
    private javax.swing.JLabel password_label;
    private javax.swing.JTextField username;
    private javax.swing.JLabel username_label;
    // End of variables declaration//GEN-END:variables

}
