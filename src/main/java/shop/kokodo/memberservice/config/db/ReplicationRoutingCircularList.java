package shop.kokodo.memberservice.config.db;

import java.util.List;

public class ReplicationRoutingCircularList<T> {
    private List<T> list;
    private static Integer counter = 0;

    public ReplicationRoutingCircularList(List<T> list) {
        this.list = list;
    }

    public T getOne() {
        int circularSize = list.size();
        if (counter + 1 > circularSize) {
            counter = 0;
        }
        return list.get(counter++ % circularSize);
    }

    public List<T> getList() {
        return list;
    }
}