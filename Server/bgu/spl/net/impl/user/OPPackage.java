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

    private short firstArg_Short;
    private String firstArg_Str;
    private String secondArg_Str;

    public OPPackage() {
        clear();
    }

    public OPPackage(short OPCode, short firstArg_Short,String firstArg, String secondArg) {
        this.OPCode = OPCode;
        this.firstArg_Short = firstArg_Short;
        if (firstArg != null)
            this.firstArg_Str = firstArg;
        else
            this.firstArg_Str = "";
        if (secondArg != null)
            this.secondArg_Str = secondArg;
        else
            this.secondArg_Str = "";
    }

    public short getOPCode() {
        return OPCode;
    }

    public void setOPCode(short OPCode) {
        this.OPCode = OPCode;
    }

    public short getFirstArg_Short() {
        return firstArg_Short;
    }

    public void setFirstArg_Short(short firstArg_Short) {
        this.firstArg_Short = firstArg_Short;
    }

    public String getFirstArg_Str() {
        return firstArg_Str;
    }

    public void setFirstArg_Str(String firstArg_Str) {
        this.firstArg_Str = firstArg_Str;
    }

    public String getSecondArg_Str() {
        return secondArg_Str;
    }

    public void setSecondArg_Str(String secondArg_Str) {
        this.secondArg_Str = secondArg_Str;
    }
    public boolean isFirstArg_Str_HasVal(){
        return !firstArg_Str.isEmpty();
    }
    public boolean isSecondArg_Str_HasVal(){
        return !secondArg_Str.isEmpty();
    }
    public boolean isFirstArg_Short_HasVal(){
        return !(firstArg_Short == 0);
    }
    //This method performs clone, but have a better name ;)
    public OPPackage mitosis(){
        return new OPPackage(OPCode,firstArg_Short,firstArg_Str,secondArg_Str);
    }

    public boolean isValid() {
        if(OPCode == 0)
            return false;
        if (getOPCode() == OP_ADMIN_REGISTER) {
            return isFirstArg_Str_HasVal() & isSecondArg_Str_HasVal();
        } else if (getOPCode() == OP_STUDENT_REGISTER) {
            return isFirstArg_Str_HasVal() & isSecondArg_Str_HasVal();
        } else if (getOPCode() == OP_LOGIN_REQUEST) {
            return isFirstArg_Str_HasVal() & isSecondArg_Str_HasVal();
        } else if (getOPCode() == OP_LOGOUT_REQUEST) {
            return true;
        } else if (getOPCode() == OP_REGISTER_COURSE) {
            return isFirstArg_Short_HasVal();
        } else if (getOPCode() == OP_CHECK_KDAM) {
            return isFirstArg_Short_HasVal();
        } else if (getOPCode() == OP_A_COURSE_STAT) {
            return isFirstArg_Short_HasVal();
        } else if (getOPCode() == OP_A_STUDENT_STAT) {
            return isFirstArg_Str_HasVal();
        } else if (getOPCode() == OP_CHECK_REGISTERED) {
            return isFirstArg_Short_HasVal();
        } else if (getOPCode() == OP_UNREGISTER_COURSE) {
            return isFirstArg_Short_HasVal();
        } else if (getOPCode() == OP_CHECK_MY_COURSES) {
            return true;
        } else if (getOPCode() == OP_ACK) {
            return true;
        } else if (getOPCode() == OP_ERROR) {
            return true;
        }
        return false;
    }

    public void clear() {
        this.OPCode = 0;
        this.firstArg_Short = 0;
        this.firstArg_Str = "";
        this.secondArg_Str = "";
    }
    public boolean needString(){
        return OPCode == OP_ADMIN_REGISTER | OPCode == OP_STUDENT_REGISTER | OPCode == OP_LOGIN_REQUEST | OPCode == OP_A_STUDENT_STAT;
    }
}
