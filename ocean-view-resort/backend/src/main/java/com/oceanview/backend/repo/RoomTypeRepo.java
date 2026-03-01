package com.oceanview.backend.repo;

import com.oceanview.backend.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoomTypeRepo extends JpaRepository<RoomType, Long> {
  Optional<RoomType> findByTypeName(RoomType.RoomTypeName typeName);
}
