package kodlama.io.rentacar.core.utilities.exceptions;

public enum BusinessErrorCodes {
    NotExists(1);

    private final int errorCode;

    BusinessErrorCodes(int errorCode){
        this.errorCode = errorCode;
    }

    public int getCode() {
        return errorCode;
    }
}
