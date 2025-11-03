package com.k1ts.dateprovider;

import java.time.LocalDate;

public class FixedDateProvider implements DateProvider {

    private final LocalDate localDate;

    public FixedDateProvider(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public LocalDate getDate() {
        return localDate;
    }
}
