package indiv.abko.taskflow.global.dto;

import java.time.ZoneOffset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DtoConstants {
    public static final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DISPLAY_TIME_ZONE_STRING = "UTC";
    // 한국시간입니다
    public static final ZoneOffset REAL_TIME_ZONE_OFFSET = ZoneOffset.ofHours(9);
}
