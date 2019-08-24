package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.models.*;
import ru.shaplov.persistence.BrandRepository;
import ru.shaplov.persistence.CarPartsRepository;

import java.util.List;

/**
 * @author shaplov
 * @since 23.07.2019
 */
@Service
@Transactional
public class LogicParts implements ILogicParts {

    private final CarPartsRepository partsRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public LogicParts(CarPartsRepository daoParts, BrandRepository brandRepository) {
        this.partsRepository = daoParts;
        this.brandRepository = brandRepository;
    }

    @Override
    public List<ITitledEntity> getBodyTypes(int modelId) {
        return partsRepository.getBodyTypes(modelId);
    }

    @Override
    public List<ITitledEntity> getEngineTypes(int modelId, int bodyId) {
        return partsRepository.getEngineTypes(modelId, bodyId);
    }

    @Override
    public List<ITitledEntity> getDriveTypes(int modelId, int bodyId, int engineId) {
        return partsRepository.getDriveTypes(modelId, bodyId, engineId);
    }

    @Override
    public List<ITitledEntity> getTransTypes(int modelId, int bodyId, int engineId, int driveId) {
        return partsRepository.getTransTypes(modelId, bodyId, engineId, driveId);
    }

    @Override
    public List<Brand> findAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(int brandId) {
        return brandRepository.findById(brandId).orElse(null);
    }
}
