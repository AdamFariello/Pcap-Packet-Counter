/* Example skeleton program for CS352 Wireshark 1 assignment 
 *
 * Reads a file call input.pcap and prints the first 5 
 * packets 
 *
 * pcap4j takes an PacketListener object as input and runs
 * a method on every packet. 
 *
 * See the PacketListener class and its gotPacket method below. 
 *
 * (c) 2021, R. P. Martin, released under the GPL version 2
 *  
 */

package com.github.username;

/*Sources given by teacher*/
import java.io.IOException;
import java.net.Inet4Address;
import com.sun.jna.Platform;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapDumper;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapStat;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.NifSelector;
import org.pcap4j.packet.IpV4Packet;


/*this is the main class*/ 
//see the pom.xml 
public class App {
    public static void main(String[] args) {
	/*Regular class starts*/
	//System.out.println("Got here!");
	final PcapHandle handle;
	try { 
	    //arg[0] = first argument given after .jar file
	    handle = Pcaps.openOffline(args[0]);
	} catch (Exception e){
	    System.out.println("opening pcap file failed!");	    
	    e.printStackTrace();
	    return;
	}

	// this is the function that is given to the
	// loop handler and is called per packet
        /*
	PacketListener listener = new PacketListener() { 
		public void gotPacket(Packet packet) {
		    System.out.println(handle.getTimestamp());
		    System.out.println(handle);
		    System.out.println("packet info");
		    System.out.println(packet);
		    IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
		    Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
		    System.out.println(srcAddr);
        	}
	};
	*/

        try {
	    /*Old version
	    int maxPackets = -1;
	    //call the packet listener on the first 5 packets 
	    handle.loop(maxPackets, listener);
	    */  
	   	
	    int packetCount 	  = 0;
	    int udpCount 	  = 0;
	    int tcpCount 	  = 0;
	    int packetDataCount   = 0;
	    double timeStampFirst = 0;
	    double timeStampLast  = 0;

	    Packet packet;
	    for (int i = 0; ((packet = handle.getNextPacket()) != null); i++) {
		    //Find Time Stamps
		    if (i == 0) 
			timeStampFirst = handle.getTimestamp().getTime(); 
		    else
			timeStampLast = handle.getTimestamp().getTime(); 

		    //Counts
		    if ((packet.toString()).contains("UDP"))
			udpCount++;
		    else
		    	tcpCount++;
		    packetCount++;
		    packetDataCount += packet.length(); 
	    }
	        

	    //8 bits = 1 Byte			(packetDataCount * 8) 
	    //100000bits = 1 megabit		(Math.pow(10, 6)))
	    //Time lapse			(timeStampFirst - timeStampLast)
	    //1000ns = 1s			(time/1000)	
	    double bandwidth = ((packetDataCount * 8) / (Math.pow(10, 6))); 
	    bandwidth = bandwidth / ((timeStampLast - timeStampFirst)/1000);

	    System.out.println("Total number of Packets, " +packetCount);
  	    System.out.println("Total number of UDP Packets, " +udpCount);
	    System.out.println("Total number of TCP Packets, " +tcpCount);
   	    System.out.println("Total bandwidth of the packet trace in Mbps, " +bandwidth);	    
	} catch (Exception e) {
	    System.out.println( "Error Processing pcap file!" );
	    e.printStackTrace();
	    return;
	}

        // Cleanup when complete
        handle.close();	
    }
}

