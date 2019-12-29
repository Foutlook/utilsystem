package com.foutin.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 常量
 *
 * @author zxq
 * @create 2016-12-22
 */
public interface CommonConstant {
    /**
     * 类型常量
     **/
    String TYPE_1 = "1";

    /**
     * 数字常量
     **/
    String NUMBER_ZERO = "0";
    String NUMBER_ONE = "1";
    String NUMBER_TWO = "2";
    String NUMBER_THREE = "3";
    String NUMBER_FOUR = "4";
    String NUMBER_SIX = "6";
    String NUMBER_SEVEN = "7";
    String NUMBER_NINE = "9";
    String NUMBER_TEN = "10";
    String NUMBER_FIFTEEN = "15";
    String NUMBER_THIRTY_TWO = "32";
    String NUMBER_SIXTY = "60";
    String NUMBER_ONE_HUNDRED = "100";
    String NUMBER_TWO_HUNDRED = "200";
    String NUMBER_TWO_THOUSAND = "2000";
    String NUMBER_THREE_THOUSAND = "3000";
    String NUMBER_TEN_THOUSAND = "10000";
    String NUMBER_ONE_MINUS = "-1";

    String SPECIAL_NUMBER_12345678 = "12345678";
    String SPECIAL_NUMBER_87654321 = "87654321";
    /**
     * 整形常量
     */
    Integer ZERO = 0;
    Integer ONE = 1;
    Integer TWO = 2;
    Integer THREE = 3;
    Integer FOUR = 4;
    Integer FIVE = 5;
    Integer SIX = 6;
    Integer SEVEN = 7;
    Integer EIGHT = 8;
    Integer NINE = 9;
    Integer TEN = 10;
    Integer FIFTEEN = 15;
    Integer FIFTY = 50;
    Integer THIRTY_TWO = 32;
    Integer SIXTY = 60;
    Integer ONE_HUNDRED = 100;
    Integer TWO_HUNDRED = 200;
    Integer ONE_THOUSAND = 1000;
    Integer TWO_THOUSAND = 2000;
    Integer THREE_THOUSAND = 3000;
    Integer TEN_THOUSAND = 10000;
    Integer DAY_SECONDS=24*3600;

    String BLANK = "";
    /**
     * 系统级别加密的sceretKey
     **/
    String AUTH_COMSUMER_SECRET_KEY = "733828MTIzNDU2CShFp1468889281801r9uV0aajI10";

    Integer DEFAULT_RECOMMEND_COMMUNITY_ID = 1;

    /**
     * boolean 常量
     **/
    Boolean TRUE = true;
    Boolean FALSE = false;

    String DEFAULT_PASSWORD = "123456";

    String DEFAULT = "default";

    String RECORD = "record";

    /**
     * 分隔符
     **/
    String SEPARATOR_1 = ",";
    String SEPARATOR_2 = "-";
    /**
     * 用.分割必须加上\\
     */
    String SEPARATOR_3 = "\\.";

    String SEPARATOR_4 = " ";

    String SEPARATOR_5 = "\\|";

    String SEPARATOR_6 = "/";

    String SEPARATOR_7 = "~";

    /**
     * 空白字符串
     **/
    String ELLIPSIS = "";

    /**
     * 省略字符串
     */
    String STRING_BLANK = "...";


    /**
     * 默认头像
     **/
    String ADMIN_DEFAULT_PROFILE = "defaultAdminProfile.png";

    String USER_DEFAULT_PROFILE = "common/default-avatar.png";

    String MALE_DOCTOR_DEFAULT_PROFILE = "picture/doctor/default/head/portrait_man.png";

    String FEMALE_DOCTOR_DEFAULT_PROFILE = "picture/doctor/default/head/portrait_woman.png";

    String CONTENT_DEFAULT_COVERMAP="common/default_covermap.png";

    String CONTENT_DEFAULT_SONG_COVERMAP="common/default_song_covermap.png";

    /**
     * 头像缩放比例
     **/
    String HEAD_PORTRAIT_SUFFIX = "@80h_80w_1e_1c";

    /**
     * 短信签名
     */
    String SMS_SINGLE_NAME = "每次科技";

    /**
     * 短信代码---代理商申请审核通过提醒
     */
    String SMS_CODE_AGENCY_APPLY_SUCCESS = "SMS_117200070";
    String SMS_CODE_AGENCY_APPLY_FAIDED = "SMS_117110082";


    String MOMENT_LIKE_AVATARS_KEY="moment_%s_like_avatars";


    String PRAISE_DAY="praiseDay";


    String PRAISE_WEEK="praiseWeek";


    String PRAISE_MONTH="praiseMonth";


    String COMMENT_DAY="commentDay";


    String COMMENT_WEEK="commentWeek";


    String COMMENT_MONTH="commentMonth";


    /**
     * like查询的字符串
     */
    String LIKE = "%";
    /**
     * 逗号分隔符
     */
    String COMMA_SEPARATOR = ",";



    /**
     * 邮件 发送方账号
     */
    String SEND_MAIL = "code@mcilife.com";
    /**
     * 添加"https://mcwebresource.mcilife.com/"，防止imgText的url前加上额外前缀
     */
    HashSet<String> URL_FILTER = new HashSet(){{
        add("http://thirdwx.qlogo.cn/");
        add("http://appattach.mcilife.com/");
        add("http://wx.qlogo.cn/");
        add("http://ouk8oqj7n.bkt.clouddn.com/");
        add("http://www.mcilife.com/");
        add("https://mcwebresource.mcilife.com/");
    }};

    List<String> SPECIAL_SYMBOL = new ArrayList<String>(){{
        add("_");
    }};

    String TYPE_ADD = "add";
    String TYPE_MINUS = "minus";

    String FINANCE_TYPE_SIGN_UP = "signUp";
    String FINANCE_TYPE_SIGN_UP_REMAIN = "signUpRemain";
    String FINANCE_TYPE_DROP_COURSE = "dropCourse";
    String CHANGE_STORE = "changeStore";

    List<String> WHITE_LIST = new ArrayList<String>(){{

    }};

}
