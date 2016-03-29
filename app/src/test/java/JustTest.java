import org.junit.Test;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;

/**
 * Created by liueq on 29/3/2016.
 */
public class JustTest {


    @Test
    public void printSomething(){
        System.out.println("liueq : max int is --> " + Integer.MAX_VALUE);
        System.out.println("liueq : min int is --> " + Integer.MIN_VALUE);

        int max = Integer.MAX_VALUE;
        max = Integer.MAX_VALUE + 1;
        System.out.println("liueq : max is --> " + max);

        assert true;
    }
}
