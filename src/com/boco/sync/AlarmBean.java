package com.boco.sync;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/5/30 0030.
 */
public class AlarmBean {
    private Long fp0;
    private Long fp1;
    private Long fp2;
    private Long fp3;
    private Timestamp event_time;
    private Timestamp cancel_time;

    public Long getFp0() {
        return fp0;
    }

    public void setFp0(Long fp0) {
        this.fp0 = fp0;
    }

    public Long getFp1() {
        return fp1;
    }

    public void setFp1(Long fp1) {
        this.fp1 = fp1;
    }

    public Long getFp2() {
        return fp2;
    }

    public void setFp2(Long fp2) {
        this.fp2 = fp2;
    }

    public Long getFp3() {
        return fp3;
    }

    public void setFp3(Long fp3) {
        this.fp3 = fp3;
    }

    public Timestamp getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Timestamp event_time) {
        this.event_time = event_time;
    }

    public Timestamp getCancel_time() {
        return cancel_time;
    }

    public void setCancel_time(Timestamp cancel_time) {
        this.cancel_time = cancel_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlarmBean alarmBean = (AlarmBean) o;

        if (!getFp0().equals(alarmBean.getFp0())) return false;
        if (!getFp1().equals(alarmBean.getFp1())) return false;
        if (!getFp2().equals(alarmBean.getFp2())) return false;
        if (!getFp3().equals(alarmBean.getFp3())) return false;
        return getEvent_time().equals(alarmBean.getEvent_time());
    }

    @Override
    public int hashCode() {
        int result = getFp0().hashCode();
        result = 31 * result + getFp1().hashCode();
        result = 31 * result + getFp2().hashCode();
        result = 31 * result + getFp3().hashCode();
        result = 31 * result + getEvent_time().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AlarmBean{" +
                "fp0=" + fp0 +
                ", fp1=" + fp1 +
                ", fp2=" + fp2 +
                ", fp3=" + fp3 +
                ", event_time=" + event_time +
                ", cancel_time=" + cancel_time +
                '}';
    }
    public Object[] toParams(){
        Object[] result = new Object[2];
        result[0] = fp0;
//        result[1] = fp1;
//        result[2] = fp2;
//        result[3] = fp3;
        result[1] = PublicVar.sdf.format(new Date(event_time.getTime()));
        return result;
    }

    public Object[] toCancelParams(){
        Object[] result = new Object[3];
        result[0] = cancel_time == null ? null : PublicVar.sdf.format(new Date(cancel_time.getTime()));
        result[1] = getFp0();
        result[2] = PublicVar.sdf.format(new Date(event_time.getTime()));
        return result;
    }
}
