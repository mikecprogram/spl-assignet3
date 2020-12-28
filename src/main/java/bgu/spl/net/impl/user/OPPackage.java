package bgu.spl.net.impl.user;

public class OPPackage{
    private int OPCode;
    private String firstArg;
    private String secondArg;

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
