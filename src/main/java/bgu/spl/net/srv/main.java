package bgu.spl.net.srv;

import bgu.spl.net.impl.user.Database;
import bgu.spl.net.impl.user.OPPackage;
import bgu.spl.net.impl.user.OPPackageMessageEncoderDecoder;
import bgu.spl.net.impl.user.UserProtocol;

public class main {
    public static void main(String[] args) {
        if(!Database.getInstance().initialize("./Courses.txt")) {
            Reactor<OPPackage> server = new Reactor<OPPackage>(Integer.decode(args[1]), Integer.decode(args[0]),
                    () -> new UserProtocol(),
                    () -> new OPPackageMessageEncoderDecoder());
            server.serve();
        }else{
            System.out.println("Courses.txt does not exist or there is an issue with it.");
        }
    }
}
