package server.database;

import commons.ColorPreset;
import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorPresetRepository extends JpaRepository<ColorPreset, Long> {
    /**
     * This method is used to get all the presets that belong to a specific board using the board's id
     * @param boardId the id using which this method will find all the necessary presets
     * @return the list of presets that the client is requesting
     */
    List<ColorPreset> findByBoardId(Long boardId);
    /**
     * This method is used to get the default preset of a specific board
     * @param isDefault the boolean which is used to get the right preset
     * @param boardId the id using which this method will find the necessary default preset
     * @return the default preset of a specific board
     */
    ColorPreset findByIsDefaultAndBoardId(Boolean isDefault, Long boardId);
}
