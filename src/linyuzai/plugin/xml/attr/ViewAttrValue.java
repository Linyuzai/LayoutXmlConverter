package linyuzai.plugin.xml.attr;

public final class ViewAttrValue {
    public static final String EMPTY = "";

    public static final class LayoutParams {
        public static final String MATCH_PARENT = "matchParent";
        public static final String WRAP_CONTENT = "wrapContent";
    }

    public static final class Orientation {
        public static final String VERTICAL = "LinearLayout.VERTICAL";
        public static final String HORIZONTAL = "LinearLayout.HORIZONTAL";
    }

    public static final class Gravity {
        public static final String CENTER = "Gravity.CENTER";
        public static final String CENTER_VERTICAL = "Gravity.CENTER_VERTICAL";
        public static final String CENTER_HORIZONTAL = "Gravity.CENTER_HORIZONTAL";
        public static final String START = "Gravity.START";
        public static final String END = "Gravity.END";
        public static final String TOP = "Gravity.TOP";
        public static final String BOTTOM = "Gravity.BOTTOM";
        public static final String LEFT = "Gravity.LEFT";
        public static final String RIGHT = "Gravity.RIGHT";
        public static final String FILL = "Gravity.FILL";
        public static final String FILL_VERTICAL = "Gravity.FILL_VERTICAL";
        public static final String FILL_HORIZONTAL = "Gravity.FILL_HORIZONTAL";
        public static final String CLIP_VERTICAL = "Gravity.CLIP_VERTICAL";
        public static final String CLIP_HORIZONTAL = "Gravity.CLIP_HORIZONTAL";
    }

    public static final class ScaleType {
        public static final String CENTER = "ImageView.ScaleType.CENTER";
        public static final String CENTER_CROP = "ImageView.ScaleType.CENTER_CROP";
        public static final String CENTER_INSIDE = "ImageView.ScaleType.CENTER_INSIDE";
        public static final String FIT_CENTER = "ImageView.ScaleType.FIT_CENTER";
        public static final String FIT_START = "ImageView.ScaleType.FIT_START";
        public static final String FIT_END = "ImageView.ScaleType.FIT_END";
        public static final String FIT_XY = "ImageView.ScaleType.FIT_XY";
        public static final String MATRIX = "ImageView.ScaleType.MATRIX";
    }

    public static final class TextStyle {
        public static final String NORMAL = "Typeface.NORMAL";
        public static final String BOLD = "Typeface.BOLD";
        public static final String ITALIC = "Typeface.ITALIC";
        public static final String BOLD_ITALIC = "Typeface.BOLD_ITALIC";
    }

    public static final class InputType {
        public static final String NONE = "InputType.TYPE_NULL";
        public static final String TEXT = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL";
        public static final String TEXT_CAP_CHARACTERS = "InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS";
        public static final String TEXT_CAP_WORDS = "InputType.TYPE_TEXT_FLAG_CAP_WORDS";
        public static final String TEXT_CAP_SENTENCES = "InputType.TYPE_TEXT_FLAG_CAP_SENTENCES";
        public static final String TEXT_AUTO_CORRECT = "InputType.TYPE_TEXT_FLAG_AUTO_CORRECT";
        public static final String TEXT_AUTO_COMPLETE = "InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE";
        public static final String TEXT_MULTI_LINE = "InputType.TYPE_TEXT_FLAG_MULTI_LINE";
        public static final String TEXT_IME_MULTI_LINE = "InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE";
        public static final String TEXT_NO_SUGGESTIONS = "InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS";
        public static final String TEXT_URI = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI";
        public static final String TEXT_EMAIL_ADDRESS = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS";
        public static final String TEXT_EMAIL_SUBJECT = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT";
        public static final String TEXT_SHORT_MESSAGE = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE";
        public static final String TEXT_LONG_MESSAGE = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE";
        public static final String TEXT_PERSON_NAME = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME";
        public static final String TEXT_POSTAL_ADDRESS = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS";
        public static final String TEXT_PASSWORD = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD";
        public static final String TEXT_VISIBLE_PASSWORD = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD";
        public static final String TEXT_WEB_EDIT_TEXT = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT";
        public static final String TEXT_FILTER = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_FILTER";
        public static final String TEXT_PHONETIC = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PHONETIC";
        public static final String TEXT_WEB_EMAIL_ADDRESS = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS";
        public static final String TEXT_WEB_PASSWORD = "InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD";
        public static final String NUMBER = "InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL";
        public static final String NUMBER_SIGNED = "InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED";
        public static final String NUMBER_DECIMAL = "InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL";
        public static final String NUMBER_PASSWORD = "InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD";
        public static final String PHONE = "InputType.TYPE_CLASS_PHONE";
        public static final String DATETIME = "InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_NORMAL";
        public static final String DATE = "InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE";
        public static final String TIME = "InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME";
    }

