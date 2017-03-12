package android.appwidget;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RemoteViews;
import android.widget.RemoteViews.OnClickHandler;
import android.widget.RemoteViews.RemoteView;
import android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback;
import android.widget.TextView;

public class AppWidgetHostView extends FrameLayout {
    static final boolean CROSSFADE = false;
    static final int FADE_DURATION = 1000;
    static final boolean LOGD = false;
    static final String TAG = "AppWidgetHostView";
    static final int VIEW_MODE_CONTENT = 1;
    static final int VIEW_MODE_DEFAULT = 3;
    static final int VIEW_MODE_ERROR = 2;
    static final int VIEW_MODE_NOINIT = 0;
    static final Filter sInflaterFilter = new Filter() {
        public boolean onLoadClass(Class clazz) {
            return clazz.isAnnotationPresent(RemoteView.class);
        }
    };
    int mAppWidgetId;
    Context mContext;
    long mFadeStartTime;
    AppWidgetProviderInfo mInfo;
    int mLayoutId;
    Bitmap mOld;
    Paint mOldPaint;
    private OnClickHandler mOnClickHandler;
    Context mRemoteContext;
    View mView;
    int mViewMode;

    private static class ParcelableSparseArray extends SparseArray<Parcelable> implements Parcelable {
        public static final Creator<ParcelableSparseArray> CREATOR = new Creator<ParcelableSparseArray>() {
            public ParcelableSparseArray createFromParcel(Parcel source) {
                ParcelableSparseArray array = new ParcelableSparseArray();
                ClassLoader loader = array.getClass().getClassLoader();
                int count = source.readInt();
                for (int i = 0; i < count; i++) {
                    array.put(source.readInt(), source.readParcelable(loader));
                }
                return array;
            }

            public ParcelableSparseArray[] newArray(int size) {
                return new ParcelableSparseArray[size];
            }
        };

        private ParcelableSparseArray() {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int count = size();
            dest.writeInt(count);
            for (int i = 0; i < count; i++) {
                dest.writeInt(keyAt(i));
                dest.writeParcelable((Parcelable) valueAt(i), 0);
            }
        }
    }

    public AppWidgetHostView(Context context) {
        this(context, R.anim.fade_in, R.anim.fade_out);
    }

    public AppWidgetHostView(Context context, OnClickHandler handler) {
        this(context, R.anim.fade_in, R.anim.fade_out);
        this.mOnClickHandler = handler;
    }

    public AppWidgetHostView(Context context, int animationIn, int animationOut) {
        super(context);
        this.mViewMode = 0;
        this.mLayoutId = -1;
        this.mFadeStartTime = -1;
        this.mOldPaint = new Paint();
        this.mContext = context;
        setIsRootNamespace(true);
    }

    public void setOnClickHandler(OnClickHandler handler) {
        this.mOnClickHandler = handler;
    }

    public void setAppWidget(int appWidgetId, AppWidgetProviderInfo info) {
        this.mAppWidgetId = appWidgetId;
        this.mInfo = info;
        if (info != null) {
            Rect padding = getDefaultPaddingForWidget(this.mContext, info.provider, null);
            setPadding(padding.left, padding.top, padding.right, padding.bottom);
            setContentDescription(info.label);
        }
    }

    public static Rect getDefaultPaddingForWidget(Context context, ComponentName component, Rect padding) {
        PackageManager packageManager = context.getPackageManager();
        if (padding == null) {
            padding = new Rect(0, 0, 0, 0);
        } else {
            padding.set(0, 0, 0, 0);
        }
        try {
            if (packageManager.getApplicationInfo(component.getPackageName(), 0).targetSdkVersion >= 14) {
                Resources r = context.getResources();
                padding.left = r.getDimensionPixelSize(17104979);
                padding.right = r.getDimensionPixelSize(17104981);
                padding.top = r.getDimensionPixelSize(17104980);
                padding.bottom = r.getDimensionPixelSize(17104982);
            }
        } catch (NameNotFoundException e) {
        }
        return padding;
    }

    public int getAppWidgetId() {
        return this.mAppWidgetId;
    }

    public AppWidgetProviderInfo getAppWidgetInfo() {
        return this.mInfo;
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        ParcelableSparseArray jail = new ParcelableSparseArray();
        super.dispatchSaveInstanceState(jail);
        container.put(generateId(), jail);
    }

    private int generateId() {
        int id = getId();
        return id == -1 ? this.mAppWidgetId : id;
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        Parcelable parcelable = (Parcelable) container.get(generateId());
        ParcelableSparseArray jail = null;
        if (parcelable != null && (parcelable instanceof ParcelableSparseArray)) {
            jail = (ParcelableSparseArray) parcelable;
        }
        if (jail == null) {
            jail = new ParcelableSparseArray();
        }
        try {
            super.dispatchRestoreInstanceState(jail);
        } catch (Exception e) {
            Log.e(TAG, "failed to restoreInstanceState for widget id: " + this.mAppWidgetId + ", " + (this.mInfo == null ? "null" : this.mInfo.provider), e);
        }
    }

    public void updateAppWidgetSize(Bundle newOptions, int minWidth, int minHeight, int maxWidth, int maxHeight) {
        updateAppWidgetSize(newOptions, minWidth, minHeight, maxWidth, maxHeight, false);
    }

