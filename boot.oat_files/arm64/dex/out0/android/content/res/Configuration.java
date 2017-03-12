package android.content.res;

import android.hardware.Camera.Parameters;
import android.hardware.usb.UsbManager;
import android.media.MediaFile;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class Configuration implements Parcelable, Comparable<Configuration> {
    public static final int ARRANGE_CASCADE = 4;
    public static final int ARRANGE_DEFINED = 2;
    public static final int ARRANGE_FULL = 1024;
    public static final int ARRANGE_MINIMIZED_ALL = 256;
    public static final int ARRANGE_MODE_MASK = 1148;
    public static final int ARRANGE_RESTORED_ALL = 512;
    public static final int ARRANGE_SPLITED = 8;
    public static final int ARRANGE_SPLITED3L = 16;
    public static final int ARRANGE_SPLITED3R = 32;
    public static final int ARRANGE_SPLITED4 = 64;
    public static final int ARRANGE_SPLITED_MODE = 112;
    public static final int ARRANGE_SWITCH = 128;
    public static final int ARRANGE_TOGGLE_MASK = 2048;
    public static final int ARRANGE_UNDEFINED = 1;
    public static final Creator<Configuration> CREATOR = new Creator<Configuration>() {
        public Configuration createFromParcel(Parcel source) {
            return new Configuration(source);
        }

        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };
    private static final boolean DEBUG = false;
    public static final int DENSITY_DPI_ANY = 65534;
    public static final int DENSITY_DPI_NONE = 65535;
    public static final int DENSITY_DPI_UNDEFINED = 0;
    public static final Configuration EMPTY = new Configuration();
    public static final int HARDKEYBOARDHIDDEN_NO = 1;
    public static final int HARDKEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int HARDKEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARDHIDDEN_NO = 1;
    public static final int KEYBOARDHIDDEN_SOFT = 3;
    public static final int KEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int KEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARD_12KEY = 3;
    public static final int KEYBOARD_NOKEYS = 1;
    public static final int KEYBOARD_QWERTY = 2;
    public static final int KEYBOARD_UNDEFINED = 0;
    public static final int MNC_ZERO = 65535;
    public static final int MOBILEKEYBOARD_COVERED_NO = 0;
    public static final int MOBILEKEYBOARD_COVERED_UNDEFINED = -1;
    public static final int MOBILEKEYBOARD_COVERED_YES = 1;
    private static final String MonoTAG = "Monotype";
    public static final int NATIVE_CONFIG_DENSITY = 256;
    public static final int NATIVE_CONFIG_DISPLAYID = 131072;
    public static final int NATIVE_CONFIG_FLIPFONT = 32768;
    public static final int NATIVE_CONFIG_KEYBOARD = 16;
    public static final int NATIVE_CONFIG_KEYBOARD_HIDDEN = 32;
    public static final int NATIVE_CONFIG_LAYOUTDIR = 16384;
    public static final int NATIVE_CONFIG_LOCALE = 4;
    public static final int NATIVE_CONFIG_MCC = 1;
    public static final int NATIVE_CONFIG_MNC = 2;
    public static final int NATIVE_CONFIG_NAVIGATION = 64;
    public static final int NATIVE_CONFIG_NONE = 0;
    public static final int NATIVE_CONFIG_ORIENTATION = 128;
    public static final int NATIVE_CONFIG_SCREEN_LAYOUT = 2048;
    public static final int NATIVE_CONFIG_SCREEN_SIZE = 512;
    public static final int NATIVE_CONFIG_SHOW_BUTTON_BACKGROUND = 262144;
    public static final int NATIVE_CONFIG_SMALLEST_SCREEN_SIZE = 8192;
    public static final int NATIVE_CONFIG_THEMESEQ = 65536;
    public static final int NATIVE_CONFIG_TOUCHSCREEN = 8;
    public static final int NATIVE_CONFIG_UI_MODE = 4096;
    public static final int NATIVE_CONFIG_VERSION = 1024;
    public static final int NAVIGATIONHIDDEN_NO = 1;
    public static final int NAVIGATIONHIDDEN_UNDEFINED = 0;
    public static final int NAVIGATIONHIDDEN_YES = 2;
    public static final int NAVIGATION_DPAD = 2;
    public static final int NAVIGATION_NONAV = 1;
    public static final int NAVIGATION_TRACKBALL = 3;
    public static final int NAVIGATION_UNDEFINED = 0;
    public static final int NAVIGATION_WHEEL = 4;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_PORTRAIT = 1;
    @Deprecated
    public static final int ORIENTATION_SQUARE = 3;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int SCREENLAYOUT_COMPAT_NEEDED = 268435456;
    public static final int SCREENLAYOUT_LAYOUTDIR_LTR = 64;
    public static final int SCREENLAYOUT_LAYOUTDIR_MASK = 192;
    public static final int SCREENLAYOUT_LAYOUTDIR_RTL = 128;
    public static final int SCREENLAYOUT_LAYOUTDIR_SHIFT = 6;
    public static final int SCREENLAYOUT_LAYOUTDIR_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_MASK = 48;
    public static final int SCREENLAYOUT_LONG_NO = 16;
    public static final int SCREENLAYOUT_LONG_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_YES = 32;
    public static final int SCREENLAYOUT_ROUND_MASK = 768;
    public static final int SCREENLAYOUT_ROUND_NO = 256;
    public static final int SCREENLAYOUT_ROUND_SHIFT = 8;
    public static final int SCREENLAYOUT_ROUND_UNDEFINED = 0;
    public static final int SCREENLAYOUT_ROUND_YES = 512;
    public static final int SCREENLAYOUT_SIZE_LARGE = 3;
    public static final int SCREENLAYOUT_SIZE_MASK = 15;
    public static final int SCREENLAYOUT_SIZE_NORMAL = 2;
    public static final int SCREENLAYOUT_SIZE_SMALL = 1;
    public static final int SCREENLAYOUT_SIZE_UNDEFINED = 0;
    public static final int SCREENLAYOUT_SIZE_XLARGE = 4;
    public static final int SCREENLAYOUT_UNDEFINED = 0;
    public static final int SCREEN_HEIGHT_DP_UNDEFINED = 0;
    public static final int SCREEN_WIDTH_DP_UNDEFINED = 0;
    public static final int SHOWBUTTONBACKGROUND_UNDEFINED = -1;
    public static final int SMALLEST_SCREEN_WIDTH_DP_UNDEFINED = 0;
    public static final int TOUCHSCREEN_FINGER = 3;
    public static final int TOUCHSCREEN_NOTOUCH = 1;
    @Deprecated
    public static final int TOUCHSCREEN_STYLUS = 2;
    public static final int TOUCHSCREEN_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_MASK = 48;
    public static final int UI_MODE_NIGHT_NO = 16;
    public static final int UI_MODE_NIGHT_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_YES = 32;
    public static final int UI_MODE_TYPE_APPLIANCE = 5;
    public static final int UI_MODE_TYPE_CAR = 3;
    public static final int UI_MODE_TYPE_DESK = 2;
    public static final int UI_MODE_TYPE_MASK = 15;
    public static final int UI_MODE_TYPE_NORMAL = 1;
    public static final int UI_MODE_TYPE_TELEVISION = 4;
    public static final int UI_MODE_TYPE_UNDEFINED = 0;
    public static final int UI_MODE_TYPE_WATCH = 6;
    private static final String XML_ATTR_DENSITY = "density";
    private static final String XML_ATTR_FONT_SCALE = "fs";
    private static final String XML_ATTR_HARD_KEYBOARD_HIDDEN = "hardKeyHid";
    private static final String XML_ATTR_KEYBOARD = "key";
    private static final String XML_ATTR_KEYBOARD_HIDDEN = "keyHid";
    private static final String XML_ATTR_LOCALE = "locale";
    private static final String XML_ATTR_MCC = "mcc";
    private static final String XML_ATTR_MNC = "mnc";
    private static final String XML_ATTR_NAVIGATION = "nav";
    private static final String XML_ATTR_NAVIGATION_HIDDEN = "navHid";
    private static final String XML_ATTR_ORIENTATION = "ori";
    private static final String XML_ATTR_SCREEN_HEIGHT = "height";
    private static final String XML_ATTR_SCREEN_LAYOUT = "scrLay";
    private static final String XML_ATTR_SCREEN_WIDTH = "width";
    private static final String XML_ATTR_SMALLEST_WIDTH = "sw";
    private static final String XML_ATTR_TOUCHSCREEN = "touch";
    private static final String XML_ATTR_UI_MODE = "ui";
    private static final boolean isElasticEnabled = true;
    public int FlipFont;
    public int arrange;
    public int compatScreenHeightDp;
    public int compatScreenWidthDp;
    public int compatSmallestScreenWidthDp;
    public int densityDpi;
    public int displayId;
    public float fontScale;
    public int hardKeyboardHidden;
    public int keyboard;
    public int keyboardHidden;
    public Locale locale;
    public int mcc;
    public int mnc;
    public int mobileKeyboardCovered;
    public int navigation;
    public int navigationHidden;
    public int orientation;
    public int screenHeightDp;
    public int screenLayout;
    public int screenWidthDp;
    public int seq;
    public int showButtonBackground;
    public int smallestScreenWidthDp;
    public int themeSeq;
    public int touchscreen;
    public int uiMode;
    public boolean userSetLocale;

    public static int resetScreenLayout(int curLayout) {
        return (-268435520 & curLayout) | 36;
    }

    public static int reduceScreenLayout(int curLayout, int longSizeDp, int shortSizeDp) {
        int screenLayoutSize;
        boolean screenLayoutLong;
        boolean screenLayoutCompatNeeded;
        if (longSizeDp < 470) {
            screenLayoutSize = 1;
            screenLayoutLong = false;
            screenLayoutCompatNeeded = false;
        } else {
            if (longSizeDp >= 960 && shortSizeDp >= 720) {
                screenLayoutSize = 4;
            } else if (longSizeDp < 640 || shortSizeDp < 480) {
                screenLayoutSize = 2;
            } else {
                screenLayoutSize = 3;
            }
            if (shortSizeDp > MediaFile.FILE_TYPE_ODF_LGU || longSizeDp > 570) {
                screenLayoutCompatNeeded = true;
            } else {
                screenLayoutCompatNeeded = false;
            }
            if ((longSizeDp * 3) / 5 >= shortSizeDp - 1) {
                screenLayoutLong = true;
            } else {
                screenLayoutLong = false;
            }
        }
        if (!screenLayoutLong) {
            curLayout = (curLayout & -49) | 16;
        }
        if (screenLayoutCompatNeeded) {
            curLayout |= 268435456;
        }
        if (screenLayoutSize < (curLayout & 15)) {
            return (curLayout & -16) | screenLayoutSize;
        }
        return curLayout;
    }

    public boolean isLayoutSizeAtLeast(int size) {
        int cur = this.screenLayout & 15;
        if (cur != 0 && cur >= size) {
            return true;
        }
        return false;
    }

    public Configuration() {
        this.themeSeq = 0;
        setToDefaults();
    }

    public Configuration(Configuration o) {
        this.themeSeq = 0;
        setTo(o);
    }

    public void setTo(Configuration o) {
        this.fontScale = o.fontScale;
        this.themeSeq = o.themeSeq;
        this.mcc = o.mcc;
        this.mnc = o.mnc;
        if (o.locale != null) {
            this.locale = (Locale) o.locale.clone();
        }
        this.userSetLocale = o.userSetLocale;
        this.touchscreen = o.touchscreen;
        this.keyboard = o.keyboard;
        this.keyboardHidden = o.keyboardHidden;
        this.hardKeyboardHidden = o.hardKeyboardHidden;
        this.navigation = o.navigation;
        this.navigationHidden = o.navigationHidden;
        this.orientation = o.orientation;
        this.screenLayout = o.screenLayout;
        this.uiMode = o.uiMode;
        this.screenWidthDp = o.screenWidthDp;
        this.screenHeightDp = o.screenHeightDp;
        this.smallestScreenWidthDp = o.smallestScreenWidthDp;
        this.densityDpi = o.densityDpi;
        this.compatScreenWidthDp = o.compatScreenWidthDp;
        this.compatScreenHeightDp = o.compatScreenHeightDp;
        this.compatSmallestScreenWidthDp = o.compatSmallestScreenWidthDp;
        this.seq = o.seq;
        this.FlipFont = o.FlipFont;
        this.mobileKeyboardCovered = o.mobileKeyboardCovered;
        this.showButtonBackground = o.showButtonBackground;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{");
        sb.append(this.FlipFont);
        sb.append(" ");
        sb.append(this.fontScale);
        sb.append(" ");
        sb.append("themeSeq = ");
        sb.append(this.themeSeq);
        sb.append(" ");
        sb.append("showBtnBg = ");
        sb.append(this.showButtonBackground);
        sb.append(" ");
        if (this.mcc != 0) {
            sb.append(this.mcc);
            sb.append(XML_ATTR_MCC);
        } else {
            sb.append("?mcc");
        }
        if (this.mnc != 0) {
            sb.append(this.mnc);
            sb.append(XML_ATTR_MNC);
        } else {
            sb.append("?mnc");
        }
        if (this.locale != null) {
            sb.append(" ");
            sb.append(this.locale);
        } else {
            sb.append(" ?locale");
        }
        int layoutDir = this.screenLayout & 192;
        switch (layoutDir) {
            case 0:
                sb.append(" ?layoutDir");
                break;
            case 64:
                sb.append(" ldltr");
                break;
            case 128:
                sb.append(" ldrtl");
                break;
            default:
                sb.append(" layoutDir=");
                sb.append(layoutDir >> 6);
                break;
        }
        if (this.smallestScreenWidthDp != 0) {
            sb.append(" sw");
            sb.append(this.smallestScreenWidthDp);
            sb.append("dp");
        } else {
            sb.append(" ?swdp");
        }
        if (this.screenWidthDp != 0) {
            sb.append(" w");
            sb.append(this.screenWidthDp);
            sb.append("dp");
        } else {
            sb.append(" ?wdp");
        }
        if (this.screenHeightDp != 0) {
            sb.append(" h");
            sb.append(this.screenHeightDp);
            sb.append("dp");
        } else {
            sb.append(" ?hdp");
        }
        if (this.densityDpi != 0) {
            sb.append(" ");
            sb.append(this.densityDpi);
            sb.append("dpi");
        } else {
            sb.append(" ?density");
        }
        switch (this.screenLayout & 15) {
            case 0:
                sb.append(" ?lsize");
                break;
            case 1:
                sb.append(" smll");
                break;
            case 2:
                sb.append(" nrml");
                break;
            case 3:
                sb.append(" lrg");
                break;
            case 4:
                sb.append(" xlrg");
                break;
            default:
                sb.append(" layoutSize=");
                sb.append(this.screenLayout & 15);
                break;
        }
        switch (this.screenLayout & 48) {
            case 0:
                sb.append(" ?long");
                break;
            case 16:
                break;
            case 32:
                sb.append(" long");
                break;
            default:
                sb.append(" layoutLong=");
                sb.append(this.screenLayout & 48);
                break;
        }
        switch (this.orientation) {
            case 0:
                sb.append(" ?orien");
                break;
            case 1:
                sb.append(" port");
                break;
            case 2:
                sb.append(" land");
                break;
            default:
                sb.append(" orien=");
                sb.append(this.orientation);
                break;
        }
        switch (this.uiMode & 15) {
            case 0:
                sb.append(" ?uimode");
                break;
            case 1:
                break;
            case 2:
                sb.append(" desk");
                break;
            case 3:
                sb.append(" car");
                break;
            case 4:
                sb.append(" television");
                break;
            case 5:
                sb.append(" appliance");
                break;
            case 6:
                sb.append(" watch");
                break;
            default:
                sb.append(" uimode=");
                sb.append(this.uiMode & 15);
                break;
        }
        switch (this.uiMode & 48) {
            case 0:
                sb.append(" ?night");
                break;
            case 16:
                break;
            case 32:
                sb.append(" night");
                break;
            default:
                sb.append(" night=");
                sb.append(this.uiMode & 48);
                break;
        }
        switch (this.touchscreen) {
            case 0:
                sb.append(" ?touch");
                break;
            case 1:
                sb.append(" -touch");
                break;
            case 2:
                sb.append(" stylus");
                break;
            case 3:
                sb.append(" finger");
                break;
            default:
                sb.append(" touch=");
                sb.append(this.touchscreen);
                break;
        }
        switch (this.keyboard) {
            case 0:
                sb.append(" ?keyb");
                break;
            case 1:
                sb.append(" -keyb");
                break;
            case 2:
                sb.append(" qwerty");
                break;
            case 3:
                sb.append(" 12key");
                break;
            default:
                sb.append(" keys=");
                sb.append(this.keyboard);
                break;
        }
        switch (this.keyboardHidden) {
            case 0:
                sb.append("/?");
                break;
            case 1:
                sb.append("/v");
                break;
            case 2:
                sb.append("/h");
                break;
            case 3:
                sb.append("/s");
                break;
            default:
                sb.append("/");
                sb.append(this.keyboardHidden);
                break;
        }
        switch (this.hardKeyboardHidden) {
            case 0:
                sb.append("/?");
                break;
            case 1:
                sb.append("/v");
                break;
            case 2:
                sb.append("/h");
                break;
            default:
                sb.append("/");
                sb.append(this.hardKeyboardHidden);
                break;
        }
        switch (this.navigation) {
            case 0:
                sb.append(" ?nav");
                break;
            case 1:
                sb.append(" -nav");
                break;
            case 2:
                sb.append(" dpad");
                break;
            case 3:
                sb.append(" tball");
                break;
            case 4:
                sb.append(" wheel");
                break;
            default:
                sb.append(" nav=");
                sb.append(this.navigation);
                break;
        }
        switch (this.navigationHidden) {
            case 0:
                sb.append("/?");
                break;
            case 1:
                sb.append("/v");
                break;
            case 2:
                sb.append("/h");
                break;
            default:
                sb.append("/");
                sb.append(this.navigationHidden);
                break;
        }
        switch (this.mobileKeyboardCovered) {
            case -1:
                sb.append(" mkbd/?");
                break;
            case 0:
                sb.append(" mkbd/h");
                break;
            case 1:
                sb.append(" mkbd/v");
                break;
            default:
                sb.append("/");
                sb.append(this.mobileKeyboardCovered);
                break;
        }
        if (this.seq != 0) {
            sb.append(" s.");
            sb.append(this.seq);
        }
        sb.append('}');
        return sb.toString();
    }

    public void setToDefaults() {
        this.fontScale = Float.parseFloat(WifiEnterpriseConfig.ENGINE_ENABLE);
        this.mnc = 0;
        this.mcc = 0;
        this.locale = null;
        this.userSetLocale = false;
        this.touchscreen = 0;
        this.keyboard = 0;
        this.keyboardHidden = 0;
        this.hardKeyboardHidden = 0;
        this.navigation = 0;
        this.navigationHidden = 0;
        this.orientation = 0;
        this.screenLayout = 0;
        this.uiMode = 0;
        this.compatScreenWidthDp = 0;
        this.screenWidthDp = 0;
        this.compatScreenHeightDp = 0;
        this.screenHeightDp = 0;
        this.compatSmallestScreenWidthDp = 0;
        this.smallestScreenWidthDp = 0;
        this.densityDpi = 0;
        this.seq = 0;
        this.FlipFont = 0;
        this.themeSeq = 0;
        this.mobileKeyboardCovered = -1;
        this.showButtonBackground = -1;
    }

    @Deprecated
    public void makeDefault() {
        setToDefaults();
    }

    public boolean isStackOverrideConfig() {
        int diff = EMPTY.diff(this);
        if (diff == 0 || (((diff & -129) & -1025) & -2049) != 0) {
            return false;
        }
        return true;
    }

    public int updateFromStackOverrideConfig(Configuration overrideConfig) {
        int changed = 0;
        if (overrideConfig == null) {
            return 0;
        }
        if (!(overrideConfig.orientation == 0 || this.orientation == overrideConfig.orientation)) {
            changed = 0 | 128;
            this.orientation = overrideConfig.orientation;
        }
        if (!(overrideConfig.screenWidthDp == 0 || this.screenWidthDp == overrideConfig.screenWidthDp)) {
            changed |= 1024;
            this.screenWidthDp = overrideConfig.screenWidthDp;
        }
        if (!(overrideConfig.screenHeightDp == 0 || this.screenHeightDp == overrideConfig.screenHeightDp)) {
            changed |= 1024;
            this.screenHeightDp = overrideConfig.screenHeightDp;
        }
        if (overrideConfig.smallestScreenWidthDp == 0 || this.smallestScreenWidthDp == overrideConfig.smallestScreenWidthDp) {
            return changed;
        }
        changed |= 2048;
        this.smallestScreenWidthDp = overrideConfig.smallestScreenWidthDp;
        return changed;
    }

    public int updateFrom(Configuration delta) {
        int changed = 0;
        if (delta.fontScale > 0.0f && this.fontScale != delta.fontScale) {
            changed = 0 | 1073741824;
            this.fontScale = delta.fontScale;
        }
        if (delta.themeSeq > 0 && this.themeSeq != delta.themeSeq) {
            changed |= 65536;
            this.themeSeq = delta.themeSeq;
        }
        if (!(delta.mcc == 0 || this.mcc == delta.mcc)) {
            changed |= 1;
            this.mcc = delta.mcc;
        }
        if (!(delta.mnc == 0 || this.mnc == delta.mnc)) {
            changed |= 2;
            this.mnc = delta.mnc;
        }
        if (delta.locale != null && (this.locale == null || !this.locale.equals(delta.locale))) {
            changed |= 4;
            this.locale = delta.locale != null ? (Locale) delta.locale.clone() : null;
            changed |= 8192;
            setLayoutDirection(this.locale);
        }
        int deltaScreenLayoutDir = delta.screenLayout & 192;
        if (!(deltaScreenLayoutDir == 0 || deltaScreenLayoutDir == (this.screenLayout & 192))) {
            this.screenLayout = (this.screenLayout & -193) | deltaScreenLayoutDir;
            changed |= 8192;
        }
        if (delta.userSetLocale && !(this.userSetLocale && (changed & 4) == 0)) {
            changed |= 4;
            this.userSetLocale = true;
        }
        if (!(delta.touchscreen == 0 || this.touchscreen == delta.touchscreen)) {
            changed |= 8;
            this.touchscreen = delta.touchscreen;
        }
        if (!(delta.keyboard == 0 || this.keyboard == delta.keyboard)) {
            changed |= 16;
            this.keyboard = delta.keyboard;
        }
        if (!(delta.keyboardHidden == 0 || this.keyboardHidden == delta.keyboardHidden)) {
            changed |= 32;
            this.keyboardHidden = delta.keyboardHidden;
        }
        if (!(delta.hardKeyboardHidden == 0 || this.hardKeyboardHidden == delta.hardKeyboardHidden)) {
            changed |= 32;
            this.hardKeyboardHidden = delta.hardKeyboardHidden;
        }
        if (!(delta.navigation == 0 || this.navigation == delta.navigation)) {
            changed |= 64;
            this.navigation = delta.navigation;
        }
        if (!(delta.navigationHidden == 0 || this.navigationHidden == delta.navigationHidden)) {
            changed |= 32;
            this.navigationHidden = delta.navigationHidden;
        }
        if (!(delta.orientation == 0 || this.orientation == delta.orientation)) {
            changed |= 128;
            this.orientation = delta.orientation;
        }
        if (!(getScreenLayoutNoDirection(delta.screenLayout) == 0 || getScreenLayoutNoDirection(this.screenLayout) == getScreenLayoutNoDirection(delta.screenLayout))) {
            changed |= 256;
            if ((delta.screenLayout & 192) == 0) {
                this.screenLayout = (this.screenLayout & 192) | delta.screenLayout;
            } else {
                this.screenLayout = delta.screenLayout;
            }
        }
        if (!(delta.uiMode == 0 || this.uiMode == delta.uiMode)) {
            changed |= 512;
            if ((delta.uiMode & 15) != 0) {
                this.uiMode = (this.uiMode & -16) | (delta.uiMode & 15);
            }
            if ((delta.uiMode & 48) != 0) {
                this.uiMode = (this.uiMode & -49) | (delta.uiMode & 48);
            }
        }
        if (!(delta.screenWidthDp == 0 || this.screenWidthDp == delta.screenWidthDp)) {
            changed |= 1024;
            this.screenWidthDp = delta.screenWidthDp;
        }
        if (!(delta.screenHeightDp == 0 || this.screenHeightDp == delta.screenHeightDp)) {
            changed |= 1024;
            this.screenHeightDp = delta.screenHeightDp;
        }
        if (!(delta.smallestScreenWidthDp == 0 || this.smallestScreenWidthDp == delta.smallestScreenWidthDp)) {
            changed |= 2048;
            this.smallestScreenWidthDp = delta.smallestScreenWidthDp;
        }
        if (!(delta.densityDpi == 0 || this.densityDpi == delta.densityDpi)) {
            changed |= 4096;
            this.densityDpi = delta.densityDpi;
        }
        if (delta.compatScreenWidthDp != 0) {
            this.compatScreenWidthDp = delta.compatScreenWidthDp;
        }
        if (delta.compatScreenHeightDp != 0) {
            this.compatScreenHeightDp = delta.compatScreenHeightDp;
        }
        if (delta.compatSmallestScreenWidthDp != 0) {
            this.compatSmallestScreenWidthDp = delta.compatSmallestScreenWidthDp;
        }
        if (delta.seq != 0) {
            this.seq = delta.seq;
        }
        if (delta.FlipFont > 0 && this.FlipFont != delta.FlipFont) {
            changed |= 536870912;
            this.FlipFont = delta.FlipFont;
        }
        if (!(delta.mobileKeyboardCovered == -1 || this.mobileKeyboardCovered == delta.mobileKeyboardCovered)) {
            this.mobileKeyboardCovered = delta.mobileKeyboardCovered;
        }
        if (delta.showButtonBackground == -1 || this.showButtonBackground == delta.showButtonBackground) {
            return changed;
        }
        changed |= 2097152;
        this.showButtonBackground = delta.showButtonBackground;
        return changed;
    }

    public int diff(Configuration delta) {
        return diff(delta, true);
    }

    public int diff(Configuration delta, boolean includeDisplayId) {
        int changed = 0;
        if (delta.fontScale > 0.0f && this.fontScale != delta.fontScale) {
            changed = 0 | 1073741824;
        }
        if (delta.themeSeq > 0 && this.themeSeq != delta.themeSeq) {
            changed |= 65536;
        }
        if (!(delta.mcc == 0 || this.mcc == delta.mcc)) {
            changed |= 1;
        }
        if (!(delta.mnc == 0 || this.mnc == delta.mnc)) {
            changed |= 2;
        }
        if (delta.locale != null && (this.locale == null || !this.locale.equals(delta.locale))) {
            changed = (changed | 4) | 8192;
        }
        int deltaScreenLayoutDir = delta.screenLayout & 192;
        if (!(deltaScreenLayoutDir == 0 || deltaScreenLayoutDir == (this.screenLayout & 192))) {
            changed |= 8192;
        }
        if (!(delta.touchscreen == 0 || this.touchscreen == delta.touchscreen)) {
            changed |= 8;
        }
        if (!(delta.keyboard == 0 || this.keyboard == delta.keyboard)) {
            changed |= 16;
        }
        if (!(delta.keyboardHidden == 0 || this.keyboardHidden == delta.keyboardHidden)) {
            changed |= 32;
        }
        if (!(delta.hardKeyboardHidden == 0 || this.hardKeyboardHidden == delta.hardKeyboardHidden)) {
            changed |= 32;
        }
        if (!(delta.navigation == 0 || this.navigation == delta.navigation)) {
            changed |= 64;
        }
        if (!(delta.navigationHidden == 0 || this.navigationHidden == delta.navigationHidden)) {
            changed |= 32;
        }
        if (!(delta.orientation == 0 || this.orientation == delta.orientation)) {
            changed |= 128;
        }
        if (!(getScreenLayoutNoDirection(delta.screenLayout) == 0 || getScreenLayoutNoDirection(this.screenLayout) == getScreenLayoutNoDirection(delta.screenLayout) || this.mobileKeyboardCovered != delta.mobileKeyboardCovered)) {
            changed |= 256;
        }
        if (!(delta.uiMode == 0 || this.uiMode == delta.uiMode)) {
            changed |= 512;
        }
        if (!(delta.screenWidthDp == 0 || this.screenWidthDp == delta.screenWidthDp)) {
            changed |= 1024;
        }
        if (!(delta.screenHeightDp == 0 || this.screenHeightDp == delta.screenHeightDp)) {
            changed |= 1024;
        }
        if (!(delta.smallestScreenWidthDp == 0 || this.smallestScreenWidthDp == delta.smallestScreenWidthDp)) {
            changed |= 2048;
        }
        if (!(delta.densityDpi == 0 || this.densityDpi == delta.densityDpi)) {
            changed |= 4096;
        }
        if (delta.FlipFont > 0 && this.FlipFont != delta.FlipFont) {
            changed |= 536870912;
        }
        if (delta.showButtonBackground == -1 || this.showButtonBackground == delta.showButtonBackground) {
            return changed;
        }
        return changed | 2097152;
    }

    public static boolean needNewResources(int configChanges, int interestingChanges) {
        return ((((1073741824 | interestingChanges) | 65536) | 536870912) & configChanges) != 0;
    }

    public static boolean needToUpdateOverlays(int configChanges) {
        return (65536 & configChanges) != 0;
    }

    public boolean isOtherSeqNewer(Configuration other) {
        if (other == null) {
            return false;
        }
        if (other.seq == 0) {
            return true;
        }
        if (this.seq == 0) {
            return true;
        }
        int diff = other.seq - this.seq;
        if (diff > 65536) {
            return false;
        }
        int themeDiff = other.themeSeq - this.themeSeq;
        if (themeDiff > 65536) {
            return false;
        }
        if (diff > 0 || themeDiff > 0) {
            return true;
        }
        return false;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.fontScale);
        dest.writeInt(this.themeSeq);
        dest.writeInt(this.mcc);
        dest.writeInt(this.mnc);
        if (this.locale == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeString(this.locale.getLanguage());
            dest.writeString(this.locale.getCountry());
            dest.writeString(this.locale.getVariant());
        }
        if (this.userSetLocale) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.touchscreen);
        dest.writeInt(this.keyboard);
        dest.writeInt(this.keyboardHidden);
        dest.writeInt(this.hardKeyboardHidden);
        dest.writeInt(this.navigation);
        dest.writeInt(this.navigationHidden);
        dest.writeInt(this.orientation);
        dest.writeInt(this.screenLayout);
        dest.writeInt(this.uiMode);
        dest.writeInt(this.screenWidthDp);
        dest.writeInt(this.screenHeightDp);
        dest.writeInt(this.smallestScreenWidthDp);
        dest.writeInt(this.densityDpi);
        dest.writeInt(this.compatScreenWidthDp);
        dest.writeInt(this.compatScreenHeightDp);
        dest.writeInt(this.compatSmallestScreenWidthDp);
        dest.writeInt(this.seq);
        dest.writeInt(this.FlipFont);
        dest.writeInt(this.mobileKeyboardCovered);
        dest.writeInt(this.showButtonBackground);
    }

    public void readFromParcel(Parcel source) {
        boolean z = true;
        this.fontScale = source.readFloat();
        this.themeSeq = source.readInt();
        this.mcc = source.readInt();
        this.mnc = source.readInt();
        if (source.readInt() != 0) {
            this.locale = new Locale(source.readString(), source.readString(), source.readString());
        }
        if (source.readInt() != 1) {
            z = false;
        }
        this.userSetLocale = z;
        this.touchscreen = source.readInt();
        this.keyboard = source.readInt();
        this.keyboardHidden = source.readInt();
        this.hardKeyboardHidden = source.readInt();
        this.navigation = source.readInt();
        this.navigationHidden = source.readInt();
        this.orientation = source.readInt();
        this.screenLayout = source.readInt();
        this.uiMode = source.readInt();
        this.screenWidthDp = source.readInt();
        this.screenHeightDp = source.readInt();
        this.smallestScreenWidthDp = source.readInt();
        this.densityDpi = source.readInt();
        this.compatScreenWidthDp = source.readInt();
        this.compatScreenHeightDp = source.readInt();
        this.compatSmallestScreenWidthDp = source.readInt();
        this.seq = source.readInt();
        this.FlipFont = source.readInt();
        this.mobileKeyboardCovered = source.readInt();
        this.showButtonBackground = source.readInt();
    }

    private Configuration(Parcel source) {
        this.themeSeq = 0;
        readFromParcel(source);
    }

    public int compareTo(Configuration that) {
        float a = this.fontScale;
        float b = that.fontScale;
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        int x = this.themeSeq;
        int y = that.themeSeq;
        if (x < y) {
            return -1;
        }
        if (x > y) {
            return 1;
        }
        int n = this.mcc - that.mcc;
        if (n != 0) {
            return n;
        }
        n = this.mnc - that.mnc;
        if (n != 0) {
            return n;
        }
        if (this.locale == null) {
            if (that.locale != null) {
                return 1;
            }
        } else if (that.locale == null) {
            return -1;
        } else {
            n = this.locale.getLanguage().compareTo(that.locale.getLanguage());
            if (n != 0) {
                return n;
            }
            n = this.locale.getCountry().compareTo(that.locale.getCountry());
            if (n != 0) {
                return n;
            }
            n = this.locale.getVariant().compareTo(that.locale.getVariant());
            if (n != 0) {
                return n;
            }
        }
        n = this.touchscreen - that.touchscreen;
        if (n != 0) {
            return n;
        }
        n = this.keyboard - that.keyboard;
        if (n != 0) {
            return n;
        }
        n = this.keyboardHidden - that.keyboardHidden;
        if (n != 0) {
            return n;
        }
        n = this.hardKeyboardHidden - that.hardKeyboardHidden;
        if (n != 0) {
            return n;
        }
        n = this.navigation - that.navigation;
        if (n != 0) {
            return n;
        }
        n = this.navigationHidden - that.navigationHidden;
        if (n != 0) {
            return n;
        }
        n = this.orientation - that.orientation;
        if (n != 0) {
            return n;
        }
        n = this.screenLayout - that.screenLayout;
        if (n != 0) {
            return n;
        }
        n = this.uiMode - that.uiMode;
        if (n != 0) {
            return n;
        }
        n = this.screenWidthDp - that.screenWidthDp;
        if (n != 0) {
            return n;
        }
        n = this.screenHeightDp - that.screenHeightDp;
        if (n != 0) {
            return n;
        }
        n = this.smallestScreenWidthDp - that.smallestScreenWidthDp;
        if (n != 0) {
            return n;
        }
        n = this.densityDpi - that.densityDpi;
        n = this.showButtonBackground - that.showButtonBackground;
        if (n != 0) {
            return n;
        }
        a = (float) this.FlipFont;
        b = (float) that.FlipFont;
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return n;
    }

    public boolean equals(Configuration that) {
        if (that == null) {
            return false;
        }
        if (that == this || compareTo(that) == 0) {
            return true;
        }
        return false;
    }

    public boolean equals(Object that) {
        try {
            return equals((Configuration) that);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return ((((((((((((((((((((((((((((((((((((((Float.floatToIntBits(this.fontScale) + 527) * 31) + this.themeSeq) * 31) + this.mcc) * 31) + this.mnc) * 31) + (this.locale != null ? this.locale.hashCode() : 0)) * 31) + this.touchscreen) * 31) + this.keyboard) * 31) + this.keyboardHidden) * 31) + this.hardKeyboardHidden) * 31) + this.navigation) * 31) + this.navigationHidden) * 31) + this.orientation) * 31) + this.screenLayout) * 31) + this.uiMode) * 31) + this.screenWidthDp) * 31) + this.screenHeightDp) * 31) + this.smallestScreenWidthDp) * 31) + this.densityDpi) * 31) + this.FlipFont) * 31) + this.showButtonBackground;
    }

    public void setLocale(Locale loc) {
        this.locale = loc;
        setLayoutDirection(this.locale);
    }

    public int getLayoutDirection() {
        return (this.screenLayout & 192) == 128 ? 1 : 0;
    }

    public void setLayoutDirection(Locale locale) {
        this.screenLayout = (this.screenLayout & -193) | ((TextUtils.getLayoutDirectionFromLocale(locale) + 1) << 6);
    }

    private static int getScreenLayoutNoDirection(int screenLayout) {
        return screenLayout & -193;
    }

    public boolean isScreenRound() {
        return (this.screenLayout & 768) == 512;
    }

    public static String localeToResourceQualifier(Locale locale) {
        boolean l;
        boolean c;
        boolean s;
        boolean v;
        StringBuilder sb = new StringBuilder();
        if (locale.getLanguage().length() != 0) {
            l = true;
        } else {
            l = false;
        }
        if (locale.getCountry().length() != 0) {
            c = true;
        } else {
            c = false;
        }
        if (locale.getScript().length() != 0) {
            s = true;
        } else {
            s = false;
        }
        if (locale.getVariant().length() != 0) {
            v = true;
        } else {
            v = false;
        }
        if (l) {
            sb.append(locale.getLanguage());
            if (c) {
                sb.append("-r").append(locale.getCountry());
                if (s) {
                    sb.append("-s").append(locale.getScript());
                    if (v) {
                        sb.append("-v").append(locale.getVariant());
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String resourceQualifierString(Configuration config) {
        ArrayList<String> parts = new ArrayList();
        if (config.mcc != 0) {
            parts.add(XML_ATTR_MCC + config.mcc);
            if (config.mnc != 0) {
                parts.add(XML_ATTR_MNC + config.mnc);
            }
        }
        if (!(config.locale == null || config.locale.getLanguage().isEmpty())) {
            parts.add(localeToResourceQualifier(config.locale));
        }
        switch (config.screenLayout & 192) {
            case 64:
                parts.add("ldltr");
                break;
            case 128:
                parts.add("ldrtl");
                break;
        }
        if (config.smallestScreenWidthDp != 0) {
            parts.add(XML_ATTR_SMALLEST_WIDTH + config.smallestScreenWidthDp + "dp");
        }
        if (config.screenWidthDp != 0) {
            parts.add("w" + config.screenWidthDp + "dp");
        }
        if (config.screenHeightDp != 0) {
            parts.add("h" + config.screenHeightDp + "dp");
        }
        switch (config.screenLayout & 15) {
            case 1:
                parts.add("small");
                break;
            case 2:
                parts.add(Parameters.FOCUS_MODE_NORMAL);
                break;
            case 3:
                parts.add("large");
                break;
            case 4:
                parts.add("xlarge");
                break;
        }
        switch (config.screenLayout & 48) {
            case 16:
                parts.add("notlong");
                break;
            case 32:
                parts.add("long");
                break;
        }
        switch (config.screenLayout & 768) {
            case 256:
                parts.add("notround");
                break;
            case 512:
                parts.add("round");
                break;
        }
        switch (config.orientation) {
            case 1:
                parts.add(UsbManager.EXTRA_PORT);
                break;
            case 2:
                parts.add("land");
                break;
        }
        switch (config.uiMode & 15) {
            case 2:
                parts.add("desk");
                break;
            case 3:
                parts.add("car");
                break;
            case 4:
                parts.add("television");
                break;
            case 5:
                parts.add("appliance");
                break;
            case 6:
                parts.add("watch");
                break;
        }
        switch (config.uiMode & 48) {
            case 16:
                parts.add("notnight");
                break;
            case 32:
                parts.add(Parameters.SCENE_MODE_NIGHT);
                break;
        }
        switch (config.densityDpi) {
            case 0:
                break;
            case 120:
                parts.add("ldpi");
                break;
            case 160:
                parts.add("mdpi");
                break;
            case 213:
                parts.add("tvdpi");
                break;
            case 240:
                parts.add("hdpi");
                break;
            case 320:
                parts.add("xhdpi");
                break;
            case 480:
                parts.add("xxhdpi");
                break;
            case 640:
                parts.add("xxxhdpi");
                break;
            case DENSITY_DPI_ANY /*65534*/:
                parts.add("anydpi");
                break;
            case 65535:
                parts.add("nodpi");
                break;
        }
        parts.add(config.densityDpi + "dpi");
        switch (config.touchscreen) {
            case 1:
                parts.add("notouch");
                break;
            case 3:
                parts.add("finger");
                break;
        }
        switch (config.keyboardHidden) {
            case 1:
                parts.add("keysexposed");
                break;
            case 2:
                parts.add("keyshidden");
                break;
            case 3:
                parts.add("keyssoft");
                break;
        }
        switch (config.keyboard) {
            case 1:
                parts.add("nokeys");
                break;
            case 2:
                parts.add("qwerty");
                break;
            case 3:
                parts.add("12key");
                break;
        }
        switch (config.navigationHidden) {
            case 1:
                parts.add("navexposed");
                break;
            case 2:
                parts.add("navhidden");
                break;
        }
        switch (config.navigation) {
            case 1:
                parts.add("nonav");
                break;
            case 2:
                parts.add("dpad");
                break;
            case 3:
                parts.add("trackball");
                break;
            case 4:
                parts.add("wheel");
                break;
        }
        parts.add("v" + VERSION.RESOURCES_SDK_INT);
        return TextUtils.join("-", parts);
    }

    public static Configuration generateDelta(Configuration base, Configuration change) {
        Configuration delta = new Configuration();
        if (base.fontScale != change.fontScale) {
            delta.fontScale = change.fontScale;
        }
        if (base.themeSeq != change.themeSeq) {
            delta.themeSeq = change.themeSeq;
        }
        if (base.showButtonBackground != change.showButtonBackground) {
            delta.showButtonBackground = change.showButtonBackground;
        }
        if (base.mcc != change.mcc) {
            delta.mcc = change.mcc;
        }
        if (base.mnc != change.mnc) {
            delta.mnc = change.mnc;
        }
        if ((base.locale == null && change.locale != null) || !(base.locale == null || base.locale.equals(change.locale))) {
            delta.locale = change.locale;
        }
        if (base.touchscreen != change.touchscreen) {
            delta.touchscreen = change.touchscreen;
        }
        if (base.keyboard != change.keyboard) {
            delta.keyboard = change.keyboard;
        }
        if (base.keyboardHidden != change.keyboardHidden) {
            delta.keyboardHidden = change.keyboardHidden;
        }
        if (base.navigation != change.navigation) {
            delta.navigation = change.navigation;
        }
        if (base.navigationHidden != change.navigationHidden) {
            delta.navigationHidden = change.navigationHidden;
        }
        if (base.orientation != change.orientation) {
            delta.orientation = change.orientation;
        }
        if ((base.screenLayout & 15) != (change.screenLayout & 15)) {
            delta.screenLayout |= change.screenLayout & 15;
        }
        if ((base.screenLayout & 192) != (change.screenLayout & 192)) {
            delta.screenLayout |= change.screenLayout & 192;
        }
        if ((base.screenLayout & 48) != (change.screenLayout & 48)) {
            delta.screenLayout |= change.screenLayout & 48;
        }
        if ((base.screenLayout & 768) != (change.screenLayout & 768)) {
            delta.screenLayout |= change.screenLayout & 768;
        }
        if ((base.uiMode & 15) != (change.uiMode & 15)) {
            delta.uiMode |= change.uiMode & 15;
        }
        if ((base.uiMode & 48) != (change.uiMode & 48)) {
            delta.uiMode |= change.uiMode & 48;
        }
        if (base.screenWidthDp != change.screenWidthDp) {
            delta.screenWidthDp = change.screenWidthDp;
        }
        if (base.screenHeightDp != change.screenHeightDp) {
            delta.screenHeightDp = change.screenHeightDp;
        }
        if (base.smallestScreenWidthDp != change.smallestScreenWidthDp) {
            delta.smallestScreenWidthDp = change.smallestScreenWidthDp;
        }
        if (base.densityDpi != change.densityDpi) {
            delta.densityDpi = change.densityDpi;
        }
        return delta;
    }

    public static void readXmlAttrs(XmlPullParser parser, Configuration configOut) throws XmlPullParserException, IOException {
        configOut.fontScale = Float.intBitsToFloat(XmlUtils.readIntAttribute(parser, XML_ATTR_FONT_SCALE, 0));
        configOut.mcc = XmlUtils.readIntAttribute(parser, XML_ATTR_MCC, 0);
        configOut.mnc = XmlUtils.readIntAttribute(parser, XML_ATTR_MNC, 0);
        String localeStr = XmlUtils.readStringAttribute(parser, XML_ATTR_LOCALE);
        if (localeStr != null) {
            configOut.locale = Locale.forLanguageTag(localeStr);
        }
        configOut.touchscreen = XmlUtils.readIntAttribute(parser, XML_ATTR_TOUCHSCREEN, 0);
        configOut.keyboard = XmlUtils.readIntAttribute(parser, "key", 0);
        configOut.keyboardHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_KEYBOARD_HIDDEN, 0);
        configOut.hardKeyboardHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_HARD_KEYBOARD_HIDDEN, 0);
        configOut.navigation = XmlUtils.readIntAttribute(parser, XML_ATTR_NAVIGATION, 0);
        configOut.navigationHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_NAVIGATION_HIDDEN, 0);
        configOut.orientation = XmlUtils.readIntAttribute(parser, XML_ATTR_ORIENTATION, 0);
        configOut.screenLayout = XmlUtils.readIntAttribute(parser, XML_ATTR_SCREEN_LAYOUT, 0);
        configOut.uiMode = XmlUtils.readIntAttribute(parser, XML_ATTR_UI_MODE, 0);
        configOut.screenWidthDp = XmlUtils.readIntAttribute(parser, "width", 0);
        configOut.screenHeightDp = XmlUtils.readIntAttribute(parser, "height", 0);
        configOut.smallestScreenWidthDp = XmlUtils.readIntAttribute(parser, XML_ATTR_SMALLEST_WIDTH, 0);
        configOut.densityDpi = XmlUtils.readIntAttribute(parser, XML_ATTR_DENSITY, 0);
    }

    public static void writeXmlAttrs(XmlSerializer xml, Configuration config) throws IOException {
        XmlUtils.writeIntAttribute(xml, XML_ATTR_FONT_SCALE, Float.floatToIntBits(config.fontScale));
        if (config.mcc != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_MCC, config.mcc);
        }
        if (config.mnc != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_MNC, config.mnc);
        }
        if (config.locale != null) {
            XmlUtils.writeStringAttribute(xml, XML_ATTR_LOCALE, config.locale.toLanguageTag());
        }
        if (config.touchscreen != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_TOUCHSCREEN, config.touchscreen);
        }
        if (config.keyboard != 0) {
            XmlUtils.writeIntAttribute(xml, "key", config.keyboard);
        }
        if (config.keyboardHidden != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_KEYBOARD_HIDDEN, config.keyboardHidden);
        }
        if (config.hardKeyboardHidden != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_HARD_KEYBOARD_HIDDEN, config.hardKeyboardHidden);
        }
        if (config.navigation != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_NAVIGATION, config.navigation);
        }
        if (config.navigationHidden != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_NAVIGATION_HIDDEN, config.navigationHidden);
        }
        if (config.orientation != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_ORIENTATION, config.orientation);
        }
        if (config.screenLayout != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_SCREEN_LAYOUT, config.screenLayout);
        }
        if (config.uiMode != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_UI_MODE, config.uiMode);
        }
        if (config.screenWidthDp != 0) {
            XmlUtils.writeIntAttribute(xml, "width", config.screenWidthDp);
        }
        if (config.screenHeightDp != 0) {
            XmlUtils.writeIntAttribute(xml, "height", config.screenHeightDp);
        }
        if (config.smallestScreenWidthDp != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_SMALLEST_WIDTH, config.smallestScreenWidthDp);
        }
        if (config.densityDpi != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_DENSITY, config.densityDpi);
        }
    }
}
