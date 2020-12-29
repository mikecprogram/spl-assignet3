package bgu.spl.net.impl.user;

public class OPPackage{
    //These codes are here because they are being used only with OPPackage.
    //Therefore they don't have to be static.
    public final short OP_ADMIN_REGISTER = 1;
    public final short OP_STUDENT_REGISTER = 2;
    public final short OP_LOGIN_REQUEST = 3;
    public final short OP_LOGOUT_REQUEST = 4;
    public final short OP_REGISTER_COURSE = 5;
    public final short OP_CHECK_KDAM = 6;
    public final short OP_A_COURSE_STAT = 7;
    public final short OP_A_STUDENT_STAT = 8;
    public final short OP_CHECK_REGISTERED = 9;
    public final short OP_UNREGISTER_COURSE = 10;
    public final short OP_CHECK_MY_COURSES = 11;
    public final short OP_ACK = 12;
    public final short OP_ERROR = 13;

    private short OPCode;
    private String firstArg;
    private String secondArg;

    public OPPackage() {
        this.OPCode = 0;
        this.firstArg = "";
        this.secondArg = "";
    }

    public OPPackage(short OPCode, String firstArg, String secondArg) {
        this.OPCode = OPCode;
        this.firstArg = firstArg;
        this.secondArg = secondArg;
    }

    public short getOPCode() {
        return OPCode;
    }

    public void setOPCode(short OPCode) {
        this.OPCode = OPCode;
    }

    public String getFirstArg() {
        return firstArg;
    }

    public void setFirstArg(String firstArg) {
        this.firstArg = firstArg;
    }

    public String getSecondArg() {
        return secondArg;
    }

    public void setSecondArg(String secondArg) {
        this.secondArg = secondArg;
    }

    //This method performs clone, but have a better name ;)
    public OPPackage mitosis(){
        return new OPPackage(OPCode,firstArg,secondArg);
    }
}
