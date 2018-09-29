import com.cv4j.rxcache.RxCache;
import com.cv4j.rxcache.domain.Record;
import com.cv4j.rxcache.memory.impl.DefaultMemoryImpl;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by tony on 2018/9/29.
 */
public class Test {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder().memory(new DefaultMemoryImpl(100)));

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u);

        Observable<Record<User>> observable = rxCache.get("test", User.class);

        observable.subscribe(new Consumer<Record<User>>() {
            @Override
            public void accept(Record<User> record) throws Exception {

                User user = record.getData();
                System.out.println(user.name);
                System.out.println(user.password);
            }
        });
    }
}
