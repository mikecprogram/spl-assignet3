package bgu.spl.net.impl.user;

public class OPPackage{
    //These codes are here because they are being used only with OPPackage.
    //Therefore they don't have to be static.
    public final int OP_ADMIN_REGISTER = 1;
    public final int OP_STUDENT_REGISTER = 2;
    public final int OP_LOGIN_REQUEST = 3;
    public final int OP_LOGOUT_REQUEST = 4;
    public final int OP_REGISTER_COURSE = 5;
    public final int OP_CHECK_KDAM = 6;
    public final int OP_A_COURSE_STAT = 7;
    public final int OP_A_STUDENT_STAT = 8;
    public final int OP_CHECK_REGISTERED = 9;
    public final int OP_UNREGISTER_COURSE = 10;
    public final int OP_CHECK_MY_COURSES = 11;
    public final int OP_ACK = 12;
    public final int OP_ERROR = 13;

    private int OPCode;
    private String firstArg;
    private String secondArg;

    public OPPackage() {
        this.OPCode = 0;
        this.firstArg = "";
        this.secondArg = "";
    }

    public OPPackage(int OPCode, String firstArg, String secondArg) {
        this.OPCode = OPCode;
        this.firstArg = firstArg;
        this.secondArg = secondArg;
    }

    public int getOPCode() {
        return OPCode;
    }

    public void setOPCode(int OPCode) {
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
