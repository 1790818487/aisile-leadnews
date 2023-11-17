import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Mytest {
    public static void main(String[] args) {

        String a="dsalkfjdsl,djfoasj,dsajlkfj";

        List<String> list = Collections.singletonList("sdlkjflsd");
        String s = String.join(",", list);
        System.out.println(s);
    }
}
