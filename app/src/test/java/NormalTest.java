import org.junit.Test;

import java.util.HashMap;

/**
 * Created by liueq on 23/2/2016.
 * Normal Test
 */
public class NormalTest {

	@Test
	public void test(){
		HashMap<String, Integer> map = new HashMap<>();
		map.put("1", 1);

		assert map.get("0") == null;
	}
}
