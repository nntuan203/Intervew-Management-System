package com.fa.ims.constant;

import java.util.ArrayList;
import java.util.Arrays;

public final class CommonConstants {

    public static final String USERNAME_SESSION = "USERNAME_SESSION";
    public static final String DEPARTMENT_OF_USER = "DEPARTMENT";

    public static final String FULLNAME_OF_USER_IN_SESSION="FULLNAME_SESSION";
    public static final ArrayList<String> VietnameseSigns = new ArrayList<>(

            Arrays.asList("aAeEoOuUiIdDyY",

                    "áàạảãâấầậẩẫăắằặẳẵ",

                    "ÁÀẠẢÃÂẤẦẬẨẪĂẮẰẶẲẴ",

                    "éèẹẻẽêếềệểễ",

                    "ÉÈẸẺẼÊẾỀỆỂỄ",

                    "óòọỏõôốồộổỗơớờợởỡ",

                    "ÓÒỌỎÕÔỐỒỘỔỖƠỚỜỢỞỠ",

                    "úùụủũưứừựửữ",

                    "ÚÙỤỦŨƯỨỪỰỬỮ",

                    "íìịỉĩ",

                    "ÍÌỊỈĨ",

                    "đ",

                    "Đ",

                    "ýỳỵỷỹ",

                    "ÝỲỴỶỸ"));


    public static final Integer PAGE_SIZE = 3;


    public static final ArrayList<String> Candidate_Status_Create = new ArrayList<>(Arrays.asList("Open", "Banned"));

}
