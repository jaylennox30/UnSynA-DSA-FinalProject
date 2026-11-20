
public void setSchedule(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime) {

    if (date == null || startTime == null || endTime == null
            || !endTime.isAfter(startTime)) {
        throw new IllegalArgumentException("Invalid schedule.");
    }

    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
}
