package com.haihua.hhplms.common.utils;

import com.haihua.hhplms.common.entity.BaseEnum;

public class EnumUtil {
    public static <T extends Enum<?> & BaseEnum> T codeOf(Class<T> enumClass, String code) {
        T [] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (enumConstant.getCode().equals(code)) {
                return enumConstant;
            }
        }
        return null;
    }
}
