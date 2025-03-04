package cleancode.studycafe.hg.model;

import java.util.Arrays;

import cleancode.studycafe.hg.exception.AppException;

public enum StudyCafePassType {

    HOURLY("시간 단위 이용권", "1"),
    WEEKLY("주 단위 이용권", "2"),
    FIXED("1인 고정석", "3");

    private final String description;
    private final String selectNumber;

    StudyCafePassType(String description, String selectNumber) {
        this.description = description;
        this.selectNumber = selectNumber;
    }

    private boolean isMySelectNumber(String selectNumber) {
        return this.selectNumber.equals(selectNumber);
    }

    public static StudyCafePassType getPassType(String selectNumber) {
        return Arrays.stream(StudyCafePassType.values())
            .filter(passType -> passType.isMySelectNumber(selectNumber))
            .findFirst()
            .orElseThrow(() -> new AppException("잘못된 입력입니다."));
    }

}
