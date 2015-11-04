package jatf.graphdb;

import org.neo4j.graphdb.traversal.TraversalDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.Map;

public class ClassRepository implements GraphRepository<Class<?>> {

    @Override
    public <S extends Class<?>> S save(S clazz) {
        return null;
    }

    @Override
    public <S extends Class<?>> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Class<?> findOne(Long id) {
        return null;
    }

    @Override
    public boolean exists(Long id) {
        return false;
    }

    @Override
    public Result<Class<?>> findAll() {
        return null;
    }

    @Override
    public Iterable<Class<?>> findAll(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(Class<?> clazz) {

    }

    @Override
    public void delete(Iterable<? extends Class<?>> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Result<Class<?>> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Class<?>> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Class getStoredJavaType(Object o) {
        return null;
    }

    @Override
    public Result<Class<?>> query(String s, Map<String, Object> map) {
        return null;
    }

    @Override
    public Class<?> findByPropertyValue(String s, Object o) {
        return null;
    }

    @Override
    public Result<Class<?>> findAllByPropertyValue(String s, Object o) {
        return null;
    }

    @Override
    public Result<Class<?>> findAllByQuery(String s, Object o) {
        return null;
    }

    @Override
    public Result<Class<?>> findAllByRange(String s, Number number, Number number1) {
        return null;
    }

    @Override
    public Class<?> findBySchemaPropertyValue(String s, Object o) {
        return null;
    }

    @Override
    public Result<Class<?>> findAllBySchemaPropertyValue(String s, Object o) {
        return null;
    }

    @Override
    public <N> Iterable<Class<?>> findAllByTraversal(N n, TraversalDescription traversalDescription) {
        return null;
    }
}
