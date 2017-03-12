package android.app.backup;

import android.app.IBackupAgent.Stub;
import android.app.QueuedWork;
import android.app.backup.FullBackup.BackupScheme;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.ArraySet;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import org.xmlpull.v1.XmlPullParserException;

public abstract class BackupAgent extends ContextWrapper {
    private static final boolean DEBUG = false;
    private static final String TAG = "BackupAgent";
    public static final int TYPE_DIRECTORY = 2;
    public static final int TYPE_EOF = 0;
    public static final int TYPE_FILE = 1;
    public static final int TYPE_SYMLINK = 3;
    private final IBinder mBinder = new BackupServiceBinder().asBinder();
    Handler mHandler = null;

    private class BackupServiceBinder extends Stub {
        private static final String TAG = "BackupServiceBinder";

        private BackupServiceBinder() {
        }

        public void doBackup(ParcelFileDescriptor oldState, ParcelFileDescriptor data, ParcelFileDescriptor newState, int token, IBackupManager callbackBinder) throws RemoteException {
            long ident = Binder.clearCallingIdentity();
            try {
                BackupAgent.this.onBackup(oldState, new BackupDataOutput(data.getFileDescriptor()), newState);
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e) {
                }
            } catch (IOException ex) {
                Log.d(TAG, "onBackup (" + BackupAgent.this.getClass().getName() + ") threw", ex);
                throw new RuntimeException(ex);
            } catch (RuntimeException ex2) {
                Log.d(TAG, "onBackup (" + BackupAgent.this.getClass().getName() + ") threw", ex2);
                throw ex2;
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e2) {
                }
            }
        }

        public void doRestore(ParcelFileDescriptor data, int appVersionCode, ParcelFileDescriptor newState, int token, IBackupManager callbackBinder) throws RemoteException {
            long ident = Binder.clearCallingIdentity();
            try {
                BackupAgent.this.onRestore(new BackupDataInput(data.getFileDescriptor()), appVersionCode, newState);
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e) {
                }
            } catch (IOException ex) {
                Log.d(TAG, "onRestore (" + BackupAgent.this.getClass().getName() + ") threw", ex);
                throw new RuntimeException(ex);
            } catch (RuntimeException ex2) {
                Log.d(TAG, "onRestore (" + BackupAgent.this.getClass().getName() + ") threw", ex2);
                throw ex2;
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e2) {
                }
            }
        }

        public void doFullBackup(ParcelFileDescriptor data, int token, IBackupManager callbackBinder) {
            long ident = Binder.clearCallingIdentity();
            BackupAgent.this.waitForSharedPrefs();
            try {
                BackupAgent.this.onFullBackup(new FullBackupDataOutput(data));
                BackupAgent.this.waitForSharedPrefs();
                try {
                    new FileOutputStream(data.getFileDescriptor()).write(new byte[4]);
                } catch (IOException e) {
                    Log.e(TAG, "Unable to finalize backup stream!");
                }
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e2) {
                }
            } catch (IOException ex) {
                Log.d(TAG, "onFullBackup (" + BackupAgent.this.getClass().getName() + ") threw", ex);
                throw new RuntimeException(ex);
            } catch (RuntimeException ex2) {
                Log.d(TAG, "onFullBackup (" + BackupAgent.this.getClass().getName() + ") threw", ex2);
                throw ex2;
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                try {
                    new FileOutputStream(data.getFileDescriptor()).write(new byte[4]);
                } catch (IOException e3) {
                    Log.e(TAG, "Unable to finalize backup stream!");
                }
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e4) {
                }
            }
        }

        public void doMeasureFullBackup(int token, IBackupManager callbackBinder) {
            long ident = Binder.clearCallingIdentity();
            FullBackupDataOutput measureOutput = new FullBackupDataOutput();
            BackupAgent.this.waitForSharedPrefs();
            try {
                BackupAgent.this.onFullBackup(measureOutput);
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, measureOutput.getSize());
                } catch (RemoteException e) {
                }
            } catch (IOException ex) {
                Log.d(TAG, "onFullBackup[M] (" + BackupAgent.this.getClass().getName() + ") threw", ex);
                throw new RuntimeException(ex);
            } catch (RuntimeException ex2) {
                Log.d(TAG, "onFullBackup[M] (" + BackupAgent.this.getClass().getName() + ") threw", ex2);
                throw ex2;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, measureOutput.getSize());
                } catch (RemoteException e2) {
                }
            }
        }

        public void doRestoreFile(ParcelFileDescriptor data, long size, int type, String domain, String path, long mode, long mtime, int token, IBackupManager callbackBinder) throws RemoteException {
            long ident = Binder.clearCallingIdentity();
            try {
                BackupAgent.this.onRestoreFile(data, size, type, domain, path, mode, mtime);
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e) {
                }
            } catch (IOException e2) {
                Log.d(TAG, "onRestoreFile (" + BackupAgent.this.getClass().getName() + ") threw", e2);
                throw new RuntimeException(e2);
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e3) {
                }
            }
        }

        public void doRestoreFinished(int token, IBackupManager callbackBinder) {
            long ident = Binder.clearCallingIdentity();
            try {
                BackupAgent.this.onRestoreFinished();
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e) {
                }
            } catch (Exception e2) {
                Log.d(TAG, "onRestoreFinished (" + BackupAgent.this.getClass().getName() + ") threw", e2);
                throw e2;
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (RemoteException e3) {
                }
            }
        }

        public void fail(String message) {
            BackupAgent.this.getHandler().post(new FailRunnable(message));
        }

        public long getBackupDataSize() {
            ArraySet<String> traversalExcludeSet = new ArraySet();
            long contentSize = 0;
            try {
                Map<String, Set<String>> manifestIncludeMap = FullBackup.getBackupScheme(BackupAgent.this).maybeParseAndGetCanonicalIncludePaths();
                ArraySet<String> manifestExcludeSet = FullBackup.getBackupScheme(BackupAgent.this).maybeParseAndGetCanonicalExcludePaths();
                ApplicationInfo appInfo = BackupAgent.this.getApplicationInfo();
                try {
                    String rootDir = new File(appInfo.dataDir).getCanonicalPath();
                    String nobackupDir = BackupAgent.this.getNoBackupFilesDir().getCanonicalPath();
                    String cacheDir = BackupAgent.this.getCacheDir().getCanonicalPath();
                    String codeCacheDir = BackupAgent.this.getCodeCacheDir().getCanonicalPath();
                    String libDir = appInfo.nativeLibraryDir != null ? new File(appInfo.nativeLibraryDir).getCanonicalPath() : null;
                    String efDir = BackupAgent.this.getExternalFilesDir(null).getCanonicalPath();
                    traversalExcludeSet.add(cacheDir);
                    traversalExcludeSet.add(codeCacheDir);
                    traversalExcludeSet.add(nobackupDir);
                    if (libDir != null) {
                        traversalExcludeSet.add(libDir);
                    }
                    if (manifestIncludeMap == null || manifestIncludeMap.size() == 0) {
                        contentSize = BackupAgent.this.getDirectorySizeEx(rootDir, manifestExcludeSet, traversalExcludeSet) + BackupAgent.this.getDirectorySizeEx(efDir, manifestExcludeSet, traversalExcludeSet);
                        return contentSize;
                    }
                    for (Entry<String, Set<String>> domain : manifestIncludeMap.entrySet()) {
                        for (String includeFile : (Set) domain.getValue()) {
                            contentSize += BackupAgent.this.getDirectorySizeEx(includeFile, manifestExcludeSet, traversalExcludeSet);
                        }
                    }
                    return contentSize;
                } catch (IOException e) {
                    Log.e(TAG, "Unable to get size of app data : ", e);
                }
            } catch (IOException e2) {
                return -1;
            } catch (XmlPullParserException e3) {
                return -1;
            }
        }
    }

    static class FailRunnable implements Runnable {
        private String mMessage;

        FailRunnable(String message) {
            this.mMessage = message;
        }

        public void run() {
            throw new IllegalStateException(this.mMessage);
        }
    }

    class SharedPrefsSynchronizer implements Runnable {
        public final CountDownLatch mLatch = new CountDownLatch(1);

        SharedPrefsSynchronizer() {
        }

        public void run() {
            QueuedWork.waitToFinish();
            this.mLatch.countDown();
        }
    }

    public abstract void onBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) throws IOException;

    public abstract void onRestore(BackupDataInput backupDataInput, int i, ParcelFileDescriptor parcelFileDescriptor) throws IOException;

    Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        return this.mHandler;
    }

    private void waitForSharedPrefs() {
        Handler h = getHandler();
        SharedPrefsSynchronizer s = new SharedPrefsSynchronizer();
        h.postAtFrontOfQueue(s);
        try {
            s.mLatch.await();
        } catch (InterruptedException e) {
        }
    }

    public BackupAgent() {
        super(null);
    }

    public void onCreate() {
    }

    public void onDestroy() {
    }

    public void onFullBackup(FullBackupDataOutput data) throws IOException {
        Exception e;
        BackupScheme backupScheme = FullBackup.getBackupScheme(this);
        if (backupScheme.isFullBackupContentEnabled()) {
            try {
                Map<String, Set<String>> manifestIncludeMap = backupScheme.maybeParseAndGetCanonicalIncludePaths();
                ArraySet<String> manifestExcludeSet = backupScheme.maybeParseAndGetCanonicalExcludePaths();
                String packageName = getPackageName();
                ApplicationInfo appInfo = getApplicationInfo();
                String rootDir = new File(appInfo.dataDir).getCanonicalPath();
                String filesDir = getFilesDir().getCanonicalPath();
                String nobackupDir = getNoBackupFilesDir().getCanonicalPath();
                String databaseDir = getDatabasePath("foo").getParentFile().getCanonicalPath();
                String sharedPrefsDir = getSharedPrefsFile("foo").getParentFile().getCanonicalPath();
                String cacheDir = getCacheDir().getCanonicalPath();
                String codeCacheDir = getCodeCacheDir().getCanonicalPath();
                String libDir = appInfo.nativeLibraryDir != null ? new File(appInfo.nativeLibraryDir).getCanonicalPath() : null;
                ArraySet<String> traversalExcludeSet = new ArraySet();
                traversalExcludeSet.add(cacheDir);
                traversalExcludeSet.add(codeCacheDir);
                traversalExcludeSet.add(nobackupDir);
                if (libDir != null) {
                    traversalExcludeSet.add(libDir);
                }
                traversalExcludeSet.add(databaseDir);
                traversalExcludeSet.add(sharedPrefsDir);
                traversalExcludeSet.add(filesDir);
                applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.ROOT_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
                traversalExcludeSet.add(rootDir);
                traversalExcludeSet.remove(filesDir);
                applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.DATA_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
                traversalExcludeSet.add(filesDir);
                traversalExcludeSet.remove(databaseDir);
                applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.DATABASE_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
                traversalExcludeSet.add(databaseDir);
                traversalExcludeSet.remove(sharedPrefsDir);
                applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.SHAREDPREFS_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
                traversalExcludeSet.add(sharedPrefsDir);
                if (Process.myUid() != 1000 && getExternalFilesDir(null) != null) {
                    applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.MANAGED_EXTERNAL_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
                }
            } catch (IOException e2) {
                e = e2;
                if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                    Log.v("BackupXmlParserLogging", "Exception trying to parse fullBackupContent xml file! Aborting full backup.", e);
                }
            } catch (XmlPullParserException e3) {
                e = e3;
                if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                    Log.v("BackupXmlParserLogging", "Exception trying to parse fullBackupContent xml file! Aborting full backup.", e);
                }
            }
        }
    }

    private void applyXmlFiltersAndDoFullBackupForDomain(String packageName, String domainToken, Map<String, Set<String>> includeMap, ArraySet<String> filterSet, ArraySet<String> traversalExcludeSet, FullBackupDataOutput data) throws IOException {
        if (includeMap == null || includeMap.size() == 0) {
            fullBackupFileTree(packageName, domainToken, FullBackup.getBackupScheme(this).tokenToDirectoryPath(domainToken), filterSet, traversalExcludeSet, data);
        } else if (includeMap.get(domainToken) != null) {
            for (String includeFile : (Set) includeMap.get(domainToken)) {
                fullBackupFileTree(packageName, domainToken, includeFile, filterSet, traversalExcludeSet, data);
            }
        }
    }

    public final void fullBackupFile(File file, FullBackupDataOutput output) {
        String efDir = null;
        ApplicationInfo appInfo = getApplicationInfo();
        try {
            String mainDir = new File(appInfo.dataDir).getCanonicalPath();
            String filesDir = getFilesDir().getCanonicalPath();
            String nbFilesDir = getNoBackupFilesDir().getCanonicalPath();
            String dbDir = getDatabasePath("foo").getParentFile().getCanonicalPath();
            String spDir = getSharedPrefsFile("foo").getParentFile().getCanonicalPath();
            String cacheDir = getCacheDir().getCanonicalPath();
            String codeCacheDir = getCodeCacheDir().getCanonicalPath();
            String libDir;
            if (appInfo.nativeLibraryDir == null) {
                libDir = null;
            } else {
                libDir = new File(appInfo.nativeLibraryDir).getCanonicalPath();
            }
            if (Process.myUid() != 1000) {
                File efLocation = getExternalFilesDir(null);
                if (efLocation != null) {
                    efDir = efLocation.getCanonicalPath();
                }
            }
            String filePath = file.getCanonicalPath();
            if (filePath.startsWith(cacheDir) || filePath.startsWith(codeCacheDir) || filePath.startsWith(libDir) || filePath.startsWith(nbFilesDir)) {
                Log.w(TAG, "lib, cache, code_cache, and no_backup files are not backed up");
                return;
            }
            String domain;
            String rootpath;
            if (filePath.startsWith(dbDir)) {
                domain = FullBackup.DATABASE_TREE_TOKEN;
                rootpath = dbDir;
            } else if (filePath.startsWith(spDir)) {
                domain = FullBackup.SHAREDPREFS_TREE_TOKEN;
                rootpath = spDir;
            } else if (filePath.startsWith(filesDir)) {
                domain = FullBackup.DATA_TREE_TOKEN;
                rootpath = filesDir;
            } else if (filePath.startsWith(mainDir)) {
                domain = FullBackup.ROOT_TREE_TOKEN;
                rootpath = mainDir;
            } else if (efDir == null || !filePath.startsWith(efDir)) {
                Log.w(TAG, "File " + filePath + " is in an unsupported location; skipping");
                return;
            } else {
                domain = FullBackup.MANAGED_EXTERNAL_TREE_TOKEN;
                rootpath = efDir;
            }
            FullBackup.backupToTar(getPackageName(), domain, null, rootpath, filePath, output);
        } catch (IOException e) {
            Log.w(TAG, "Unable to obtain canonical paths");
        }
    }

    protected final void fullBackupFileTree(String packageName, String domain, String startingPath, ArraySet<String> manifestExcludes, ArraySet<String> systemExcludes, FullBackupDataOutput output) {
        String domainPath = FullBackup.getBackupScheme(this).tokenToDirectoryPath(domain);
        if (domainPath != null) {
            File rootFile = new File(startingPath);
            if (rootFile.exists()) {
                LinkedList<File> scanQueue = new LinkedList();
                scanQueue.add(rootFile);
                while (scanQueue.size() > 0) {
                    File file = (File) scanQueue.remove(0);
                    try {
                        String filePath = file.getCanonicalPath();
                        if ((manifestExcludes == null || !manifestExcludes.contains(filePath)) && (systemExcludes == null || !systemExcludes.contains(filePath))) {
                            StructStat stat = Os.lstat(filePath);
                            if (!OsConstants.S_ISLNK(stat.st_mode)) {
                                if (OsConstants.S_ISDIR(stat.st_mode)) {
                                    File[] contents = file.listFiles();
                                    if (contents != null) {
                                        for (File entry : contents) {
                                            scanQueue.add(0, entry);
                                        }
                                    }
                                }
                                FullBackup.backupToTar(packageName, domain, null, domainPath, filePath, output);
                            }
                        }
                    } catch (IOException e) {
                        if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                            Log.v("BackupXmlParserLogging", "Error canonicalizing path of " + file);
                        }
                    } catch (ErrnoException e2) {
                        if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                            Log.v("BackupXmlParserLogging", "Error scanning file " + file + " : " + e2);
                        }
                    }
                }
            }
        }
    }

    private long getDirectorySizeEx(String startingPath, ArraySet<String> manifestExcludes, ArraySet<String> systemExcludes) {
        File rootFile = new File(startingPath);
        long directorySize = 0;
        if (rootFile.exists()) {
            LinkedList<File> scanQueue = new LinkedList();
            scanQueue.add(rootFile);
            while (scanQueue.size() > 0) {
                File file = (File) scanQueue.remove(0);
                try {
                    String filePath = file.getCanonicalPath();
                    if ((manifestExcludes == null || !manifestExcludes.contains(filePath)) && (systemExcludes == null || !systemExcludes.contains(filePath))) {
                        StructStat stat = Os.lstat(filePath);
                        if (!OsConstants.S_ISLNK(stat.st_mode)) {
                            if (OsConstants.S_ISDIR(stat.st_mode)) {
                                File[] contents = file.listFiles();
                                if (contents != null) {
                                    for (File entry : contents) {
                                        scanQueue.add(0, entry);
                                    }
                                }
                            } else {
                                directorySize += stat.st_size;
                            }
                        }
                    }
                } catch (IOException e) {
                } catch (ErrnoException e2) {
                }
            }
        }
        return directorySize;
    }

    public void onRestoreFile(ParcelFileDescriptor data, long size, File destination, int type, long mode, long mtime) throws IOException {
        FullBackup.restoreFile(data, size, type, mode, mtime, isFileEligibleForRestore(destination) ? destination : null);
    }

    private boolean isFileEligibleForRestore(File destination) throws IOException {
        BackupScheme bs = FullBackup.getBackupScheme(this);
        if (bs.isFullBackupContentEnabled()) {
            String destinationCanonicalPath = destination.getCanonicalPath();
            try {
                Map<String, Set<String>> includes = bs.maybeParseAndGetCanonicalIncludePaths();
                ArraySet<String> excludes = bs.maybeParseAndGetCanonicalExcludePaths();
                if (excludes == null || !isFileSpecifiedInPathList(destination, excludes)) {
                    if (!(includes == null || includes.isEmpty())) {
                        boolean explicitlyIncluded = false;
                        for (Set<String> domainIncludes : includes.values()) {
                            explicitlyIncluded |= isFileSpecifiedInPathList(destination, domainIncludes);
                            if (explicitlyIncluded) {
                                break;
                            }
                        }
                        if (!explicitlyIncluded) {
                            if (!Log.isLoggable("BackupXmlParserLogging", 2)) {
                                return false;
                            }
                            Log.v("BackupXmlParserLogging", "onRestoreFile: Trying to restore \"" + destinationCanonicalPath + "\" but it isn't specified" + " in the included files; skipping.");
                            return false;
                        }
                    }
                    return true;
                } else if (!Log.isLoggable("BackupXmlParserLogging", 2)) {
                    return false;
                } else {
                    Log.v("BackupXmlParserLogging", "onRestoreFile: \"" + destinationCanonicalPath + "\": listed in" + " excludes; skipping.");
                    return false;
                }
            } catch (XmlPullParserException e) {
                if (!Log.isLoggable("BackupXmlParserLogging", 2)) {
                    return false;
                }
                Log.v("BackupXmlParserLogging", "onRestoreFile \"" + destinationCanonicalPath + "\" : Exception trying to parse fullBackupContent xml file!" + " Aborting onRestoreFile.", e);
                return false;
            }
        } else if (!Log.isLoggable("BackupXmlParserLogging", 2)) {
            return false;
        } else {
            Log.v("BackupXmlParserLogging", "onRestoreFile \"" + destination.getCanonicalPath() + "\" : fullBackupContent not enabled for " + getPackageName());
            return false;
        }
    }

    private boolean isFileSpecifiedInPathList(File file, Collection<String> canonicalPathList) throws IOException {
        for (String canonicalPath : canonicalPathList) {
            File fileFromList = new File(canonicalPath);
            if (fileFromList.isDirectory()) {
                if (file.isDirectory()) {
                    return file.equals(fileFromList);
                }
                return file.getCanonicalPath().startsWith(canonicalPath);
            } else if (file.equals(fileFromList)) {
                return true;
            }
        }
        return false;
    }

    protected void onRestoreFile(ParcelFileDescriptor data, long size, int type, String domain, String path, long mode, long mtime) throws IOException {
        String basePath = FullBackup.getBackupScheme(this).tokenToDirectoryPath(domain);
        if (domain.equals(FullBackup.MANAGED_EXTERNAL_TREE_TOKEN)) {
            mode = -1;
        }
        if (basePath != null) {
            File outFile = new File(basePath, path);
            if (outFile.getCanonicalPath().startsWith(basePath + File.separatorChar)) {
                onRestoreFile(data, size, outFile, type, mode, mtime);
                return;
            }
        }
        FullBackup.restoreFile(data, size, type, mode, mtime, null);
    }

    public void onRestoreFinished() {
    }

    public final IBinder onBind() {
        return this.mBinder;
    }

    public void attach(Context context) {
        attachBaseContext(context);
    }
}
