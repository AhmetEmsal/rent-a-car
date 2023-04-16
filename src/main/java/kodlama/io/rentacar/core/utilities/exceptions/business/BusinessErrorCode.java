package kodlama.io.rentacar.core.utilities.exceptions.business;

import kodlama.io.rentacar.core.utilities.exceptions.ExceptionErrorCode;

public enum BusinessErrorCode implements ExceptionErrorCode {
    NotExists(1),
    UnsuitableState(2),
    AlreadyUsed(3),
    Incorrect(4),
    NotEnough(5),

    Unknown(1000);

    private final int errorCode;

    BusinessErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getCode() {
        return errorCode;
    }
}
