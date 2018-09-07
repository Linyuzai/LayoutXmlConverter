package linyuzai.plugin.xml.attr;

public final class XmlAttrValue {
    public static final String NONE = "none";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final class View {

        public static final class AccessibilityLiveRegion {
            public static final String NONE = "none";
            public static final String ASSERTIVE = "assertive";
            public static final String POLITE = "polite";
        }

        public static final class AutofillHint {
            public static final String EMAIL_ADDRESS = "emailAddress";
            public static final String NAME = "name";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String PHONE = "phone";
            public static final String POSTAL_ADDRESS = "postalAddress";
            public static final String POSTAL_CODE = "postalCode";
            public static final String CREDIT_CARD_NUMBER = "creditCardNumber";
            public static final String CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode";
            public static final String CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate";
            public static final String CREDIT_CARD_EXPIRATION_MONTH = "creditCardExpirationMonth";
            public static final String CREDIT_CARD_EXPIRATION_YEAR = "creditCardExpirationYear";
            public static final String CREDIT_CARD_EXPIRATION_DAY = "creditCardExpirationDay";
        }

        public static final class DrawingCacheQuality {
            public static final String LOW = "low";
            public static final String HIGH = "high";
            public static final String AUTO = "auto";
        }

        public static final class Focusable {
            public static final String AUTO = "auto";
        }

        public static final class ImportantForAccessibility {
            public static final String AUTO = "auto";
            public static final String YES = "yes";
            public static final String NO = "no";
            public static final String NO_HIDE_DESCENDANTS = "noHideDescendants";
        }

        public static final class ImportantForAutofill {
            public static final String AUTO = "auto";
            public static final String YES = "yes";
            public static final String NO = "no";
            public static final String YES_EXCLUDE_DESCENDANTS = "yesExcludeDescendants";
            public static final String NO_EXCLUDE_DESCENDANTS = "noExcludeDescendants";
        }

        public static final class LayerType {
            public static final String NONE = "none";
            public static final String SOFTWARE = "software";
            public static final String HARDWARE = "hardware";
        }

        public static final class LayoutDirection {
            public static final String LTR = "ltr";
            public static final String RTL = "rtl";
            public static final String INHERIT = "inherit";
            public static final String LOCALE = "locale";
        }

        public static final class OutlineProvider {
            public static final String NONE = "none";
            public static final String BACKGROUND = "background";
            public static final String BOUNDS = "bounds";
            public static final String PADDED_BOUNDS = "paddedBounds";
        }

        public static final class OverScrollMode {
            public static final String ALWAYS = "always";
            public static final String IF_CONTENT_SCROLLS = "ifContentScrolls";
            public static final String NEVER = "never";
        }

        public static final class PointerIconType {
            public static final String NULL = "none";
            public static final String ARROW = "arrow";
            public static final String CONTEXT_MENU = "context_menu";
            public static final String HAND = "hand";
            public static final String HELP = "help";
            public static final String WAIT = "wait";
            public static final String CELL = "cell";
            public static final String CROSSHAIR = "crosshair";
            public static final String TEXT = "text";
            public static final String VERTICAL_TEXT = "vertical_text";
            public static final String ALIAS = "alias";
            public static final String COPY = "copy";
            public static final String NO_DROP = "no_drop";
            public static final String ALL_SCROLL = "all_scroll";
            public static final String HORIZONTAL_DOUBLE_ARROW = "horizontal_double_arrow";
            public static final String VERTICAL_DOUBLE_ARROW = "vertical_double_arrow";
            public static final String TOP_RIGHT_DIAGONAL_DOUBLE_ARROW = "top_right_diagonal_double_arrow";
            public static final String TOP_LEFT_DIAGONAL_DOUBLE_ARROW = "top_left_diagonal_double_arrow";
            public static final String ZOOM_IN = "zoom_in";
            public static final String ZOOM_OUT = "zoom_out";
            public static final String GRAB = "grab";
            public static final String GRABBING = "grabbing";
        }

        public static final class ScrollIndicator {
            public static final String TOP = "top";
            public static final String BOTTOM = "bottom";
            public static final String LEFT = "left";
            public static final String RIGHT = "right";
            public static final String START = "start";
            public static final String END = "end";
        }

        public static final class ScrollBarStyle {
            public static final String SCROLLBARS_INSIDE_OVERLAY = "insideOverlay";
            public static final String SCROLLBARS_INSIDE_INSET = "insideInset";
            public static final String SCROLLBARS_OUTSIDE_OVERLAY = "outsideOverlay";
            public static final String SCROLLBARS_OUTSIDE_INSET = "outsideInset";
        }

        public static final class TextAlignment {
            public static final String INHERIT = "inherit";
            public static final String GRAVITY = "gravity";
            public static final String TEXT_START = "textStart";
            public static final String TEXT_END = "textEnd";
            public static final String CENTER = "center";
            public static final String VIEW_START = "viewStart";
            public static final String VIEW_END = "viewEnd";
        }

        public static final class TextDirection {
            public static final String INHERIT = "inherit";
            public static final String FIRST_STRONG = "firstStrong";
            public static final String ANY_RTL = "anyRtl";
            public static final String LTR = "ltr";
            public static final String RTL = "rtl";
            public static final String LOCALE = "locale";
            public static final String FIRST_STRONG_LTR = "firstStrongLtr";
            public static final String FIRST_STRONG_RTL = "firstStrongRtl";
        }

        public static final class VerticalScrollbarPosition {
            public static final String SCROLLBAR_POSITION_DEFAULT = "defaultPosition";
            public static final String SCROLLBAR_POSITION_LEFT = "left";
            public static final String SCROLLBAR_POSITION_RIGHT = "right";
        }

        public static final class Visibility {
            public static final String VISIBLE = "visible";
            public static final String INVISIBLE = "invisible";
            public static final String GONE = "gone";
        }
    }

