package org.moxie.ply;

import java.io.*;
import java.net.URL;

/**
 * User: blangel
 * Date: 10/3/11
 * Time: 6:35 PM
 *
 * Provides utilities when interacting with {@link File} objects.
 */
public final class FileUtil {

    /**
     * Copies the contents of {@code fromDir} to {@code toDir} recursively.
     * @param fromDir from which to copy
     * @param toDir to which to copy
     * @return true on success; false otherwise
     */
    public static boolean copyDir(File fromDir, File toDir) {
        if (!fromDir.isDirectory() || !fromDir.exists()) {
            return false;
        }
        toDir.mkdirs();
        for (File subFile : fromDir.listFiles()) {
            File toDirSubFile = new File(toDir.getPath() + File.separator + subFile.getName());
            if (subFile.isDirectory()) {
                if (!copyDir(subFile, toDirSubFile)) {
                    return false;
                }
            } else if (!copy(subFile, toDirSubFile)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Copies {@code from} to {@code to}, creating all directories up to {@code to} and the file itself.
     * @param from which to copy
     * @param to which to copy
     * @return true on success; false otherwise
     */
    public static boolean copy(File from, File to) {
        try {
            return copy(new BufferedInputStream(new FileInputStream(from)), to);
        } catch (FileNotFoundException fnfe) {
            Output.print(fnfe);
        }
        return false;
    }

    /**
     * Saves {@code from} to {@code to}.
     * @param from which to copy
     * @param to which to copy
     * @return true if success; false otherwise
     */
    public static boolean copy(URL from, File to) {
        InputStream inputStream = null;
        try {
            inputStream = from.openStream();
            return copy(inputStream, to);
        } catch (IOException ioe) {
            Output.print(ioe);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }
        return false;
    }

    /**
     * Copies {@code from} to {@code to}.  Creates {@code to} if it does not exist (including any sub-directory).
     * @param from which to copy
     * @param to which to copy
     * @return true if success; false otherwise
     */
    private static boolean copy(InputStream from, File to) {
        OutputStream outputStream = null;
        try {
            if (!to.exists()) {
                to.getParentFile().mkdirs();
                to.createNewFile();
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(to));
            byte[] tx = new byte[8192];
            int read;
            while ((read = from.read(tx)) != -1) {
                outputStream.write(tx, 0, read);
            }
            return true;
        } catch (IOException ioe) {
            Output.print(ioe);
        } finally {
            try {
                if (from != null) {
                    from.close();
                }
            } catch (IOException ioe) {
                // ignore
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                // ignore
            }
        }
        return false;
    }

    private FileUtil() { }

}