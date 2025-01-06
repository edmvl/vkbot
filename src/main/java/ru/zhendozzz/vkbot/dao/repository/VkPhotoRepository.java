package ru.zhendozzz.vkbot.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zhendozzz.vkbot.dao.entity.VkPhoto;

import java.util.List;

@Repository
public interface VkPhotoRepository extends CrudRepository<VkPhoto, Long> {
    List<VkPhoto> findByGroupId(Long integer);
}
