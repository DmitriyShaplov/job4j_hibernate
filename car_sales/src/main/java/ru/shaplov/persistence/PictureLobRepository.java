package ru.shaplov.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.models.PictureLob;

/**
 * @author shaplov
 * @since 24.08.2019
 */
public interface PictureLobRepository extends JpaRepository<PictureLob, Integer> {
}
