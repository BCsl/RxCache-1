package com.cv4j.rxcache;

import com.cv4j.rxcache.domain.Record;
import com.cv4j.rxcache.memory.Memory;
import com.cv4j.rxcache.memory.impl.DefaultMemoryImpl;
import com.cv4j.rxcache.persistence.Persistence;
import com.cv4j.rxcache.strategy.*;
import io.reactivex.*;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public class RxCache {

    private final CacheRepository cacheRepository;

    private static RxCache mRxCache;

    public static RxCache getRxCache() {

        if (mRxCache == null) {

            mRxCache = new RxCache.Builder().build();
        }

        return mRxCache;
    }

    public static void config(Builder builder) {

        if (mRxCache == null) {

            RxCache.mRxCache = builder.build();
        }
    }

    private RxCache(Builder builder) {

        Memory memory= builder.memory;
        Persistence persistence = builder.persistence;

        cacheRepository = new CacheRepository(memory, persistence);
    }

    public <T> ObservableTransformer<T, Record<T>> transformObservable(final String key, final Type type, final ObservableStrategy strategy) {
        return new ObservableTransformer<T, Record<T>>() {
            @Override
            public ObservableSource<Record<T>> apply(Observable<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> FlowableTransformer<T, Record<T>> transformFlowable(final String key, final Type type, final FlowableStrategy strategy) {
        return new FlowableTransformer<T, Record<T>>() {
            @Override
            public Publisher<Record<T>> apply(Flowable<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> SingleTransformer<T, Record<T>> transformSingle(final String key, final Type type, final SingleStrategy strategy) {
        return new SingleTransformer<T, Record<T>>() {

            @Override
            public SingleSource<Record<T>> apply(Single<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> CompletableTransformer transformCompletable(final String key, final Type type, final CompletableStrategy strategy) {
        return new CompletableTransformer() {

            @Override
            public CompletableSource apply(Completable upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> MaybeTransformer<T, Record<T>> transformMaybe(final String key, final Type type, final MaybeStrategy strategy) {
        return new MaybeTransformer<T, Record<T>>() {

            @Override
            public MaybeSource<Record<T>> apply(Maybe<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> Observable<Record<T>> get(final String key, final Type type) {

        return Observable.create(new ObservableOnSubscribe<Record<T>>() {
            @Override
            public void subscribe(ObservableEmitter<Record<T>> emitter) throws Exception {

                Record<T> record = cacheRepository.get(key,type);
                if (!emitter.isDisposed()) {
                    if (record != null) {
                        emitter.onNext(record);
                        emitter.onComplete();
                    } else {

                        Observable.empty();
                    }
                }
            }
        });
    }

    public <T> void save(final String key, final T value) {

        cacheRepository.save(key, value);
    }

    public static final class Builder {

        private Memory memory;
        private int maxCacheSize = 100;
        private Persistence persistence;

        public Builder() {
        }

        public Builder memory(Memory memory) {
            this.memory = memory;
            return this;
        }

        public Builder persistence(Persistence persistence) {
            this.persistence = persistence;
            return this;
        }

        public Builder maxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        public RxCache build() {

            if (memory == null) {

                memory = new DefaultMemoryImpl(maxCacheSize);
            }

            return new RxCache(this);
        }
    }
}
