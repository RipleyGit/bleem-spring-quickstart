package site.bleem.boot.hazelcast.jpa;


import site.bleem.boot.hazelcast.pojo.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by pengchao on 2018/2/23
 */
public interface DocRepository extends JpaRepository<Doc, Long> {
}