    public static final class ViewGroup {
        public static final class LayoutParams {
            public static final String FILL_PARENT = "fill_parent";
            public static final String MATCH_PARENT = "match_parent";
            public static final String WRAP_CONTENT = "wrap_content";
        }

        public static final class DescendantFocusability {
            public static final String FOCUS_BEFORE_DESCENDANTS = "beforeDescendants";
            public static final String FOCUS_AFTER_DESCENDANTS = "afterDescendants";
            public static final String FOCUS_BLOCK_DESCENDANTS = "blocksDescendants";
        }

        public static final class LayoutMode {
            public static final String CLIP_BOUNDS = "clipBounds";
            public static final String OPTICAL_BOUNDS = "opticalBounds";
        }

        public static final class PersistentDrawingCache {
            public static final String NO_CACHE = "none";
            public static final String ANIMATION_CACHE = "animation";
            public static final String SCROLLING_CACHE = "scrolling";
            public static final String ALL_CACHES = "all";
        }
    }

    public static final class Orientation {
        public static final String VERTICAL = "vertical";
        public static final String HORIZONTAL = "horizontal";
    }

    public static final class Gravity {
        public static final String CENTER = "center";
        public static final String CENTER_VERTICAL = "center_vertical";
        public static final String CENTER_HORIZONTAL = "center_horizontal";
        public static final String START = "start";
        public static final String END = "end";
        public static final String TOP = "top";
        public static final String BOTTOM = "bottom";
        public static final String LEFT = "left";
        public static final String RIGHT = "right";
        public static final String FILL = "fill";
        public static final String FILL_VERTICAL = "fill_vertical";
        public static final String FILL_HORIZONTAL = "fill_horizontal";
        public static final String CLIP_VERTICAL = "clip_vertical";
        public static final String CLIP_HORIZONTAL = "clip_horizontal";
    }

