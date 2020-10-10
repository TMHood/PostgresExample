package sparrows.ucd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparrows.ucd.entities.PropertyEntity;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
//    Optional<PropertyEntity> findUniqueByName(String name);

//    default boolean exists(String name) {
//        return this.findUniqueByName(name).isPresent();
//    }
}
