package com.kingsoft.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * @Author sunjiacheng
 * @Date 2025/10/21 23:12
 * @PackageName:com.kingsoft.pojo
 * @ClassName: CheckInLog
 * @Description: TODO
 * @Version 1.0
 */
@Data
@TableName("check_in_log") // 对应表名
public class CheckInLog {
    @TableId(type = IdType.AUTO) // 自增主键
    private Integer id;
    private String employeeId;
    private String userId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkTime;
    
    private String location;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date checkDate;
    
    // 格式化的打卡时间字符串（用于AI展示，避免流式输出时数字问题）
    @TableField(exist = false)
    private String checkTimeStr;
    
    // 格式化的打卡日期字符串（用于AI展示，避免流式输出时数字问题）
    @TableField(exist = false)
    private String checkDateStr;
    
    /**
     * 获取格式化的打卡时间字符串
     */
    public String getCheckTimeStr() {
        if (checkTime == null) return "";
        if (checkTimeStr == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            checkTimeStr = sdf.format(checkTime);
        }
        return checkTimeStr;
    }
    
    /**
     * 获取格式化的打卡日期字符串
     */
    public String getCheckDateStr() {
        if (checkDate == null) return "";
        if (checkDateStr == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            checkDateStr = sdf.format(checkDate);
        }
        return checkDateStr;
    }
}