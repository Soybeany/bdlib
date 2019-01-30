package com.soybeany.bdlib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() throws Exception {
        String str = "中国";
        printBytes("utf-8", getUTF_8(str));
        printBytes("gbk", getGbk(str));
        printBytes("iso-8859-1", getISO(str));

        String s = getISOStr(getGbk(str));
        System.out.println(s + " " + s.length() + " " + getGbkStr(getISO(s)));

    }

    private String getUTF_8Str(byte[] data) throws Exception {
        return new String(data, "utf-8");
    }

    private String getGbkStr(byte[] data) throws Exception {
        return new String(data, "gbk");
    }

    private String getISOStr(byte[] data) throws Exception {
        return new String(data, "iso-8859-1");
    }

    private byte[] getUTF_8(String data) throws Exception {
        return data.getBytes("utf-8");
    }

    private byte[] getGbk(String data) throws Exception {
        return data.getBytes("gbk");
    }

    private byte[] getISO(String data) throws Exception {
        return data.getBytes("iso-8859-1");
    }

    private void printBytes(String tag, byte[] data) {
        System.out.print(tag + ": ");
        for (byte b : data) {
            System.out.print(b + " ");
        }
        System.out.println();
    }

}