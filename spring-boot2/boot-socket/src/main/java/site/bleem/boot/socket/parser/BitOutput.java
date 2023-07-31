package site.bleem.boot.socket.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.BitSet;

public class BitOutput {
    protected final ByteOutput output;
    private final BitSet bitset = new BitSet(8);
    private int index = 0;
    private int count = 0;

    public BitOutput(ByteOutput output) {
        if (output == null) {
            throw new NullPointerException("null output");
        } else {
            this.output = output;
        }
    }

    protected void writeUnsignedByte(int length, int value) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else if (length > 8) {
            throw new IllegalArgumentException("length(" + length + ") > 8");
        } else {
            int required = length - (8 - this.index);
            if (required > 0) {
                this.writeUnsignedByte(length - required, value >> required);
                this.writeUnsignedByte(required, value);
            } else {
                int octet;
                for(octet = this.index + length - 1; octet >= this.index; --octet) {
                    this.bitset.set(octet, (value & 1) == 1);
                    value >>= 1;
                }

                this.index += length;
                if (this.index == 8) {
                    octet = 0;

                    for(int i = 0; i < 8; ++i) {
                        octet <<= 1;
                        octet |= this.bitset.get(i) ? 1 : 0;
                    }

                    this.output.writeUnsignedByte(octet);
                    ++this.count;
                    this.index = 0;
                }

            }
        }
    }

    public void writeBoolean(boolean value) throws IOException {
        this.writeUnsignedByte(1, value ? 1 : 0);
    }

    protected void writeUnsignedShort(int length, int value) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else if (length > 16) {
            throw new IllegalArgumentException("length(" + length + ") > 16");
        } else {
            int quotient = length / 8;
            int remainder = length % 8;
            if (remainder > 0) {
                this.writeUnsignedByte(remainder, value >> quotient * 8);
            }

            for(int i = quotient - 1; i >= 0; --i) {
                this.writeUnsignedByte(8, value >> 8 * i);
            }

        }
    }

    protected void writeUnsignedShortLE(int length, int value) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        } else if (length > 16) {
            throw new IllegalArgumentException("length(" + length + ") > 16");
        } else {
            int quotient = length / 8;
            int remainder = length % 8;

            for(int i = 0; i < quotient; ++i) {
                this.writeUnsignedByte(8, value >> 8 * i);
            }

            if (remainder > 0) {
                this.writeUnsignedByte(remainder, value >> quotient * 8);
            }

        }
    }

    public void writeUnsignedInt(int length, int value) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        } else if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        } else {
            int quotient = length / 16;
            int remainder = length % 16;
            if (remainder > 0) {
                this.writeUnsignedShort(remainder, value >> quotient * 16);
            }

            for(int i = quotient - 1; i >= 0; --i) {
                this.writeUnsignedShort(16, value >> 16 * i);
            }

        }
    }

    public void writeUnsignedIntLE(int length, int value) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        } else if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        } else {
            int quotient = length / 16;
            int remainder = length % 16;

            for(int i = 0; i < quotient; ++i) {
                this.writeUnsignedShortLE(16, value >> 16 * i);
            }

            if (remainder > 0) {
                this.writeUnsignedShortLE(remainder, value >> quotient * 16);
            }

        }
    }

    public void writeInt(int length, int value) throws IOException {
        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        } else if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        } else {
            if (length != 32) {
                if (value < 0) {
                    if (value >> length - 1 != -1) {
                        throw new IllegalArgumentException("value(" + value + ") >> (length(" + length + ") - 1) != ~0");
                    }
                } else if (value >> length - 1 != 0) {
                    throw new IllegalArgumentException("value(" + value + ") >> (length(" + length + ") - 1) != 0");
                }
            }

            int quotient = length / 16;
            int remainder = length % 16;
            if (remainder > 0) {
                this.writeUnsignedShort(remainder, value >> quotient * 16);
            }

            for(int i = quotient - 1; i >= 0; --i) {
                this.writeUnsignedShort(16, value >> 16 * i);
            }

        }
    }

    public void writeFloat(float value) throws IOException {
        this.writeInt(32, Float.floatToRawIntBits(value));
    }

    public void writeUnsignedLong(int length, long value) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        } else if (length >= 64) {
            throw new IllegalArgumentException("length(" + length + ") >= 64");
        } else if (value >> length != 0L) {
            throw new IllegalArgumentException("(value(" + value + ") >> length(" + length + ")) != 0");
        } else {
            int quotient = length / 16;
            int remainder = length % 16;
            if (remainder > 0) {
                this.writeUnsignedShort(remainder, (int)(value >> quotient * 16));
            }

            for(int i = quotient - 1; i >= 0; --i) {
                this.writeUnsignedShort(16, (int)(value >> i * 16));
            }

        }
    }

    public void writeLong(int length, long value) throws IOException {
        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        } else if (length > 64) {
            throw new IllegalArgumentException("length(" + length + ") > 64");
        } else {
            if (length < 64) {
                if (value < 0L) {
                    if (value >> length - 1 != -1L) {
                        throw new IllegalArgumentException("(value(" + value + ") >> (length(" + length + ") - 1)) != ~0L");
                    }
                } else if (value >> length - 1 != 0L) {
                    throw new IllegalArgumentException("(value(" + value + ") >> (length(" + length + ") - 1)) != 0L");
                }
            }

            int quotient = length / 16;
            int remainder = length % 16;
            if (remainder > 0) {
                this.writeUnsignedShort(remainder, (int)(value >> quotient * 16));
            }

            for(int i = quotient - 1; i >= 0; --i) {
                this.writeUnsignedShort(16, (int)(value >> i * 16));
            }

        }
    }

    public void writeDouble(double value) throws IOException {
        this.writeLong(64, Double.doubleToRawLongBits(value));
    }

    public int align(int length) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        } else {
            int bits = 0;
            if (this.index > 0) {
                bits = 8 - this.index;
                this.writeUnsignedByte(bits, 0);
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
                    this.writeUnsignedByte(8, 0);
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

    public static class ChannelOutput implements ByteOutput {
        private final WritableByteChannel output;
        private final ByteBuffer buffer;

        public ChannelOutput(WritableByteChannel output) {
            if (output == null) {
                throw new NullPointerException("null output");
            } else {
                this.output = output;
                this.buffer = ByteBuffer.allocate(1);
            }
        }

        public void writeUnsignedByte(int value) throws IOException {
            this.buffer.put((byte)value);
            this.buffer.flip();

            while(this.output.write(this.buffer) != 1) {
            }

            this.buffer.clear();
        }
    }

    public static class StreamOutput implements ByteOutput {
        private final OutputStream output;

        public StreamOutput(OutputStream output) {
            if (output == null) {
                throw new NullPointerException("null output");
            } else {
                this.output = output;
            }
        }

        public void writeUnsignedByte(int value) throws IOException {
            this.output.write(value);
        }
    }

    public interface ByteOutput {
        void writeUnsignedByte(int var1) throws IOException;
    }
}
