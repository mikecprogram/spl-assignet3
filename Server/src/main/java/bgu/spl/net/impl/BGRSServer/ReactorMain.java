package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.Database;
import bgu.spl.net.impl.BGRSServer.OPPackage;
import bgu.spl.net.impl.BGRSServer.OPPackageMessageEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.UserProtocol;
import bgu.spl.net.srv.Reactor;

public class ReactorMain {
    public static void main(String[] args) {
        if(Database.getInstance().initialize("./Courses.txt")) {
            Reactor<OPPackage> server = new Reactor<>(Integer.decode(args[1]), Integer.decode(args[0]),
                    () -> new UserProtocol(),
                    () -> new OPPackageMessageEncoderDecoder());
            server.serve();
        }else{
            System.out.println("Courses.txt does not exist or there is an issue with it.");
        }
    }
}
