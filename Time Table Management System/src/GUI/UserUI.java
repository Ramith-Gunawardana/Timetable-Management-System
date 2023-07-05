package GUI;

import DB.DBCon;
import DB.User;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicComboBoxUI;
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

public class UserUI extends javax.swing.JFrame {
    static JFrame dashboard;
    private static String username = "User";
    private static int userRole = 0;
    /**
     * Creates new form Users
     */
    public UserUI(String username, int role, JFrame dashboard) {
        this.username = username;
        this.userRole = role;
        this.dashboard = dashboard;
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/icon.png"));
        this.setIconImage(icon.getImage());
        
        initComponents();
        modifyTabPane();
        customizeScrollPane();
        try {
            loadTableData();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.WARNING_MESSAGE);
        }
        setCharLimit();
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
        tblUser.setBackground(background);
        ((DefaultTableCellRenderer)tblUser.getDefaultRenderer(Object.class)).setBackground(background);
        tblUser.setForeground(Color.white);
        
        
        scroll.setBackground(background);
        tblUser.setOpaque(false);
        
        scroll.setOpaque(false);
        ((DefaultTableCellRenderer)tblUser.getDefaultRenderer(Object.class)).setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        tblUser.setShowGrid(false);
//        tblUser.setGridColor(Color.gray);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        JTableHeader th = tblUser.getTableHeader();
        th.setBackground(new Color(132,3,93));
        th.setForeground(Color.white);
        th.setOpaque(false);
        Font  f1  = new Font("Segoe UI", Font.PLAIN,  14);
        th.setFont(f1);
        
