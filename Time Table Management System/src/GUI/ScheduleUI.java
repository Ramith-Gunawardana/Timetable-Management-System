package GUI;

import DB.Schedule;
import DB.Subject;
import static GUI.ClassroomUI.dashboard;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import com.itextpdf.text.Document;  
import com.itextpdf.text.DocumentException;  
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;  
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;

/**
 *
 * @author Ramith Gunawardana
 */
public class ScheduleUI extends javax.swing.JFrame {
    static JFrame scheduleUI;
    private static int userRole = 1;
    ImageIcon icon = new ImageIcon(getClass().getResource("/Images/icon.png"));
    JDialog d = new JDialog(scheduleUI);
    private static com.itextpdf.text.Font small = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14,com.itextpdf.text.Font.NORMAL);
    private static com.itextpdf.text.Font smallBold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14,com.itextpdf.text.Font.BOLD);
    private static com.itextpdf.text.Font medium = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18,com.itextpdf.text.Font.NORMAL);
    private static com.itextpdf.text.Font large = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 22,com.itextpdf.text.Font.NORMAL);
    //Date
    LocalDate today = LocalDate.now();
    int weekOfYear = 0;
    LocalDate monday,tuesday,wednesday,thursday,friday;

    /**
     * Creates new form Schedule
     */
    public ScheduleUI(int role, JFrame scheduleUI) {
        this.userRole = role;
        this.scheduleUI = scheduleUI;
        
        this.setIconImage(icon.getImage());
        
        initComponents();
        modifyTable();
        setDateRange();
        retrieveData();
    }
    
    private void setDateRange(){
        String output = txtDateRange.getText();
        weekOfYear = today.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        
        // Go backward to get Monday
        monday = today;
        if(monday.getDayOfWeek() == DayOfWeek.SUNDAY){
            weekOfYear -=1;
        }
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        tuesday = monday.plusDays(1);
        wednesday = monday.plusDays(2);
        thursday = monday.plusDays(3);
        friday = monday.plusDays(4);
        
        output = "Week " + weekOfYear + ": " + monday + " to " + friday;
        txtDateRange.setText(output);
    }
    
    
    private void modifyTable(){
        //table
        Color background = new Color(0,0,0,0);
        tblSchedule.setBackground(background);
        ((DefaultTableCellRenderer)tblSchedule.getDefaultRenderer(Object.class)).setBackground(background);
        tblSchedule.setForeground(Color.white);
        
        
        tblSchedule.setSelectionBackground(new Color(41,199,255));
        tblSchedule.setSelectionForeground(Color.black);
        
        
        scroll.setBackground(background);
        tblSchedule.setOpaque(false);
        
        scroll.setOpaque(false);
//        ((DefaultTableCellRenderer)tblSchedule.getDefaultRenderer(Object.class)).setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        tblSchedule.setShowGrid(true);
        tblSchedule.setGridColor(new Color(35,147,186));
        Color color = tblSchedule.getGridColor();
        scroll.setBorder(new MatteBorder(0, 1, 0, 0, color));
        
        JTableHeader th = tblSchedule.getTableHeader();
        th.setBackground(new Color(35,147,186));
        th.setForeground(Color.white);
        th.setOpaque(false);
        Font  f1  = new Font("Segoe UI", Font.PLAIN,  14);
        th.setFont(f1);
        TableCellRenderer rendererFromHeader = tblSchedule.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        tblSchedule.setColumnSelectionAllowed(true);
        tblSchedule.setRowSelectionAllowed(true);
        tblSchedule.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        tblSchedule.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblSchedule.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblSchedule.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblSchedule.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblSchedule.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblSchedule.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        
        
//        String str = "<html><center>"
//                + "Subject ID: "
//                + "<br>Subject Name: "
//                + "<br>Classroom: "
//                + "</center></html>";
//        
//        tblSchedule.setValueAt(str,0,2); 
        
    }
    private void clearTable(){
        for(int row=0; row<9; row++){
            for(int col=1; col<6; col++){
                if(row==3 || row==7){
                    continue;
                }else{
                    tblSchedule.setValueAt("", row, col);
                }
            }
        }
        
    }
    public void retrieveData(){
        
        for(int row=0; row<9; row++){
            for(int col=1; col<6; col++){
                if(row==3 || row==7){
                    continue;
                }else{
                    String subID = "";
                    String subName = "";
                    String classroom = "";
                    String timeStr,dateStr = "";
        
                    timeStr = switch (row) {
                        case 0 -> tblSchedule.getValueAt(0, 0).toString();
                        case 1 -> tblSchedule.getValueAt(1, 0).toString();
                        case 2 -> tblSchedule.getValueAt(2, 0).toString();
                        case 4 -> tblSchedule.getValueAt(4, 0).toString();
                        case 5 -> tblSchedule.getValueAt(5, 0).toString();
                        case 6 -> tblSchedule.getValueAt(6, 0).toString();
                        case 8 -> tblSchedule.getValueAt(8, 0).toString();
                        case 9 -> tblSchedule.getValueAt(9, 0).toString();
                        default -> "";
                    };

                    switch(col){
                        case 0:
                            break;
                        case 1:
                            dateStr = monday.toString();
                            break;
                        case 2:
                            dateStr = tuesday.toString();
                            break;
                        case 3:
                            dateStr = wednesday.toString();
                            break;
                        case 4:
                            dateStr = thursday.toString();
                            break;
                        case 5:
                            dateStr = friday.toString();
                            break;
                        default:
                            break;
                    }
                    
                    Schedule schedule = new Schedule("", "", "", 0, 0, dateStr, timeStr);
                    try {
                        if(schedule.isScheduleExsit()){
                            Subject sub = new Subject(schedule.getSubjectID(), "", "");
                            subID = schedule.getSubjectID();
                            subName = sub.getSubjectName();
                            classroom = schedule.getDepartment() + "-" + schedule.getFloor() + "-" + schedule.getNumber();
                        }
                    } catch (SQLException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    
                    String str = "<html><b><center>"
                    + subName
                    + "<br>" + subID
                    + "<br>" + classroom
                    + "</center></b></html>";
                    
                    tblSchedule.setValueAt(str, row, col);
                }
            }
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

        txtDateRange = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();
        btnPDF = new javax.swing.JButton();
        btnNextWeek = new javax.swing.JButton();
        btnPreviousWeek = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        tblSchedule = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setTitle("Schedules");
        setMinimumSize(new java.awt.Dimension(1000, 800));
        getContentPane().setLayout(null);

        txtDateRange.setBackground(new java.awt.Color(30, 118, 190));
        txtDateRange.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDateRange.setForeground(new java.awt.Color(255, 255, 255));
        txtDateRange.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDateRange.setToolTipText("Details of the week");
        txtDateRange.setBorder(null);
        txtDateRange.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtDateRange.setEnabled(false);
        getContentPane().add(txtDateRange);
        txtDateRange.setBounds(650, 20, 260, 30);

        btnRefresh.setBackground(new java.awt.Color(3, 133, 173));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/refresh.png"))); // NOI18N
        btnRefresh.setText("Refresh ");
        btnRefresh.setToolTipText("Click to refresh data");
        btnRefresh.setBorder(null);
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh);
        btnRefresh.setBounds(150, 20, 110, 30);

        btnPDF.setBackground(new java.awt.Color(3, 133, 173));
        btnPDF.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnPDF.setForeground(new java.awt.Color(255, 255, 255));
        btnPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/pdf.png"))); // NOI18N
        btnPDF.setText("Save Schedule ");
        btnPDF.setToolTipText("Click to save schedule as a .pdf file");
        btnPDF.setBorder(null);
        btnPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF);
        btnPDF.setBounds(280, 20, 140, 30);

        btnNextWeek.setBackground(new java.awt.Color(30, 118, 190));
        btnNextWeek.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/next.png"))); // NOI18N
        btnNextWeek.setToolTipText("Click to go to next week");
        btnNextWeek.setBorder(null);
        btnNextWeek.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNextWeek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextWeekActionPerformed(evt);
            }
        });
        getContentPane().add(btnNextWeek);
        btnNextWeek.setBounds(920, 20, 40, 30);

        btnPreviousWeek.setBackground(new java.awt.Color(30, 118, 190));
        btnPreviousWeek.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/previous.png"))); // NOI18N
        btnPreviousWeek.setToolTipText("Click to go to previous week");
        btnPreviousWeek.setBorder(null);
        btnPreviousWeek.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPreviousWeek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousWeekActionPerformed(evt);
            }
        });
        getContentPane().add(btnPreviousWeek);
        btnPreviousWeek.setBounds(600, 20, 40, 30);

        scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tblSchedule.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"08:00 AM - 09:00 AM", null, null, null, null, null},
                {"09:00 AM - 10:00 AM", null, null, null, null, null},
                {"10:00 AM - 11:00 AM", null, null, null, null, null},
                {"11:00 AM - 11:30 AM", "B", "R", "E", "A", "K"},
                {"11:30 AM - 12:30 PM", null, null, null, null, null},
                {"12:30 PM - 01:30 PM", null, null, null, null, null},
                {"01:30 PM - 02:30 PM", null, null, null, null, null},
                {"02:30 PM - 03:00 PM", "B", "R", "E", "A", "K"},
                {"03:00 PM - 04:00 PM", null, null, null, null, null}
            },
            new String [] {
                "Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSchedule.setToolTipText("Double click to update details");
        tblSchedule.setCellSelectionEnabled(true);
        tblSchedule.setRowHeight(72);
        tblSchedule.setShowGrid(true);
        tblSchedule.getTableHeader().setReorderingAllowed(false);
        tblSchedule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblScheduleMouseClicked(evt);
            }
        });
        scroll.setViewportView(tblSchedule);

        getContentPane().add(scroll);
        scroll.setBounds(31, 64, 929, 680);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Schedules");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(440, 20, 118, 32);

        btnBack.setBackground(new java.awt.Color(0, 109, 142));
        btnBack.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back.png"))); // NOI18N
        btnBack.setText("Back ");
        btnBack.setToolTipText("Click to go back to dashbaord");
        btnBack.setBorder(null);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack);
        btnBack.setBounds(31, 20, 95, 30);

        jLabel3.setBackground(new java.awt.Color(0, 109, 142));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg8.png"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(0, -10, 990, 790);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblScheduleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblScheduleMouseClicked
        // TODO add your handling code here:
        int row = tblSchedule.getSelectedRow();
        int col = tblSchedule.getSelectedColumn();
        String timeStr,dateStr = "";
        