    public static final class ScaleType {
        public static final String CENTER = "center";
        public static final String CENTER_CROP = "centerCrop";
        public static final String CENTER_INSIDE = "centerInside";
        public static final String FIT_CENTER = "fitCenter";
        public static final String FIT_START = "fitStart";
        public static final String FIT_END = "fitEnd";
        public static final String FIT_XY = "fitXY";
        public static final String MATRIX = "matrix";
    }

    public static final class TextStyle {
        public static final String NORMAL = "normal";
        public static final String BOLD = "bold";
        public static final String ITALIC = "italic";
    }

    public static final class InputType {
        public static final String NONE = "none";
        public static final String TEXT = "text";
        public static final String TEXT_CAP_CHARACTERS = "textCapCharacters";
        public static final String TEXT_CAP_WORDS = "textCapWords";
        public static final String TEXT_CAP_SENTENCES = "textCapSentences";
        public static final String TEXT_AUTO_CORRECT = "textAutoCorrect";
        public static final String TEXT_AUTO_COMPLETE = "textAutoComplete";
        public static final String TEXT_MULTI_LINE = "textMultiLine";
        public static final String TEXT_IME_MULTI_LINE = "textImeMultiLine";
        public static final String TEXT_NO_SUGGESTIONS = "textNoSuggestions";
        public static final String TEXT_URI = "textUri";
        public static final String TEXT_EMAIL_ADDRESS = "textEmailAddress";
        public static final String TEXT_EMAIL_SUBJECT = "textEmailSubject";
        public static final String TEXT_SHORT_MESSAGE = "textShortMessage";
        public static final String TEXT_LONG_MESSAGE = "textLongMessage";
        public static final String TEXT_PERSON_NAME = "textPersonName";
        public static final String TEXT_POSTAL_ADDRESS = "textPostalAddress";
        public static final String TEXT_PASSWORD = "textPassword";
        public static final String TEXT_VISIBLE_PASSWORD = "textVisiblePassword";
        public static final String TEXT_WEB_EDIT_TEXT = "textWebEditText";
        public static final String TEXT_FILTER = "textFilter";
        public static final String TEXT_PHONETIC = "textPhonetic";
        public static final String TEXT_WEB_EMAIL_ADDRESS = "textWebEmailAddress";
        public static final String TEXT_WEB_PASSWORD = "textWebPassword";
        public static final String NUMBER = "number";
        public static final String NUMBER_SIGNED = "numberSigned";
        public static final String NUMBER_DECIMAL = "numberDecimal";
        public static final String NUMBER_PASSWORD = "numberPassword";
        public static final String PHONE = "phone";
        public static final String DATETIME = "datetime";
        public static final String DATE = "date";
        public static final String TIME = "time";
    }

    public static final class PorterDuffMode {
        public static final String CLEAR = "clear";
        public static final String SRC = "src";
        public static final String DST = "dst";
        public static final String SRC_OVER = "src_over";
        public static final String DST_OVER = "dst_over";
        public static final String SRC_IN = "src_in";
        public static final String DST_IN = "dst_in";
        public static final String SRC_OUT = "src_out";
        public static final String DST_OUT = "dst_out";
        public static final String SRC_ATOP = "src_atop";
        public static final String DST_ATOP = "dst_atop";
        public static final String XOR = "xor";
        public static final String DARKEN = "darken";
        public static final String LIGHTEN = "lighten";
        public static final String MULTIPLY = "multiply";
        public static final String SCREEN = "screen";
        public static final String ADD = "add";
        public static final String OVERLAY = "overlay";
    }

    public static final class ScrollFlag {
        public static final String SCROLL = "scroll";
        public static final String EXIT_UNTIL_COLLAPSED = "exitUntilCollapsed";
        public static final String ENTER_ALWAYS = "enterAlways";
        public static final String ENTER_ALWAYS_COLLAPSED = "enterAlwaysCollapsed";
        public static final String SNAP = "snap";
    }
}
