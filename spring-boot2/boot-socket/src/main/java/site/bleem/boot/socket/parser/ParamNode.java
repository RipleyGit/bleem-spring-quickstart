package site.bleem.boot.socket.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public class ParamNode {
    private static final int DATATYPE_BOOL = 1;
    private static final int DATATYPE_INT = 2;
    private static final int DATATYPE_STRING = 3;
    private static final int DATATYPE_CONST = 4;
    private static final int DATATYPE_RETAIN = 5;
    private static final int DATATYPE_ARRAY = 6;
    private static final int BINTYPE_BIT = 1;
    private static final int BINTYPE_BCD = 2;
    private static final int BINTYPE_BYTE = 3;
    private static final int BINTYPE_HEX = 4;
    private static final int LEN_FIXED = 1;
    private static final int LEN_UNFIXED = 2;

    private ParamNode() {
    }

    public static <T extends JSON> T doParser(BitInput input, JSONArray configs, Class<T> tClass) throws IOException {
        if (null == configs) {
            return null;
        } else {
            try {
                T ret = (T) tClass.getDeclaredConstructor().newInstance();

                for(int i = 0; i < configs.size(); ++i) {
                    JSONObject obj = configs.getJSONObject(i);
                    if (ret instanceof JSONArray) {
                        ((JSONArray)ret).add(doParser(input, obj, tClass));
                    }

                    if (ret instanceof JSONObject) {
                        ((JSONObject)ret).putAll(doParser(input, obj, tClass));
                    }
                }

                return ret;
            } catch (InstantiationException var6) {
                var6.printStackTrace();
            } catch (IllegalAccessException var7) {
                var7.printStackTrace();
            } catch (InvocationTargetException var8) {
                var8.printStackTrace();
            } catch (NoSuchMethodException var9) {
                var9.printStackTrace();
            }

            return null;
        }
    }

    public static <T extends JSON> JSONObject doParser(BitInput input, JSONObject obj, Class<T> tClass) throws IOException {
        JSONObject retObj = new JSONObject();
        int dataType = obj.getIntValue("dataType");
        int binType = obj.getIntValue("binType");
        int size = obj.getIntValue("size");
        String fieldName = obj.getString("fieldName");
        if (0 != dataType && (6 == dataType || 0 != binType) && 0 != size && (5 == dataType || null != fieldName)) {
            int fixed;
            int endian;
            switch (dataType) {
                case 1:
                    retObj.put(fieldName, input.readBoolean());
                    break;
                case 2:
                    switch (binType) {
                        case 1:
                            retObj.put(fieldName, input.readUnsignedByte(size));
                            return retObj;
                        case 3:
                            fixed = obj.getIntValue("endian");
                            if (0 == fixed) {
                                if (size == 4) {
                                    retObj.put(fieldName, (long)input.readUnsignedIntLE(8 * size) & 4294967295L);
                                } else {
                                    retObj.put(fieldName, input.readUnsignedIntLE(8 * size));
                                }

                                return retObj;
                            } else {
                                if (size == 4) {
                                    retObj.put(fieldName, (long)input.readUnsignedInt(8 * size) & 4294967295L);
                                } else {
                                    retObj.put(fieldName, input.readUnsignedInt(8 * size));
                                }

                                return retObj;
                            }
                        default:
                            return retObj;
                    }
                case 3:
                    fixed = obj.getIntValue("fixed");
                    if (0 == fixed) {
                        throw new IllegalArgumentException("parser config error");
                    }

                    if (fixed == 2) {
                        size = input.readUnsignedIntLE(8 * size);
                    }

                    switch (binType) {
                        case 2:
                            endian = obj.getIntValue("endian");
                            retObj.put(fieldName, input.readBCD(size, endian));
                            return retObj;
                        case 3:
                            retObj.put(fieldName, input.readAscii(size));
                            return retObj;
                        case 4:
                            retObj.put(fieldName, input.readBytes(size));
                            return retObj;
                        default:
                            return retObj;
                    }
                case 4:
                    retObj.put(fieldName, obj.get("constData"));
                    break;
                case 5:
                    switch (binType) {
                        case 1:
                            retObj.put(fieldName, input.readUnsignedByte(size));
                            return retObj;
                        case 3:
                            retObj.put(fieldName, input.readUnsignedIntLE(8 * size));
                            return retObj;
                        default:
                            return retObj;
                    }
                case 6:
                    endian = input.readUnsignedIntLE(8 * size);
                    JSONArray array = obj.getJSONArray("item");
                    if (null == array) {
                        throw new IllegalArgumentException("parser config error");
                    }

                    JSONArray items = new JSONArray();

                    for(int j = 0; j < endian; ++j) {
                        T tempObject = doParser(input, array, tClass);
                        items.add(tempObject);
                    }

                    retObj.put(fieldName, items);
            }

            return retObj;
        } else {
            throw new IllegalArgumentException("parser config error");
        }
    }

    public static void doControl(JSONObject data, JSONArray config, BitOutput output) throws IOException {
        label137:
        for(int i = 0; i < config.size(); ++i) {
            JSONObject obj = config.getJSONObject(i);
            int dataType = obj.getIntValue("dataType");
            int binType = obj.getIntValue("binType");
            int size = obj.getIntValue("size");
            String fieldName = obj.getString("fieldName");
            int fixed;
            int j;
            switch (dataType) {
                case 1:
                    Integer bData = data.getInteger(fieldName);
                    if (null == bData || bData > 1 || bData < 0) {
                        throw new IOException();
                    }

                    if (bData.equals(0)) {
                        output.writeBoolean(false);
                    } else {
                        output.writeBoolean(true);
                    }
                    break;
                case 2:
                    Integer iData = data.getInteger(fieldName);
                    switch (binType) {
                        case 1:
                            if (null != iData && iData <= 255 && iData >= 0) {
                                output.writeUnsignedByte(size, iData.byteValue());
                                continue;
                            }

                            throw new IOException();
                        case 3:
                            if (null == iData) {
                                throw new IOException();
                            }

                            fixed = obj.getIntValue("endian");
                            if (0 == fixed) {
                                output.writeUnsignedIntLE(size * 8, iData);
                            } else {
                                output.writeUnsignedInt(size * 8, iData);
                            }
                        default:
                            continue;
                    }
                case 3:
                    fixed = obj.getIntValue("fixed");
                    int c;
                    switch (binType) {
                        case 2:
                            String bcdData = data.getString(fieldName);
                            if (null == bcdData) {
                                throw new IOException();
                            }

                            if (fixed == 2) {
                                output.writeUnsignedIntLE(8 * size, bcdData.length() / 2);
                            }

                            c = obj.getIntValue("endian");
                            char lC;
                            int hi;
                            int li;
                            int b;
                            char hC;
                            if (0 == c) {
                                j = bcdData.length() - 2;

                                while(true) {
                                    if (j < 0) {
                                        continue label137;
                                    }

                                    hC = bcdData.charAt(j);
                                    lC = bcdData.charAt(j + 1);
                                    hi = Integer.valueOf(String.valueOf(hC), 16);
                                    li = Integer.valueOf(String.valueOf(lC), 16);
                                    b = (hi & 15) << 4 | li & 15;
                                    output.writeUnsignedByte(8, b);
                                    j -= 2;
                                }
                            } else {
                                j = 0;

                                while(true) {
                                    if (j > bcdData.length() - 2) {
                                        continue label137;
                                    }

                                    hC = bcdData.charAt(j);
                                    lC = bcdData.charAt(j + 1);
                                    hi = Integer.valueOf(String.valueOf(hC), 16);
                                    li = Integer.valueOf(String.valueOf(lC), 16);
                                    b = (hi & 15) << 4 | li & 15;
                                    output.writeUnsignedByte(8, b);
                                    j += 2;
                                }
                            }
                        case 3:
                            String asciiData = data.getString(fieldName);
                            if (null == asciiData) {
                                throw new IOException();
                            }

                            if (fixed == 2) {
                                output.writeUnsignedIntLE(8 * size, asciiData.length());
                            }

                            j = 0;

                            while(true) {
                                if (j >= asciiData.length()) {
                                    continue label137;
                                }

                                c = asciiData.charAt(j) & 255;
                                output.writeUnsignedByte(8, c);
                                ++j;
                            }
                        case 4:
                            JSONArray hexData = data.getJSONArray(fieldName);
                            if (null == hexData) {
                                throw new IOException();
                            }

                            if (fixed == 2) {
                                output.writeUnsignedIntLE(8 * size, hexData.size());
                            }

                            Iterator<Object> iter = hexData.iterator();

                            while(iter.hasNext()) {
                                output.writeUnsignedByte(8, (Integer)iter.next());
                            }
                    }
                case 4:
                default:
                    break;
                case 5:
                    switch (binType) {
                        case 1:
                            output.writeUnsignedByte(size, 0);
                            continue;
                        case 3:
                            output.writeUnsignedIntLE(8 * size, 0);
                        default:
                            continue;
                    }
                case 6:
                    JSONArray dataArr = data.getJSONArray(fieldName);
                    j = dataArr.size();
                    output.writeUnsignedByte(8, j);
                    JSONArray itemArray = obj.getJSONArray("item");

                    for(j = 0; j < j; ++j) {
                        JSONObject tempObj = dataArr.getJSONObject(j);
                        doControl(tempObj, itemArray, output);
                    }
            }
        }

    }
}
