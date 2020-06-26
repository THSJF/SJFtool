/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.content;

import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import java.io.*;

import android.os.Process;

public class ContextCompat {
    private static final String TAG = "ContextCompat";

    private static final String DIR_ANDROID = "Android";
    private static final String DIR_DATA = "data";
    private static final String DIR_OBB = "obb";
    private static final String DIR_FILES = "files";
    private static final String DIR_CACHE = "cache";
 
    public static boolean startActivities(Context context, Intent[] intents) {
        return startActivities(context, intents, null);
    }
    
    public static boolean startActivities(Context context, Intent[] intents,
            Bundle options) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 16) {
            ContextCompatJellybean.startActivities(context, intents, options);
            return true;
        } else if (version >= 11) {
            ContextCompatHoneycomb.startActivities(context, intents);
            return true;
        }
        return false;
    }
    
    public static File[] getObbDirs(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return ContextCompatKitKat.getObbDirs(context);
        } else {
            final File single;
            if (version >= 11) {
                single = ContextCompatHoneycomb.getObbDir(context);
            } else {
                single = buildPath(Environment.getExternalStorageDirectory(), DIR_ANDROID, DIR_OBB,
                        context.getPackageName());
            }
            return new File[] { single };
        }
    }

    public static File[] getExternalFilesDirs(Context context, String type) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return ContextCompatKitKat.getExternalFilesDirs(context, type);
        } else {
            final File single;
            if (version >= 8) {
                single = ContextCompatFroyo.getExternalFilesDir(context, type);
            } else {
                single = buildPath(Environment.getExternalStorageDirectory(), DIR_ANDROID, DIR_DATA,
                        context.getPackageName(), DIR_FILES, type);
            }
            return new File[] { single };
        }
    }
    
    public static File[] getExternalCacheDirs(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return ContextCompatKitKat.getExternalCacheDirs(context);
        } else {
            final File single;
            if (version >= 8) {
                single = ContextCompatFroyo.getExternalCacheDir(context);
            } else {
                single = buildPath(Environment.getExternalStorageDirectory(), DIR_ANDROID, DIR_DATA,
                        context.getPackageName(), DIR_CACHE);
            }
            return new File[] { single };
        }
    }

    private static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (cur == null) {
                cur = new File(segment);
            } else if (segment != null) {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }

    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompatApi21.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static final ColorStateList getColorStateList(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompatApi23.getColorStateList(context, id);
        } else {
            return context.getResources().getColorStateList(id);
        }
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompatApi23.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static int checkSelfPermission(Context context, String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }

        return context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
    }

    public final File getNoBackupFilesDir(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompatApi21.getNoBackupFilesDir(context);
        } else {
            ApplicationInfo appInfo = context.getApplicationInfo();
            return createFilesDir(new File(appInfo.dataDir, "no_backup"));
        }
    }

    public static File getCodeCacheDir(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompatApi21.getCodeCacheDir(context);
        } else {
            ApplicationInfo appInfo = context.getApplicationInfo();
            return createFilesDir(new File(appInfo.dataDir, "code_cache"));
        }
    }

    private synchronized static File createFilesDir(File file) {
        if (!file.exists()) {
            if (!file.mkdirs()) {
                if (file.exists()) {
                    // spurious failure; probably racing with another process for this app
                    return file;
                }
                Log.w(TAG, "Unable to create files subdir " + file.getPath());
                return null;
            }
        }
        return file;
    }
}