        TableCellRenderer rendererFromHeader = tblUser.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        tblUser.setColumnSelectionAllowed(false);
        tblUser.setRowSelectionAllowed(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        tblUser.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblUser.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        tblUser.setSelectionBackground(new Color(186,12,131));
        tblUser.setSelectionForeground(Color.white);
        
        //combo box
        comboUserRole.setUI(new BasicComboBoxUI(){
            @Override
            public void paintCurrentValueBackground(Graphics g,Rectangle bounds,boolean hasFocus){

                 Color t = new Color(187,51,144);
                    if ( comboBox.isEnabled() )
                        g.setColor(new Color(187,51,144));
                    else
                        g.setColor(new Color(187,51,144));
                    g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
                    g.setColor(t);
                }
            });
        
        comboUserRole.setBackground(new Color(187,51,144));
        comboUserRole.setForeground(Color.white);
        comboUserRole.setBorder(BorderFactory.createEmptyBorder());
   }
    private void loadTableData() throws SQLException,ClassNotFoundException{
        String user,role = "";
        DefaultTableModel model = (DefaultTableModel) tblUser.getModel();
        model.setRowCount(0);
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT username,role FROM users");
            
            while(rs.next()){
                user = rs.getString(1);
                int userRole = Integer.parseInt(rs.getString(2));
                
                switch(userRole){
                    case 1:
                       role = "Admin";
                       break;
                    case 2:
                       role = "Faculty Memeber";
                       break;
                    case 3:
                       role = "Staff";
                       break;
                    default:
                       role = "User";
                       break;
                }
                String[] data = {user, role};
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
    
    private void customizeScrollPane(){
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                
                Color background  = new Color(132,3,93);
                Color foreground  = new Color(187,51,144);
                
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
        
    }
    
    private void setCharLimit(){
        txtUsername.setDocument(new JTextFieldLimit(20));
        txtPassword.setDocument(new JTextFieldLimit(64));
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
        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollBar2 = new javax.swing.JScrollBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        tabPane = new javax.swing.JTabbedPane();
        allUsers = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        manageUsers = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPwd = new javax.swing.JPasswordField();
        comboUserRole = new javax.swing.JComboBox<>();
        btnSearch = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

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

        jScrollPane2.setViewportView(jEditorPane1);

        setTitle("Users");
        setMinimumSize(new java.awt.Dimension(560, 520));
        getContentPane().setLayout(null);

        btnBack.setBackground(new java.awt.Color(181, 52, 61));
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
        btnBack.setBounds(60, 32, 80, 30);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Users");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(61, 29, 439, 32);

        btnRefresh.setBackground(new java.awt.Color(140, 0, 77));
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
        btnRefresh.setBounds(377, 100, 90, 32);

        tabPane.setBackground(new java.awt.Color(90, 6, 52));
        tabPane.setForeground(new java.awt.Color(255, 255, 255));
        tabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPane.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        allUsers.setOpaque(false);

        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tblUser.setAutoCreateRowSorter(true);
        tblUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Username", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUser.setFocusable(false);
        tblUser.setOpaque(false);
        tblUser.setRequestFocusEnabled(false);
        tblUser.setRowHeight(25);
        tblUser.setShowGrid(false);
        tblUser.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(tblUser);

        javax.swing.GroupLayout allUsersLayout = new javax.swing.GroupLayout(allUsers);
        allUsers.setLayout(allUsersLayout);
        allUsersLayout.setHorizontalGroup(
            allUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(allUsersLayout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 30, Short.MAX_VALUE))
        );
        allUsersLayout.setVerticalGroup(
            allUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, allUsersLayout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabPane.addTab("   Show all users   ", allUsers);

        manageUsers.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Username");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Password");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Re-enter password");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("User role");

        txtUsername.setBackground(new java.awt.Color(187, 51, 144));
        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtUsername.setToolTipText("Maximum 20 characters are allowed without spaces");
        txtUsername.setBorder(null);
        txtUsername.setCaretColor(new java.awt.Color(255, 255, 255));
        txtUsername.setOpaque(true);

        txtPassword.setBackground(new java.awt.Color(187, 51, 144));
        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtPassword.setToolTipText("Maximum 64 characters are allowed");
        txtPassword.setBorder(null);
        txtPassword.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPassword.setOpaque(true);

        txtConfirmPwd.setBackground(new java.awt.Color(187, 51, 144));
        txtConfirmPwd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtConfirmPwd.setForeground(new java.awt.Color(255, 255, 255));
        txtConfirmPwd.setToolTipText("Maximum 64 characters are allowed");
        txtConfirmPwd.setBorder(null);
        txtConfirmPwd.setCaretColor(new java.awt.Color(255, 255, 255));
        txtConfirmPwd.setOpaque(true);

        comboUserRole.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboUserRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Staff", "Faculty Member", "Admin" }));
        comboUserRole.setSelectedIndex(-1);
        comboUserRole.setToolTipText("Click to select the user role");
        comboUserRole.setBorder(null);
        comboUserRole.setOpaque(true);
        comboUserRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboUserRoleActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(170, 2, 117));
        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        btnSearch.setText("Search ");
        btnSearch.setToolTipText("Enter a username to search");
        btnSearch.setBorder(null);
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(186, 12, 131));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/new.png"))); // NOI18N
        btnAdd.setText("Craete new ");
        btnAdd.setToolTipText("Click to add new user");
        btnAdd.setBorder(null);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(186, 12, 131));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/update.png"))); // NOI18N
        btnUpdate.setText("Update ");
        btnUpdate.setToolTipText("Click to update password and user role");
        btnUpdate.setBorder(null);
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(186, 12, 131));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete.png"))); // NOI18N
        btnDelete.setText("Delete ");
        btnDelete.setToolTipText("Click to delete user");
        btnDelete.setBorder(null);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(186, 12, 131));
        btnClear.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clear.png"))); // NOI18N
        btnClear.setText("Clear ");
        btnClear.setToolTipText("Click to clear fields");
        btnClear.setBorder(null);
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout manageUsersLayout = new javax.swing.GroupLayout(manageUsers);
        manageUsers.setLayout(manageUsersLayout);
        manageUsersLayout.setHorizontalGroup(
            manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageUsersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(manageUsersLayout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(manageUsersLayout.createSequentialGroup()
                        .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboUserRole, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(manageUsersLayout.createSequentialGroup()
                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtConfirmPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 34, Short.MAX_VALUE))
        );
        manageUsersLayout.setVerticalGroup(
            manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageUsersLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtConfirmPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(comboUserRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addGroup(manageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );

        tabPane.addTab("   Manage users   ", manageUsers);

        getContentPane().add(tabPane);
        tabPane.setBounds(60, 100, 440, 340);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg7.png"))); // NOI18N
        getContentPane().add(jLabel7);
        jLabel7.setBounds(0, 0, 560, 520);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        String username = txtUsername.getText();
        
        User newUser = new User(username,"",0);
        
        if(username.isEmpty()){
          JOptionPane.showMessageDialog(rootPane, "Username can not be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            if(username.contains(" ")){
                JOptionPane.showMessageDialog(rootPane, "Username can not have spaces. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }else{
                try {
                    if(!newUser.isUserExsit()){
                        JOptionPane.showMessageDialog(rootPane, "User not found! Please check the username again.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        int result = JOptionPane.showConfirmDialog(rootPane, "This action can not be undone. Are you sure want to delete?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(result == JOptionPane.YES_OPTION){
                            if(newUser.deleteUser()){
                                JOptionPane.showMessageDialog(rootPane, "User removed successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
                             }else{
                                JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                             }
                        }
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } 
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String username = txtUsername.getText();
        User user = new User(username, "", 0);
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT role FROM users WHERE  username='"+username+"'");
            
            if(user.isUserExsit()){
                while(rs.next()){
                    if (Integer.parseInt(rs.getString(1)) == 1 ){
                        comboUserRole.setSelectedIndex(2);
                    }else if(Integer.parseInt(rs.getString(1)) == 2 ){
                        comboUserRole.setSelectedIndex(1);
                    }else if(Integer.parseInt(rs.getString(1)) == 3 ){
                        comboUserRole.setSelectedIndex(0);
                    }else{
                        comboUserRole.setSelectedIndex(-1);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane, "Incorect username. No users found.", "Error", JOptionPane.WARNING_MESSAGE);
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went wrong!.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void comboUserRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboUserRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboUserRoleActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String repassword = txtConfirmPwd.getText();
        int role = 0;
        
        switch (comboUserRole.getSelectedIndex()) {
            case 0:
                role = 3;
                break;
            case 1:
                role = 2;
                break;
            case 2:
                role = 1;
                break;
            default:
                role = 0;
                break;
        }
        User newUser = new User(username,password,role);
        
        if(username.isEmpty()){
          JOptionPane.showMessageDialog(rootPane, "Username can not be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            if(username.contains(" ")){
                JOptionPane.showMessageDialog(rootPane, "Username can not have spaces. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }else{
                try {
                    if(newUser.isUserExsit()){
                        JOptionPane.showMessageDialog(rootPane, "Username already exsists. Please try again with a diffrent username.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        if(password.isEmpty() || repassword.isEmpty()){
                            JOptionPane.showMessageDialog(rootPane, "Password can not be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }else{
                            if(!password.equals(repassword)){
                                JOptionPane.showMessageDialog(rootPane, "Passwords do not match. Please check again.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }else{
                                if(comboUserRole.getSelectedIndex()==-1){
                                    JOptionPane.showMessageDialog(rootPane, "Please select the user role.", "Warning", JOptionPane.WARNING_MESSAGE);
                                }else{
                                    if(newUser.addUser()){ //include backup
                                       JOptionPane.showMessageDialog(rootPane, "New user added successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
                                    }else{
                                       JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                    
                                }
                            }
                        }
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } 
        }
        
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        try {
            loadTableData();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String repassword = txtConfirmPwd.getText();
        int role = 0;
        
        switch (comboUserRole.getSelectedIndex()) {
            case 0:
                role = 3;
                break;
            case 1:
                role = 2;
                break;
            case 2:
                role = 1;
                break;
            default:
                role = 0;
                break;
        }
        User newUser = new User(username,password,role);
        
        if(username.isEmpty()){
          JOptionPane.showMessageDialog(rootPane, "Username can not be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            if(username.contains(" ")){
                JOptionPane.showMessageDialog(rootPane, "Username can not have spaces. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }else{
                if(password.isEmpty() || repassword.isEmpty()){
                    JOptionPane.showMessageDialog(rootPane, "Password can not be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    try {
                        if(!newUser.isUserExsit()){
                            JOptionPane.showMessageDialog(rootPane, "User not found! Please check the username again.", "Warning", JOptionPane.WARNING_MESSAGE);
                        }else{
                            if(!password.equals(repassword)){
                                JOptionPane.showMessageDialog(rootPane, "Passwords do not match. Please check again.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }else{
                                if(comboUserRole.getSelectedIndex()==-1){
                                    JOptionPane.showMessageDialog(rootPane, "Please select the user role.", "Warning", JOptionPane.WARNING_MESSAGE);
                                }else{
                                    if(newUser.updateUser()){ //include backup
                                       JOptionPane.showMessageDialog(rootPane, "User data updated successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
                                    }else{
                                       JOptionPane.showMessageDialog(rootPane, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                    } catch (SQLException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(rootPane, "Can't connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } 
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
       txtUsername.setText("");
       txtPassword.setText("");
       txtConfirmPwd.setText("");
       comboUserRole.setSelectedIndex(-1);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
//        Dashboard dash = new Dashboard(username, userRole);
//        this.setVisible(false);
//        dash.setResizable(false);
//        dash.setLocationRelativeTo(null);
//        dash.setVisible(true);
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
            java.util.logging.Logger.getLogger(UserUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(LoadingUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            UserUI frame = new UserUI(username, userRole,dashboard);
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
    private javax.swing.JPanel allUsers;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> comboUserRole;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollBar jScrollBar2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel manageUsers;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblUser;
    private javax.swing.JPasswordField txtConfirmPwd;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
