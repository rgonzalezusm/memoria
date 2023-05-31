package cl.rgonzalez.memoria.core;

public enum RSBlock {
    BLOQUE_01(1, "08:15", "08:50"),
    BLOQUE_02(2, "08:50", "09:25"),
    //
    BLOQUE_03(3, "09:35", "10:10"),
    BLOQUE_04(4, "10:10", "10:45"),
    //
    BLOQUE_05(5, "10:55", "11:30"),
    BLOQUE_06(6, "11:30", "12:05"),
    //
    BLOQUE_07(7, "12:15", "12:50"),
    BLOQUE_08(8, "12:50", "13:25"),
    //
    BLOQUE_09(9, "14:30", "15:05"),
    BLOQUE_10(10, "15:05", "15:40"),
    //
    BLOQUE_11(11, "15:50", "16:25"),
    BLOQUE_12(12, "16:25", "17:00"),
    //
    BLOQUE_13(13, "17:10", "17:45"),
    BLOQUE_14(14, "17:45", "18:20"),
    //
    BLOQUE_15(15, "18:30", "19:05"),
    BLOQUE_16(16, "19:05", "19:40");

    private int value;
    private String beginTime;
    private String endTime;

    RSBlock(int value, String beginTime, String endTime) {
        this.value = value;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public int getValue() {
        return value;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String formatFull() {
        return "[" + value + "] " + beginTime + " - " + endTime;
    }

    public String formatRange() {
        return beginTime + " - " + endTime;
    }
}
