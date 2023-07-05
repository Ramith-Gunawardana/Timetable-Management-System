package GUI;

import DB.Classroom;
import DB.DBCon;
import DB.Subject;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Ramith Gunawardana
 */
public class ClassroomUI extends javax.swing.JFrame {
    static JFrame dashboard;
    private static int userRole = 0;
    /**
     * Creates new form Classroom
     */
    public ClassroomUI(int role, JFrame dashboard) {
        this.userRole = role;
        this.dashboard = dashboard;
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/icon.png"));
        this.setIconImage(icon.getImage());
        
        initComponents();
        modifyTabPane();
        modifyTable();
        try {
            loadTableData();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.WARNING_MESSAGE);
        }
        setCharLimit();
        uac();
    }
    private void loadTableData() throws SQLException,ClassNotFoundException{
        //class table
        String department,floor,number,capacity = "";
        
        DefaultTableModel model = (DefaultTableModel) tblClassroom.getModel();
        model.setRowCount(0);
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT department,floor,number,capacity FROM classroom order by(department) asc");
            
            while(rs.next()){
                department = rs.getString(1);
                floor = rs.getString(2);
                number = rs.getString(3);
                capacity = rs.getString(4);
                
                String[] data = {department,floor,number,capacity};
                model.addRow(data);
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
        
        //subject table
        String subjectid,module,facultyid= "";
        
        DefaultTableModel model1 = (DefaultTableModel) tblSubject.getModel();
        model1.setRowCount(0);
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT subjectid,module,facultyid FROM subject");
            
            while(rs.next()){
                subjectid = rs.getString(1);
                module = rs.getString(2);
                facultyid = rs.getString(3);
                
                String[] data = {subjectid,module,facultyid};
                model1.addRow(data);
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
    }
    private void modifyTabPane(){
        
        //tab pane
        UIManager.put("TabbedPane.contentOpaque",  false);  //removes background of tab content (not tab title)
        tabPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI(){
            protected void paintContentBorder(Graphics g,int tabPlacement,int selectedIndex){} //removes tab content border.
            protected void paintTabBorder(Graphics g, int tabPlacement,int tabIndex,int x, int y, int w, int h,boolean isSelected){} //removes tab (title) border.
        });
        
        //spinner
        spinnerNumber.getEditor().getComponent(0).setBackground(new Color(70,164,214));
        spinnerNumber.setBackground(new Color(70,164,214));
        spinnerNumber.getEditor().getComponent(0).setForeground(Color.white);
        spinnerNumber.setForeground(Color.white);
        
        spinnerCapacity.getEditor().getComponent(0).setBackground(new Color(70,164,214));
        spinnerCapacity.setBackground(new Color(70,164,214));
        spinnerCapacity.getEditor().getComponent(0).setForeground(Color.white);
        spinnerCapacity.setForeground(Color.white);
   }
    private void modifyTable(){
        //table
        Color background = new Color(0,0,0,0);
        tblClassroom.setBackground(background);
        tblSubject.setBackground(background);
        
        ((DefaultTableCellRenderer)tblClassroom.getDefaultRenderer(Object.class)).setBackground(background);
        ((DefaultTableCellRenderer)tblSubject.getDefaultRenderer(Object.class)).setBackground(background);
        
        tblClassroom.setForeground(Color.white);
        tblSubject.setForeground(Color.white);
        
        
        tblClassroom.setSelectionBackground(new Color(41,199,255));
        tblSubject.setSelectionBackground(new Color(41,199,255));
        
        tblClassroom.setSelectionForeground(Color.black);
        tblSubject.setSelectionForeground(Color.black);
        
        tblClassroom.setOpaque(false);
        tblSubject.setOpaque(false);
        
        scrollClassroom.getViewport().setOpaque(false);
        scrollSubject.getViewport().setOpaque(false);
        
        tblClassroom.setShowGrid(false);
        tblSubject.setShowGrid(false);
        
        tblClassroom.setGridColor(new Color(35,147,186));
        tblSubject.setGridColor(new Color(35,147,186));
        
        Color color1 = new Color(35,147,186);
        Color color2 = new Color(47,201,255);
        
        scrollClassroom.setBackground(color1);
        scrollSubject.setBackground(color2);
        
        scrollClassroom.setOpaque(false);
        scrollSubject.setOpaque(false);
        
        ((DefaultTableCellRenderer)tblClassroom.getDefaultRenderer(Object.class)).setOpaque(false);
        scrollClassroom.getViewport().setOpaque(false);
        ((DefaultTableCellRenderer)tblSubject.getDefaultRenderer(Object.class)).setOpaque(false);
        scrollSubject.getViewport().setOpaque(false);
        
        scrollClassroom.setBorder(BorderFactory.createEmptyBorder());
        scrollSubject.setBorder(BorderFactory.createEmptyBorder());
        
        scrollSubject.getVerticalScrollBar().setUnitIncrement(16);
        scrollSubject.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                
                this.thumbColor = color1;
                this.thumbHighlightColor = color1;
                this.thumbDarkShadowColor = color1;
                this.thumbLightShadowColor = color1;
                
                this.trackColor = color2;
                this.trackHighlightColor = color2;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override    
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
        
        scrollClassroom.getVerticalScrollBar().setUnitIncrement(16);
        scrollClassroom.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                
                this.thumbColor = color1;
                this.thumbHighlightColor = color1;
                this.thumbDarkShadowColor = color1;
                this.thumbLightShadowColor = color1;
                
                this.trackColor = color2;
                this.trackHighlightColor = color2;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override    
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
        
        JTableHeader thClassroom = tblClassroom.getTableHeader();
        JTableHeader thSubject = tblSubject.getTableHeader();
        
        thClassroom.setBackground(new Color(35,147,186));
        thSubject.setBackground(new Color(35,147,186));
        
        thClassroom.setForeground(Color.white);
        thSubject.setForeground(Color.white);
        
        thClassroom.setOpaque(false);
        thSubject.setOpaque(false);
        
        Font  f1  = new Font("Segoe UI", Font.PLAIN,  14);
        
        thClassroom.setFont(f1);
        thSubject.setFont(f1);
        
        TableCellRenderer rendererFromHeader = tblClassroom.getTableHeader().getDefaultRenderer();
        TableCellRenderer rendererFromHeader1 = tblSubject.getTableHeader().getDefaultRenderer();
        
        JLabel headerLabel = (JLabel) rendererFromHeader;
        JLabel headerLabel1 = (JLabel) rendererFromHeader1;
        
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel1.setHorizontalAlignment(JLabel.CENTER);
        
//        tblClassroom.setColumnSelectionAllowed(true);
//        tblSubject.setColumnSelectionAllowed(true);
        
        tblClassroom.setRowSelectionAllowed(true);
        tblSubject.setRowSelectionAllowed(true);
        
        tblClassroom.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblSubject.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        
        tblClassroom.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblClassroom.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblClassroom.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblClassroom.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        tblSubject.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblSubject.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblSubject.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        
    }
    
    private void setCharLimit(){
       txtDepartment.setDocument(new JTextFieldLimit(10));
       txtFloor.setDocument(new JTextFieldLimit(10));
       txtSubID.setDocument(new JTextFieldLimit(8));
       txtSubName.setDocument(new JTextFieldLimit(20));
       txtFacMem.setDocument(new JTextFieldLimit(20));
    }
    
    private void uac(){
        if(userRole==1 ||userRole==2 ){ //admin or fac member
        }else{ // staff
            tabPane.remove(1);
            tabPane.remove(2);
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

        btnBack = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tabPane = new javax.swing.JTabbedPane();
        panelAllClassroom = new javax.swing.JPanel();
        scrollClassroom = new javax.swing.JScrollPane();
        tblClassroom = new javax.swing.JTable();
        panelClassroom = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDepartment = new javax.swing.JTextField();
        txtFloor = new javax.swing.JTextField();
        spinnerNumber = new javax.swing.JSpinner();
        btnNewClassroom = new javax.swing.JButton();
        btnUpdateClassroom = new javax.swing.JButton();
        btnDeleteClassroom = new javax.swing.JButton();
        btnClearClassroom = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        spinnerCapacity = new javax.swing.JSpinner();
        panelAllSubject = new javax.swing.JPanel();
        scrollSubject = new javax.swing.JScrollPane();
        tblSubject = new javax.swing.JTable();
        panelSubject = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtSubID = new javax.swing.JTextField();
        txtSubName = new javax.swing.JTextField();
        txtFacMem = new javax.swing.JTextField();
        btnSearchSubID = new javax.swing.JButton();
        btnSearchFacMem = new javax.swing.JButton();
        btnNewSubject = new javax.swing.JButton();
        btnUpdateSubject = new javax.swing.JButton();
        btnDeleteSubject = new javax.swing.JButton();
        btnClearSubject = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setTitle("Classrooms and Subjects");
        setMinimumSize(new java.awt.Dimension(635, 500));
        getContentPane().setLayout(null);

        btnBack.setBackground(new java.awt.Color(155, 182, 206));
        btnBack.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back.png"))); // NOI18N
        btnBack.setText("Back ");
        btnBack.setToolTipText("Click to go back to dashboard");
        btnBack.setBorder(null);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.setMaximumSize(new java.awt.Dimension(47, 12));
        btnBack.setMinimumSize(new java.awt.Dimension(47, 12));
        btnBack.setPreferredSize(new java.awt.Dimension(47, 12));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack);
        btnBack.setBounds(60, 40, 80, 30);

        btnRefresh.setBackground(new java.awt.Color(17, 107, 195));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/refresh.png"))); // NOI18N
        btnRefresh.setText("Refresh ");
        btnRefresh.setToolTipText("Click to refresh data");
        btnRefresh.setBorder(null);
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.setMaximumSize(new java.awt.Dimension(47, 12));
        btnRefresh.setMinimumSize(new java.awt.Dimension(47, 12));
        btnRefresh.setPreferredSize(new java.awt.Dimension(47, 12));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh);
        btnRefresh.setBounds(480, 40, 90, 30);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Classrooms and Subjects");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(79, 35, 476, 32);

        tabPane.setBackground(new java.awt.Color(14, 131, 189));
        tabPane.setForeground(new java.awt.Color(255, 255, 255));
        tabPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabPane.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        panelAllClassroom.setOpaque(false);

        tblClassroom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblClassroom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Department", "Floor", "Number", "Capacity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClassroom.setRowHeight(25);
        tblClassroom.getTableHeader().setReorderingAllowed(false);
        scrollClassroom.setViewportView(tblClassroom);

        javax.swing.GroupLayout panelAllClassroomLayout = new javax.swing.GroupLayout(panelAllClassroom);
        panelAllClassroom.setLayout(panelAllClassroomLayout);
        panelAllClassroomLayout.setHorizontalGroup(
            panelAllClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllClassroomLayout.createSequentialGroup()
                .addComponent(scrollClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 35, Short.MAX_VALUE))
        );
        panelAllClassroomLayout.setVerticalGroup(
            panelAllClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllClassroomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollClassroom, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("  All classrooms  ", panelAllClassroom);

        panelClassroom.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Department");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Floor");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Room Number");

        txtDepartment.setBackground(new java.awt.Color(70, 164, 214));
        txtDepartment.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDepartment.setForeground(new java.awt.Color(255, 255, 255));
        txtDepartment.setToolTipText("Maximum 10 characters are allowed");
        txtDepartment.setBorder(null);
        txtDepartment.setCaretColor(new java.awt.Color(255, 255, 255));

        txtFloor.setBackground(new java.awt.Color(70, 164, 214));
        txtFloor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFloor.setForeground(new java.awt.Color(255, 255, 255));
        txtFloor.setToolTipText("Maximum 10 characters are allowed");
        txtFloor.setBorder(null);
        txtFloor.setCaretColor(new java.awt.Color(255, 255, 255));
        txtFloor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFloorActionPerformed(evt);
            }
        });

        spinnerNumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        spinnerNumber.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        spinnerNumber.setBorder(null);

        btnNewClassroom.setBackground(new java.awt.Color(50, 107, 194));
        btnNewClassroom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnNewClassroom.setForeground(new java.awt.Color(255, 255, 255));
        btnNewClassroom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/new.png"))); // NOI18N
        btnNewClassroom.setText("Create new ");
        btnNewClassroom.setToolTipText("Click to add new classroom");
        btnNewClassroom.setBorder(null);
        btnNewClassroom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewClassroom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewClassroomActionPerformed(evt);
            }
        });

        btnUpdateClassroom.setBackground(new java.awt.Color(50, 107, 194));
        btnUpdateClassroom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUpdateClassroom.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateClassroom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/update.png"))); // NOI18N
        btnUpdateClassroom.setText("Update ");
        btnUpdateClassroom.setToolTipText("Click to update student capacity");
        btnUpdateClassroom.setBorder(null);
        btnUpdateClassroom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateClassroom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateClassroomActionPerformed(evt);
            }
        });

        btnDeleteClassroom.setBackground(new java.awt.Color(50, 107, 194));
        btnDeleteClassroom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDeleteClassroom.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteClassroom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete.png"))); // NOI18N
        btnDeleteClassroom.setText("Delete ");
        btnDeleteClassroom.setToolTipText("Click to delete classroom");
        btnDeleteClassroom.setBorder(null);
        btnDeleteClassroom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteClassroomActionPerformed(evt);
            }
        });

        btnClearClassroom.setBackground(new java.awt.Color(50, 107, 194));
        btnClearClassroom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnClearClassroom.setForeground(new java.awt.Color(255, 255, 255));
        btnClearClassroom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clear.png"))); // NOI18N
        btnClearClassroom.setText("Clear ");
        btnClearClassroom.setToolTipText("Click to clear the fields");
        btnClearClassroom.setBorder(null);
        btnClearClassroom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearClassroomActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Student Capacity");

        spinnerCapacity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        spinnerCapacity.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        spinnerCapacity.setBorder(null);

        javax.swing.GroupLayout panelClassroomLayout = new javax.swing.GroupLayout(panelClassroom);
        panelClassroom.setLayout(panelClassroomLayout);
        panelClassroomLayout.setHorizontalGroup(
            panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClassroomLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelClassroomLayout.createSequentialGroup()
                        .addComponent(btnNewClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClearClassroom, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                    .addGroup(panelClassroomLayout.createSequentialGroup()
                        .addGroup(panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinnerCapacity)
                            .addComponent(txtFloor, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                            .addComponent(spinnerNumber)
                            .addComponent(txtDepartment))))
                .addGap(68, 68, 68))
        );
        panelClassroomLayout.setVerticalGroup(
            panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClassroomLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFloor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(spinnerNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(spinnerCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(panelClassroomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        tabPane.addTab("  Classrooms  ", panelClassroom);

        panelAllSubject.setOpaque(false);

        tblSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblSubject.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject ID", "Subject Name", "Faculty Member"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSubject.setRowHeight(25);
        tblSubject.getTableHeader().setReorderingAllowed(false);
        scrollSubject.setViewportView(tblSubject);

        javax.swing.GroupLayout panelAllSubjectLayout = new javax.swing.GroupLayout(panelAllSubject);
        panelAllSubject.setLayout(panelAllSubjectLayout);
        panelAllSubjectLayout.setHorizontalGroup(
            panelAllSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllSubjectLayout.createSequentialGroup()
                .addComponent(scrollSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 34, Short.MAX_VALUE))
        );
        panelAllSubjectLayout.setVerticalGroup(
            panelAllSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllSubjectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabPane.addTab("  All subjects  ", panelAllSubject);

        panelSubject.setOpaque(false);

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Subject ID");

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Subject Name");

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Faculty Member Username");

        txtSubID.setBackground(new java.awt.Color(70, 164, 214));
        txtSubID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSubID.setForeground(new java.awt.Color(255, 255, 255));
        txtSubID.setToolTipText("Maximum 8 characters are allowed");
        txtSubID.setBorder(null);
        txtSubID.setCaretColor(new java.awt.Color(255, 255, 255));

        txtSubName.setBackground(new java.awt.Color(70, 164, 214));
        txtSubName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSubName.setForeground(new java.awt.Color(255, 255, 255));
        txtSubName.setToolTipText("Maximum 20 characters are allowed");
        txtSubName.setBorder(null);
        txtSubName.setCaretColor(new java.awt.Color(255, 255, 255));

        txtFacMem.setBackground(new java.awt.Color(70, 164, 214));
        txtFacMem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFacMem.setForeground(new java.awt.Color(255, 255, 255));
        txtFacMem.setToolTipText("Maximum 20 characters are allowed");
        txtFacMem.setBorder(null);
        txtFacMem.setCaretColor(new java.awt.Color(255, 255, 255));

        btnSearchSubID.setBackground(new java.awt.Color(30, 96, 131));
        btnSearchSubID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSearchSubID.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchSubID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        btnSearchSubID.setText("Search ");
        btnSearchSubID.setToolTipText("Enter subject id to search");
        btnSearchSubID.setBorder(null);
        btnSearchSubID.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchSubID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchSubIDActionPerformed(evt);
            }
        });

        btnSearchFacMem.setBackground(new java.awt.Color(30, 96, 131));
        btnSearchFacMem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSearchFacMem.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchFacMem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        btnSearchFacMem.setText("Search ");
        btnSearchFacMem.setToolTipText("Enter faculty member username to search");
        btnSearchFacMem.setBorder(null);
        btnSearchFacMem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchFacMem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchFacMemActionPerformed(evt);
            }
        });

        btnNewSubject.setBackground(new java.awt.Color(50, 107, 194));
        btnNewSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnNewSubject.setForeground(new java.awt.Color(255, 255, 255));
        btnNewSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/new.png"))); // NOI18N
        btnNewSubject.setText("Create new ");
        btnNewSubject.setToolTipText("Click to add new subject");
        btnNewSubject.setBorder(null);
        btnNewSubject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSubjectActionPerformed(evt);
            }
        });

        btnUpdateSubject.setBackground(new java.awt.Color(50, 107, 194));
        btnUpdateSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUpdateSubject.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/update.png"))); // NOI18N
        btnUpdateSubject.setText("Update ");
        btnUpdateSubject.setToolTipText("Click to update subject name or faculty member");
        btnUpdateSubject.setBorder(null);
        btnUpdateSubject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSubjectActionPerformed(evt);
            }
        });

        btnDeleteSubject.setBackground(new java.awt.Color(50, 107, 194));
        btnDeleteSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDeleteSubject.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete.png"))); // NOI18N
        btnDeleteSubject.setText("Delete ");
        btnDeleteSubject.setToolTipText("Click to delete subject");
        btnDeleteSubject.setBorder(null);
        btnDeleteSubject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSubjectActionPerformed(evt);
            }
        });

        btnClearSubject.setBackground(new java.awt.Color(50, 107, 194));
        btnClearSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnClearSubject.setForeground(new java.awt.Color(255, 255, 255));
        btnClearSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clear.png"))); // NOI18N
        btnClearSubject.setText(" Clear ");
        btnClearSubject.setToolTipText("Click to clear the fields");
        btnClearSubject.setBorder(null);
        btnClearSubject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSubjectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSubjectLayout = new javax.swing.GroupLayout(panelSubject);
        panelSubject.setLayout(panelSubjectLayout);
        panelSubjectLayout.setHorizontalGroup(
            panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSubjectLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSubjectLayout.createSequentialGroup()
                        .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSubjectLayout.createSequentialGroup()
                                .addComponent(txtSubID, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSearchSubID, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                            .addComponent(txtSubName)
                            .addGroup(panelSubjectLayout.createSequentialGroup()
                                .addComponent(txtFacMem, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSearchFacMem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(panelSubjectLayout.createSequentialGroup()
                        .addComponent(btnNewSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btnDeleteSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClearSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelSubjectLayout.setVerticalGroup(
            panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSubjectLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSubID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchSubID, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtSubName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearchFacMem, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(txtFacMem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(53, 53, 53)
                .addGroup(panelSubjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        tabPane.addTab("  Subjects  ", panelSubject);

        getContentPane().add(tabPane);
        tabPane.setBounds(79, 96, 480, 320);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg6.png"))); // NOI18N
        jLabel9.setMaximumSize(new java.awt.Dimension(735, 645));
        jLabel9.setMinimumSize(new java.awt.Dimension(735, 645));
        jLabel9.setPreferredSize(new java.awt.Dimension(735, 645));
        getContentPane().add(jLabel9);
        jLabel9.setBounds(0, 0, 630, 480);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSubjectActionPerformed
        // TODO add your handling code here:
        String subjectid = txtSubID.getText().toUpperCase();
        String module = txtSubName.getText();
        String facultyid = txtFacMem.getText();
                
        Subject subject = new Subject(subjectid,module,facultyid);
        
        if(subjectid.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter subject code", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(module.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter subject name", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(facultyid.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter faculty member username", "Warning", JOptionPane.WARNING_MESSAGE);
        }try {
            if(subject.isFacMember()){
                try {
                    if(!subject.updateSubject()){
                        JOptionPane.showMessageDialog(rootPane, "Subject code doesn't found. Please check again." , "Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(rootPane, "Subject " +subjectid.toUpperCase() +" details updated successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
                }
                //backup
                try {
                    if(!subject.updateSubject_Backup()){
                        System.out.println("BACKUP: " + "Subject code doesn't found. Please check again.");
                    }else{
                        System.out.println("BACKUP: " + "Subject " +subjectid.toUpperCase() +" details updated successfully");
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    System.out.println("BACKUP: " +  "Can't connect to database" + ex);
                }
            }else{
                JOptionPane.showMessageDialog(rootPane, "No faculty members found. Please check username again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went wrong!.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateSubjectActionPerformed

    private void btnClearSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSubjectActionPerformed
        // TODO add your handling code here:
        txtSubID.setText("");
        txtSubName.setText("");
        txtFacMem.setText("");
    }//GEN-LAST:event_btnClearSubjectActionPerformed

    private void txtFloorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFloorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFloorActionPerformed

    private void btnNewClassroomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewClassroomActionPerformed
        // TODO add your handling code here:
        String department = txtDepartment.getText().toUpperCase();
        String floor = txtFloor.getText();
        int number = Integer.parseInt(spinnerNumber.getValue().toString());
        int capacity = Integer.parseInt(spinnerCapacity.getValue().toString());
        
        String classroomname = department.toUpperCase() + "-" + floor.toUpperCase() +"-" +spinnerNumber.getValue().toString();
                
        Classroom classroom = new Classroom(department,floor,number,capacity);
        
        if(department.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter department name", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(floor.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter floor number or name", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(number<=0){
            JOptionPane.showMessageDialog(rootPane, "Please enter room number", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(capacity<=0){
            JOptionPane.showMessageDialog(rootPane, "Please enter student capacity", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            try {
                if(!classroom.addClassroom()){
                    JOptionPane.showMessageDialog(rootPane, "Classroom already exsist in " +classroomname , "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Classroom " +classroomname +" created successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
            //backup
            try {
                if(!classroom.addClassroom_Backup()){
                    System.out.println("BACKUP: " + "Classroom already exsist in " +classroomname);
                }else{
                    System.out.println("BACKUP: " + "Classroom " +classroomname +" created successfully");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println("BACKUP: " + "Can't connect to database" + ex);
            }
        }
    }//GEN-LAST:event_btnNewClassroomActionPerformed

    private void btnUpdateClassroomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateClassroomActionPerformed
        // TODO add your handling code here:
        String department = txtDepartment.getText().toUpperCase();
        String floor = txtFloor.getText();
        int number = Integer.parseInt(spinnerNumber.getValue().toString());
        int capacity = Integer.parseInt(spinnerCapacity.getValue().toString());
        
        String classroomname = department.toUpperCase() + "-" + floor.toUpperCase() +"-" +spinnerNumber.getValue().toString();
                
        Classroom classroom = new Classroom(department,floor,number,capacity);
        
        if(department.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter department name", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(floor.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter floor number or name", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(number<=0){
            JOptionPane.showMessageDialog(rootPane, "Please enter room number", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(capacity<=0){
            JOptionPane.showMessageDialog(rootPane, "Please enter student capacity", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            try {
                if(!classroom.updateClassroom()){
                    JOptionPane.showMessageDialog(rootPane, "Classroom " +classroomname+ " not found" , "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(rootPane, classroomname +" student capacity updated successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
            //backup
            try {
                if(!classroom.updateClassroom_Backup()){
                    System.out.println("BACKUP: " + "Classroom " +classroomname+ " not found");
                }else{
                    System.out.println("BACKUP: " + classroomname +" student capacity updated successfully");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println("BACKUP: " + "Can't connect to database" + ex);
            }
        }
    }//GEN-LAST:event_btnUpdateClassroomActionPerformed

    private void btnDeleteClassroomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteClassroomActionPerformed
        // TODO add your handling code here:
        String department = txtDepartment.getText().toUpperCase();
        String floor = txtFloor.getText();
        int number = Integer.parseInt(spinnerNumber.getValue().toString());
        int capacity = Integer.parseInt(spinnerCapacity.getValue().toString());
        
        String classroomname = department.toUpperCase() + "-" + floor.toUpperCase() +"-" +spinnerNumber.getValue().toString();
                
        Classroom classroom = new Classroom(department,floor,number,capacity);
        
        if(department.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter department name", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(floor.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter floor number or name", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(number<=0){
            JOptionPane.showMessageDialog(rootPane, "Please enter room number", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            int result = JOptionPane.showConfirmDialog(rootPane, "This action can not be undone. Are you sure want to delete?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
                try {
                    if(!classroom.deleteClassroom()){
                        JOptionPane.showMessageDialog(rootPane, "Classroom " +classroomname+ " not found" , "Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(rootPane, classroomname +" deleted successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Can't delete! " + classroomname + " is linked to schedule(s) or resource(s)", "Warning", JOptionPane.WARNING_MESSAGE);
                }catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnDeleteClassroomActionPerformed

    private void btnClearClassroomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearClassroomActionPerformed
        // TODO add your handling code here:
        txtDepartment.setText("");
        txtFloor.setText("");
        spinnerNumber.setValue(0);
        spinnerCapacity.setValue(0);
    }//GEN-LAST:event_btnClearClassroomActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        try {
            loadTableData();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSearchSubIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchSubIDActionPerformed
        // TODO add your handling code here:
        String subjectid = txtSubID.getText().toUpperCase();
        String module = txtSubName.getText();
        String facultyid = txtFacMem.getText();
        
        Subject subject = new Subject(subjectid,"" , "");
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT module,facultyid FROM subject WHERE subjectid='"+subjectid+"'");
            
            while(rs.next()){
                    module = rs.getString(1);
                    facultyid = rs.getString(2);
                }
            if(subject.isSubjectExsit()){
                txtSubName.setText(module);
                txtFacMem.setText(facultyid);
            }else{
                JOptionPane.showMessageDialog(rootPane, "Incorect subject id. No subjects found.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went wrong!.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnSearchSubIDActionPerformed

    private void btnSearchFacMemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchFacMemActionPerformed
        // TODO add your handling code here:
        String facMem = txtFacMem.getText();
        Subject subject = new Subject("","" , facMem);
        try{
            if(subject.isFacMember()){
                JOptionPane.showMessageDialog(rootPane, "Faculty member is validated successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(rootPane, "No faculty members found. Please check username again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went wrong!.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnSearchFacMemActionPerformed

    private void btnNewSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSubjectActionPerformed
        // TODO add your handling code here:
        String subjectid = txtSubID.getText().toUpperCase();
        String module = txtSubName.getText();
        String facultyid = txtFacMem.getText();
                
        Subject subject = new Subject(subjectid,module,facultyid);
        
        if(subjectid.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter subject code", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(module.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter subject name", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(facultyid.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter faculty member username", "Warning", JOptionPane.WARNING_MESSAGE);
        }try {
            if(subject.isFacMember()){
                try {
                    if(!subject.addSubject()){
                        JOptionPane.showMessageDialog(rootPane, "Subject already exsist." , "Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(rootPane, "Subject " +subjectid.toUpperCase() +" created successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
                }
                //backup
                try {
                    if(!subject.addSubject_Backup()){
                        System.out.println("BACKUP: " + "Subject already exsist.");
                    }else{
                        System.out.println("BACKUP: " + "Subject " +subjectid.toUpperCase() +" created successfully");
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    System.out.println("BACKUP: " + "Can't connect to database" +ex);
                }
            }else{
                JOptionPane.showMessageDialog(rootPane, "No faculty members found. Please check username again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_btnNewSubjectActionPerformed

    private void btnDeleteSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSubjectActionPerformed
        // TODO add your handling code here:
        String subjectid = txtSubID.getText().toUpperCase();
                
        Subject subject = new Subject(subjectid,"","");
        
        if(subjectid.isEmpty()){
            JOptionPane.showMessageDialog(rootPane, "Please enter subject code", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            int result = JOptionPane.showConfirmDialog(rootPane, "This action can not be undone. Are you sure want to delete?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
                try {
                    if(!subject.deleteSubject()){
                        JOptionPane.showMessageDialog(rootPane, "Subject code doesn't found. Please check again." , "Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(rootPane, "Subject " +subjectid.toUpperCase() +" deleted successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Can't delete! "+ subjectid.toUpperCase() + " is linked to schedule(s)", "Error", JOptionPane.WARNING_MESSAGE);
                }catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnDeleteSubjectActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        dashboard.setState(java.awt.Frame.NORMAL);
        this.setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed

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
            java.util.logging.Logger.getLogger(ClassroomUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClassroomUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClassroomUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClassroomUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                    Logger.getLogger(ClassroomUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                ClassroomUI frame = new ClassroomUI(1,dashboard);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    class JTextFieldLimit extends PlainDocument {
    private int limit;
    JTextFieldLimit(int limit) {
       super();
       this.limit = limit;
    }
    JTextFieldLimit(int limit, boolean upper) {
       super();
       this.limit = limit;
    }
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
       if (str == null)
          return;
       if ((getLength() + str.length()) <= limit) {
          super.insertString(offset, str, attr);
       }
    }
 }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClearClassroom;
    private javax.swing.JButton btnClearSubject;
    private javax.swing.JButton btnDeleteClassroom;
    private javax.swing.JButton btnDeleteSubject;
    private javax.swing.JButton btnNewClassroom;
    private javax.swing.JButton btnNewSubject;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearchFacMem;
    private javax.swing.JButton btnSearchSubID;
    private javax.swing.JButton btnUpdateClassroom;
    private javax.swing.JButton btnUpdateSubject;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel panelAllClassroom;
    private javax.swing.JPanel panelAllSubject;
    private javax.swing.JPanel panelClassroom;
    private javax.swing.JPanel panelSubject;
    private javax.swing.JScrollPane scrollClassroom;
    private javax.swing.JScrollPane scrollSubject;
    private javax.swing.JSpinner spinnerCapacity;
    private javax.swing.JSpinner spinnerNumber;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblClassroom;
    private javax.swing.JTable tblSubject;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtFacMem;
    private javax.swing.JTextField txtFloor;
    private javax.swing.JTextField txtSubID;
    private javax.swing.JTextField txtSubName;
    // End of variables declaration//GEN-END:variables
}
