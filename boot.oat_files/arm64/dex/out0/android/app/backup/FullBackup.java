package android.app.backup;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.XmlResourceParser;
import android.net.ProxyInfo;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FullBackup {
    public static final String APK_TREE_TOKEN = "a";
    public static final String APPS_PREFIX = "apps/";
    public static final String CACHE_TREE_TOKEN = "c";
    public static final String CONF_TOKEN_INTENT_EXTRA = "conftoken";
    public static final String DATABASE_TREE_TOKEN = "db";
    public static final String DATA_TREE_TOKEN = "f";
    public static final String FULL_BACKUP_INTENT_ACTION = "fullback";
    public static final String FULL_RESTORE_INTENT_ACTION = "fullrest";
    public static final String MANAGED_EXTERNAL_TREE_TOKEN = "ef";
    public static final String NO_BACKUP_TREE_TOKEN = "nb";
    public static final String OBB_TREE_TOKEN = "obb";
    public static final String ROOT_TREE_TOKEN = "r";
    public static final String SHAREDPREFS_TREE_TOKEN = "sp";
    public static final String SHARED_PREFIX = "shared/";
    public static final String SHARED_STORAGE_TOKEN = "shared";
    static final String TAG = "FullBackup";
    static final String TAG_XML_PARSER = "BackupXmlParserLogging";
    private static final Map<String, BackupScheme> kPackageBackupSchemeMap = new ArrayMap();

    public static class BackupScheme {
        private final File CACHE_DIR;
        private final File DATABASE_DIR;
        private final File EXTERNAL_DIR;
        private final File FILES_DIR;
        private final File NOBACKUP_DIR;
        private final File ROOT_DIR;
        private final File SHAREDPREF_DIR;
        ArraySet<String> mExcludes;
        final int mFullBackupContent;
        Map<String, Set<String>> mIncludes;
        final PackageManager mPackageManager;
        final String mPackageName;

        String tokenToDirectoryPath(String domainToken) {
            try {
                if (domainToken.equals(FullBackup.DATA_TREE_TOKEN)) {
                    return this.FILES_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.DATABASE_TREE_TOKEN)) {
                    return this.DATABASE_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.ROOT_TREE_TOKEN)) {
                    return this.ROOT_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.SHAREDPREFS_TREE_TOKEN)) {
                    return this.SHAREDPREF_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.CACHE_TREE_TOKEN)) {
                    return this.CACHE_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.MANAGED_EXTERNAL_TREE_TOKEN)) {
                    if (this.EXTERNAL_DIR != null) {
                        return this.EXTERNAL_DIR.getCanonicalPath();
                    }
                    return null;
                } else if (domainToken.equals(FullBackup.NO_BACKUP_TREE_TOKEN)) {
                    return this.NOBACKUP_DIR.getCanonicalPath();
                } else {
                    Log.i(FullBackup.TAG, "Unrecognized domain " + domainToken);
                    return null;
                }
            } catch (IOException e) {
                Log.i(FullBackup.TAG, "Error reading directory for domain: " + domainToken);
                return null;
            }
        }

        BackupScheme(Context context) {
            this.mFullBackupContent = context.getApplicationInfo().fullBackupContent;
            this.mPackageManager = context.getPackageManager();
            this.mPackageName = context.getPackageName();
            this.FILES_DIR = context.getFilesDir();
            this.DATABASE_DIR = context.getDatabasePath("foo").getParentFile();
            this.ROOT_DIR = new File(context.getApplicationInfo().dataDir);
            this.SHAREDPREF_DIR = context.getSharedPrefsFile("foo").getParentFile();
            this.CACHE_DIR = context.getCacheDir();
            this.NOBACKUP_DIR = context.getNoBackupFilesDir();
            if (Process.myUid() != 1000) {
                this.EXTERNAL_DIR = context.getExternalFilesDir(null);
            } else {
                this.EXTERNAL_DIR = null;
            }
        }

        boolean isFullBackupContentEnabled() {
            if (this.mFullBackupContent >= 0) {
                return true;
            }
            if (Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                Log.v(FullBackup.TAG_XML_PARSER, "android:fullBackupContent - \"false\"");
            }
            return false;
        }

        public synchronized Map<String, Set<String>> maybeParseAndGetCanonicalIncludePaths() throws IOException, XmlPullParserException {
            if (this.mIncludes == null) {
                maybeParseBackupSchemeLocked();
            }
            return this.mIncludes;
        }

        public synchronized ArraySet<String> maybeParseAndGetCanonicalExcludePaths() throws IOException, XmlPullParserException {
            if (this.mExcludes == null) {
                maybeParseBackupSchemeLocked();
            }
            return this.mExcludes;
        }

        private void maybeParseBackupSchemeLocked() throws IOException, XmlPullParserException {
            this.mIncludes = new ArrayMap();
            this.mExcludes = new ArraySet();
            if (this.mFullBackupContent != 0) {
                if (Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                    Log.v(FullBackup.TAG_XML_PARSER, "android:fullBackupContent - found xml resource");
                }
                XmlResourceParser parser = null;
                try {
                    parser = this.mPackageManager.getResourcesForApplication(this.mPackageName).getXml(this.mFullBackupContent);
                    parseBackupSchemeFromXmlLocked(parser, this.mExcludes, this.mIncludes);
                    if (parser != null) {
                        parser.close();
                    }
                } catch (NameNotFoundException e) {
                    throw new IOException(e);
                } catch (Throwable th) {
                    if (parser != null) {
                        parser.close();
                    }
                }
            } else if (Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                Log.v(FullBackup.TAG_XML_PARSER, "android:fullBackupContent - \"true\"");
            }
        }

        public void parseBackupSchemeFromXmlLocked(XmlPullParser parser, Set<String> excludes, Map<String, Set<String>> includes) throws IOException, XmlPullParserException {
            int event = parser.getEventType();
            while (event != 2) {
                event = parser.next();
            }
            if ("full-backup-content".equals(parser.getName())) {
                if (Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                    Log.v(FullBackup.TAG_XML_PARSER, "\n");
                    Log.v(FullBackup.TAG_XML_PARSER, "====================================================");
                    Log.v(FullBackup.TAG_XML_PARSER, "Found valid fullBackupContent; parsing xml resource.");
                    Log.v(FullBackup.TAG_XML_PARSER, "====================================================");
                    Log.v(FullBackup.TAG_XML_PARSER, ProxyInfo.LOCAL_EXCL_LIST);
                }
                while (true) {
                    event = parser.next();
                    if (event != 1) {
                        switch (event) {
                            case 2:
                                validateInnerTagContents(parser);
                                String domainFromXml = parser.getAttributeValue(null, "domain");
                                File domainDirectory = getDirectoryForCriteriaDomain(domainFromXml);
                                if (domainDirectory == null) {
                                    if (!Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                                        break;
                                    }
                                    Log.v(FullBackup.TAG_XML_PARSER, "...parsing \"" + parser.getName() + "\": " + "domain=\"" + domainFromXml + "\" invalid; skipping");
                                    break;
                                }
                                File canonicalFile = extractCanonicalFile(domainDirectory, parser.getAttributeValue(null, "path"));
                                if (canonicalFile == null) {
                                    break;
                                }
                                Set<String> activeSet = parseCurrentTagForDomain(parser, excludes, includes, domainFromXml);
                                activeSet.add(canonicalFile.getCanonicalPath());
                                if (Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                                    Log.v(FullBackup.TAG_XML_PARSER, "...parsed " + canonicalFile.getCanonicalPath() + " for domain \"" + domainFromXml + "\"");
                                }
                                if ("database".equals(domainFromXml) && !canonicalFile.isDirectory()) {
                                    String canonicalJournalPath = canonicalFile.getCanonicalPath() + "-journal";
                                    activeSet.add(canonicalJournalPath);
                                    if (!Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                                        break;
                                    }
                                    Log.v(FullBackup.TAG_XML_PARSER, "...automatically generated " + canonicalJournalPath + ". Ignore if nonexistant.");
                                    break;
                                }
                            default:
                                break;
                        }
                    } else if (Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                        Log.v(FullBackup.TAG_XML_PARSER, "\n");
                        Log.v(FullBackup.TAG_XML_PARSER, "Xml resource parsing complete.");
                        Log.v(FullBackup.TAG_XML_PARSER, "Final tally.");
                        Log.v(FullBackup.TAG_XML_PARSER, "Includes:");
                        if (includes.isEmpty()) {
                            Log.v(FullBackup.TAG_XML_PARSER, "  ...nothing specified (This means the entirety of app data minus excludes)");
                        } else {
                            for (Entry<String, Set<String>> entry : includes.entrySet()) {
                                String str = FullBackup.TAG_XML_PARSER;
                                String str2 = str;
                                Log.v(str2, "  domain=" + ((String) entry.getKey()));
                                for (String includeData : (Set) entry.getValue()) {
                                    Log.v(FullBackup.TAG_XML_PARSER, "  " + includeData);
                                }
                            }
                        }
                        Log.v(FullBackup.TAG_XML_PARSER, "Excludes:");
                        if (excludes.isEmpty()) {
                            Log.v(FullBackup.TAG_XML_PARSER, "  ...nothing to exclude.");
                        } else {
                            for (String excludeData : excludes) {
                                Log.v(FullBackup.TAG_XML_PARSER, "  " + excludeData);
                            }
                        }
                        Log.v(FullBackup.TAG_XML_PARSER, "  ");
                        Log.v(FullBackup.TAG_XML_PARSER, "====================================================");
                        Log.v(FullBackup.TAG_XML_PARSER, "\n");
                        return;
                    } else {
                        return;
                    }
                }
            }
            throw new XmlPullParserException("Xml file didn't start with correct tag (<full-backup-content>). Found \"" + parser.getName() + "\"");
        }

        private Set<String> parseCurrentTagForDomain(XmlPullParser parser, Set<String> excludes, Map<String, Set<String>> includes, String domain) throws XmlPullParserException {
            if ("include".equals(parser.getName())) {
                String domainToken = getTokenForXmlDomain(domain);
                Set<String> includeSet = (Set) includes.get(domainToken);
                if (includeSet != null) {
                    return includeSet;
                }
                includeSet = new ArraySet();
                includes.put(domainToken, includeSet);
                return includeSet;
            } else if ("exclude".equals(parser.getName())) {
                return excludes;
            } else {
                if (Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                    Log.v(FullBackup.TAG_XML_PARSER, "Invalid tag found in xml \"" + parser.getName() + "\"; aborting operation.");
                }
                throw new XmlPullParserException("Unrecognised tag in backup criteria xml (" + parser.getName() + ")");
            }
        }

        private String getTokenForXmlDomain(String xmlDomain) {
            if ("root".equals(xmlDomain)) {
                return FullBackup.ROOT_TREE_TOKEN;
            }
            if (ContentResolver.SCHEME_FILE.equals(xmlDomain)) {
                return FullBackup.DATA_TREE_TOKEN;
            }
            if ("database".equals(xmlDomain)) {
                return FullBackup.DATABASE_TREE_TOKEN;
            }
            if ("sharedpref".equals(xmlDomain)) {
                return FullBackup.SHAREDPREFS_TREE_TOKEN;
            }
            if ("external".equals(xmlDomain)) {
                return FullBackup.MANAGED_EXTERNAL_TREE_TOKEN;
            }
            return null;
        }

        private File extractCanonicalFile(File domain, String filePathFromXml) {
            if (filePathFromXml == null) {
                filePathFromXml = ProxyInfo.LOCAL_EXCL_LIST;
            }
            if (filePathFromXml.contains("..")) {
                if (!Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                    return null;
                }
                Log.v(FullBackup.TAG_XML_PARSER, "...resolved \"" + domain.getPath() + " " + filePathFromXml + "\", but the \"..\" path is not permitted; skipping.");
                return null;
            } else if (!filePathFromXml.contains("//")) {
                return new File(domain, filePathFromXml);
            } else {
                if (!Log.isLoggable(FullBackup.TAG_XML_PARSER, 2)) {
                    return null;
                }
                Log.v(FullBackup.TAG_XML_PARSER, "...resolved \"" + domain.getPath() + " " + filePathFromXml + "\", which contains the invalid \"//\" sequence; skipping.");
                return null;
            }
        }

        private File getDirectoryForCriteriaDomain(String domain) {
            if (TextUtils.isEmpty(domain)) {
                return null;
            }
            if (ContentResolver.SCHEME_FILE.equals(domain)) {
                return this.FILES_DIR;
            }
            if ("database".equals(domain)) {
                return this.DATABASE_DIR;
            }
            if ("root".equals(domain)) {
                return this.ROOT_DIR;
            }
            if ("sharedpref".equals(domain)) {
                return this.SHAREDPREF_DIR;
            }
            if ("external".equals(domain)) {
                return this.EXTERNAL_DIR;
            }
            return null;
        }

        private void validateInnerTagContents(XmlPullParser parser) throws XmlPullParserException {
            if (parser.getAttributeCount() > 2) {
                throw new XmlPullParserException("At most 2 tag attributes allowed for \"" + parser.getName() + "\" tag (\"domain\" & \"path\".");
            } else if (!"include".equals(parser.getName()) && !"exclude".equals(parser.getName())) {
                throw new XmlPullParserException("A valid tag is one of \"<include/>\" or \"<exclude/>. You provided \"" + parser.getName() + "\"");
            }
        }
    }

    public static native int backupToTar(String str, String str2, String str3, String str4, String str5, FullBackupDataOutput fullBackupDataOutput);

    static synchronized BackupScheme getBackupScheme(Context context) {
        BackupScheme backupSchemeForPackage;
        synchronized (FullBackup.class) {
            backupSchemeForPackage = (BackupScheme) kPackageBackupSchemeMap.get(context.getPackageName());
            if (backupSchemeForPackage == null) {
                backupSchemeForPackage = new BackupScheme(context);
                kPackageBackupSchemeMap.put(context.getPackageName(), backupSchemeForPackage);
            }
        }
        return backupSchemeForPackage;
    }

    public static BackupScheme getBackupSchemeForTest(Context context) {
        BackupScheme testing = new BackupScheme(context);
        testing.mExcludes = new ArraySet();
        testing.mIncludes = new ArrayMap();
        return testing;
    }

    public static void restoreFile(ParcelFileDescriptor data, long size, int type, long mode, long mtime, File outFile) throws IOException {
        if (type != 2) {
            FileOutputStream fileOutputStream = null;
            if (outFile != null) {
                try {
                    File parent = outFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                } catch (IOException e) {
                    Log.e(TAG, "Unable to create/open file " + outFile.getPath(), e);
                }
            }
            byte[] buffer = new byte[32768];
            long origSize = size;
            FileInputStream in = new FileInputStream(data.getFileDescriptor());
            while (size > 0) {
                int got = in.read(buffer, 0, size > ((long) buffer.length) ? buffer.length : (int) size);
                if (got <= 0) {
                    Log.w(TAG, "Incomplete read: expected " + size + " but got " + (origSize - size));
                    break;
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.write(buffer, 0, got);
                    } catch (IOException e2) {
                        Log.e(TAG, "Unable to write to file " + outFile.getPath(), e2);
                        fileOutputStream.close();
                        fileOutputStream = null;
                        outFile.delete();
                    }
                }
                size -= (long) got;
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } else if (outFile != null) {
            outFile.mkdirs();
        }
        if (mode >= 0 && outFile != null) {
            try {
                Os.chmod(outFile.getPath(), (int) (mode & 448));
            } catch (ErrnoException e3) {
                e3.rethrowAsIOException();
            }
            outFile.setLastModified(mtime);
        }
    }
}
