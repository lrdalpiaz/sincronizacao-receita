package br.com.sicredi.sync.tool.balancer.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Disponibiliza estruturas de dados utilizando implementações.
 * Nesse caso, simula uma estrutura que poderia ser compartilhada, como um Hazelcast, Zookeeper.
 * Isso poderia ser substituído por um barramento qualquer como um JMS, Kafka e etc.
 */
public class DefaultDataStructuresProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataStructuresProvider.class);

    private final Map<String, ILock> locks;
    private final Map<String, Set<?>> sets;
    private final Map<String, BlockingQueue<?>> queues;
    private final Map<String, ConcurrentMap<?, ?>> maps;

    public DefaultDataStructuresProvider() {
        this.locks = new ConcurrentHashMap<>();
        this.sets = new HashMap<String, Set<?>>();
        this.queues = new HashMap<String, BlockingQueue<?>>();
        this.maps = new HashMap<String, ConcurrentMap<?, ?>>();
    }

    public ILock lock(String name) {
        synchronized (this) {
            if (!locks.containsKey(name)) {
                locks.put(name, new DefaultLock(new ReentrantLock()));
            }
            return locks.get(name);
        }
    }

    @SuppressWarnings("unchecked")
    public <K, V> ConcurrentMap<K, V> map(String name) {
        return (ConcurrentMap<K, V>) this.getStructure(maps, name, ConcurrentHashMap.class);
    }

    @SuppressWarnings("unchecked")
    public <E> BlockingQueue<E> queue(String name) {
        return (BlockingQueue<E>) this.getStructure(queues, name, LinkedBlockingQueue.class);
    }

    @SuppressWarnings("unchecked")
    public <E> Set<E> set(String name) {
        return (Set<E>) this.getStructure(sets, name, HashSet.class);
    }

    private <T, E extends T> T getStructure(Map<String, T> holder, String name, Class<E> clazz) {
        if (!holder.containsKey(name)) {
            try {
                holder.put(name, clazz.newInstance());
            } catch (InstantiationException e) {
                LOGGER.error("It was not possible to create an instance of type {}", clazz.getName(), e); //$NON-NLS-1$
            } catch (IllegalAccessException e) {
                LOGGER.error("It was not possible to reflectively create an instance of type {}", clazz.getName(), e); //$NON-NLS-1$
            }
        }
        return holder.get(name);
    }

}
