package net.sourceforge.queried;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.*;

/**
 * @author DeadEd
 */
public class Util {

    // table to convert a nibble to a hex char.
    static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    // Fast convert a byte array to a hex string
    // with possible leading zero.
    // Note, ref: http://mindprod.com/jgloss/hex.html
    public static String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (byte element : b) {
            // look up high nibble char
            sb.append(hexChar[(element & 0xf0) >>> 4]);

            // look up low nibble char
            sb.append(hexChar[element & 0x0f]);
        }
        return sb.toString();
    }

    public static DatagramPacket getDatagramPacket(String request, InetAddress inet, int port) {
        ByteBuffer Request = ByteBuffer.allocate(1400);
        Charset charset = Charset.forName("ISO-8859-1");
        Request.putInt(-1);
        Request.put(charset.encode(request));
        return new DatagramPacket(Request.array(), 1400, inet, port);
    }

    private static int charToNibble(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 0xa;
        } else if ('A' <= c && c <= 'F') {
            return c - 'A' + 0xa;
        } else {
            throw new IllegalArgumentException("Invalid hex character: " + c);
        }
    }

    public static byte[] fromHexString(String s) {
        int stringLength = s.length();
        if ((stringLength & 0x1) != 0) {
            throw new IllegalArgumentException(
                    "fromHexString requires an even number of hex characters");
        }
        byte[] b = new byte[stringLength / 2];

        for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
            int high = charToNibble(s.charAt(i));
            int low = charToNibble(s.charAt(i + 1));
            b[j] = (byte) ((high << 4) | low);
        }
        return b;
    }

    public static DatagramPacket getDatagramPacketGS2(InetAddress inet, int port) {
        byte[] bite = fromHexString("FEFD0004050607FFFFFF");

        return new DatagramPacket(bite, bite.length, inet, port);
    }

    public static DatagramPacket getDatagramPacketBF2(InetAddress inet, int port) {

        byte[] bite = null;
        bite = fromHexString("FEFD000405060700FF0001");

        return new DatagramPacket(bite, bite.length, inet, port);
    }

    public static DatagramPacket getDatagramPacketBF2142(InetAddress inet,
                                                         int port, String challengeResponse) {

        byte[] bite = null;
        bite = fromHexString("FEFD005DAC8159" + challengeResponse + "FFFFFF01");

        return new DatagramPacket(bite, bite.length, inet, port);
    }

    public static DatagramPacket getDatagramPacketBF2142Challenge(
            InetAddress inet, int port) {

        byte[] bite = null;
        bite = fromHexString("FEFD0901020300");

        return new DatagramPacket(bite, bite.length, inet, port);
    }

    public static DatagramPacket getDatagramPacketD3(InetAddress inet, int port) {
        byte[] bite = fromHexString("FFFF676574496E666F0000000000");

        return new DatagramPacket(bite, bite.length, inet, port);
    }

    public static DatagramPacket getDatagramPacketNWN(InetAddress inet, int port) {
        byte[] bite = fromHexString("FEFD00E0EB2D0E14010B0105080A333435130436373839143A3B3C3D0000");

        return new DatagramPacket(bite, bite.length, inet, port);
    }

    public static DatagramPacket getDatagramPacketWSW(InetAddress inet, int port) {
        // ����getinfo 6 full empty�
        byte[] bite = fromHexString("ffffffff676574696e666f20362066756c6c20656d707479ff");

        return new DatagramPacket(bite, bite.length, inet, port);
    }

    // no request needed for GS2
    public static String getInfo(int localPort, String ipStr, int port,
                                 int infoType, int queryType, int gameType) {

        return getInfo(localPort, ipStr, port, "", infoType, queryType,
                gameType);
    }

    public static String getInfo(String ipStr, int port, String request,
                                 int infoType, int queryType, int gameType) {

        return getInfo(0, ipStr, port, request, infoType, queryType, gameType);
    }

    public static String getInfo(int localPort, String ipStr, int port,
                                 String request, int infoType, int queryType, int gameType) {

        StringBuffer recStr = new StringBuffer();
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(localPort);
            // default packet size
            int packetSize = 12288;

            // need a bigger packet size for this one
            if (infoType == QueriEd.INFO_PLAYERS
                    && (queryType == QueriEd.QUERY_GAMESPY || queryType == QueriEd.QUERY_UT2S)) {
                packetSize = 24576;
            }

            InetAddress address = InetAddress.getByName(ipStr);
            InetAddress inet = InetAddress.getByName(ipStr);


            socket.connect(address, port);

            DatagramPacket out = null;
            if (gameType == QueriEd.GAME_UT || gameType == QueriEd.GAME_UT2003
                    || gameType == QueriEd.GAME_UT2004) {

                byte[] requestBytes;
                try {
                    requestBytes = request.getBytes("ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    requestBytes = request.getBytes();
                }
                out = new DatagramPacket(requestBytes, requestBytes.length,
                        inet, port);
            } else if (gameType == QueriEd.GAME_BFV
                    || gameType == QueriEd.GAME_NWN
                    || gameType == QueriEd.GAME_AA) {
                out = getDatagramPacketGS2(inet, port);
            } else if (gameType == QueriEd.GAME_BF2) {
                out = getDatagramPacketBF2(inet, port);
            } else if (gameType == QueriEd.GAME_BF2142) {
                // there is a challenge with this one ...
                out = getDatagramPacketBF2142Challenge(inet, port);
                socket.send(out);
                byte[] challengeData = new byte[packetSize];
                DatagramPacket cPacket = new DatagramPacket(challengeData,
                        packetSize);
                socket.setSoTimeout(QueriEd.TIMEOUT);

                // get the response
                socket.receive(cPacket);

                byte[] packetData = cPacket.getData();
                byte[] shrunk = new byte[7];
                for (int i = 0; i < shrunk.length; i++) {
                    shrunk[i] = packetData[i + 4];
                }
                String hexxed = toHexString(shrunk);

                String challengeResponse = hexxed.substring(hexxed.length() - 8, hexxed.length());
                // out = getDatagramPacketBF2142(inet, port, infoType, toHexString(fromHexString(challengeResponse)));
                out = getDatagramPacketBF2142(inet, port, toHexString(challengeResponse.getBytes()));
            } else if (gameType == QueriEd.GAME_D3
                    || gameType == QueriEd.GAME_Q4) {
                out = getDatagramPacketD3(inet, port);
            } else if (gameType == QueriEd.GAME_WSW) {
                out = getDatagramPacketWSW(inet, port);
            } else {
                out = getDatagramPacket(request, inet, port);
            }

            socket.send(out);

            byte[] data = new byte[packetSize];
            DatagramPacket inPacket = new DatagramPacket(data, packetSize);
            socket.setSoTimeout(QueriEd.TIMEOUT);

            // get the response
            socket.receive(inPacket);

            recStr = new StringBuffer(new String(inPacket.getData(), 0,
                    inPacket.getLength(), "ISO-8859-1"));
            if ((queryType == QueriEd.QUERY_SOURCE || queryType == QueriEd.QUERY_HALFLIFE)
                    && infoType == QueriEd.INFO_PLAYERS) {
                // we just got a challenge, need to go back again with the
                // request + challenge
                String sourceChallenge = recStr
                        .substring(recStr.indexOf("A") + 1);
                out = getDatagramPacket("U" + sourceChallenge, inet, port);
                socket.send(out);
                byte[] data2 = new byte[packetSize];

                DatagramPacket inPacket2 = new DatagramPacket(data2, packetSize);
                // get the response
                socket.receive(inPacket2);
                recStr = new StringBuffer(new String(inPacket2.getData(), 0,
                        inPacket2.getLength(), "ISO-8859-1"));
            }

            int qtimeout = 0;
            // going to be more packets with the rest of the data in
            if (queryType == QueriEd.QUERY_GAMESPY) {
                while (recStr.indexOf("\\final\\") < 0) {
                    if (qtimeout == 50) {
                        return "";
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        // purposely do nothing
                    }
                    qtimeout++;

                    if (infoType == QueriEd.INFO_PLAYERS) {
                        // get the response
                        socket.receive(inPacket);
                        recStr.append(new String(inPacket.getData(), 0,
                                inPacket.getLength(), "ISO-8859-1"));
                    }
                }
            } else if ((gameType == QueriEd.GAME_BF2 || gameType == QueriEd.GAME_AA)
                    && infoType == QueriEd.INFO_PLAYERS) {
                int hack = 0;
                while (hack != 10) {
                    try {
                        // get the response
                        socket.receive(inPacket);
                        recStr.append(new String(inPacket.getData(), 0,
                                inPacket.getLength(), "ISO-8859-1"));
                        hack++;
                    } catch (Exception ex) {
                        // urgh!!!!
                        hack = 10;
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("util.java getInfo exception:");
            System.out.println(ex.toString());
            recStr = new StringBuffer("");
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
        return recStr.toString();
    }

    public static String getPart(String queryString, String part) {
        if ((queryString == null) || (queryString.length() <= 0)) {
            return "Bad queryString";
        }

        int start = queryString.toLowerCase().indexOf(
                "\\" + part.toLowerCase() + "\\")
                + 2 + part.length();
        if (start < (2 + part.length())) {
            return "ERROR: " + part + " not found in query string";
        }
        String tempStr = queryString.substring(start);
        int end = tempStr.indexOf("\\") + start;
        if (end <= start) {
            // check for a newline
            end = tempStr.indexOf("\n") + start;
            if (end <= start) {
                return "ERROR: " + part + " not found in query string";
            }
        }
        String tmp = queryString.substring(start, end);
        return tmp;
    }

    public static String getPartGS2(String queryString, String part) {
        String querySubString = queryString.substring(5);
        char chr = 00;
        int start = querySubString.toLowerCase().indexOf(
                chr + part.toLowerCase() + chr)
                + 2 + part.length();
        if (start < 0) {
            return "ERROR: " + part + " not found in query string";
        }
        String tempStr = querySubString.substring(start);
        int end = tempStr.indexOf(chr) + start;
        if (end <= start) {
            return "ERROR: " + part + " not found in query string";
        }
        return querySubString.substring(start, end);
    }
}