    public static final class ScrollFlag {
        public static final String SCROLL = "AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL";
        public static final String EXIT_UNTIL_COLLAPSED = "AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED";
        public static final String ENTER_ALWAYS = "AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS";
        public static final String ENTER_ALWAYS_COLLAPSED = "AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED";
        public static final String SNAP = "AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP";
    }

    public static final class AccessibilityLiveRegion {
        public static final String NONE = "View.ACCESSIBILITY_LIVE_REGION_NONE";
        public static final String ASSERTIVE = "View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE";
        public static final String POLITE = "View.ACCESSIBILITY_LIVE_REGION_POLITE";
    }

    public static final class AutofillHint {
        public static final String EMAIL_ADDRESS = "View.AUTOFILL_HINT_EMAIL_ADDRESS";
        public static final String NAME = "View.AUTOFILL_HINT_NAME";
        public static final String USERNAME = "View.AUTOFILL_HINT_USERNAME";
        public static final String PASSWORD = "View.AUTOFILL_HINT_PASSWORD";
        public static final String PHONE = "View.AUTOFILL_HINT_PHONE";
        public static final String POSTAL_ADDRESS = "View.AUTOFILL_HINT_POSTAL_ADDRESS";
        public static final String POSTAL_CODE = "View.AUTOFILL_HINT_POSTAL_CODE";
        public static final String CREDIT_CARD_NUMBER = "View.AUTOFILL_HINT_CREDIT_CARD_NUMBER";
        public static final String CREDIT_CARD_SECURITY_CODE = "View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE";
        public static final String CREDIT_CARD_EXPIRATION_DATE = "View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE";
        public static final String CREDIT_CARD_EXPIRATION_MONTH = "View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH";
        public static final String CREDIT_CARD_EXPIRATION_YEAR = "View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR";
        public static final String CREDIT_CARD_EXPIRATION_DAY = "View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY";
    }

    public static final class PorterDuffMode {
        public static final String CLEAR = "PorterDuff.Mode.CLEAR";
        public static final String SRC = "PorterDuff.Mode.SRC";
        public static final String DST = "PorterDuff.Mode.DST";
        public static final String SRC_OVER = "PorterDuff.Mode.SRC_OVER";
        public static final String DST_OVER = "PorterDuff.Mode.DST_OVER";
        public static final String SRC_IN = "PorterDuff.Mode.SRC_IN";
        public static final String DST_IN = "PorterDuff.Mode.DST_IN";
        public static final String SRC_OUT = "PorterDuff.Mode.SRC_OUT";
        public static final String DST_OUT = "PorterDuff.Mode.DST_OUT";
        public static final String SRC_ATOP = "PorterDuff.Mode.SRC_ATOP";
        public static final String DST_ATOP = "PorterDuff.Mode.DST_ATOP";
        public static final String XOR = "PorterDuff.Mode.XOR";
        public static final String DARKEN = "PorterDuff.Mode.DARKEN";
        public static final String LIGHTEN = "PorterDuff.Mode.LIGHTEN";
        public static final String MULTIPLY = "PorterDuff.Mode.MULTIPLY";
        public static final String SCREEN = "PorterDuff.Mode.SCREEN";
        public static final String ADD = "PorterDuff.Mode.ADD";
        public static final String OVERLAY = "PorterDuff.Mode.OVERLAY";
    }

    public static final class DrawingCacheQuality {
        public static final String LOW = "View.DRAWING_CACHE_QUALITY_LOW";
        public static final String HIGH = "View.DRAWING_CACHE_QUALITY_HIGH";
        public static final String AUTO = "View.DRAWING_CACHE_QUALITY_AUTO";
    }

}
