
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class DbProcess{
    Connection connection = null;
    ResultSet rs = null;

    //mysql数据库url
    String userMySql="root";
    String passwordMySql="2002818zZ.";//xxxx表示自己的数据库密码
    String urlMySql = "jdbc:mysql://localhost:3306/CourseDesign?user="
            +userMySql+"&password="+passwordMySql + "&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";

    //sqlserver数据库url
    //String urlSqlServer = "jdbc:sqlserver://localhost:1433;integratedSecurity=true;DatabaseName=InfoDb";

    public DbProcess() {
        try {
            //mysql数据库设置驱动程序类型
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("mysql数据库驱动加载成功");

            //sqlserver数据库设置驱动程序类型
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //System.out.println("sqlserver数据库驱动加载成功");

        }
        catch(java.lang.ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        try{
            //mysql数据库
            connection = DriverManager.getConnection(urlMySql);

            //sqlserver数据库
            //connection = DriverManager.getConnection(urlSqlServer);


            if(connection!=null){
                System.out.println("数据库连接成功");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try{
            if(connection != null){
                connection.close();
                connection = null;
                System.out.println("数据库断开成功");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public ResultSet executeQuery(String sql) {
        try {
            System.out.println("executeQuery(). sql = " + sql);

            PreparedStatement pstm = connection.prepareStatement(sql);
            // 执行查询
            rs = pstm.executeQuery();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    //插入
    //executeUpdate 的返回值是一个整数，指示受影响的行数（即更新计数）。
    //executeUpdate用于执行 INSERT、UPDATE 或 DELETE 语句
    //以及 SQL DDL（数据定义语言）语句，例如 CREATE TABLE 和 DROP TABLE。

    //执行增、删、改语句的方法
    public int executeUpdate(String sql) {
        int count = 0;
        connect();
        try {
            Statement stmt = connection.createStatement();
            count = stmt.executeUpdate(sql);
        }
        catch(SQLException ex) {
            System.err.println(ex.getMessage());
        }
        disconnect();
        return count;
    }
}
