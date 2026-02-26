package com.kingsoft.enums;

/**
 * @Author sunjiacheng
 * @Date 2025/10/21 21:46
 * @PackageName:com.kingsoft.enums
 * @ClassName: ListSortEnum
 * @Description: TODO
 * @Version 1.0
 */
public enum ListSortEnum {
    ASC("asc", "整序"),
    DESC("desc","倒序");
    public String type;
    public String value;
    ListSortEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
