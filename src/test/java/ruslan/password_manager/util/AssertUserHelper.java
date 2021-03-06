package ruslan.password_manager.util;

import org.assertj.core.api.Assertions;

public class AssertUserHelper {

    public static <T> void  isEqual(T expected, T actual) {
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("passwords").
                isEqualTo(expected);
    }
}
