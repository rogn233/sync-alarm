package com.boco.test;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/5/30 0030.
 */
public class TestDbutils {
    public static void main(String[] args) {
        QueryRunner queryRunner = new QueryRunner();
        String sql = "insert into a (a) values (?)";
        Object[][] params = new Object[10][];
        for (int i = 0; i < 10; i++) {
            params[i] = new Object[]{i};
        }

        Connection connection = null;
        try {
            connection = getConn();
            queryRunner.batch(connection,sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbUtils.commitAndCloseQuietly(connection);
        }

    }

    public static Connection getConn() throws SQLException {
        DbUtils.loadDriver("org.hsqldb.jdbcDriver");
        return DriverManager.getConnection("jdbc:hsqldb:mem:hsql","sa","");
    }
}
