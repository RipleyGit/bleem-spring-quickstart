package site.bleem.boot.hazelcast.jpa;


import site.bleem.boot.hazelcast.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by pengchao on 2018/2/23
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}