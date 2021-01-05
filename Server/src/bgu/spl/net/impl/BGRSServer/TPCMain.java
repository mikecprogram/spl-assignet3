package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.Database;
import bgu.spl.net.impl.BGRSServer.OPPackage;
import bgu.spl.net.impl.BGRSServer.OPPackageMessageEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.UserProtocol;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.BlockingConnectionHandler;

public class TPCMain {
    public static void main(String[] args) {
        int port = Integer.decode(args[0]);
        if(Database.getInstance().initialize("./Courses.txt")) {
            BaseServer<OPPackage> server = new BaseServer<OPPackage>(port,
                    () -> new UserProtocol(),
                    () -> new OPPackageMessageEncoderDecoder()) {
                @Override
                protected void execute(BlockingConnectionHandler<OPPackage> handler) {
                    handler.run();
                }
            };
            server.serve();
        }else{
            System.out.println("Courses.txt does not exist or there is an issue with it.");
        }
    }

}
