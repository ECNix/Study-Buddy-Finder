import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTest {
    @Test
    public void testAdd() {
        assertEquals(42, Integer.sum(19, 23));
    }
}
