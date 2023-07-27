package site.bleem.boot.socket.utils;

public class DecodeRule {
    public static final byte frame_header = -1;
    public static final byte frame_footer = -2;
    public static final byte frame_EscapeFlag = -3;

    public DecodeRule() {
    }

    public static byte[] shortToBytes(short n) {
        byte[] b = new byte[]{(byte)(n & 255), (byte)(n >> 8 & 255)};
        return b;
    }

    public static int byteToInt(byte data) {
        return data & 255;
    }

    public static String bcdToStrLE(byte[] bytes) {
        char[] temp = new char[bytes.length * 2];

        for(int i = 0; i < bytes.length; ++i) {
            char val = (char)((bytes[i] & 240) >> 4 & 15);
            temp[(bytes.length - i - 1) * 2] = (char)(val > '\t' ? val + 65 - 10 : val + 48);
            val = (char)(bytes[i] & 15);
            temp[(bytes.length - i - 1) * 2 + 1] = (char)(val > '\t' ? val + 65 - 10 : val + 48);
        }

        return new String(temp);
    }

    public static byte[] strLEToBcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        if (len >= 2) {
            len >>= 1;
        }

        byte[] bbt = new byte[len];
        byte[] abt = asc.getBytes();

        for(int p = 0; p < asc.length() / 2; ++p) {
            int j;
            if (abt[2 * p] >= 48 && abt[2 * p] <= 57) {
                j = abt[2 * p] - 48;
            } else if (abt[2 * p] >= 97 && abt[2 * p] <= 122) {
                j = abt[2 * p] - 97 + 10;
            } else {
                j = abt[2 * p] - 65 + 10;
            }

            int k;
            if (abt[2 * p + 1] >= 48 && abt[2 * p + 1] <= 57) {
                k = abt[2 * p + 1] - 48;
            } else if (abt[2 * p + 1] >= 97 && abt[2 * p + 1] <= 122) {
                k = abt[2 * p + 1] - 97 + 10;
            } else {
                k = abt[2 * p + 1] - 65 + 10;
            }

            int a = (j << 4) + k;
            byte b = (byte)a;
            bbt[asc.length() / 2 - p - 1] = b;
        }

        return bbt;
    }
}
