package linyuzai.plugin.xml.attr;

public final class WidgetAttrValue {
    public static final String EMPTY = "";

    public static final class View {
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

        public static final class DrawingCacheQuality {
            public static final String LOW = "View.DRAWING_CACHE_QUALITY_LOW";
            public static final String HIGH = "View.DRAWING_CACHE_QUALITY_HIGH";
            public static final String AUTO = "View.DRAWING_CACHE_QUALITY_AUTO";
        }

        public static final class ImportantForAccessibility {
            public static final String AUTO = "View.IMPORTANT_FOR_ACCESSIBILITY_AUTO";
            public static final String YES = "View.IMPORTANT_FOR_ACCESSIBILITY_YES";
            public static final String NO = "View.IMPORTANT_FOR_ACCESSIBILITY_NO";
            public static final String NO_HIDE_DESCENDANTS = "View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS";
        }

        public static final class ImportantForAutofill {
            public static final String AUTO = "View.IMPORTANT_FOR_AUTOFILL_AUTO";
            public static final String YES = "View.IMPORTANT_FOR_AUTOFILL_YES";
            public static final String NO = "View.IMPORTANT_FOR_AUTOFILL_NO";
            public static final String YES_EXCLUDE_DESCENDANTS = "View.IMPORTANT_FOR_AUTOFILL_YES_EXCLUDE_DESCENDANTS";
            public static final String NO_EXCLUDE_DESCENDANTS = "View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS";
        }

        public static final class LayerType {
            public static final String NONE = "View.LAYER_TYPE_NONE";
            public static final String SOFTWARE = "View.LAYER_TYPE_SOFTWARE";
            public static final String HARDWARE = "View.LAYER_TYPE_HARDWARE";
        }

        public static final class LayoutDirection {
            public static final String LTR = "View.LAYOUT_DIRECTION_LTR";
            public static final String RTL = "View.LAYOUT_DIRECTION_RTL";
            public static final String INHERIT = "View.LAYOUT_DIRECTION_INHERIT";
            public static final String LOCALE = "View.LAYOUT_DIRECTION_LOCALE";
        }

        public static final class OutlineProvider {
            public static final String NONE = "null";
            public static final String BACKGROUND = "ViewOutlineProvider.BACKGROUND";
            public static final String BOUNDS = "ViewOutlineProvider.BOUNDS";
            public static final String PADDED_BOUNDS = "ViewOutlineProvider.PADDED_BOUNDS";
        }

        public static final class OverScrollMode {
            public static final String ALWAYS = "View.OVER_SCROLL_ALWAYS";
            public static final String IF_CONTENT_SCROLLS = "View.OVER_SCROLL_IF_CONTENT_SCROLLS";
            public static final String NEVER = "View.OVER_SCROLL_NEVER";

        }

        public static final class PointerIconType {
            public static final String NULL = "PointerIcon.TYPE_NULL";
            public static final String ARROW = "PointerIcon.TYPE_ARROW";
            public static final String CONTEXT_MENU = "PointerIcon.TYPE_CONTEXT_MENU";
            public static final String HAND = "PointerIcon.TYPE_HAND";
            public static final String HELP = "PointerIcon.TYPE_HELP";
            public static final String WAIT = "PointerIcon.TYPE_WAIT";
            public static final String CELL = "PointerIcon.TYPE_CELL";
            public static final String CROSSHAIR = "PointerIcon.TYPE_CROSSHAIR";
            public static final String TEXT = "PointerIcon.TYPE_TEXT";
            public static final String VERTICAL_TEXT = "PointerIcon.TYPE_VERTICAL_TEXT";
            public static final String ALIAS = "PointerIcon.TYPE_ALIAS";
            public static final String COPY = "PointerIcon.TYPE_COPY";
            public static final String NO_DROP = "PointerIcon.TYPE_NO_DROP";
            public static final String ALL_SCROLL = "PointerIcon.TYPE_ALL_SCROLL";
            public static final String HORIZONTAL_DOUBLE_ARROW = "PointerIcon.TYPE_HORIZONTAL_DOUBLE_ARROW";
            public static final String VERTICAL_DOUBLE_ARROW = "PointerIcon.TYPE_VERTICAL_DOUBLE_ARROW";
            public static final String TOP_RIGHT_DIAGONAL_DOUBLE_ARROW = "PointerIcon.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW";
            public static final String TOP_LEFT_DIAGONAL_DOUBLE_ARROW = "PointerIcon.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW";
            public static final String ZOOM_IN = "PointerIcon.TYPE_ZOOM_IN";
            public static final String ZOOM_OUT = "PointerIcon.TYPE_ZOOM_OUT";
            public static final String GRAB = "PointerIcon.TYPE_GRAB";
            public static final String GRABBING = "PointerIcon.TYPE_GRABBING";
        }

        public static final class ScrollIndicator {
            public static final String TOP = "View.SCROLL_INDICATOR_TOP";
            public static final String BOTTOM = "View.SCROLL_INDICATOR_BOTTOM";
            public static final String LEFT = "View.SCROLL_INDICATOR_LEFT";
            public static final String RIGHT = "View.SCROLL_INDICATOR_RIGHT";
            public static final String START = "View.SCROLL_INDICATOR_START";
            public static final String END = "View.SCROLL_INDICATOR_END";
        }

