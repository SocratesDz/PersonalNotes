package com.socratesdiaz.personalnotes;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by socratesdiaz on 11/15/16.
 */
final public class GoogleDriveUtility {

    private GoogleDriveUtility() {

    }

    private static final String L_TAG = "_";

    static final String MYROOT = "PersonalNotes";
    static final String TMP_FILENAME = "temp";
    static final String JPEG_EXT = AppConstant.JPG;
    static final String MIME_JPEG = "image/jpeg";
    static final String MIME_FOLDER = "application/vnd.google-apps.folder";

    static final String TITLE_FORMAT = "yyMMdd-HHmmss";

    static final int BUFFER_SIZE = 2048;

    private static GoogleDriveUtility mInstance;
    static SharedPreferences sSharedPreferences;
    static Context sContext;

    static GoogleDriveUtility init (Context context) {
        if(mInstance == null) {
            sContext = context;
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
            mInstance = new GoogleDriveUtility();
        }
        return mInstance;
    }

    final static class AM {
        private AM() {

        }

        private static final String ACC_NAME = "account_name";
        static final int FAIL = -1;
        static final int UNCHANGED = 0;
        static final int CHANGED = 1;

        private static String mCurrentEmail = null; // cache locally

        static Account[] getAllAccounts() {
            return AccountManager.get(GoogleDriveUtility.sContext)
                    .getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        }

        static Account getPrimaryAccount(boolean boolOnly) {
            Account[] accounts = getAllAccounts();
            if(boolOnly)
                return accounts == null || accounts.length != 1 ? null : accounts[0];
            return accounts == null || accounts.length == 0 ? null : accounts[0];
        }

        static Account getActiveAccount() {
            return emailToAccount(getActiveEmail());
        }

        static String getActiveEmail() {
            if(mCurrentEmail != null) {
                return mCurrentEmail;
            }
            return GoogleDriveUtility.sSharedPreferences.getString(ACC_NAME, null);
        }

        static Account emailToAccount(String email) {
            if(email != null) {
                Account[] accounts =
                        AccountManager.get(GoogleDriveUtility.sContext)
                                .getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                for (Account account : accounts) {
                    if(email.equalsIgnoreCase(account.name)) {
                        return account;
                    }
                }
            }
            return null;
        }

        static int sentEmail(String newEmail) {
            int result = FAIL;

            String prevEmail = getActiveEmail();
            if((prevEmail == null) && (newEmail != null)) {
                result = CHANGED;
            }
            else if((prevEmail != null) && (newEmail == null)) {
                result = UNCHANGED;
            }
            else if(prevEmail != null) {
                result = prevEmail.equalsIgnoreCase(newEmail) ? UNCHANGED : CHANGED;
            }
            if(result == CHANGED) {
                mCurrentEmail = newEmail;
                GoogleDriveUtility.sSharedPreferences.edit().putString(ACC_NAME, newEmail).commit();
            }
            return result;
        }

        static void removeActiveAccount() {
            mCurrentEmail = null;
            GoogleDriveUtility.sSharedPreferences.edit().remove(ACC_NAME).commit();
        }
    }

    static File bytesToFile(byte[] buffer, File file) {
        if(buffer == null || file == null) return null;
        BufferedOutputStream bs = null;
        try {
            bs = new BufferedOutputStream(new FileOutputStream(file));
            bs.write(buffer);
        } catch (Exception e) {
            logError(e);
        } finally {
            if(bs != null) try {
                bs.close();
            } catch (Exception e) {
                logError(e);
            }
        }
        return file;
    }

    static byte[] fileToBytes(File file) {
        if(file != null) try {
            return isToBytes(new FileInputStream(file));
        } catch (Exception e) {
            logError(e);
        }
        return null;
    }

    static byte[] isToBytes(InputStream is) {
        byte[] buffer = null;
        BufferedInputStream bufferIS = null;
        if(is != null) try {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            bufferIS = new BufferedInputStream(is);
            buffer = new byte[BUFFER_SIZE];
            int cnt;
            while ((cnt = bufferIS.read(buffer)) >= 0) {
                byteBuffer.write(buffer, 0, cnt);
            }
            buffer = byteBuffer.size() > 0 ? byteBuffer.toByteArray() : null;
        } catch (Exception e) {
            logError(e);
        } finally {
            try {
                if(bufferIS != null) bufferIS.close();
            } catch (Exception e) {
                logError(e);
            }
        }
        return buffer;
    }

    static String timeToTitle(Long milis) {
        Date dt = (milis == null) ? new Date() : (milis >= 0) ? new Date(milis) : null;
        return (dt == null) ? null : new SimpleDateFormat(TITLE_FORMAT, Locale.US).format(dt);
    }

    static String titleToMonth(String title) {
        return title == null ? null : ("20" + title.substring(0, 2) + "-" + title.substring(2, 4));
    }

    private static String stackToString(Throwable ex) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        try {
            ex.printStackTrace(printWriter);
            return result.toString();
        } finally {
            printWriter.close();
        }
    }

    static void logError(Throwable ex) {
        String msg = (ex == null || ex.getMessage() == null) ? "" : ex.getMessage() + "\n";
        try {
            Log.e(L_TAG, msg + stackToString(ex));
        } catch (Exception e) {

        }
    }
}
