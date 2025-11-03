package com.k1ts.email;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class Email {
    private final String from;
    private final String to;
    private final String subject;
    private final String content;
}
