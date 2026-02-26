package com.kingsoft.enums;

/**
 * @Author sunjiacheng
 * @Date 2025/10/21 21:46
 * @PackageName:com.kingsoft.enums
 * @ClassName: ListSortEnum
 * @Description: TODO
 * @Version 1.0
 */
public enum PriceCompareEnum {
    GREATER_THAN(">", "大于"),
    LESS_THAN("<", "小于"),
    GREATER_THAN_EQUAL_TO(">=", "大于等于"),
    LESS_THAN_EQUAL_TO("<=", "小于等于"),

    HIGHER_THAN(">", "高于"),
    LOWER_THAN("<", "低于"),
    NOT_HIGHER_THAN("<=", "不高于"),
    NOT_LOWER_THAN(">=", "不低于"),
    EQUAL_TO("=","等于");
    public String type;
    public String value;
    PriceCompareEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
