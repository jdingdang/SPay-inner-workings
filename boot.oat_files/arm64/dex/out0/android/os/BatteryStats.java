package android.os;

import android.app.IVRManager;
import android.app.Notification;
import android.app.backup.FullBackup;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.hardware.usb.UsbManager;
import android.net.ProxyInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiConfiguration.GroupCipher;
import android.net.wifi.WifiEnterpriseConfig;
import android.provider.Settings.Global;
import android.telephony.SignalStrength;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.Printer;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TimeUtils;
import com.android.internal.os.BatterySipper;
import com.android.internal.os.BatterySipper.DrainType;
import com.android.internal.os.BatteryStatsHelper;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BatteryStats implements Parcelable {
    private static final String APK_DATA = "apk";
    private static final String AUDIO_DATA = "aud";
    public static final int AUDIO_TURNED_ON = 15;
    private static final String BATTERY_DATA = "bt";
    private static final String BATTERY_DISCHARGE_DATA = "dc";
    private static final String BATTERY_LEVEL_DATA = "lv";
    private static final int BATTERY_STATS_CHECKIN_VERSION = 9;
    private static final long BYTES_PER_GB = 1073741824;
    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = 1048576;
    private static final String CAMERA_DATA = "cam";
    public static final int CAMERA_TURNED_ON = 17;
    private static final String CHARGE_STEP_DATA = "csd";
    private static final String CHARGE_TIME_REMAIN_DATA = "ctr";
    static final String CHECKIN_VERSION = "15";
    public static final int CONTROLLER_IDLE_TIME = 0;
    public static final int CONTROLLER_POWER_DRAIN = 3;
    public static final int CONTROLLER_RX_TIME = 1;
    public static final int CONTROLLER_TX_TIME = 2;
    private static final String CPU_DATA = "cpu";
    public static final int DATA_CONNECTION_1xRTT = 7;
    public static final int DATA_CONNECTION_CDMA = 4;
    private static final String DATA_CONNECTION_COUNT_DATA = "dcc";
    public static final int DATA_CONNECTION_EDGE = 2;
    public static final int DATA_CONNECTION_EHRPD = 14;
    public static final int DATA_CONNECTION_EVDO_0 = 5;
    public static final int DATA_CONNECTION_EVDO_A = 6;
    public static final int DATA_CONNECTION_EVDO_B = 12;
    public static final int DATA_CONNECTION_GPRS = 1;
    public static final int DATA_CONNECTION_HSDPA = 8;
    public static final int DATA_CONNECTION_HSPA = 10;
    public static final int DATA_CONNECTION_HSPAP = 15;
    public static final int DATA_CONNECTION_HSUPA = 9;
    public static final int DATA_CONNECTION_IDEN = 11;
    public static final int DATA_CONNECTION_LTE = 13;
    static final String[] DATA_CONNECTION_NAMES = new String[]{"none", "gprs", "edge", "umts", "cdma", "evdo_0", "evdo_A", "1xrtt", "hsdpa", "hsupa", "hspa", "iden", "evdo_b", "lte", "ehrpd", "hspap", "other"};
    public static final int DATA_CONNECTION_NONE = 0;
    public static final int DATA_CONNECTION_OTHER = 16;
    private static final String DATA_CONNECTION_TIME_DATA = "dct";
    public static final int DATA_CONNECTION_UMTS = 3;
    private static final String DISCHARGE_STEP_DATA = "dsd";
    private static final String DISCHARGE_TIME_REMAIN_DATA = "dtr";
    public static final int DUMP_CHARGED_ONLY = 2;
    public static final int DUMP_DAILY_ONLY = 4;
    public static final int DUMP_DEVICE_WIFI_ONLY = 64;
    public static final int DUMP_HISTORY_ONLY = 8;
    public static final int DUMP_INCLUDE_HISTORY = 16;
    public static final int DUMP_VERBOSE = 32;
    private static final String FLASHLIGHT_DATA = "fla";
    public static final int FLASHLIGHT_TURNED_ON = 16;
    public static final int FOREGROUND_ACTIVITY = 10;
    private static final String FOREGROUND_DATA = "fg";
    public static final int FULL_WIFI_LOCK = 5;
    private static final String GLOBAL_BLUETOOTH_DATA = "gble";
    private static final String GLOBAL_NETWORK_DATA = "gn";
    private static final String GLOBAL_WIFI_DATA = "gwfl";
    private static final String HISTORY_DATA = "h";
    public static final String[] HISTORY_EVENT_CHECKIN_NAMES = new String[]{"Enl", "Epr", "Efg", "Etp", "Esy", "Ewl", "Ejb", "Eur", "Euf", "Ecn", "Eac", "Epi", "Epu", "Eal", "Est", "Eai", "Eaa", "Etw", "Esw"};
    public static final String[] HISTORY_EVENT_NAMES = new String[]{"null", "proc", FOREGROUND_DATA, "top", "sync", "wake_lock_in", "job", Context.USER_SERVICE, "userfg", "conn", "active", "pkginst", "pkgunin", "alarm", "stats", "inactive", "active", "tmpwhitelist", "screenwake"};
    public static final BitDescription[] HISTORY_STATE2_DESCRIPTIONS;
    public static final BitDescription[] HISTORY_STATE_DESCRIPTIONS;
    private static final String HISTORY_STRING_POOL = "hsp";
    public static final int JOB = 14;
    private static final String JOB_DATA = "jb";
    private static final String KERNEL_WAKELOCK_DATA = "kwl";
    private static final boolean LOCAL_LOGV = false;
    private static final String MISC_DATA = "m";
    private static final String NETWORK_DATA = "nt";
    public static final int NETWORK_MOBILE_RX_DATA = 0;
    public static final int NETWORK_MOBILE_TX_DATA = 1;
    public static final int NETWORK_WIFI_RX_DATA = 2;
    public static final int NETWORK_WIFI_TX_DATA = 3;
    public static final int NUM_CONTROLLER_ACTIVITY_TYPES = 4;
    public static final int NUM_DATA_CONNECTION_TYPES = 17;
    public static final int NUM_NETWORK_ACTIVITY_TYPES = 4;
    public static final int NUM_SCREEN_BRIGHTNESS_BINS = 5;
    public static final int NUM_WIFI_SIGNAL_STRENGTH_BINS = 5;
    public static final int NUM_WIFI_STATES = 8;
    public static final int NUM_WIFI_SUPPL_STATES = 13;
    private static final String POWER_USE_ITEM_DATA = "pwi";
    private static final String POWER_USE_SUMMARY_DATA = "pws";
    private static final String PROCESS_DATA = "pr";
    public static final int PROCESS_STATE = 12;
    public static final int SCREEN_BRIGHTNESS_BRIGHT = 4;
    public static final int SCREEN_BRIGHTNESS_DARK = 0;
    private static final String SCREEN_BRIGHTNESS_DATA = "br";
    public static final int SCREEN_BRIGHTNESS_DIM = 1;
    public static final int SCREEN_BRIGHTNESS_LIGHT = 3;
    public static final int SCREEN_BRIGHTNESS_MEDIUM = 2;
    static final String[] SCREEN_BRIGHTNESS_NAMES = new String[]{"dark", "dim", "medium", "light", IVRManager.VR_BRIGHTNESS};
    static final String[] SCREEN_BRIGHTNESS_SHORT_NAMES = new String[]{"0", WifiEnterpriseConfig.ENGINE_ENABLE, "2", "3", "4"};
    public static final int SENSOR = 3;
    private static final String SENSOR_DATA = "sr";
    public static final String SERVICE_NAME = "batterystats";
    private static final String SIGNAL_SCANNING_TIME_DATA = "sst";
    private static final String SIGNAL_STRENGTH_COUNT_DATA = "sgc";
    private static final String SIGNAL_STRENGTH_TIME_DATA = "sgt";
    private static final String STATE_TIME_DATA = "st";
    public static final int STATS_CURRENT = 1;
    public static final int STATS_SINCE_CHARGED = 0;
    public static final int STATS_SINCE_UNPLUGGED = 2;
    private static final String[] STAT_NAMES = new String[]{"l", FullBackup.CACHE_TREE_TOKEN, "u"};
    public static final long STEP_LEVEL_INITIAL_MODE_MASK = 71776119061217280L;
    public static final int STEP_LEVEL_INITIAL_MODE_SHIFT = 48;
    public static final long STEP_LEVEL_LEVEL_MASK = 280375465082880L;
    public static final int STEP_LEVEL_LEVEL_SHIFT = 40;
    public static final int[] STEP_LEVEL_MODES_OF_INTEREST = new int[]{7, 15, 11, 7, 7, 7, 7, 7, 15, 11};
    public static final int STEP_LEVEL_MODE_DEVICE_IDLE = 8;
    public static final String[] STEP_LEVEL_MODE_LABELS = new String[]{"screen off", "screen off power save", "screen off device idle", "screen on", "screen on power save", "screen doze", "screen doze power save", "screen doze-suspend", "screen doze-suspend power save", "screen doze-suspend device idle"};
    public static final int STEP_LEVEL_MODE_POWER_SAVE = 4;
    public static final int STEP_LEVEL_MODE_SCREEN_STATE = 3;
    public static final int[] STEP_LEVEL_MODE_VALUES = new int[]{0, 4, 8, 1, 5, 2, 6, 3, 7, 11};
    public static final long STEP_LEVEL_MODIFIED_MODE_MASK = -72057594037927936L;
    public static final int STEP_LEVEL_MODIFIED_MODE_SHIFT = 56;
    public static final long STEP_LEVEL_TIME_MASK = 1099511627775L;
    public static final int SYNC = 13;
    private static final String SYNC_DATA = "sy";
    private static final String UID_DATA = "uid";
    private static final String USER_ACTIVITY_DATA = "ua";
    private static final String VERSION_DATA = "vers";
    private static final String VIBRATOR_DATA = "vib";
    public static final int VIBRATOR_ON = 9;
    private static final String VIDEO_DATA = "vid";
    public static final int VIDEO_TURNED_ON = 8;
    private static final String WAKELOCK_DATA = "wl";
    private static final String WAKEUP_REASON_DATA = "wr";
    public static final int WAKE_TYPE_DRAW = 18;
    public static final int WAKE_TYPE_FULL = 1;
    public static final int WAKE_TYPE_PARTIAL = 0;
    public static final int WAKE_TYPE_WINDOW = 2;
    public static final int WIFI_BATCHED_SCAN = 11;
    private static final String WIFI_DATA = "wfl";
    public static final int WIFI_MULTICAST_ENABLED = 7;
    public static final int WIFI_RUNNING = 4;
    public static final int WIFI_SCAN = 6;
    private static final String WIFI_SIGNAL_STRENGTH_COUNT_DATA = "wsgc";
    private static final String WIFI_SIGNAL_STRENGTH_TIME_DATA = "wsgt";
    private static final String WIFI_STATE_COUNT_DATA = "wsc";
    static final String[] WIFI_STATE_NAMES = new String[]{"off", "scanning", "no_net", "disconn", "sta", "p2p", "sta_p2p", "soft_ap"};
    public static final int WIFI_STATE_OFF = 0;
    public static final int WIFI_STATE_OFF_SCANNING = 1;
    public static final int WIFI_STATE_ON_CONNECTED_P2P = 5;
    public static final int WIFI_STATE_ON_CONNECTED_STA = 4;
    public static final int WIFI_STATE_ON_CONNECTED_STA_P2P = 6;
    public static final int WIFI_STATE_ON_DISCONNECTED = 3;
    public static final int WIFI_STATE_ON_NO_NETWORKS = 2;
    public static final int WIFI_STATE_SOFT_AP = 7;
    private static final String WIFI_STATE_TIME_DATA = "wst";
    public static final int WIFI_SUPPL_STATE_ASSOCIATED = 7;
    public static final int WIFI_SUPPL_STATE_ASSOCIATING = 6;
    public static final int WIFI_SUPPL_STATE_AUTHENTICATING = 5;
    public static final int WIFI_SUPPL_STATE_COMPLETED = 10;
    private static final String WIFI_SUPPL_STATE_COUNT_DATA = "wssc";
    public static final int WIFI_SUPPL_STATE_DISCONNECTED = 1;
    public static final int WIFI_SUPPL_STATE_DORMANT = 11;
    public static final int WIFI_SUPPL_STATE_FOUR_WAY_HANDSHAKE = 8;
    public static final int WIFI_SUPPL_STATE_GROUP_HANDSHAKE = 9;
    public static final int WIFI_SUPPL_STATE_INACTIVE = 3;
    public static final int WIFI_SUPPL_STATE_INTERFACE_DISABLED = 2;
    public static final int WIFI_SUPPL_STATE_INVALID = 0;
    static final String[] WIFI_SUPPL_STATE_NAMES = new String[]{"invalid", "disconn", "disabled", "inactive", "scanning", "authenticating", "associating", "associated", "4-way-handshake", "group-handshake", "completed", "dormant", "uninit"};
    public static final int WIFI_SUPPL_STATE_SCANNING = 4;
    static final String[] WIFI_SUPPL_STATE_SHORT_NAMES = new String[]{"inv", "dsc", "dis", "inact", "scan", "auth", "ascing", "asced", "4-way", GroupCipher.varName, "compl", "dorm", "uninit"};
    private static final String WIFI_SUPPL_STATE_TIME_DATA = "wsst";
    public static final int WIFI_SUPPL_STATE_UNINITIALIZED = 12;
    private final StringBuilder mFormatBuilder = new StringBuilder(32);
    private final Formatter mFormatter = new Formatter(this.mFormatBuilder);

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$os$BatterySipper$DrainType = new int[DrainType.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.CELL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.PHONE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.WIFI.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.BLUETOOTH.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.SCREEN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.FLASHLIGHT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.APP.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.USER.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.UNACCOUNTED.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.OVERCOUNTED.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$internal$os$BatterySipper$DrainType[DrainType.CAMERA.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
        }
    }

    public static final class BitDescription {
        public final int mask;
        public final String name;
        public final int shift;
        public final String shortName;
        public final String[] shortValues;
        public final String[] values;

        public BitDescription(int mask, String name, String shortName) {
            this.mask = mask;
            this.shift = -1;
            this.name = name;
            this.shortName = shortName;
            this.values = null;
            this.shortValues = null;
        }

        public BitDescription(int mask, int shift, String name, String shortName, String[] values, String[] shortValues) {
            this.mask = mask;
            this.shift = shift;
            this.name = name;
            this.shortName = shortName;
            this.values = values;
            this.shortValues = shortValues;
        }
    }

    public static abstract class Counter {
        public abstract int getCountLocked(int i);

        public abstract void logState(Printer printer, String str);
    }

    public static final class DailyItem {
        public LevelStepTracker mChargeSteps;
        public LevelStepTracker mDischargeSteps;
        public long mEndTime;
        public ArrayList<PackageChange> mPackageChanges;
        public long mStartTime;
    }

    public static final class HistoryEventTracker {
        private final HashMap<String, SparseIntArray>[] mActiveEvents = new HashMap[19];

        public boolean updateState(int code, String name, int uid, int poolIdx) {
            int idx;
            HashMap<String, SparseIntArray> active;
            SparseIntArray uids;
            if ((32768 & code) != 0) {
                idx = code & HistoryItem.EVENT_TYPE_MASK;
                active = this.mActiveEvents[idx];
                if (active == null) {
                    active = new HashMap();
                    this.mActiveEvents[idx] = active;
                }
                uids = (SparseIntArray) active.get(name);
                if (uids == null) {
                    uids = new SparseIntArray();
                    active.put(name, uids);
                }
                if (uids.indexOfKey(uid) >= 0) {
                    return false;
                }
                uids.put(uid, poolIdx);
            } else if ((code & 16384) != 0) {
                active = this.mActiveEvents[code & HistoryItem.EVENT_TYPE_MASK];
                if (active == null) {
                    return false;
                }
                uids = (SparseIntArray) active.get(name);
                if (uids == null) {
                    return false;
                }
                idx = uids.indexOfKey(uid);
                if (idx < 0) {
                    return false;
                }
                uids.removeAt(idx);
                if (uids.size() <= 0) {
                    active.remove(name);
                }
            }
            return true;
        }

        public void removeEvents(int code) {
            this.mActiveEvents[code & HistoryItem.EVENT_TYPE_MASK] = null;
        }

        public HashMap<String, SparseIntArray> getStateForEvent(int code) {
            return this.mActiveEvents[code];
        }
    }

    public static final class HistoryItem implements Parcelable {
        public static final byte CMD_CURRENT_TIME = (byte) 5;
        public static final byte CMD_NULL = (byte) -1;
        public static final byte CMD_OVERFLOW = (byte) 6;
        public static final byte CMD_RESET = (byte) 7;
        public static final byte CMD_SHUTDOWN = (byte) 8;
        public static final byte CMD_START = (byte) 4;
        public static final byte CMD_UPDATE = (byte) 0;
        public static final int EVENT_ACTIVE = 10;
        public static final int EVENT_ALARM = 13;
        public static final int EVENT_ALARM_FINISH = 16397;
        public static final int EVENT_ALARM_START = 32781;
        public static final int EVENT_COLLECT_EXTERNAL_STATS = 14;
        public static final int EVENT_CONNECTIVITY_CHANGED = 9;
        public static final int EVENT_COUNT = 19;
        public static final int EVENT_FLAG_FINISH = 16384;
        public static final int EVENT_FLAG_START = 32768;
        public static final int EVENT_FOREGROUND = 2;
        public static final int EVENT_FOREGROUND_FINISH = 16386;
        public static final int EVENT_FOREGROUND_START = 32770;
        public static final int EVENT_JOB = 6;
        public static final int EVENT_JOB_FINISH = 16390;
        public static final int EVENT_JOB_START = 32774;
        public static final int EVENT_NONE = 0;
        public static final int EVENT_PACKAGE_ACTIVE = 16;
        public static final int EVENT_PACKAGE_INACTIVE = 15;
        public static final int EVENT_PACKAGE_INSTALLED = 11;
        public static final int EVENT_PACKAGE_UNINSTALLED = 12;
        public static final int EVENT_PROC = 1;
        public static final int EVENT_PROC_FINISH = 16385;
        public static final int EVENT_PROC_START = 32769;
        public static final int EVENT_SCREEN_WAKE_UP = 18;
        public static final int EVENT_SYNC = 4;
        public static final int EVENT_SYNC_FINISH = 16388;
        public static final int EVENT_SYNC_START = 32772;
        public static final int EVENT_TEMP_WHITELIST = 17;
        public static final int EVENT_TEMP_WHITELIST_FINISH = 16401;
        public static final int EVENT_TEMP_WHITELIST_START = 32785;
        public static final int EVENT_TOP = 3;
        public static final int EVENT_TOP_FINISH = 16387;
        public static final int EVENT_TOP_START = 32771;
        public static final int EVENT_TYPE_MASK = -49153;
        public static final int EVENT_USER_FOREGROUND = 8;
        public static final int EVENT_USER_FOREGROUND_FINISH = 16392;
        public static final int EVENT_USER_FOREGROUND_START = 32776;
        public static final int EVENT_USER_RUNNING = 7;
        public static final int EVENT_USER_RUNNING_FINISH = 16391;
        public static final int EVENT_USER_RUNNING_START = 32775;
        public static final int EVENT_WAKE_LOCK = 5;
        public static final int EVENT_WAKE_LOCK_FINISH = 16389;
        public static final int EVENT_WAKE_LOCK_START = 32773;
        public static final int MOST_INTERESTING_STATES = 1572864;
        public static final int MOST_INTERESTING_STATES2 = -1753219072;
        public static final int SETTLE_TO_ZERO_STATES = -1638400;
        public static final int SETTLE_TO_ZERO_STATES2 = 1753153536;
        public static final int STATE2_BLUETOOTH_ON_FLAG = 8388608;
        public static final int STATE2_CAMERA_FLAG = 4194304;
        public static final int STATE2_CHARGING_FLAG = 33554432;
        public static final int STATE2_DEVICE_IDLE_FLAG = 67108864;
        public static final int STATE2_FLASHLIGHT_FLAG = 134217728;
        public static final int STATE2_PHONE_IN_CALL_FLAG = 16777216;
        public static final int STATE2_POWER_SAVE_FLAG = Integer.MIN_VALUE;
        public static final int STATE2_VIDEO_ON_FLAG = 1073741824;
        public static final int STATE2_WIFI_ON_FLAG = 268435456;
        public static final int STATE2_WIFI_RUNNING_FLAG = 536870912;
        public static final int STATE2_WIFI_SIGNAL_STRENGTH_MASK = 112;
        public static final int STATE2_WIFI_SIGNAL_STRENGTH_SHIFT = 4;
        public static final int STATE2_WIFI_SUPPL_STATE_MASK = 15;
        public static final int STATE2_WIFI_SUPPL_STATE_SHIFT = 0;
        public static final int STATE_AUDIO_ON_FLAG = 4194304;
        public static final int STATE_BATTERY_PLUGGED_FLAG = 524288;
        public static final int STATE_BRIGHTNESS_MASK = 7;
        public static final int STATE_BRIGHTNESS_SHIFT = 0;
        public static final int STATE_CPU_RUNNING_FLAG = Integer.MIN_VALUE;
        public static final int STATE_DATA_CONNECTION_MASK = 15872;
        public static final int STATE_DATA_CONNECTION_SHIFT = 9;
        public static final int STATE_GPS_ON_FLAG = 536870912;
        public static final int STATE_MOBILE_RADIO_ACTIVE_FLAG = 33554432;
        public static final int STATE_PHONE_SCANNING_FLAG = 2097152;
        public static final int STATE_PHONE_SIGNAL_STRENGTH_MASK = 56;
        public static final int STATE_PHONE_SIGNAL_STRENGTH_SHIFT = 3;
        public static final int STATE_PHONE_STATE_MASK = 448;
        public static final int STATE_PHONE_STATE_SHIFT = 6;
        public static final int STATE_SCREEN_ON_FLAG = 1048576;
        public static final int STATE_SENSOR_ON_FLAG = 8388608;
        public static final int STATE_WAKE_LOCK_FLAG = 1073741824;
        public static final int STATE_WIFI_FULL_LOCK_FLAG = 268435456;
        public static final int STATE_WIFI_MULTICAST_ON_FLAG = 65536;
        public static final int STATE_WIFI_RADIO_ACTIVE_FLAG = 67108864;
        public static final int STATE_WIFI_SCAN_FLAG = 134217728;
        public byte batteryHealth;
        public byte batteryLevel;
        public byte batteryPlugType;
        public byte batteryStatus;
        public short batteryTemperature;
        public char batteryVoltage;
        public byte cmd = (byte) -1;
        public long currentTime;
        public int eventCode;
        public HistoryTag eventTag;
        public final HistoryTag localEventTag = new HistoryTag();
        public final HistoryTag localWakeReasonTag = new HistoryTag();
        public final HistoryTag localWakelockTag = new HistoryTag();
        public HistoryItem next;
        public int numReadInts;
        public int states;
        public int states2;
        public HistoryStepDetails stepDetails;
        public long time;
        public HistoryTag wakeReasonTag;
        public HistoryTag wakelockTag;

        public boolean isDeltaData() {
            return this.cmd == (byte) 0;
        }

        public HistoryItem(long time, Parcel src) {
            this.time = time;
            this.numReadInts = 2;
            readFromParcel(src);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i;
            int i2 = 0;
            dest.writeLong(this.time);
            int i3 = ((this.batteryPlugType << 24) & 251658240) | ((((this.cmd & 255) | ((this.batteryLevel << 8) & 65280)) | ((this.batteryStatus << 16) & 983040)) | ((this.batteryHealth << 20) & 15728640));
            if (this.wakelockTag != null) {
                i = 268435456;
            } else {
                i = 0;
            }
            i = (this.wakeReasonTag != null ? 536870912 : 0) | (i3 | i);
            if (this.eventCode != 0) {
                i2 = 1073741824;
            }
            dest.writeInt(i | i2);
            dest.writeInt((this.batteryTemperature & 65535) | ((this.batteryVoltage << 16) & Color.RED));
            dest.writeInt(this.states);
            dest.writeInt(this.states2);
            if (this.wakelockTag != null) {
                this.wakelockTag.writeToParcel(dest, flags);
            }
            if (this.wakeReasonTag != null) {
                this.wakeReasonTag.writeToParcel(dest, flags);
            }
            if (this.eventCode != 0) {
                dest.writeInt(this.eventCode);
                this.eventTag.writeToParcel(dest, flags);
            }
            if (this.cmd == (byte) 5 || this.cmd == (byte) 7) {
                dest.writeLong(this.currentTime);
            }
        }

        public void readFromParcel(Parcel src) {
            int start = src.dataPosition();
            int bat = src.readInt();
            this.cmd = (byte) (bat & 255);
            this.batteryLevel = (byte) ((bat >> 8) & 255);
            this.batteryStatus = (byte) ((bat >> 16) & 15);
            this.batteryHealth = (byte) ((bat >> 20) & 15);
            this.batteryPlugType = (byte) ((bat >> 24) & 15);
            int bat2 = src.readInt();
            this.batteryTemperature = (short) (bat2 & 65535);
            this.batteryVoltage = (char) ((bat2 >> 16) & 65535);
            this.states = src.readInt();
            this.states2 = src.readInt();
            if ((268435456 & bat) != 0) {
                this.wakelockTag = this.localWakelockTag;
                this.wakelockTag.readFromParcel(src);
            } else {
                this.wakelockTag = null;
            }
            if ((536870912 & bat) != 0) {
                this.wakeReasonTag = this.localWakeReasonTag;
                this.wakeReasonTag.readFromParcel(src);
            } else {
                this.wakeReasonTag = null;
            }
            if ((1073741824 & bat) != 0) {
                this.eventCode = src.readInt();
                this.eventTag = this.localEventTag;
                this.eventTag.readFromParcel(src);
            } else {
                this.eventCode = 0;
                this.eventTag = null;
            }
            if (this.cmd == (byte) 5 || this.cmd == (byte) 7) {
                this.currentTime = src.readLong();
            } else {
                this.currentTime = 0;
            }
            this.numReadInts += (src.dataPosition() - start) / 4;
        }

        public void clear() {
            this.time = 0;
            this.cmd = (byte) -1;
            this.batteryLevel = (byte) 0;
            this.batteryStatus = (byte) 0;
            this.batteryHealth = (byte) 0;
            this.batteryPlugType = (byte) 0;
            this.batteryTemperature = (short) 0;
            this.batteryVoltage = '\u0000';
            this.states = 0;
            this.states2 = 0;
            this.wakelockTag = null;
            this.wakeReasonTag = null;
            this.eventCode = 0;
            this.eventTag = null;
        }

        public void setTo(HistoryItem o) {
            this.time = o.time;
            this.cmd = o.cmd;
            setToCommon(o);
        }

        public void setTo(long time, byte cmd, HistoryItem o) {
            this.time = time;
            this.cmd = cmd;
            setToCommon(o);
        }

        private void setToCommon(HistoryItem o) {
            this.batteryLevel = o.batteryLevel;
            this.batteryStatus = o.batteryStatus;
            this.batteryHealth = o.batteryHealth;
            this.batteryPlugType = o.batteryPlugType;
            this.batteryTemperature = o.batteryTemperature;
            this.batteryVoltage = o.batteryVoltage;
            this.states = o.states;
            this.states2 = o.states2;
            if (o.wakelockTag != null) {
                this.wakelockTag = this.localWakelockTag;
                this.wakelockTag.setTo(o.wakelockTag);
            } else {
                this.wakelockTag = null;
            }
            if (o.wakeReasonTag != null) {
                this.wakeReasonTag = this.localWakeReasonTag;
                this.wakeReasonTag.setTo(o.wakeReasonTag);
            } else {
                this.wakeReasonTag = null;
            }
            this.eventCode = o.eventCode;
            if (o.eventTag != null) {
                this.eventTag = this.localEventTag;
                this.eventTag.setTo(o.eventTag);
            } else {
                this.eventTag = null;
            }
            this.currentTime = o.currentTime;
        }

        public boolean sameNonEvent(HistoryItem o) {
            return this.batteryLevel == o.batteryLevel && this.batteryStatus == o.batteryStatus && this.batteryHealth == o.batteryHealth && this.batteryPlugType == o.batteryPlugType && this.batteryTemperature == o.batteryTemperature && this.batteryVoltage == o.batteryVoltage && this.states == o.states && this.states2 == o.states2 && this.currentTime == o.currentTime;
        }

        public boolean same(HistoryItem o) {
            if (!sameNonEvent(o) || this.eventCode != o.eventCode) {
                return false;
            }
            if (this.wakelockTag != o.wakelockTag && (this.wakelockTag == null || o.wakelockTag == null || !this.wakelockTag.equals(o.wakelockTag))) {
                return false;
            }
            if (this.wakeReasonTag != o.wakeReasonTag && (this.wakeReasonTag == null || o.wakeReasonTag == null || !this.wakeReasonTag.equals(o.wakeReasonTag))) {
                return false;
            }
            if (this.eventTag == o.eventTag || (this.eventTag != null && o.eventTag != null && this.eventTag.equals(o.eventTag))) {
                return true;
            }
            return false;
        }
    }

    public static class HistoryPrinter {
        long lastTime = -1;
        int oldHealth = -1;
        int oldLevel = -1;
        int oldPlug = -1;
        int oldState = 0;
        int oldState2 = 0;
        int oldStatus = -1;
        int oldTemp = -1;
        int oldVolt = -1;

        void reset() {
            this.oldState2 = 0;
            this.oldState = 0;
            this.oldLevel = -1;
            this.oldStatus = -1;
            this.oldHealth = -1;
            this.oldPlug = -1;
            this.oldTemp = -1;
            this.oldVolt = -1;
        }

        public void printNextItem(PrintWriter pw, HistoryItem rec, long baseTime, boolean checkin, boolean verbose) {
            if (checkin) {
                pw.print(9);
                pw.print(',');
                pw.print(BatteryStats.HISTORY_DATA);
                pw.print(',');
                if (this.lastTime < 0) {
                    pw.print(rec.time - baseTime);
                } else {
                    pw.print(rec.time - this.lastTime);
                }
                this.lastTime = rec.time;
            } else {
                pw.print("  ");
                TimeUtils.formatDuration(rec.time - baseTime, pw, 19);
                pw.print(" (");
                pw.print(rec.numReadInts);
                pw.print(") ");
            }
            if (rec.cmd == (byte) 4) {
                if (checkin) {
                    pw.print(":");
                }
                pw.println("START");
                reset();
            } else if (rec.cmd == (byte) 5 || rec.cmd == (byte) 7) {
                if (checkin) {
                    pw.print(":");
                }
                if (rec.cmd == (byte) 7) {
                    pw.print("RESET:");
                    reset();
                }
                pw.print("TIME:");
                if (checkin) {
                    pw.println(rec.currentTime);
                    return;
                }
                pw.print(" ");
                pw.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", rec.currentTime).toString());
            } else if (rec.cmd == (byte) 8) {
                if (checkin) {
                    pw.print(":");
                }
                pw.println("SHUTDOWN");
            } else if (rec.cmd == (byte) 6) {
                if (checkin) {
                    pw.print(":");
                }
                pw.println("*OVERFLOW*");
            } else {
                if (!checkin) {
                    if (rec.batteryLevel < (byte) 10) {
                        pw.print("00");
                    } else if (rec.batteryLevel < (byte) 100) {
                        pw.print("0");
                    }
                    pw.print(rec.batteryLevel);
                    if (verbose) {
                        pw.print(" ");
                        if (rec.states >= 0) {
                            if (rec.states < 16) {
                                pw.print("0000000");
                            } else if (rec.states < 256) {
                                pw.print("000000");
                            } else if (rec.states < 4096) {
                                pw.print("00000");
                            } else if (rec.states < 65536) {
                                pw.print("0000");
                            } else if (rec.states < 1048576) {
                                pw.print("000");
                            } else if (rec.states < 16777216) {
                                pw.print("00");
                            } else if (rec.states < 268435456) {
                                pw.print("0");
                            }
                        }
                        pw.print(Integer.toHexString(rec.states));
                    }
                } else if (this.oldLevel != rec.batteryLevel) {
                    this.oldLevel = rec.batteryLevel;
                    pw.print(",Bl=");
                    pw.print(rec.batteryLevel);
                }
                if (this.oldStatus != rec.batteryStatus) {
                    this.oldStatus = rec.batteryStatus;
                    pw.print(checkin ? ",Bs=" : " status=");
                    switch (this.oldStatus) {
                        case 1:
                            pw.print(checkin ? "?" : "unknown");
                            break;
                        case 2:
                            pw.print(checkin ? FullBackup.CACHE_TREE_TOKEN : UsbManager.USB_FUNCTION_CHARGING);
                            break;
                        case 3:
                            pw.print(checkin ? "d" : "discharging");
                            break;
                        case 4:
                            pw.print(checkin ? "n" : "not-charging");
                            break;
                        case 5:
                            pw.print(checkin ? FullBackup.DATA_TREE_TOKEN : "full");
                            break;
                        default:
                            pw.print(this.oldStatus);
                            break;
                    }
                }
                if (this.oldHealth != rec.batteryHealth) {
                    this.oldHealth = rec.batteryHealth;
                    pw.print(checkin ? ",Bh=" : " health=");
                    switch (this.oldHealth) {
                        case 1:
                            pw.print(checkin ? "?" : "unknown");
                            break;
                        case 2:
                            pw.print(checkin ? "g" : "good");
                            break;
                        case 3:
                            pw.print(checkin ? BatteryStats.HISTORY_DATA : "overheat");
                            break;
                        case 4:
                            pw.print(checkin ? "d" : "dead");
                            break;
                        case 5:
                            pw.print(checkin ? "v" : "over-voltage");
                            break;
                        case 6:
                            pw.print(checkin ? FullBackup.DATA_TREE_TOKEN : "failure");
                            break;
                        case 7:
                            pw.print(checkin ? FullBackup.CACHE_TREE_TOKEN : "cold");
                            break;
                        default:
                            pw.print(this.oldHealth);
                            break;
                    }
                }
                if (this.oldPlug != rec.batteryPlugType) {
                    this.oldPlug = rec.batteryPlugType;
                    pw.print(checkin ? ",Bp=" : " plug=");
                    switch (this.oldPlug) {
                        case 0:
                            pw.print(checkin ? "n" : "none");
                            break;
                        case 1:
                            pw.print(checkin ? FullBackup.APK_TREE_TOKEN : "ac");
                            break;
                        case 2:
                            pw.print(checkin ? "u" : Context.USB_SERVICE);
                            break;
                        case 4:
                            pw.print(checkin ? "w" : "wireless");
                            break;
                        default:
                            pw.print(this.oldPlug);
                            break;
                    }
                }
                if (this.oldTemp != rec.batteryTemperature) {
                    this.oldTemp = rec.batteryTemperature;
                    pw.print(checkin ? ",Bt=" : " temp=");
                    pw.print(this.oldTemp);
                }
                if (this.oldVolt != rec.batteryVoltage) {
                    this.oldVolt = rec.batteryVoltage;
                    pw.print(checkin ? ",Bv=" : " volt=");
                    pw.print(this.oldVolt);
                }
                BatteryStats.printBitDescriptions(pw, this.oldState, rec.states, rec.wakelockTag, BatteryStats.HISTORY_STATE_DESCRIPTIONS, !checkin);
                BatteryStats.printBitDescriptions(pw, this.oldState2, rec.states2, null, BatteryStats.HISTORY_STATE2_DESCRIPTIONS, !checkin);
                if (rec.wakeReasonTag != null) {
                    if (checkin) {
                        pw.print(",wr=");
                        pw.print(rec.wakeReasonTag.poolIdx);
                    } else {
                        pw.print(" wake_reason=");
                        pw.print(rec.wakeReasonTag.uid);
                        pw.print(":\"");
                        pw.print(rec.wakeReasonTag.string);
                        pw.print("\"");
                    }
                }
                if (rec.eventCode != 0) {
                    pw.print(checkin ? "," : " ");
                    if ((rec.eventCode & 32768) != 0) {
                        pw.print("+");
                    } else if ((rec.eventCode & 16384) != 0) {
                        pw.print("-");
                    }
                    String[] eventNames = checkin ? BatteryStats.HISTORY_EVENT_CHECKIN_NAMES : BatteryStats.HISTORY_EVENT_NAMES;
                    int idx = rec.eventCode & HistoryItem.EVENT_TYPE_MASK;
                    if (idx < 0 || idx >= eventNames.length) {
                        pw.print(checkin ? "Ev" : Notification.CATEGORY_EVENT);
                        pw.print(idx);
                    } else {
                        pw.print(eventNames[idx]);
                    }
                    pw.print("=");
                    if (checkin) {
                        pw.print(rec.eventTag.poolIdx);
                    } else {
                        UserHandle.formatUid(pw, rec.eventTag.uid);
                        pw.print(":\"");
                        pw.print(rec.eventTag.string);
                        pw.print("\"");
                    }
                }
                pw.println();
                if (rec.stepDetails != null) {
                    if (checkin) {
                        pw.print(9);
                        pw.print(',');
                        pw.print(BatteryStats.HISTORY_DATA);
                        pw.print(",0,Dcpu=");
                        pw.print(rec.stepDetails.userTime);
                        pw.print(":");
                        pw.print(rec.stepDetails.systemTime);
                        if (rec.stepDetails.appCpuUid1 >= 0) {
                            printStepCpuUidCheckinDetails(pw, rec.stepDetails.appCpuUid1, rec.stepDetails.appCpuUTime1, rec.stepDetails.appCpuSTime1);
                            if (rec.stepDetails.appCpuUid2 >= 0) {
                                printStepCpuUidCheckinDetails(pw, rec.stepDetails.appCpuUid2, rec.stepDetails.appCpuUTime2, rec.stepDetails.appCpuSTime2);
                            }
                            if (rec.stepDetails.appCpuUid3 >= 0) {
                                printStepCpuUidCheckinDetails(pw, rec.stepDetails.appCpuUid3, rec.stepDetails.appCpuUTime3, rec.stepDetails.appCpuSTime3);
                            }
                        }
                        pw.println();
                        pw.print(9);
                        pw.print(',');
                        pw.print(BatteryStats.HISTORY_DATA);
                        pw.print(",0,Dpst=");
                        pw.print(rec.stepDetails.statUserTime);
                        pw.print(',');
                        pw.print(rec.stepDetails.statSystemTime);
                        pw.print(',');
                        pw.print(rec.stepDetails.statIOWaitTime);
                        pw.print(',');
                        pw.print(rec.stepDetails.statIrqTime);
                        pw.print(',');
                        pw.print(rec.stepDetails.statSoftIrqTime);
                        pw.print(',');
                        pw.print(rec.stepDetails.statIdlTime);
                        pw.println();
                    } else {
                        pw.print("                 Details: cpu=");
                        pw.print(rec.stepDetails.userTime);
                        pw.print("u+");
                        pw.print(rec.stepDetails.systemTime);
                        pw.print("s");
                        if (rec.stepDetails.appCpuUid1 >= 0) {
                            pw.print(" (");
                            printStepCpuUidDetails(pw, rec.stepDetails.appCpuUid1, rec.stepDetails.appCpuUTime1, rec.stepDetails.appCpuSTime1);
                            if (rec.stepDetails.appCpuUid2 >= 0) {
                                pw.print(", ");
                                printStepCpuUidDetails(pw, rec.stepDetails.appCpuUid2, rec.stepDetails.appCpuUTime2, rec.stepDetails.appCpuSTime2);
                            }
                            if (rec.stepDetails.appCpuUid3 >= 0) {
                                pw.print(", ");
                                printStepCpuUidDetails(pw, rec.stepDetails.appCpuUid3, rec.stepDetails.appCpuUTime3, rec.stepDetails.appCpuSTime3);
                            }
                            pw.print(')');
                        }
                        pw.println();
                        pw.print("                          /proc/stat=");
                        pw.print(rec.stepDetails.statUserTime);
                        pw.print(" usr, ");
                        pw.print(rec.stepDetails.statSystemTime);
                        pw.print(" sys, ");
                        pw.print(rec.stepDetails.statIOWaitTime);
                        pw.print(" io, ");
                        pw.print(rec.stepDetails.statIrqTime);
                        pw.print(" irq, ");
                        pw.print(rec.stepDetails.statSoftIrqTime);
                        pw.print(" sirq, ");
                        pw.print(rec.stepDetails.statIdlTime);
                        pw.print(" idle");
                        int totalRun = (((rec.stepDetails.statUserTime + rec.stepDetails.statSystemTime) + rec.stepDetails.statIOWaitTime) + rec.stepDetails.statIrqTime) + rec.stepDetails.statSoftIrqTime;
                        int total = totalRun + rec.stepDetails.statIdlTime;
                        if (total > 0) {
                            pw.print(" (");
                            float perc = (((float) totalRun) / ((float) total)) * SensorManager.LIGHT_CLOUDY;
                            pw.print(String.format("%.1f%%", new Object[]{Float.valueOf(perc)}));
                            pw.print(" of ");
                            StringBuilder sb = new StringBuilder(64);
                            BatteryStats.formatTimeMsNoSpace(sb, (long) (total * 10));
                            pw.print(sb);
                            pw.print(")");
                        }
                        pw.println();
                    }
                }
                this.oldState = rec.states;
                this.oldState2 = rec.states2;
            }
        }

        private void printStepCpuUidDetails(PrintWriter pw, int uid, int utime, int stime) {
            UserHandle.formatUid(pw, uid);
            pw.print("=");
            pw.print(utime);
            pw.print("u+");
            pw.print(stime);
            pw.print("s");
        }

        private void printStepCpuUidCheckinDetails(PrintWriter pw, int uid, int utime, int stime) {
            pw.print('/');
            pw.print(uid);
            pw.print(":");
            pw.print(utime);
            pw.print(":");
            pw.print(stime);
        }
    }

    public static final class HistoryStepDetails {
        public int appCpuSTime1;
        public int appCpuSTime2;
        public int appCpuSTime3;
        public int appCpuUTime1;
        public int appCpuUTime2;
        public int appCpuUTime3;
        public int appCpuUid1;
        public int appCpuUid2;
        public int appCpuUid3;
        public int statIOWaitTime;
        public int statIdlTime;
        public int statIrqTime;
        public int statSoftIrqTime;
        public int statSystemTime;
        public int statUserTime;
        public int systemTime;
        public int userTime;

        public HistoryStepDetails() {
            clear();
        }

        public void clear() {
            this.systemTime = 0;
            this.userTime = 0;
            this.appCpuUid3 = -1;
            this.appCpuUid2 = -1;
            this.appCpuUid1 = -1;
            this.appCpuSTime3 = 0;
            this.appCpuUTime3 = 0;
            this.appCpuSTime2 = 0;
            this.appCpuUTime2 = 0;
            this.appCpuSTime1 = 0;
            this.appCpuUTime1 = 0;
        }

        public void writeToParcel(Parcel out) {
            out.writeInt(this.userTime);
            out.writeInt(this.systemTime);
            out.writeInt(this.appCpuUid1);
            out.writeInt(this.appCpuUTime1);
            out.writeInt(this.appCpuSTime1);
            out.writeInt(this.appCpuUid2);
            out.writeInt(this.appCpuUTime2);
            out.writeInt(this.appCpuSTime2);
            out.writeInt(this.appCpuUid3);
            out.writeInt(this.appCpuUTime3);
            out.writeInt(this.appCpuSTime3);
            out.writeInt(this.statUserTime);
            out.writeInt(this.statSystemTime);
            out.writeInt(this.statIOWaitTime);
            out.writeInt(this.statIrqTime);
            out.writeInt(this.statSoftIrqTime);
            out.writeInt(this.statIdlTime);
        }

        public void readFromParcel(Parcel in) {
            this.userTime = in.readInt();
            this.systemTime = in.readInt();
            this.appCpuUid1 = in.readInt();
            this.appCpuUTime1 = in.readInt();
            this.appCpuSTime1 = in.readInt();
            this.appCpuUid2 = in.readInt();
            this.appCpuUTime2 = in.readInt();
            this.appCpuSTime2 = in.readInt();
            this.appCpuUid3 = in.readInt();
            this.appCpuUTime3 = in.readInt();
            this.appCpuSTime3 = in.readInt();
            this.statUserTime = in.readInt();
            this.statSystemTime = in.readInt();
            this.statIOWaitTime = in.readInt();
            this.statIrqTime = in.readInt();
            this.statSoftIrqTime = in.readInt();
            this.statIdlTime = in.readInt();
        }
    }

    public static final class HistoryTag {
        public int poolIdx;
        public String string;
        public int uid;

        public void setTo(HistoryTag o) {
            this.string = o.string;
            this.uid = o.uid;
            this.poolIdx = o.poolIdx;
        }

        public void setTo(String _string, int _uid) {
            this.string = _string;
            this.uid = _uid;
            this.poolIdx = -1;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.string);
            dest.writeInt(this.uid);
        }

        public void readFromParcel(Parcel src) {
            this.string = src.readString();
            this.uid = src.readInt();
            this.poolIdx = -1;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HistoryTag that = (HistoryTag) o;
            if (this.uid != that.uid) {
                return false;
            }
            if (this.string.equals(that.string)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.string.hashCode() * 31) + this.uid;
        }
    }

    public static final class LevelStepTracker {
        public long mLastStepTime = -1;
        public int mNumStepDurations;
        public final long[] mStepDurations;

        public LevelStepTracker(int maxLevelSteps) {
            this.mStepDurations = new long[maxLevelSteps];
        }

        public LevelStepTracker(int numSteps, long[] steps) {
            this.mNumStepDurations = numSteps;
            this.mStepDurations = new long[numSteps];
            System.arraycopy(steps, 0, this.mStepDurations, 0, numSteps);
        }

        public long getDurationAt(int index) {
            return this.mStepDurations[index] & BatteryStats.STEP_LEVEL_TIME_MASK;
        }

        public int getLevelAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_LEVEL_MASK) >> 40);
        }

        public int getInitModeAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK) >> 48);
        }

        public int getModModeAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK) >> 56);
        }

        private void appendHex(long val, int topOffset, StringBuilder out) {
            boolean hasData = false;
            while (topOffset >= 0) {
                int digit = (int) ((val >> topOffset) & 15);
                topOffset -= 4;
                if (hasData || digit != 0) {
                    hasData = true;
                    if (digit < 0 || digit > 9) {
                        out.append((char) ((digit + 97) - 10));
                    } else {
                        out.append((char) (digit + 48));
                    }
                }
            }
        }

        public void encodeEntryAt(int index, StringBuilder out) {
            long item = this.mStepDurations[index];
            long duration = item & BatteryStats.STEP_LEVEL_TIME_MASK;
            int level = (int) ((BatteryStats.STEP_LEVEL_LEVEL_MASK & item) >> 40);
            int initMode = (int) ((BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK & item) >> 48);
            int modMode = (int) ((BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK & item) >> 56);
            switch ((initMode & 3) + 1) {
                case 1:
                    out.append('f');
                    break;
                case 2:
                    out.append('o');
                    break;
                case 3:
                    out.append('d');
                    break;
                case 4:
                    out.append('z');
                    break;
            }
            if ((initMode & 4) != 0) {
                out.append('p');
            }
            if ((initMode & 8) != 0) {
                out.append('i');
            }
            switch ((modMode & 3) + 1) {
                case 1:
                    out.append('F');
                    break;
                case 2:
                    out.append('O');
                    break;
                case 3:
                    out.append('D');
                    break;
                case 4:
                    out.append('Z');
                    break;
            }
            if ((modMode & 4) != 0) {
                out.append('P');
            }
            if ((modMode & 8) != 0) {
                out.append('I');
            }
            out.append('-');
            appendHex((long) level, 4, out);
            out.append('-');
            appendHex(duration, 36, out);
        }

        public void decodeEntryAt(int index, String value) {
            long level;
            long duration;
            int N = value.length();
            int i = 0;
            long out = 0;
            while (i < N) {
                char c = value.charAt(i);
                if (c != '-') {
                    i++;
                    switch (c) {
                        case 'D':
                            out |= 144115188075855872L;
                            break;
                        case 'F':
                            out |= 0;
                            break;
                        case 'I':
                            out |= 576460752303423488L;
                            break;
                        case 'O':
                            out |= 72057594037927936L;
                            break;
                        case 'P':
                            out |= 288230376151711744L;
                            break;
                        case 'Z':
                            out |= 216172782113783808L;
                            break;
                        case 'd':
                            out |= 562949953421312L;
                            break;
                        case 'f':
                            out |= 0;
                            break;
                        case 'i':
                            out |= 2251799813685248L;
                            break;
                        case 'o':
                            out |= 281474976710656L;
                            break;
                        case 'p':
                            out |= TrafficStats.PB_IN_BYTES;
                            break;
                        case 'z':
                            out |= 844424930131968L;
                            break;
                        default:
                            break;
                    }
                }
                i++;
                level = 0;
                while (i < N) {
                    c = value.charAt(i);
                    if (c == '-') {
                        i++;
                        level <<= 4;
                        if (c < '0' && c <= '9') {
                            level += (long) (c - 48);
                        } else if (c < 'a' && c <= 'f') {
                            level += (long) ((c - 97) + 10);
                        } else if (c >= 'A' && c <= 'F') {
                            level += (long) ((c - 65) + 10);
                        }
                    } else {
                        i++;
                        out |= (level << 40) & BatteryStats.STEP_LEVEL_LEVEL_MASK;
                        duration = 0;
                        while (i < N) {
                            c = value.charAt(i);
                            if (c == '-') {
                                i++;
                                duration <<= 4;
                                if (c < '0' && c <= '9') {
                                    duration += (long) (c - 48);
                                } else if (c < 'a' && c <= 'f') {
                                    duration += (long) ((c - 97) + 10);
                                } else if (c >= 'A' && c <= 'F') {
                                    duration += (long) ((c - 65) + 10);
                                }
                            } else {
                                this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
                            }
                        }
                        this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
                    }
                }
                i++;
                out |= (level << 40) & BatteryStats.STEP_LEVEL_LEVEL_MASK;
                duration = 0;
                while (i < N) {
                    c = value.charAt(i);
                    if (c == '-') {
                        this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
                    }
                    i++;
                    duration <<= 4;
                    if (c < '0') {
                    }
                    if (c < 'a') {
                    }
                    duration += (long) ((c - 65) + 10);
                }
                this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
            }
            i++;
            level = 0;
            while (i < N) {
                c = value.charAt(i);
                if (c == '-') {
                    i++;
                    out |= (level << 40) & BatteryStats.STEP_LEVEL_LEVEL_MASK;
                    duration = 0;
                    while (i < N) {
                        c = value.charAt(i);
                        if (c == '-') {
                            i++;
                            duration <<= 4;
                            if (c < '0') {
                            }
                            if (c < 'a') {
                            }
                            duration += (long) ((c - 65) + 10);
                        } else {
                            this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
                        }
                    }
                    this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
                }
                i++;
                level <<= 4;
                if (c < '0') {
                }
                if (c < 'a') {
                }
                level += (long) ((c - 65) + 10);
            }
            i++;
            out |= (level << 40) & BatteryStats.STEP_LEVEL_LEVEL_MASK;
            duration = 0;
            while (i < N) {
                c = value.charAt(i);
                if (c == '-') {
                    this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
                }
                i++;
                duration <<= 4;
                if (c < '0') {
                }
                if (c < 'a') {
                }
                duration += (long) ((c - 65) + 10);
            }
            this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
        }

        public void init() {
            this.mLastStepTime = -1;
            this.mNumStepDurations = 0;
        }

        public void clearTime() {
            this.mLastStepTime = -1;
        }

        public long computeTimePerLevel() {
            long[] steps = this.mStepDurations;
            int numSteps = this.mNumStepDurations;
            if (numSteps <= 0) {
                return -1;
            }
            long total = 0;
            for (int i = 0; i < numSteps; i++) {
                total += steps[i] & BatteryStats.STEP_LEVEL_TIME_MASK;
            }
            return total / ((long) numSteps);
        }

        public long computeTimeEstimate(long modesOfInterest, long modeValues, int[] outNumOfInterest) {
            long[] steps = this.mStepDurations;
            int count = this.mNumStepDurations;
            if (count <= 0) {
                return -1;
            }
            long total = 0;
            int numOfInterest = 0;
            for (int i = 0; i < count; i++) {
                long initMode = (steps[i] & BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK) >> 48;
                if ((((steps[i] & BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK) >> 56) & modesOfInterest) == 0 && (initMode & modesOfInterest) == modeValues) {
                    numOfInterest++;
                    total += steps[i] & BatteryStats.STEP_LEVEL_TIME_MASK;
                }
            }
            if (numOfInterest <= 0) {
                return -1;
            }
            if (outNumOfInterest != null) {
                outNumOfInterest[0] = numOfInterest;
            }
            return (total / ((long) numOfInterest)) * 100;
        }

        public void addLevelSteps(int numStepLevels, long modeBits, long elapsedRealtime) {
            int stepCount = this.mNumStepDurations;
            long lastStepTime = this.mLastStepTime;
            if (lastStepTime >= 0 && numStepLevels > 0) {
                long[] steps = this.mStepDurations;
                long duration = elapsedRealtime - lastStepTime;
                for (int i = 0; i < numStepLevels; i++) {
                    System.arraycopy(steps, 0, steps, 1, steps.length - 1);
                    long thisDuration = duration / ((long) (numStepLevels - i));
                    duration -= thisDuration;
                    if (thisDuration > BatteryStats.STEP_LEVEL_TIME_MASK) {
                        thisDuration = BatteryStats.STEP_LEVEL_TIME_MASK;
                    }
                    steps[0] = thisDuration | modeBits;
                }
                stepCount += numStepLevels;
                if (stepCount > steps.length) {
                    stepCount = steps.length;
                }
            }
            this.mNumStepDurations = stepCount;
            this.mLastStepTime = elapsedRealtime;
        }

        public void readFromParcel(Parcel in) {
            int N = in.readInt();
            if (N > this.mStepDurations.length) {
                throw new ParcelFormatException("more step durations than available: " + N);
            }
            this.mNumStepDurations = N;
            for (int i = 0; i < N; i++) {
                this.mStepDurations[i] = in.readLong();
            }
        }

        public void writeToParcel(Parcel out) {
            int N = this.mNumStepDurations;
            out.writeInt(N);
            for (int i = 0; i < N; i++) {
                out.writeLong(this.mStepDurations[i]);
            }
        }
    }

    public static abstract class LongCounter {
        public abstract long getCountLocked(int i);

        public abstract void logState(Printer printer, String str);
    }

    public static final class PackageChange {
        public String mPackageName;
        public boolean mUpdate;
        public int mVersionCode;
    }

    public static abstract class Timer {
        public abstract int getCountLocked(int i);

        public abstract long getTimeSinceMarkLocked(long j);

        public abstract long getTotalTimeLocked(long j, int i);

        public abstract void logState(Printer printer, String str);
    }

    static final class TimerEntry {
        final int mId;
        final String mName;
        final long mTime;
        final Timer mTimer;

        TimerEntry(String name, int id, Timer timer, long time) {
            this.mName = name;
            this.mId = id;
            this.mTimer = timer;
            this.mTime = time;
        }
    }

    public static abstract class Uid {
        public static final int NUM_PROCESS_STATE = 3;
        public static final int NUM_USER_ACTIVITY_TYPES = 3;
        public static final int NUM_WIFI_BATCHED_SCAN_BINS = 5;
        public static final int PROCESS_STATE_ACTIVE = 1;
        public static final int PROCESS_STATE_FOREGROUND = 0;
        static final String[] PROCESS_STATE_NAMES = new String[]{"Foreground", "Active", "Running"};
        public static final int PROCESS_STATE_RUNNING = 2;
        static final String[] USER_ACTIVITY_TYPES = new String[]{"other", "button", "touch"};

        public class Pid {
            public int mWakeNesting;
            public long mWakeStartMs;
            public long mWakeSumMs;
        }

        public static abstract class Pkg {

            public abstract class Serv {
                public abstract int getLaunches(int i);

                public abstract long getStartTime(long j, int i);

                public abstract int getStarts(int i);
            }

            public abstract ArrayMap<String, ? extends Serv> getServiceStats();

            public abstract ArrayMap<String, ? extends Counter> getWakeupAlarmStats();
        }

        public static abstract class Proc {

            public static class ExcessivePower {
                public static final int TYPE_CPU = 2;
                public static final int TYPE_WAKE = 1;
                public long overTime;
                public int type;
                public long usedTime;
            }

            public abstract int countExcessivePowers();

            public abstract ExcessivePower getExcessivePower(int i);

            public abstract long getForegroundTime(int i);

            public abstract int getNumAnrs(int i);

            public abstract int getNumCrashes(int i);

            public abstract int getStarts(int i);

            public abstract long getSystemTime(int i);

            public abstract long getUserTime(int i);

            public abstract boolean isActive();
        }

        public static abstract class Sensor {
            public static final int GPS = -10000;

            public abstract int getHandle();

            public abstract Timer getSensorTime();
        }

        public static abstract class Wakelock {
            public abstract Timer getWakeTime(int i);
        }

        public abstract Timer getAudioTurnedOnTimer();

        public abstract Timer getCameraTurnedOnTimer();

        public abstract long getCpuPowerMaUs(int i);

        public abstract Timer getFlashlightTurnedOnTimer();

        public abstract Timer getForegroundActivityTimer();

        public abstract long getFullWifiLockTime(long j, int i);

        public abstract ArrayMap<String, ? extends Timer> getJobStats();

        public abstract int getMobileRadioActiveCount(int i);

        public abstract long getMobileRadioActiveTime(int i);

        public abstract long getNetworkActivityBytes(int i, int i2);

        public abstract long getNetworkActivityPackets(int i, int i2);

        public abstract ArrayMap<String, ? extends Pkg> getPackageStats();

        public abstract SparseArray<? extends Pid> getPidStats();

        public abstract long getProcessStateTime(int i, long j, int i2);

        public abstract ArrayMap<String, ? extends Proc> getProcessStats();

        public abstract SparseArray<? extends Sensor> getSensorStats();

        public abstract ArrayMap<String, ? extends Timer> getSyncStats();

        public abstract long getSystemCpuTimeUs(int i);

        public abstract long getTimeAtCpuSpeed(int i, int i2, int i3);

        public abstract int getUid();

        public abstract int getUserActivityCount(int i, int i2);

        public abstract long getUserCpuTimeUs(int i);

        public abstract Timer getVibratorOnTimer();

        public abstract Timer getVideoTurnedOnTimer();

        public abstract ArrayMap<String, ? extends Wakelock> getWakelockStats();

        public abstract int getWifiBatchedScanCount(int i, int i2);

        public abstract long getWifiBatchedScanTime(int i, long j, int i2);

        public abstract long getWifiControllerActivity(int i, int i2);

        public abstract long getWifiMulticastTime(long j, int i);

        public abstract long getWifiRunningTime(long j, int i);

        public abstract int getWifiScanCount(int i);

        public abstract long getWifiScanTime(long j, int i);

        public abstract boolean hasNetworkActivity();

        public abstract boolean hasUserActivity();

        public abstract void noteActivityPausedLocked(long j);

        public abstract void noteActivityResumedLocked(long j);

        public abstract void noteFullWifiLockAcquiredLocked(long j);

        public abstract void noteFullWifiLockReleasedLocked(long j);

        public abstract void noteUserActivityLocked(int i);

        public abstract void noteWifiBatchedScanStartedLocked(int i, long j);

        public abstract void noteWifiBatchedScanStoppedLocked(long j);

        public abstract void noteWifiMulticastDisabledLocked(long j);

        public abstract void noteWifiMulticastEnabledLocked(long j);

        public abstract void noteWifiRunningLocked(long j);

        public abstract void noteWifiScanStartedLocked(long j);

        public abstract void noteWifiScanStoppedLocked(long j);

        public abstract void noteWifiStoppedLocked(long j);
    }

    public abstract void commitCurrentHistoryBatchLocked();

    public abstract long computeBatteryRealtime(long j, int i);

    public abstract long computeBatteryScreenOffRealtime(long j, int i);

    public abstract long computeBatteryScreenOffUptime(long j, int i);

    public abstract long computeBatteryTimeRemaining(long j);

    public abstract long computeBatteryUptime(long j, int i);

    public abstract long computeChargeTimeRemaining(long j);

    public abstract long computeRealtime(long j, int i);

    public abstract long computeUptime(long j, int i);

    public abstract void finishIteratingHistoryLocked();

    public abstract void finishIteratingOldHistoryLocked();

    public abstract long getBatteryRealtime(long j);

    public abstract long getBatteryUptime(long j);

    public abstract long getBluetoothControllerActivity(int i, int i2);

    public abstract long getCameraOnTime(long j, int i);

    public abstract LevelStepTracker getChargeLevelStepTracker();

    public abstract long getCurrentDailyStartTime();

    public abstract LevelStepTracker getDailyChargeLevelStepTracker();

    public abstract LevelStepTracker getDailyDischargeLevelStepTracker();

    public abstract DailyItem getDailyItemLocked(int i);

    public abstract ArrayList<PackageChange> getDailyPackageChanges();

    public abstract int getDeviceIdleModeEnabledCount(int i);

    public abstract long getDeviceIdleModeEnabledTime(long j, int i);

    public abstract int getDeviceIdlingCount(int i);

    public abstract long getDeviceIdlingTime(long j, int i);

    public abstract int getDischargeAmount(int i);

    public abstract int getDischargeAmountScreenOff();

    public abstract int getDischargeAmountScreenOffSinceCharge();

    public abstract int getDischargeAmountScreenOn();

    public abstract int getDischargeAmountScreenOnSinceCharge();

    public abstract int getDischargeCurrentLevel();

    public abstract LevelStepTracker getDischargeLevelStepTracker();

    public abstract int getDischargeStartLevel();

    public abstract String getEndPlatformVersion();

    public abstract long getFlashlightOnCount(int i);

    public abstract long getFlashlightOnTime(long j, int i);

    public abstract long getGlobalWifiRunningTime(long j, int i);

    public abstract int getHighDischargeAmountSinceCharge();

    public abstract long getHistoryBaseTime();

    public abstract int getHistoryStringPoolBytes();

    public abstract int getHistoryStringPoolSize();

    public abstract String getHistoryTagPoolString(int i);

    public abstract int getHistoryTagPoolUid(int i);

    public abstract int getHistoryTotalSize();

    public abstract int getHistoryUsedSize();

    public abstract long getInteractiveTime(long j, int i);

    public abstract boolean getIsOnBattery();

    public abstract Map<String, ? extends Timer> getKernelWakelockStats();

    public abstract int getLowDischargeAmountSinceCharge();

    public abstract long getMobileRadioActiveAdjustedTime(int i);

    public abstract int getMobileRadioActiveCount(int i);

    public abstract long getMobileRadioActiveTime(long j, int i);

    public abstract int getMobileRadioActiveUnknownCount(int i);

    public abstract long getMobileRadioActiveUnknownTime(int i);

    public abstract long getNetworkActivityBytes(int i, int i2);

    public abstract long getNetworkActivityPackets(int i, int i2);

    public abstract boolean getNextHistoryLocked(HistoryItem historyItem);

    public abstract long getNextMaxDailyDeadline();

    public abstract long getNextMinDailyDeadline();

    public abstract boolean getNextOldHistoryLocked(HistoryItem historyItem);

    public abstract int getNumConnectivityChange(int i);

    public abstract int getParcelVersion();

    public abstract int getPhoneDataConnectionCount(int i, int i2);

    public abstract long getPhoneDataConnectionTime(int i, long j, int i2);

    public abstract int getPhoneOnCount(int i);

    public abstract long getPhoneOnTime(long j, int i);

    public abstract long getPhoneSignalScanningTime(long j, int i);

    public abstract int getPhoneSignalStrengthCount(int i, int i2);

    public abstract long getPhoneSignalStrengthTime(int i, long j, int i2);

    public abstract int getPowerSaveModeEnabledCount(int i);

    public abstract long getPowerSaveModeEnabledTime(long j, int i);

    public abstract long getScreenBrightnessTime(int i, long j, int i2);

    public abstract int getScreenOnCount(int i);

    public abstract long getScreenOnTime(long j, int i);

    public abstract long getStartClockTime();

    public abstract int getStartCount();

    public abstract String getStartPlatformVersion();

    public abstract SparseArray<? extends Uid> getUidStats();

    public abstract Map<String, ? extends Timer> getWakeupReasonStats();

    public abstract long getWifiControllerActivity(int i, int i2);

    public abstract long getWifiOnTime(long j, int i);

    public abstract int getWifiSignalStrengthCount(int i, int i2);

    public abstract long getWifiSignalStrengthTime(int i, long j, int i2);

    public abstract int getWifiStateCount(int i, int i2);

    public abstract long getWifiStateTime(int i, long j, int i2);

    public abstract int getWifiSupplStateCount(int i, int i2);

    public abstract long getWifiSupplStateTime(int i, long j, int i2);

    public abstract boolean hasBluetoothActivityReporting();

    public abstract boolean hasWifiActivityReporting();

    public abstract boolean startIteratingHistoryLocked();

    public abstract boolean startIteratingOldHistoryLocked();

    public abstract void writeToParcelWithoutUids(Parcel parcel, int i);

    static {
        r7 = new BitDescription[17];
        r7[14] = new BitDescription(448, 6, "phone_state", "Pst", new String[]{"in", "out", "emergency", "off"}, new String[]{"in", "out", "em", "off"});
        int i = 3;
        r7[15] = new BitDescription(56, i, "phone_signal_strength", "Pss", SignalStrength.SIGNAL_STRENGTH_NAMES, new String[]{"0", WifiEnterpriseConfig.ENGINE_ENABLE, "2", "3", "4", "5", "6"});
        r7[16] = new BitDescription(7, 0, "brightness", "Sb", SCREEN_BRIGHTNESS_NAMES, SCREEN_BRIGHTNESS_SHORT_NAMES);
        HISTORY_STATE_DESCRIPTIONS = r7;
        r7 = new BitDescription[12];
        i = 4;
        r7[9] = new BitDescription(112, i, "wifi_signal_strength", "Wss", new String[]{"0", WifiEnterpriseConfig.ENGINE_ENABLE, "2", "3", "4"}, new String[]{"0", WifiEnterpriseConfig.ENGINE_ENABLE, "2", "3", "4"});
        r7[10] = new BitDescription(15, 0, "wifi_suppl", "Wsp", WIFI_SUPPL_STATE_NAMES, WIFI_SUPPL_STATE_SHORT_NAMES);
        r7[11] = new BitDescription(4194304, Context.CAMERA_SERVICE, "ca");
        HISTORY_STATE2_DESCRIPTIONS = r7;
    }

    private static final void formatTimeRaw(StringBuilder out, long seconds) {
        long days = seconds / 86400;
        if (days != 0) {
            out.append(days);
            out.append("d ");
        }
        long used = ((60 * days) * 60) * 24;
        long hours = (seconds - used) / 3600;
        if (!(hours == 0 && used == 0)) {
            out.append(hours);
            out.append("h ");
        }
        used += (60 * hours) * 60;
        long mins = (seconds - used) / 60;
        if (!(mins == 0 && used == 0)) {
            out.append(mins);
            out.append("m ");
        }
        used += 60 * mins;
        if (seconds != 0 || used != 0) {
            out.append(seconds - used);
            out.append("s ");
        }
    }

    public static final void formatTimeMs(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatTimeRaw(sb, sec);
        sb.append(time - (1000 * sec));
        sb.append("ms ");
    }

    public static final void formatTimeMsNoSpace(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatTimeRaw(sb, sec);
        sb.append(time - (1000 * sec));
        sb.append("ms");
    }

    public final String formatRatioLocked(long num, long den) {
        if (den == 0) {
            return "--%";
        }
        float perc = (((float) num) / ((float) den)) * SensorManager.LIGHT_CLOUDY;
        this.mFormatBuilder.setLength(0);
        this.mFormatter.format("%.1f%%", new Object[]{Float.valueOf(perc)});
        return this.mFormatBuilder.toString();
    }

    final String formatBytesLocked(long bytes) {
        this.mFormatBuilder.setLength(0);
        if (bytes < 1024) {
            return bytes + "B";
        }
        if (bytes < 1048576) {
            this.mFormatter.format("%.2fKB", new Object[]{Double.valueOf(((double) bytes) / 1024.0d)});
            return this.mFormatBuilder.toString();
        } else if (bytes < 1073741824) {
            this.mFormatter.format("%.2fMB", new Object[]{Double.valueOf(((double) bytes) / 1048576.0d)});
            return this.mFormatBuilder.toString();
        } else {
            this.mFormatter.format("%.2fGB", new Object[]{Double.valueOf(((double) bytes) / 1.073741824E9d)});
            return this.mFormatBuilder.toString();
        }
    }

    private static long computeWakeLock(Timer timer, long elapsedRealtimeUs, int which) {
        if (timer != null) {
            return (500 + timer.getTotalTimeLocked(elapsedRealtimeUs, which)) / 1000;
        }
        return 0;
    }

    private static final String printWakeLock(StringBuilder sb, Timer timer, long elapsedRealtimeUs, String name, int which, String linePrefix) {
        if (timer == null) {
            return linePrefix;
        }
        long totalTimeMillis = computeWakeLock(timer, elapsedRealtimeUs, which);
        int count = timer.getCountLocked(which);
        if (totalTimeMillis == 0) {
            return linePrefix;
        }
        sb.append(linePrefix);
        formatTimeMs(sb, totalTimeMillis);
        if (name != null) {
            sb.append(name);
            sb.append(' ');
        }
        sb.append('(');
        sb.append(count);
        sb.append(" times)");
        return ", ";
    }

    private static final boolean printTimer(PrintWriter pw, StringBuilder sb, Timer timer, long rawRealtime, int which, String prefix, String type) {
        if (timer == null) {
            return false;
        }
        long totalTime = (timer.getTotalTimeLocked(rawRealtime, which) + 500) / 1000;
        int count = timer.getCountLocked(which);
        if (totalTime == 0) {
            return false;
        }
        sb.setLength(0);
        sb.append(prefix);
        sb.append("    ");
        sb.append(type);
        sb.append(": ");
        formatTimeMs(sb, totalTime);
        sb.append("realtime (");
        sb.append(count);
        sb.append(" times)");
        pw.println(sb.toString());
        return true;
    }

    private static final String printWakeLockCheckin(StringBuilder sb, Timer timer, long elapsedRealtimeUs, String name, int which, String linePrefix) {
        long totalTimeMicros = 0;
        int count = 0;
        if (timer != null) {
            totalTimeMicros = timer.getTotalTimeLocked(elapsedRealtimeUs, which);
            count = timer.getCountLocked(which);
        }
        sb.append(linePrefix);
        sb.append((500 + totalTimeMicros) / 1000);
        sb.append(',');
        sb.append(name != null ? name + "," : ProxyInfo.LOCAL_EXCL_LIST);
        sb.append(count);
        return ",";
    }

    private static final void dumpLine(PrintWriter pw, int uid, String category, String type, Object... args) {
        pw.print(9);
        pw.print(',');
        pw.print(uid);
        pw.print(',');
        pw.print(category);
        pw.print(',');
        pw.print(type);
        for (Object arg : args) {
            pw.print(',');
            pw.print(arg);
        }
        pw.println();
    }

    private static final void dumpTimer(PrintWriter pw, int uid, String category, String type, Timer timer, long rawRealtime, int which) {
        if (timer != null) {
            long totalTime = (timer.getTotalTimeLocked(rawRealtime, which) + 500) / 1000;
            int count = timer.getCountLocked(which);
            if (totalTime != 0) {
                dumpLine(pw, uid, category, type, Long.valueOf(totalTime), Integer.valueOf(count));
            }
        }
    }

    public final void dumpCheckinLocked(Context context, PrintWriter pw, int which, int reqUid) {
        dumpCheckinLocked(context, pw, which, reqUid, BatteryStatsHelper.checkWifiOnly(context));
    }

    public final void dumpCheckinLocked(Context context, PrintWriter pw, int which, int reqUid, boolean wifiOnly) {
        int iu;
        int iw;
        int i;
        int uid;
        long rawUptime = SystemClock.uptimeMillis() * 1000;
        long rawRealtime = SystemClock.elapsedRealtime() * 1000;
        long batteryUptime = getBatteryUptime(rawUptime);
        long whichBatteryUptime = computeBatteryUptime(rawUptime, which);
        long whichBatteryRealtime = computeBatteryRealtime(rawRealtime, which);
        long whichBatteryScreenOffUptime = computeBatteryScreenOffUptime(rawUptime, which);
        long whichBatteryScreenOffRealtime = computeBatteryScreenOffRealtime(rawRealtime, which);
        long totalRealtime = computeRealtime(rawRealtime, which);
        long totalUptime = computeUptime(rawUptime, which);
        long screenOnTime = getScreenOnTime(rawRealtime, which);
        long interactiveTime = getInteractiveTime(rawRealtime, which);
        long powerSaveModeEnabledTime = getPowerSaveModeEnabledTime(rawRealtime, which);
        long deviceIdleModeEnabledTime = getDeviceIdleModeEnabledTime(rawRealtime, which);
        long deviceIdlingTime = getDeviceIdlingTime(rawRealtime, which);
        int connChanges = getNumConnectivityChange(which);
        long phoneOnTime = getPhoneOnTime(rawRealtime, which);
        StringBuilder sb = new StringBuilder(128);
        SparseArray<? extends Uid> uidStats = getUidStats();
        int NU = uidStats.size();
        String category = STAT_NAMES[which];
        String str = BATTERY_DATA;
        Object[] objArr = new Object[8];
        objArr[0] = which == 0 ? Integer.valueOf(getStartCount()) : "N/A";
        objArr[1] = Long.valueOf(whichBatteryRealtime / 1000);
        objArr[2] = Long.valueOf(whichBatteryUptime / 1000);
        objArr[3] = Long.valueOf(totalRealtime / 1000);
        objArr[4] = Long.valueOf(totalUptime / 1000);
        objArr[5] = Long.valueOf(getStartClockTime());
        objArr[6] = Long.valueOf(whichBatteryScreenOffRealtime / 1000);
        objArr[7] = Long.valueOf(whichBatteryScreenOffUptime / 1000);
        dumpLine(pw, 0, category, str, objArr);
        long fullWakeLockTimeTotal = 0;
        long partialWakeLockTimeTotal = 0;
        for (iu = 0; iu < NU; iu++) {
            ArrayMap<String, ? extends Wakelock> wakelocks = ((Uid) uidStats.valueAt(iu)).getWakelockStats();
            for (iw = wakelocks.size() - 1; iw >= 0; iw--) {
                Wakelock wl = (Wakelock) wakelocks.valueAt(iw);
                Timer fullWakeTimer = wl.getWakeTime(1);
                if (fullWakeTimer != null) {
                    fullWakeLockTimeTotal += fullWakeTimer.getTotalTimeLocked(rawRealtime, which);
                }
                Timer partialWakeTimer = wl.getWakeTime(0);
                if (partialWakeTimer != null) {
                    partialWakeLockTimeTotal += partialWakeTimer.getTotalTimeLocked(rawRealtime, which);
                }
            }
        }
        long mobileRxTotalBytes = getNetworkActivityBytes(0, which);
        long mobileTxTotalBytes = getNetworkActivityBytes(1, which);
        long wifiRxTotalBytes = getNetworkActivityBytes(2, which);
        long wifiTxTotalBytes = getNetworkActivityBytes(3, which);
        long mobileRxTotalPackets = getNetworkActivityPackets(0, which);
        long mobileTxTotalPackets = getNetworkActivityPackets(1, which);
        long wifiRxTotalPackets = getNetworkActivityPackets(2, which);
        long wifiTxTotalPackets = getNetworkActivityPackets(3, which);
        dumpLine(pw, 0, category, GLOBAL_NETWORK_DATA, Long.valueOf(mobileRxTotalBytes), Long.valueOf(mobileTxTotalBytes), Long.valueOf(wifiRxTotalBytes), Long.valueOf(wifiTxTotalBytes), Long.valueOf(mobileRxTotalPackets), Long.valueOf(mobileTxTotalPackets), Long.valueOf(wifiRxTotalPackets), Long.valueOf(wifiTxTotalPackets));
        long wifiOnTime = getWifiOnTime(rawRealtime, which);
        long wifiRunningTime = getGlobalWifiRunningTime(rawRealtime, which);
        long wifiIdleTimeMs = getWifiControllerActivity(0, which);
        long wifiRxTimeMs = getWifiControllerActivity(1, which);
        long wifiTxTimeMs = getWifiControllerActivity(2, which);
        long wifiPowerMaMs = getWifiControllerActivity(3, which);
        dumpLine(pw, 0, category, GLOBAL_WIFI_DATA, Long.valueOf(wifiOnTime / 1000), Long.valueOf(wifiRunningTime / 1000), Long.valueOf(wifiIdleTimeMs), Long.valueOf(wifiRxTimeMs), Long.valueOf(wifiTxTimeMs), Long.valueOf(wifiPowerMaMs / 3600000));
        long btIdleTimeMs = getBluetoothControllerActivity(0, which);
        long btRxTimeMs = getBluetoothControllerActivity(1, which);
        long btTxTimeMs = getBluetoothControllerActivity(2, which);
        long btPowerMaMs = getBluetoothControllerActivity(3, which);
        dumpLine(pw, 0, category, GLOBAL_BLUETOOTH_DATA, Long.valueOf(btIdleTimeMs), Long.valueOf(btRxTimeMs), Long.valueOf(btTxTimeMs), Long.valueOf(btPowerMaMs / 3600000));
        dumpLine(pw, 0, category, MISC_DATA, Long.valueOf(screenOnTime / 1000), Long.valueOf(phoneOnTime / 1000), Long.valueOf(fullWakeLockTimeTotal / 1000), Long.valueOf(partialWakeLockTimeTotal / 1000), Long.valueOf(getMobileRadioActiveTime(rawRealtime, which) / 1000), Long.valueOf(getMobileRadioActiveAdjustedTime(which) / 1000), Long.valueOf(interactiveTime / 1000), Long.valueOf(powerSaveModeEnabledTime / 1000), Integer.valueOf(connChanges), Long.valueOf(deviceIdleModeEnabledTime / 1000), Integer.valueOf(getDeviceIdleModeEnabledCount(which)), Long.valueOf(deviceIdlingTime / 1000), Integer.valueOf(getDeviceIdlingCount(which)), Integer.valueOf(getMobileRadioActiveCount(which)), Long.valueOf(getMobileRadioActiveUnknownTime(which) / 1000));
        Object[] args = new Object[5];
        for (i = 0; i < 5; i++) {
            args[i] = Long.valueOf(getScreenBrightnessTime(i, rawRealtime, which) / 1000);
        }
        dumpLine(pw, 0, category, "br", args);
        args = new Object[SignalStrength.NUM_SIGNAL_STRENGTH_BINS];
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            args[i] = Long.valueOf(getPhoneSignalStrengthTime(i, rawRealtime, which) / 1000);
        }
        dumpLine(pw, 0, category, SIGNAL_STRENGTH_TIME_DATA, args);
        dumpLine(pw, 0, category, SIGNAL_SCANNING_TIME_DATA, Long.valueOf(getPhoneSignalScanningTime(rawRealtime, which) / 1000));
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            args[i] = Integer.valueOf(getPhoneSignalStrengthCount(i, which));
        }
        dumpLine(pw, 0, category, SIGNAL_STRENGTH_COUNT_DATA, args);
        args = new Object[17];
        for (i = 0; i < 17; i++) {
            args[i] = Long.valueOf(getPhoneDataConnectionTime(i, rawRealtime, which) / 1000);
        }
        dumpLine(pw, 0, category, DATA_CONNECTION_TIME_DATA, args);
        for (i = 0; i < 17; i++) {
            args[i] = Integer.valueOf(getPhoneDataConnectionCount(i, which));
        }
        dumpLine(pw, 0, category, DATA_CONNECTION_COUNT_DATA, args);
        args = new Object[8];
        for (i = 0; i < 8; i++) {
            args[i] = Long.valueOf(getWifiStateTime(i, rawRealtime, which) / 1000);
        }
        dumpLine(pw, 0, category, WIFI_STATE_TIME_DATA, args);
        for (i = 0; i < 8; i++) {
            args[i] = Integer.valueOf(getWifiStateCount(i, which));
        }
        dumpLine(pw, 0, category, WIFI_STATE_COUNT_DATA, args);
        args = new Object[13];
        for (i = 0; i < 13; i++) {
            args[i] = Long.valueOf(getWifiSupplStateTime(i, rawRealtime, which) / 1000);
        }
        dumpLine(pw, 0, category, WIFI_SUPPL_STATE_TIME_DATA, args);
        for (i = 0; i < 13; i++) {
            args[i] = Integer.valueOf(getWifiSupplStateCount(i, which));
        }
        dumpLine(pw, 0, category, WIFI_SUPPL_STATE_COUNT_DATA, args);
        args = new Object[5];
        for (i = 0; i < 5; i++) {
            args[i] = Long.valueOf(getWifiSignalStrengthTime(i, rawRealtime, which) / 1000);
        }
        dumpLine(pw, 0, category, WIFI_SIGNAL_STRENGTH_TIME_DATA, args);
        for (i = 0; i < 5; i++) {
            args[i] = Integer.valueOf(getWifiSignalStrengthCount(i, which));
        }
        dumpLine(pw, 0, category, WIFI_SIGNAL_STRENGTH_COUNT_DATA, args);
        if (which == 2) {
            dumpLine(pw, 0, category, BATTERY_LEVEL_DATA, Integer.valueOf(getDischargeStartLevel()), Integer.valueOf(getDischargeCurrentLevel()));
        }
        if (which == 2) {
            dumpLine(pw, 0, category, BATTERY_DISCHARGE_DATA, Integer.valueOf(getDischargeStartLevel() - getDischargeCurrentLevel()), Integer.valueOf(getDischargeStartLevel() - getDischargeCurrentLevel()), Integer.valueOf(getDischargeAmountScreenOn()), Integer.valueOf(getDischargeAmountScreenOff()));
        } else {
            dumpLine(pw, 0, category, BATTERY_DISCHARGE_DATA, Integer.valueOf(getLowDischargeAmountSinceCharge()), Integer.valueOf(getHighDischargeAmountSinceCharge()), Integer.valueOf(getDischargeAmountScreenOnSinceCharge()), Integer.valueOf(getDischargeAmountScreenOffSinceCharge()));
        }
        if (reqUid < 0) {
            Map<String, ? extends Timer> kernelWakelocks = getKernelWakelockStats();
            if (kernelWakelocks.size() > 0) {
                for (Entry<String, ? extends Timer> ent : kernelWakelocks.entrySet()) {
                    sb.setLength(0);
                    printWakeLockCheckin(sb, (Timer) ent.getValue(), rawRealtime, null, which, ProxyInfo.LOCAL_EXCL_LIST);
                    dumpLine(pw, 0, category, KERNEL_WAKELOCK_DATA, ent.getKey(), sb.toString());
                }
            }
            Map<String, ? extends Timer> wakeupReasons = getWakeupReasonStats();
            if (wakeupReasons.size() > 0) {
                for (Entry<String, ? extends Timer> ent2 : wakeupReasons.entrySet()) {
                    long totalTimeMicros = ((Timer) ent2.getValue()).getTotalTimeLocked(rawRealtime, which);
                    int count = ((Timer) ent2.getValue()).getCountLocked(which);
                    dumpLine(pw, 0, category, WAKEUP_REASON_DATA, "\"" + ((String) ent2.getKey()) + "\"", Long.valueOf((500 + totalTimeMicros) / 1000), Integer.valueOf(count));
                }
            }
        }
        BatteryStatsHelper batteryStatsHelper = new BatteryStatsHelper(context, false, wifiOnly);
        batteryStatsHelper.create(this);
        batteryStatsHelper.refreshStats(which, -1);
        List<BatterySipper> sippers = batteryStatsHelper.getUsageList();
        if (sippers != null && sippers.size() > 0) {
            dumpLine(pw, 0, category, POWER_USE_SUMMARY_DATA, BatteryStatsHelper.makemAh(batteryStatsHelper.getPowerProfile().getBatteryCapacity()), BatteryStatsHelper.makemAh(batteryStatsHelper.getComputedPower()), BatteryStatsHelper.makemAh(batteryStatsHelper.getMinDrainedPower()), BatteryStatsHelper.makemAh(batteryStatsHelper.getMaxDrainedPower()));
            for (i = 0; i < sippers.size(); i++) {
                String label;
                BatterySipper bs = (BatterySipper) sippers.get(i);
                uid = 0;
                switch (AnonymousClass2.$SwitchMap$com$android$internal$os$BatterySipper$DrainType[bs.drainType.ordinal()]) {
                    case 1:
                        label = "idle";
                        break;
                    case 2:
                        label = Global.RADIO_CELL;
                        break;
                    case 3:
                        label = "phone";
                        break;
                    case 4:
                        label = "wifi";
                        break;
                    case 5:
                        label = "blue";
                        break;
                    case 6:
                        label = "scrn";
                        break;
                    case 7:
                        label = "flashlight";
                        break;
                    case 8:
                        uid = bs.uidObj.getUid();
                        label = "uid";
                        break;
                    case 9:
                        uid = UserHandle.getUid(bs.userId, 0);
                        label = Context.USER_SERVICE;
                        break;
                    case 10:
                        label = "unacc";
                        break;
                    case 11:
                        label = "over";
                        break;
                    case 12:
                        label = Context.CAMERA_SERVICE;
                        break;
                    default:
                        label = "???";
                        break;
                }
                dumpLine(pw, uid, category, POWER_USE_ITEM_DATA, label, BatteryStatsHelper.makemAh(bs.totalPowerMah));
            }
        }
        for (iu = 0; iu < NU; iu++) {
            uid = uidStats.keyAt(iu);
            if (reqUid < 0 || uid == reqUid) {
                Timer timer;
                long totalTime;
                int starts;
                Uid u = (Uid) uidStats.valueAt(iu);
                long mobileBytesRx = u.getNetworkActivityBytes(0, which);
                long mobileBytesTx = u.getNetworkActivityBytes(1, which);
                long wifiBytesRx = u.getNetworkActivityBytes(2, which);
                long wifiBytesTx = u.getNetworkActivityBytes(3, which);
                long mobilePacketsRx = u.getNetworkActivityPackets(0, which);
                long mobilePacketsTx = u.getNetworkActivityPackets(1, which);
                long mobileActiveTime = u.getMobileRadioActiveTime(which);
                int mobileActiveCount = u.getMobileRadioActiveCount(which);
                long wifiPacketsRx = u.getNetworkActivityPackets(2, which);
                long wifiPacketsTx = u.getNetworkActivityPackets(3, which);
                if (mobileBytesRx > 0 || mobileBytesTx > 0 || wifiBytesRx > 0 || wifiBytesTx > 0 || mobilePacketsRx > 0 || mobilePacketsTx > 0 || wifiPacketsRx > 0 || wifiPacketsTx > 0 || mobileActiveTime > 0 || mobileActiveCount > 0) {
                    dumpLine(pw, uid, category, NETWORK_DATA, Long.valueOf(mobileBytesRx), Long.valueOf(mobileBytesTx), Long.valueOf(wifiBytesRx), Long.valueOf(wifiBytesTx), Long.valueOf(mobilePacketsRx), Long.valueOf(mobilePacketsTx), Long.valueOf(wifiPacketsRx), Long.valueOf(wifiPacketsTx), Long.valueOf(mobileActiveTime), Integer.valueOf(mobileActiveCount));
                }
                long fullWifiLockOnTime = u.getFullWifiLockTime(rawRealtime, which);
                long wifiScanTime = u.getWifiScanTime(rawRealtime, which);
                int wifiScanCount = u.getWifiScanCount(which);
                long uidWifiRunningTime = u.getWifiRunningTime(rawRealtime, which);
                long uidWifiIdleTimeMs = u.getWifiControllerActivity(0, which);
                long uidWifiRxTimeMs = u.getWifiControllerActivity(1, which);
                long uidWifiTxTimeMs = u.getWifiControllerActivity(2, which);
                if (!(fullWifiLockOnTime == 0 && wifiScanTime == 0 && wifiScanCount == 0 && uidWifiRunningTime == 0 && uidWifiIdleTimeMs == 0 && uidWifiRxTimeMs == 0 && uidWifiTxTimeMs == 0)) {
                    dumpLine(pw, uid, category, WIFI_DATA, Long.valueOf(fullWifiLockOnTime), Long.valueOf(wifiScanTime), Long.valueOf(uidWifiRunningTime), Integer.valueOf(wifiScanCount), Long.valueOf(uidWifiIdleTimeMs), Long.valueOf(uidWifiRxTimeMs), Long.valueOf(uidWifiTxTimeMs));
                }
                if (u.hasUserActivity()) {
                    args = new Object[3];
                    boolean hasData = false;
                    for (i = 0; i < 3; i++) {
                        int val = u.getUserActivityCount(i, which);
                        args[i] = Integer.valueOf(val);
                        if (val != 0) {
                            hasData = true;
                        }
                    }
                    if (hasData) {
                        dumpLine(pw, uid, category, USER_ACTIVITY_DATA, args);
                    }
                }
                wakelocks = u.getWakelockStats();
                Slog.d("BatteryStat", "Wakelocks size=" + wakelocks.size());
                for (iw = wakelocks.size() - 1; iw >= 0; iw--) {
                    wl = (Wakelock) wakelocks.valueAt(iw);
                    String linePrefix = ProxyInfo.LOCAL_EXCL_LIST;
                    sb.setLength(0);
                    linePrefix = printWakeLockCheckin(sb, wl.getWakeTime(2), rawRealtime, "w", which, printWakeLockCheckin(sb, wl.getWakeTime(0), rawRealtime, TtmlUtils.TAG_P, which, printWakeLockCheckin(sb, wl.getWakeTime(1), rawRealtime, FullBackup.DATA_TREE_TOKEN, which, linePrefix)));
                    if (sb.length() > 0) {
                        String name = (String) wakelocks.keyAt(iw);
                        if (name == null) {
                            Slog.d("BatteryStats", "Wakelocks size=" + wakelocks.size() + " sb=" + sb.toString());
                        }
                        if (name.indexOf(44) >= 0) {
                            name = name.replace(',', '_');
                        }
                        dumpLine(pw, uid, category, WAKELOCK_DATA, name, sb.toString());
                    }
                }
                ArrayMap<String, ? extends Timer> syncs = u.getSyncStats();
                for (int isy = syncs.size() - 1; isy >= 0; isy--) {
                    timer = (Timer) syncs.valueAt(isy);
                    totalTime = (timer.getTotalTimeLocked(rawRealtime, which) + 500) / 1000;
                    count = timer.getCountLocked(which);
                    if (totalTime != 0) {
                        dumpLine(pw, uid, category, SYNC_DATA, syncs.keyAt(isy), Long.valueOf(totalTime), Integer.valueOf(count));
                    }
                }
                ArrayMap<String, ? extends Timer> jobs = u.getJobStats();
                for (int ij = jobs.size() - 1; ij >= 0; ij--) {
                    timer = (Timer) jobs.valueAt(ij);
                    totalTime = (timer.getTotalTimeLocked(rawRealtime, which) + 500) / 1000;
                    count = timer.getCountLocked(which);
                    if (totalTime != 0) {
                        dumpLine(pw, uid, category, JOB_DATA, jobs.keyAt(ij), Long.valueOf(totalTime), Integer.valueOf(count));
                    }
                }
                dumpTimer(pw, uid, category, FLASHLIGHT_DATA, u.getFlashlightTurnedOnTimer(), rawRealtime, which);
                dumpTimer(pw, uid, category, CAMERA_DATA, u.getCameraTurnedOnTimer(), rawRealtime, which);
                dumpTimer(pw, uid, category, VIDEO_DATA, u.getVideoTurnedOnTimer(), rawRealtime, which);
                dumpTimer(pw, uid, category, AUDIO_DATA, u.getAudioTurnedOnTimer(), rawRealtime, which);
                SparseArray<? extends Sensor> sensors = u.getSensorStats();
                int NSE = sensors.size();
                for (int ise = 0; ise < NSE; ise++) {
                    Sensor se = (Sensor) sensors.valueAt(ise);
                    int sensorNumber = sensors.keyAt(ise);
                    timer = se.getSensorTime();
                    if (timer != null) {
                        totalTime = (timer.getTotalTimeLocked(rawRealtime, which) + 500) / 1000;
                        count = timer.getCountLocked(which);
                        if (totalTime != 0) {
                            dumpLine(pw, uid, category, SENSOR_DATA, Integer.valueOf(sensorNumber), Long.valueOf(totalTime), Integer.valueOf(count));
                        }
                    }
                }
                dumpTimer(pw, uid, category, VIBRATOR_DATA, u.getVibratorOnTimer(), rawRealtime, which);
                dumpTimer(pw, uid, category, FOREGROUND_DATA, u.getForegroundActivityTimer(), rawRealtime, which);
                Object[] stateTimes = new Object[3];
                long totalStateTime = 0;
                for (int ips = 0; ips < 3; ips++) {
                    totalStateTime += u.getProcessStateTime(ips, rawRealtime, which);
                    stateTimes[ips] = Long.valueOf((500 + totalStateTime) / 1000);
                }
                if (totalStateTime > 0) {
                    dumpLine(pw, uid, category, STATE_TIME_DATA, stateTimes);
                }
                long userCpuTimeUs = u.getUserCpuTimeUs(which);
                long systemCpuTimeUs = u.getSystemCpuTimeUs(which);
                long powerCpuMaUs = u.getCpuPowerMaUs(which);
                if (userCpuTimeUs > 0 || systemCpuTimeUs > 0 || powerCpuMaUs > 0) {
                    dumpLine(pw, uid, category, CPU_DATA, Long.valueOf(userCpuTimeUs / 1000), Long.valueOf(systemCpuTimeUs / 1000), Long.valueOf(powerCpuMaUs / 1000));
                }
                ArrayMap<String, ? extends Proc> processStats = u.getProcessStats();
                for (int ipr = processStats.size() - 1; ipr >= 0; ipr--) {
                    Proc ps = (Proc) processStats.valueAt(ipr);
                    long userMillis = ps.getUserTime(which);
                    long systemMillis = ps.getSystemTime(which);
                    long foregroundMillis = ps.getForegroundTime(which);
                    starts = ps.getStarts(which);
                    int numCrashes = ps.getNumCrashes(which);
                    int numAnrs = ps.getNumAnrs(which);
                    if (userMillis != 0 || systemMillis != 0 || foregroundMillis != 0 || starts != 0 || numAnrs != 0 || numCrashes != 0) {
                        dumpLine(pw, uid, category, PROCESS_DATA, processStats.keyAt(ipr), Long.valueOf(userMillis), Long.valueOf(systemMillis), Long.valueOf(foregroundMillis), Integer.valueOf(starts), Integer.valueOf(numAnrs), Integer.valueOf(numCrashes));
                    }
                }
                ArrayMap<String, ? extends Pkg> packageStats = u.getPackageStats();
                for (int ipkg = packageStats.size() - 1; ipkg >= 0; ipkg--) {
                    Pkg ps2 = (Pkg) packageStats.valueAt(ipkg);
                    int wakeups = 0;
                    ArrayMap<String, ? extends Counter> alarms = ps2.getWakeupAlarmStats();
                    for (int iwa = alarms.size() - 1; iwa >= 0; iwa--) {
                        wakeups += ((Counter) alarms.valueAt(iwa)).getCountLocked(which);
                    }
                    ArrayMap<String, ? extends Serv> serviceStats = ps2.getServiceStats();
                    for (int isvc = serviceStats.size() - 1; isvc >= 0; isvc--) {
                        Serv ss = (Serv) serviceStats.valueAt(isvc);
                        long startTime = ss.getStartTime(batteryUptime, which);
                        starts = ss.getStarts(which);
                        int launches = ss.getLaunches(which);
                        if (startTime != 0 || starts != 0 || launches != 0) {
                            dumpLine(pw, uid, category, APK_DATA, Integer.valueOf(wakeups), packageStats.keyAt(ipkg), serviceStats.keyAt(isvc), Long.valueOf(startTime / 1000), Integer.valueOf(starts), Integer.valueOf(launches));
                        }
                    }
                }
            }
        }
    }

    private void printmAh(PrintWriter printer, double power) {
        printer.print(BatteryStatsHelper.makemAh(power));
    }

    private void printmAh(StringBuilder sb, double power) {
        sb.append(BatteryStatsHelper.makemAh(power));
    }

    public final void dumpLocked(Context context, PrintWriter pw, String prefix, int which, int reqUid) {
        dumpLocked(context, pw, prefix, which, reqUid, BatteryStatsHelper.checkWifiOnly(context));
    }

    public final void dumpLocked(Context context, PrintWriter pw, String prefix, int which, int reqUid, boolean wifiOnly) {
        int i;
        int iu;
        BatterySipper bs;
        long totalTime;
        Timer timer;
        long rawUptime = SystemClock.uptimeMillis() * 1000;
        long rawRealtime = SystemClock.elapsedRealtime() * 1000;
        long batteryUptime = getBatteryUptime(rawUptime);
        long whichBatteryUptime = computeBatteryUptime(rawUptime, which);
        long whichBatteryRealtime = computeBatteryRealtime(rawRealtime, which);
        long totalRealtime = computeRealtime(rawRealtime, which);
        long totalUptime = computeUptime(rawUptime, which);
        long whichBatteryScreenOffUptime = computeBatteryScreenOffUptime(rawUptime, which);
        long whichBatteryScreenOffRealtime = computeBatteryScreenOffRealtime(rawRealtime, which);
        long batteryTimeRemaining = computeBatteryTimeRemaining(rawRealtime);
        long chargeTimeRemaining = computeChargeTimeRemaining(rawRealtime);
        StringBuilder stringBuilder = new StringBuilder(128);
        SparseArray<? extends Uid> uidStats = getUidStats();
        int NU = uidStats.size();
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("rawRealtime = ");
        stringBuilder.append(rawRealtime / 1000);
        stringBuilder.append(" (ms) ");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Time on battery: ");
        formatTimeMs(stringBuilder, whichBatteryRealtime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(whichBatteryRealtime, totalRealtime));
        stringBuilder.append(") realtime, ");
        formatTimeMs(stringBuilder, whichBatteryUptime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(whichBatteryUptime, totalRealtime));
        stringBuilder.append(") uptime");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Time on battery screen off: ");
        formatTimeMs(stringBuilder, whichBatteryScreenOffRealtime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(whichBatteryScreenOffRealtime, totalRealtime));
        stringBuilder.append(") realtime, ");
        formatTimeMs(stringBuilder, whichBatteryScreenOffUptime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(whichBatteryScreenOffUptime, totalRealtime));
        stringBuilder.append(") uptime");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Total run time: ");
        formatTimeMs(stringBuilder, totalRealtime / 1000);
        stringBuilder.append("realtime, ");
        formatTimeMs(stringBuilder, totalUptime / 1000);
        stringBuilder.append("uptime");
        pw.println(stringBuilder.toString());
        if (batteryTimeRemaining >= 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Battery time remaining: ");
            formatTimeMs(stringBuilder, batteryTimeRemaining / 1000);
            pw.println(stringBuilder.toString());
        }
        if (chargeTimeRemaining >= 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Charge time remaining: ");
            formatTimeMs(stringBuilder, chargeTimeRemaining / 1000);
            pw.println(stringBuilder.toString());
        }
        pw.print("  Start clock time: ");
        pw.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getStartClockTime()).toString());
        long screenOnTime = getScreenOnTime(rawRealtime, which);
        long interactiveTime = getInteractiveTime(rawRealtime, which);
        long powerSaveModeEnabledTime = getPowerSaveModeEnabledTime(rawRealtime, which);
        long deviceIdleModeEnabledTime = getDeviceIdleModeEnabledTime(rawRealtime, which);
        long deviceIdlingTime = getDeviceIdlingTime(rawRealtime, which);
        long phoneOnTime = getPhoneOnTime(rawRealtime, which);
        long wifiRunningTime = getGlobalWifiRunningTime(rawRealtime, which);
        long wifiOnTime = getWifiOnTime(rawRealtime, which);
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Screen on: ");
        formatTimeMs(stringBuilder, screenOnTime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(screenOnTime, whichBatteryRealtime));
        stringBuilder.append(") ");
        stringBuilder.append(getScreenOnCount(which));
        stringBuilder.append("x, Interactive: ");
        formatTimeMs(stringBuilder, interactiveTime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(interactiveTime, whichBatteryRealtime));
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Screen brightnesses:");
        boolean didOne = false;
        for (i = 0; i < 5; i++) {
            long time = getScreenBrightnessTime(i, rawRealtime, which);
            if (time != 0) {
                stringBuilder.append("\n    ");
                stringBuilder.append(prefix);
                didOne = true;
                stringBuilder.append(SCREEN_BRIGHTNESS_NAMES[i]);
                stringBuilder.append(" ");
                formatTimeMs(stringBuilder, time / 1000);
                stringBuilder.append("(");
                stringBuilder.append(formatRatioLocked(time, screenOnTime));
                stringBuilder.append(")");
            }
        }
        if (!didOne) {
            stringBuilder.append(" (no activity)");
        }
        pw.println(stringBuilder.toString());
        if (powerSaveModeEnabledTime != 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Power save mode enabled: ");
            formatTimeMs(stringBuilder, powerSaveModeEnabledTime / 1000);
            stringBuilder.append("(");
            stringBuilder.append(formatRatioLocked(powerSaveModeEnabledTime, whichBatteryRealtime));
            stringBuilder.append(")");
            pw.println(stringBuilder.toString());
        }
        if (deviceIdlingTime != 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Device idling: ");
            formatTimeMs(stringBuilder, deviceIdlingTime / 1000);
            stringBuilder.append("(");
            stringBuilder.append(formatRatioLocked(deviceIdlingTime, whichBatteryRealtime));
            stringBuilder.append(") ");
            stringBuilder.append(getDeviceIdlingCount(which));
            stringBuilder.append("x");
            pw.println(stringBuilder.toString());
        }
        if (deviceIdleModeEnabledTime != 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Idle mode time: ");
            formatTimeMs(stringBuilder, deviceIdleModeEnabledTime / 1000);
            stringBuilder.append("(");
            stringBuilder.append(formatRatioLocked(deviceIdleModeEnabledTime, whichBatteryRealtime));
            stringBuilder.append(") ");
            stringBuilder.append(getDeviceIdleModeEnabledCount(which));
            stringBuilder.append("x");
            pw.println(stringBuilder.toString());
        }
        if (phoneOnTime != 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Active phone call: ");
            formatTimeMs(stringBuilder, phoneOnTime / 1000);
            stringBuilder.append("(");
            stringBuilder.append(formatRatioLocked(phoneOnTime, whichBatteryRealtime));
            stringBuilder.append(") ");
            stringBuilder.append(getPhoneOnCount(which));
            stringBuilder.append("x");
        }
        int connChanges = getNumConnectivityChange(which);
        if (connChanges != 0) {
            pw.print(prefix);
            pw.print("  Connectivity changes: ");
            pw.println(connChanges);
        }
        long fullWakeLockTimeTotalMicros = 0;
        long partialWakeLockTimeTotalMicros = 0;
        ArrayList<TimerEntry> timers = new ArrayList();
        for (iu = 0; iu < NU; iu++) {
            int iw;
            Uid u = (Uid) uidStats.valueAt(iu);
            ArrayMap<String, ? extends Wakelock> wakelocks = u.getWakelockStats();
            for (iw = wakelocks.size() - 1; iw >= 0; iw--) {
                Wakelock wl = (Wakelock) wakelocks.valueAt(iw);
                Timer fullWakeTimer = wl.getWakeTime(1);
                if (fullWakeTimer != null) {
                    fullWakeLockTimeTotalMicros += fullWakeTimer.getTotalTimeLocked(rawRealtime, which);
                }
                Timer partialWakeTimer = wl.getWakeTime(0);
                if (partialWakeTimer != null) {
                    long totalTimeMicros = partialWakeTimer.getTotalTimeLocked(rawRealtime, which);
                    if (totalTimeMicros > 0) {
                        if (reqUid < 0) {
                            timers.add(new TimerEntry((String) wakelocks.keyAt(iw), u.getUid(), partialWakeTimer, totalTimeMicros));
                        }
                        partialWakeLockTimeTotalMicros += totalTimeMicros;
                    }
                }
            }
        }
        long mobileRxTotalBytes = getNetworkActivityBytes(0, which);
        long mobileTxTotalBytes = getNetworkActivityBytes(1, which);
        long wifiRxTotalBytes = getNetworkActivityBytes(2, which);
        long wifiTxTotalBytes = getNetworkActivityBytes(3, which);
        long mobileRxTotalPackets = getNetworkActivityPackets(0, which);
        long mobileTxTotalPackets = getNetworkActivityPackets(1, which);
        long wifiRxTotalPackets = getNetworkActivityPackets(2, which);
        long wifiTxTotalPackets = getNetworkActivityPackets(3, which);
        if (fullWakeLockTimeTotalMicros != 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Total full wakelock time: ");
            formatTimeMsNoSpace(stringBuilder, (500 + fullWakeLockTimeTotalMicros) / 1000);
            pw.println(stringBuilder.toString());
        }
        if (partialWakeLockTimeTotalMicros != 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Total partial wakelock time: ");
            formatTimeMsNoSpace(stringBuilder, (500 + partialWakeLockTimeTotalMicros) / 1000);
            pw.println(stringBuilder.toString());
        }
        pw.print(prefix);
        pw.print("  Mobile total received: ");
        pw.print(formatBytesLocked(mobileRxTotalBytes));
        pw.print(", sent: ");
        pw.print(formatBytesLocked(mobileTxTotalBytes));
        pw.print(" (packets received ");
        pw.print(mobileRxTotalPackets);
        pw.print(", sent ");
        pw.print(mobileTxTotalPackets);
        pw.println(")");
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Phone signal levels:");
        didOne = false;
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            time = getPhoneSignalStrengthTime(i, rawRealtime, which);
            if (time != 0) {
                stringBuilder.append("\n    ");
                stringBuilder.append(prefix);
                didOne = true;
                stringBuilder.append(SignalStrength.SIGNAL_STRENGTH_NAMES[i]);
                stringBuilder.append(" ");
                formatTimeMs(stringBuilder, time / 1000);
                stringBuilder.append("(");
                stringBuilder.append(formatRatioLocked(time, whichBatteryRealtime));
                stringBuilder.append(") ");
                stringBuilder.append(getPhoneSignalStrengthCount(i, which));
                stringBuilder.append("x");
            }
        }
        if (!didOne) {
            stringBuilder.append(" (no activity)");
        }
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Signal scanning time: ");
        formatTimeMsNoSpace(stringBuilder, getPhoneSignalScanningTime(rawRealtime, which) / 1000);
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Radio types:");
        didOne = false;
        for (i = 0; i < 17; i++) {
            time = getPhoneDataConnectionTime(i, rawRealtime, which);
            if (time != 0) {
                stringBuilder.append("\n    ");
                stringBuilder.append(prefix);
                didOne = true;
                stringBuilder.append(DATA_CONNECTION_NAMES[i]);
                stringBuilder.append(" ");
                formatTimeMs(stringBuilder, time / 1000);
                stringBuilder.append("(");
                stringBuilder.append(formatRatioLocked(time, whichBatteryRealtime));
                stringBuilder.append(") ");
                stringBuilder.append(getPhoneDataConnectionCount(i, which));
                stringBuilder.append("x");
            }
        }
        if (!didOne) {
            stringBuilder.append(" (no activity)");
        }
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Mobile radio active time: ");
        long mobileActiveTime = getMobileRadioActiveTime(rawRealtime, which);
        formatTimeMs(stringBuilder, mobileActiveTime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(mobileActiveTime, whichBatteryRealtime));
        stringBuilder.append(") ");
        stringBuilder.append(getMobileRadioActiveCount(which));
        stringBuilder.append("x");
        pw.println(stringBuilder.toString());
        long mobileActiveUnknownTime = getMobileRadioActiveUnknownTime(which);
        if (mobileActiveUnknownTime != 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Mobile radio active unknown time: ");
            formatTimeMs(stringBuilder, mobileActiveUnknownTime / 1000);
            stringBuilder.append("(");
            stringBuilder.append(formatRatioLocked(mobileActiveUnknownTime, whichBatteryRealtime));
            stringBuilder.append(") ");
            stringBuilder.append(getMobileRadioActiveUnknownCount(which));
            stringBuilder.append("x");
            pw.println(stringBuilder.toString());
        }
        long mobileActiveAdjustedTime = getMobileRadioActiveAdjustedTime(which);
        if (mobileActiveAdjustedTime != 0) {
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("  Mobile radio active adjusted time: ");
            formatTimeMs(stringBuilder, mobileActiveAdjustedTime / 1000);
            stringBuilder.append("(");
            stringBuilder.append(formatRatioLocked(mobileActiveAdjustedTime, whichBatteryRealtime));
            stringBuilder.append(")");
            pw.println(stringBuilder.toString());
        }
        pw.print(prefix);
        pw.print("  Wi-Fi total received: ");
        pw.print(formatBytesLocked(wifiRxTotalBytes));
        pw.print(", sent: ");
        pw.print(formatBytesLocked(wifiTxTotalBytes));
        pw.print(" (packets received ");
        pw.print(wifiRxTotalPackets);
        pw.print(", sent ");
        pw.print(wifiTxTotalPackets);
        pw.println(")");
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Wifi on: ");
        formatTimeMs(stringBuilder, wifiOnTime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(wifiOnTime, whichBatteryRealtime));
        stringBuilder.append("), Wifi running: ");
        formatTimeMs(stringBuilder, wifiRunningTime / 1000);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(wifiRunningTime, whichBatteryRealtime));
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Wifi states:");
        didOne = false;
        for (i = 0; i < 8; i++) {
            time = getWifiStateTime(i, rawRealtime, which);
            if (time != 0) {
                stringBuilder.append("\n    ");
                didOne = true;
                stringBuilder.append(WIFI_STATE_NAMES[i]);
                stringBuilder.append(" ");
                formatTimeMs(stringBuilder, time / 1000);
                stringBuilder.append("(");
                stringBuilder.append(formatRatioLocked(time, whichBatteryRealtime));
                stringBuilder.append(") ");
                stringBuilder.append(getWifiStateCount(i, which));
                stringBuilder.append("x");
            }
        }
        if (!didOne) {
            stringBuilder.append(" (no activity)");
        }
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Wifi supplicant states:");
        didOne = false;
        for (i = 0; i < 13; i++) {
            time = getWifiSupplStateTime(i, rawRealtime, which);
            if (time != 0) {
                stringBuilder.append("\n    ");
                didOne = true;
                stringBuilder.append(WIFI_SUPPL_STATE_NAMES[i]);
                stringBuilder.append(" ");
                formatTimeMs(stringBuilder, time / 1000);
                stringBuilder.append("(");
                stringBuilder.append(formatRatioLocked(time, whichBatteryRealtime));
                stringBuilder.append(") ");
                stringBuilder.append(getWifiSupplStateCount(i, which));
                stringBuilder.append("x");
            }
        }
        if (!didOne) {
            stringBuilder.append(" (no activity)");
        }
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Wifi signal levels:");
        didOne = false;
        for (i = 0; i < 5; i++) {
            time = getWifiSignalStrengthTime(i, rawRealtime, which);
            if (time != 0) {
                stringBuilder.append("\n    ");
                stringBuilder.append(prefix);
                didOne = true;
                stringBuilder.append("level(");
                stringBuilder.append(i);
                stringBuilder.append(") ");
                formatTimeMs(stringBuilder, time / 1000);
                stringBuilder.append("(");
                stringBuilder.append(formatRatioLocked(time, whichBatteryRealtime));
                stringBuilder.append(") ");
                stringBuilder.append(getWifiSignalStrengthCount(i, which));
                stringBuilder.append("x");
            }
        }
        if (!didOne) {
            stringBuilder.append(" (no activity)");
        }
        pw.println(stringBuilder.toString());
        long wifiIdleTimeMs = getWifiControllerActivity(0, which);
        long wifiRxTimeMs = getWifiControllerActivity(1, which);
        long wifiTxTimeMs = getWifiControllerActivity(2, which);
        long wifiPowerDrainMaMs = getWifiControllerActivity(3, which);
        long wifiTotalTimeMs = (wifiIdleTimeMs + wifiRxTimeMs) + wifiTxTimeMs;
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  WiFi Idle time: ");
        formatTimeMs(stringBuilder, wifiIdleTimeMs);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(wifiIdleTimeMs, wifiTotalTimeMs));
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  WiFi Rx time:   ");
        formatTimeMs(stringBuilder, wifiRxTimeMs);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(wifiRxTimeMs, wifiTotalTimeMs));
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  WiFi Tx time:   ");
        formatTimeMs(stringBuilder, wifiTxTimeMs);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(wifiTxTimeMs, wifiTotalTimeMs));
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  WiFi Power drain: ").append(BatteryStatsHelper.makemAh(((double) wifiPowerDrainMaMs) / 3600000.0d));
        stringBuilder.append("mAh");
        pw.println(stringBuilder.toString());
        long bluetoothIdleTimeMs = getBluetoothControllerActivity(0, which);
        long bluetoothRxTimeMs = getBluetoothControllerActivity(1, which);
        long bluetoothTxTimeMs = getBluetoothControllerActivity(2, which);
        long bluetoothTotalTimeMs = (bluetoothIdleTimeMs + bluetoothRxTimeMs) + bluetoothTxTimeMs;
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Bluetooth Idle time: ");
        formatTimeMs(stringBuilder, bluetoothIdleTimeMs);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(bluetoothIdleTimeMs, bluetoothTotalTimeMs));
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Bluetooth Rx time:   ");
        formatTimeMs(stringBuilder, bluetoothRxTimeMs);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(bluetoothRxTimeMs, bluetoothTotalTimeMs));
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Bluetooth Tx time:   ");
        formatTimeMs(stringBuilder, bluetoothTxTimeMs);
        stringBuilder.append("(");
        stringBuilder.append(formatRatioLocked(bluetoothTxTimeMs, bluetoothTotalTimeMs));
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(prefix);
        stringBuilder.append("  Bluetooth Power drain: ").append(BatteryStatsHelper.makemAh(((double) getBluetoothControllerActivity(3, which)) / 3600000.0d));
        stringBuilder.append("mAh");
        pw.println(stringBuilder.toString());
        pw.println();
        if (which == 2) {
            if (getIsOnBattery()) {
                pw.print(prefix);
                pw.println("  Device is currently unplugged");
                pw.print(prefix);
                pw.print("    Discharge cycle start level: ");
                pw.println(getDischargeStartLevel());
                pw.print(prefix);
                pw.print("    Discharge cycle current level: ");
                pw.println(getDischargeCurrentLevel());
            } else {
                pw.print(prefix);
                pw.println("  Device is currently plugged into power");
                pw.print(prefix);
                pw.print("    Last discharge cycle start level: ");
                pw.println(getDischargeStartLevel());
                pw.print(prefix);
                pw.print("    Last discharge cycle end level: ");
                pw.println(getDischargeCurrentLevel());
            }
            pw.print(prefix);
            pw.print("    Amount discharged while screen on: ");
            pw.println(getDischargeAmountScreenOn());
            pw.print(prefix);
            pw.print("    Amount discharged while screen off: ");
            pw.println(getDischargeAmountScreenOff());
            pw.println(" ");
        } else {
            pw.print(prefix);
            pw.println("  Device battery use since last full charge");
            pw.print(prefix);
            pw.print("    Amount discharged (lower bound): ");
            pw.println(getLowDischargeAmountSinceCharge());
            pw.print(prefix);
            pw.print("    Amount discharged (upper bound): ");
            pw.println(getHighDischargeAmountSinceCharge());
            pw.print(prefix);
            pw.print("    Amount discharged while screen on: ");
            pw.println(getDischargeAmountScreenOnSinceCharge());
            pw.print(prefix);
            pw.print("    Amount discharged while screen off: ");
            pw.println(getDischargeAmountScreenOffSinceCharge());
            pw.println();
        }
        BatteryStatsHelper batteryStatsHelper = new BatteryStatsHelper(context, false, wifiOnly);
        batteryStatsHelper.create(this);
        batteryStatsHelper.refreshStats(which, -1);
        List<BatterySipper> sippers = batteryStatsHelper.getUsageList();
        if (sippers != null && sippers.size() > 0) {
            pw.print(prefix);
            pw.println("  Estimated power use (mAh):");
            pw.print(prefix);
            pw.print("    Capacity: ");
            printmAh(pw, batteryStatsHelper.getPowerProfile().getBatteryCapacity());
            pw.print(", Computed drain: ");
            printmAh(pw, batteryStatsHelper.getComputedPower());
            pw.print(", actual drain: ");
            printmAh(pw, batteryStatsHelper.getMinDrainedPower());
            if (batteryStatsHelper.getMinDrainedPower() != batteryStatsHelper.getMaxDrainedPower()) {
                pw.print("-");
                printmAh(pw, batteryStatsHelper.getMaxDrainedPower());
            }
            pw.println();
            for (i = 0; i < sippers.size(); i++) {
                bs = (BatterySipper) sippers.get(i);
                pw.print(prefix);
                switch (AnonymousClass2.$SwitchMap$com$android$internal$os$BatterySipper$DrainType[bs.drainType.ordinal()]) {
                    case 1:
                        pw.print("    Idle: ");
                        break;
                    case 2:
                        pw.print("    Cell standby: ");
                        break;
                    case 3:
                        pw.print("    Phone calls: ");
                        break;
                    case 4:
                        pw.print("    Wifi: ");
                        break;
                    case 5:
                        pw.print("    Bluetooth: ");
                        break;
                    case 6:
                        pw.print("    Screen: ");
                        break;
                    case 7:
                        pw.print("    Flashlight: ");
                        break;
                    case 8:
                        pw.print("    Uid ");
                        UserHandle.formatUid(pw, bs.uidObj.getUid());
                        pw.print(": ");
                        break;
                    case 9:
                        pw.print("    User ");
                        pw.print(bs.userId);
                        pw.print(": ");
                        break;
                    case 10:
                        pw.print("    Unaccounted: ");
                        break;
                    case 11:
                        pw.print("    Over-counted: ");
                        break;
                    case 12:
                        pw.print("    Camera: ");
                        break;
                    default:
                        pw.print("    ???: ");
                        break;
                }
                printmAh(pw, bs.totalPowerMah);
                if (bs.usagePowerMah != bs.totalPowerMah) {
                    pw.print(" (");
                    if (bs.usagePowerMah != 0.0d) {
                        pw.print(" usage=");
                        printmAh(pw, bs.usagePowerMah);
                    }
                    if (bs.cpuPowerMah != 0.0d) {
                        pw.print(" cpu=");
                        printmAh(pw, bs.cpuPowerMah);
                    }
                    if (bs.wakeLockPowerMah != 0.0d) {
                        pw.print(" wake=");
                        printmAh(pw, bs.wakeLockPowerMah);
                    }
                    if (bs.mobileRadioPowerMah != 0.0d) {
                        pw.print(" radio=");
                        printmAh(pw, bs.mobileRadioPowerMah);
                    }
                    if (bs.wifiPowerMah != 0.0d) {
                        pw.print(" wifi=");
                        printmAh(pw, bs.wifiPowerMah);
                    }
                    if (bs.gpsPowerMah != 0.0d) {
                        pw.print(" gps=");
                        printmAh(pw, bs.gpsPowerMah);
                    }
                    if (bs.sensorPowerMah != 0.0d) {
                        pw.print(" sensor=");
                        printmAh(pw, bs.sensorPowerMah);
                    }
                    if (bs.cameraPowerMah != 0.0d) {
                        pw.print(" camera=");
                        printmAh(pw, bs.cameraPowerMah);
                    }
                    if (bs.flashlightPowerMah != 0.0d) {
                        pw.print(" flash=");
                        printmAh(pw, bs.flashlightPowerMah);
                    }
                    pw.print(" )");
                }
                pw.println();
            }
            pw.println();
        }
        sippers = batteryStatsHelper.getMobilemsppList();
        if (sippers != null && sippers.size() > 0) {
            pw.print(prefix);
            pw.println("  Per-app mobile ms per packet:");
            totalTime = 0;
            for (i = 0; i < sippers.size(); i++) {
                bs = (BatterySipper) sippers.get(i);
                stringBuilder.setLength(0);
                stringBuilder.append(prefix);
                stringBuilder.append("    Uid ");
                UserHandle.formatUid(stringBuilder, bs.uidObj.getUid());
                stringBuilder.append(": ");
                stringBuilder.append(BatteryStatsHelper.makemAh(bs.mobilemspp));
                stringBuilder.append(" (");
                stringBuilder.append(bs.mobileRxPackets + bs.mobileTxPackets);
                stringBuilder.append(" packets over ");
                formatTimeMsNoSpace(stringBuilder, bs.mobileActive);
                stringBuilder.append(") ");
                stringBuilder.append(bs.mobileActiveCount);
                stringBuilder.append("x");
                pw.println(stringBuilder.toString());
                totalTime += bs.mobileActive;
            }
            stringBuilder.setLength(0);
            stringBuilder.append(prefix);
            stringBuilder.append("    TOTAL TIME: ");
            formatTimeMs(stringBuilder, totalTime);
            stringBuilder.append("(");
            stringBuilder.append(formatRatioLocked(totalTime, whichBatteryRealtime));
            stringBuilder.append(")");
            pw.println(stringBuilder.toString());
            pw.println();
        }
        Comparator<TimerEntry> anonymousClass1 = new Comparator<TimerEntry>() {
            public int compare(TimerEntry lhs, TimerEntry rhs) {
                long lhsTime = lhs.mTime;
                long rhsTime = rhs.mTime;
                if (lhsTime < rhsTime) {
                    return 1;
                }
                if (lhsTime > rhsTime) {
                    return -1;
                }
                return 0;
            }
        };
        if (reqUid < 0) {
            TimerEntry timer2;
            Map<String, ? extends Timer> kernelWakelocks = getKernelWakelockStats();
            if (kernelWakelocks.size() > 0) {
                ArrayList<TimerEntry> ktimers = new ArrayList();
                for (Entry<String, ? extends Timer> ent : kernelWakelocks.entrySet()) {
                    timer = (Timer) ent.getValue();
                    long totalTimeMillis = computeWakeLock(timer, rawRealtime, which);
                    if (totalTimeMillis > 0) {
                        ktimers.add(new TimerEntry((String) ent.getKey(), 0, timer, totalTimeMillis));
                    }
                }
                if (ktimers.size() > 0) {
                    Collections.sort(ktimers, anonymousClass1);
                    pw.print(prefix);
                    pw.println("  All kernel wake locks:");
                    for (i = 0; i < ktimers.size(); i++) {
                        timer2 = (TimerEntry) ktimers.get(i);
                        stringBuilder.setLength(0);
                        stringBuilder.append(prefix);
                        stringBuilder.append("  Kernel Wake lock ");
                        stringBuilder.append(timer2.mName);
                        if (!printWakeLock(stringBuilder, timer2.mTimer, rawRealtime, null, which, ": ").equals(": ")) {
                            stringBuilder.append(" realtime");
                            pw.println(stringBuilder.toString());
                        }
                    }
                    pw.println();
                }
            }
            if (timers.size() > 0) {
                Collections.sort(timers, anonymousClass1);
                pw.print(prefix);
                pw.println("  All partial wake locks:");
                for (i = 0; i < timers.size(); i++) {
                    timer2 = (TimerEntry) timers.get(i);
                    stringBuilder.setLength(0);
                    stringBuilder.append("  Wake lock ");
                    UserHandle.formatUid(stringBuilder, timer2.mId);
                    stringBuilder.append(" ");
                    stringBuilder.append(timer2.mName);
                    printWakeLock(stringBuilder, timer2.mTimer, rawRealtime, null, which, ": ");
                    stringBuilder.append(" realtime");
                    pw.println(stringBuilder.toString());
                }
                timers.clear();
                pw.println();
            }
            Map<String, ? extends Timer> wakeupReasons = getWakeupReasonStats();
            if (wakeupReasons.size() > 0) {
                pw.print(prefix);
                pw.println("  All wakeup reasons:");
                ArrayList<TimerEntry> reasons = new ArrayList();
                for (Entry<String, ? extends Timer> ent2 : wakeupReasons.entrySet()) {
                    timer = (Timer) ent2.getValue();
                    reasons.add(new TimerEntry((String) ent2.getKey(), 0, timer, (long) timer.getCountLocked(which)));
                }
                Collections.sort(reasons, anonymousClass1);
                for (i = 0; i < reasons.size(); i++) {
                    timer2 = (TimerEntry) reasons.get(i);
                    String str = ": ";
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("  Wakeup reason ");
                    stringBuilder.append(timer2.mName);
                    printWakeLock(stringBuilder, timer2.mTimer, rawRealtime, null, which, ": ");
                    stringBuilder.append(" realtime");
                    pw.println(stringBuilder.toString());
                }
                pw.println();
            }
        }
        for (iu = 0; iu < NU; iu++) {
            int uid = uidStats.keyAt(iu);
            if (reqUid < 0 || uid == reqUid || uid == 1000) {
                int count;
                int starts;
                u = (Uid) uidStats.valueAt(iu);
                pw.print(prefix);
                pw.print("  ");
                UserHandle.formatUid(pw, uid);
                pw.println(":");
                boolean uidActivity = false;
                long mobileRxBytes = u.getNetworkActivityBytes(0, which);
                long mobileTxBytes = u.getNetworkActivityBytes(1, which);
                long wifiRxBytes = u.getNetworkActivityBytes(2, which);
                long wifiTxBytes = u.getNetworkActivityBytes(3, which);
                long mobileRxPackets = u.getNetworkActivityPackets(0, which);
                long mobileTxPackets = u.getNetworkActivityPackets(1, which);
                long uidMobileActiveTime = u.getMobileRadioActiveTime(which);
                int uidMobileActiveCount = u.getMobileRadioActiveCount(which);
                long wifiRxPackets = u.getNetworkActivityPackets(2, which);
                long wifiTxPackets = u.getNetworkActivityPackets(3, which);
                long fullWifiLockOnTime = u.getFullWifiLockTime(rawRealtime, which);
                long wifiScanTime = u.getWifiScanTime(rawRealtime, which);
                int wifiScanCount = u.getWifiScanCount(which);
                long uidWifiRunningTime = u.getWifiRunningTime(rawRealtime, which);
                if (mobileRxBytes > 0 || mobileTxBytes > 0 || mobileRxPackets > 0 || mobileTxPackets > 0) {
                    pw.print(prefix);
                    pw.print("    Mobile network: ");
                    pw.print(formatBytesLocked(mobileRxBytes));
                    pw.print(" received, ");
                    pw.print(formatBytesLocked(mobileTxBytes));
                    pw.print(" sent (packets ");
                    pw.print(mobileRxPackets);
                    pw.print(" received, ");
                    pw.print(mobileTxPackets);
                    pw.println(" sent)");
                }
                if (uidMobileActiveTime > 0 || uidMobileActiveCount > 0) {
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Mobile radio active: ");
                    formatTimeMs(stringBuilder, uidMobileActiveTime / 1000);
                    stringBuilder.append("(");
                    stringBuilder.append(formatRatioLocked(uidMobileActiveTime, mobileActiveTime));
                    stringBuilder.append(") ");
                    stringBuilder.append(uidMobileActiveCount);
                    stringBuilder.append("x");
                    long packets = mobileRxPackets + mobileTxPackets;
                    if (packets == 0) {
                        packets = 1;
                    }
                    stringBuilder.append(" @ ");
                    stringBuilder.append(BatteryStatsHelper.makemAh(((double) (uidMobileActiveTime / 1000)) / ((double) packets)));
                    stringBuilder.append(" mspp");
                    pw.println(stringBuilder.toString());
                }
                if (wifiRxBytes > 0 || wifiTxBytes > 0 || wifiRxPackets > 0 || wifiTxPackets > 0) {
                    pw.print(prefix);
                    pw.print("    Wi-Fi network: ");
                    pw.print(formatBytesLocked(wifiRxBytes));
                    pw.print(" received, ");
                    pw.print(formatBytesLocked(wifiTxBytes));
                    pw.print(" sent (packets ");
                    pw.print(wifiRxPackets);
                    pw.print(" received, ");
                    pw.print(wifiTxPackets);
                    pw.println(" sent)");
                }
                if (!(fullWifiLockOnTime == 0 && wifiScanTime == 0 && wifiScanCount == 0 && uidWifiRunningTime == 0)) {
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Wifi Running: ");
                    formatTimeMs(stringBuilder, uidWifiRunningTime / 1000);
                    stringBuilder.append("(");
                    stringBuilder.append(formatRatioLocked(uidWifiRunningTime, whichBatteryRealtime));
                    stringBuilder.append(")\n");
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Full Wifi Lock: ");
                    formatTimeMs(stringBuilder, fullWifiLockOnTime / 1000);
                    stringBuilder.append("(");
                    stringBuilder.append(formatRatioLocked(fullWifiLockOnTime, whichBatteryRealtime));
                    stringBuilder.append(")\n");
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Wifi Scan: ");
                    formatTimeMs(stringBuilder, wifiScanTime / 1000);
                    stringBuilder.append("(");
                    stringBuilder.append(formatRatioLocked(wifiScanTime, whichBatteryRealtime));
                    stringBuilder.append(") ");
                    stringBuilder.append(wifiScanCount);
                    stringBuilder.append("x");
                    pw.println(stringBuilder.toString());
                }
                long uidWifiIdleTimeMs = u.getWifiControllerActivity(0, which);
                long uidWifiRxTimeMs = u.getWifiControllerActivity(1, which);
                long uidWifiTxTimeMs = u.getWifiControllerActivity(2, which);
                long uidWifiTotalTimeMs = (uidWifiIdleTimeMs + uidWifiRxTimeMs) + uidWifiTxTimeMs;
                if (uidWifiTotalTimeMs > 0) {
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix).append("    WiFi Idle time: ");
                    formatTimeMs(stringBuilder, uidWifiIdleTimeMs);
                    stringBuilder.append("(").append(formatRatioLocked(uidWifiIdleTimeMs, uidWifiTotalTimeMs)).append(")\n");
                    stringBuilder.append(prefix).append("    WiFi Rx time:   ");
                    formatTimeMs(stringBuilder, uidWifiRxTimeMs);
                    stringBuilder.append("(").append(formatRatioLocked(uidWifiRxTimeMs, uidWifiTotalTimeMs)).append(")\n");
                    stringBuilder.append(prefix).append("    WiFi Tx time:   ");
                    formatTimeMs(stringBuilder, uidWifiTxTimeMs);
                    stringBuilder.append("(").append(formatRatioLocked(uidWifiTxTimeMs, uidWifiTotalTimeMs)).append(")");
                    pw.println(stringBuilder.toString());
                }
                if (u.hasUserActivity()) {
                    boolean hasData = false;
                    for (i = 0; i < 3; i++) {
                        int val = u.getUserActivityCount(i, which);
                        if (val != 0) {
                            if (hasData) {
                                stringBuilder.append(", ");
                            } else {
                                stringBuilder.setLength(0);
                                stringBuilder.append("    User activity: ");
                                hasData = true;
                            }
                            stringBuilder.append(val);
                            stringBuilder.append(" ");
                            stringBuilder.append(Uid.USER_ACTIVITY_TYPES[i]);
                        }
                    }
                    if (hasData) {
                        pw.println(stringBuilder.toString());
                    }
                }
                wakelocks = u.getWakelockStats();
                long totalFullWakelock = 0;
                long totalPartialWakelock = 0;
                long totalWindowWakelock = 0;
                long totalDrawWakelock = 0;
                int countWakelock = 0;
                for (iw = wakelocks.size() - 1; iw >= 0; iw--) {
                    wl = (Wakelock) wakelocks.valueAt(iw);
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Wake lock ");
                    stringBuilder.append((String) wakelocks.keyAt(iw));
                    str = printWakeLock(stringBuilder, wl.getWakeTime(18), rawRealtime, "draw", which, printWakeLock(stringBuilder, wl.getWakeTime(2), rawRealtime, Context.WINDOW_SERVICE, which, printWakeLock(stringBuilder, wl.getWakeTime(0), rawRealtime, "partial", which, printWakeLock(stringBuilder, wl.getWakeTime(1), rawRealtime, "full", which, ": "))));
                    stringBuilder.append(" realtime");
                    pw.println(stringBuilder.toString());
                    uidActivity = true;
                    countWakelock++;
                    totalFullWakelock += computeWakeLock(wl.getWakeTime(1), rawRealtime, which);
                    totalPartialWakelock += computeWakeLock(wl.getWakeTime(0), rawRealtime, which);
                    totalWindowWakelock += computeWakeLock(wl.getWakeTime(2), rawRealtime, which);
                    totalDrawWakelock += computeWakeLock(wl.getWakeTime(18), rawRealtime, which);
                }
                if (countWakelock > 1 && !(totalFullWakelock == 0 && totalPartialWakelock == 0 && totalWindowWakelock == 0)) {
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("    TOTAL wake: ");
                    boolean needComma = false;
                    if (totalFullWakelock != 0) {
                        needComma = true;
                        formatTimeMs(stringBuilder, totalFullWakelock);
                        stringBuilder.append("full");
                    }
                    if (totalPartialWakelock != 0) {
                        if (needComma) {
                            stringBuilder.append(", ");
                        }
                        needComma = true;
                        formatTimeMs(stringBuilder, totalPartialWakelock);
                        stringBuilder.append("partial");
                    }
                    if (totalWindowWakelock != 0) {
                        if (needComma) {
                            stringBuilder.append(", ");
                        }
                        needComma = true;
                        formatTimeMs(stringBuilder, totalWindowWakelock);
                        stringBuilder.append(Context.WINDOW_SERVICE);
                    }
                    if (totalDrawWakelock != 0) {
                        if (needComma) {
                            stringBuilder.append(",");
                        }
                        formatTimeMs(stringBuilder, totalDrawWakelock);
                        stringBuilder.append("draw");
                    }
                    stringBuilder.append(" realtime");
                    pw.println(stringBuilder.toString());
                }
                ArrayMap<String, ? extends Timer> syncs = u.getSyncStats();
                for (int isy = syncs.size() - 1; isy >= 0; isy--) {
                    timer = (Timer) syncs.valueAt(isy);
                    totalTime = (timer.getTotalTimeLocked(rawRealtime, which) + 500) / 1000;
                    count = timer.getCountLocked(which);
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Sync ");
                    stringBuilder.append((String) syncs.keyAt(isy));
                    stringBuilder.append(": ");
                    if (totalTime != 0) {
                        formatTimeMs(stringBuilder, totalTime);
                        stringBuilder.append("realtime (");
                        stringBuilder.append(count);
                        stringBuilder.append(" times)");
                    } else {
                        stringBuilder.append("(not used)");
                    }
                    pw.println(stringBuilder.toString());
                    uidActivity = true;
                }
                ArrayMap<String, ? extends Timer> jobs = u.getJobStats();
                for (int ij = jobs.size() - 1; ij >= 0; ij--) {
                    timer = (Timer) jobs.valueAt(ij);
                    totalTime = (timer.getTotalTimeLocked(rawRealtime, which) + 500) / 1000;
                    count = timer.getCountLocked(which);
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Job ");
                    stringBuilder.append((String) jobs.keyAt(ij));
                    stringBuilder.append(": ");
                    if (totalTime != 0) {
                        formatTimeMs(stringBuilder, totalTime);
                        stringBuilder.append("realtime (");
                        stringBuilder.append(count);
                        stringBuilder.append(" times)");
                    } else {
                        stringBuilder.append("(not used)");
                    }
                    pw.println(stringBuilder.toString());
                    uidActivity = true;
                }
                uidActivity = (((uidActivity | printTimer(pw, stringBuilder, u.getFlashlightTurnedOnTimer(), rawRealtime, which, prefix, "Flashlight")) | printTimer(pw, stringBuilder, u.getCameraTurnedOnTimer(), rawRealtime, which, prefix, "Camera")) | printTimer(pw, stringBuilder, u.getVideoTurnedOnTimer(), rawRealtime, which, prefix, "Video")) | printTimer(pw, stringBuilder, u.getAudioTurnedOnTimer(), rawRealtime, which, prefix, "Audio");
                SparseArray<? extends Sensor> sensors = u.getSensorStats();
                int NSE = sensors.size();
                for (int ise = 0; ise < NSE; ise++) {
                    Sensor se = (Sensor) sensors.valueAt(ise);
                    int sensorNumber = sensors.keyAt(ise);
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Sensor ");
                    int handle = se.getHandle();
                    if (handle == -10000) {
                        stringBuilder.append("GPS");
                    } else {
                        stringBuilder.append(handle);
                    }
                    stringBuilder.append(": ");
                    timer = se.getSensorTime();
                    if (timer != null) {
                        totalTime = (timer.getTotalTimeLocked(rawRealtime, which) + 500) / 1000;
                        count = timer.getCountLocked(which);
                        if (totalTime != 0) {
                            formatTimeMs(stringBuilder, totalTime);
                            stringBuilder.append("realtime (");
                            stringBuilder.append(count);
                            stringBuilder.append(" times)");
                        } else {
                            stringBuilder.append("(not used)");
                        }
                    } else {
                        stringBuilder.append("(not used)");
                    }
                    pw.println(stringBuilder.toString());
                    uidActivity = true;
                }
                uidActivity = (uidActivity | printTimer(pw, stringBuilder, u.getVibratorOnTimer(), rawRealtime, which, prefix, "Vibrator")) | printTimer(pw, stringBuilder, u.getForegroundActivityTimer(), rawRealtime, which, prefix, "Foreground activities");
                long totalStateTime = 0;
                for (int ips = 0; ips < 3; ips++) {
                    time = u.getProcessStateTime(ips, rawRealtime, which);
                    if (time > 0) {
                        totalStateTime += time;
                        stringBuilder.setLength(0);
                        stringBuilder.append(prefix);
                        stringBuilder.append("    ");
                        stringBuilder.append(Uid.PROCESS_STATE_NAMES[ips]);
                        stringBuilder.append(" for: ");
                        formatTimeMs(stringBuilder, (500 + totalStateTime) / 1000);
                        pw.println(stringBuilder.toString());
                        uidActivity = true;
                    }
                }
                long userCpuTimeUs = u.getUserCpuTimeUs(which);
                long systemCpuTimeUs = u.getSystemCpuTimeUs(which);
                long powerCpuMaUs = u.getCpuPowerMaUs(which);
                if (userCpuTimeUs > 0 || systemCpuTimeUs > 0 || powerCpuMaUs > 0) {
                    stringBuilder.setLength(0);
                    stringBuilder.append(prefix);
                    stringBuilder.append("    Total cpu time: u=");
                    formatTimeMs(stringBuilder, userCpuTimeUs / 1000);
                    stringBuilder.append("s=");
                    formatTimeMs(stringBuilder, systemCpuTimeUs / 1000);
                    stringBuilder.append("p=");
                    printmAh(stringBuilder, ((double) powerCpuMaUs) / 3.6E9d);
                    stringBuilder.append("mAh");
                    pw.println(stringBuilder.toString());
                }
                ArrayMap<String, ? extends Proc> processStats = u.getProcessStats();
                for (int ipr = processStats.size() - 1; ipr >= 0; ipr--) {
                    Proc ps = (Proc) processStats.valueAt(ipr);
                    long userTime = ps.getUserTime(which);
                    long systemTime = ps.getSystemTime(which);
                    long foregroundTime = ps.getForegroundTime(which);
                    starts = ps.getStarts(which);
                    int numCrashes = ps.getNumCrashes(which);
                    int numAnrs = ps.getNumAnrs(which);
                    int numExcessive = which == 0 ? ps.countExcessivePowers() : 0;
                    if (userTime != 0 || systemTime != 0 || foregroundTime != 0 || starts != 0 || numExcessive != 0 || numCrashes != 0 || numAnrs != 0) {
                        stringBuilder.setLength(0);
                        stringBuilder.append(prefix);
                        stringBuilder.append("    Proc ");
                        stringBuilder.append((String) processStats.keyAt(ipr));
                        stringBuilder.append(":\n");
                        stringBuilder.append(prefix);
                        stringBuilder.append("      CPU: ");
                        formatTimeMs(stringBuilder, userTime);
                        stringBuilder.append("usr + ");
                        formatTimeMs(stringBuilder, systemTime);
                        stringBuilder.append("krn ; ");
                        formatTimeMs(stringBuilder, foregroundTime);
                        stringBuilder.append(FOREGROUND_DATA);
                        if (!(starts == 0 && numCrashes == 0 && numAnrs == 0)) {
                            stringBuilder.append("\n");
                            stringBuilder.append(prefix);
                            stringBuilder.append("      ");
                            boolean hasOne = false;
                            if (starts != 0) {
                                hasOne = true;
                                stringBuilder.append(starts);
                                stringBuilder.append(" starts");
                            }
                            if (numCrashes != 0) {
                                if (hasOne) {
                                    stringBuilder.append(", ");
                                }
                                hasOne = true;
                                stringBuilder.append(numCrashes);
                                stringBuilder.append(" crashes");
                            }
                            if (numAnrs != 0) {
                                if (hasOne) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(numAnrs);
                                stringBuilder.append(" anrs");
                            }
                        }
                        pw.println(stringBuilder.toString());
                        for (int e = 0; e < numExcessive; e++) {
                            ExcessivePower ew = ps.getExcessivePower(e);
                            if (ew != null) {
                                pw.print(prefix);
                                pw.print("      * Killed for ");
                                if (ew.type == 1) {
                                    pw.print("wake lock");
                                } else if (ew.type == 2) {
                                    pw.print(CPU_DATA);
                                } else {
                                    pw.print("unknown");
                                }
                                pw.print(" use: ");
                                TimeUtils.formatDuration(ew.usedTime, pw);
                                pw.print(" over ");
                                TimeUtils.formatDuration(ew.overTime, pw);
                                if (ew.overTime != 0) {
                                    pw.print(" (");
                                    pw.print((ew.usedTime * 100) / ew.overTime);
                                    pw.println("%)");
                                }
                            }
                        }
                        uidActivity = true;
                    }
                }
                ArrayMap<String, ? extends Pkg> packageStats = u.getPackageStats();
                for (int ipkg = packageStats.size() - 1; ipkg >= 0; ipkg--) {
                    pw.print(prefix);
                    pw.print("    Apk ");
                    pw.print((String) packageStats.keyAt(ipkg));
                    pw.println(":");
                    boolean apkActivity = false;
                    Pkg ps2 = (Pkg) packageStats.valueAt(ipkg);
                    ArrayMap<String, ? extends Counter> alarms = ps2.getWakeupAlarmStats();
                    for (int iwa = alarms.size() - 1; iwa >= 0; iwa--) {
                        pw.print(prefix);
                        pw.print("      Wakeup alarm ");
                        pw.print((String) alarms.keyAt(iwa));
                        pw.print(": ");
                        pw.print(((Counter) alarms.valueAt(iwa)).getCountLocked(which));
                        pw.println(" times");
                        apkActivity = true;
                    }
                    ArrayMap<String, ? extends Serv> serviceStats = ps2.getServiceStats();
                    for (int isvc = serviceStats.size() - 1; isvc >= 0; isvc--) {
                        Serv ss = (Serv) serviceStats.valueAt(isvc);
                        long startTime = ss.getStartTime(batteryUptime, which);
                        starts = ss.getStarts(which);
                        int launches = ss.getLaunches(which);
                        if (startTime != 0 || starts != 0 || launches != 0) {
                            stringBuilder.setLength(0);
                            stringBuilder.append(prefix);
                            stringBuilder.append("      Service ");
                            stringBuilder.append((String) serviceStats.keyAt(isvc));
                            stringBuilder.append(":\n");
                            stringBuilder.append(prefix);
                            stringBuilder.append("        Created for: ");
                            formatTimeMs(stringBuilder, startTime / 1000);
                            stringBuilder.append("uptime\n");
                            stringBuilder.append(prefix);
                            stringBuilder.append("        Starts: ");
                            stringBuilder.append(starts);
                            stringBuilder.append(", launches: ");
                            stringBuilder.append(launches);
                            pw.println(stringBuilder.toString());
                            apkActivity = true;
                        }
                    }
                    if (!apkActivity) {
                        pw.print(prefix);
                        pw.println("      (nothing executed)");
                    }
                    uidActivity = true;
                }
                if (!uidActivity) {
                    pw.print(prefix);
                    pw.println("    (nothing executed)");
                }
            }
        }
    }

    static void printBitDescriptions(PrintWriter pw, int oldval, int newval, HistoryTag wakelockTag, BitDescription[] descriptions, boolean longNames) {
        int diff = oldval ^ newval;
        if (diff != 0) {
            boolean didWake = false;
            for (BitDescription bd : descriptions) {
                if ((bd.mask & diff) != 0) {
                    pw.print(longNames ? " " : ",");
                    if (bd.shift < 0) {
                        pw.print((bd.mask & newval) != 0 ? "+" : "-");
                        pw.print(longNames ? bd.name : bd.shortName);
                        if (bd.mask == 1073741824 && wakelockTag != null) {
                            didWake = true;
                            pw.print("=");
                            if (longNames) {
                                UserHandle.formatUid(pw, wakelockTag.uid);
                                pw.print(":\"");
                                pw.print(wakelockTag.string);
                                pw.print("\"");
                            } else {
                                pw.print(wakelockTag.poolIdx);
                            }
                        }
                    } else {
                        pw.print(longNames ? bd.name : bd.shortName);
                        pw.print("=");
                        int val = (bd.mask & newval) >> bd.shift;
                        if (bd.values == null || val < 0 || val >= bd.values.length) {
                            pw.print(val);
                        } else {
                            pw.print(longNames ? bd.values[val] : bd.shortValues[val]);
                        }
                    }
                }
            }
            if (!didWake && wakelockTag != null) {
                pw.print(longNames ? " wake_lock=" : ",w=");
                if (longNames) {
                    UserHandle.formatUid(pw, wakelockTag.uid);
                    pw.print(":\"");
                    pw.print(wakelockTag.string);
                    pw.print("\"");
                    return;
                }
                pw.print(wakelockTag.poolIdx);
            }
        }
    }

    public void prepareForDumpLocked() {
    }

    private void printSizeValue(PrintWriter pw, long size) {
        float result = (float) size;
        String suffix = ProxyInfo.LOCAL_EXCL_LIST;
        if (result >= 10240.0f) {
            suffix = "KB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "MB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "GB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "TB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "PB";
            result /= 1024.0f;
        }
        pw.print((int) result);
        pw.print(suffix);
    }

    private static boolean dumpTimeEstimate(PrintWriter pw, String label1, String label2, String label3, long estimatedTime) {
        if (estimatedTime < 0) {
            return false;
        }
        pw.print(label1);
        pw.print(label2);
        pw.print(label3);
        StringBuilder sb = new StringBuilder(64);
        formatTimeMs(sb, estimatedTime);
        pw.print(sb);
        pw.println();
        return true;
    }

    private static boolean dumpDurationSteps(PrintWriter pw, String prefix, String header, LevelStepTracker steps, boolean checkin) {
        if (steps == null) {
            return false;
        }
        int count = steps.mNumStepDurations;
        if (count <= 0) {
            return false;
        }
        if (!checkin) {
            pw.println(header);
        }
        String[] lineArgs = new String[5];
        for (int i = 0; i < count; i++) {
            long duration = steps.getDurationAt(i);
            int level = steps.getLevelAt(i);
            long initMode = (long) steps.getInitModeAt(i);
            long modMode = (long) steps.getModModeAt(i);
            if (checkin) {
                lineArgs[0] = Long.toString(duration);
                lineArgs[1] = Integer.toString(level);
                if ((3 & modMode) == 0) {
                    switch (((int) (3 & initMode)) + 1) {
                        case 1:
                            lineArgs[2] = "s-";
                            break;
                        case 2:
                            lineArgs[2] = "s+";
                            break;
                        case 3:
                            lineArgs[2] = "sd";
                            break;
                        case 4:
                            lineArgs[2] = "sds";
                            break;
                        default:
                            lineArgs[2] = "?";
                            break;
                    }
                }
                lineArgs[2] = ProxyInfo.LOCAL_EXCL_LIST;
                if ((4 & modMode) == 0) {
                    lineArgs[3] = (4 & initMode) != 0 ? "p+" : "p-";
                } else {
                    lineArgs[3] = ProxyInfo.LOCAL_EXCL_LIST;
                }
                if ((8 & modMode) == 0) {
                    lineArgs[4] = (8 & initMode) != 0 ? "i+" : "i-";
                } else {
                    lineArgs[4] = ProxyInfo.LOCAL_EXCL_LIST;
                }
                dumpLine(pw, 0, "i", header, (Object[]) lineArgs);
            } else {
                pw.print(prefix);
                pw.print("#");
                pw.print(i);
                pw.print(": ");
                TimeUtils.formatDuration(duration, pw);
                pw.print(" to ");
                pw.print(level);
                boolean haveModes = false;
                if ((3 & modMode) == 0) {
                    pw.print(" (");
                    switch (((int) (3 & initMode)) + 1) {
                        case 1:
                            pw.print("screen-off");
                            break;
                        case 2:
                            pw.print("screen-on");
                            break;
                        case 3:
                            pw.print("screen-doze");
                            break;
                        case 4:
                            pw.print("screen-doze-suspend");
                            break;
                        default:
                            pw.print("screen-?");
                            break;
                    }
                    haveModes = true;
                }
                if ((4 & modMode) == 0) {
                    pw.print(haveModes ? ", " : " (");
                    pw.print((4 & initMode) != 0 ? "power-save-on" : "power-save-off");
                    haveModes = true;
                }
                if ((8 & modMode) == 0) {
                    pw.print(haveModes ? ", " : " (");
                    pw.print((8 & initMode) != 0 ? "device-idle-on" : "device-idle-off");
                    haveModes = true;
                }
                if (haveModes) {
                    pw.print(")");
                }
                pw.println();
            }
        }
        return true;
    }

    private void dumpHistoryLocked(PrintWriter pw, int flags, long histStart, boolean checkin) {
        HistoryPrinter hprinter = new HistoryPrinter();
        HistoryItem rec = new HistoryItem();
        long lastTime = -1;
        long baseTime = -1;
        boolean printed = false;
        HistoryEventTracker tracker = null;
        while (getNextHistoryLocked(rec)) {
            lastTime = rec.time;
            if (baseTime < 0) {
                baseTime = lastTime;
            }
            if (rec.time >= histStart) {
                if (histStart >= 0 && !printed) {
                    if (rec.cmd == (byte) 5 || rec.cmd == (byte) 7 || rec.cmd == (byte) 4 || rec.cmd == (byte) 8) {
                        printed = true;
                        hprinter.printNextItem(pw, rec, baseTime, checkin, (flags & 32) != 0);
                        rec.cmd = (byte) 0;
                    } else if (rec.currentTime != 0) {
                        printed = true;
                        byte cmd = rec.cmd;
                        rec.cmd = (byte) 5;
                        hprinter.printNextItem(pw, rec, baseTime, checkin, (flags & 32) != 0);
                        rec.cmd = cmd;
                    }
                    if (tracker != null) {
                        if (rec.cmd != (byte) 0) {
                            hprinter.printNextItem(pw, rec, baseTime, checkin, (flags & 32) != 0);
                            rec.cmd = (byte) 0;
                        }
                        int oldEventCode = rec.eventCode;
                        HistoryTag oldEventTag = rec.eventTag;
                        rec.eventTag = new HistoryTag();
                        for (int i = 0; i < 19; i++) {
                            HashMap<String, SparseIntArray> active = tracker.getStateForEvent(i);
                            if (active != null) {
                                for (Entry<String, SparseIntArray> ent : active.entrySet()) {
                                    SparseIntArray uids = (SparseIntArray) ent.getValue();
                                    for (int j = 0; j < uids.size(); j++) {
                                        rec.eventCode = i;
                                        rec.eventTag.string = (String) ent.getKey();
                                        rec.eventTag.uid = uids.keyAt(j);
                                        rec.eventTag.poolIdx = uids.valueAt(j);
                                        hprinter.printNextItem(pw, rec, baseTime, checkin, (flags & 32) != 0);
                                        rec.wakeReasonTag = null;
                                        rec.wakelockTag = null;
                                    }
                                }
                            }
                        }
                        rec.eventCode = oldEventCode;
                        rec.eventTag = oldEventTag;
                        tracker = null;
                    }
                }
                hprinter.printNextItem(pw, rec, baseTime, checkin, (flags & 32) != 0);
            }
        }
        if (histStart >= 0) {
            commitCurrentHistoryBatchLocked();
            pw.print(checkin ? "NEXT: " : "  NEXT: ");
            pw.println(1 + lastTime);
        }
    }

    private void dumpDailyLevelStepSummary(PrintWriter pw, String prefix, String label, LevelStepTracker steps, StringBuilder tmpSb, int[] tmpOutInt) {
        if (steps != null) {
            long timeRemaining = steps.computeTimeEstimate(0, 0, tmpOutInt);
            if (timeRemaining >= 0) {
                pw.print(prefix);
                pw.print(label);
                pw.print(" total time: ");
                tmpSb.setLength(0);
                formatTimeMs(tmpSb, timeRemaining);
                pw.print(tmpSb);
                pw.print(" (from ");
                pw.print(tmpOutInt[0]);
                pw.println(" steps)");
            }
            for (int i = 0; i < STEP_LEVEL_MODES_OF_INTEREST.length; i++) {
                long estimatedTime = steps.computeTimeEstimate((long) STEP_LEVEL_MODES_OF_INTEREST[i], (long) STEP_LEVEL_MODE_VALUES[i], tmpOutInt);
                if (estimatedTime > 0) {
                    pw.print(prefix);
                    pw.print(label);
                    pw.print(" ");
                    pw.print(STEP_LEVEL_MODE_LABELS[i]);
                    pw.print(" time: ");
                    tmpSb.setLength(0);
                    formatTimeMs(tmpSb, estimatedTime);
                    pw.print(tmpSb);
                    pw.print(" (from ");
                    pw.print(tmpOutInt[0]);
                    pw.println(" steps)");
                }
            }
        }
    }

    private void dumpDailyPackageChanges(PrintWriter pw, String prefix, ArrayList<PackageChange> changes) {
        if (changes != null) {
            pw.print(prefix);
            pw.println("Package changes:");
            for (int i = 0; i < changes.size(); i++) {
                PackageChange pc = (PackageChange) changes.get(i);
                if (pc.mUpdate) {
                    pw.print(prefix);
                    pw.print("  Update ");
                    pw.print(pc.mPackageName);
                    pw.print(" vers=");
                    pw.println(pc.mVersionCode);
                } else {
                    pw.print(prefix);
                    pw.print("  Uninstall ");
                    pw.println(pc.mPackageName);
                }
            }
        }
    }

    public void dumpLocked(Context context, PrintWriter pw, int flags, int reqUid, long histStart) {
        prepareForDumpLocked();
        boolean filtering = (flags & 14) != 0;
        if (!((flags & 8) == 0 && filtering)) {
            long historyTotalSize = (long) getHistoryTotalSize();
            long historyUsedSize = (long) getHistoryUsedSize();
            if (startIteratingHistoryLocked()) {
                try {
                    pw.print("Battery History (");
                    pw.print((100 * historyUsedSize) / historyTotalSize);
                    pw.print("% used, ");
                    printSizeValue(pw, historyUsedSize);
                    pw.print(" used of ");
                    printSizeValue(pw, historyTotalSize);
                    pw.print(", ");
                    pw.print(getHistoryStringPoolSize());
                    pw.print(" strings using ");
                    printSizeValue(pw, (long) getHistoryStringPoolBytes());
                    pw.println("):");
                    dumpHistoryLocked(pw, flags, histStart, false);
                    pw.println();
                } finally {
                    finishIteratingHistoryLocked();
                }
            }
            if (startIteratingOldHistoryLocked()) {
                try {
                    HistoryItem rec = new HistoryItem();
                    pw.println("Old battery History:");
                    HistoryPrinter hprinter = new HistoryPrinter();
                    long baseTime = -1;
                    while (getNextOldHistoryLocked(rec)) {
                        if (baseTime < 0) {
                            baseTime = rec.time;
                        }
                        hprinter.printNextItem(pw, rec, baseTime, false, (flags & 32) != 0);
                    }
                    pw.println();
                } finally {
                    finishIteratingOldHistoryLocked();
                }
            }
        }
        if (!filtering || (flags & 6) != 0) {
            int i;
            if (!filtering) {
                SparseArray<? extends Uid> uidStats = getUidStats();
                int NU = uidStats.size();
                boolean didPid = false;
                long nowRealtime = SystemClock.elapsedRealtime();
                for (i = 0; i < NU; i++) {
                    SparseArray<? extends Pid> pids = ((Uid) uidStats.valueAt(i)).getPidStats();
                    if (pids != null) {
                        for (int j = 0; j < pids.size(); j++) {
                            Pid pid = (Pid) pids.valueAt(j);
                            if (!didPid) {
                                pw.println("Per-PID Stats:");
                                didPid = true;
                            }
                            long time = pid.mWakeSumMs + (pid.mWakeNesting > 0 ? nowRealtime - pid.mWakeStartMs : 0);
                            pw.print("  PID ");
                            pw.print(pids.keyAt(j));
                            pw.print(" wake time: ");
                            TimeUtils.formatDuration(time, pw);
                            pw.println(ProxyInfo.LOCAL_EXCL_LIST);
                        }
                    }
                }
                if (didPid) {
                    pw.println();
                }
            }
            if (!(filtering && (flags & 2) == 0)) {
                long timeRemaining;
                if (dumpDurationSteps(pw, "  ", "Discharge step durations:", getDischargeLevelStepTracker(), false)) {
                    timeRemaining = computeBatteryTimeRemaining(SystemClock.elapsedRealtime());
                    if (timeRemaining >= 0) {
                        pw.print("  Estimated discharge time remaining: ");
                        TimeUtils.formatDuration(timeRemaining / 1000, pw);
                        pw.println();
                    }
                    LevelStepTracker steps = getDischargeLevelStepTracker();
                    for (i = 0; i < STEP_LEVEL_MODES_OF_INTEREST.length; i++) {
                        dumpTimeEstimate(pw, "  Estimated ", STEP_LEVEL_MODE_LABELS[i], " time: ", steps.computeTimeEstimate((long) STEP_LEVEL_MODES_OF_INTEREST[i], (long) STEP_LEVEL_MODE_VALUES[i], null));
                    }
                    pw.println();
                }
                if (dumpDurationSteps(pw, "  ", "Charge step durations:", getChargeLevelStepTracker(), false)) {
                    timeRemaining = computeChargeTimeRemaining(SystemClock.elapsedRealtime());
                    if (timeRemaining >= 0) {
                        pw.print("  Estimated charge time remaining: ");
                        TimeUtils.formatDuration(timeRemaining / 1000, pw);
                        pw.println();
                    }
                    pw.println();
                }
            }
            if (!(filtering && (flags & 6) == 0)) {
                pw.println("Daily stats:");
                pw.print("  Current start time: ");
                pw.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getCurrentDailyStartTime()).toString());
                pw.print("  Next min deadline: ");
                pw.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getNextMinDailyDeadline()).toString());
                pw.print("  Next max deadline: ");
                pw.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getNextMaxDailyDeadline()).toString());
                StringBuilder stringBuilder = new StringBuilder(64);
                int[] outInt = new int[1];
                LevelStepTracker dsteps = getDailyDischargeLevelStepTracker();
                LevelStepTracker csteps = getDailyChargeLevelStepTracker();
                ArrayList<PackageChange> pkgc = getDailyPackageChanges();
                if (dsteps.mNumStepDurations > 0 || csteps.mNumStepDurations > 0 || pkgc != null) {
                    if ((flags & 4) == 0 && filtering) {
                        pw.println("  Current daily steps:");
                        dumpDailyLevelStepSummary(pw, "    ", "Discharge", dsteps, stringBuilder, outInt);
                        dumpDailyLevelStepSummary(pw, "    ", "Charge", csteps, stringBuilder, outInt);
                    } else {
                        if (dumpDurationSteps(pw, "    ", "  Current daily discharge step durations:", dsteps, false)) {
                            dumpDailyLevelStepSummary(pw, "      ", "Discharge", dsteps, stringBuilder, outInt);
                        }
                        if (dumpDurationSteps(pw, "    ", "  Current daily charge step durations:", csteps, false)) {
                            dumpDailyLevelStepSummary(pw, "      ", "Charge", csteps, stringBuilder, outInt);
                        }
                        dumpDailyPackageChanges(pw, "    ", pkgc);
                    }
                }
                int curIndex = 0;
                while (true) {
                    DailyItem dit = getDailyItemLocked(curIndex);
                    if (dit == null) {
                        break;
                    }
                    curIndex++;
                    if ((flags & 4) != 0) {
                        pw.println();
                    }
                    pw.print("  Daily from ");
                    pw.print(DateFormat.format("yyyy-MM-dd-HH-mm-ss", dit.mStartTime).toString());
                    pw.print(" to ");
                    pw.print(DateFormat.format("yyyy-MM-dd-HH-mm-ss", dit.mEndTime).toString());
                    pw.println(":");
                    if ((flags & 4) == 0 && filtering) {
                        dumpDailyLevelStepSummary(pw, "    ", "Discharge", dit.mDischargeSteps, stringBuilder, outInt);
                        dumpDailyLevelStepSummary(pw, "    ", "Charge", dit.mChargeSteps, stringBuilder, outInt);
                    } else {
                        if (dumpDurationSteps(pw, "      ", "    Discharge step durations:", dit.mDischargeSteps, false)) {
                            dumpDailyLevelStepSummary(pw, "        ", "Discharge", dit.mDischargeSteps, stringBuilder, outInt);
                        }
                        if (dumpDurationSteps(pw, "      ", "    Charge step durations:", dit.mChargeSteps, false)) {
                            dumpDailyLevelStepSummary(pw, "        ", "Charge", dit.mChargeSteps, stringBuilder, outInt);
                        }
                        dumpDailyPackageChanges(pw, "    ", dit.mPackageChanges);
                    }
                }
                pw.println();
            }
            if (!filtering || (flags & 2) != 0) {
                pw.println("Statistics since last charge:");
                pw.println("  System starts: " + getStartCount() + ", currently on battery: " + getIsOnBattery());
                dumpLocked(context, pw, ProxyInfo.LOCAL_EXCL_LIST, 0, reqUid, (flags & 64) != 0);
                pw.println();
            }
        }
    }

    public void dumpCheckinLocked(Context context, PrintWriter pw, List<ApplicationInfo> apps, int flags, long histStart) {
        int i;
        prepareForDumpLocked();
        dumpLine(pw, 0, "i", VERSION_DATA, CHECKIN_VERSION, Integer.valueOf(getParcelVersion()), getStartPlatformVersion(), getEndPlatformVersion());
        long now = getHistoryBaseTime() + SystemClock.elapsedRealtime();
        boolean filtering = (flags & 14) != 0;
        if (!((flags & 16) == 0 && (flags & 8) == 0) && startIteratingHistoryLocked()) {
            i = 0;
            while (i < getHistoryStringPoolSize()) {
                try {
                    pw.print(9);
                    pw.print(',');
                    pw.print(HISTORY_STRING_POOL);
                    pw.print(',');
                    pw.print(i);
                    pw.print(",");
                    pw.print(getHistoryTagPoolUid(i));
                    pw.print(",\"");
                    pw.print(getHistoryTagPoolString(i).replace("\\", "\\\\").replace("\"", "\\\""));
                    pw.print("\"");
                    pw.println();
                    i++;
                } finally {
                    finishIteratingHistoryLocked();
                }
            }
            dumpHistoryLocked(pw, flags, histStart, true);
        }
        if (!filtering || (flags & 6) != 0) {
            String[] lineArgs;
            if (apps != null) {
                ArrayList<String> pkgs;
                SparseArray<ArrayList<String>> uids = new SparseArray();
                for (i = 0; i < apps.size(); i++) {
                    ApplicationInfo ai = (ApplicationInfo) apps.get(i);
                    pkgs = (ArrayList) uids.get(ai.uid);
                    if (pkgs == null) {
                        pkgs = new ArrayList();
                        uids.put(ai.uid, pkgs);
                    }
                    pkgs.add(ai.packageName);
                }
                SparseArray<? extends Uid> uidStats = getUidStats();
                int NU = uidStats.size();
                lineArgs = new String[2];
                for (i = 0; i < NU; i++) {
                    int uid = uidStats.keyAt(i);
                    pkgs = (ArrayList) uids.get(uid);
                    if (pkgs != null) {
                        for (int j = 0; j < pkgs.size(); j++) {
                            lineArgs[0] = Integer.toString(uid);
                            lineArgs[1] = (String) pkgs.get(j);
                            dumpLine(pw, 0, "i", "uid", (Object[]) lineArgs);
                        }
                    }
                }
            }
            if (!filtering || (flags & 2) != 0) {
                dumpDurationSteps(pw, ProxyInfo.LOCAL_EXCL_LIST, DISCHARGE_STEP_DATA, getDischargeLevelStepTracker(), true);
                lineArgs = new String[1];
                long timeRemaining = computeBatteryTimeRemaining(SystemClock.elapsedRealtime());
                if (timeRemaining >= 0) {
                    lineArgs[0] = Long.toString(timeRemaining);
                    dumpLine(pw, 0, "i", DISCHARGE_TIME_REMAIN_DATA, (Object[]) lineArgs);
                }
                dumpDurationSteps(pw, ProxyInfo.LOCAL_EXCL_LIST, CHARGE_STEP_DATA, getChargeLevelStepTracker(), true);
                timeRemaining = computeChargeTimeRemaining(SystemClock.elapsedRealtime());
                if (timeRemaining >= 0) {
                    lineArgs[0] = Long.toString(timeRemaining);
                    dumpLine(pw, 0, "i", CHARGE_TIME_REMAIN_DATA, (Object[]) lineArgs);
                }
                dumpCheckinLocked(context, pw, 0, -1, (flags & 64) != 0);
            }
        }
    }
}