//        Date date = dateChooser.getDate();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String dateStr = dateFormat.format(date);
//        System.out.println("\nDate: " + dateStr);
        
        timeStr = switch (row) {
            case 0 -> tblSchedule.getValueAt(0, 0).toString();
            case 1 -> tblSchedule.getValueAt(1, 0).toString();
            case 2 -> tblSchedule.getValueAt(2, 0).toString();
            case 4 -> tblSchedule.getValueAt(4, 0).toString();
            case 5 -> tblSchedule.getValueAt(5, 0).toString();
            case 6 -> tblSchedule.getValueAt(6, 0).toString();
            case 8 -> tblSchedule.getValueAt(8, 0).toString();
            case 9 -> tblSchedule.getValueAt(9, 0).toString();
            default -> "";
        };
        
        switch(col){
            case 0:
                break;
            case 1:
                dateStr = monday.toString();
                break;
            case 2:
                dateStr = tuesday.toString();
                break;
            case 3:
                dateStr = wednesday.toString();
                break;
            case 4:
                dateStr = thursday.toString();
                break;
            case 5:
                dateStr = friday.toString();
                break;
            default:
                break;
        }
        
         
        
        if(row== 3 || row == 7 || col == 0){
            tblSchedule.setSelectionBackground(new Color(0,0,0,0));
            tblSchedule.setSelectionForeground(Color.white);
            
            if (evt.getClickCount() == 2 && !evt.isConsumed()) {
                    evt.consume();
                    JOptionPane.showMessageDialog(rootPane, "Can't edit details.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }else{
            tblSchedule.setSelectionBackground(new Color(41,199,255));
            tblSchedule.setSelectionForeground(Color.black);
//            System.out.println("Selected Row: " + row);       
//            System.out.println("Selected Column: " + col);

//            System.out.println("Date: " + dateStr);  
//            System.out.println("Time: " + timeStr); 
            
            if (evt.getClickCount() == 2 && !evt.isConsumed()) {
                    evt.consume();
//                    System.out.println("Double Click");

                    //all frames to a array
                    Frame[] allFrames = Frame.getFrames();

                    //Iterate through the allFrames array
                    for(Frame fr : allFrames){  
                        //to get specific frame name
                        String specificFrameName = fr.getClass().getName();

                        if(specificFrameName.equals("GUI.ScheduleUI")){
                            scheduleUI = (JFrame) fr;
                        }
                    }
                    ScheduleEditUI frame = new ScheduleEditUI(userRole, scheduleUI, dateStr,timeStr);
                    frame.setResizable(false);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
            }

        }
    }//GEN-LAST:event_tblScheduleMouseClicked

    private void btnNextWeekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextWeekActionPerformed
        // TODO add your handling code here:
        d.setSize(200, 80);
        Label l = new Label("Loading...");
        l.setAlignment(Label.CENTER);
        d.add(l);
        d.setAlwaysOnTop(true);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
        
        today = today.plusDays(7);
        setDateRange();
        clearTable();
        retrieveData();
        
        d.setVisible(false);
    }//GEN-LAST:event_btnNextWeekActionPerformed

    private void btnPreviousWeekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousWeekActionPerformed
        // TODO add your handling code here:
        d.setSize(200, 80);
        Label l = new Label("Loading...");
        l.setAlignment(Label.CENTER);
        d.add(l);
        d.setAlwaysOnTop(true);
        d.setLocationRelativeTo(null);
        d.setVisible(true);

        today = today.minusDays(7);
        setDateRange();
        clearTable();
        retrieveData();
        
        d.setVisible(false);
    }//GEN-LAST:event_btnPreviousWeekActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        scheduleUI.setState(java.awt.Frame.NORMAL);
        this.setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        d.setSize(200, 80);
        Label l = new Label("Loading...");
        l.setAlignment(Label.CENTER);
        d.add(l);
        d.setAlwaysOnTop(true);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
        
        retrieveData();
        d.setVisible(false);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFActionPerformed
        // TODO add your handling code here:
        String path ="";
        String week = txtDateRange.getText();
        String weekNum = week.substring(0, 7);
        
        JFileChooser j = new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int x = j.showSaveDialog(this);
        
        if(x ==JFileChooser.APPROVE_OPTION){
            path = j.getSelectedFile().getPath();
        }
        
        Document doc = new Document();
        
        try {
            PdfWriter writer = PdfWriter.getInstance(doc,new FileOutputStream(path+"\\" + weekNum + " - Schedule.pdf"));
            doc.open();
            
            Paragraph p = new Paragraph("Time Table Management System",large);
            p.setAlignment(Element.ALIGN_CENTER);
            doc.add(p);   
            
            Paragraph p1 = new Paragraph("Schedule",medium);
            p1.setAlignment(Element.ALIGN_CENTER);
            doc.add(p1);
            
            Paragraph p2 = new Paragraph(txtDateRange.getText(),smallBold);
            p2.setAlignment(Element.ALIGN_CENTER);
            doc.add(p2);
            
            Paragraph p3 = new Paragraph(" ",smallBold);
            p2.setAlignment(Element.ALIGN_CENTER);
            doc.add(p3);
            
            PdfPTable tbl = new PdfPTable(6);
            tbl.addCell("Time");
            tbl.addCell("Monday");
            tbl.addCell("Tuesday");
            tbl.addCell("Wednesday");
            tbl.addCell("Thursday");
            tbl.addCell("Friday");
            
            for(int i=0; i<9; i++){
                 String Time = tblSchedule.getValueAt(i, 0).toString();
                 
                 String Monday = tblSchedule.getValueAt(i, 1).toString();
                 String mon1 = Monday.replace("<html>", "");
                 String mon2 = mon1.replace("<center>", "");
                 String mon3 = mon2.replace("<b>", "");
                 String mon4 = mon3.replace("<br>", "\n");
                 String mon5 = mon4.replace("<br>", "\n");
                 String mon6 = mon5.replace("</b>", "");
                 String mon7 = mon6.replace("</center>", "");
                 String mon8 = mon7.replace("</html>", "");
                 
                 String Tuesday = tblSchedule.getValueAt(i, 2).toString();
                 String tue1 = Tuesday.replace("<html>", "");
                 String tue2 = tue1.replace("<center>", "");
                 String tue3 = tue2.replace("<b>", "");
                 String tue4 = tue3.replace("<br>", "\n");
                 String tue5 = tue4.replace("<br>", "\n");
                 String tue6 = tue5.replace("</b>", "");
                 String tue7 = tue6.replace("</center>", "");
                 String tue8 = tue7.replace("</html>", "");
                 
                 String Wednesday = tblSchedule.getValueAt(i, 3).toString();
                 String wed1 = Wednesday.replace("<html>", "");
                 String wed2 = wed1.replace("<center>", "");
                 String wed3 = wed2.replace("<b>", "");
                 String wed4 = wed3.replace("<br>", "\n");
                 String wed5 = wed4.replace("<br>", "\n");
                 String wed6 = wed5.replace("</b>", "");
                 String wed7 = wed6.replace("</center>", "");
                 String wed8 = wed7.replace("</html>", "");
                 
                 String Thursday = tblSchedule.getValueAt(i, 4).toString();
                 String thu1 = Thursday.replace("<html>", "");
                 String thu2 = thu1.replace("<center>", "");
                 String thu3 = thu2.replace("<b>", "");
                 String thu4 = thu3.replace("<br>", "\n");
                 String thu5 = thu4.replace("<br>", "\n");
                 String thu6 = thu5.replace("</b>", "");
                 String thu7 = thu6.replace("</center>", "");
                 String thu8 = thu7.replace("</html>", "");
                 
                 String Friday = tblSchedule.getValueAt(i, 5).toString();
                 String fri1 = Friday.replace("<html>", "");
                 String fri2 = fri1.replace("<center>", "");
                 String fri3 = fri2.replace("<b>", "");
                 String fri4 = fri3.replace("<br>", "\n");
                 String fri5 = fri4.replace("<br>", "\n");
                 String fri6 = fri5.replace("</b>", "");
                 String fri7 = fri6.replace("</center>", "");
                 String fri8 = fri7.replace("</html>", "");
                 
                 
                 tbl.addCell(Time);
                 tbl.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                 tbl.addCell(mon8);
                 tbl.addCell(tue8);
                 tbl.addCell(wed8);
                 tbl.addCell(thu8);
                 tbl.addCell(fri8);
             }
             
             doc.add(tbl);
             JOptionPane.showMessageDialog(null, "' " +weekNum + " - Schedule.pdf '" + " saved at " +  path  , "Information", JOptionPane.INFORMATION_MESSAGE);
             doc.close();
             writer.close();
            
            
        } catch (FileNotFoundException | DocumentException ex) {
            JOptionPane.showMessageDialog(null, "Error while generating pdf", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPDFActionPerformed

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
            java.util.logging.Logger.getLogger(ScheduleUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScheduleUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScheduleUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScheduleUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(ScheduleUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                ScheduleUI frame = new ScheduleUI(userRole,dashboard);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnNextWeek;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnPreviousWeek;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable tblSchedule;
    private javax.swing.JTextField txtDateRange;
    // End of variables declaration//GEN-END:variables
}
