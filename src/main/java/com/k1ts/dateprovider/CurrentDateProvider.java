package com.k1ts.dateprovider;

import java.time.LocalDate;

public class CurrentDateProvider implements DateProvider {
    @Override
    public LocalDate getDate() {
        return LocalDate.now();
    }
}
