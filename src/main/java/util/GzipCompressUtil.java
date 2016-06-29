package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP压缩工具类
 * Created by zyy on 2016/6/17.
 */
public class GzipCompressUtil {

    /**
     * Gzip压缩
     */
    public  static byte[] gzip(byte[] sourceBytes) {
        if (sourceBytes == null || sourceBytes.length <= 0) {
            return null;
        }
        byte[] targetBytes = null;
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gzipOs = null;
        try {
            bos = new ByteArrayOutputStream();
            gzipOs = new GZIPOutputStream(bos);
            gzipOs.write(sourceBytes);
            gzipOs.finish();
            targetBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzipOs != null) {
                try {
                    gzipOs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null) {
                        try {
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return targetBytes;
    }

    /**
     * Gzip解压缩
     */
    public  static byte[] gunzip(byte[] sourceBytes) {
        if (sourceBytes == null || sourceBytes.length <= 0) {
            return null;
        }
        byte[] targetBytes = null;
        ByteArrayInputStream bio = null;
        GZIPInputStream gzipis = null;
        ByteArrayOutputStream bos = null;
        try {
            bio = new ByteArrayInputStream(sourceBytes);
            gzipis = new GZIPInputStream(bio);
            bos = new ByteArrayOutputStream();
            byte[] tempBuffer = new byte[1024];
            int numbers = -1;
            while ((numbers = gzipis.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                bos.write(tempBuffer, 0, numbers);
            }
            targetBytes = bos.toByteArray();
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (gzipis != null) {
                        try {
                            gzipis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (bio != null) {
                                try {
                                    bio.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        return targetBytes;

    }
}
