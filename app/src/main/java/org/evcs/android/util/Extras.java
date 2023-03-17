package org.evcs.android.util;

public class Extras {

    private Extras() {}

    public static final class Root {
        public static final String VIEW_KEY = "view";
        public static final String ID = "id";
        public static final String OPENING_KEY = "opened_app";
    }

    public static final class FilterActivity {
        public static final String FILTER_STATE = "Filter state";
    }

    public static final class LocationActivity {
        public static final String LOCATION = "Location";
    }

    public static final class StationsActivity {
        public static final String STATIONS = "Stations";
    }

    public static final class SearchActivity {
        public static final String HISTORY = "History";
        public static final String LATLNG = "Latlng";
        public static final String LOCATIONS = "Locations";
        public static final String VIEWPORT = "Viewport";
    }

    public static final class ForgotPassword {
        public static final String PATH = "/reset_password";
        public static final String EMAIL = "email";
        public static final String ID = "identifier";
    }

    public static class SessionInformationActivity {
        public static final String CHARGE = "Charge";
    }

    public static class PlanInfo {
        public static final String STATION_ID = "station_id";
    }

    public static class ChangePaymentMethod {
        public static final String PAYMENT_METHODS = "payment_methods";
    }

    public static class StartCharging {
        public static final String STATION_ID = "station_id";
        public static final String PM_ID = "pm_id";
        public static final String COUPONS = "coupons";
        public static final String SESSION = "session";
    }

    public static final class WebView {
        public static final String URL = "url";
    }

    public static final class Configuration {
        public static final String PHONE_NUMBER = "phone_number";
    }

    }