    public void updateAppWidgetSize(Bundle newOptions, int minWidth, int minHeight, int maxWidth, int maxHeight, boolean ignorePadding) {
        int i;
        if (newOptions == null) {
            newOptions = new Bundle();
        }
        Rect padding = new Rect();
        if (this.mInfo != null) {
            padding = getDefaultPaddingForWidget(this.mContext, this.mInfo.provider, padding);
        }
        float density = getResources().getDisplayMetrics().density;
        int xPaddingDips = (int) (((float) (padding.left + padding.right)) / density);
        int yPaddingDips = (int) (((float) (padding.top + padding.bottom)) / density);
        if (ignorePadding) {
            i = 0;
        } else {
            i = xPaddingDips;
        }
        int newMinWidth = minWidth - i;
        if (ignorePadding) {
            i = 0;
        } else {
            i = yPaddingDips;
        }
        int newMinHeight = minHeight - i;
        if (ignorePadding) {
            xPaddingDips = 0;
        }
        int newMaxWidth = maxWidth - xPaddingDips;
        if (ignorePadding) {
            yPaddingDips = 0;
        }
        int newMaxHeight = maxHeight - yPaddingDips;
        Bundle oldOptions = AppWidgetManager.getInstance(this.mContext).getAppWidgetOptions(this.mAppWidgetId);
        boolean needsUpdate = false;
        if (!(newMinWidth == oldOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) && newMinHeight == oldOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) && newMaxWidth == oldOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH) && newMaxHeight == oldOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT))) {
            needsUpdate = true;
        }
        if (needsUpdate) {
            newOptions.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, newMinWidth);
            newOptions.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, newMinHeight);
            newOptions.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, newMaxWidth);
            newOptions.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, newMaxHeight);
            updateAppWidgetOptions(newOptions);
        }
    }

    public void updateAppWidgetOptions(Bundle options) {
        AppWidgetManager.getInstance(this.mContext).updateAppWidgetOptions(this.mAppWidgetId, options);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.mRemoteContext != null ? this.mRemoteContext : this.mContext, attrs);
    }

    void resetAppWidget(AppWidgetProviderInfo info) {
        this.mInfo = info;
        this.mViewMode = 0;
        updateAppWidget(null);
    }

    public void updateAppWidget(RemoteViews remoteViews) {
        boolean recycled = false;
        View content = null;
        Exception exception = null;
        if (remoteViews != null) {
            this.mRemoteContext = getRemoteContext();
            int layoutId = remoteViews.getLayoutId();
            if (content == null && layoutId == this.mLayoutId) {
                try {
                    remoteViews.reapply(this.mContext, this.mView, this.mOnClickHandler);
                    content = this.mView;
                    recycled = true;
                } catch (Exception e) {
                    exception = e;
                }
            }
            if (content == null) {
                try {
                    content = remoteViews.apply(this.mContext, this, this.mOnClickHandler);
                } catch (Exception e2) {
                    exception = e2;
                }
            }
            this.mLayoutId = layoutId;
            this.mViewMode = 1;
        } else if (this.mViewMode != 3) {
            content = getDefaultView();
            this.mLayoutId = -1;
            this.mViewMode = 3;
        } else {
            return;
        }
        if (content == null) {
            if (this.mViewMode != 2) {
                Log.w(TAG, "updateAppWidget couldn't find any view, using error view", exception);
                content = getErrorView();
                this.mViewMode = 2;
            } else {
                return;
            }
        }
        if (!recycled) {
            prepareView(content);
            addView(content);
        }
        if (this.mView != content) {
            removeView(this.mView);
            this.mView = content;
        }
    }

    void viewDataChanged(int viewId) {
        View v = findViewById(viewId);
        if (v != null && (v instanceof AdapterView)) {
            AdapterView<?> adapterView = (AdapterView) v;
            Adapter adapter = adapterView.getAdapter();
            if (adapter instanceof BaseAdapter) {
                ((BaseAdapter) adapter).notifyDataSetChanged();
            } else if (adapter == null && (adapterView instanceof RemoteAdapterConnectionCallback)) {
                ((RemoteAdapterConnectionCallback) adapterView).deferNotifyDataSetChanged();
            }
        }
    }

    private Context getRemoteContext() {
        try {
            return this.mContext.createApplicationContext(this.mInfo.providerInfo.applicationInfo, 4);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Package name " + this.mInfo.providerInfo.packageName + " not found");
            return this.mContext;
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    protected void prepareView(View view) {
        LayoutParams requested = (LayoutParams) view.getLayoutParams();
        if (requested == null) {
            requested = new LayoutParams(-1, -1);
        }
        requested.gravity = 17;
        view.setLayoutParams(requested);
    }

    protected View getDefaultView() {
        View defaultView = null;
        Exception exception = null;
        try {
            if (this.mInfo != null) {
                Context theirContext = getRemoteContext();
                this.mRemoteContext = theirContext;
                LayoutInflater inflater = ((LayoutInflater) theirContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).cloneInContext(theirContext);
                inflater.setFilter(sInflaterFilter);
                Bundle options = AppWidgetManager.getInstance(this.mContext).getAppWidgetOptions(this.mAppWidgetId);
                int layoutId = this.mInfo.initialLayout;
                if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY) && options.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY) == 2) {
                    int kgLayoutId = this.mInfo.initialKeyguardLayout;
                    if (kgLayoutId != 0) {
                        layoutId = kgLayoutId;
                    }
                }
                defaultView = inflater.inflate(layoutId, this, false);
            } else {
                Log.w(TAG, "can't inflate defaultView because mInfo is missing");
            }
        } catch (Exception e) {
            exception = e;
        }
        if (exception != null) {
            Log.w(TAG, "Error inflating AppWidget " + this.mInfo + ": " + exception.toString());
        }
        if (defaultView == null) {
            return getErrorView();
        }
        return defaultView;
    }

    protected View getErrorView() {
        TextView tv = new TextView(this.mContext);
        tv.setText(17040427);
        tv.setBackgroundColor(Color.argb(127, 0, 0, 0));
        return tv;
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setClassName(AppWidgetHostView.class.getName());
    }
}