        public static final class ScrollBarStyle {
            public static final String SCROLLBARS_INSIDE_OVERLAY = "View.SCROLLBARS_INSIDE_OVERLAY";
            public static final String SCROLLBARS_INSIDE_INSET = "View.SCROLLBARS_INSIDE_INSET";
            public static final String SCROLLBARS_OUTSIDE_OVERLAY = "View.SCROLLBARS_OUTSIDE_OVERLAY";
            public static final String SCROLLBARS_OUTSIDE_INSET = "View.SCROLLBARS_OUTSIDE_INSET";
        }

        public static final class TextAlignment {
            public static final String INHERIT = "View.TEXT_ALIGNMENT_INHERIT";
            public static final String GRAVITY = "View.TEXT_ALIGNMENT_GRAVITY";
            public static final String TEXT_START = "View.TEXT_ALIGNMENT_TEXT_START";
            public static final String TEXT_END = "View.TEXT_ALIGNMENT_TEXT_END";
            public static final String CENTER = "View.TEXT_ALIGNMENT_CENTER";
            public static final String VIEW_START = "View.TEXT_ALIGNMENT_VIEW_START";
            public static final String VIEW_END = "View.TEXT_ALIGNMENT_VIEW_END";
        }

        public static final class TextDirection {
            public static final String INHERIT = "View.TEXT_DIRECTION_INHERIT";
            public static final String FIRST_STRONG = "View.TEXT_DIRECTION_FIRST_STRONG";
            public static final String ANY_RTL = "View.TEXT_DIRECTION_ANY_RTL";
            public static final String LTR = "View.TEXT_DIRECTION_LTR";
            public static final String RTL = "View.TEXT_DIRECTION_RTL";
            public static final String LOCALE = "View.TEXT_DIRECTION_LOCALE";
            public static final String FIRST_STRONG_LTR = "View.TEXT_DIRECTION_FIRST_STRONG_LTR";
            public static final String FIRST_STRONG_RTL = "View.TEXT_DIRECTION_FIRST_STRONG_RTL";
        }

        public static final class VerticalScrollbarPosition {
            public static final String SCROLLBAR_POSITION_DEFAULT = "View.SCROLLBAR_POSITION_DEFAULT";
            public static final String SCROLLBAR_POSITION_LEFT = "View.SCROLLBAR_POSITION_LEFT";
            public static final String SCROLLBAR_POSITION_RIGHT = "View.SCROLLBAR_POSITION_RIGHT";
        }

        public static final class Visibility {
            public static final String VISIBLE = "View.VISIBLE";
            public static final String INVISIBLE = "View.INVISIBLE";
            public static final String GONE = "View.GONE";
        }
    }

    public static final class ViewGroup {
        public static final class LayoutParams {
            public static final String MATCH_PARENT = "matchParent";
            public static final String WRAP_CONTENT = "wrapContent";
        }

        public static final class DescendantFocusability {
            public static final String FOCUS_BEFORE_DESCENDANTS = "ViewGroup.FOCUS_BEFORE_DESCENDANTS";
            public static final String FOCUS_AFTER_DESCENDANTS = "ViewGroup.FOCUS_AFTER_DESCENDANTS";
            public static final String FOCUS_BLOCK_DESCENDANTS = "ViewGroup.FOCUS_BLOCK_DESCENDANTS";
        }

        public static final class LayoutMode {
            public static final String CLIP_BOUNDS = "ViewGroup.LAYOUT_MODE_CLIP_BOUNDS";
            public static final String OPTICAL_BOUNDS = "ViewGroup.LAYOUT_MODE_CLIP_BOUNDS";
        }

        public static final class PersistentDrawingCache {
            public static final String NO_CACHE = "ViewGroup.PERSISTENT_NO_CACHE";
            public static final String ANIMATION_CACHE = "ViewGroup.PERSISTENT_ANIMATION_CACHE";
            public static final String SCROLLING_CACHE = "ViewGroup.PERSISTENT_SCROLLING_CACHE";
            public static final String ALL_CACHES = "ViewGroup.PERSISTENT_ALL_CACHES";
        }
    }

    public static final class LinearLayout {
        public static final class Orientation {
            public static final String VERTICAL = "LinearLayout.VERTICAL";
            public static final String HORIZONTAL = "LinearLayout.HORIZONTAL";
        }

        public static final class ShowDivider {
            public static final String NONE = "LinearLayout.SHOW_DIVIDER_NONE";
            public static final String BEGINNING = "LinearLayout.SHOW_DIVIDER_BEGINNING";
            public static final String MIDDLE = "LinearLayout.SHOW_DIVIDER_MIDDLE";
            public static final String END = "LinearLayout.SHOW_DIVIDER_END";
        }
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

    public static final class ScrollFlag {
        public static final String SCROLL = "AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL";
        public static final String EXIT_UNTIL_COLLAPSED = "AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED";
        public static final String ENTER_ALWAYS = "AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS";
        public static final String ENTER_ALWAYS_COLLAPSED = "AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED";
        public static final String SNAP = "AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP";
    }
}
