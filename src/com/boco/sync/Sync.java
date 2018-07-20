package com.boco.sync;

import com.boco.SQLUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/30 0030.
 */
public class Sync {
    public static final Logger log = Logger.getLogger(Sync.class);
    public static void main(String[] args) {
        log.info("-------------告警同步程序开始执行-------------");
        String queryActAlarm = "SELECT fp0,fp1,fp2,fp3,event_time,cancel_time\n" +
                "FROM tfa_alarm_act\n" +
                "WHERE professional_type IN (1,4) and active_status = 1\n" +
                "AND event_time >='"+args[0]+"'\n" +
                "AND event_time < '"+args[1]+"'\n";
        String queryCancelTime = "select fp0,fp1,fp2,fp3,event_time,cancel_time" +
                " from tfa_alarm_clr where active_status = 0 and fp0 = ? and event_time = ?";
        String updateNiosCancelTime = "update tfa_alarm_act set cancel_time = ?,active_status = 0 where fp0 = ? and event_time = ?";
        try {
            SQLUtil sqlUtil = SQLUtil.getInstance();
        //1.获取集中故障活动告警（无线专业与动环专业）
            log.info("开始查询集中故障活动告警数");
            List<AlarmBean> nmosActAlarmList = getAlarmList(queryActAlarm, sqlUtil.getConn("nmosdb"),null);
            log.info("集中故障活动告警数："+nmosActAlarmList.size());
//        2.获取网优活动告警（无线专业与动环专业）
            log.info("开始查询网优活动告警数");
            List<AlarmBean> niosActAlarmList = getAlarmList(queryActAlarm,sqlUtil.getConn("niosdb"),null);
            log.info("网优活动告警数："+niosActAlarmList.size());
//        3.比较集中故障无网优有的告警
            log.info("开始查询集中故障告警清除时间");
            List<AlarmBean> cancelAlarmList = new ArrayList<AlarmBean>();
            for(AlarmBean niosAlarm:niosActAlarmList){
                if(nmosActAlarmList.contains(niosAlarm))
                    continue;
                List<AlarmBean> cancelAlarmTempList = getAlarmList(queryCancelTime,sqlUtil.getConn("nmosdb"),niosAlarm.toParams());
                if(cancelAlarmTempList.size() > 0){
                    cancelAlarmList.add(cancelAlarmTempList.get(0));
                }
            }
            log.info("查询集中故障清除时间结束");
        //4.根据比较结果查询集中故障清除时间，并封装成bean数组
            log.info("开始更新网优活动告警表");
            Object[][] params = new Object[cancelAlarmList.size()][];
            for (int i = 0; i < cancelAlarmList.size(); i++) {
                params[i] = cancelAlarmList.get(i).toCancelParams();
            }

            int result = batchUpdate(updateNiosCancelTime,sqlUtil.getConn("niosdb"),params);
            log.info("网优活动告警表更新了["+result+"]条记录");

        } catch (SQLException e) {
            log.error("清除告警同步失败",e);
        }
        log.info("-------------告警同步程序执行完毕-------------");
    }

    private static List<AlarmBean> getAlarmList(String sql,Connection connection,Object[] params) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        BeanListHandler<AlarmBean> resultSetHandler = new BeanListHandler<AlarmBean>(AlarmBean.class);
        setOracleDateFormat(connection);
        List<AlarmBean> result = queryRunner.query(connection,sql,resultSetHandler,params);
        DbUtils.closeQuietly(connection);
        return result;
    }

    private static int batchUpdate(String sql,Connection connection,Object[][] params) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        setOracleDateFormat(connection);
        int result = queryRunner.batch(connection,sql,params).length;
        DbUtils.closeQuietly(connection);
        return result;
    }

    private static void setOracleDateFormat(Connection connection){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("alter session set nls_date_format = 'yyyy-mm-dd hh24:mi:ss'");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbUtils.closeQuietly(statement);
        }
    }
}
