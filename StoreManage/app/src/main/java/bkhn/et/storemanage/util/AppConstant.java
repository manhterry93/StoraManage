package bkhn.et.storemanage.util;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by PL_itto on 2/12/2018.
 */

public class AppConstant {
    public final static String TAGG = "PL_itto.";
    public final static int FAILE_VALUE = -1;

    public final static class UserMode {
        public static final int NONE = -1;
        public static final int USER_MANAGER = 200;
        public static final int USER_STAFF = 201;
    }

    public final static class Main {
        public static final String EXTRA_USER_ID = "user_id";
        public static final String EXTRA_USER_MODE = "user_mode";
    }

    public final static class Bill {
        public static final String EXTRA_BILL_MODE = "bill_mode";
        public static final int BILL_IMPORT = 0;
        public static final int BILL_PAY = 1;

        public static final String EXTRA_USER_ID ="extra_user_id";
        public static final String EXTRA_USER_NAME = "extra_user_name";

        public static final int REQUEST_QR_CODE = 100;
    }

    public static final class Scanner {
        public static final String CODE_RESULT = "code_result";
    }

    public static final class StaffInfo{
        public static final String EXTRA_USER_ID ="extra_user_id";
    }

    public static final class ReportInfo{
        public static final String EXTRA_REPORT_ID ="extra_report_id";
    }
}
