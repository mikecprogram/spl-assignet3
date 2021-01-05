package bgu.spl.net.impl.user;

import bgu.spl.net.api.MessagingProtocol;


public class UserProtocol implements MessagingProtocol<OPPackage> {
    private boolean shouldTerminate = false;
    private String userName = "";
    private Role role = Role.None;

    @Override
    public OPPackage process(OPPackage packet) {
        //switch case can not be used here for the 'Constant Expression Require' error
        Database db = Database.getInstance();
        String result = "";
        try {
            if (packet.getOPCode() == packet.OP_ADMIN_REGISTER) {
                if (role == Role.None)
                    db.registerAdmin(packet.getFirstArg_Str(), packet.getSecondArg_Str());
                else
                    throw new Exception("Client already logged in.");
            } else if (packet.getOPCode() == packet.OP_STUDENT_REGISTER) {
                if (role == Role.None)
                    db.registerStudent(packet.getFirstArg_Str(), packet.getSecondArg_Str());
                else
                    throw new Exception("Client already logged in.");
            } else if (packet.getOPCode() == packet.OP_LOGIN_REQUEST) {
                if (role == Role.None) {
                    role = db.login(packet.getFirstArg_Str(), packet.getSecondArg_Str());
                    userName = packet.getFirstArg_Str();
                }
                else
                    throw new Exception("Client already logged in.");
            } else if (packet.getOPCode() == packet.OP_LOGOUT_REQUEST) {
                if (!(role == Role.None)) {
                    shouldTerminate = true;
                    db.logout(userName);
                    role = Role.None;
                    userName = "";
                }
                else
                    throw new Exception("Client not logged in.");
            } else if (packet.getOPCode() == packet.OP_REGISTER_COURSE) {
                if (role == Role.Student)
                    db.registerToCourse(userName, packet.getFirstArg_Short());
                else
                    throw new Exception("Not connected as students.");
            } else if (packet.getOPCode() == packet.OP_CHECK_KDAM) {
                if (!(role == Role.None))
                    result = db.checkKdam(packet.getFirstArg_Short());
                else
                    throw new Exception("Not logged in.");
            } else if (packet.getOPCode() == packet.OP_A_COURSE_STAT) {
                if (role == Role.Admin)
                    result = db.admin_courseStats(packet.getFirstArg_Short());
                else
                    throw new Exception("Not connected as admin.");
            } else if (packet.getOPCode() == packet.OP_A_STUDENT_STAT) {
                if (role == Role.Admin)
                    result = db.admin_studentStats(packet.getFirstArg_Str());
                else
                    throw new Exception("Not connected as admin.");
            } else if (packet.getOPCode() == packet.OP_CHECK_REGISTERED) {
                if (role == Role.Student)
                    if (db.isRegistered(userName, packet.getFirstArg_Short()))
                        result = "REGISTERED";
                    else
                        result = "NOT REGISTERED";
                else
                    throw new Exception("Not connected as student.");
            } else if (packet.getOPCode() == packet.OP_UNREGISTER_COURSE) {
                if (role == Role.Student)
                    db.unregister(userName, packet.getFirstArg_Short());
                else
                    throw new Exception("Not connected as student.");
            } else if (packet.getOPCode() == packet.OP_CHECK_MY_COURSES) {
                if (role == Role.Student) {
                    result = db.getStudentCourses(userName);
                } else
                    throw new Exception("No student is connected.");
            } else if (packet.getOPCode() == packet.OP_ACK) {
                throw new Exception("Server should not get Acknowledgement packet.");
            } else if (packet.getOPCode() == packet.OP_ERROR) {
                throw new Exception("Server should not get Error packet.");
            } else {
                throw new IllegalStateException("Unexpected value: " + packet.getOPCode());
            }
        } catch (Exception e) {
            //DEBUG: System.out.println("Debug->"+e.getMessage());
            return new OPPackage(packet.OP_ERROR, packet.getOPCode(), null, null);
        }
        return new OPPackage(packet.OP_ACK, packet.getOPCode(), result,"");
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
