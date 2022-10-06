package org.evcs.android.util;

public class Extras {

    private Extras() {}

    public static class CarshareSubscription {
        public static final String PLAN_KEY = "plan_key";
        public static final String PLAN_SUBSCRIPTION = "plan_subscription";
        public static final String SHOW_HAMBURGUER = "show_hamburguer";
    }

    public static final class VanpoolMap {
        public static final String VANPOOLS = "vanpools";
    }

    public static final class VanpoolDetails {
        public static final String VANPOOL  = "vanpool_detail";
        public static final String VANPOOL_INSTANCE = "vanpool_instance";
        public static final String VANPOOL_STATE = "vanpool_state";
        public static final String TOOLBAR_STATE = "toolbar_state";
    }

    public static final class VanpoolJoinFinish {
        public static final String VANPOOL = "vanpool_join_finish";
    }

    public static final class VanpoolSearch {
        public static final String PARAMS = "vanpool_search_params";
        public static final String TITLE = "vanpool_search_title";
        public static final String ICON = "vanpool_search_icon";
        public static final String HINT = "vanpool_search_time";
    }

    public static final class VanpoolInvite {
        public static final String VANPOOL_ID = "vanpool_invite_id";
        public static final String VANPOOL_EMAILS = "vanpool_invite_emails";
    }

    public static final class VanpoolAbsence {
        public static final String VANPOOL_ID = "vanpool_absence_id";
        public static final String TIMEZONE = "vanpool_absence_tz";
    }

    public static final class VanpoolSubscription {
        public static final String VANPOOL = "subscribe_vanpool_id";
    }

    public static final class VanpoolPreChecklist {
        public static final String VANPOOL = "pre_checklist_vanpool";
        public static final String VANPOOL_INSTANCE = "pre_checklist_vanpool_instance";
        public static final String CHECKLIST = "simple_pre_checklist";
    }

    public static final class VanpoolOngoingRide {
        public static final String VANPOOL = "vanpool_ongoing_ride";
        public static final String SHOW_WEB_VIEW = "vanpool_ongoing_show_web_view";
        public static final String STATE = "ongoing_ride_state";
        public static final String GEOTABS_TOKEN = "ORS2";
    }

    public static final class VanpoolPostChecklist {
        public static final String CHECKLIST = "simple_post_checklist";
    }

    public static final class CreateBooking {
        public static final String PARKING_SPACE = "create_booking_parking_space";
        public static final String PICK_UP_TIME = "create_booking_pick_up";
        public static final String DROP_OFF_TIME = "create_booking_drop_off";
        public static final String CAR = "create_booking_car";
        public static final String CORPORATE = "create_booking_corporate";
    }

    public static final class ChooseCar {
        public static final String BOOKABLE_CARS = "bookable_cars";
        public static final String PICK_UP_TIME = "pick_up_time";
        public static final String DROP_OFF_TIME = "drop_off_time";
        public static final String DISTANCE_TO_CARS = "distance_to_cars";
        public static final String PARKING_SPACE = "parking_space";
        public static final String CAR_CHOSEN = "car_chosen";
    }

    public static final class MyTrip {
        public static final String CAR_BOOKING = "my_trip_car_booking";
        public static final String SHOW_POPUP = "my_trip_show_popup";
        public static final String INITIAL_ACTION = "my_trip_initial_action";
        public static final String VANPOOL_DETAILS = "my_vanpool_trip_details";
        public static final String IMAGES_LIST = "my_trip_images_list";
    }

    public static final class FutureReservation {
        public static final String NAME = "future_reseration_name";
        public static final String TITLE = "future_reservation_title";
        public static final String ADDRESS = "future_reservation_address";
        public static final String TIME_FROM = "future_reservation_time_from";
        public static final String TIME_TO = "future_reservation_time_to";
        public static final String LATITUDE = "future_reservation_latitude";
        public static final String LONGITUDE = "future_reservation_longitude";
        public static final String CORPORATE = "future_reservation_corporate";
        public static final String STATE = "future_reservation_state";
    }

    public static final class CarSharePreChecklist {
        public static final String CAR_BOOKING = "pre_checklist_car_booking";
        public static final String CHECKLIST = "simple_car_share_pre_checklist";
    }

    public static final class CarSharePostChecklist {
        public static final String CHECKLIST = "simple_car_share_post_checklist";
        public static final String CAR_BOOKING = "post_checklist_car_booking";
    }

    public static final class CarShareOngoingRide {
        public static final String CAR_SHARE = "car_share_ongoing_ride";
        public static final String CAR = "car_ongoing_ride";
        public static final String SHOW_WEB_VIEW = "carshare_ongoing_show_web_view";
        public static final String DEPARTURE_INFORMATION = "carshare_ongoing_departure_information";
    }

    public static final class CarSharePayBooking {
        public static final String BOOKING_ID = "car_share_pay_booking_id";
        public static final String BOOKING_PRICE = "car_share_pay_booking_price";
        public static final String WALLET_AMOUNT = "car_share_pay_wallet_amount";
    }

    public static final class BookNow {
        public static final String TITLE = "book_now_title";
        public static final String CORPORATE = "book_now_corporate";
        public static final String PICK_UP_TIME = "book_now_pick_up_time";
        public static final String DROP_OFF_TIME = "book_now_drop_off_time";
    }

    public static final class IncidentReport {
        public static final String CAR_BOOKING = "car_booking_id";
    }

    public static final class SearchCar {
        public static final String TITLE = "search_car_title";
        public static final String CORPORATE = "search_car_corporate";
    }

    public static final class Configuration {
        public static final String MIN_AGE_LIMIT = "min_age_limit";
        public static final String FUTURE_BOOKING_HOURS = "max_future_booking_hours";
        public static final String MAX_BOOKING_DURATION_HOURS = "max_booking_duration_hours";
        public static final String DEFAULT_MILES_SEARCH_RADIUS = "default_miles_search_radius";
        public static final String EARLY_CANCELLATION_GRACE_PERIOD = "early_cancellation_grace_period";
        public static final String HALF_REFUND_HOURS = "half_refund_hours";
        public static final String FULL_REFUND_HOURS = "full_refund_hours";
        public static final String MAX_INCIDENT_PICTURES = "max_incident_pictures";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String VANPOOL_POSTCHECKLIST_DURATION = "vanpool_post_checklist_duration";
        public static final String CARSHARE_POSTCHECKLIST_DURATION = "carshare_post_checklist_duration";
    }

    public static final class Root {
        public static final String VIEW_KEY = "view";
        public static final String ID = "id";
        public static final String OPENING_KEY = "opened_app";
    }

    public static final class ForgotPassword {
        public static final String EMAIL = "email";
        public static final String ID = "identifier";
    }

    public static final class PictureDialog {
        public static final String TITLE = "picture_dialog_title";
    }

    public static final class PaymentDetails {
        public static final String PAYMENT = "payment";
    }

    public static final class PromoCodeDetails {
        public static final String PROMO_CODE = "promo_code";
    }

    public static final class WebView {
        public static final String WEB_VIEW_RESPONSE = "web_view_response";
    }

    public static final class CarbonFootprint {
        public static final String STATS = "CO2_stats";
    }

    public static final class Placeholder {
        public static final String TITLE = "placeholder_title";
    }
}
