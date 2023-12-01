package site.bleem.boot.web.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author zhouyanhuang：
 * @Date 2023/4/24 17:06
 * @Version 1.0
 */

public class IdCardUtil {
    /**
     * 一代身份证
     */
    private static final Integer FIRST_GENERATION_ID_CARD = 15;

    /**
     * 二代身份证
     */
    private static final Integer SECOND_GENERATION_ID_CARD = 18;

    /**
     * 省编码前2位
     */
    private static final List<String> PROVINCES = Arrays.asList(
            "11", "12", "13", "14", "15",
            "21", "22", "23", "31", "32",
            "33", "34", "35", "36", "37",
            "41", "42", "43", "44", "45",
            "46", "50", "51", "52", "53",
            "54", "61", "62", "63", "64",
            "65", "71", "81", "82", "91"
    );

    /**
     * 加权因子
     */
    private static final Integer[] FACTOR = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 身份证正则
     */
    private static final String REGEX = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

    /**
     * 校验位
     */
    private static final String[] PARITY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    /**
     * format
     */
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * now现在
     */
    private static final Date NOW = new Date();

    /**
     * 验证身份证是否合法
     *
     * @param idCard 身份证号码
     * @return 身份证信息对象
     */
    public static IdCardInformation identityCodeValid(String idCard) {
        final String finalIdCard = trim(idCard);
        // 校验身份证
        if ("".equals(finalIdCard) || finalIdCard.length() != 18) {
            return new IdCardInformation();
        }
        // 匹配正则
        if (!Pattern.compile(REGEX).matcher(finalIdCard).matches()) {
            return new IdCardInformation();
        }
        // 校验身份证前2位省编码是否正确
        if (PROVINCES.stream().noneMatch(i -> i.equals(finalIdCard.substring(0, 2)))) {
            return new IdCardInformation();
        }
        // 切割厂
        String[] code = finalIdCard.split("");
        // 拿到最后一位字母
        String lastOneCode = code[17];
        int sum = 0, ai, wi;
        // 取前17位，（每一位数字 * 每一位数字在加权因子数组的对应的数字） 之和
        for (int i = 0; i < 17; i++) {
            ai = Integer.parseInt(code[i]);
            wi = FACTOR[i];
            sum += ai * wi;
        }
        // 算出来的和 mod 11 得到 最后一位，再把传进来的身份证的最后一位和算出来的最后一位对比
        return lastOneCode.equals(PARITY[sum % 11]) ? new IdCardInformation(true, getSex(finalIdCard), getAge(finalIdCard), getBirthday(finalIdCard)) : new IdCardInformation();
    }

    /**
     * 根据身份证号获取性别
     *
     * @param idCard 身份证号码
     * @return 性别
     */
    private static boolean getSex(String idCard) {
        // 一代身份证
        if (idCard.length() == FIRST_GENERATION_ID_CARD) {
            return Integer.parseInt(idCard.substring(14, 15)) % 2 != 0;
        } else if (idCard.length() == SECOND_GENERATION_ID_CARD) {
            // 二代身份证 判断性别
            return Integer.parseInt(idCard.substring(16).substring(0, 1)) % 2 != 0;
        } else {
            return true;
        }
    }

    /**
     * 根据身份证号获取年龄
     *
     * @param idCard 身份证号码
     * @return age
     */
    private static Integer getAge(String idCard) {
        int age = 0;
        //15位身份证号
        if (idCard.length() == FIRST_GENERATION_ID_CARD) {
            // 身份证上的年份(15位身份证为1980年前的)
            String idCardYear = "19" + idCard.substring(6, 8);
            // 身份证上的月份
            String idCardMonth = idCard.substring(8, 10);
            age = getAge(idCardYear, idCardMonth);
            //18位身份证号
        } else if (idCard.length() == SECOND_GENERATION_ID_CARD) {
            // 身份证上的年份
            String idCardYear = idCard.substring(6).substring(0, 4);
            // 身份证上的月份
            String idCardMonth = idCard.substring(10).substring(0, 2);
            age = getAge(idCardYear, idCardMonth);
        }
        return age;
    }

    /**
     * 计算年月获得年龄
     *
     * @param idCardYear  身份证的上年份
     * @param idCardMonth 身份的证上月份
     * @return age
     */
    private static Integer getAge(String idCardYear, String idCardMonth) {
        // 当前年份
        String nowYear = FORMAT.format(NOW).substring(0, 4);
        // 当前月份
        String nowMonth = FORMAT.format(NOW).substring(5, 7);
        int yearDiff = Integer.parseInt(nowYear) - Integer.parseInt(idCardYear);
        if (Integer.parseInt(idCardMonth) <= Integer.parseInt(nowMonth)) {
            return yearDiff + 1;
            // 当前用户还没过生
        } else {
            return yearDiff;
        }
    }

    /**
     * 获取出生日期  yyyy年MM月dd日
     *
     * @param idCard 身份证号码
     * @return 生日
     */
    private static LocalDate getBirthday(String idCard) {
        String year = "";
        String month = "";
        String day = "";
        //15位身份证号
        if (idCard.length() == FIRST_GENERATION_ID_CARD) {
            // 身份证上的年份(15位身份证为1980年前的)
            year = "19" + idCard.substring(6, 8);
            //身份证上的月份
            month = idCard.substring(8, 10);
            //身份证上的日期
            day = idCard.substring(10, 12);
        } else if (idCard.length() == SECOND_GENERATION_ID_CARD) {
            // 18位身份证号 身份证上的年份
            year = idCard.substring(6).substring(0, 4);
            // 身份证上的月份
            month = idCard.substring(10).substring(0, 2);
            //身份证上的日期
            day = idCard.substring(12).substring(0, 2);
        }
        try {
            return LocalDate.now().withYear(Integer.parseInt(year)).withMonth(Integer.parseInt(month)).withDayOfMonth(Integer.parseInt(day));
        } catch (Exception e) {
            int start = idCard.indexOf("19");
            return LocalDate.now().withYear(Integer.parseInt(idCard.substring(start, start + 4))).withMonth(Integer.parseInt(idCard.substring(start + 4, start + 6)))
                    .withDayOfMonth(Integer.parseInt(idCard.substring(start + 6, start + 8)));
        }
    }

    /**
     * 去空格
     *
     * @param str 处理字符串
     * @return 结果字符串
     */
    private static String trim(String str) {
        return str.replaceAll("\n", "").replace(" ", "").trim();
    }

    /**
     * 身份证信息
     */
    @Getter
    @NoArgsConstructor
    public static class IdCardInformation {

        /**
         * 是否成功
         */
        private boolean success = false;

        /**
         * 性别
         */
        private boolean sex;

        /**
         * 年龄
         */
        private Integer age;

        /**
         * 生日
         */
        private LocalDate birthday;

        public IdCardInformation(boolean success, boolean sex, Integer age, LocalDate birthday) {
            this.success = success;
            this.sex = sex;
            this.age = age;
            this.birthday = birthday;
        }
    }
}
