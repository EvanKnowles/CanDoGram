package za.co.knonchalant.candogram;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by evan on 2016/03/04.
 */
@Local
public interface IBot extends ShutdownNotify {
    public void start(List<Bots> bots);

    public void shutdown();

    public Bots find(String name);
}
