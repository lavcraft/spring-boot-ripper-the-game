package strategy0;

import com.sbr.api.SpiderStrategy;
import com.sbr.api.Thing;

public class BlazinglyFastSpider implements SpiderStrategy {
    public BlazinglyFastSpider() {
        System.out.println("I'm fast and alive");
    }

    @Override
    public Thing gamble() {
        return Thing.PAPER;
    }
}
