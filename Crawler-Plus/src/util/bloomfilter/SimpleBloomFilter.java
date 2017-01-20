package util.bloomfilter;

import com.CrawlUrl;

import java.util.*;

public class SimpleBloomFilter {
    private static final int DEFAULT_SIZE = 2 << 24;
    private static final int[] seeds = new int[]{7, 13, 17, 31, 37, 61};

    private BitSet bits = new BitSet();
    private SimpleHash[] func = new SimpleHash[seeds.length];

    public SimpleBloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }

    public void add(String value) {
        for (SimpleHash f : func) {
            bits.set(f.hash(value), true);
        }
    }

    public void add(CrawlUrl value) {
        if (value != null) {
            add(value.getUrl());
        }
    }

    public boolean contains(CrawlUrl value) {
        return contains(value.getUrl());
    }

    public boolean contains(String value) {
        if (value == null) {
            return false;
        }

        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }
}
