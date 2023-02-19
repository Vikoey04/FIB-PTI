# ANNEX. Enable MYSQL access (through JNDI)

## 1 Install MySQL and create a database

Run the following commands:

    sudo apt-get install mysql-server 
    sudo service mysql start
    mysql -u root -p
    mysql> GRANT ALL PRIVILEGES ON *.* TO javauser@localhost IDENTIFIED BY 'javadude' WITH GRANT OPTION;
    mysql> create database javatest;
    mysql> use javatest;
    mysql> create table testdata (id int not null auto_increment primary key, foo varchar(25), bar int);
    mysql> exit

## 2 Download the driver and configure Tomcat

From the apache-tomcat-9.0.37 folder type:

    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/servlets/mysql-connector-java-8.0.15.jar
    mv mysql-connector-java-8.0.15.jar lib

Add the following to the file conf/context.xml:

    <Context>
        <!-- .... keep previous entries ... -->
        <Resource name="jdbc/TestDB" auth="Container" type="javax.sql.DataSource"
                   maxTotal="100" maxIdle="30" maxWaitMillis="10000"
                   username="javauser" password="javadude" driverClassName="com.mysql.jdbc.Driver"
                   url="jdbc:mysql://localhost:3306/javatest"/>
    </Context>

Add the following to the web.xml of your application:

      <web-app>
       <!-- .... keep previous entries ... -->
        <resource-ref>
            <description>DB Connection</description>
            <res-ref-name>jdbc/TestDB</res-ref-name>
            <res-type>javax.sql.DataSource</res-type>
            <res-auth>Container</res-auth>
        </resource-ref>
      </web-app>

## 3 Access the DB from your code

For example:

    ....
    import java.sql.*;
    import javax.sql.DataSource;
    import javax.naming.*;
    ...
    try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup("jdbc/TestDB");
            Connection conn = ds.getConnection();
            out.println("<big>DB working!!</big></html>");
            ResultSet rset = st.executeQuery("select * from testdata");
            while(rset.next()) {
               out.println("<p>" + rset.getString("foo")+"</p>");
            }
            conn.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    ...

You will need to include mysql-connector-java-8.0.15.jar within the classpath when compiling.

## 4 Troubleshooting

If you encounter problems check the logs at logs/catalina.out (from the apache-tomcat-9.0.37 folder). If the error is something like "MySQL connector "The server time zone value Central European Time" is unrecognized or represents more than one time zone." the solution is to start the MySQL client this way:

    mysql -u root -p

And run the following command:

    sql>SET GLOBAL time_zone = '+5:30';
    sql>exit





