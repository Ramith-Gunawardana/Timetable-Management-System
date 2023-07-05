package GUI;

import DB.DBCon;
import DB.Resource;
import com.formdev.flatlaf.FlatDarkLaf;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Ramith Gunawardana
 */
public class ResourceUI extends javax.swing.JFrame {
    static JFrame dashboard;
    private static int userRole = 0;
    private static com.itextpdf.text.Font small = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14,com.itextpdf.text.Font.NORMAL);
    private static com.itextpdf.text.Font smallBold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14,com.itextpdf.text.Font.BOLD);
    private static com.itextpdf.text.Font medium = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18,com.itextpdf.text.Font.NORMAL);
    private static com.itextpdf.text.Font large = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 22,com.itextpdf.text.Font.NORMAL);
    
    /**
     * Creates new form Resource
     */
    public ResourceUI(int role, JFrame dashboard) {
        this.userRole = role;
        this.dashboard = dashboard;
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/icon.png"));
        this.setIconImage(icon.getImage());
        
        initComponents();
        modifyTabPane();
        try {
            fillDetails();
            loadTableData();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
        uac();
    }
    private void loadTableData() throws SQLException,ClassNotFoundException{
        
        String classroom = comboClassroom.getSelectedItem().toString();
        String[] classStr = classroom.split("-");
        
        String department = classStr[0];
        String floor = classStr[1];
        String number = classStr[2];
        String name = "";
        int qty = 0;
        
        //table resoure
        DefaultTableModel model = (DefaultTableModel) tblResource.getModel();
        model.setRowCount(0);
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT name,qty FROM resource WHERE department='"+department+"' AND floor='"+floor+"' AND number="+number+"");
            
            while(rs.next()){
                name = rs.getString(1);
                qty = Integer.parseInt(rs.getString(2));
                
                String[] data = {name, String.valueOf(qty)};
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
    }
    
    private void fillDetails() throws SQLException, ClassNotFoundException{
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
                comboClassroom1.addItem(department + "-" + floor + "-" + number);
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
        
        //table
        Color background = new Color(0,0,0,0);
        tblResource.setBackground(background);
        ((DefaultTableCellRenderer)tblResource.getDefaultRenderer(Object.class)).setBackground(background);
        tblResource.setForeground(Color.white);
        
        
        scroll.setBackground(background);
        tblResource.setOpaque(false);
        
        scroll.setOpaque(false);
        ((DefaultTableCellRenderer)tblResource.getDefaultRenderer(Object.class)).setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        tblResource.setShowGrid(false);
//        tblResource.setGridColor(new Color(115,27,161));
        scroll.setBorder(new MatteBorder(0, 1, 0, 0, new Color(0,0,0,0)));
        
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                
                Color background  = new Color(51,12,72);
                Color foreground  = new Color(88,21,123);
                
                this.thumbColor = background;
                this.thumbHighlightColor = background;
                this.thumbDarkShadowColor = background;
                this.thumbLightShadowColor = background;
                
                this.trackColor = foreground;
                this.trackHighlightColor = foreground;
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
        
        JTableHeader th = tblResource.getTableHeader();
        th.setBackground(new Color(115,27,161));
        th.setForeground(Color.white);
        th.setOpaque(false);
        Font  f1  = new Font("Segoe UI", Font.PLAIN,  14);
        th.setFont(f1);
        
        
        TableCellRenderer rendererFromHeader = tblResource.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        tblResource.setColumnSelectionAllowed(false);
        tblResource.setRowSelectionAllowed(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        tblResource.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblResource.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        tblResource.setSelectionBackground(new Color(142,33,199));
        tblResource.setSelectionForeground(Color.white);
        //combo box
        Color comboBackground = new Color(139,32,197);
        comboClassroom.setUI(new BasicComboBoxUI(){
            @Override
            public void paintCurrentValueBackground(Graphics g,Rectangle bounds,boolean hasFocus){

                 Color t = new Color(187,51,144);
                    if ( comboBox.isEnabled() )
                        g.setColor(comboBackground);
                    else
                        g.setColor(comboBackground);
                    g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
                    g.setColor(t);
                }
            });
        
        comboClassroom.setBackground(comboBackground);
        comboClassroom.setForeground(Color.white);
        comboClassroom.setBorder(BorderFactory.createEmptyBorder());
        
        comboClassroom1.setUI(new BasicComboBoxUI(){
            @Override
            public void paintCurrentValueBackground(Graphics g,Rectangle bounds,boolean hasFocus){

                 Color t = new Color(187,51,144);
                    if ( comboBox.isEnabled() )
                        g.setColor(comboBackground);
                    else
                        g.setColor(comboBackground);
                    g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
                    g.setColor(t);
                }
            });
        
        comboClassroom1.setBackground(comboBackground);
        comboClassroom1.setForeground(Color.white);
        comboClassroom1.setBorder(BorderFactory.createEmptyBorder());
        
        comboItem.setUI(new BasicComboBoxUI(){
            @Override
            public void paintCurrentValueBackground(Graphics g,Rectangle bounds,boolean hasFocus){

                 Color t = new Color(187,51,144);
                    if ( comboBox.isEnabled() )
                        g.setColor(comboBackground);
                    else
                        g.setColor(comboBackground);
                    g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
                    g.setColor(t);
                }
            });
        
        comboItem.setBackground(comboBackground);
        comboItem.setForeground(Color.white);
        comboItem.setBorder(BorderFactory.createEmptyBorder());
        comboItem.getEditor().getEditorComponent().setBackground(comboBackground);
        comboItem.getEditor().getEditorComponent().setForeground(Color.white);
        
        //spinner
        spinnerQty.getEditor().getComponent(0).setBackground(comboBackground);
        spinnerQty.setBackground(comboBackground);
        spinnerQty.getEditor().getComponent(0).setForeground(Color.white);
        spinnerQty.setForeground(Color.white);
   }
    
    private void uac(){
        if(userRole==1 ||userRole==2 ){ //admin or fac member
        }else{ // staff
            tabPane.remove(1);
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

        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnPDF = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tabPane = new javax.swing.JTabbedPane();
        panelAllResources = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        comboClassroom = new javax.swing.JComboBox<>();
        scroll = new javax.swing.JScrollPane();
        tblResource = new javax.swing.JTable();
        panelManageResources = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboClassroom1 = new javax.swing.JComboBox<>();
        comboItem = new javax.swing.JComboBox<>();
        spinnerQty = new javax.swing.JSpinner();
        btnNew = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Resources");
        setMinimumSize(new java.awt.Dimension(550, 440));
        getContentPane().setLayout(null);

        btnBack.setBackground(new java.awt.Color(112, 24, 138));
        btnBack.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back.png"))); // NOI18N
        btnBack.setText("Back ");
        btnBack.setToolTipText("Click to go back to dashboard");
        btnBack.setBorder(null);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack);
        btnBack.setBounds(60, 30, 80, 30);

        btnPDF.setBackground(new java.awt.Color(137, 26, 169));
        btnPDF.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnPDF.setForeground(new java.awt.Color(255, 255, 255));
        btnPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/pdf.png"))); // NOI18N
        btnPDF.setText("Save Report ");
        btnPDF.setToolTipText("Click to save resource report as a .pdf file");
        btnPDF.setBorder(null);
        btnPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF);
        btnPDF.setBounds(360, 30, 120, 30);

        btnRefresh.setBackground(new java.awt.Color(220, 9, 152));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/refresh.png"))); // NOI18N
        btnRefresh.setText("Refresh ");
        btnRefresh.setToolTipText("Click to to refresh data");
        btnRefresh.setBorder(null);
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh);
        btnRefresh.setBounds(390, 90, 90, 30);

        jLabel1.setBackground(new java.awt.Color(112, 24, 138));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Resources");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(58, 32, 420, 32);

        tabPane.setBackground(new java.awt.Color(184, 17, 153));
        tabPane.setForeground(new java.awt.Color(255, 255, 255));
        tabPane.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        panelAllResources.setOpaque(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Classroom");

        comboClassroom.setBackground(new java.awt.Color(139, 32, 197));
        comboClassroom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboClassroom.setForeground(new java.awt.Color(255, 255, 255));
        comboClassroom.setBorder(null);
        comboClassroom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboClassroomActionPerformed(evt);
            }
        });

        tblResource.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblResource.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblResource.setRowHeight(25);
        tblResource.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(tblResource);

        javax.swing.GroupLayout panelAllResourcesLayout = new javax.swing.GroupLayout(panelAllResources);
        panelAllResources.setLayout(panelAllResourcesLayout);
        panelAllResourcesLayout.setHorizontalGroup(
            panelAllResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllResourcesLayout.createSequentialGroup()
                .addGroup(panelAllResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAllResourcesLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAllResourcesLayout.setVerticalGroup(
            panelAllResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllResourcesLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelAllResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(comboClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabPane.addTab("   View Resources   ", panelAllResources);

        panelManageResources.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Classroom");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Item");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Quantity");

        comboClassroom1.setBackground(new java.awt.Color(139, 32, 197));
        comboClassroom1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboClassroom1.setForeground(new java.awt.Color(255, 255, 255));
        comboClassroom1.setBorder(null);

        comboItem.setBackground(new java.awt.Color(139, 32, 197));
        comboItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboItem.setForeground(new java.awt.Color(255, 255, 255));
        comboItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Projector", "Whiteboard", "Marker Pen", "Eraser", "Mic", "Speaker" }));
        comboItem.setSelectedIndex(-1);
        comboItem.setBorder(null);
        comboItem.setFocusCycleRoot(true);

        spinnerQty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        spinnerQty.setBorder(null);

        btnNew.setBackground(new java.awt.Color(114, 27, 161));
        btnNew.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/new.png"))); // NOI18N
        btnNew.setText("Create new ");
        btnNew.setToolTipText("Click to add new resource");
        btnNew.setBorder(null);
        btnNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(114, 27, 161));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/update.png"))); // NOI18N
        btnUpdate.setText("Update ");
        btnUpdate.setToolTipText("Click to update resource quantity");
        btnUpdate.setBorder(null);
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(114, 27, 161));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete.png"))); // NOI18N
        btnDelete.setText("Delete ");
        btnDelete.setToolTipText("Click to delete resource");
        btnDelete.setBorder(null);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(114, 27, 161));
        btnClear.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clear.png"))); // NOI18N
        btnClear.setText("Clear ");
        btnClear.setToolTipText("Click to clear the fields");
        btnClear.setBorder(null);
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelManageResourcesLayout = new javax.swing.GroupLayout(panelManageResources);
        panelManageResources.setLayout(panelManageResourcesLayout);
        panelManageResourcesLayout.setHorizontalGroup(
            panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManageResourcesLayout.createSequentialGroup()
                .addGroup(panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelManageResourcesLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(comboClassroom1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelManageResourcesLayout.createSequentialGroup()
                        .addGroup(panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(49, 49, 49)
                        .addGroup(panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboItem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spinnerQty)))
                    .addGroup(panelManageResourcesLayout.createSequentialGroup()
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelManageResourcesLayout.setVerticalGroup(
            panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManageResourcesLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboClassroom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(spinnerQty, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
                .addGroup(panelManageResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        tabPane.addTab("   Manage Resources   ", panelManageResources);

        getContentPane().add(tabPane);
        tabPane.setBounds(65, 89, 420, 320);

        jLabel13.setBackground(new java.awt.Color(157, 5, 129));
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg4.png"))); // NOI18N
        getContentPane().add(jLabel13);
        jLabel13.setBounds(0, 0, 550, 440);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        dashboard.setState(java.awt.Frame.NORMAL);
        this.setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        try {
            DefaultTableModel model = (DefaultTableModel) tblResource.getModel();
            model.setColumnCount(2);
            modifyTabPane();
            loadTableData();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void comboClassroomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboClassroomActionPerformed
        // TODO add your handling code here:
        try {
            loadTableData();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_comboClassroomActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        String classroom = comboClassroom1.getSelectedItem().toString();
        String[] classStr = classroom.split("-");
        
        int qty = Integer.parseInt(spinnerQty.getValue().toString());
        
        if(comboClassroom1.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(rootPane, "Please select a classroom", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(comboItem.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(rootPane, "Please select a resource", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(qty<=0){
            JOptionPane.showMessageDialog(rootPane, "Please enter resource quantity", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            
            String department = classStr[0];
            String floor = classStr[1];
            int number = Integer.parseInt(classStr[2]);
            String name = comboItem.getSelectedItem().toString();

            Resource resource = new Resource(department,floor,number,name,qty);
            try {
                if(!resource.addResource()){
                    JOptionPane.showMessageDialog(rootPane, name + " already exsist in " +classroom , "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(rootPane, qty + " " + name + "(s) added to " +classroom +" successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
                //backup
                if(!resource.addResource_Backup()){
                    System.out.println("BACKUP: " + name + " already exsist in " +classroom);
                }else{
                    System.out.println("BACKUP: " +  qty + " " + name + "(s) added to " +classroom +" successfully");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        String classroom = comboClassroom1.getSelectedItem().toString();
        String[] classStr = classroom.split("-");
        int qty = Integer.parseInt(spinnerQty.getValue().toString());
        
        if(comboClassroom1.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(rootPane, "Please select a classroom", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(comboItem.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(rootPane, "Please select a resource", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(qty<=0){
            JOptionPane.showMessageDialog(rootPane, "Please enter resource quantity", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{

            String department = classStr[0];
            String floor = classStr[1];
            int number = Integer.parseInt(classStr[2]);
            String name = comboItem.getSelectedItem().toString();
            Resource resource = new Resource(department,floor,number,name,qty);
            try {
                if(!resource.updateResource()){
                    JOptionPane.showMessageDialog(rootPane, name + "s not found in " +classroom , "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(rootPane, qty + " " + name + "(s) updated to " +classroom +" successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
                //backup
                if(!resource.updateResource_Backup()){
                    System.out.println("BACKUP: " + name + "s not found in " +classroom );
                }else{
                    System.out.println("BACKUP: " + qty + " " + name + "(s) updated to " +classroom +" successfully");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        String classroom = comboClassroom1.getSelectedItem().toString();
        String[] classStr = classroom.split("-");
        
        if(comboClassroom1.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(rootPane, "Please select a classroom", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(comboItem.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(rootPane, "Please select a resource", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{

            String department = classStr[0];
            String floor = classStr[1];
            int number = Integer.parseInt(classStr[2]);
            String name = comboItem.getSelectedItem().toString();
            int qty = Integer.parseInt(spinnerQty.getValue().toString());

            Resource resource = new Resource(department,floor,number,name,qty);
            try {
                int result = JOptionPane.showConfirmDialog(rootPane, "This action can not be undone. Are you sure want to delete?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        if(!resource.deleteResource()){
                            JOptionPane.showMessageDialog(rootPane, name + "s not found in " +classroom , "Warning", JOptionPane.WARNING_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(rootPane, name + "s deleted from " +classroom +" successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        comboClassroom.setSelectedIndex(0);
        comboItem.setSelectedIndex(-1);
        spinnerQty.setValue(0);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFActionPerformed
        // TODO add your handling code here:
        String department,floor,classroom,name = "";
        int number, qty = 0;
        
        //table resoure
        DefaultTableModel model = (DefaultTableModel) tblResource.getModel();
        model.addColumn("Classroom");
        model.setRowCount(0);
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT department,floor,number,name,qty FROM resource");
            
            while(rs.next()){
                department = rs.getString(1);
                floor = rs.getString(2);
                number = Integer.parseInt(rs.getString(3));
                classroom = department + "-" + floor + "-" + String.valueOf(number);
                name = rs.getString(4);
                qty = Integer.parseInt(rs.getString(5));
                
                String[] data = {name, String.valueOf(qty),classroom};
                model.addRow(data);
            }
            st.close();
            rs.close();
            con.close();
        }catch(SQLException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        
        
        String path ="";
        
        JFileChooser j = new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int x = j.showSaveDialog(this);
        
        if(x ==JFileChooser.APPROVE_OPTION){
            path = j.getSelectedFile().getPath();
        }else{
            model.setColumnCount(2);
            modifyTabPane();
        }
        
        Document doc = new Document();
        
        try {
            PdfWriter writer = PdfWriter.getInstance(doc,new FileOutputStream(path+"\\Resources.pdf"));
            doc.open();
            
            Paragraph p = new Paragraph("Time Table Management System",large);
            p.setAlignment(Element.ALIGN_CENTER);
            doc.add(p);   
            
            Paragraph p1 = new Paragraph("Resources",medium);
            p1.setAlignment(Element.ALIGN_CENTER);
            doc.add(p1);
            
            Paragraph p3 = new Paragraph(" ",smallBold);
            p3.setAlignment(Element.ALIGN_CENTER);
            doc.add(p3);
            
            PdfPTable tbl = new PdfPTable(3);
            tbl.addCell("Classroom");
            tbl.addCell("Item");
            tbl.addCell("Quantity");
            
            for(int i=0; i<tblResource.getRowCount(); i++){
                 String Item = tblResource.getValueAt(i, 0).toString();
                 String Qty = tblResource.getValueAt(i, 1).toString();
                 String Classroom = tblResource.getValueAt(i, 2).toString();
                 
                 tbl.addCell(Classroom);
                 tbl.addCell(Item);
                 tbl.addCell(Qty);
             }
             
             doc.add(tbl);
             JOptionPane.showMessageDialog(null, "' Resources.pdf '" + " saved at " +  path  , "Information", JOptionPane.INFORMATION_MESSAGE);
             doc.close();
             writer.close();
             
             model.setColumnCount(2);
             modifyTabPane();
            try {
                loadTableData();
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (FileNotFoundException | DocumentException ex) {
            model = (DefaultTableModel) tblResource.getModel();;
            model.setColumnCount(2);
            modifyTabPane();
            try {
                loadTableData();
            } catch (SQLException | ClassNotFoundException ex1) {
                JOptionPane.showMessageDialog(rootPane, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Error while generating pdf", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: " + ex);
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
            java.util.logging.Logger.getLogger(ResourceUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResourceUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResourceUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResourceUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(ResourceUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                ResourceUI frame = new ResourceUI(1,dashboard);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> comboClassroom;
    private javax.swing.JComboBox<String> comboClassroom1;
    private javax.swing.JComboBox<String> comboItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel panelAllResources;
    private javax.swing.JPanel panelManageResources;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JSpinner spinnerQty;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblResource;
    // End of variables declaration//GEN-END:variables
}
