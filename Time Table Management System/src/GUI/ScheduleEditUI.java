/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import DB.DBCon;
import DB.Schedule;
import DB.Subject;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 *
 * @author Ramith Gunawardana
 */
public class ScheduleEditUI extends javax.swing.JFrame {
    static JFrame scheduleUI;
    private static int userRole = 0;
    private String date, time = ""; 
    /**
     * Creates new form ScheduleEdit
     */
    public ScheduleEditUI(int role, JFrame schedule, String date, String time) {
        this.userRole = role;
        this.scheduleUI = schedule;
        this.date = date;
        this.time = time;
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/icon.png"));
        this.setIconImage(icon.getImage());
        
        initComponents();
        customise();
        try {
            fillDetails();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.WARNING_MESSAGE);
        }
        uac();
    }
    private void fillDetails() throws SQLException, ClassNotFoundException{
        txtDate.setText(date);
        txtTime.setText(time);
        
        //show available classsrooms
        String department,floor,number,capacity = "";
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT department,floor,number,capacity FROM classroom");
            
            while(rs.next()){
                department = rs.getString(1);
                floor = rs.getString(2);
                number = rs.getString(3);
                capacity = rs.getString(4);
                
                comboClassroom.addItem(department + "-" + floor + "-" + number);
            }
            st.close();
            rs.close();
            con.close();
        }catch(SQLException e){
                throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        //show subject id
        String subid = "";
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT subjectid FROM subject");
            
            while(rs.next()){
                subid = rs.getString(1);
                
                comboSubject.addItem(subid);
            }
            st.close();
            rs.close();
            con.close();
        }catch(SQLException e){
                throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        
        //fill schedule details
        Schedule schedule = new Schedule("", "", "", 0, 0, date, time);
                        
        try {
            if(schedule.isScheduleExsit()){
                String classroom = schedule.getDepartment() + "-" + schedule.getFloor() + "-" + schedule.getNumber();
                
                for (int i=0; i<comboClassroom.getItemCount(); i++){
                    if(comboClassroom.getItemAt(i).toString().equals(classroom)){
                        comboClassroom.setSelectedIndex(i);
                    }
                }
                for (int i=0; i<comboSubject.getItemCount(); i++){
                    if(comboSubject.getItemAt(i).toString().equals(classroom)){
                        comboSubject.setSelectedIndex(i);
                    }
                }
                
                spinnerCapacity.setValue(schedule.getCapacity());
                
                Subject sub = new Subject(schedule.getSubjectID(), "", "");
                txtSubName.setText(sub.getSubjectName());
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
    }
    
    private void customise(){
        Color background = new Color(179,3,136);
        //combobox
        comboClassroom.setUI(new BasicComboBoxUI(){
            @Override
            public void paintCurrentValueBackground(Graphics g,Rectangle bounds,boolean hasFocus){

                 Color t = new Color(187,51,144);
                    if ( comboBox.isEnabled() )
                        g.setColor(background);
                    else
                        g.setColor(background);
                    g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
                    g.setColor(t);
                }
            }    
        );
        
        comboClassroom.setBackground(background);
        comboClassroom.setForeground(Color.white);
        comboClassroom.setBorder(BorderFactory.createEmptyBorder());
        comboClassroom.getEditor().getEditorComponent().setBackground(background);
        comboClassroom.getEditor().getEditorComponent().setForeground(Color.white);
        
        comboSubject.setUI(new BasicComboBoxUI(){
            @Override
            public void paintCurrentValueBackground(Graphics g,Rectangle bounds,boolean hasFocus){

                 Color t = new Color(187,51,144);
                    if ( comboBox.isEnabled() )
                        g.setColor(background);
                    else
                        g.setColor(background);
                    g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
                    g.setColor(t);
                }
            }    
        );
        
        comboSubject.setBackground(background);
        comboSubject.setForeground(Color.white);
        comboSubject.setBorder(BorderFactory.createEmptyBorder());
        comboSubject.getEditor().getEditorComponent().setBackground(background);
        comboSubject.getEditor().getEditorComponent().setForeground(Color.white);
        
        
        //spinner
        spinnerCapacity.getEditor().getComponent(0).setBackground(background);
        spinnerCapacity.setBackground(background);
        spinnerCapacity.getEditor().getComponent(0).setForeground(Color.white);
        spinnerCapacity.setForeground(Color.white);
    }
    
    private void uac(){
        if(userRole==1 ||userRole==2 ){ //admin or fac member
//            txtSubID.setEditable(true);
//            btnSearch.setEnabled(true);
//            comboClassroom.setEnabled(true);
//            spinnerCapacity.setEnabled(true);
        }else{ // staff
//            txtSubID.setEditable(false);
//            btnSearch.setEnabled(false);
//            comboClassroom.setEnabled(false);
//            spinnerCapacity.setEnabled(false);
            btnSave.setVisible(false);
            btnDelete.setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTime = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        comboSubject = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtSubName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        comboClassroom = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        spinnerCapacity = new javax.swing.JSpinner();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setTitle("Schedule Details");
        setMinimumSize(new java.awt.Dimension(560, 490));
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Schedule Details");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(67, 25, 420, 32);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Date");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(67, 96, 101, 20);

        txtDate.setBackground(new java.awt.Color(180, 2, 136));
        txtDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDate.setForeground(new java.awt.Color(255, 255, 255));
        txtDate.setBorder(null);
        txtDate.setCaretColor(new java.awt.Color(255, 255, 255));
        txtDate.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtDate.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtDate.setEnabled(false);
        getContentPane().add(txtDate);
        txtDate.setBounds(220, 100, 270, 20);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Time");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(67, 140, 101, 20);

        txtTime.setBackground(new java.awt.Color(180, 2, 136));
        txtTime.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTime.setForeground(new java.awt.Color(255, 255, 255));
        txtTime.setBorder(null);
        txtTime.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTime.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtTime.setEnabled(false);
        txtTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimeActionPerformed(evt);
            }
        });
        getContentPane().add(txtTime);
        txtTime.setBounds(220, 140, 270, 20);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Subject Code");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(67, 184, 101, 20);

        comboSubject.setBackground(new java.awt.Color(180, 2, 136));
        comboSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboSubject.setForeground(new java.awt.Color(255, 255, 255));
        comboSubject.setBorder(null);
        comboSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSubjectActionPerformed(evt);
            }
        });
        getContentPane().add(comboSubject);
        comboSubject.setBounds(220, 180, 270, 24);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Subject Name");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(67, 228, 101, 20);

        txtSubName.setBackground(new java.awt.Color(180, 2, 136));
        txtSubName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSubName.setForeground(new java.awt.Color(255, 255, 255));
        txtSubName.setBorder(null);
        txtSubName.setCaretColor(new java.awt.Color(255, 255, 255));
        txtSubName.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtSubName.setEnabled(false);
        txtSubName.setMinimumSize(new java.awt.Dimension(62, 26));
        txtSubName.setPreferredSize(new java.awt.Dimension(62, 26));
        getContentPane().add(txtSubName);
        txtSubName.setBounds(220, 220, 270, 26);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Available Classrooms");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(70, 260, 150, 40);

        comboClassroom.setBackground(new java.awt.Color(180, 2, 136));
        comboClassroom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboClassroom.setForeground(new java.awt.Color(255, 255, 255));
        comboClassroom.setBorder(null);
        getContentPane().add(comboClassroom);
        comboClassroom.setBounds(220, 270, 270, 24);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Student Capacity");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(70, 320, 110, 20);

        spinnerCapacity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        spinnerCapacity.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        spinnerCapacity.setBorder(null);
        getContentPane().add(spinnerCapacity);
        spinnerCapacity.setBounds(220, 320, 270, 24);

        btnCancel.setBackground(new java.awt.Color(81, 30, 168));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clear.png"))); // NOI18N
        btnCancel.setText("Cancel ");
        btnCancel.setToolTipText("Click to cancel edit panel");
        btnCancel.setBorder(null);
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel);
        btnCancel.setBounds(410, 380, 80, 40);

        btnSave.setBackground(new java.awt.Color(81, 30, 168));
        btnSave.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/schedule save.png"))); // NOI18N
        btnSave.setText("Save ");
        btnSave.setToolTipText("Click to save schedule");
        btnSave.setBorder(null);
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        getContentPane().add(btnSave);
        btnSave.setBounds(310, 380, 90, 40);

        btnDelete.setBackground(new java.awt.Color(81, 30, 168));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/schedule delete.png"))); // NOI18N
        btnDelete.setText("Delete ");
        btnDelete.setToolTipText("Click to delete schedule");
        btnDelete.setBorder(null);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnDelete);
        btnDelete.setBounds(220, 380, 80, 40);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg9.png"))); // NOI18N
        getContentPane().add(jLabel11);
        jLabel11.setBounds(0, 0, 550, 460);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimeActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        String subID = comboSubject.getSelectedItem().toString();
        String classroomStr = comboClassroom.getSelectedItem().toString();
        
        String[] classroom = classroomStr.split("-");
        String department = classroom[0];
        String floor = classroom[1];
        int number = Integer.parseInt(classroom[2]);
        int inputCapacity = Integer.parseInt(spinnerCapacity.getValue().toString());
        
        int capacity = 0;
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT capacity FROM classroom WHERE department='"+department+"' and  floor='"+floor+"' and number="+number+"");
            
            while(rs.next()){
                capacity = Integer.parseInt(rs.getString(1));
                
                comboClassroom.addItem(department + "-" + floor + "-" + number);
            }
            st.close();
            rs.close();
            con.close();
        }catch(SQLException e){
            try {
                throw new SQLException("Can't connect to database." + e);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }catch(ClassNotFoundException e){
            try {
                throw new ClassNotFoundException("Can't connect to database.");
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        if(comboSubject.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(rootPane, "Please select subject id", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(inputCapacity==0){
            JOptionPane.showMessageDialog(rootPane, "Please enter number of students", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            Subject subject = new Subject(subID,"","");
            
            try {
                if(!subject.isSubjectExsit()){
                    JOptionPane.showMessageDialog(rootPane, "Subject not found. Please check again subject code.", "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    if(inputCapacity>capacity){
                        JOptionPane.showMessageDialog(rootPane, "Student capacity exceeded. Maximum is " + capacity, "Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        Schedule schedule = new Schedule(subID, department, floor, number, inputCapacity, date, time);
                        
                        if(schedule.isScheduleExsit()){
                            //backup
                            if(schedule.updateSchedule_Backup()){
                                System.out.println("BACKUP: " + "Schedule updated successfully");
                            }else{
                                System.out.println("BACKUP: " + "Something went wrong!");
                            }
                            
                            if(schedule.updateSchedule()){
                                JOptionPane.showMessageDialog(rootPane, "Schedule updated successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                                ScheduleUI frame = (ScheduleUI) scheduleUI;
                                frame.retrieveData();
                                this.dispose();
                            }else{
                                JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            //backup
                            if(schedule.addSchedule_Backup()){
                                System.out.println("BACKUP: " + "Schedule saved successfully");
                            }else{
                                System.out.println("BACKUP: " + "Something went wrong!");
                            }
                            
                            if(schedule.addSchedule()){
                                JOptionPane.showMessageDialog(rootPane, "Schedule saved successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                                ScheduleUI frame = (ScheduleUI) scheduleUI;
                                frame.retrieveData();
                                this.dispose();
                            }else{
                                JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            
                        }
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database. ", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        Schedule schedule = new Schedule("", "", "", 0, 0, date, time);
                        
        try {
            if(schedule.isScheduleExsit()){
                int result = JOptionPane.showConfirmDialog(rootPane, "This action can not be undone. Are you sure want to delete?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        if(schedule.deleteScedule()){
                            JOptionPane.showMessageDialog(rootPane, "Schedule deleted successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
            }else{
                JOptionPane.showMessageDialog(rootPane, "Schedule is empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void comboSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSubjectActionPerformed
        // TODO add your handling code here:
        String subjectid = comboSubject.getSelectedItem().toString();
        String module = txtSubName.getText();
        
        Subject subject = new Subject(subjectid,"" , "");
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT module FROM subject WHERE subjectid='"+subjectid+"'");
            
            while(rs.next()){
                    module = rs.getString(1);
                }
            if(subject.isSubjectExsit()){
                txtSubName.setText(module);
            }else{
                JOptionPane.showMessageDialog(rootPane, "Incorect subject id. No subjects found.", "Warning", JOptionPane.WARNING_MESSAGE);
                txtSubName.setText("");
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went wrong!.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_comboSubjectActionPerformed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScheduleEditUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScheduleEditUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScheduleEditUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScheduleEditUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(ScheduleEditUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                ScheduleEditUI  frame = new ScheduleEditUI(1, scheduleUI, "","");
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> comboClassroom;
    private javax.swing.JComboBox<String> comboSubject;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSpinner spinnerCapacity;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtSubName;
    private javax.swing.JTextField txtTime;
    // End of variables declaration//GEN-END:variables
}
