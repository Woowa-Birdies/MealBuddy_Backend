package com.woowa.gather.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class KSTLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 6, true) // 초의 소수 부분을 선택적으로 0에서 6자리까지 허용
            .optionalEnd()
            .optionalStart()
            .appendLiteral('Z') // 'Z' 문자 처리
            .optionalEnd()
            .optionalStart()
            .appendOffset("+HH:MM", "Z") // 시간대 오프셋 처리
            .optionalEnd()
            .toFormatter(Locale.ROOT);
    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText().trim();
        ZonedDateTime utcTime = ZonedDateTime.parse(date, formatter.withZone(ZoneId.of("UTC")));
        return utcTime.withZoneSameInstant(KST_ZONE).toLocalDateTime();
    }
}
