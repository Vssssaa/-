
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame implements ActionListener{
    // 定义组件
    JLabel jLPersonnelManagement = null;//人事管理系统
    JLabel jLLoginUser = null;//登录工号
    JLabel jLLoginPassword = null;//登录密码
    JLabel jLLoginStatus = null;//身份

    JTextField jTFLoginUser = null;//用户框
    JPasswordField jPFLoginPassword = null;//密码框

    JButton jBLogin = null;//登录
    JButton jBExit = null;//退出

    JComboBox<String> jCBSelectUser = null;//用户选择

    JPanel jP1, jP2,jP3,jP4,jP5 = null;
    JPanel jP= null;

    JScrollPane LoginJScrollPane = null;

    private static DbProcess LdbProcess;
    private static Manage M;
    private static Employee E;
    private static Md5 md5;
    String SelectUserStr = "管理员";

    public Login() {
        this.md5 = new Md5();
        // 创建标签组件
        jLPersonnelManagement = new JLabel("人事管理系统");
        jLPersonnelManagement.setFont(new Font("宋体",Font.BOLD,25));
        jLLoginUser = new JLabel("工号：");
        jLLoginUser.setFont(new Font("宋体",Font.BOLD,15));
        jLLoginPassword = new JLabel("密码：");
        jLLoginPassword.setFont(new Font("宋体",Font.BOLD,15));
        jLLoginStatus = new JLabel("身份：");
        jLLoginStatus.setFont(new Font("宋体",Font.BOLD,15));

        jTFLoginUser = new JTextField(15);//用户
        jPFLoginPassword = new JPasswordField(15);//密码

        jBLogin = new JButton("登录");
        jBLogin.setFont(new Font("宋体",Font.BOLD,15));
        jBExit = new JButton("退出");
        jBExit.setFont(new Font("宋体",Font.BOLD,15));

        // 设置监听
        jBLogin.addActionListener(this);
        jBExit.addActionListener(this);

        jCBSelectUser = new JComboBox<String>();//查询字段
        jCBSelectUser.setFont(new Font("宋体",Font.BOLD,15));
        //添加选项
        jCBSelectUser.addItem("管理员");
        jCBSelectUser.addItem("普通员工");
        jCBSelectUser.addItemListener(new ItemListener() {//下拉框事件监听
            public void itemStateChanged(ItemEvent event) {
                switch (event.getStateChange()) {
                    case ItemEvent.SELECTED:
                        SelectUserStr = (String) event.getItem();
                        System.out.println("选中：" + SelectUserStr);
                        break;
                    case ItemEvent.DESELECTED:
                        System.out.println("取消选中：" + event.getItem());
                        break;
                }
            }
        });

        LoginJScrollPane = new JScrollPane();

        jP1 = new JPanel();
        jP2 = new JPanel();
        jP3 = new JPanel();
        jP4 = new JPanel();
        jP5 = new JPanel();
        jP = new JPanel();

        jP1.add(jLPersonnelManagement);
        jP1.setLayout(new FlowLayout(FlowLayout.CENTER));
        jP1.setPreferredSize(new Dimension(20,85));

        jP2.add(jLLoginUser);
        jP2.add(jTFLoginUser);
        jP2.setLayout(new FlowLayout(FlowLayout.CENTER));
        jP2.setPreferredSize(new Dimension(20,85));

        jP3.add(jLLoginPassword);
        jP3.add(jPFLoginPassword);
        jP3.setLayout(new FlowLayout(FlowLayout.CENTER));
        jP3.setPreferredSize(new Dimension(20,85));

        jP4.add(jLLoginStatus);
        jP4.add(jCBSelectUser);
        jP4.setLayout(new FlowLayout(FlowLayout.CENTER));
        jP4.setPreferredSize(new Dimension(20,85));

        jP5.add(jBLogin);
        jP5.add(jBExit);
        jP5.setLayout(new FlowLayout(FlowLayout.CENTER));
        jP5.setPreferredSize(new Dimension(20,85));

        jP.setLayout(new GridLayout(5, 1));
        jP.add(jP1);
        jP.add(jP2);
        jP.add(jP3);
        jP.add(jP4);
        jP.add(jP5);

        this.add("North",LoginJScrollPane);
        this.add("South",jP);

        this.setLayout(new BorderLayout());
        this.add(jP,BorderLayout.SOUTH);

        this.setTitle("登录界面");
        this.setSize(460, 500);
        this.setLocation(450, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        LdbProcess = new DbProcess();

    }
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("登录")
                &&!jTFLoginUser.getText().isEmpty()
                &&!String.valueOf(jPFLoginPassword.getPassword()).equals("")){
            String sID = jTFLoginUser.getText().trim();
            String sPassword = new String(jPFLoginPassword.getPassword());
            System.out.println("actionPerformed(). 登录"+sPassword);
            loginProcess(sID,sPassword,SelectUserStr);
            jPFLoginPassword.setText("");
        }else if(e.getActionCommand().equals("退出")) {
            System.out.println("actionPerformed(). 退出");
            System.exit(0);
        }
    }
    public void loginProcess(String sID, String sPassword,String userStr){
        try{
            // 建立查询条件
            String sql = "select * from staff where ";

            sql = sql + "sID";
            sql = sql + " = ";
            sql = sql + "'" + sID + "';";

            System.out.println("queryProcess(). sql = " + sql);

            LdbProcess.connect();
            ResultSet rs = LdbProcess.executeQuery(sql);
            rs.next();
            if(rs.getString(14).equals("T")){
                if(this.md5.md5(sPassword).equals(rs.getString(2))){
                    if(rs.getString(3).equals("普通员工")){
                        if(userStr.equals("普通员工")){
                            JOptionPane.showMessageDialog(null,
                                    "登录成功！","提示",JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                            this.E = new Employee(rs);
                        }else{
                            JOptionPane.showMessageDialog(null,
                                    "用户身份错误，请重新选择！","错误",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if(rs.getString(3).equals("管理员")){
                        JOptionPane.showMessageDialog(null,
                                "登录成功！","提示",JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                        if(userStr.equals("普通员工")){
                            this.E = new Employee(rs);
                        }else if(userStr.equals("管理员")){
                            this.M = new Manage();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,
                            "用户密码错误，请重新输入！","错误",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null,
                        "你已非本公司员工！","错误",JOptionPane.ERROR_MESSAGE);
            }
            LdbProcess.disconnect();

        }catch(SQLException sqle){
            System.out.println("sqle = " + sqle);
            JOptionPane.showMessageDialog(null,
                    "你输入的工号不存在，请从新输入！","错误",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            System.out.println("e = " + e);
            JOptionPane.showMessageDialog(null,
                    "你输入的工号不存在，请从新输入！","错误",JOptionPane.ERROR_MESSAGE);
        }
    }
}
