package com.k1ts.check;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

public class CheckConstants {
    public static final Map<Integer, Map<Integer, Map<Integer, LocalDate>>> YEAR_COURSE_SUBJECT_EXPIRED_DATE_MAP = Map.of(
            2024, Map.of(
                    2, Map.of(
                            1, LocalDate.of(2025, Month.MAY, 25),
                            2, LocalDate.of(2024, Month.NOVEMBER, 25),
                            3, LocalDate.of(2025, Month.MAY, 25)),
                    3, Map.of(1, LocalDate.of(2024, Month.NOVEMBER, 25)),
                    4, Map.of(1, LocalDate.of(2024, Month.NOVEMBER, 25))));
}
