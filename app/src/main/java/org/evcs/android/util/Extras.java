package org.evcs.android.util;

public class Extras {

    private Extras() {}

    public static final class Root {
        public static final String VIEW_KEY = "view";
        public static final String ID = "id";
        public static final String OPENING_KEY = "opened_app";
    }

    public static class AuthActivity {
        public static final String SKIP_ROOT = "skip_root";
    }

    public static class MainActivity {
        public static final String IS_BOTTOM = "is_bottom";
    }

    public static final class LocationActivity {
        public static final String LOCATION = "Location";
    }

    public static final class SearchActivity {
        public static final String HISTORY = "History";
    }

    public static final class VerifyActivity {
        public static final String RESULT = "result";
        public static final String USE_CASE = "from_auth";
        public static final String ASK_FOR_CAR = "ask_for_car";
    }

    public static final class ForgotPassword {
        public static final String PATH = "/reset_password";
        public static final String EMAIL = "email";
        public static final String ID = "identifier";
    }

    public static class SessionInformationActivity {
        public static final String CHARGE = "Charge";
        public static final String CHARGE_ID = "ChargeId";
        public static final String PAYMENT = "Subscription";
    }

    public static class PlanInfo {
        public static final String FROM_QR = "from_qr";
        public static final String STATION_ID = "station_id";
    }

    public static class ChangePaymentMethod {
        public static final String PAYMENT_METHODS = "payment_methods";
        public static final String FINISH_ON_CLICK = "finish_on_click";
    }

    public static class StartCharging {
        public static final String STATION_ID = "station_id";
        public static final String PM_ID = "pm_id";
        public static final String COUPONS = "coupons";
        public static final String SESSION = "session";
    }

    public static final class WebView {
        public static final String SUBTITLE = "subtitle";
        public static final String TITLE = "title";
        public static final String URL = "url";
    }

    public static final class Configuration {
        public static final String PHONE_NUMBER = "phone_number";
    }

    public static class CreditCard {
        public static final String CREDIT_CARD = "credit_card";
    }

    public static class ContactSupportActivity {
        public static final String SHOW_ADDRESS = "show_address";
    }

    public static class PlanActivity {
        public static final String PLAN = "plan";
        public static final String IS_CORPORATE = "show_corporate_plans";
    }
}
