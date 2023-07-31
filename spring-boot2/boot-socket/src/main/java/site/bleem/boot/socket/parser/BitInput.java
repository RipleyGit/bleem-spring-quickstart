package site.bleem.boot.socket.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.BitSet;

public class BitInput {
    protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final ByteInput input;
    private final BitSet bitset = new BitSet(8);
    private int index = 8;
    private int count = 0;

    public BitInput(ByteInput input) {
        if (input == null) {
            throw new NullPointerException("null input");
        } else {
            this.input = input;
        }
    }

    protected int readUnsignedByte(int length) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else if (length > 8) {
            throw new IllegalArgumentException("length(" + length + ") > 8");
        } else {
            int octet;
            int i;
            if (this.index == 8) {
                octet = this.input.readUnsignedByte();
                if (octet == -1) {
                    throw new EOFException("eof");
                }

                ++this.count;

                for(i = 7; i >= 0; --i) {
                    this.bitset.set(i, (octet & 1) == 1);
                    octet >>= 1;
                }

                this.index = 0;
            }

            octet = 8 - this.index;
            i = length - octet;
            if (i > 0) {
                return this.readUnsignedByte(octet) << i | this.readUnsignedByte(i);
            } else {
                int value = 0;

                for(int j = 0; j < length; ++i) {
                    value <<= 1;
                    value |= this.bitset.get(this.index++) ? 1 : 0;
                }

                return value;
            }
        }
    }

    public boolean readBoolean() throws IOException {
        return this.readUnsignedByte(1) == 1;
    }

    public String readAscii(int length) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else {
            byte[] buffer = new byte[length];

            for(int i = 0; i < length; ++i) {
                buffer[i] = (byte)this.input.readUnsignedByte();
            }

            return new String(buffer);
        }
    }

    public byte[] readBytes(int length) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else {
            byte[] buffer = new byte[length];

            for(int i = 0; i < length; ++i) {
                buffer[i] = (byte)this.input.readUnsignedByte();
            }

            return buffer;
        }
    }

    public String readBCD(int length, int endian) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else {
            char[] hexChars = new char[2 * length];
            int i;
            int b;
            if (0 == endian) {
                for(i = length - 1; i >= 0; --i) {
                    b = this.input.readUnsignedByte();
                    hexChars[i * 2] = hexArray[(b & 255) >>> 4];
                    hexChars[i * 2 + 1] = hexArray[b & 15];
                }
            } else {
                for(i = 0; i <= length - 1; ++i) {
                    b = this.input.readUnsignedByte();
                    hexChars[i * 2] = hexArray[(b & 255) >>> 4];
                    hexChars[i * 2 + 1] = hexArray[b & 15];
                }
            }

            return new String(hexChars);
        }
    }

    public static final String byteToHex(byte b) {
        char[] hexChars = new char[]{hexArray[(b & 255) >>> 4], hexArray[b & 15]};
        return new String(hexChars);
    }

    protected int readUnsignedShort(int length) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else if (length > 16) {
            throw new IllegalArgumentException("length(" + length + ") > 16");
        } else {
            int quotient = length / 8;
            int remainder = length % 8;
            int value = 0;

            for(int i = 0; i < quotient; ++i) {
                value <<= 8;
                value |= this.readUnsignedByte(8);
            }

            if (remainder > 0) {
                value <<= remainder;
                value |= this.readUnsignedByte(remainder);
            }

            return value;
        }
    }

    protected int readUnsignedShortLE(int length) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else if (length > 16) {
            throw new IllegalArgumentException("length(" + length + ") > 16");
        } else {
            int quotient = length / 8;
            int remainder = length % 8;
            int value = 0;

            for(int i = 0; i < quotient; ++i) {
                value |= this.readUnsignedByte(8) << 8 * i;
            }

            if (remainder > 0) {
                value |= this.readUnsignedByte(remainder) << 8;
            }

            return value;
        }
    }

    public int readUnsignedIntLE(int length) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        } else if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        } else {
            int quotient = length / 8;
            int remainder = length % 8;
            int value = 0;

            for(int i = 0; i < quotient; ++i) {
                value |= this.readUnsignedByte(8) << 8 * i;
            }

            if (remainder > 0) {
                value |= this.readUnsignedByte(remainder) << 8;
            }

            return value;
        }
    }

    public int readUnsignedInt(int length) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        } else if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        } else {
            int quotient = length / 16;
            int remainder = length % 16;
            int value = 0;

            for(int i = 0; i < quotient; ++i) {
                value <<= 16;
                value |= this.readUnsignedShort(16);
            }

            if (remainder > 0) {
                value <<= remainder;
                value |= this.readUnsignedShort(remainder);
            }

            return value;
        }
    }

    public int readInt(int length) throws IOException {
        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        } else if (length >= 32) {
            throw new IllegalArgumentException("length(" + length + ") >= 32");
        } else {
            return (this.readBoolean() ? -1 : 0) << length - 1 | this.readUnsignedInt(length - 1);
        }
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt(32));
    }

    public long readUnsignedLong(int length) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        } else if (length >= 64) {
            throw new IllegalArgumentException("length(" + length + ") >= 64");
        } else {
            int quotient = length / 31;
            int remainder = length % 31;
            long result = 0L;

            for(int i = 0; i < quotient; ++i) {
                result <<= 31;
                result |= (long)this.readUnsignedInt(31);
            }

            if (remainder > 0) {
                result <<= remainder;
                result |= (long)this.readUnsignedInt(remainder);
            }

            return result;
        }
    }

    public long readLong(int length) throws IOException {
        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        } else if (length > 64) {
            throw new IllegalArgumentException("length(" + length + ") > 64");
        } else {
            return (this.readBoolean() ? -1L : 0L) << length - 1 | this.readUnsignedLong(length - 1);
        }
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong(64));
    }

    public int align(int length) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else {
            int bits = 0;
            if (this.index < 8) {
                bits = 8 - this.index;
                this.readUnsignedByte(bits);
            }

            int bytes = this.count % length;
            if (bytes == 0) {
                return bits;
            } else {
                if (bytes > 0) {
                    bytes = length - bytes;
                } else {
                    bytes = 0 - bytes;
                }

                while(bytes > 0) {
                    this.readUnsignedByte(8);
                    bits += 8;
                    --bytes;
                }

                return bits;
            }
        }
    }

    public int getCount() {
        return this.count;
    }

    public static class ChannelInput implements ByteInput {
        private final ReadableByteChannel input;
        private final ByteBuffer buffer;

        public ChannelInput(ReadableByteChannel input) {
            if (input == null) {
                throw new NullPointerException("null input");
            } else {
                this.input = input;
                this.buffer = ByteBuffer.allocate(1);
            }
        }

        public int readUnsignedByte() throws IOException {
            this.buffer.clear();

            int read;
            do {
                read = this.input.read(this.buffer);
                if (read == -1) {
                    throw new EOFException("eof");
                }
            } while(read != 1);

            this.buffer.flip();
            return this.buffer.get() & 255;
        }
    }

    public static class StreamInput implements ByteInput {
        private final InputStream input;

        public StreamInput(InputStream input) {
            if (input == null) {
                throw new NullPointerException("null input");
            } else {
                this.input = input;
            }
        }

        public int readUnsignedByte() throws IOException {
            return this.input.read();
        }
    }

    public interface ByteInput {
        int readUnsignedByte() throws IOException;
    }
}
