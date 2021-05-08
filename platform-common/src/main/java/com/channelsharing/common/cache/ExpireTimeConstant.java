package com.channelsharing.common.cache;

/**
 * Created by liuhangjun on 2017/9/7.
 */
public interface ExpireTimeConstant {

    long NONE = 0L; // "无固定期限"


    long ONE_SEC = 1L; // "1秒钟"


    long FIVE_SEC = 5L;  //  "5秒钟"

    long TEN_SEC = 10L;  //  "10秒钟"


    long HALF_A_MIN = 30L;  //  "30秒钟"


    long ONE_MIN = 60L;  //  "1分钟"


    long FIVE_MIN = 5 * 60L;  //  "5分钟"


    long TEN_MIN = 10 * 60L;  //  "10分钟"


    long TWENTY_MIN = 20 * 60L;  //  "20分钟"


    long HALF_AN_HOUR = 30 * 60L;  //  "30分钟"


    long ONE_HOUR = 60 * 60L;  //  "1小时"


    long SIX_HOUR = 6 * 60 * 60L;  //  "6小时"


    long TWELVE_HOUR = 12 * 60 * 60L;  //  "12小时"


    long ONE_DAY = 24 * 60 * 60L;  //  "1天"
    
    long ONE_WEEK = 7 * 24 * 60 * 60L;  //  "1个星期"

    long HALF_A_MON = 15 * 24 * 60 * 60L;  //  "半个月"

    long ONE_MON = 30 * 24 * 60 * 60L;  //  "1个月"


    long ONE_YEAR = 365 * 24 * 60 * 60L;  //  "1年"

}
