import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

public class Methods {
    public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遞迴所有網路通道
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = ifaces.nextElement();
                // 在所有的網路通道下遍历IP
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback類型的IP位址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-localIP位址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local類型的IP位址沒被發現，先記錄起來
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 若只有 loopback IP位址,則只能選其他的
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
    public static String compare(String x, String ans) {
        int a = 0, b = 0;
        for(int i = 0; i < ans.length();i++) {
            for(int j = 0; j < x.length();j++) {
                boolean equals = ans.substring(i, i + 1).equals(x.substring(j, j + 1));
                if(i == j && equals) {
                    a++;
                    //System.out.println("a i:"+i+" j:"+j);
                } else if(i!=j && equals) {
                    b++;
                    //System.out.println("b i:"+i+" j:"+j);
                }
            }
        }
        return (a+"A"+b+"B");
    }
    public static void writeFile(String fileName, List<String> content) throws IOException {
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        for(int i=0;i< content.size();i++) {
            writer.write(content.get(i)+"\r\n");
        }
        writer.flush();
        writer.close();
    }

}